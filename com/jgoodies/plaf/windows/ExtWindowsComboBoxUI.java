package com.jgoodies.plaf.windows;

/*
 * Copyright (c) 2002 Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of Karsten Lentzsch.  
 * Use is subject to license terms.
 *
 */

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicArrowButton;
import javax.swing.plaf.basic.BasicComboBoxUI;

import com.jgoodies.plaf.LookUtils;
import com.sun.java.swing.plaf.windows.WindowsComboBoxUI;

/**
 * The JGoodies Windows Look and Feel implementation of
 * <code>ComboBoxUI</code>.<p>
 * 
 * Has the same height as text fields - unless you change the renderer.
 *
 * @author Karsten Lentzsch
 */

public final class ExtWindowsComboBoxUI extends WindowsComboBoxUI {
	
	/* 
	 * Used to determine the minimum height of a text field, 
	 * which in turn is used to answer the combobox's minimum height.
	 */
	private static final JTextField phantom = new JTextField("Phantom");
	

	public static ComponentUI createUI(JComponent b) {
		return new ExtWindowsComboBoxUI();
	}
	
	
	/**
     * The minumum size is the size of the display area plus insets plus the button.
     */
    public Dimension getMinimumSize(JComponent c) {
    	Dimension size = super.getMinimumSize(c);
    	Dimension textFieldSize = phantom.getMinimumSize();
    	return new Dimension(size.width, Math.max(textFieldSize.height, size.height));
    }


    /**
     * Creates a layout manager for managing the components which 
     * make up the combo box.<p>
     * 
     * Overriden to use a layout that has a fixed width arrow button.
     * 
     * @return an instance of a layout manager
     */
    protected LayoutManager createLayoutManager() {
        return LookUtils.HAS_XP_LAF
                    ? super.createLayoutManager()
                    : new ExtWindowsComboBoxLayoutManager();
    }


	/**
	 * Creates the arrow button that is to be used in the combo box.<p>
	 * 
	 * Overridden to paint black triangles.
	 */
    protected JButton createArrowButton() {
        return LookUtils.HAS_XP_LAF
                    ? super.createArrowButton()
                    : new ExtWindowsArrowButton(BasicArrowButton.SOUTH);
    }

	
    /**
     * This layout manager handles the 'standard' layout of combo boxes.  
     * It puts the arrow button to the right and the editor to the left.
     * If there is no editor it still keeps the arrow button to the right.
     * 
     * Overriden to use a fixed arrow button width. 
     */
    private class ExtWindowsComboBoxLayoutManager extends BasicComboBoxUI.ComboBoxLayoutManager {
    	
		public void layoutContainer(Container parent) {
			JComboBox cb = (JComboBox) parent;
			int width  = cb.getWidth();
			int height = cb.getHeight();

			Insets insets = getInsets();
			int buttonWidth  = UIManager.getInt("ScrollBar.width");
			int buttonHeight = height - (insets.top + insets.bottom);

			if (arrowButton != null) {
				if (cb.getComponentOrientation().isLeftToRight()) {
					arrowButton.setBounds(width - (insets.right + buttonWidth),
						insets.top, buttonWidth, buttonHeight);
				} else {
					arrowButton.setBounds(insets.left, insets.top, buttonWidth, buttonHeight);
				}
			}
			if (editor != null) {
				editor.setBounds(rectangleForCurrentValue());
			}
		}
    
   }
	
}