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

import com.jgoodies.plaf.plastic.PlasticLookAndFeel;
import com.jgoodies.plaf.plastic.PlasticScrollBarUI;
import com.jgoodies.plaf.plastic.PlasticTheme;

/**
 * A theme with medium blue primary colors and a light gray background.
 *
 * @author Karsten Lentzsch
 */
public class SkyBluer extends PlasticTheme {

    public String getName() {
        return "Sky Bluer";
    }

    protected ColorUIResource getPrimary1() {
        return Colors.BLUE_MEDIUM_DARKEST;
    }
    
    protected ColorUIResource getPrimary2() {
        return Colors.BLUE_MEDIUM_MEDIUM;
    }
    
    protected ColorUIResource getPrimary3() {
        return Colors.BLUE_MEDIUM_LIGHTEST;
    }
    
    protected ColorUIResource getSecondary1() {
        return Colors.GRAY_MEDIUMDARK;
    }
    
    protected ColorUIResource getSecondary2() {
        return Colors.GRAY_LIGHT;
    }
    
    protected ColorUIResource getSecondary3() {
        return Colors.GRAY_LIGHTER;
    }

    public ColorUIResource getMenuItemSelectedBackground() {
        return getPrimary2();
    }
    
    public ColorUIResource getMenuItemSelectedForeground() {
        return getWhite();
    }
    
    public ColorUIResource getMenuSelectedBackground() {
        return getSecondary2();
    }

    public ColorUIResource getFocusColor() {
        return PlasticLookAndFeel.useHighContrastFocusColors
            ? Colors.YELLOW_FOCUS
            : super.getFocusColor();
    }

    /*
     * TODO: The following two lines are likely an improvement.
     *       However, they require a rewrite of the PlasticInternalFrameTitlePanel.
    public    ColorUIResource getWindowTitleBackground() 		{ return getPrimary1(); }
    public    ColorUIResource getWindowTitleForeground() 		{ return WHITE; 		}  
    */

    public void addCustomEntriesToTable(UIDefaults table) {
        super.addCustomEntriesToTable(table);
        Object[] uiDefaults =
            { PlasticScrollBarUI.MAX_BUMPS_WIDTH_KEY, new Integer(30), };
        table.putDefaults(uiDefaults);
    }

}
