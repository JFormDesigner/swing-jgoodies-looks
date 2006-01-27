/*
 * Copyright (c) 2001-2006 JGoodies Karsten Lentzsch. All Rights Reserved.
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

package com.jgoodies.looks;

import java.awt.Font;
import java.awt.Toolkit;


/**
 * Provides only static access to popular Windows fonts.
 *
 * @author  Karsten Lentzsch
 * @version $Revision: 1.3 $
 * 
 * @see     FontSet
 * @see     FontSets
 * @see     FontChoicePolicy
 * @see     FontChoicePolicies
 * 
 * @since 2.0
 */ 
public final class Fonts {
    
    /**
     * The name of the default dialog font on western Windows XP.
     */
    public static final String TAHOMA_NAME   = "Tahoma";
    
    /**
     * The name of the default dialog font on western Windows Vista.
     */
    public static final String SEGOE_UI_NAME = "Segoe UI";
    
    
    // Physical Fonts *********************************************************
    
    /**
     * This is the default font on western XP with 96dpi and normal fonts.
     * Ascent=11, descent=3, height=14, dbuX=6, dbuY=12, 14dluY=21px.
     */
    public static final Font TAHOMA_11PT   = new Font(TAHOMA_NAME, Font.PLAIN, 11);

    /**
     * Ascent=13, descent=3, height=16, dbuX=8, dbuY=13, 14dluY=22.75px.
     */
    public static final Font TAHOMA_13PT   = new Font(TAHOMA_NAME, Font.PLAIN, 13);

    /**
     * Ascent=14, descent=3, height=17, dbuX=8, dbuY=14, 14dluY=24.5px.
     */
    public static final Font TAHOMA_14PT   = new Font(TAHOMA_NAME, Font.PLAIN, 14);
    
    /**
     * This is Segoe UI 9pt, the default font on western Vista with 96dpi.
     * Ascent=13, descent=4, height=17, dbuX=7, dbuY=13, 13dluY=21.125px.
     */
    public static final Font SEGOE_UI_12PT = new Font(SEGOE_UI_NAME, Font.PLAIN, 12);

    /**
     * Ascent=14, descent=4, height=18, dbuX=8, dbuY=14, 13dluY=22.75px.
     */
    public static final Font SEGOE_UI_13PT = new Font(SEGOE_UI_NAME, Font.PLAIN, 13);

    /**
     * Ascent=16, descent=5, height=21, dbuX=9, dbuY=16, 13dluY=26px.
     */
    public static final Font SEGOE_UI_15PT = new Font(SEGOE_UI_NAME, Font.PLAIN, 15);
    
    
    // Default Windows Fonts **************************************************

    /**
     * The default icon font on western Windows XP with 96dpi
     * and the dialog font desktop setting "Normal".
     */
    public static final Font XP_96DPI_NORMAL = TAHOMA_11PT;

    /**
     * The default GUI font on western Windows XP with 96dpi
     * and the dialog font desktop setting "Normal".
     */
    public static final Font XP_96DPI_DEFAULT_GUI = TAHOMA_11PT;
    
    /**
     * The default icon font on western Windows XP with 96dpi
     * and the dialog font desktop setting "Large".
     */
    public static final Font XP_96DPI_LARGE = TAHOMA_13PT;
    
    /**
     * The default icon font on western Windows XP with 120dpi
     * and the dialog font desktop setting "Normal".
     */
    public static final Font XP_120DPI_NORMAL = TAHOMA_14PT;

    /**
     * The default GUI font on western Windows XP with 120dpi
     * and the dialog font desktop setting "Normal".
     */
    public static final Font XP_120DPI_DEFAULT_GUI = TAHOMA_13PT;
    
    /**
     * The default icon font on western Windows Vista with 96dpi
     * and the dialog font desktop setting "Normal".
     */
    public static final Font VISTA_96DPI_NORMAL = SEGOE_UI_12PT;

    /**
     * The default icon font on western Windows Vista with 96dpi
     * and the dialog font desktop setting "Large".
     */
    public static final Font VISTA_96DPI_LARGE = SEGOE_UI_15PT;

    /**
     * The default icon font on western Windows Vista with 101dpi
     * and the dialog font desktop setting "Normal".
     */
    public static final Font VISTA_101DPI_NORMAL = SEGOE_UI_13PT;
    
    /**
     * The default icon font on western Windows Vista with 120dpi
     * and the dialog font desktop setting "Normal".
     */
    public static final Font VISTA_120DPI_NORMAL = SEGOE_UI_15PT;
    
    
    // Instance Creation ******************************************************
    
    private Fonts() {
        // Override default constructor; prevents instantation.
    }
    
    
    // Font Lookup ************************************************************
    
    static Font getDefaultGUIFontWesternModernWindowsNormal() {
        return LookUtils.IS_LOW_RESOLUTION
            ? XP_96DPI_DEFAULT_GUI
            : XP_120DPI_DEFAULT_GUI;
    }
    
    static Font getDefaultIconFontWesternModernWindowsNormal() {
        return LookUtils.IS_LOW_RESOLUTION
            ? XP_96DPI_NORMAL
            : XP_120DPI_NORMAL;
    }
    
    static Font getDefaultIconFontWesternWindowsVistaNormal() {
        return LookUtils.IS_LOW_RESOLUTION
            ? VISTA_96DPI_NORMAL
            : VISTA_120DPI_NORMAL;
    }


    /**
     * Returns the Windows icon font - unless Java can't render it well. The 
     * icon title font scales with the resolution (96dpi, 101dpi, 120dpi, etc) 
     * and the desktop font size settings (normal, large, extra large).
     * Since Java 1.4 and Java 5 render the Windows Vista icon font
     * Segoe UI poorly, we return the default GUI font in these environments.
     *  
     * @return the Windows scalable control font - unless Java can't render it well
     * 
     * @throws UnsupportedOperationException on non-Windows platforms
     */
    static Font getWindowsControlFont() {
        if (!LookUtils.IS_OS_WINDOWS)
            throw new UnsupportedOperationException();
        
        String fontName = LookUtils.IS_JAVA_6_OR_LATER || !LookUtils.IS_OS_WINDOWS_VISTA
            ? "win.icon.font"
            : "win.defaultGUI.font";
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        return (Font) toolkit.getDesktopProperty(fontName);
    }
    
    
}
