/*
 * Copyright (c) 2003 Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of Karsten Lentzsch.  
 * Use is subject to license terms.
 *
 */

package com.jgoodies.plaf.plastic;

import java.awt.Color;
import java.awt.Font;

import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.FontUIResource;
import javax.swing.plaf.metal.DefaultMetalTheme;

/**
 * Unlike its superclass this theme class has relaxed access.
 * 
 * @author Karsten Lentzsch
 */
public abstract class PlasticTheme extends DefaultMetalTheme {

    // Default 3D Effect Colors *********************************************

    public static final Color DARKEN_START = new Color(0, 0, 0, 0);
    public static final Color DARKEN_STOP = new Color(0, 0, 0, 64);
    public static final Color LT_DARKEN_STOP = new Color(0, 0, 0, 32);
    public static final Color BRIGHTEN_START = new Color(255, 255, 255, 0);
    public static final Color BRIGHTEN_STOP = new Color(255, 255, 255, 128);
    public static final Color LT_BRIGHTEN_STOP = new Color(255, 255, 255, 64);

    protected static final ColorUIResource WHITE =
        new ColorUIResource(255, 255, 255);

    protected static final ColorUIResource BLACK = new ColorUIResource(0, 0, 0);

    protected FontUIResource titleFont;
    protected FontUIResource controlFont;
    protected FontUIResource systemFont;
    protected FontUIResource userFont;
    protected FontUIResource smallFont;

    // Accessing Colors *****************************************************

    protected ColorUIResource getBlack() {
        return BLACK;
    }

    protected ColorUIResource getWhite() {
        return WHITE;
    }

    public ColorUIResource getSystemTextColor() {
        return getControlInfo();
    }

    public ColorUIResource getTitleTextColor() {
        return getPrimary1();
    }

    public ColorUIResource getMenuForeground() {
        return getControlInfo();
    }

    public ColorUIResource getMenuItemBackground() {
        return getMenuBackground();
    }
    
    public ColorUIResource getMenuItemSelectedBackground() {
        return getMenuSelectedBackground();
    }

    public ColorUIResource getMenuItemSelectedForeground() {
        return getMenuSelectedForeground();
    }

    public ColorUIResource getSimpleInternalFrameForeground() {
        return getWhite();
    }

    public ColorUIResource getSimpleInternalFrameBackground() {
        return getPrimary1();
    }

    public ColorUIResource getToggleButtonCheckColor() {
        return getPrimary1();
    }

    // Accessing Fonts ******************************************************

    public FontUIResource getSubTextFont() {
        if (smallFont == null) {
            smallFont =
                new FontUIResource(
                    Font.getFont(
                        "swing.plaf.metal.smallFont",
                        new Font("Dialog", Font.PLAIN, 10)));
        }
        return smallFont;
    }

    public FontUIResource getTitleTextFont() {
        if (titleFont == null) {
            titleFont =
                new FontUIResource(
                    Font.getFont(
                        "swing.plaf.metal.controlFont",
                        new Font("Dialog", Font.BOLD, 12)));
        }
        return titleFont;
    }

    public FontUIResource getSystemTextFont() {
        if (systemFont == null) {
            systemFont =
                new FontUIResource(
                    Font.getFont(
                        "swing.plaf.metal.systemFont",
                        new Font("Dialog", Font.PLAIN, 12)));
        }
        return systemFont;
    }

    public FontUIResource getUserTextFont() {
        if (userFont == null) {
            userFont =
                new FontUIResource(
                    Font.getFont(
                        "swing.plaf.metal.userFont",
                        new Font("Dialog", Font.PLAIN, 12)));
        }
        return userFont;
    }

    public FontUIResource getControlTextFont() {
        return getFont();
    }
    
    public FontUIResource getMenuTextFont() {
        return getFont();
    }
    
    public FontUIResource getWindowTitleFont() {
        return getFont();
    }

    // Helper Code **********************************************************

    protected FontUIResource getFont() {
        if (null == controlFont)
            controlFont = new FontUIResource(getFont0());

        return controlFont;
    }

    protected Font getFont0() {
        Font font = Font.getFont("swing.plaf.metal.controlFont");
        return font != null
            ? font.deriveFont(Font.PLAIN)
            : new Font("Dialog", Font.PLAIN, 12);
    }

}