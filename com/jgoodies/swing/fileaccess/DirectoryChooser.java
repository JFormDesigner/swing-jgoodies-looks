package com.jgoodies.swing.fileaccess;

/*
 * Copyright (c) 2002 Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of Karsten Lentzsch.  
 * Use is subject to license terms.
 *
 */
 
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreeSelectionModel;

import com.jgoodies.layout.Resizer;
import com.jgoodies.swing.AbstractDialog;
import com.jgoodies.swing.AbstractFrame;
import com.jgoodies.swing.ExtTree;
import com.jgoodies.swing.application.ResourceIDs;
import com.jgoodies.swing.application.ResourceManager;
import com.jgoodies.swing.panels.HeaderPanel;
import com.jgoodies.util.logging.Logger;
import com.jgoodies.lazy.Preparable;

/**
 * A dialog for choosing a directory. All access to the file system 
 * is delegated to an <code>ExtFileSystemView</code>.
 *
 * @see	ExtFileSystemView
 * 
 * @author Karsten Lentzsch
 */

public final class DirectoryChooser extends AbstractDialog {
	
	private boolean canSelectLeafDirectory;
	private File	selection = null;
	private JTree	directoryTree;


	/**
	 * Constructs a directory chooser for the given owner frame.
	 */
	private DirectoryChooser(AbstractFrame owner) {
		super(owner);
		canSelectLeafDirectory = false;
	}


	/**
	 * Lets the user choose and directory, which is then returned.
	 * Opens a directory chooser dialog using the owner frame.
	 */
	public static File chooseDirectory(AbstractFrame owner) {
		return chooseDirectory(owner, false);
	}


	/**
	 * Lets the user choose and directory, which is then returned.
	 * Opens a directory chooser dialog using the owner frame
	 * and the given select-leaf-directory property.
	 */
	public static File chooseDirectory(AbstractFrame owner, boolean canSelectLeafDirectory) {
		DirectoryChooser chooser = new DirectoryChooser(owner);
		chooser.setCanSelectLeafDirectory(canSelectLeafDirectory);
		chooser.open();
		return chooser.getSelectedDirectory();
	}


	// Building *************************************************************************

	/**
	 * Builds and answers the dialog's header panel.
	 */
	protected JComponent buildHeader() {
	    return new HeaderPanel(
	   		"Choose Directory",
	   		"Select a directory from the list below, then click \"OK\"."
			+ "\nYou can expand directories by clicking on the anchors.",
			ResourceManager.getIcon(ResourceIDs.CHOOSE_DIRECTORY_ICON)
			);
	}


	/**
	 * Builds and answers the dialog's content panel.
	 */
	protected JComponent buildContent() {
		JPanel content = new JPanel(new BorderLayout());
		content.add(createDirectoryTreePane(), BorderLayout.CENTER);
		content.add(buildButtonBarWithOKCancel(), BorderLayout.SOUTH);
		okButton.setEnabled(false);

		content.setPreferredSize(new Dimension(200, 310));
		return content;
	}


	/**
	 * Uses the default <code>Resizer</code> to give the dialog an aesthetic 
	 * aspect ratio.
	 */
	protected void resizeHook(JComponent component) {
		Resizer.DEFAULT.resize(component);
	}


	/**
	 * Creates, configures, and answers the directory tree pane.
	 */
	private JComponent createDirectoryTreePane() {
		TreeNode root = DirectoryTreeNode.createRoot();
		directoryTree = new ExtTree(root);
		directoryTree.setRootVisible(false);
		directoryTree.setShowsRootHandles(true);
		directoryTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		if (root.getChildCount() == 1)
			directoryTree.expandRow(0);

		// Set a JFileChooser-like Renderer
		directoryTree.setCellRenderer(new DirectoryTreeRenderer());

		// Listen to selection changes.
		directoryTree.addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent e) {
				updateOKButtonEnablement((DirectoryTreeNode) e.getPath().getLastPathComponent());
			}
		});

		// Listen to double click; doOk iff okButton is enabled.
		directoryTree.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if ((e.getClickCount() == 2) && okButton.isEnabled())
					doAccept();
			}
		});

		// Load DirectoryTreeNode's children before the tree will expand.
		directoryTree.addTreeWillExpandListener(new TreeWillExpandListener() {
			public void treeWillExpand(TreeExpansionEvent e) {
				DirectoryTreeNode node = (DirectoryTreeNode) e.getPath().getLastPathComponent();
				try {
					setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
					node.loadChildren();
				} finally {
					setCursor(Cursor.getDefaultCursor());
				}
			}
			public void treeWillCollapse(TreeExpansionEvent e) {}
		});

		//
		return new JScrollPane(directoryTree);
	}


	// Actions **************************************************************************
	
	/**
	 * Closes the dialog.
	 */
	public void close() {
		setVisible(false);
	}


	/**
	 * Cancels the choice and closes the dialog.
	 */
	public void doCancel() {
		selection = null;
		super.doCancel();
	}


	// Misc *****************************************************************************

	/**
	 * Answers the selected directory.
	 */	
	private File getSelectedDirectory() {
		return selection;
	}


	/**
	 * Enables or disabled the selection of leaf directories.
	 */	
	public void setCanSelectLeafDirectory(boolean canSelectLeafDirectory) {
		this.canSelectLeafDirectory = canSelectLeafDirectory;
	}


	/**
	 * Updates the enablement of the OK button.
	 */	
	private void updateOKButtonEnablement(DirectoryTreeNode selectedNode) {
		selection = selectedNode.getDirectory();
		boolean hasChildren = canSelectLeafDirectory || (selectedNode.getChildCount() > 0);
		okButton.setEnabled(hasChildren && selectedNode.isFileSystem());
	}
	
	
	// Helper Class *********************************************************************
	
	// Helper class that renders directory tree nodes.
	private static class DirectoryTreeRenderer extends DefaultTreeCellRenderer {
		
		public Component getTreeCellRendererComponent(
					JTree tree, Object value,
					boolean isSelected, boolean expanded, boolean leaf,
					int row, boolean focused) {
			super.getTreeCellRendererComponent(tree, value, isSelected, expanded, false, row, focused);
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
			Object userObject = node.getUserObject();
			if ((null == userObject) || !(userObject instanceof File))
				return this;

			File directory = (File) userObject;
			ExtFileSystemView v = ExtFileSystemView.getInstance();
			setText(v.getDirectoryName(directory));
			setIcon(v.getIcon(directory, isSelected));
			return this;
		}
	}


	// Preparing ************************************************************************
	
	/**
	 * An implementation of the <code>Preparable</code> interface 
	 * that instantiates and prebuilds a <code>DirectoryChooser</code>.
	 * This will in turn load the many classes required by the
	 * <code>ExtFileSystemView</code>.
	 * <p>
	 * This will significantly speed up the use of the <code>DirectoryChooser</code>.
	 */
	public static class EagerBuilder implements Preparable {
		
		/**
		 * Instantiates and prebuilds a <code>DirectoryChooser</code>.
		 */
		public void prepare() { 
			Logger.getLogger("DirectoryChooser").info("Preparing the directory chooser...");
			new DirectoryChooser(null).build(); 
		}
	}
}