package com.jgoodies.plaf.windows;

/*
 * Copyright (c) 2003 JGoodies Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of JGoodies Karsten Lentzsch.  
 * Use is subject to license terms.
 *
 */

import java.awt.Component;
import java.awt.Container;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.JToolBar;
import javax.swing.LookAndFeel;
import javax.swing.border.Border;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.metal.MetalToolBarUI;

import com.jgoodies.plaf.BorderStyle;
import com.jgoodies.plaf.HeaderStyle;
import com.jgoodies.plaf.LookUtils;
import com.jgoodies.plaf.Options;
import com.sun.java.swing.plaf.windows.WindowsButtonUI;
import com.sun.java.swing.plaf.windows.WindowsToggleButtonUI;

/**
 * Corrects superclass behavior for rollover borders
 * and adds behavior for handling different types of borders.
 *
 * @author Karsten Lentzsch
 */
public final class ExtWindowsToolBarUI extends MetalToolBarUI {

    private final Border myRolloverBorder = createRolloverBorder();

    private PropertyChangeListener listener;

    public static ComponentUI createUI(JComponent b) {
        return new ExtWindowsToolBarUI();
    }

    // Handling Special Borders *********************************************

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
                    || prop.equals(ExtWindowsLookAndFeel.BORDER_STYLE_KEY)) {
                    ExtWindowsToolBarUI.this.installSpecialBorder();
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
            BorderStyle.from(toolBar, ExtWindowsLookAndFeel.BORDER_STYLE_KEY);
        if (borderStyle == BorderStyle.EMPTY)
            suffix = "emptyBorder";
        else if (borderStyle == BorderStyle.SEPARATOR)
            suffix = "separatorBorder";
        else if (borderStyle == BorderStyle.ETCHED)
            suffix = "etchedBorder";
        else if (HeaderStyle.from(toolBar) == HeaderStyle.BOTH)
            suffix = "headerBorder";
        else
            return;
        LookAndFeel.installBorder(toolBar, "ToolBar." + suffix);
    }

    protected DockingListener createDockingListener(JToolBar toolbar) {
        return new DockingListener(toolbar);
    }

    // Handling Rollover Borders ********************************************

    protected Border createRolloverBorder() {
        return ExtWindowsBorders.getRolloverButtonBorder();
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
            if ((ui instanceof WindowsButtonUI)
                || (ui instanceof WindowsToggleButtonUI))
                b.setBorder(myRolloverBorder);
            b.setRolloverEnabled(true);
        } else if (c instanceof Container) {
            Container cont = (Container) c;
            for (int i = 0; i < cont.getComponentCount(); i++)
                setBorderToRollover(cont.getComponent(i));
        }
    }
}