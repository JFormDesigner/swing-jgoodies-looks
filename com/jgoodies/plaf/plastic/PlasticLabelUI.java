/*
 * Copyright (c) 2003 JGoodies Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of JGoodies Karsten Lentzsch.  
 * Use is subject to license terms.
 *
 */

package com.jgoodies.plaf.plastic;

import java.awt.Graphics;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.border.Border;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.metal.MetalLabelUI;

import com.jgoodies.clearlook.ClearLookManager;
import com.jgoodies.clearlook.ClearLookUtils;


/**
 * The JGoodies Plastic Look&amp;Feel implementation of <code>LabelUI</code>.
 * <p>
 * It differs from its superclass in that it can replace ugly <code>Border</code>s.
 * 
 * @author Karsten Lentzsch
 */
public final class PlasticLabelUI extends MetalLabelUI {
    
    private static final PlasticLabelUI INSTANCE = new PlasticLabelUI();
    
    public static ComponentUI createUI(JComponent x) {
        return INSTANCE;
    }

    /**
     * Replaces the <code>Border</code> if appropriate, then paints.
     */
    public void paint(Graphics g, JComponent c) {
        JLabel label = (JLabel) c;
        if (!ClearLookUtils.hasCheckedBorder(label)) {
            Border oldBorder = ClearLookManager.replaceBorder(label);
            ClearLookUtils.storeBorder(label, oldBorder);
        }
        super.paint(g, c);
    }

    /**
     * Restores the original <code>Border</code>, in case we have replaced it before.
     */
    protected void uninstallDefaults(JLabel label) {
        Border storedBorder = ClearLookUtils.getStoredBorder(label);
        if (storedBorder != null) {
            label.setBorder(storedBorder);
        }
        super.uninstallDefaults(label);
    }
	
}