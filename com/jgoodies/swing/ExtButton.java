package com.jgoodies.swing;

/*
 * Copyright (c) 2002 Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of Karsten Lentzsch.  
 * Use is subject to license terms.
 *
 */
import javax.swing.Icon;
import javax.swing.JButton;

/**
 * Allows to set a hint that the look-and-feel should use a narrow margin.
 *
 * @author Karsten Lentzsch
 */

public final class ExtButton extends JButton {
	
	private static final String IS_NARROW_KEY = "jgoodies.isNarrow";
	
	public ExtButton() 				{}
	public ExtButton(String label)		{ super(label); 	}
	public ExtButton(Icon icon)   		{ super(icon);	}
	
	
	public boolean isNarrow() { 
		return Boolean.TRUE.equals(getClientProperty(IS_NARROW_KEY));
	}
	
	
	public void setNarrow(boolean b) {
		putClientProperty(IS_NARROW_KEY, new Boolean(b));
	}
	
}