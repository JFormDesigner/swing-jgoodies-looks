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

package com.jgoodies.plaf.plastic;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.AbstractButton;
import javax.swing.ButtonModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.border.AbstractBorder;
import javax.swing.border.Border;
import javax.swing.plaf.BorderUIResource;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicBorders;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.text.JTextComponent;


/**
 * This class consists of a set of <code>Border</code>s used 
 * by the JGoodies Plastic XP Look and Feel UI delegates.
 *
 * @author Karsten Lentzsch
 */

final class PlasticXPBorders {


    // Accessing and Creating Borders ***************************************

    private static Border buttonBorder;
    private static Border comboBoxArrowButtonBorder;
    private static Border comboBoxEditorBorder;
    private static Border scrollPaneBorder;
    private static Border textFieldBorder;
    private static Border toggleButtonBorder;
    

    /*
     * Returns a border instance for a <code>JButton</code>.
     */
    static Border getButtonBorder() {
        if (buttonBorder == null) {
            buttonBorder = new BorderUIResource.CompoundBorderUIResource(
                    new XPButtonBorder(),
                    new BasicBorders.MarginBorder());
        }
        return buttonBorder;
    }

    /*
     * Returns a border instance for a <code>JComboBox</code>'s arrow button.
     */
    static Border getComboBoxArrowButtonBorder() {
        if (comboBoxArrowButtonBorder == null) {
            comboBoxArrowButtonBorder = new BorderUIResource.CompoundBorderUIResource(
                                    new XPComboBoxArrowButtonBorder(),
                                    new BasicBorders.MarginBorder());
        }
        return comboBoxArrowButtonBorder;
    }

    /*
     * Returns a border instance for a <code>JComboBox</code>'s editor.
     */
    static Border getComboBoxEditorBorder() {
        if (comboBoxEditorBorder == null) {
            comboBoxEditorBorder = new BorderUIResource.CompoundBorderUIResource(
                                    new XPComboBoxEditorBorder(),
                                    new BasicBorders.MarginBorder());
        }
        return comboBoxEditorBorder;
    }

    /*
     * Returns a border instance for a <code>JScrollPane</code>.
     */
    static Border getScrollPaneBorder() {
        if (scrollPaneBorder == null) {
            scrollPaneBorder = new BorderUIResource(new XPScrollPaneBorder());
        }
        return scrollPaneBorder;
    }

    /*
     * Returns a border instance for a <code>JTextField</code>.
     */
    static Border getTextFieldBorder() {
        if (textFieldBorder == null) {
            textFieldBorder = new BorderUIResource.CompoundBorderUIResource(
                                    new XPTextFieldBorder(),
                                    new BasicBorders.MarginBorder());
        }
        return textFieldBorder;
    }

    /*
     * Returns a border instance for a <code>JToggleButton</code>.
     */
    static Border getToggleButtonBorder() {
        if (toggleButtonBorder == null) {
            toggleButtonBorder = new BorderUIResource.CompoundBorderUIResource(
                    new XPButtonBorder(),
                    new BasicBorders.MarginBorder());
        }
        return toggleButtonBorder;
    }

    /*
     * A border for buttons.
     */
    private static class XPButtonBorder extends AbstractBorder implements UIResource {

        protected static final Insets INSETS = new Insets(3, 3, 3, 3);

        public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
            AbstractButton button = (AbstractButton) c;
            ButtonModel    model  = button.getModel();

            if (!model.isEnabled()) {
                PlasticXPUtils.drawDisabledButtonBorder(g, x, y, w, h);
                return;
            }
            
            boolean isPressed = model.isPressed() && model.isArmed();
            boolean isDefault = button instanceof JButton
                                     && ((JButton) button).isDefaultButton();
            boolean isFocused = button.isFocusPainted() && button.hasFocus();

            if (isPressed)
                PlasticXPUtils.drawPressedButtonBorder(g, x, y, w, h);
            else if (isDefault)
                PlasticXPUtils.drawDefaultButtonBorder(g, x, y, w, h);
            else if (isFocused)
                PlasticXPUtils.drawFocusedButtonBorder(g, x, y, w, h);
            else
                PlasticXPUtils.drawPlainButtonBorder(g, x, y, w, h);
        }

        public Insets getBorderInsets(Component c) { return INSETS; }

        public Insets getBorderInsets(Component c, Insets newInsets) {
            newInsets.top    = INSETS.top;
            newInsets.left   = INSETS.left;
            newInsets.bottom = INSETS.bottom;
            newInsets.right  = INSETS.right;
            return newInsets;
        }
    }


    /*
     * A border for combo box arrow buttons.
     */
    private static class XPComboBoxArrowButtonBorder extends AbstractBorder implements UIResource {

        protected static final Insets INSETS = new Insets(2, 2, 2, 2);

        public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
            PlasticComboBoxButton button = (PlasticComboBoxButton) c;
            JComboBox comboBox = button.getComboBox();
            ButtonModel model = button.getModel();

            if (!model.isEnabled()) {
                PlasticXPUtils.drawDisabledButtonBorder(g, x, y, w, h);
            } else {
                boolean isPressed = model.isPressed() && model.isArmed();
                boolean isFocused = comboBox.hasFocus();
                if (isPressed)
                    PlasticXPUtils.drawPressedButtonBorder(g, x, y, w, h);
                else if (isFocused)
                    PlasticXPUtils.drawFocusedButtonBorder(g, x, y, w, h);
                else
                    PlasticXPUtils.drawPlainButtonBorder(g, x, y, w, h);
            }
            if (comboBox.isEditable()) {
                // Paint two pixel on the arrow button's left hand side.
                g.setColor(model.isEnabled() 
                                ? PlasticLookAndFeel.getControlDarkShadow() 
                                : MetalLookAndFeel.getControlShadow());
                g.fillRect(x, y,       1, 1);
                g.fillRect(x, y + h-1, 1, 1);
            }
        }

        public Insets getBorderInsets(Component c) { return INSETS; }
    }


    /*
     * A border for combo box editors.
     */
    private static class XPComboBoxEditorBorder extends AbstractBorder {

        private static final Insets INSETS  = new Insets(2, 2, 2, 0);

        public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
            g.setColor(c.isEnabled()
                           ? PlasticLookAndFeel.getControlDarkShadow()
                           : MetalLookAndFeel.getControlShadow());
            PlasticXPUtils.drawRect(g, x, y, w+1, h-1);
        }

        public Insets getBorderInsets(Component c) { return INSETS; }
    }


    /*
     * A border for text fields.
     */
    private static class XPTextFieldBorder extends AbstractBorder  {

        private static final Insets INSETS = new Insets(2, 2, 2, 2);

		public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
            
            boolean enabled = ((c instanceof JTextComponent) 
                               && (c.isEnabled() && ((JTextComponent) c).isEditable()))
                               ||
                               c.isEnabled();
		
	        g.setColor(enabled 
                            ? PlasticLookAndFeel.getControlDarkShadow()
                            : MetalLookAndFeel.getControlShadow());
            PlasticXPUtils.drawRect(g, x, y, w-1, h-1);
    	}    
 
        public Insets getBorderInsets(Component c) { return INSETS; }

        public Insets getBorderInsets(Component c, Insets newInsets) {
            newInsets.top    = INSETS.top;
            newInsets.left   = INSETS.left;
            newInsets.bottom = INSETS.bottom;
            newInsets.right  = INSETS.right;
            return newInsets;
        }
	}

    /*
     * A border for text fields.
     */
    private static class XPScrollPaneBorder extends AbstractBorder  {

        private static final Insets INSETS = new Insets(1, 1, 1, 1);

        public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
            
            g.setColor(c.isEnabled() 
                    ? PlasticLookAndFeel.getControlDarkShadow()
                    : MetalLookAndFeel.getControlShadow());
            PlasticXPUtils.drawRect(g, x, y, w-1, h-1);
        }    
        
        public Insets getBorderInsets(Component c) { return INSETS; }

        public Insets getBorderInsets(Component c, Insets newInsets) {
            newInsets.top    = INSETS.top;
            newInsets.left   = INSETS.left;
            newInsets.bottom = INSETS.bottom;
            newInsets.right  = INSETS.right;
            return newInsets;
        }
    }

    
}