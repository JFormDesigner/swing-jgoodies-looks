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
 * A theme with low saturated blue primary colors and a light gray background.
 *
 * @author Karsten Lentzsch
 */
public abstract class AbstractSkyTheme extends SkyBluerTahoma {

    private static final ColorUIResource secondary2 =
        new ColorUIResource(164, 164, 164);
        
    private static final ColorUIResource secondary3 =
        new ColorUIResource(225, 225, 225);

    protected ColorUIResource getPrimary1() {
        return Colors.GRAY_DARK;
    }
    
    protected ColorUIResource getPrimary2() {
        return Colors.BLUE_LOW_MEDIUM;
    }
    
    protected ColorUIResource getPrimary3() {
        return Colors.BLUE_LOW_LIGHTEST;
    }

    protected ColorUIResource getSecondary1() {
        return Colors.GRAY_MEDIUM;
    }
    protected ColorUIResource getSecondary2() {
        return secondary2;
    }
    
    protected ColorUIResource getSecondary3() {
        return secondary3;
    }

    // Background
    public ColorUIResource getPrimaryControlShadow() {
        return getPrimary3();
    }
    
    public ColorUIResource getMenuItemSelectedBackground() {
        return getPrimary1();
    }

    public void addCustomEntriesToTable(UIDefaults table) {
        super.addCustomEntriesToTable(table);
        Object[] uiDefaults =
            {
                PlasticScrollBarUI.MAX_BUMPS_WIDTH_KEY,
                null,
                "ScrollBar.thumbHighlight",
                getPrimaryControlHighlight(),
                };
        table.putDefaults(uiDefaults);
    }

}