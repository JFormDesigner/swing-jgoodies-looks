/*
 * Copyright (c) 2003 JGoodies Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of JGoodies Karsten Lentzsch.  
 * Use is subject to license terms.
 *
 */

package com.jgoodies.plaf.plastic;

import java.awt.*;
import java.beans.PropertyChangeListener;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicHTML;
import javax.swing.plaf.metal.MetalToggleButtonUI;
import javax.swing.text.View;

import com.jgoodies.plaf.LookUtils;
import com.jgoodies.plaf.Options;
import com.jgoodies.plaf.common.ButtonMarginListener;

/**
 * The JGoodies Plastic L&amp;F implementation of
 * <code>ToggleButtonUI</code>.
 * <p>
 * It differs from its superclass in that it can add a pseudo 3D effect, 
 * and that it listens to the <code>jgoodies.isNarrow</code> property to
 * choose an appropriate margin.
 *
 * @author Karsten Lentzsch
 */

public class PlasticToggleButtonUI extends MetalToggleButtonUI {

    private static final PlasticToggleButtonUI INSTANCE =
        new PlasticToggleButtonUI();
        
    /* 
     * Implementation note: The protected visibility prevents
     * the String value from being encrypted by the obfuscator.
     * An encrypted String key would break the client property lookup
     * in the #paint method below.
     */    
    protected static final String HTML_KEY = BasicHTML.propertyKey;

    private boolean borderPaintsFocus;

    public static ComponentUI createUI(JComponent b) {
        return INSTANCE;
    }

    /**
     * Installs defaults and honors the client property <code>isNarrow</code>.
     */
    public void installDefaults(AbstractButton b) {
        super.installDefaults(b);
        LookUtils.installNarrowMargin(b, getPropertyPrefix());
        borderPaintsFocus =
            Boolean.TRUE.equals(
                UIManager.get("ToggleButton.borderPaintsFocus"));
    }

    /**
     * Installs an extra listener for a change of the isNarrow property.
     */
    public void installListeners(AbstractButton b) {
        super.installListeners(b);
        PropertyChangeListener listener =
            new ButtonMarginListener(getPropertyPrefix());
        b.putClientProperty(ButtonMarginListener.CLIENT_KEY, listener);
        b.addPropertyChangeListener(Options.IS_NARROW_KEY, listener);
    }

    /**
     * Uninstalls the extra listener for a change of the isNarrow property.
     */
    public void uninstallListeners(AbstractButton b) {
        super.uninstallListeners(b);
        PropertyChangeListener listener =
            (PropertyChangeListener) b.getClientProperty(
                ButtonMarginListener.CLIENT_KEY);
        b.removePropertyChangeListener(listener);
    }

    // Painting ***************************************************************

    public void update(Graphics g, JComponent c) {
        AbstractButton b = (AbstractButton) c;
        if (c.isOpaque()) {
            if (isToolBarButton(b)) {
                c.setOpaque(false);
            } else if (b.isContentAreaFilled()) {
                g.setColor(c.getBackground());
                g.fillRect(0, 0, c.getWidth(), c.getHeight());

                if (is3D(b)) {
                    Rectangle r =
                        new Rectangle(
                            1,
                            1,
                            c.getWidth() - 2,
                            c.getHeight() - 1);
                    PlasticUtils.add3DEffekt(g, r);
                }
            }
        }
        paint(g, c);
    }

    /**
     * Paints the focus close to the button's border.
     */
    protected void paintFocus(
        Graphics g,
        AbstractButton b,
        Rectangle viewRect,
        Rectangle textRect,
        Rectangle iconRect) {

        if (borderPaintsFocus)
            return;

        boolean isDefault = false;
        int topLeftInset = isDefault ? 3 : 2;
        int width = b.getWidth() - 1 - topLeftInset * 2;
        int height = b.getHeight() - 1 - topLeftInset * 2;

        g.setColor(getFocusColor());
        g.drawRect(topLeftInset, topLeftInset, width - 1, height - 1);
    }

    /**
     * Unlike the BasicToggleButtonUI.paint, we don't fill the content area;
     * this has been done by the update method before.
     */
    public void paint(Graphics g, JComponent c) {
        AbstractButton b = (AbstractButton) c;
        ButtonModel model = b.getModel();

        Dimension size = b.getSize();
        FontMetrics fm = g.getFontMetrics();

        Insets i = c.getInsets();

        Rectangle viewRect = new Rectangle(size);

        viewRect.x += i.left;
        viewRect.y += i.top;
        viewRect.width  -= (i.right  + viewRect.x);
        viewRect.height -= (i.bottom + viewRect.y);

        Rectangle iconRect = new Rectangle();
        Rectangle textRect = new Rectangle();

        Font f = c.getFont();
        g.setFont(f);

        // layout the text and icon
        String text =
            SwingUtilities.layoutCompoundLabel(
                c,
                fm,
                b.getText(),
                b.getIcon(),
                b.getVerticalAlignment(),
                b.getHorizontalAlignment(),
                b.getVerticalTextPosition(),
                b.getHorizontalTextPosition(),
                viewRect,
                iconRect,
                textRect,
                b.getText() == null ? 0 : getDefaultTextIconGap(b));
        // [Pending 1.4]: b.getIconTextGap());

        g.setColor(b.getBackground());

        if (model.isArmed() && model.isPressed() || model.isSelected()) {
            paintButtonPressed(g, b);
        } /*else if (b.isOpaque() && b.isContentAreaFilled() && !(is3D(b))) {
        			g.fillRect(0, 0, size.width, size.height);
        			
        		    Insets insets = b.getInsets();
        		    Insets margin = b.getMargin();
        	    
        	    	g.fillRect(insets.left - margin.left, insets.top - margin.top, 
        		       size.width  - (insets.left-margin.left) - (insets.right - margin.right),
        		       size.height - (insets.top-margin.top)   - (insets.bottom - margin.bottom));
        		       
        		}*/

        // Paint the Icon
        if (b.getIcon() != null) {
            paintIcon(g, b, iconRect);
        }

        // Draw the Text
        if (text != null && !text.equals("")) {
            View v = (View) c.getClientProperty(HTML_KEY);
            if (v != null) {
                v.paint(g, textRect);
            } else {
                paintText(g, c, textRect, text);
            }
        }

        // draw the dashed focus line.
        if (b.isFocusPainted() && b.hasFocus()) {
            paintFocus(g, b, viewRect, textRect, iconRect);
        }
    }

    // Private Helper Code **************************************************************

    /**
     * Checks and answers if this is button is in a tool bar.
     * 
     * @param b   the button to check
     * @return true if in tool bar, false otherwise
     */
    protected boolean isToolBarButton(AbstractButton b) {
        Container parent = b.getParent();
        return parent != null
            && (parent instanceof JToolBar
                || parent.getParent() instanceof JToolBar);
    }

    /**
     * Checks and answers if this button shall use a pseudo 3D effect
     * 
     * @param b  the button to check
     * @return true indicates a 3D effect, false flat
     */
    protected boolean is3D(AbstractButton b) {
        if (PlasticUtils.force3D(b))
            return true;
        if (PlasticUtils.forceFlat(b))
            return false;
        ButtonModel model = b.getModel();
        return PlasticUtils.is3D("ToggleButton.")
            && b.isBorderPainted()
            && model.isEnabled()
            && !model.isPressed()
            && !(b.getBorder() instanceof EmptyBorder);

        /*
         * Implementation note regarding the last line: instead of checking 
         * for the EmptyBorder in NetBeans, I'd prefer to just check the
         * 'borderPainted' property. I'd recommend to the NetBeans developers,
         * to switch this property on and off, instead of changing the border.
         */
    }

}