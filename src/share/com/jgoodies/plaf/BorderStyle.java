/*
 * Copyright (c) 2003 JGoodies Karsten Lentzsch. All Rights Reserved.
 *
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 * 
 *  o Redistributions of source code must retain the above copyright notice, 
 *    this list of conditions and the following disclaimer. 
 *     
 *  o Redistributions in binary form must reproduce the above copyright notice, 
 *    this list of conditions and the following disclaimer in the documentation 
 *    and/or other materials provided with the distribution. 
 *     
 *  o Neither the name of JGoodies Karsten Lentzsch nor the names of 
 *    its contributors may be used to endorse or promote products derived 
 *    from this software without specific prior written permission. 
 *     
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, 
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR 
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR 
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, 
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, 
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; 
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, 
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE 
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, 
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. 
 */

package com.jgoodies.plaf;

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
     * Looks up the client property for the header style from the 
     * <code>JToolBar</code.
     * 
     * @param toolBar  the tool bar to inspect
     * @param clientPropertyKey   the key used to lookup the property
     * @return the border style used to choose a border in the UI delegate
     */
   	public static BorderStyle from(JToolBar toolBar, String clientPropertyKey) {
   		return from0(toolBar, clientPropertyKey);
   	}
   	
    /**
     * Looks up the client property for the header style from the <code>JToolBar</code.
     * 
     * @param menuBar  the menu bar to inspect
     * @param clientPropertyKey   the key used to lookup the property
     * @return the border style used to choose a border in the UI delegate
     */
   	public static BorderStyle from(JMenuBar menuBar, String clientPropertyKey) {
   		return from0(menuBar, clientPropertyKey);
   	}
   	
   	
   	/**
   	 * Looks up the client property for the header style from the specified
   	 * <code>JComponent</code>.
     * 
     * @param c  the compoent to inspect
     * @param clientPropertyKey   the key used to lookup the property
     * @return the border style used to choose a border in the UI delegate
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