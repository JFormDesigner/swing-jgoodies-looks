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

package com.jgoodies.plaf.plastic;

import java.awt.Graphics;

import javax.swing.JComponent;
import javax.swing.JSplitPane;
import javax.swing.border.Border;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;

import com.jgoodies.clearlook.ClearLookManager;


/**
 * The JGoodies Plastic Look and Feel implementation of
 * <code>SplitPaneUI</code>.<p>
 * 
 * It can replace obsolete <code>Border</code>s and uses a special divider,
 * that paints a single drag handle instead of many bumps.
 * 
 * @author Karsten Lentzsch
 */
public final class PlasticSplitPaneUI extends BasicSplitPaneUI {
	
	
	// Stores the original border, in case we replace it.
	private Border   storedBorder;

	// Have we already checked the parent container?
	private boolean hasCheckedBorderReplacement = false;
	
	
	public static ComponentUI createUI(JComponent x) {
		return new PlasticSplitPaneUI();
	}


	public BasicSplitPaneDivider createDefaultDivider() {
		return new PlasticSplitPaneDivider(this);
	}
	

	/**
	 * Replaces the scrollpane's <code>Border</code> if appropriate,
	 * then paints.
	 */
    public void paint(Graphics g, JComponent c) {
    	if (!hasCheckedBorderReplacement) {
    		storedBorder = ClearLookManager.replaceBorder((JSplitPane) c);
    		hasCheckedBorderReplacement = true;
    	}
   		super.paint(g, c);
    }
    

	/**
	 * Restores the original <code>Border</code>, in case we replaced it.
	 */
    protected void uninstallDefaults() {
    	if (storedBorder != null) {
    		splitPane.setBorder(storedBorder);
    	}
    	super.uninstallDefaults();
    }

	
}