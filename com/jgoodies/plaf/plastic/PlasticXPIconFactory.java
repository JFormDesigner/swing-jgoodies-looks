package com.jgoodies.plaf.plastic;

/*
 * Copyright (c) 2003 JGoodies Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of JGoodies Karsten Lentzsch.  
 * Use is subject to license terms.
 *
 */

import java.awt.*;
import java.io.Serializable;

import javax.swing.AbstractButton;
import javax.swing.ButtonModel;
import javax.swing.Icon;
import javax.swing.JCheckBox;
import javax.swing.UIManager;
import javax.swing.plaf.UIResource;

import com.jgoodies.plaf.LookUtils;

/**
 * Factory class that vends <code>Icon</code>s for the JGoodies 
 * Plastic XP look&amp;feel.
 * These icons are used extensively in PlasticXP via the defaults mechanism.
 * While other look and feels often use GIFs for icons, creating icons
 * in code facilitates switching to other themes.
 * <p>
 * Each method in this class returns either an <code>Icon</code> or 
 * <code>null</code>, where <code>null</code> implies that there is 
 * no default icon.
 *
 * @author Torge Husfeldt
 * @author Karsten Lentzsch
 */
public final class PlasticXPIconFactory {

    private static CheckBoxIcon    checkBoxIcon;
    private static RadioButtonIcon radioButtonIcon;


    /**
     * Lazily creates and answers the check box icon.
     * 
     * @return the check box icon
     */
    static Icon getCheckBoxIcon() {
        if (checkBoxIcon == null) {
            checkBoxIcon = new CheckBoxIcon();
        }
        return checkBoxIcon;
    }

    /**
     * Lazily creates and answers the radio button icon.
     * 
     * @return the check box icon
     */
    static Icon getRadioButtonIcon() {
        if (radioButtonIcon == null) {
            radioButtonIcon = new RadioButtonIcon();
        }
        return radioButtonIcon;
    }


    private static class CheckBoxIcon implements Icon, UIResource, Serializable {

        private static final int SIZE = LookUtils.isLowRes ? 13 : 15;

        public int getIconWidth()  { return SIZE; }
        public int getIconHeight() { return SIZE; }

        public void paintIcon(Component c, Graphics g, int x, int y) {
            JCheckBox cb = (JCheckBox) c;
            ButtonModel model = cb.getModel();
            Graphics2D g2 = (Graphics2D) g;
            Object hint = g2.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            drawBorder(g2, model.isEnabled(), x, y, SIZE - 1, SIZE - 1);
            drawFill(g2, model.isPressed(), x + 1, y + 1, SIZE - 2, SIZE - 2);
            if (model.isEnabled() && (model.isArmed() && !(model.isPressed()))) {
                drawFocus(g2, x + 1, y + 1, SIZE - 3, SIZE - 3);
            }
            if (model.isSelected()) {
                drawCheck(g2, model.isEnabled(), x + 3, y + 3, SIZE - 7, SIZE - 7);
            }
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, hint);
        }

        private void drawBorder(Graphics2D g2, boolean enabled, int x, int y, int width, int height) {
            g2.setColor(enabled
                    ? PlasticXPLookAndFeel.getControlDarkShadow()
                    : PlasticXPLookAndFeel.getControlDisabled());
            g2.drawRect(x, y, width, height);
        }

        private void drawCheck(Graphics2D g2, boolean enabled, int x, int y, int width, int height) {
            g2.setColor(enabled
                    ? UIManager.getColor("CheckBox.check")
                    : PlasticXPLookAndFeel.getControlDisabled());
            int right  = x + width;
            int bottom = y + height;
            int startY = y + height / 3;
            int turnX  = x + width  / 2 - 2;
            g2.drawLine(x        , startY    , turnX, bottom-3);
			g2.drawLine(x        , startY + 1, turnX, bottom-2);
			g2.drawLine(x        , startY + 2, turnX, bottom-1);
			g2.drawLine(turnX + 1, bottom - 2, right, y       );
			g2.drawLine(turnX + 1, bottom - 1, right, y + 1   );
			g2.drawLine(turnX + 1, bottom    , right, y + 2   );
        }

        private void drawFill(Graphics2D g2, boolean pressed, int x, int y, int w, int h) {
            Color upperLeft;
            Color lowerRight;
            if (pressed) {
                upperLeft  = PlasticXPLookAndFeel.getControlShadow();
                lowerRight = PlasticXPLookAndFeel.getControlHighlight();
            } else {
                upperLeft  = PlasticXPLookAndFeel.getControl();
                lowerRight = PlasticXPLookAndFeel.getControlHighlight().brighter();
            }
            g2.setPaint(new GradientPaint(x, y, upperLeft, x + w, y + h, lowerRight));
            g2.fillRect(x, y, w, h);
        }

        private void drawFocus(Graphics2D g2, int x, int y, int width, int height) {
            g2.setPaint(new GradientPaint(
                    x,
                    y,
                    PlasticXPLookAndFeel.getFocusColor().brighter(),
                    width,
                    height,
                    PlasticXPLookAndFeel.getFocusColor() /*.darker()*/
            ));
            g2.drawRect(x, y, width, height);
            g2.drawRect(x + 1, y + 1, width - 2, height - 2);
        }

    }

    // Paints the icon for a radio button.
    private static class RadioButtonIcon implements Icon, UIResource, Serializable {

        private static final int SIZE = LookUtils.isLowRes ? 13 : 15;
        
        private static final Stroke FOCUS_STROKE = new BasicStroke(2);

        public int getIconWidth()  { return SIZE; }
        public int getIconHeight() { return SIZE; }

        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g;
            AbstractButton b = (AbstractButton) c;
            ButtonModel model = b.getModel();
        
            Object hint = g2.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
            drawFill(g2, model.isPressed(), x, y, SIZE - 1, SIZE - 1);
        
            if (model.isArmed() && !(model.isPressed())) {
                drawFocus(g2, x + 1, y + 1, SIZE - 3, SIZE - 3);
            }
            if (model.isSelected()) {
                drawCheck(g2, c, model.isEnabled(), x + 4, y + 4, SIZE - 8, SIZE - 8);
            }
        	drawBorder(g2, model.isEnabled(), x, y, SIZE-1, SIZE-1);
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, hint);
        }

        private void drawBorder(Graphics2D g2, boolean enabled, int x, int y, int w, int h) {
            g2.setColor(enabled
                ? PlasticXPLookAndFeel.getControlDarkShadow()
                : PlasticXPLookAndFeel.getControlDisabled());
            g2.drawOval(x, y, w, h);
        }

        private void drawCheck(Graphics2D g2, Component c, boolean enabled, int x, int y, int w, int h) {
            g2.translate(x,y);
            if (enabled) {
                g2.setColor(UIManager.getColor("RadioButton.check"));
                g2.fillOval(0,0,w,h);
                UIManager.getIcon("RadioButton.checkIcon").paintIcon(c, g2, 0,0);
            } else {
                g2.setColor(PlasticXPLookAndFeel.getControlDisabled());
                g2.fillOval(0,0,w,h);
            }
            g2.translate(-x, -y);
        }

        private void drawFill(Graphics2D g2, boolean pressed, int x, int y, int w, int h) {
            Color upperLeft;
            Color lowerRight;
            if (pressed) {
                upperLeft  = PlasticXPLookAndFeel.getControlShadow();
                lowerRight = PlasticXPLookAndFeel.getControlHighlight();
            } else {
                upperLeft  = PlasticXPLookAndFeel.getControl();
                lowerRight = PlasticXPLookAndFeel.getControlHighlight().brighter();
            }
            g2.setPaint(new GradientPaint(x, y, upperLeft, x + w, y + h, lowerRight));
            g2.fillOval(x, y, w, h);
        }

        private void drawFocus(Graphics2D g2, int x, int y, int w, int h) {
            g2.setPaint(
                new GradientPaint(
                    x,
                    y,
                    PlasticXPLookAndFeel.getFocusColor().brighter(),
                    w,
                    h,
                    PlasticXPLookAndFeel.getFocusColor()));
            Stroke stroke = g2.getStroke();
            g2.setStroke(FOCUS_STROKE);
            g2.drawOval(x, y, w, h);
            g2.setStroke(stroke);
        }

    }

}
