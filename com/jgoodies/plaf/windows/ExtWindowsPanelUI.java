/*
 * Copyright (c) 2003 JGoodies Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of Karsten Lentzsch.  
 * Use is subject to license terms.
 *
 */

package com.jgoodies.plaf.windows;

import java.awt.Graphics;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicPanelUI;

import com.jgoodies.clearlook.ClearLookManager;
import com.jgoodies.clearlook.ClearLookUtils;


/**
 * The JGoodies Windows Look&amp;Feel implementation of <code>PanelUI</code>.<p>
 * 
 * Can replace obsolete or ugly <code>Border</code>s.
 * 
 * @author Karsten Lentzsch
 */
public final class ExtWindowsPanelUI extends BasicPanelUI {
		
    private static final ExtWindowsPanelUI INSTANCE = new ExtWindowsPanelUI();
    
    public static ComponentUI createUI(JComponent x) {
        return INSTANCE;
    }

    /**
     * Replaces the <code>Border</code> if appropriate, then updates.
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