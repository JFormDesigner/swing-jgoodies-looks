package com.jgoodies.plaf;

/*
 * Copyright (c) 2002 Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of Karsten Lentzsch.  
 * Use is subject to license terms.
 *
 */

import javax.swing.JComponent;
import javax.swing.JMenuBar;
import javax.swing.JToolBar;

/**
 * Describes the header styles for <code>JMenuBar</code> and <code>JToolBar</code>.
 * Header styles are look-independent and can be shadowed by a look-dependent 
 * <code>BorderStyle</code>.
 * 
 * @author Karsten Lentzsch
 * @see	BorderStyle
 */
public final class HeaderStyle {
	
	public static final HeaderStyle SINGLE = new HeaderStyle("Single");
	public static final HeaderStyle BOTH	 = new HeaderStyle("Both");

	private final String name;


	private HeaderStyle(String name) { 
		this.name = name; 
	}
	
	
    /**
     * Looks up the client property for the <code>HeaderStyle</code>
     * from the <code>JToolBar</code.
     */
   	public static HeaderStyle from(JMenuBar menuBar) {
   		return from0(menuBar);
   	}
   	
   	
    /**
     * Looks up the client property for the <code>HeaderStyle</code>
     * from the <code>JToolBar</code.
     */
   	public static HeaderStyle from(JToolBar toolBar) {
   		return from0(toolBar);
   	}

   	
   	/**
   	 * Looks up the client property for the <code>HeaderStyle</code> 
   	 * from the specified <code>JComponent</code>.
   	 */
   	private static HeaderStyle from0(JComponent c) {
   		Object value = c.getClientProperty(Options.HEADER_STYLE_KEY);
   		if (value instanceof HeaderStyle)
   			return (HeaderStyle) value;
   		
   		if (value instanceof String) {
   			return HeaderStyle.valueOf((String) value);
   		} 
   		
   		return null;
   	}
   	
   	
   	/**
   	 * Looks up and answers the <code>HeaderStyle</code> with the specified name.
   	 */
	private static HeaderStyle valueOf(String name) {
		if (name.equalsIgnoreCase(SINGLE.name))
			return SINGLE;
		else if (name.equalsIgnoreCase(BOTH.name))
			return BOTH;
		else
			throw new IllegalArgumentException("Invalid HeaderStyle name " + name);		
	}
	

	public  String toString() { 
		return name; 
	}
	
}