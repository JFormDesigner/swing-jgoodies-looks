package com.jgoodies.plaf.plastic;

/*
 * Copyright (c) 2003 JGoodies Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of JGoodies Karsten Lentzsch.  
 * Use is subject to license terms.
 *
 */

import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.LookAndFeel;
import javax.swing.border.Border;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.metal.MetalButtonUI;
import javax.swing.plaf.metal.MetalToggleButtonUI;
import javax.swing.plaf.metal.MetalToolBarUI;

import com.jgoodies.plaf.BorderStyle;
import com.jgoodies.plaf.HeaderStyle;
import com.jgoodies.plaf.LookUtils;
import com.jgoodies.plaf.Options;

/**
 * The JGoodies Plastic look and feel implementation of <code>ToolBarUI</code>.<p>
 * 
 * Corrects the rollover borders and can handle optional <code>Border</code> types,
 * as specified by the <code>BorderStyle</code> or <code>HeaderStyle</code>
 * client properties.
 *
 * @author Karsten Lentzsch
 */

public final class PlasticToolBarUI extends MetalToolBarUI {

    private static final String PROPERTY_PREFIX = "ToolBar.";
    private final Border myRolloverBorder = createRolloverBorder();

    private PropertyChangeListener listener;

    public static ComponentUI createUI(JComponent b) {
        return new PlasticToolBarUI();
    }

    // Rollover Borders *****************************************************

    protected Border createRolloverBorder() {
        return PlasticBorders.getRolloverButtonBorder();
    }

    protected void setBorderToRollover(Component c) {
        if (LookUtils.IS_BEFORE_14) {
            setBorderToRollover13(c);
            return;
        }

        if (c instanceof AbstractButton) {
            super.setBorderToRollover(c);
        } else if (c instanceof Container) {
            Container cont = (Container) c;
            for (int i = 0; i < cont.getComponentCount(); i++)
                super.setBorderToRollover(cont.getComponent(i));
        }
    }

    private void setBorderToRollover13(Component c) {
        if (c instanceof AbstractButton) {
            AbstractButton b = (AbstractButton) c;
            Object ui = b.getUI();
            if ((ui instanceof MetalButtonUI)
                || (ui instanceof MetalToggleButtonUI))
                b.setBorder(myRolloverBorder);
            b.setRolloverEnabled(true);
        } else if (c instanceof Container) {
            Container cont = (Container) c;
            for (int i = 0; i < cont.getComponentCount(); i++)
                setBorderToRollover13(cont.getComponent(i));
        }
    }

    // Handling Special Borders *********************************************

    /**
     * Installs a special border, if indicated by the <code>HeaderStyle</code>.
     */
    protected void installDefaults() {
        super.installDefaults();
        installSpecialBorder();
    }

    protected void installListeners() {
        super.installListeners();
        listener = createBorderStyleListener();
        toolBar.addPropertyChangeListener(listener);
    }

    protected void uninstallListeners() {
        toolBar.removePropertyChangeListener(listener);
        super.uninstallListeners();
    }

    private PropertyChangeListener createBorderStyleListener() {
        return new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent e) {
                String prop = e.getPropertyName();
                if (prop.equals(Options.HEADER_STYLE_KEY)
                    || prop.equals(PlasticLookAndFeel.BORDER_STYLE_KEY)) {
                    PlasticToolBarUI.this.installSpecialBorder();
                }
            }

        };
    }

    /**
     * Installs a special border, if either a look-dependent 
     * <code>BorderStyle</code> or a look-independent 
     * <code>HeaderStyle</code> has been specified.
     * A look specific <code>BorderStyle<code> shadows 
     * a <code>HeaderStyle</code>.<p>
     * 
     * Specifying a <code>HeaderStyle</code> is recommend.
     */
    private void installSpecialBorder() {
        String suffix;
        BorderStyle borderStyle =
            BorderStyle.from(toolBar, PlasticLookAndFeel.BORDER_STYLE_KEY);
        if (borderStyle == BorderStyle.EMPTY)
            suffix = "emptyBorder";
        else if (borderStyle == BorderStyle.ETCHED)
            suffix = "etchedBorder";
        else if (borderStyle == BorderStyle.SEPARATOR)
            suffix = "separatorBorder";
        else {
            HeaderStyle headerStyle = HeaderStyle.from(toolBar);
            if (headerStyle == HeaderStyle.BOTH)
                suffix = "headerBorder";
            else if (headerStyle == HeaderStyle.SINGLE && is3D())
                suffix = "etchedBorder";
            else
                return;
        }

        LookAndFeel.installBorder(toolBar, PROPERTY_PREFIX + suffix);
    }

    // 3D Effect ************************************************************

    public void update(Graphics g, JComponent c) {
        if (c.isOpaque()) {
            g.setColor(c.getBackground());
            g.fillRect(0, 0, c.getWidth(), c.getHeight());
        }
        if (is3D()) {
            Rectangle bounds =
                new Rectangle(0, 0, c.getWidth(), c.getHeight());
            PlasticUtils.addLight3DEffekt(g, bounds, true);
        }
        paint(g, c);
    }

    /**
     * Checks and answers if we should add a pseudo 3D effect.
     */
    private boolean is3D() {
        if (PlasticUtils.force3D(toolBar))
            return true;
        if (PlasticUtils.forceFlat(toolBar))
            return false;
        return PlasticUtils.is3D(PROPERTY_PREFIX)
            && (HeaderStyle.from(toolBar) != null)
            && (BorderStyle.from(toolBar, PlasticLookAndFeel.BORDER_STYLE_KEY)
                != BorderStyle.EMPTY);
    }

}