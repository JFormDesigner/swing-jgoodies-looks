/*
 * Copyright (c) 2003 JGoodies Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of JGoodies Karsten Lentzsch.  
 * Use is subject to license terms.
 *
 */

package com.jgoodies.plaf.plastic.theme;

import javax.swing.UIDefaults;
import javax.swing.plaf.ColorUIResource;

import com.jgoodies.plaf.plastic.PlasticScrollBarUI;

/**
 * A theme with pink foreground and light gray background colors.
 *
 * @author Karsten Lentzsch
 */
public class SkyPink extends AbstractSkyTheme {

    public String getName() {
        return "Sky Pink";
    }

    protected ColorUIResource getPrimary1() {
        return Colors.PINK_LOW_DARK;
    }
    
    protected ColorUIResource getPrimary2() {
        return Colors.PINK_LOW_MEDIUM;
    }
    
    protected ColorUIResource getPrimary3() {
        return Colors.PINK_LOW_LIGHTER;
    }

    public ColorUIResource getHighlightedTextColor() {
        return getControlTextColor();
    }

    public void addCustomEntriesToTable(UIDefaults table) {
        super.addCustomEntriesToTable(table);
        Object[] uiDefaults =
            { PlasticScrollBarUI.MAX_BUMPS_WIDTH_KEY, new Integer(30), };
        table.putDefaults(uiDefaults);
    }

}
