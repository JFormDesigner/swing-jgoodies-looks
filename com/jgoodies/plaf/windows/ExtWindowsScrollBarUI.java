package com.jgoodies.plaf.windows;

/*
 * Copyright (c) 2002 Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of Karsten Lentzsch.  
 * Use is subject to license terms.
 *
 */

import java.awt.Dimension;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JScrollBar;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;

import com.jgoodies.plaf.LookUtils;
import com.sun.java.swing.plaf.windows.WindowsScrollBarUI;

/**
 * The JGoodies Windows Look and Feel implementation of
 * <code>ScrollBarUI</code>.<p>
 * 
 * It differs from Sun's Windows Look in that it paints black button triangles
 * and that it honors the <code>ScrollBar.width</code> property to determine
 * the preferred size. In 1.3 environments, this property is set honoring 
 * the screen resolution.
 * 
 * @author Karsten Lentzsch
 */
public final class ExtWindowsScrollBarUI extends WindowsScrollBarUI {
	
	public static ComponentUI createUI(JComponent b) {
		return new ExtWindowsScrollBarUI();
	}


	protected JButton createDecreaseButton(int orientation) {
		return new ExtWindowsArrowButton(orientation);
	}
	
	
	protected JButton createIncreaseButton(int orientation) {
		return createDecreaseButton(orientation);
	}
	

    /*
     * Differs from superclass behavior in that it honors
     * the screen resolution in 1.3 environments.
     */
    public Dimension getPreferredSize(JComponent c) {
    	if (!LookUtils.IS_BEFORE_14)
    		return super.getPreferredSize(c);
    		
    	int width  = UIManager.getInt("ScrollBar.width");
    	int height = width * 3;
		return (scrollbar.getOrientation() == JScrollBar.VERTICAL)
		    ? new Dimension(width,  height)
	    	: new Dimension(height, width);
    }

	
}