/*
 * Copyright (c) 2003 JGoodies Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of JGoodies Karsten Lentzsch.  
 * Use is subject to license terms.
 *
 */

package com.jgoodies.plaf.windows;

import java.beans.PropertyChangeListener;

import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;

import com.jgoodies.plaf.LookUtils;
import com.jgoodies.plaf.Options;
import com.jgoodies.plaf.common.ButtonMarginListener;
import com.sun.java.swing.plaf.windows.WindowsButtonUI;

/**
 * The JGoodies Windows look&amp;feel implementation of <code>ButtonUI</code>.
 * <p>
 * 
 * It differs from its superclass in that it it listens to the 
 * <code>jgoodies.isNarrow</code> property to choose an appropriate margin.
 *
 * @author Karsten Lentzsch
 */
public final class ExtWindowsButtonUI extends WindowsButtonUI {

    private static final ExtWindowsButtonUI INSTANCE = new ExtWindowsButtonUI();

    public static ComponentUI createUI(JComponent b) {
        return INSTANCE;
    }

    /**
     * Installs defaults and honors the client property <code>isNarrow</code>.
     */
    protected void installDefaults(AbstractButton b) {
        super.installDefaults(b);
        LookUtils.installNarrowMargin(b, getPropertyPrefix());
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

}