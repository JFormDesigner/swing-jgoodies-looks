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
 * Describes the border styles for <code>JMenuBar</code> and <code>JToolBar</code>.
 * Border styles are look-dependent and shadow look-independent <code>HeaderStyle</code>.
 * 
 * @author Karsten Lentzsch
 * @see	HeaderStyle
 */
public final class BorderStyle {
	
	public static final BorderStyle EMPTY		= new BorderStyle("Empty");
	public static final BorderStyle SEPARATOR = new BorderStyle("Separator");
	public static final BorderStyle ETCHED	= new BorderStyle("Etched");

	private final String name;


	private BorderStyle(String name) { 
		this.name = name; 
	}
	
	
    /**
     * Looks up the client property for the header style from the <code>JToolBar</code.
     */
   	public static BorderStyle from(JToolBar toolBar, String clientPropertyKey) {
   		return from0(toolBar, clientPropertyKey);
   	}
   	
    /**
     * Looks up the client property for the header style from the <code>JToolBar</code.
     */
   	public static BorderStyle from(JMenuBar menuBar, String clientPropertyKey) {
   		return from0(menuBar, clientPropertyKey);
   	}
   	
   	
   	/**
   	 * Looks up the client property for the header style from the specified
   	 * <code>JComponent</code>.
   	 */
   	private static BorderStyle from0(JComponent c, String clientPropertyKey) {
   		Object value = c.getClientProperty(clientPropertyKey);
   		if (value instanceof BorderStyle)
   			return (BorderStyle) value;
   		
   		if (value instanceof String) {
   			return BorderStyle.valueOf((String) value);
   		} 
   		
   		return null;
   	}
   	
   	
	private static BorderStyle valueOf(String name) {
		if (name.equalsIgnoreCase(EMPTY.name))
			return EMPTY;
		else if (name.equalsIgnoreCase(SEPARATOR.name))
			return SEPARATOR;
		else if (name.equalsIgnoreCase(ETCHED.name))
			return ETCHED;
		else
			throw new IllegalArgumentException("Invalid BorderStyle name " + name);		
	}
	

	public  String toString() { 
		return name; 
	}
	
}