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

package com.jgoodies.plaf.windows;

import java.awt.Color;

import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;

import com.jgoodies.plaf.LookUtils;
import com.sun.java.swing.plaf.windows.WindowsInternalFrameUI;


/**
 * The JGoodies Windows Look and Feel implementation of <code>InternalFrameUI</code>.<p>
 * 
 * Corrects a background bug in 1.3 environments.
 *
 * @author Karsten Lentzsch
 */
public final class ExtWindowsInternalFrameUI extends WindowsInternalFrameUI {


	public ExtWindowsInternalFrameUI(JInternalFrame b) {
		super(b);
	}


	public static ComponentUI createUI(JComponent c) {
		return new ExtWindowsInternalFrameUI((JInternalFrame) c);
	}


	/* Enable the content pane to inherit the background color 
	 * from its parent by setting its background color to null. 
	 * Fixes bug#4268949, which has been fixed in 1.4, too.
	 */
    public void installDefaults() {
    	super.installDefaults();
    	
    	if (!LookUtils.IS_BEFORE_14) {
    		return;
    	}
	
		JComponent contentPane = (JComponent) frame.getContentPane();
		if (contentPane != null) {
	          Color bg = contentPane.getBackground();
		  if (bg instanceof UIResource)
		    contentPane.setBackground(null);
		}
		frame.setBackground(UIManager.getLookAndFeelDefaults().getColor("control"));
    }
    
    
}
