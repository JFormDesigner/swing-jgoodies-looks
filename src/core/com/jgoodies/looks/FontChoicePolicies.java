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

package com.jgoodies.looks;

import java.awt.Font;
import java.awt.Toolkit;

import javax.swing.UIDefaults;
import javax.swing.plaf.FontUIResource;

import com.jgoodies.looks.FontSets.LogicalFontSet;


/**
 * Provides or creates predefined FontChoicePolicy implementations.
 *
 * @author  Karsten Lentzsch
 * @version $Revision: 1.2 $
 * 
 * @see     FontChoicePolicy
 * 
 * @since 2.0
 */ 
public final class FontChoicePolicies {
	

	private FontChoicePolicies() {
        // Override default constructor; prevents instantation.
    }
    
    
    // Getting a FontChoicePolicy *********************************************
    
    public static FontChoicePolicy getDefaultPolicy() {
        FontChoicePolicy customPolicy = getCustomPolicy();
        if (customPolicy != null) {
            return customPolicy;
        }
        FontSet customFontSet = getCustomFontSet();
        if (customFontSet != null) {
            return new FixedFontSetPolicy(customFontSet);
        }
        if (LookUtils.IS_OS_WINDOWS) {
            return getDefaultWindowsPolicy();
        } else {
            return getDefaultCrossPlatformPolicy();
        }
    }
    
    
    public static FontChoicePolicy getDefaultWindowsPolicy() {
        return new DefaultWindowsPolicy();
    }
    
    
    public static FontChoicePolicy getDefaultCrossPlatformPolicy() {
        return new DefaultCrossPlatformPolicy();
    }
    
    
    public static FontChoicePolicy getLogicalFontsPolicy() {
        return new FixedFontSetPolicy(new FontSets.LogicalFontSet());
    }
    
    public static FontChoicePolicy getCustomFontSettingsPolicy(String keySuffix) {
        return null;
    }
    
    
    // Utility Methods ********************************************************
    
    
    private static FontChoicePolicy getCustomPolicy() {
        return null;
    }
    
    
    private static FontSet getCustomFontSet() {
        return null;
    }
	
	
    /**
     * Returns the Windows icon font - unless Java can't render it well. The 
     * icon title font scales with the resolution (96dpi, 101dpi, 120dpi, etc) 
     * and the desktop font size settings (normal, large, extra large).
     * Since Java 1.4 and Java 5 render the Windows Vista icon font
     * Segoe UI poorly, we return the default GUI font in these environments.
     *  
     * @return the Windows scalable control font - unless Java can't render it well
     */
    private static Font getWindowsControlFont() {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        String fontName = ((LookUtils.IS_JAVA_5 || LookUtils.IS_JAVA_1_4) && LookUtils.IS_OS_WINDOWS_VISTA)
            ? "win.defaultGUI.font"
            : "win.icon.font";
        return (Font) toolkit.getDesktopProperty(fontName);
    }


    // FontChoicePolicy Implementations ***************************************       

    private static final class FixedFontSetPolicy implements FontChoicePolicy {
        
        private final FontSet fontSet;
        
        FixedFontSetPolicy(FontSet fontSet) {
            this.fontSet = fontSet;
        }
        
        public FontSet getFontSet(UIDefaults table) {
            return fontSet;
        }
    }
    

    private static final class DefaultWindowsPolicy implements FontChoicePolicy {
        
        public FontSet getFontSet(UIDefaults table) {
            FontUIResource controlFont = new FontUIResource(FontChoicePolicies.getWindowsControlFont());
            
            // Derive a bold version of the control font.
            FontUIResource titleFont = new FontUIResource(controlFont.deriveFont(Font.BOLD));
            
            FontUIResource menuFont = table == null
                ? controlFont
                : (FontUIResource) table.getFont("Menu.font");
            FontUIResource messageFont = table == null
                ? controlFont 
                : (FontUIResource) table.getFont("OptionPane.font");
            FontUIResource smallFont = table == null
                ? new FontUIResource(controlFont.deriveFont(controlFont.getSize() - 2))
                : (FontUIResource) table.getFont("ToolTip.font");
            FontUIResource windowTitleFont  = table == null
                ? controlFont
                : (FontUIResource) table.getFont("InternalFrame.titleFont");
            return new FontSets.DefaultFontSet(
                    controlFont, 
                    menuFont,
                    titleFont, 
                    messageFont, 
                    smallFont, 
                    windowTitleFont);
        }
    }
    

    private static final class DefaultCrossPlatformPolicy implements FontChoicePolicy {
        
        public FontSet getFontSet(UIDefaults table) {
            // If Tahoma or Segoe UI is available, return them
            // in a size appropriate for the screen resolution.
            // Otherwise return the logical font set.
            return new LogicalFontSet();
        }
    }
    
    
}