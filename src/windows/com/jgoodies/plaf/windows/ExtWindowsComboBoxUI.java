/*
 * Copyright (c) 2003 JGoodies Karsten Lentzsch. All Rights Reserved.
 *
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 * 
 *  o Redistributions of source code must retain the above copyright notice, 
 *    this list of conditions and the following disclaimer. 
 *     
 *  o Redistributions in binary form must reproduce the above copyright notice, 
 *    this list of conditions and the following disclaimer in the documentation 
 *    and/or other materials provided with the distribution. 
 *     
 *  o Neither the name of JGoodies Karsten Lentzsch nor the names of 
 *    its contributors may be used to endorse or promote products derived 
 *    from this software without specific prior written permission. 
 *     
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, 
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR 
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR 
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, 
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, 
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; 
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, 
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE 
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, 
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. 
 */

package com.jgoodies.plaf.windows;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Rectangle;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicComboBoxUI;

import com.sun.java.swing.plaf.windows.WindowsComboBoxUI;

import com.jgoodies.plaf.LookUtils;

/**
 * The JGoodies Windows Look&amp;Feel implementation of 
 * {@link javax.swing.plaf.ComboBoxUI}.<p>
 * 
 * Corrects the editor insets for editable combo boxes as well as
 * the render insets for non-editable combos.
 * Also, it has the same height as text fields - unless you change the renderer.
 *
 * @author Karsten Lentzsch
 * @version $Revision: 1.9 $
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
     * Creates the editor that is to be used in editable combo boxes. 
     * This method only gets called if a custom editor has not already 
     * been installed in the JComboBox.
     */
    protected ComboBoxEditor createEditor() {
        return new ExtWindowsComboBoxEditor.UIResource();
    }

    /**
     * Creates the default renderer that will be used in a non-editiable combo 
     * box. A default renderer will used only if a renderer has not been 
     * explicitly set with <code>setRenderer</code>.
     * 
     * @return a <code>ListCellRender</code> used for the combo box
     * @see javax.swing.JComboBox#setRenderer
     */
//    protected ListCellRenderer createRenderer() {
//        ListCellRenderer renderer = super.createRenderer();
//        if (renderer instanceof JLabel) {
//             JLabel label = (JLabel) renderer;
//             //label.setBorder(new EmptyBorder(2, 2, 2, 2));
//        }
//        return renderer;
//    }

    /**
     * Creates a layout manager for managing the components which 
     * make up the combo box.<p>
     * 
     * Overriden to use a layout that has a fixed width arrow button.
     * 
     * @return an instance of a layout manager
     */
    protected LayoutManager createLayoutManager() {
        return new ExtWindowsComboBoxLayoutManager();
    }


    /**
     * Creates the arrow button that is to be used in the combo box.<p>
     * 
     * Overridden to paint black triangles.
     */
    protected JButton createArrowButton() {
        return LookUtils.IS_LAF_WINDOWS_XP_ENABLED
                    ? super.createArrowButton()
                    : new ExtWindowsArrowButton(SwingConstants.SOUTH);
    }

    
    /**
     * Returns the area that is reserved for drawing the currently selected item.
     */
    protected Rectangle rectangleForCurrentValue() {
        if (comboBox.isEditable() || !comboBox.isEnabled())
            return super.rectangleForCurrentValue();
        
        int width  = comboBox.getWidth();
        int height = comboBox.getHeight();
        Insets insets = getInsets();
        Insets rendererMargin = UIManager.getInsets("ComboBox.rendererMargin");
        int buttonSize = height - (insets.top + insets.bottom);
        if (arrowButton != null) {
            buttonSize = arrowButton.getWidth();
        }
                if (comboBox.getComponentOrientation().isLeftToRight()) {
            return new Rectangle(
                    insets.left + rendererMargin.left,
                    insets.top + rendererMargin.top,
                    width
                            - (insets.left + rendererMargin.left + insets.right
                                    + rendererMargin.right + buttonSize),
                    height
                            - (insets.top + rendererMargin.top + insets.bottom + rendererMargin.bottom));
        } else {
            return new Rectangle(
                    insets.left + rendererMargin.left + buttonSize,
                    insets.top + rendererMargin.top,
                    width
                            - (insets.left + rendererMargin.left + insets.right
                                    + rendererMargin.right + buttonSize),
                    height
                            - (insets.top + rendererMargin.top + insets.bottom + rendererMargin.bottom));
        }

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