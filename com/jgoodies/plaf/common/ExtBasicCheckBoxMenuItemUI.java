package com.jgoodies.plaf.common;

/*
 * Copyright (c) 2002 Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of Karsten Lentzsch.  
 * Use is subject to license terms.
 *
 */

import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;


/**
 * Renders aligned <code>JCheckBoxMenuItem</code>s.
 *
 * @author Karsten Lentzsch
 */
public final class ExtBasicCheckBoxMenuItemUI extends ExtBasicRadioButtonMenuItemUI {
	
	protected String getPropertyPrefix() { return "CheckBoxMenuItem"; }
	
	
	public static ComponentUI createUI(JComponent b) {
		return new ExtBasicCheckBoxMenuItemUI();
	}
	
}