/*
 * Copyright (c) 2001-2004 JGoodies Karsten Lentzsch. All Rights Reserved.
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

package com.jgoodies.looks.plastic;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.*;

/**
 * The default button for combo boxes in the JGoodies Plastic Look&amp;Feel.
 * <p>
 * It differs from <code>MetalComboBoxButton</code> in that the border
 * is quite the same as for text fields: a compound border with an inner
 * <code>MarginBorder</code>.
 * <p>
 * Also, we try to switch the <code>ListCellRenderer</code> to transparent,
 * which works for most <code>JComponent</code> renderes including the
 * <code>BasicComboBoxRenderer</code>.
 *
 * @author  Karsten Lentzsch
 * @version $Revision: 1.2 $
 */
final class PlasticComboBoxButton extends JButton {

    private static final int LEFT_INSET  = 2;
    private static final int RIGHT_INSET = 3;

    private final JList listBox;
    private final CellRendererPane rendererPane;

    private   JComboBox  comboBox;
    private   Icon       comboIcon;
    protected boolean   iconOnly = false;
    private   boolean   borderPaintsFocus;

    /**
     * Constructs a <code>PlasticComboBoxButton</code>.
     */
    PlasticComboBoxButton(
        JComboBox comboBox,
        Icon comboIcon,
        boolean iconOnly,
        CellRendererPane rendererPane,
        JList listBox) {
        super("");
        setModel(new DefaultButtonModel() {
            public void setArmed(boolean armed) {
                super.setArmed(isPressed() || armed);
            }
        });
        this.comboBox  = comboBox;
        this.comboIcon = comboIcon;
        this.iconOnly  = iconOnly;
        this.rendererPane = rendererPane;
        this.listBox = listBox;
        setEnabled(comboBox.isEnabled());
        setFocusable(false);
        setRequestFocusEnabled(comboBox.isEnabled());
        setBorder(UIManager.getBorder("ComboBox.arrowButtonBorder"));
        setMargin(new Insets(0, LEFT_INSET, 0, RIGHT_INSET));
        borderPaintsFocus = UIManager.getBoolean("ComboBox.borderPaintsFocus");
    }

    public JComboBox getComboBox() {
        return comboBox;
    }
    
    public void setComboBox(JComboBox cb) {
        comboBox = cb;
    }

    public Icon getComboIcon() {
        return comboIcon;
    }
    
    public void setComboIcon(Icon i) {
        comboIcon = i;
    }

    public boolean isIconOnly() {
        return iconOnly;
    }
    
    public void setIconOnly(boolean b) {
        iconOnly = b;
    }

    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        // Set the background and foreground to the combobox colors.
        if (enabled) {
            setBackground(comboBox.getBackground());
            setForeground(comboBox.getForeground());
        } else {
            setBackground(UIManager.getColor("ComboBox.disabledBackground"));
            setForeground(UIManager.getColor("ComboBox.disabledForeground"));
        }
    }

    /**
     * Checks and answers if we should paint a pseudo 3D effect.
     */
    private boolean is3D() {
        if (PlasticUtils.force3D(comboBox))
            return true;
        if (PlasticUtils.forceFlat(comboBox))
            return false;
        return PlasticUtils.is3D("ComboBox.");
    }

    /**
     * Paints the component; honors the 3D settings and 
     * tries to switch the renderer component to transparent.
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        boolean leftToRight = PlasticUtils.isLeftToRight(comboBox);

        Insets insets = getInsets();

        int width  = getWidth()  - (insets.left + insets.right);
        int height = getHeight() - (insets.top  + insets.bottom);

        if (height <= 0 || width <= 0) {
            return;
        }

        int left   = insets.left;
        int top    = insets.top;
        int right  = left + (width - 1);

        int iconWidth = 0;
        int iconLeft = (leftToRight) ? right : left;

        // Paint the icon
        if (comboIcon != null) {
            iconWidth = comboIcon.getIconWidth();
            int iconHeight = comboIcon.getIconHeight();
            int iconTop;

            if (iconOnly) {
                iconLeft = (getWidth()  - iconWidth)  / 2;
                iconTop  = (getHeight() - iconHeight) / 2;
            } else {
                if (leftToRight) {
                    iconLeft = (left + (width - 1)) - iconWidth;
                } else {
                    iconLeft = left;
                }
                iconTop = (getHeight() - iconHeight) / 2;
            }

            comboIcon.paintIcon(this, g, iconLeft, iconTop);

        }

        // Let the renderer paint
        if (!iconOnly && comboBox != null) {
            ListCellRenderer renderer = comboBox.getRenderer();
            boolean renderPressed = getModel().isPressed();
            Component c =
                renderer.getListCellRendererComponent(
                    listBox,
                    comboBox.getSelectedItem(),
                    -1,
                    renderPressed,
                    false);
            c.setFont(rendererPane.getFont());

            if (model.isArmed() && model.isPressed()) {
                if (isOpaque()) {
                    c.setBackground(UIManager.getColor("Button.select"));
                }
                c.setForeground(comboBox.getForeground());
            } else if (!comboBox.isEnabled()) {
                if (isOpaque()) {
                    c.setBackground(
                        UIManager.getColor("ComboBox.disabledBackground"));
                }
                c.setForeground(
                    UIManager.getColor("ComboBox.disabledForeground"));
            } else {
                c.setForeground(comboBox.getForeground());
                c.setBackground(comboBox.getBackground());
            }

            int cWidth = width - (insets.right + iconWidth);

            // Fix for 4238829: should lay out the JPanel.
            boolean shouldValidate = c instanceof JPanel;
            int x = leftToRight ? left : left + iconWidth;
            int myHeight = getHeight() - LEFT_INSET - RIGHT_INSET - 1;

            if (!is3D()) {
                rendererPane.paintComponent(
                    g,
                    c,
                    this,
                    x,
                    top + 2,
                    cWidth,
                    myHeight,
                    shouldValidate);
            } else if (!(c instanceof JComponent)) {
                rendererPane.paintComponent(
                    g,
                    c,
                    this,
                    x,
                    top + 2,
                    cWidth,
                    myHeight,
                    shouldValidate);
                //LookUtils.log("Custom renderer detected: " + c);				
                //LookUtils.log("Custom renderer superclass: " + c.getClass().getSuperclass().getName());				
            } else if (!c.isOpaque()) {
                rendererPane.paintComponent(
                    g,
                    c,
                    this,
                    x,
                    top + 2,
                    cWidth,
                    myHeight,
                    shouldValidate);
            } else {
                // In case, we are in 3D mode _and_ have a non-transparent
                // JComponent renderer, store the opaque state, set it
                // to transparent, paint, then restore.
                JComponent component = (JComponent) c;
                boolean hasBeenOpaque = component.isOpaque();
                component.setOpaque(false);
                rendererPane.paintComponent(
                    g,
                    c,
                    this,
                    x,
                    top + 2,
                    cWidth,
                    myHeight,
                    shouldValidate);
                component.setOpaque(hasBeenOpaque);
            }
        }
        
        if (comboIcon != null) {
            // Paint the focus
            boolean hasFocus = comboBox.hasFocus();
            if (!borderPaintsFocus && hasFocus) {
                g.setColor(PlasticLookAndFeel.getFocusColor());
                int x = LEFT_INSET;
                int y = LEFT_INSET;
                int w = getWidth()  - LEFT_INSET - RIGHT_INSET;
                int h = getHeight() - LEFT_INSET - RIGHT_INSET;
                g.drawRect(x, y, w - 1, h - 1);
            }
        }

    }

}
