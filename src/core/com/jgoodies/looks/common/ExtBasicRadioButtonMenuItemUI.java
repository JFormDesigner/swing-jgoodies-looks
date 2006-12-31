/*
 * Copyright (c) 2001-2007 JGoodies Karsten Lentzsch. All Rights Reserved.
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

import java.awt.Point;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.MenuElement;
import javax.swing.MenuSelectionManager;
import javax.swing.plaf.ComponentUI;


/**
 * Renders aligned <code>JRadioButtonMenuItem</code>s.
 *
 * @author  Karsten Lentzsch
 * @version $Revision: 1.3 $
 */
public class ExtBasicRadioButtonMenuItemUI extends ExtBasicMenuItemUI {
	
	protected String getPropertyPrefix() { return "RadioButtonMenuItem"; }
	
	
	public static ComponentUI createUI(JComponent b) {
		return new ExtBasicRadioButtonMenuItemUI();
	}
	
	
	// RadioButtonMenuItems and CheckBoxMenuItems will override
	protected boolean iconBorderEnabled() { return true; }


	public void processMouseEvent(JMenuItem item, MouseEvent e, 
								   MenuElement[] path, MenuSelectionManager manager) {
		Point p = e.getPoint();
		if (p.x >= 0 && p.x < item.getWidth() && 
		    p.y >= 0 && p.y < item.getHeight()) {
			if (e.getID() == MouseEvent.MOUSE_RELEASED) {
				manager.clearSelectedPath();
				item.doClick(0);
				item.setArmed(false);
			} else
				manager.setSelectedPath(path);
		} else if (item.getModel().isArmed()) {
			MenuElement[] newPath = new MenuElement[path.length - 1];
			int i, c;
			for (i = 0, c = path.length - 1; i < c; i++)
				newPath[i] = path[i];
			manager.setSelectedPath(newPath);
		}
	}

}