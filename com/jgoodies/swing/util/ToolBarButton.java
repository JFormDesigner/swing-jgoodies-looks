package com.jgoodies.swing.util;

/*
 * Copyright (c) 2003 JGoodies Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of JGoodies Karsten Lentzsch.
 * Use is subject to license terms.
 *
 */
import java.awt.Dimension;
import java.awt.Insets;

import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.UIManager;

import com.jgoodies.swing.application.ActionManager;

/**
 * Adds a special configuration to its superclass; useful in toolbars.
 *
 * @author Karsten Lentzsch
 */
public final class ToolBarButton extends JButton {

    private static final Insets EMPTY_INSETS = new Insets(0, 0, 0, 0);

    /**
     * Constructs a <code>ToolBarButton</code> using 
     * the specified <code>Icon</code>.
     */
    public ToolBarButton(Icon icon) {
        super(icon);
        configureButton(this);
    }

    /**
     * Constructs a <code>ToolBarButton</code> using 
     * the specified <code>Action</code>.
     */
    public ToolBarButton(Action action) {
        super(action);
        configureButton(this);
    }

    /**
     * Configures an <code>AbstractButton</code> for being used
     * in a tool bar.
     */
    public static void configureButton(AbstractButton button) {
        button.setHorizontalTextPosition(CENTER);
        button.setVerticalTextPosition(BOTTOM);
        button.setAlignmentY(CENTER_ALIGNMENT);
        button.setFocusPainted(false);
        button.setMargin(getButtonMargin(button));
        button.setMnemonic(0);
    }

    /**
     * Answers the button's margin.
     */
    private static Insets getButtonMargin(AbstractButton button) {
        Dimension defaultIconSize =
            UIManager.getDimension("jgoodies.defaultIconSize");
        Icon icon = button.getIcon();
        if (defaultIconSize == null || icon == null)
            return EMPTY_INSETS;

        int hpad = Math.max(0, defaultIconSize.width - icon.getIconWidth());
        int vpad = Math.max(0, defaultIconSize.height - icon.getIconHeight());
        int top    = vpad / 2;
        int left   = hpad / 2;
        int bottom = top  + vpad % 2;
        int right  = left + hpad % 2;
        return new Insets(top, left, bottom, right);
    }

    /*
     * Configures the button.
     */
    private void configureButton(JButton button) {
        configureButton((AbstractButton) button);
        button.setDefaultCapable(false);
    }

    /**
     * Configures the button's properties from the given action.
     */
    protected void configurePropertiesFromAction(Action a) {
        super.configurePropertiesFromAction(a);
        Icon icon = getIcon();
        Icon grayIcon = (Icon) a.getValue(ActionManager.SMALL_GRAY_ICON);
        if (grayIcon != null) {
            setRolloverIcon(icon);
            setIcon(grayIcon);
        }
        if (icon != null) {
            putClientProperty("hideActionText", Boolean.TRUE);
        }
    }

    public boolean isFocusTraversable() {
        return false;
    }

    public void setText(String text) {}

}