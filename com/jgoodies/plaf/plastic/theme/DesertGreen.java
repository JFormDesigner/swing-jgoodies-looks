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
 * A theme with low saturated green primary colors and a light brown background.
 *
 * @author Karsten Lentzsch
 */
public class DesertGreen extends DesertBlue {

    public String getName() {
        return "Desert Green";
    }

    protected ColorUIResource getPrimary2() {
        return Colors.GREEN_LOW_MEDIUM;
    }
    
    protected ColorUIResource getPrimary3() {
        return Colors.GREEN_LOW_LIGHTEST;
    }

    public ColorUIResource getTitleTextColor() {
        return Colors.GRAY_DARKER;
    }

}