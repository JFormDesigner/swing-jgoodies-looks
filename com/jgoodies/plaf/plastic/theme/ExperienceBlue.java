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
 * A theme with low saturated blue primary colors and a light brown background;
 * has been optimized to work with Windows XP default desktop settings.
 *
 * @author Karsten Lentzsch
 */
public class ExperienceBlue extends DesertBluer {

    public String getName() {
        return "Experience Blue";
    }

    private static final ColorUIResource secondary1 =
        new ColorUIResource(128, 128, 128);
        
    private static final ColorUIResource secondary2 =
        new ColorUIResource(189, 190, 176);
        
    private static final ColorUIResource secondary3 =
        new ColorUIResource(236, 233, 216);

    protected ColorUIResource getPrimary1() {
        return Colors.BLUE_MEDIUM_DARK;
    }
    
    protected ColorUIResource getPrimary2() {
        return Colors.BLUE_LOW_MEDIUM;
    }
    
    protected ColorUIResource getPrimary3() {
        return Colors.BLUE_LOW_LIGHTEST;
    }

    protected ColorUIResource getSecondary1() {
        return secondary1;
    }
    
    protected ColorUIResource getSecondary2() {
        return secondary2;
    }
    
    protected ColorUIResource getSecondary3() {
        return secondary3;
    }

    public ColorUIResource getFocusColor() {
        return Colors.ORANGE_FOCUS;
    }
    
    public ColorUIResource getPrimaryControlShadow() {
        return getPrimary3();
    }

    public ColorUIResource getMenuSelectedBackground() {
        return getPrimary1();
    }
    public ColorUIResource getMenuSelectedForeground() {
        return WHITE;
    }
    
    public ColorUIResource getMenuItemBackground() {
        return WHITE;
    }

    public ColorUIResource getToggleButtonCheckColor() {
        return Colors.GREEN_CHECK;
    }

    public void addCustomEntriesToTable(UIDefaults table) {
        super.addCustomEntriesToTable(table);
        Object[] uiDefaults =
            {
                "ScrollBar.thumbHighlight",
                getPrimaryControlHighlight(),
                PlasticScrollBarUI.MAX_BUMPS_WIDTH_KEY,
                new Integer(22),
                };
        table.putDefaults(uiDefaults);
    }

}