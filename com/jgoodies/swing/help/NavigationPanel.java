package com.jgoodies.swing.help;

/*
 * Copyright (c) 2002 Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of Karsten Lentzsch.  
 * Use is subject to license terms.
 *
 */

import java.awt.Component;
import java.net.URL;
import java.util.Enumeration;

import javax.swing.JTree;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import com.jgoodies.swing.ExtTree;
import com.jgoodies.swing.model.ValueModel;

/**
 * The navigation tree used in the help viewer.<p>
 * 
 * TODO: Use a tree instead of extending it.
 *
 * @author Karsten Lentzsch
 */

final class NavigationPanel extends ExtTree implements ChangeListener {


	NavigationPanel(TreeNode root, URL helpBase) {
		super(root);
		setRootVisible(false);
		setCellRenderer(new HelpTreeCellRenderer());
		getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		expandTopLevel();
	}


	private DefaultMutableTreeNode findNode(URL newURL, DefaultMutableTreeNode node) {
		// Check whether this node is the one we're looking for.
		HelpItem item = (HelpItem) node.getUserObject();
		if ((item != null) && item.matches(newURL))
			return node;

		// Otherwise check the node's children.
		Enumeration enum = node.children();
		while (enum.hasMoreElements()) {
			DefaultMutableTreeNode child = (DefaultMutableTreeNode) enum.nextElement();
			DefaultMutableTreeNode matchingNode = findNode(newURL, child);
			if (matchingNode != null)
				return matchingNode;
		}

		// We haven't found a matching node.
		return null;
	}


	private boolean isSelection(URL newURL) {
		DefaultMutableTreeNode selection = (DefaultMutableTreeNode) getLastSelectedPathComponent();
		if (null == selection)
			return false;
		HelpItem item = (HelpItem) selection.getUserObject();
		return null == item ? false : newURL.equals(item.getURL());
	}


	private void setSelection(URL newURL) {
		if (isSelection(newURL))
			return;

		DefaultMutableTreeNode root = (DefaultMutableTreeNode) getModel().getRoot();
		DefaultMutableTreeNode matchingNode = findNode(newURL, root);
		if (null == matchingNode)
			return;
		TreePath pathToRoot = new TreePath(matchingNode.getPath());
		setSelectionPath(pathToRoot);
		scrollPathToVisible(pathToRoot);
	}


	public void stateChanged(ChangeEvent event) {
		ValueModel model = (ValueModel) event.getSource();
		setSelection((URL) model.getValue());
	}


	public void updateUI() {
		super.updateUI();
		setCellRenderer(new HelpTreeCellRenderer());
	}
	
	
	private static class HelpTreeCellRenderer extends DefaultTreeCellRenderer {
			
		public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean sel, boolean expanded, boolean leaf, int row, boolean focused) {
			super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, focused);
			ExtTree extTree = (ExtTree) tree;
			String stringValue = tree.convertValueToText(value, sel, expanded, leaf, row, focused);
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
			HelpItem item = (HelpItem) node.getUserObject();
			if (null == item)
				return this;
			setText(stringValue);
			selected = item.isChapter() ? false : sel;
			this.setForeground(selected ? getTextSelectionColor() : getTextNonSelectionColor());
			this.setIcon(item.getIcon(expanded));
			this.setFont(item.isChapter() ? extTree.getBoldFont() : extTree.getFont());
			return this;
		}
	}
}