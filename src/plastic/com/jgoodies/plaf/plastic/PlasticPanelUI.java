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
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicPanelUI;

import com.jgoodies.clearlook.ClearLookManager;
import com.jgoodies.clearlook.ClearLookUtils;


/**
 * The JGoodies Plastic Look&amp;Feel implementation of <code>PanelUI</code>.
 * Uses ClearLook(tm) to replace visually redundant <code>Border</code>s.
 * 
 * @author Karsten Lentzsch
 */
public final class PlasticPanelUI extends BasicPanelUI {
	
	
    private static final PlasticPanelUI INSTANCE = new PlasticPanelUI();
    
    public static ComponentUI createUI(JComponent x) {
        return INSTANCE;
    }

	
    /**
     * Replaces the <code>Border</code> if appropriate, then paints.
     */
    public void update(Graphics g, JComponent c) {
        if (!ClearLookUtils.hasCheckedBorder(c)) {
            Border oldBorder = ClearLookManager.replaceBorder((JPanel) c);
            ClearLookUtils.storeBorder(c, oldBorder);
        }
        super.update(g, c);
    }

    /**
     * Restores the original <code>Border</code>, in case we have replaced it before.
     */
    protected void uninstallDefaults(JPanel panel) {
        Border storedBorder = ClearLookUtils.getStoredBorder(panel);
        if (storedBorder != null) {
            panel.setBorder(storedBorder);
        }
        super.uninstallDefaults(panel);
    }
    
}