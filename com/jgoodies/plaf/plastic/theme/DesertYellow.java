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
 * A theme with low saturated yellow primary colors and a light brown background.
 *
 * @author Karsten Lentzsch
 */
public class DesertYellow extends DesertBlue {

    public String getName() {
        return "Desert Yellow";
    }

    protected ColorUIResource getPrimary2() {
        return Colors.YELLOW_LOW_MEDIUM;
    }
    
    protected ColorUIResource getPrimary3() {
        return Colors.YELLOW_LOW_LIGHTEST;
    }

    public ColorUIResource getTitleTextColor() {
        return Colors.GRAY_DARKER;
    }

    public ColorUIResource getMenuItemSelectedBackground() {
        return Colors.YELLOW_LOW_MEDIUMDARK;
    }

    public void addCustomEntriesToTable(UIDefaults table) {
        super.addCustomEntriesToTable(table);
        Object[] uiDefaults =
            {
                "ScrollBar.is3DEnabled",
                Boolean.TRUE,
                PlasticScrollBarUI.MAX_BUMPS_WIDTH_KEY,
                new Integer(30),
                };
        table.putDefaults(uiDefaults);
    }

}