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
 * A theme with yellow primary colors and a light gray background.
 *
 * @author Karsten Lentzsch
 */

public class SkyYellow extends AbstractSkyTheme {

    public String getName() {
        return "Sky Yellow";
    }

    protected ColorUIResource getPrimary1() {
        return Colors.GRAY_DARK;
    }
    
    protected ColorUIResource getPrimary2() {
        return Colors.YELLOW_LOW_MEDIUM;
    }
    
    protected ColorUIResource getPrimary3() {
        return Colors.YELLOW_LOW_LIGHTEST;
    }

    public ColorUIResource getFocusColor() {
        return Colors.ORANGE_FOCUS;
    }
    
    public ColorUIResource getPrimaryControlShadow() {
        return getPrimary3();
    }
    
    public ColorUIResource getMenuItemSelectedBackground() {
        return Colors.YELLOW_LOW_MEDIUMDARK;
    }

    public void addCustomEntriesToTable(UIDefaults table) {
        super.addCustomEntriesToTable(table);
        Object[] uiDefaults =
            { PlasticScrollBarUI.MAX_BUMPS_WIDTH_KEY, new Integer(30), };
        table.putDefaults(uiDefaults);
    }

}
