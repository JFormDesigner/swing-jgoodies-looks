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

import javax.swing.UIDefaults;
import javax.swing.plaf.FontUIResource;


/**
 * Provides predefined FontChoicePolicy implementations.<p>
 * 
 * <strong>Note:</strong> The available policies work well on Windows.
 * On other platforms the fonts specified by the runtime environment
 * are chosen. I plan to provide more logic or options for other platforms,
 * for example that a Linux system checks for a Tahoma or Segoe UI.<p>
 * 
 * TODO: Add a check for a custom font choice policy set in the
 * System properties.<p>
 * 
 * TODO: Add policies that emulate different Windows setups:
 * default XP on 96dpi with normal fonts ("XP-normal-96"),
 * Vista on 120dpi with large fonts ("Vista-large-120"), etc.
 *
 * @author  Karsten Lentzsch
 * @version $Revision: 1.7 $
 * 
 * @see     FontChoicePolicy
 * @see     FontSet
 * @see     FontSets
 * 
 * @since 2.0
 */ 
public final class FontChoicePolicies {


    private FontChoicePolicies() {
        // Override default constructor; prevents instantation.
    }
    
    
    // Getting a FontChoicePolicy *********************************************
    
    /**
     * Returns a font choice policy that checks for a custom FontChoicePolicy
     * and a custom FontSet specified in the System settings or UIManager.
     * If no custom settings are available, the given default policy will
     * be used to look up the FontSet. 
     * 
     * @param defaultPolicy   the policy used if there are no custom settings  
     * @return a FontChoicePolicy that checks for custom settings
     *     before the default policy is returned.
     */
    public static FontChoicePolicy customSettingsPolicy(FontChoicePolicy defaultPolicy) {
        return new CustomSettingsPolicy(defaultPolicy);
    }
    
    
//    /**
//     * Returns the default platform independent font choice policy.<p>
//     * 
//     * The current implementation just returns the logical fonts.
//     * A future version shall check for available good fonts
//     * and shall use them before it falls back to the logical fonts.
//     * 
//     * @return the default platform independent font choice policy.
//     */
//    public static FontChoicePolicy getDefaultCrossPlatformPolicy() {
//        return new DefaultCrossPlatformPolicy();
//    }
    
    
    /**
     * Returns the default font choice policy for Plastic on the Windows platform.
     * It differs from the default Windows policy in that it uses a bold font
     * for TitledBorders, titles, and titled separators.
     * 
     * @return the default font choice policy for Plastic on the Windows platform.
     */
    public static FontChoicePolicy getDefaultPlasticOnWindowsPolicy() {
        return new DefaultPlasticOnWindowsPolicy();
    }
    
    
    /**
     * Returns the default Plastic FontChoicePolicy that may vary 
     * with the platform and environment. 
     * On Windows, the PlasticOnWindowsPolicy is returned that
     * is much like the defualt WindowsPolicy but uses a bold title font.
     * On other Platforms, the logical fonts policy is returned
     * that uses the logical fonts as specified by the Java runtime environment.
     * 
     * @return a Windows-like policy on Windows, a logical fonts policy 
     *    on all other platforms
     */
    public static FontChoicePolicy getDefaultPlasticPolicy() {
        if (LookUtils.IS_OS_WINDOWS) {
            return getDefaultPlasticOnWindowsPolicy();
        } else {
            return getLogicalFontsPolicy();
//            return getDefaultCrossPlatformPolicy();
        }
    }
    
    
    /**
     * Returns the default font choice policy for the Windows platform.
     * It aims to return a FontSet that is close to the native guidelines
     * and useful for the current Java environment.<p>
     * 
     * The control font scales with the platform screen resolution 
     * (96dpi/101dpi/120dpi/144dpi/...) and honors the desktop font settings
     * (normal/large/extra large).
     * 
     * @return the default font choice policy for the Windows platform.
     */
    public static FontChoicePolicy getDefaultWindowsPolicy() {
        return new DefaultWindowsPolicy();
    }
    
    
    /**
     * Returns a font choice policy that returns the specified FontSet.
     * 
     * @param fontSet   the FontSet to be return by this policy
     * @return a font choice policy that returns the specified FontSet.
     */
    public static FontChoicePolicy getFixedFontSetPolicy(FontSet fontSet) {
        return new FixedFontSetPolicy(fontSet);
    }
    
    
    /**
     * Returns a font choice policy that returns the logical fonts
     * as specified by the Java runtime environment.
     * 
     * @return a font choice policy that returns logical fonts.
     */
    public static FontChoicePolicy getLogicalFontsPolicy() {
        return new FixedFontSetPolicy(FontSets.getLogicalFontSet());
    }
    
    
    /**
     * Returns a font choice policy that returns the logical fonts
     * as specified by the Java runtime environment.
     * 
     * @return a font choice policy that returns logical fonts.
     */
    public static FontChoicePolicy getPlasticLooks13Policy() {
        if (LookUtils.IS_OS_WINDOWS) {
            return getDefaultPlasticOnWindowsPolicy();
        } 
        int tahomaFontSize = LookUtils.IS_LOW_RESOLUTION ? 11 : 13;
        Font tahoma = new Font("Tahoma", Font.PLAIN, tahomaFontSize);
        FontSet fontSet = FontSets.createDefaultFontSet(tahoma);
        return new FixedFontSetPolicy(fontSet);
    }
    
    
    // Utility Methods ********************************************************
    
    /**
     * Looks up and returns a custom FontSet for the given 
     * Look&amp;Feel name, or <code>null</code> if no custom font set 
     * has been defined for this Look&amp;Feel.
     * 
     * @param the name of the Look&amp;Feel, one of <code>"Plastic"</code> or
     *     <code>"Windows"</code>
     * @return a custom FontChoicePolicy - if any - or otherwise <code>null</code>
     */
    private static FontSet getCustomFontSet(String lafName) {
        String controlFontKey = lafName + ".controlFont";
        String menuFontKey    = lafName + ".menuFont";
        String decodedControlFont = LookUtils.getSystemProperty(controlFontKey);
        if (decodedControlFont == null) 
            return null;
        FontUIResource controlFont = new FontUIResource(Font.decode(decodedControlFont));
        String decodedMenuFont = LookUtils.getSystemProperty(menuFontKey);
        FontUIResource menuFont = decodedMenuFont == null
            ? null 
            : new FontUIResource(Font.decode(decodedMenuFont));
        return FontSets.createDefaultFontSet(controlFont, menuFont);
    }


    /**
     * Looks up and returns a custom FontChoicePolicy for the given 
     * Look&amp;Feel name, or <code>null</code> if no custom policy has been 
     * defined for this Look&amp;Feel.
     * 
     * @param the name of the Look&amp;Feel, one of <code>"Plastic"</code> or
     *     <code>"Windows"</code>
     * @return a custom FontChoicePolicy - if any - or otherwise <code>null</code>
     */
    private static FontChoicePolicy getCustomPolicy(String lafName) {
        // TODO: Look up predefined font choice policies
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
    
    
    private static final class CustomSettingsPolicy implements FontChoicePolicy {
        
        private final FontChoicePolicy wrappedPolicy;
        
        CustomSettingsPolicy(FontChoicePolicy wrappedPolicy) {
            this.wrappedPolicy = wrappedPolicy;
        }
        
        public FontSet getFontSet(String lafName, UIDefaults table) {
            FontChoicePolicy customPolicy = getCustomPolicy(lafName);
            if (customPolicy != null) {
                return customPolicy.getFontSet(null, table);
            }
            FontSet customFontSet = getCustomFontSet(lafName);
            if (customFontSet != null) {
                return customFontSet;
            }
            return wrappedPolicy.getFontSet(null, table);
        }
    }
    
    
//    private static final class DefaultCrossPlatformPolicy implements FontChoicePolicy {
//        
//        public FontSet getFontSet(String lafName, UIDefaults table) {
//            // TODO: If Tahoma or Segoe UI is available, return them
//            // in a size appropriate for the screen resolution.
//            // Otherwise return the logical font set.
//            return FontSets.getLogicalFontSet();
//        }
//    }
    
    
    private static final class DefaultPlasticOnWindowsPolicy implements FontChoicePolicy {
        
        public FontSet getFontSet(String lafName, UIDefaults table) {
            FontUIResource controlFont = new FontUIResource(FontChoicePolicies.getWindowsControlFont());
            
            FontUIResource menuFont = table == null
                ? controlFont
                : (FontUIResource) table.getFont("Menu.font");

            FontUIResource titleFont = new FontUIResource(controlFont.deriveFont(Font.BOLD));
            FontUIResource smallFont = new FontUIResource(controlFont.deriveFont(controlFont.getSize() - 2));
            FontUIResource messageFont = controlFont;
            FontUIResource windowTitleFont = titleFont;
            return FontSets.createDefaultFontSet(
                    controlFont, 
                    menuFont,
                    titleFont, 
                    messageFont,
                    smallFont, 
                    windowTitleFont);  
        }
    }
    

    private static final class DefaultWindowsPolicy implements FontChoicePolicy {
        
        public FontSet getFontSet(String lafName, UIDefaults table) {
            FontUIResource controlFont = new FontUIResource(FontChoicePolicies.getWindowsControlFont());
            
            FontUIResource menuFont = table == null
                ? controlFont
                : (FontUIResource) table.getFont("Menu.font");

            FontUIResource titleFont = controlFont; 
            
            FontUIResource messageFont = table == null
                ? controlFont 
                : (FontUIResource) table.getFont("OptionPane.font");
            FontUIResource smallFont = table == null
                ? new FontUIResource(controlFont.deriveFont(controlFont.getSize() - 2))
                : (FontUIResource) table.getFont("ToolTip.font");
            FontUIResource windowTitleFont  = table == null
                ? controlFont
                : (FontUIResource) table.getFont("InternalFrame.titleFont");
            return FontSets.createDefaultFontSet(
                    controlFont, 
                    menuFont,
                    titleFont, 
                    messageFont, 
                    smallFont, 
                    windowTitleFont);
        }
    }
    

    private static final class FixedFontSetPolicy implements FontChoicePolicy {
        
        private final FontSet fontSet;
        
        FixedFontSetPolicy(FontSet fontSet) {
            this.fontSet = fontSet;
        }
        
        public FontSet getFontSet(String lafName, UIDefaults table) {
            return fontSet;
        }
    }
    

}
