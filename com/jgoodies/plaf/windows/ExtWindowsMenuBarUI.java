package com.jgoodies.plaf.windows;

/*
 * Copyright (c) 2002 Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of Karsten Lentzsch.  
 * Use is subject to license terms.
 *
 */

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComponent;
import javax.swing.LookAndFeel;
import javax.swing.plaf.ComponentUI;

import com.jgoodies.plaf.BorderStyle;
import com.jgoodies.plaf.HeaderStyle;
import com.jgoodies.plaf.Options;
import com.sun.java.swing.plaf.windows.WindowsMenuBarUI;

/**
 * The JGoodies Windows look and feel implemenation of <code>MenuBarUI</code>.<p>
 * 
 * Can handle optional <code>Border</code> types as specified by the 
 * <code>BorderStyle</code> or <code>HeaderStyle</code> client properties.
 *
 * @author Karsten Lentzsch
 */
public final class ExtWindowsMenuBarUI extends WindowsMenuBarUI {
	
	private PropertyChangeListener listener;
	
	
	public static ComponentUI createUI(JComponent b) {
		return new ExtWindowsMenuBarUI();
	}
	
	
	// Handling Special Borders *********************************************************
	
	protected void installDefaults() {
		super.installDefaults();
		installSpecialBorder();
	}
	
	
	protected void installListeners() {
		super.installListeners();
		listener = createBorderStyleListener();
		menuBar.addPropertyChangeListener(listener);
	}
	
	
	protected void uninstallListeners() {
		menuBar.removePropertyChangeListener(listener);
		super.uninstallListeners();
	}
	
	
	private PropertyChangeListener createBorderStyleListener() {
		return new PropertyChangeListener() {
			
			public void propertyChange(PropertyChangeEvent e) {
				String prop = e.getPropertyName();
				if(prop.equals(Options.HEADER_STYLE_KEY) || 
				   prop.equals(ExtWindowsLookAndFeel.BORDER_STYLE_KEY)) {
				   ExtWindowsMenuBarUI.this.installSpecialBorder();
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
		BorderStyle borderStyle = BorderStyle.from(menuBar, 
												ExtWindowsLookAndFeel.BORDER_STYLE_KEY);
		if (borderStyle == BorderStyle.EMPTY)
			suffix = "emptyBorder";
		else if (borderStyle == BorderStyle.ETCHED)
			suffix = "etchedBorder";
		else if (borderStyle == BorderStyle.SEPARATOR)
			suffix = "separatorBorder";
		else if (HeaderStyle.from(menuBar) == HeaderStyle.BOTH)
			suffix = "headerBorder";
		else
			return;
			
		LookAndFeel.installBorder(menuBar, "MenuBar." + suffix);
	}

}