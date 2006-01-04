/*
 * Copyright (c) 2001-2006 JGoodies Karsten Lentzsch. All Rights Reserved.
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

package com.jgoodies.looks.windows;

import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicComboBoxRenderer;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.ComboPopup;

import com.jgoodies.looks.LookUtils;
import com.jgoodies.looks.Options;

/**
 * The JGoodies Windows Look&amp;Feel implementation of 
 * {@link javax.swing.plaf.ComboBoxUI}.
 * Corrects the editor insets for editable combo boxes 
 * as well as the render insets for non-editable combos. And it has 
 * the same height as text fields - unless you change the renderer.
 * Also, it honors an optional popup prototype display value
 * that is used to compute the combo's popup menu width.
 *
 * @author Karsten Lentzsch
 * @version $Revision: 1.6 $
 */
public final class WindowsComboBoxUI extends com.sun.java.swing.plaf.windows.WindowsComboBoxUI {
    
    private static final String CELL_EDITOR_KEY = "JComboBox.isTableCellEditor";
    
    /** 
     * Used to determine the minimum height of a text field, 
     * which in turn is used to answer the combobox's minimum height.
     */
    private static final JTextField PHANTOM = new JTextField("Phantom");
    
    private static final Insets EMPTY_INSETS = new Insets(0, 0, 0, 0);
    
    private static final Border EMPTY_BORDER = new EmptyBorder(EMPTY_INSETS);
    
    
    private boolean tableCellEditor;
    private PropertyChangeListener propertyChangeListener;
    
    
    // ************************************************************************
    
    public static ComponentUI createUI(JComponent b) {
        return new WindowsComboBoxUI();
    }
    
    
    // ************************************************************************
    
    public void installUI( JComponent c ) {
        super.installUI(c);
        tableCellEditor = isTableCellEditor();
    }   
    
    protected void installListeners() {
        super.installListeners();
        propertyChangeListener = new TableCellEditorPropertyChangeHandler();
        comboBox.addPropertyChangeListener(CELL_EDITOR_KEY, propertyChangeListener);
    }
    
    protected void uninstallListeners() {
        super.uninstallListeners();
        comboBox.removePropertyChangeListener(CELL_EDITOR_KEY, propertyChangeListener);
        propertyChangeListener = null;
    }
    
    
    /**
     * The minumum size is the size of the display area plus insets plus the button.
     */
    public Dimension getMinimumSize(JComponent c) {
        if (!isMinimumSizeDirty) {
            return new Dimension(cachedMinimumSize);
        }
        Dimension size = getDisplaySize();
        Insets insets = getInsets();
        size.height += insets.top + insets.bottom;
        int buttonWidth = UIManager.getInt("ScrollBar.width");
        size.width +=  insets.left + insets.right + buttonWidth;
        // The combo editor benefits from extra space for the caret.
        // To make editable and non-editable equally wide, 
        // we always add 1 pixel.
        size.width += 1;
        
        // Honor corrections made in #paintCurrentValue
        ListCellRenderer renderer = comboBox.getRenderer();
        if (renderer instanceof JComponent) {
            JComponent component = (JComponent) renderer;
            Insets rendererInsets = component.getInsets();
            Insets editorInsets = UIManager.getInsets("ComboBox.editorInsets");
            int offsetLeft   = Math.max(0, editorInsets.left - rendererInsets.left);
            int offsetRight  = Math.max(0, editorInsets.right - rendererInsets.right);
            // int offsetTop    = Math.max(0, editorInsets.top - rendererInsets.top);
            // int offsetBottom = Math.max(0, editorInsets.bottom - rendererInsets.bottom);
            size.width += offsetLeft + offsetRight;
            //size.height += offsetTop + offsetBottom;
        }
        
        // The height is oriented on the JTextField height
        Dimension textFieldSize = PHANTOM.getMinimumSize();
        int height = (LookUtils.IS_OS_WINDOWS_VISTA && !LookUtils.IS_LAF_WINDOWS_XP_ENABLED) 
           ? textFieldSize.height
           : Math.max(textFieldSize.height, size.height);

        cachedMinimumSize.setSize(size.width, height); 
        isMinimumSizeDirty = false;

        return new Dimension(size);
    }

    /**
     * Delegates to #getMinimumSize(Component).
     * Overridden to return the same result in JDK 1.5 as in JDK 1.4.
     */
    public Dimension getPreferredSize(JComponent c) {
        return getMinimumSize(c);
    }

    /**
     * Creates the editor that is to be used in editable combo boxes. 
     * This method only gets called if a custom editor has not already 
     * been installed in the JComboBox.
     */
    protected ComboBoxEditor createEditor() {
        return new com.jgoodies.looks.windows.WindowsComboBoxEditor.UIResource(tableCellEditor);
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
        return new WindowsComboBoxLayoutManager();
    }


    /**
     * Creates a ComboPopup that honors the optional combo popup display value
     * that is used to compute the popup menu width. 
     */
    protected ComboPopup createPopup() {
        return new WindowsComboPopup(comboBox);
    }
    
    
    /**
     * Creates the default renderer that will be used in a non-editiable combo 
     * box. A default renderer will used only if a renderer has not been 
     * explicitly set with <code>setRenderer</code>.<p>
     * 
     * This method differs from the superclass implementation in that 
     * it uses an empty border with the default left and right text insets,
     * the same as used by a combo box editor. 
     * 
     * @return a <code>ListCellRender</code> used for the combo box
     * @see javax.swing.JComboBox#setRenderer
     */
    protected ListCellRenderer createRenderer() {
        if (tableCellEditor) {
            return super.createRenderer();
        }
        BasicComboBoxRenderer renderer = new BasicComboBoxRenderer.UIResource();
        renderer.setBorder(UIManager.getBorder("ComboBox.rendererBorder"));
        return renderer;
    }


    /**
     * Creates the arrow button that is to be used in the combo box.<p>
     * 
     * Overridden to paint black triangles.
     */
    protected JButton createArrowButton() {
        return LookUtils.IS_LAF_WINDOWS_XP_ENABLED
                    ? super.createArrowButton()
                    : new WindowsArrowButton(SwingConstants.SOUTH);
    }
    
    
    /**
     * Returns the area that is reserved for drawing the currently selected item.
     */
    protected Rectangle rectangleForCurrentValue() {
        int width  = comboBox.getWidth();
        int height = comboBox.getHeight();
        Insets insets = getInsets();
        int buttonWidth = UIManager.getInt("ScrollBar.width");
        if (arrowButton != null) {
            buttonWidth = arrowButton.getWidth();
        }
        if (comboBox.getComponentOrientation().isLeftToRight()) {
            return new Rectangle(
                    insets.left,
                    insets.top,
                    width  - (insets.left + insets.right + buttonWidth),
                    height - (insets.top  + insets.bottom));
        } else {
            return new Rectangle(
                    insets.left + buttonWidth,
                    insets.top ,
                    width  - (insets.left + insets.right + buttonWidth),
                    height - (insets.top  + insets.bottom));
        }
    }
    

    /**
     * Paints the currently selected item.
     */
    public void paintCurrentValue(Graphics g,Rectangle bounds,boolean hasFocus) {
        ListCellRenderer renderer = comboBox.getRenderer();
        Component c;

        if ( hasFocus && !isPopupVisible(comboBox) ) {
            c = renderer.getListCellRendererComponent( listBox,
                                                       comboBox.getSelectedItem(),
                                                       -1,
                                                       true,
                                                       false );
        }
        else {
            c = renderer.getListCellRendererComponent( listBox,
                                                       comboBox.getSelectedItem(),
                                                       -1,
                                                       false,
                                                       false );
            c.setBackground(UIManager.getColor("ComboBox.background"));
        }
        Border oldBorder = null;
        if ((c instanceof JComponent) && !tableCellEditor) {
            JComponent component = (JComponent) c;
            if (c instanceof BasicComboBoxRenderer.UIResource) {
                oldBorder = component.getBorder();
                component.setBorder(EMPTY_BORDER);
            }
            Insets rendererInsets = component.getInsets();
            Insets editorInsets = UIManager.getInsets("ComboBox.editorInsets");
            int offsetLeft   = Math.max(0, editorInsets.left - rendererInsets.left);
            int offsetRight  = Math.max(0, editorInsets.right - rendererInsets.right);
            int offsetTop    = Math.max(0, editorInsets.top - rendererInsets.top);
            int offsetBottom = Math.max(0, editorInsets.bottom - rendererInsets.bottom);
            bounds.x += offsetLeft;
            bounds.y += offsetTop;
            bounds.width  -= offsetLeft + offsetRight - 1;
            bounds.height -= offsetTop + offsetBottom;
        }
        
        c.setFont(comboBox.getFont());
        if ( hasFocus && !isPopupVisible(comboBox) ) {
            c.setForeground(listBox.getSelectionForeground());
            c.setBackground(listBox.getSelectionBackground());
        }
        else {
            if ( comboBox.isEnabled() ) {
                c.setForeground(comboBox.getForeground());
                c.setBackground(comboBox.getBackground());
            }
            else {
                c.setForeground(UIManager.getColor("ComboBox.disabledForeground"));
                c.setBackground(UIManager.getColor("ComboBox.disabledBackground"));
            }
        }

        // Fix for 4238829: should lay out the JPanel.
        boolean shouldValidate = false;
        if (c instanceof JPanel)  {
            shouldValidate = true;
        }

        currentValuePane.paintComponent(g,c,comboBox,bounds.x,bounds.y,
                                        bounds.width,bounds.height, shouldValidate);
        if (oldBorder != null) {
            ((JComponent) c).setBorder(oldBorder);
        }
    }
    
    
    // Helper Code ************************************************************
    
    /**
     * Checks and answers if this UI's combo has a client property
     * that indicates that the combo is used as a table cell editor.
     * 
     * @return <code>true</code> if the table cell editor client property
     *    is set to <code>Boolean.TRUE</code>, <code>false</code> otherwise
     */
    private boolean isTableCellEditor() {
        return Boolean.TRUE.equals(comboBox.getClientProperty(CELL_EDITOR_KEY));
    }
    

    // Collaborator Classes ***************************************************

    /**
     * This layout manager handles the 'standard' layout of combo boxes.  
     * It puts the arrow button to the right and the editor to the left.
     * If there is no editor it still keeps the arrow button to the right.
     * 
     * Overriden to use a fixed arrow button width. 
     */
    private final class WindowsComboBoxLayoutManager extends BasicComboBoxUI.ComboBoxLayoutManager {
        
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
    
    
    /**
     * Differs from the BasicComboPopup in that it uses the standard 
     * popmenu border and honors an optional popup prototype display value.
     */
    private static final class WindowsComboPopup extends BasicComboPopup {

        private WindowsComboPopup(JComboBox combo) {
            super(combo);
        }

        /**
         * Calculates the placement and size of the popup portion 
         * of the combo box based on the combo box location and 
         * the enclosing screen bounds. If no transformations are required,
         * then the returned rectangle will have the same values 
         * as the parameters.<p>
         * 
         * In addition to the superclass behavior, this class uses the combo's 
         * popup prototype display value to compute the popup menu width. 
         * This is an optional feature of the JGoodies Plastic L&amp;f
         * implemented via a client property key.
         * 
         * @param px starting x location
         * @param py starting y location
         * @param pw starting width
         * @param ph starting height
         * @return a rectangle which represents the placement and size of the popup
         * 
         * @see Options#COMBO_POPUP_PROTOTYPE_DISPLAY_VALUE_KEY
         */
        protected Rectangle computePopupBounds(int px, int py, int pw, int ph) {
            Object popupPrototypeDisplayValue = comboBox.getClientProperty(
                    Options.COMBO_POPUP_PROTOTYPE_DISPLAY_VALUE_KEY);
            if (popupPrototypeDisplayValue != null) {
                ListCellRenderer renderer = list.getCellRenderer();
                Component c = renderer.getListCellRendererComponent(list, popupPrototypeDisplayValue,
                        -1, true, true);
                pw = c.getPreferredSize().width;
            }
            return super.computePopupBounds(px, py, pw, ph); 
        }

    }    
    
    
    // Handling Combo Changes *************************************************
    
    /**
     * Listens to changes in the table cell editor client property
     * and updates the default editor - if any - to use the correct
     * insets for this case.
     */
    private final class TableCellEditorPropertyChangeHandler implements PropertyChangeListener {
        public void propertyChange(PropertyChangeEvent evt) {
            tableCellEditor = isTableCellEditor();
            if (comboBox.getRenderer() == null || comboBox.getRenderer() instanceof UIResource) {
                comboBox.setRenderer(createRenderer());
            }
            if (comboBox.getEditor() == null || comboBox.getEditor() instanceof UIResource) {
                comboBox.setEditor(createEditor());
            }
        }
    }
    
    
}