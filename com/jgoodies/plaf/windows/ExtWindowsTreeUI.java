/*
 * Copyright (c) 2003 JGoodies Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of JGoodies Karsten Lentzsch.  
 * Use is subject to license terms.
 *
 */

package com.jgoodies.plaf.windows;

import java.awt.Component;
import java.awt.Graphics;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;

import com.sun.java.swing.plaf.windows.WindowsTreeUI;

import com.jgoodies.plaf.Options;

/**
 * The JGoodies Windows Look&amp;Feel implementation of <code>TreeUI</code>.
 * Corrects the position of the tree button icon.
 * <p>
 * It provides two line styles: angled dashed lines, or no lines at all.
 * By default, lines are drawn. 
 * <p>
 * You can change the line style by setting a client property.
 * The property key and values are a subset of the values used
 * by the Metal L&amp;F tree. To hide lines use one of the following:
 * <pre>
 * JTree tree1 = new JTree();
 * tree1.putClientProperty("JTree.lineStyle", "None");
 * 
 * JTree tree2 = new JTree();
 * tree1.putClientProperty(Options.TREE_LINE_STYLE_KEY, 
 *                         Options.TREE_LINE_STYLE_NONE_VALUE);
 * </pre>
 * 
 * Although lines are shown by default, you could code:
 * <pre>
 * JTree tree1 = new JTree();
 * tree1.putClientProperty("JTree.lineStyle", "Angled");
 * 
 * JTree tree2 = new JTree();
 * tree1.putClientProperty(Options.TREE_LINE_STYLE_KEY, 
 *                         Options.TREE_LINE_STYLE_ANGLED_VALUE);
 * </pre>
 * 
 * @author Karsten Lentzsch
 */

public final class ExtWindowsTreeUI extends WindowsTreeUI {
	
    private boolean linesEnabled = true;
    private PropertyChangeListener lineStyleHandler;

	public static ComponentUI createUI(JComponent b) {
		return new ExtWindowsTreeUI();
	}
    

    // Installation ***********************************************************

    public void installUI(JComponent c) {
        super.installUI(c);
        updateLineStyle(c.getClientProperty(Options.TREE_LINE_STYLE_KEY));
        lineStyleHandler = new LineStyleHandler();
        c.addPropertyChangeListener(lineStyleHandler);
    }

    public void uninstallUI(JComponent c) {
        c.removePropertyChangeListener(lineStyleHandler);
        super.uninstallUI(c);
    }
    
    
    // Painting ***************************************************************

    protected void paintVerticalLine(Graphics g, JComponent c, int x, int top, int bottom) {
        if (linesEnabled) {
            super.paintVerticalLine(g, c, x, top, bottom);
        }
    }

    protected void paintHorizontalLine(Graphics g, JComponent c, int y, int left, int right) {
        if (linesEnabled) {
            super.paintHorizontalLine(g, c, y, left, right);
        }
    }

    // Draws the icon centered at (x,y)
    protected void drawCentered(Component c, Graphics graphics, Icon icon, int x, int y) {
		icon.paintIcon(c, graphics, 
				x - icon.getIconWidth()  / 2 - 1, 
				y - icon.getIconHeight() / 2);
    }
    

    // Helper Code ************************************************************

    private void updateLineStyle(Object lineStyle) {
        linesEnabled = !Options.TREE_LINE_STYLE_NONE_VALUE.equals(lineStyle);
    }
    
    // Listens for changes of the line style property 
    private class LineStyleHandler implements PropertyChangeListener {
        public void propertyChange(PropertyChangeEvent e) {
            String name  = e.getPropertyName();
            Object value = e.getNewValue();
            if (name.equals(Options.TREE_LINE_STYLE_KEY)) {
                updateLineStyle(value);
            }
        }
    }
    
}