package com.jgoodies.swing.util;

/*
 * Copyright (c) 2003 JGoodies Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of JGoodies Karsten Lentzsch.  
 * Use is subject to license terms.
 *
 */

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JToggleButton;

import com.jgoodies.swing.ToggleAction;
import com.jgoodies.swing.application.ActionManager;

/**
 * Provides a default configuration suitable for most toolbar toggle buttons. 
 *
 * @author Karsten Lentzsch
 */
public final class ToolBarToggleButton extends JToggleButton {

    /**
     * Constructs a toggle button for use in tool bars.
     */
    public ToolBarToggleButton() {
        ToolBarButton.configureButton(this);
    }

    /**
     * Constructs a toggle button with the specified icon for use in tool bars.
     */
    public ToolBarToggleButton(Icon icon) {
        super(icon);
        ToolBarButton.configureButton(this);
    }

    /**
     * Constructs a <code>ToolBarButton</code> using 
     * the specified <code>ToggleAction</code>.
     */
    public ToolBarToggleButton(ToggleAction toggleAction) {
        super(toggleAction);
        ToolBarButton.configureButton(this);
    }

    /**
     * Configures the button's properties from the given action.
     */
    protected final void configurePropertiesFromAction(Action a) {
        super.configurePropertiesFromAction(a);
        Icon icon = getIcon();
        Icon grayIcon = (Icon) a.getValue(ActionManager.SMALL_GRAY_ICON);
        if (grayIcon != null) {
            setSelectedIcon(icon);
            setRolloverIcon(icon);
            setIcon(grayIcon);
        }
        if (icon != null) {
            setText(null); // Would like to say: putClientProperty("hideActionText", Boolean.TRUE);
        }
        if (a instanceof ToggleAction) {
            setModel(((ToggleAction) a).model());
        }
    }

    public boolean isFocusTraversable() {
        return false;
    }

}