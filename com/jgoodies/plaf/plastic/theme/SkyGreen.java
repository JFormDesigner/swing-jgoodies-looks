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
 * A theme with low saturated green primary colors and a light gray background.
 *
 * @author Karsten Lentzsch
 */
public class SkyGreen extends AbstractSkyTheme {

    public String getName() {
        return "Sky Green";
    }

    protected ColorUIResource getPrimary2() {
        return Colors.GREEN_LOW_MEDIUM;
    }
    
    protected ColorUIResource getPrimary3() {
        return Colors.GREEN_LOW_LIGHTEST;
    }

}