/*
 * Copyright (c) 2003 JGoodies Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of JGoodies Karsten Lentzsch.  
 * Use is subject to license terms.
 *
 */

package com.jgoodies.plaf.plastic.theme;

import javax.swing.plaf.ColorUIResource;

import com.jgoodies.plaf.plastic.PlasticLookAndFeel;

/**
 * A theme with metal blue primary colors and a light gray background.
 *
 * @author Karsten Lentzsch
 */

public class SkyKrupp extends AbstractSkyTheme {

    public String getName() {
        return "Sky Krupp";
    }

    private final ColorUIResource primary1 = new ColorUIResource(54, 54, 90);
    private final ColorUIResource primary2 = new ColorUIResource(156, 156, 178);
    private final ColorUIResource primary3 = new ColorUIResource(197, 197, 221);

    protected ColorUIResource getPrimary1() {
        return primary1;
    }
    protected ColorUIResource getPrimary2() {
        return primary2;
    }
    protected ColorUIResource getPrimary3() {
        return primary3;
    }

    public ColorUIResource getMenuItemSelectedBackground() {
        return getPrimary1();
    }
    public ColorUIResource getMenuItemSelectedForeground() {
        return getWhite();
    }
    public ColorUIResource getMenuSelectedBackground() {
        return getSecondary2();
    }

    public ColorUIResource getFocusColor() {
        return PlasticLookAndFeel.useHighContrastFocusColors
            ? Colors.ORANGE_FOCUS
            : Colors.GRAY_DARK;
    }

}
