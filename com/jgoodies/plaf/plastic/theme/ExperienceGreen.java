/*
 * Copyright (c) 2003 JGoodies Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of JGoodies Karsten Lentzsch.  
 * Use is subject to license terms.
 *
 */

package com.jgoodies.plaf.plastic.theme;

import javax.swing.plaf.ColorUIResource;

/**
 * A theme with low saturated green primary colors and a light brown background;
 * has been optimized to work with Windows XP default desktop settings.
 *
 * @author Karsten Lentzsch
 */
public class ExperienceGreen extends ExperienceBlue {

    public String getName() {
        return "Experience Green";
    }

    private static final ColorUIResource FOCUS =
        new ColorUIResource(245, 165, 16);

    protected ColorUIResource getPrimary1() {
        return Colors.GREEN_LOW_DARK;
    }
    
    protected ColorUIResource getPrimary2() {
        return Colors.GREEN_LOW_MEDIUM;
    }
    
    protected ColorUIResource getPrimary3() {
        return Colors.GREEN_LOW_LIGHTEST;
    }

    public ColorUIResource getFocusColor() {
        return FOCUS;
    }

}