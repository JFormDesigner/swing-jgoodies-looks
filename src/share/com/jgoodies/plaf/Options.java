/*
 * Copyright (c) 2001-2004 JGoodies Karsten Lentzsch. All Rights Reserved.
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

package com.jgoodies.plaf;

import java.awt.Dimension;
import java.util.HashMap;
import java.util.Map;

import javax.swing.UIManager;

import com.jgoodies.clearlook.ClearLookMode;

/**
 * Provides access to several optional properties for the 
 * JGoodies L&amp;Fs, either by a key to the <code>UIDefaults</code> table
 * or via a method or both.
 * 
 * @author  Karsten Lentzsch
 * @version $Revision: 1.11 $
 */

public final class Options {

    // Look & Feel Names ****************************************************

    public static final String PLASTIC_NAME =
        "com.jgoodies.plaf.plastic.PlasticLookAndFeel";
        
    public static final String PLASTIC3D_NAME =
        "com.jgoodies.plaf.plastic.Plastic3DLookAndFeel";
        
    public static final String PLASTICXP_NAME =
        "com.jgoodies.plaf.plastic.PlasticXPLookAndFeel";
        
    public static final String EXT_WINDOWS_NAME =
        "com.jgoodies.plaf.windows.ExtWindowsLookAndFeel";
        
    public static final String DEFAULT_LOOK_NAME = 
        PLASTIC3D_NAME;

    /**
     * Holds a <code>Map</code> that enables the look&amp;feel replacement
     * mechanism to replace one look by another. 
     * Maps the original class names to their replacement class names.
     */
    private static final Map LAF_REPLACEMENTS;
    static {
        LAF_REPLACEMENTS = new HashMap();
        initializeDefaultReplacements();
    }
    

    // Keys for Overriding Font Settings ************************************

    public static final String MENU_FONT_KEY = 
        "jgoodies.menuFont";
        
    public static final String CONTROL_FONT_KEY = 
        "jgoodies.controlFont";
        
    public static final String FONT_SIZE_HINTS_KEY = 
        "jgoodies.fontSizeHints";
        
    public static final String USE_SYSTEM_FONTS_KEY =
        "swing.useSystemFontSettings";
        
    public static final String USE_SYSTEM_FONTS_APP_KEY =
        "Application.useSystemFontSettings";


    // Optional Global User Properties **************************************

    public static final String DEFAULT_ICON_SIZE_KEY =
        "jgoodies.defaultIconSize";
        
    public static final String USE_NARROW_BUTTONS_KEY =
        "jgoodies.useNarrowButtons";
        
    public static final String TAB_ICONS_ENABLED_KEY =
        "jgoodies.tabIconsEnabled";
        
    public static final String POPUP_DROP_SHADOW_ENABLED_KEY =
        "jgoodies.popupDropShadowEnabled";
        

    // ClearLook Properties *************************************************

    public static final String CLEAR_LOOK_MODE_KEY = 
        "ClearLook.mode";
        
    public static final String CLEAR_LOOK_POLICY_KEY = 
        "ClearLook.policy";
        
    public static final String CLEAR_LOOK_OFF = 
        ClearLookMode.OFF.getName();
        
    public static final String CLEAR_LOOK_ON = 
        ClearLookMode.ON.getName();
        
    public static final String CLEAR_LOOK_VERBOSE =
        ClearLookMode.VERBOSE.getName();
        
    public static final String CLEAR_LOOK_DEBUG = 
        ClearLookMode.DEBUG.getName();


    // Optional Client Properties *******************************************

    /** 
     * Hint that the button margin should be narrow.                   
     */
    public static final String IS_NARROW_KEY = "jgoodies.isNarrow";

    /** 
     * Hint that the scroll pane border should be etched.				
     */
    public static final String IS_ETCHED_KEY = "jgoodies.isEtched";

    /** 
     * Hint for the style: Single or Both, see <code>HeaderStyle</code>.
     */
    public static final String HEADER_STYLE_KEY = "jgoodies.headerStyle";

    /** 
     * Hint that the menu items in the menu have no icons.				
     */
    public static final String NO_ICONS_KEY = "jgoodies.noIcons";

    /** 
     * A client property key for <code>JTree</code>s.
     * Used with the angled and none style values.
     */
    public static final String TREE_LINE_STYLE_KEY = 
        "JTree.lineStyle";

    /** 
     * A client property value for <code>JTree</code>s
     * that indicates that lines shall be drawn.
     */
    public static final String TREE_LINE_STYLE_ANGLED_VALUE = 
        "Angled";

    /** 
     * A client property value for <code>JTree</code>s
     * that indicates that lines shall be hidden.
     */
    public static final String TREE_LINE_STYLE_NONE_VALUE   = 
        "None";

    /** 
     * A client property key for <code>JTabbedPane</code>s that indicates 
     * that no content border shall be painted. 
     * Supported by the Plastic look and feel family.
     * This effect will be achieved also if the EMBEDDED property is true.
     */
    public static final String NO_CONTENT_BORDER_KEY =
        "jgoodies.noContentBorder";

    /**
     * A client property key for <code>JTabbedPane</code>s that indicates
     * that tabs are painted with a special embedded appearance. 
     * Supported by the Plastic look and feel family.
     * This effect will be achieved also if the EMBEDDED property is true.
     */
    public static final String EMBEDDED_TABS_KEY = 
        "jgoodies.embeddedTabs";


    // Private ****************************************************************

    private static final Dimension DEFAULT_ICON_SIZE = 
        new Dimension(20, 20);

    private Options() {
        // Override default constructor; prevents instantiation.
    }


    // Accessing Options ******************************************************

    /**
     * Returns whether a hint is set in the <code>UIManager</code> 
     * that indicates, that a look&amp;feel may use the native system fonts.
     * 
     * @return true if the UIManager indicates that system fonts shall be used
     * @see #setUseSystemFonts(boolean)
     */
    public static boolean getUseSystemFonts() {
        return UIManager.get(USE_SYSTEM_FONTS_APP_KEY).equals(Boolean.TRUE);
    }

    /**
     * Sets a value in the <code>UIManager</code> to indicate, 
     * that a look&amp;feel may use the native system fonts.
     * 
     * @param useSystemFonts   true to enable system fonts in the UIManager
     * @see #getUseSystemFonts()
     */
    public static void setUseSystemFonts(boolean useSystemFonts) {
        UIManager.put(USE_SYSTEM_FONTS_APP_KEY, Boolean.valueOf(useSystemFonts));
    }

    /**
     * Returns the default icon size that is used in menus, menu items and
     * toolbars. Menu items that have no icon set are aligned using the default
     * icon dimensions.
     * 
     * @return the dimension of the default icon
     * @see #setDefaultIconSize(Dimension)
     */
    public static Dimension getDefaultIconSize() {
        Dimension size = UIManager.getDimension(DEFAULT_ICON_SIZE_KEY);
        return size == null ? DEFAULT_ICON_SIZE : size;
    }

    /**
     * Sets the default icon size.
     * 
     * @param defaultIconSize   the default icon size to set
     * @see #getDefaultIconSize()
     */
    public static void setDefaultIconSize(Dimension defaultIconSize) {
        UIManager.put(DEFAULT_ICON_SIZE_KEY, defaultIconSize);
    }

    /**
     * Returns the global <code>FontSizeHints</code>, can be overriden 
     * by look specific setting.
     * 
     * @return the gobally used FontSizeHints object
     * @see #setGlobalFontSizeHints(FontSizeHints)
     */
    public static FontSizeHints getGlobalFontSizeHints() {
        Object value = UIManager.get(FONT_SIZE_HINTS_KEY);
        if (value != null)
            return (FontSizeHints) value;

        String name = LookUtils.getSystemProperty(FONT_SIZE_HINTS_KEY, "");
        try {
            return FontSizeHints.valueOf(name);
        } catch (IllegalArgumentException e) {
            return FontSizeHints.DEFAULT;
        }
    }

    /**
     * Sets the global <code>FontSizeHints</code>.
     * 
     * @param hints   the FontSizeHints object to be used globally
     * @see #getGlobalFontSizeHints()
     */
    public static void setGlobalFontSizeHints(FontSizeHints hints) {
        UIManager.put(FONT_SIZE_HINTS_KEY, hints);
    }

    /**
     * Checks and answers if we shall use narrow button margins of 4 pixels.
     * Sun's L&F implementations use a much wider button margin of 14 pixels, 
     * which leads to good button minimum width in the typical case.<p>
     * 
     * Using narrow button margins can potentially cause compatibility issues, 
     * so this feature must be switched on programmatically.<p>
     * 
     * If you use narrow margin, you should take care of minimum button width,
     * either by the layout management or appropriate ButtonUI minimum widths.
     * 
     * @return true if all buttons shall use narrow margins
     * @see #setUseNarrowButtons(boolean)
     */
    public static boolean getUseNarrowButtons() {
        return UIManager.getBoolean(USE_NARROW_BUTTONS_KEY);
    }

    /**
     * Sets if we use narrow or standard button margins.
     * 
     * @param b   true to use narrow button margins globally
     * @see #getUseNarrowButtons()
     */
    public static void setUseNarrowButtons(boolean b) {
        UIManager.put(USE_NARROW_BUTTONS_KEY, Boolean.valueOf(b));
    }

    /**
     * Detects and answers if we shall use icons in <code>JTabbedPanes</code>.
     * This has an effect only inside NetBeans, it will answer 'yes'
     * if we are outside NetBeans.<p>
     * 
     * If the user has set a system property, we log a message 
     * about the choosen style.
     * 
     * @return true if icons in tabbed panes are enabled, false if disabled
     * @see #setTabIconsEnabled(boolean)
     */
    public static boolean isTabIconsEnabled() {
        if (!LookUtils.IS_NETBEANS)
            return true;

        String userMode = LookUtils.getSystemProperty(TAB_ICONS_ENABLED_KEY, "");
        boolean overridden = userMode.length() > 0;

        boolean result = overridden
                ? userMode.equalsIgnoreCase("true")
                : Boolean.TRUE.equals(UIManager.get(TAB_ICONS_ENABLED_KEY));

        if (overridden) {
            LookUtils.log(
                "You have "
                    + (result ? "en" : "dis")
                    + "abled icons in tabbed panes.");
        }
        return result;
    }

    /**
     * Enables or disables the use of icons in <code>JTabbedPane</code>s.
     * 
     * @param b   true to enable icons in tabbed panes, false to disable them
     * @see #isTabIconsEnabled()
     */
    public static void setTabIconsEnabled(boolean b) {
        UIManager.put(TAB_ICONS_ENABLED_KEY, Boolean.valueOf(b));
    }
    
    
    /**
     * Checks and answers whether popup drop shadows are active.
     * This feature shall be inactive on platforms that provide 
     * native drop shadows, such as the Mac OS X. Otherwise the feature's 
     * enablement state is returned.<p>
     * 
     * Currently only the Mac OS X is detected as platform with
     * native drop shadows. 
     * 
     * @return true if drop shadows are active, false if inactive
     * 
     * @see #isPopupDropShadowEnabled()
     * @see #setPopupDropShadowEnabled(boolean)
     */
    public static boolean isPopupDropShadowActive() {
        boolean platformProvidesNativeDropShadows =
            LookUtils.IS_OS_MAC;
        
        return !platformProvidesNativeDropShadows 
             && isPopupDropShadowEnabled(); 
    }

    /**
     * Checks and answers whether the optional drop shadows for 
     * <code>PopupMenus</code> are enabled or disabled.
     * If the user has set a system property, we log a message 
     * about the choosen style.<p>
     * 
     * This property just set the feature's enablement, not its actual 
     * activation. For example, drop shadows are always inactive on 
     * the Mac OS X, because this platform already provides shadows. 
     * The activation is requested in <code>#isPopupDropShadowActive</code>. 
     * 
     * @return true if drop shadows are enabled, false if disabled
     * 
     * @see #isPopupDropShadowActive()
     * @see #setPopupDropShadowEnabled(boolean)
     */
    public static boolean isPopupDropShadowEnabled() {
        String userMode = LookUtils.getSystemProperty(POPUP_DROP_SHADOW_ENABLED_KEY, "");
        boolean overridden = userMode.length() > 0;
        Object value = UIManager.get(POPUP_DROP_SHADOW_ENABLED_KEY);

        boolean result;
        if (overridden) {
            result = userMode.equalsIgnoreCase("true");
        } else {
            result = value == null
                ? isPopupDropShadowEnabledDefault()
                : Boolean.TRUE.equals(value);
        }

        if (overridden) {
            LookUtils.log(
                "You have "
                    + (result ? "en" : "dis")
                    + "abled drop shadows in popup menus.");
        }
        return result;
    }

    /**
     * Enables or disables drop shadows in <code>PopupMenu</code>s.
     * Note that drop shadows are always inactive on the Mac OS X.<p>
     * 
     * It is recommended to enable this feature only on platforms 
     * that accelerate translucency and snapshots with the hardware.<p>
     * 
     * <strong>Note:</strong> The current implementation fails 
     * to paint heavy-weight popup drop-shadows under some conditions. 
     * Before you enable drop shadows, you should make sure that 
     * your application won't get into these problems. 
     * Heavy weight popups fail if you display cascaded popups 
     * or pull-down menus. If you are using cascaded menus and
     * the application won't be executed in full-screen mode,
     * users may experience paint glitches.
     * 
     * @param b   true to enable drop shadows, false to disable them
     * 
     * @see #isPopupDropShadowActive()
     * @see #isPopupDropShadowEnabled()
     */
    public static void setPopupDropShadowEnabled(boolean b) {
        UIManager.put(POPUP_DROP_SHADOW_ENABLED_KEY, Boolean.valueOf(b));
    }
    
    /**
     * Checks and answers whether popup drop shadows are enabled
     * or disabled by default. Since the current implementation fails to
     * paint properly in all cases, it is disabled by default.
     * 
     * @return false
     */
    private static boolean isPopupDropShadowEnabledDefault() {
        return false;
        // return LookUtils.IS_OS_WINDOWS_MODERN;
    }


    // Look And Feel Replacements *******************************************

    /**
     * Puts a replacement name for a given <code>LookAndFeel</code> 
     * class name in the list of all look and feel replacements.
     * 
     * @param original   the name of the look-and-feel to replace
     * @param replacement   the name of the replacement look-and-feel
     * @see #removeLookAndFeelReplacement(String)
     * @see #getReplacementClassNameFor(String)
     */
    public static void putLookAndFeelReplacement(
        String original,
        String replacement) {
        LAF_REPLACEMENTS.put(original, replacement);
    }

    /**
     * Removes a replacement name for a given <code>LookAndFeel</code> 
     * class name from the list of all look and feel replacements.
     * 
     * @param original   the name of the look-and-feel that has been replaced
     * @see #putLookAndFeelReplacement(String, String)
     * @see #getReplacementClassNameFor(String)
     */
    public static void removeLookAndFeelReplacement(String original) {
        LAF_REPLACEMENTS.remove(original);
    }

    /**
     * Initializes some default class name replacements, that replace
     * Sun's Java look and feel, and Sun's Windows look and feel by
     * the appropriate JGoodies replacements.
     * 
     * @see #putLookAndFeelReplacement(String, String)
     * @see #removeLookAndFeelReplacement(String)
     * @see #getReplacementClassNameFor(String)
     */
    public static void initializeDefaultReplacements() {
        putLookAndFeelReplacement(
            "javax.swing.plaf.metal.MetalLookAndFeel",
            PLASTIC3D_NAME);
        putLookAndFeelReplacement(
            "com.sun.java.swing.plaf.windows.WindowsLookAndFeel",
            EXT_WINDOWS_NAME);
    }

    /**
     * Returns the class name that can be used to replace the specified
     * <code>LookAndFeel</code> class name.
     * 
     * @param className   the name of the look-and-feel class
     * @return the name of the suggested replacement class
     * @see #putLookAndFeelReplacement(String, String)
     * @see #removeLookAndFeelReplacement(String)
     * @see #initializeDefaultReplacements()
     */
    public static String getReplacementClassNameFor(String className) {
        String replacement = (String) LAF_REPLACEMENTS.get(className);
        return replacement == null ? className : replacement;
    }

    /**
     * Returns the class name for a cross-platform <code>LookAndFeel</code>.
     * 
     * @return the name of a cross platform look-and-feel class
     * @see #getSystemLookAndFeelClassName()
     */
    public static String getCrossPlatformLookAndFeelClassName() {
        return PLASTIC3D_NAME;
    }

    /**
     * Returns the class name for a system specific <code>LookAndFeel</code>.
     * 
     * @return the name of the system look-and-feel class
     * @see #getCrossPlatformLookAndFeelClassName()
     */
    public static String getSystemLookAndFeelClassName() {
        String osName = System.getProperty("os.name");
        if (osName.startsWith("Windows"))
            return Options.EXT_WINDOWS_NAME;
        else if (osName.startsWith("Mac"))
            return UIManager.getSystemLookAndFeelClassName();
        else
            return getCrossPlatformLookAndFeelClassName();
    }

}