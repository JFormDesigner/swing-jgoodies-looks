/*
 * Copyright (c) 2003 JGoodies Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of JGoodies Karsten Lentzsch.  
 * Use is subject to license terms.
 *
 */

package com.jgoodies.plaf.plastic.theme;

import java.awt.Font;

import javax.swing.plaf.FontUIResource;

import com.jgoodies.plaf.FontSizeHints;
import com.jgoodies.plaf.plastic.PlasticLookAndFeel;

/**
 * Unlike its superclass <code>SkyBluer</code>, this theme
 * tries to use the MS Tahoma font.
 *
 * @author Karsten Lentzsch
 */

public class SkyBluerTahoma extends SkyBluer {

    public String getName() {
        return "Sky Bluer - Tahoma";
    }

    protected Font getFont0() {
        FontSizeHints sizeHints = PlasticLookAndFeel.getFontSizeHints();
        return getFont0(sizeHints.controlFontSize());
    }

    protected Font getFont0(int size) {
        Font font = new Font("Tahoma", Font.PLAIN, size);
        return font != null ? font : new Font("Dialog", Font.PLAIN, size);
    }

    public FontUIResource getSubTextFont() {
        if (null == smallFont) {
            smallFont = new FontUIResource(getFont0(10));
        }
        return smallFont;
    }

    public FontUIResource getSystemTextFont() {
        return getFont();
    }
    
    public FontUIResource getUserTextFont() {
        return getFont();
    }
}