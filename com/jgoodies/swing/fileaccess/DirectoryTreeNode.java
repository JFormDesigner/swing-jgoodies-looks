package com.jgoodies.swing.fileaccess;

/*
 * Copyright (c) 2002 Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of Karsten Lentzsch.  
 * Use is subject to license terms.
 *
 */
 
import java.io.File;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

import com.jgoodies.util.Utilities;

/**
 * A <code>TreeNode</code> implementation that describes nodes in 
 * a tree of directories. It is used in the <code>DirectoryChooser</code> tree.
 *
 * @see	TreeNode
 * @see	DirectoryChooser
 * 
 * @author Karsten Lentzsch
 */

final class DirectoryTreeNode extends DefaultMutableTreeNode {

	/** Have the children been loaded? 			*/
	protected boolean childrenAreLoaded;

	/** Shall I defer the loading of children ? */
	protected boolean deferLoadingChildren;
	
	
	/**
	 * Constructs a <code>DirectoryTreeNode</code> for the given directory.
	 */
	private DirectoryTreeNode(File directory) {
		this(directory, true);
	}
	
	
	/**
	 * Constructs a <code>DirectoryTreeNode</code> for the given directory
	 * either by eagerly or lazily loading the children.
	 */
	private DirectoryTreeNode(File directory, boolean deferLoadingChildren) {
		super(directory);
		childrenAreLoaded = false;
		this.deferLoadingChildren = deferLoadingChildren;
	}
	
	
	/**
	 * Constructs a <code>DirectoryTreeNode</code> for the specified name and children.
	 */
	private DirectoryTreeNode(String name, File[] children) {
		super(name);
		addChildren(children);
		deferLoadingChildren = true;
	}
	
	
	/**
	 * Creates and answers a root node.
	 */
	public static DirectoryTreeNode createRoot() {
		return new DirectoryTreeNode("Filesystem Roots", ExtFileSystemView.getInstance().getRoots());
	}
	
	
	/**
	 * Adds the given children to this node.
	 */
	protected void addChildren(File[] theChildren) {
		childrenAreLoaded = true;

		for (int i = 0; i < theChildren.length; i++)
			add(new DirectoryTreeNode(theChildren[i], true));
	}


	// TreeNode Implementation *********************************************************	
	
	/**
	 * Returns an <code>Enumeration</code> of the node's children.
	 */
	public Enumeration children() {
		ensureChildrenAreLoaded();
		return super.children();
	}
	

	/**
	 * Returns the child node at the specified index.
	 */
	public TreeNode getChildAt(int index) {
		ensureChildrenAreLoaded();
		return super.getChildAt(index);
	}
	
	
	/**
	 * Answers the number of chilren.
	 */
	public int getChildCount() {
		ensureChildrenAreLoaded();
		return super.getChildCount();
	}
	

	/**
	 * Checks and answers if the node is a leaf.
	 */	
	public boolean isLeaf() {
		return !childrenAreLoaded && shouldDeferLoadingChildren() ? false : super.isLeaf();
	}
	
	
	/**
	 * Ensures that the node's children are loaded.
	 */	
	private void ensureChildrenAreLoaded() {
		if (!childrenAreLoaded)
			loadChildren();
	}
	
	
	/**
	 * Answers the directory for this node.
	 */	
	public File getDirectory() {
		return (File) getUserObject();
	}
	

	/**
	 * Checks and answers if the node belongs to a directory in the file system;
	 * note that some shell folders may not correspond to a file.
	 */	
	public boolean isFileSystem() {
		File   dir  = getDirectory();
		String name = dir.getName();
		boolean isFileSystem =  ExtFileSystemView.getInstance().isFileSystem(dir);
		return isFileSystem 
			|| name.equals("Eigene Dateien") // Workaround for 1.4.0 ShellFolder bug 
			|| name.equals("My Documents");  // Workaround for 1.4.0 ShellFolder bug
	}
	
	
	/**
	 * Loads the children if this node - if they have not been loaded before.
	 */	
	public void loadChildren() {
		// Do nothing if children are already loaded.
		if (childrenAreLoaded)
			return;

		File directory = getDirectory();
		childrenAreLoaded = true;
		//

		File[] fileList;
		if (Utilities.IS_BEFORE_14)
			fileList = directory.listFiles();
		else {
			fileList = ExtFileSystemView.getInstance().getFiles(directory, false);
		}
		if (null == fileList)
			return;

		List childDirectories = new LinkedList();
		// Iterate over the directory's content. Collect child directories.
		for (int i = 0; i < fileList.length; i++) {
			File file = fileList[i];
			if (file.isDirectory())
				childDirectories.add(file);
		}

		// Sort the child directories by name;
		Collections.sort(childDirectories);

		// Create and add child nodes.
		Iterator i = childDirectories.iterator();
		while (i.hasNext()) {
			File aDirectory = (File) i.next();
			add(new DirectoryTreeNode(aDirectory));
		}
	}
	

	/**
	 * Answers if children are loaded lazily.
	 */	
	protected boolean shouldDeferLoadingChildren() {
		return deferLoadingChildren;
	}
}