/*
 * Copyright (c) 2001-2005 JGoodies Karsten Lentzsch. All Rights Reserved.
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

package com.jgoodies.looks.common;

import java.awt.*;

import javax.swing.ImageIcon;
import javax.swing.JWindow;
import javax.swing.border.Border;

/**
 * A border with a nice looking drop shadow, intended to be used
 * as the outer border of popup menus. Can snapshot and paint the
 * screen background if used with heavy-weight popup windows.
 * 
 * @author Stefan Matthias Aust
 * @author Karsten Lentzsch
 * @version $Revision: 1.2 $
 * 
 * @see com.jgoodies.looks.common.ShadowPopupMenuUtils
 * @see java.awt.Robot
 */
public final class ShadowPopupBorder implements Border {
    
    /**
     * The border's insets used if the shadow feature is active.
     * The drop shadow needs 5 pixels at the bottom and the right hand side. 
     */
    private static final Insets SHADOW_INSETS = new Insets(0, 0, 5, 5);
    
    /**
     * The border's insets used if the shadow feature is inactive.
     */
    private static final Insets EMPTY_INSETS = new Insets(0, 0, 0, 0);
    
	/**
	 * The singleton instance used to draw all borders.
	 */
	private static ShadowPopupBorder instance = new ShadowPopupBorder();

	/**
	 * In the case of heavy weight menus, hShadowBg and vShadowBg hold a snapshot
	 * of the screen background to simulate the drop shadow effect.  Due to the
	 * nature of popup menus, there's at most one popup menu visible at a time and
	 * so a pair of static variables is enough. 
	 */
	private static Image hShadowBg, vShadowBg;
	
	/**
	 * The drop shadow is created from a PNG image with 8 bit alpha channel.
	 */
	private static Image shadow
		= new ImageIcon(ShadowPopupBorder.class.getResource("shadow.png")).getImage();

    /**
     * Describes whether the drop shadow is active or inactive.
     * 
     * @see #setActive(boolean)
     */
    private static boolean active = true;
    
    // Instance Creation *****************************************************
    
	/**
	 * Returns the singleton instance used to draw all borders.
	 */
	public static ShadowPopupBorder getInstance() {
		return instance;
	}
    
    
    // API *******************************************************************

    /**
     * Answers whether the drop shadow feature is active or inactive.
     * 
     * @return true for active drop shadows, false for inactive
     * 
     * @see #setActive(boolean)
     */
    private static boolean isActive() {
        return active;
    }
    
    
    /**
     * Activates or deactivates the drop shadow feature.
     * 
     * @param b true to activate, false to deactivate drop shadows
     */
    public static void setActive(boolean b) {
        active = b;
    }

	/**
	 * The next time the border is drawn no background snaphot is used.  
	 */
	public static void clearSnapshot() {
		hShadowBg = vShadowBg = null;
	}
    

	/**
	 * Snapshots the background. The next time the border is drawn, this
	 * background will be used.<p>
     * 
     * Uses a robot on the default screen device to capture the screen region
     * under the drop shadow. Does <em>not</em> use the window's device, 
     * because that may be an outdated device (due to popup reuse) and 
     * the robot's origin seems to be adjusted with the default screen device.<p>
     * 
     * Unfortunately under certain circumstances we don't get the background 
     * but a previous menu, still on the screen. Use light weight menus 
     * to work around this limitation. 
	 */
	public static void makeSnapshot(JWindow window) {
		try {
            Robot robot = new Robot(); // uses the default screen device
			// In 1.5 the window has no bounds assigned yet.
            // Therefore we use the origin and the preferred size.
			int x = window.getX();
			int y = window.getY();
			Dimension dim = window.getPreferredSize();
			hShadowBg = robot.createScreenCapture(new Rectangle(x, y + dim.height - 5, dim.width, 5));
			vShadowBg = robot.createScreenCapture(new Rectangle(x + dim.width - 5, y,  5, dim.height - 5));
		} catch (AWTException e) {
			clearSnapshot();
		}
	}

	/**
	 * Returns whether or not the border is opaque.
	 * The drop shadow is obviously not opaque. 
	 */
	public boolean isBorderOpaque() {
		return false;
	}

	/**
	 * Paints the border for the specified component with the specified 
     * position and size. Does nothing if the drop shadow is inactive. 
	 */
	public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        if (!isActive())
            return;
        
		// fake drop shadow effect in case of heavy weight menus
		if (hShadowBg != null) {
			g.drawImage(hShadowBg, x, y + height - 5, c);
		}
		if (vShadowBg != null) {
			g.drawImage(vShadowBg, x + width - 5, y, c);
		}
		
		// draw drop shadow
		g.drawImage(shadow, x +  5, y + height - 5, x + 10, y + height, 0, 6, 5, 11, null, c);
		g.drawImage(shadow, x + 10, y + height - 5, x + width - 5, y + height, 5, 6, 6, 11, null, c);
		g.drawImage(shadow, x + width - 5, y + 5, x + width, y + 10, 6, 0, 11, 5, null, c);
		g.drawImage(shadow, x + width - 5, y + 10, x + width, y + height - 5, 6, 5, 11, 6, null, c);
		g.drawImage(shadow, x + width - 5, y + height - 5, x + width, y + height, 6, 6, 11, 11, null, c);
	}

	/**
	 * Returns the insets of the border. If the drop shadow feature is
     * inactive, empty insets are used.
	 */
	public Insets getBorderInsets(Component c) {
		return isActive() ? SHADOW_INSETS : EMPTY_INSETS;
	}
    
}
