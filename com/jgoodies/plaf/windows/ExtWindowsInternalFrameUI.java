package com.jgoodies.plaf.windows;

/*
 * Copyright (c) 2002 Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of Karsten Lentzsch.  
 * Use is subject to license terms.
 *
 */
 
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
