/*
 * Copyright (c) 2003 JGoodies Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of JGoodies Karsten Lentzsch.  
 * Use is subject to license terms.
 *
 */

package com.jgoodies.plaf.common;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.AbstractButton;

import com.jgoodies.plaf.LookUtils;
import com.jgoodies.plaf.Options;

/**
 * Listens for changes of the <code>jgoodies.isNarrow</code> property,
 * and installs a wide or narrow margin.
 *
 * @author Karsten Lentzsch
 */

public final class ButtonMarginListener implements PropertyChangeListener {

    public static final String CLIENT_KEY = "jgoodies.buttonMarginListener";

    private final String propertyPrefix;

    public ButtonMarginListener(String propertyPrefix) {
        this.propertyPrefix = propertyPrefix;
    }

    public void propertyChange(PropertyChangeEvent e) {
        String prop = e.getPropertyName();
        AbstractButton button = (AbstractButton) e.getSource();
        if (prop.equals(Options.IS_NARROW_KEY)) {
            LookUtils.installNarrowMargin(button, propertyPrefix);
        }
    }

}