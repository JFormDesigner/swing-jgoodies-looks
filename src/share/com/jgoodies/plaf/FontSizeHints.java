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

/**
 * Describes font size hints used by the JGoodies Windows look&amp;feel; future
 * implementations of the Plastic l&amp;f will use the same hints. In 1.3
 * environments the sizes are used as absolute font sizes, in 1.4 environments
 * size deltas are used between SYSTEM and the specified sizes.<p>
 * 
 * NOTE: This is work in progress and will probably change in the
 * next release, to better reflect the font choice in the J2SE 1.4.1.
 * Currently, the size delta is used only for the Tahoma font!<p>
 * 
 * In 1.3 environments, the font guess is Tahoma on modern Windows,
 * "dialog" otherwise. In 1.4 environments, the system fonts will be used.
 *
 * @author Karsten Lentzsch
 * @see	Options#setGlobalFontSizeHints
 * @see	FontUtils
 */
public final class FontSizeHints {
	
	public static final FontSizeHints LARGE	= new FontSizeHints(12, 12, 14, 14);
	public static final FontSizeHints SYSTEM	= new FontSizeHints(11, 11, 14, 14);
    public static final FontSizeHints MIXED2  = new FontSizeHints(11, 11, 14, 13);
	public static final FontSizeHints MIXED	= new FontSizeHints(11, 11, 14, 12);
	public static final FontSizeHints SMALL	= new FontSizeHints(11, 11, 12, 12);
	public static final FontSizeHints FIXED	= new FontSizeHints(12, 12, 12, 12);
	
	public static final FontSizeHints DEFAULT = SYSTEM;
	
	
	private final int loResMenuFontSize;
	private final int loResControlFontSize;
	private final int hiResMenuFontSize;
	private final int hiResControlFontSize;
	
	
	/**
	 * Constructs <code>FontSizeHints</code> for the specified menu and
	 * control fonts, both for low and high resolution environments.
	 */
	public FontSizeHints(int loResMenuFontSize, int loResControlFontSize, 
					  	  int hiResMenuFontSize, int hiResControlFontSize) {
		this.loResMenuFontSize		= loResMenuFontSize; 
		this.loResControlFontSize	= loResControlFontSize;
		this.hiResMenuFontSize		= hiResMenuFontSize;
		this.hiResControlFontSize	= hiResControlFontSize;
	}
	
	
	/**
	 * Answers the low resolution menu font size.
	 */
	public int loResMenuFontSize()		{ return loResMenuFontSize;	}


	/**
	 * Answers the low resolution control font size.
	 */
	public int loResControlFontSize()	{ return loResControlFontSize;	}


	/**
	 * Answers the high resolution menu font size.
	 */
	public int hiResMenuFontSize()		{ return hiResMenuFontSize;	}


	/**
	 * Answers the high resolution control font size.
	 */
	public int hiResControlFontSize()	{ return hiResControlFontSize;	}
	
	
	/**
	 * Answers the menu font size.
	 */
	public int menuFontSize() {
		return LookUtils.isLowRes ? loResMenuFontSize : hiResMenuFontSize();
	}
	
	
	/**
	 * Answers the control font size.
	 */
	public int controlFontSize() {
		return LookUtils.isLowRes ? loResControlFontSize : hiResControlFontSize();
	}
	
	
	/**
	 * Answers the delta between system menu font size and our menu font size hint.
	 */
	public float menuFontSizeDelta() {
		return menuFontSize() - SYSTEM.menuFontSize();
	}
	
	
	/**
	 * Answers the delta between system control font size and our control font size hint.
	 */
	public float controlFontSizeDelta() {
		return controlFontSize() - SYSTEM.controlFontSize();
	}
	
	
	
	/**
	 * Answers the <code>FontSizeHints</code> for the specified name.
	 */
	public static FontSizeHints valueOf(String name) {
		if (name.equalsIgnoreCase("LARGE"))
			return LARGE;
		else if (name.equalsIgnoreCase("SYSTEM"))
			return SYSTEM;
		else if (name.equalsIgnoreCase("MIXED"))
			return MIXED;
		else if (name.equalsIgnoreCase("SMALL"))
			return SMALL;
		else if (name.equalsIgnoreCase("FIXED"))
			return FIXED;
		else 
			throw new IllegalArgumentException("Unknown font size hints name: " + name);
	}
}