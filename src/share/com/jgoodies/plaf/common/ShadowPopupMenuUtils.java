/*
 * Copyright (c) 2004 JGoodies Karsten Lentzsch. All Rights Reserved.
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

package com.jgoodies.plaf.common;

import java.lang.reflect.Method;

import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JWindow;
import javax.swing.Popup;

/**
 * A helper class used to implements drop shadows for popup menus.  
 * Hooked into the creation of either the light weight or heavy weight 
 * <code>JPopupMenu</code> container. Either removes its opaqueness 
 * or creates a background snapshot to create the illusion that menus 
 * can shadow even parts of the screen outside the menu's window.  
 * Unfortunately this illusion isn't perfect.<p>
 * 
 * We need access to {@link javax.swing.Popup#getComponent()} and require
 * {@link java.lang.reflect.AccessibleObject#ACCESS_PERMISSION} to do so.
 * If this code is executed with some security manager set, it will fail
 * silently and drop shadow support is inactive.
 * 
 * @author Stefan Matthias Aust
 * @version $Revision: 1.1 $
 * 
 * @see com.jgoodies.plaf.common.ShadowPopupBorder
 */
public final class ShadowPopupMenuUtils {

    /**
     * Refers to <code>javax.swing.Popup#getComponent</code>
     * if the method lookup was permitted by the runtime.
     * If <code>null</code> <code>#getPopupWithShadow</code> just returns
     * the unmodified popup.
     */
	private static Method getComponentMethod;
    
    /**
     * Tries to access {@link javax.swing.Popup#getComponent()}. 
     */
	static {
		try {
			getComponentMethod = Popup.class.getDeclaredMethod("getComponent", null);
			getComponentMethod.setAccessible(true);
		} catch (Exception e) {
            // Likely we have no permission to access this method. Do nothing.
		}
	}

	/**
	 * Returns the Popup that will be responsible for displaying the JPopupMenu.
	 * Overwritten to fix the opaqueness of the component in the case of light weight
	 * menus and to make a background snapshot to simulate the shadows in the case of
	 * heavy weight menus. 
	 */
	public static Popup getPopupWithShadow(JPopupMenu popupMenu, Popup popup) {
		if (getComponentMethod != null) {
			try {
				final Object component = getComponentMethod.invoke(popup, null);
				if (component instanceof JPanel) {
					// It's a light weight menu. Remove the opaqueness from 
                    // both the component and the menu.
					((JPanel) component).setOpaque(false);
					popupMenu.setOpaque(false);
					ShadowPopupBorder.clearSnapshot();
				} else if (component instanceof JWindow) {
					// It's a heavy weight menu.  Let the border snapshot the 
                    // part of the screen that will shine through the drop shadow.
					ShadowPopupBorder.makeSnapshot((JWindow) component);
				} 
			} catch (Exception e) {
				// Something went wrong; stop trying.
				getComponentMethod = null;
			}
		}
		return popup;
	}
    
}
