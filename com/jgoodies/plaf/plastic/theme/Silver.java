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

/**
 * A completely gray or silver theme that has been optimized to work
 * in Windows XP environments.
 *
 * @author Karsten Lentzsch
 */
public class Silver extends ExperienceBlue {

    public String getName() {
        return "Silver";
    }

    protected ColorUIResource getPrimary1() {
        return Colors.GRAY_MEDIUM;
    }
    
    protected ColorUIResource getPrimary2() {
        return Colors.GRAY_MEDIUMLIGHT;
    }
    
    protected ColorUIResource getPrimary3() {
        return Colors.GRAY_LIGHTER2;
    }

    protected ColorUIResource getSecondary1() {
        return getPrimary1();
    }
    
    protected ColorUIResource getSecondary2() {
        return getPrimary2();
    }
    
    protected ColorUIResource getSecondary3() {
        return getPrimary3();
    }

    public ColorUIResource getFocusColor() {
        return PlasticLookAndFeel.useHighContrastFocusColors
            ? Colors.ORANGE_FOCUS
            : Colors.BLUE_MEDIUM_DARK;
    }

    public ColorUIResource getTitleTextColor() {
        return Colors.GRAY_DARKEST;
    }

    public void addCustomEntriesToTable(UIDefaults table) {
        super.addCustomEntriesToTable(table);
        Object[] uiDefaults =
            { PlasticScrollBarUI.MAX_BUMPS_WIDTH_KEY, new Integer(50), };
        table.putDefaults(uiDefaults);
    }

}