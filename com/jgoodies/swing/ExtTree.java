package com.jgoodies.swing;

/*
 * Copyright (c) 2002 Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of Karsten Lentzsch.  
 * Use is subject to license terms.
 *
 */

import java.awt.Font;
import java.util.Enumeration;

import javax.swing.JTree;
import javax.swing.event.EventListenerList;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

/**
 * A <code>JTree</code> subclass that adds support for 
 * a bold font and some convenience tree actions.
 *
 * @author Karsten Lentzsch
 * @see	JTree
 */
public class ExtTree extends JTree {

    private Font boldFont;

    /**
     * Constructs an <code>ExtTree</code> on a sample <code>TreeModel</code>.
     */
    public ExtTree() {
        super();
    }

    /**
     * Constructs an <code>ExtTree</code> for the given <code>TreeModel</code>.
     */
    public ExtTree(TreeModel model) {
        super(model);
    }

    /**
     * Constructs an <code>ExtTree</code> for the given <code>TreeNode</code>.
     */
    public ExtTree(TreeNode root) {
        super(root);
    }

    // Creating and Accessing the Bold Font *********************************

    /**
     * Answers the bold font.
     */
    public Font getBoldFont() {
        return boldFont;
    }

    /**
     * Updates the UI and resets the bold font.
     */
    public void updateUI() {
        TreePath path = getSelectionPath();
        super.updateUI();
        setSelectionPath(path);
        boldFont = getFont().deriveFont(Font.BOLD);
    }

    // Convenience Tree View Behavior ***************************************

    /**
     * Collapses all siblings of the specified <code>TreePath</code>.
     */
    public void collapseAllSiblings(TreePath path) {
        TreeNode node = (TreeNode) path.getLastPathComponent();
        collapseChildren(path);
        path = path.getParentPath();
        while (path != null) {
            TreeNode parent = (TreeNode) path.getLastPathComponent();

            Enumeration siblings = parent.children();
            while (siblings.hasMoreElements()) {
                TreeNode aSibling = (TreeNode) siblings.nextElement();
                if (aSibling != node) {
                    TreePath siblingPath = path.pathByAddingChild(aSibling);
                    collapsePath(siblingPath);
                }
            }
            node = parent;
            path = path.getParentPath();
        }
    }

    /**
     * Collapses the children of the specified <code>TreePath</code>.
     */
    public void collapseChildren(TreePath path) {
        TreeNode parent = (TreeNode) path.getLastPathComponent();
        Enumeration enum = parent.children();
        while (enum.hasMoreElements()) {
            TreeNode child = (TreeNode) enum.nextElement();
            TreePath childPath = path.pathByAddingChild(child);
            collapsePath(childPath);
        }
        collapsePath(path);
    }

    /**
     * Expands the top level rows.
     */
    public void expandTopLevel() {
        for (int row = getRowCount(); row >= 0; row--)
            expandRow(row);
    }

    /**
     * Sets the specified <code>TreePath</code> as selection path
     * without firing selection events.
     */
    public void setSelectionPathWithoutFiring(TreePath path) {
        EventListenerList storedListenerList = listenerList;
        listenerList = new EventListenerList();
        setSelectionPath(path);
        listenerList = storedListenerList;
    }

}