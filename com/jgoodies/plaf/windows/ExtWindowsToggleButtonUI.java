package com.jgoodies.plaf.windows;

/*
 * Copyright (c) 2002 Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of Karsten Lentzsch.  
 * Use is subject to license terms.
 *
 */

import java.beans.PropertyChangeListener;

import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;

import com.jgoodies.plaf.LookUtils;
import com.jgoodies.plaf.Options;
import com.jgoodies.plaf.common.ButtonMarginListener;
import com.sun.java.swing.plaf.windows.WindowsToggleButtonUI;

/**
 * Allows to use an optional narrow button margin.
 *
 * @author Karsten Lentzsch
 */
public final class ExtWindowsToggleButtonUI extends WindowsToggleButtonUI {
	
	
	private static final ExtWindowsToggleButtonUI INSTANCE = new ExtWindowsToggleButtonUI();
	
	
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
		PropertyChangeListener listener = new ButtonMarginListener(getPropertyPrefix());
		b.putClientProperty(ButtonMarginListener.CLIENT_KEY, listener);
		b.addPropertyChangeListener(Options.IS_NARROW_KEY, listener);
	}


	/**
	 * Uninstalls the extra listener for a change of the isNarrow property.
	 */
	public void uninstallListeners(AbstractButton b) {
		super.uninstallListeners(b);
		PropertyChangeListener listener = (PropertyChangeListener) b.getClientProperty(
												ButtonMarginListener.CLIENT_KEY);
		b.removePropertyChangeListener(listener);
	}
	
	
	
}