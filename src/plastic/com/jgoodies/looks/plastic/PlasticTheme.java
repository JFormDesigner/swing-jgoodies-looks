/*
 * Copyright (c) 2001-2005 JGoodies Karsten Lentzsch. All Rights Reserved.
 *
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 * 
 *  o Redistributions of source code must retain the above copyright notice, 
 *    this list of conditions and the following disclaimer. 
 *     
 *  o Redistributions in binary form must reproduce the above copyright notice, 
 *    this list of conditions and the following disclaimer in the documentation 
 *    and/or other materials provided with the distribution. 
 *     
 *  o Neither the name of JGoodies Karsten Lentzsch nor the names of 
 *    its contributors may be used to endorse or promote products derived 
 *    from this software without specific prior written permission. 
 *     
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, 
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR 
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR 
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, 
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, 
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; 
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, 
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE 
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, 
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. 
 */

package com.jgoodies.looks.plastic;

import java.awt.Color;
import java.awt.Font;

import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.FontUIResource;
import javax.swing.plaf.metal.DefaultMetalTheme;

/**
 * Unlike its superclass this theme class has relaxed access.
 * 
 * @author  Karsten Lentzsch
 * @version $Revision: 1.3 $
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

    public FontUIResource getTitleTextFont() {
//        return getControlTextFont();
        
        if (titleFont == null) {
            titleFont =
                new FontUIResource(
                    Font.getFont(
                        "swing.plaf.metal.controlFont",
                        new Font("Dialog", Font.BOLD, 12)));
        }
        return titleFont;
    }

    public FontUIResource getControlTextFont() {
        return getFont();
    }
    
    public FontUIResource getMenuTextFont() {
        return getFont();
    }
    
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
    
    
    // Custom Equals Implementation *****************************************

    /**
     * Plastic themes are equal if and only if their classes are the same.
     * 
     * @return true if this theme is equal to the given object 
     */
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null)
            return false;
        return getClass().equals(o.getClass());
    }

    
    /**
     * Returns this theme's hash code, the classes' hash code.
     * 
     * @return this theme's hash code
     */
    public int hashCode() {
        return getClass().hashCode();
    }
    

}