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

import java.awt.Color;
import java.awt.Component;
import java.awt.Insets;
import java.awt.Toolkit;
import java.util.Collections;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.InsetsUIResource;
import javax.swing.plaf.UIResource;

import com.jgoodies.clearlook.ClearLookManager;
import com.jgoodies.plaf.plastic.PlasticLookAndFeel;
import com.jgoodies.plaf.plastic.PlasticTheme;

/**
 * Provides convenience behavior used by the JGoodies Looks.
 *
 * @author Karsten Lentzsch
 * @version $revision: $
 * @see com.jgoodies.plaf.FontUtils
 */

public final class LookUtils {
    
    // Basics System Properties **********************************************

    /**
     * The <code>java.version</code> System Property.<p>
     *
     * Defaults to <code>null</code> if the runtime does not have security 
     * access to read this property or the property does not exist.
     */
    private static final String JAVA_VERSION = getSystemProperty("java.version");    
    
    /**
     * The <code>os.name</code> System Property. Operating system name.<p>
     *
     * Defaults to <code>null</code> if the runtime does not have security 
     * access to read this property or the property does not exist.
     */
    private static final String OS_NAME = getSystemProperty("os.name");

    /**
     * The <code>os.version</code> System Property. Operating system version.<p>
     *
     * Defaults to <code>null</code> if the runtime does not have security 
     * access to read this property or the property does not exist.
     */
    private static final String OS_VERSION = getSystemProperty("os.version");

    
    // Requesting the Java Version ******************************************** 

    /**
     * True if this is Java 1.4.
     */
    public static final boolean IS_JAVA_1_4 = 
        startsWith(JAVA_VERSION, "1.4"); 
    
    /**
     * True if this is Java 1.4.0_*.
     */
    static final boolean IS_JAVA_1_4_0 = startsWith(JAVA_VERSION, "1.4.0");
    
    /**
     * True if this is Java 1.4.2 or later. Since we assume Java 1.4
     * we just check for 1.4.0 and 1.4.1.
     */
    public static final boolean IS_JAVA_1_4_2_OR_LATER = 
        !startsWith(JAVA_VERSION, "1.4.0") &&
        !startsWith(JAVA_VERSION, "1.4.1");
    
    
    // Requesting the Operating System Name ***********************************
    
    /**
     * True if this is FreeBSD.
     */
    public static final boolean IS_OS_FREEBSD = 
        startsWithIgnoreCase(OS_NAME, "FreeBSD");

    /**
     * True if this is Linux.
     */
    public static final boolean IS_OS_LINUX = 
        startsWithIgnoreCase(OS_NAME, "Linux");

    /**
     * True if this is OS/2.
     */
    public static final boolean IS_OS_OS2 = 
        startsWith(OS_NAME, "OS/2");

    /**
     * True if this is the Mac OS X.
     */
    public static final boolean IS_OS_MAC = 
        startsWith(OS_NAME, "Mac");

    /**
     * True if this is Windows.
     */
    public static final boolean IS_OS_WINDOWS = 
        startsWith(OS_NAME, "Windows");

    /**
     * True if this is Windows 98/ME/2000/XP.
     */
    public static final boolean IS_OS_WINDOWS_MODERN = 
        startsWith(OS_NAME, "Windows") && !startsWith(OS_VERSION, "4.0");

    /**
     * True if this is Windows XP.
     */
    public static final boolean IS_OS_WINDOWS_XP =
        startsWith(OS_NAME, "Windows") && startsWith(OS_VERSION, "5.1");
    
    /**
     * True if this is Solaris.
     */
    public static final boolean IS_OS_SOLARIS = 
        startsWith(OS_NAME, "Solaris");
    
    
    // Other Properties *******************************************************

    /**
     * True if the Windows XP Look&amp;Feel is enabled.
     */
    public static final boolean IS_LAF_WINDOWS_XP_ENABLED = isWindowsXPLafEnabled();
    
    /**
     * True if this is a NetBeans environment.
     */
    public static final boolean IS_NETBEANS;

    public static boolean isLowRes = isLowResolution();
    
    private static boolean loggingEnabled;
    
    static {
        loggingEnabled = true;
        IS_NETBEANS    = isNetBeans();
        if (IS_NETBEANS) {
            // Force the ClearLookManager to immediately log messages.
            ClearLookManager.getMode();
        }
    }
    

    private LookUtils() {
        // Override default constructor; prevents instantiation.
    }
    
    
    // Accessing System Configuration *****************************************

    /**
     * Tries to look up the System property for the given key.
     * In untrusted environments this may throw a SecurityException.
     * In this case we catch the exception and answer <code>null</code>. 
     * 
     * @param key   the name of the system property
     * @return the system property's String value, or <code>null</code> if there's
     *     no such value, or a SecurityException has been catched
     */
    public static String getSystemProperty(String key) {
        try {
            return System.getProperty(key);
        } catch (SecurityException e) {
            log("Can't read the System property " + key + ".");
            return null;
        }
    }

    /**
     * Tries to look up the System property for the given key.
     * In untrusted environments this may throw a SecurityException.
     * In this case, we catch the exception and answer the default value. 
     * 
     * @param key          the name of the system property
     * @param defaultValue the default value if no property exists.
     * @return the system property's String value, or the defaultValue 
     *     if there's no such value, or a SecurityException has been catched
     */
    public static String getSystemProperty(String key, String defaultValue) {
        try {
            return System.getProperty(key, defaultValue);
        } catch (SecurityException e) {
            log("Can't read the System property " + key + ".");
            return defaultValue;
        }
    }

    /**
     * Checks and answers whether the Windows XP style is enabled. 
     * This method is intended to be called only if a Windows look&feel
     * is about to be installed or already active in the UIManager.
     * The XP style of the Windows look&amp;feel is enabled by default on
     * Windows XP platforms since the J2SE 1.4.2; it can be disabled either 
     * in the Windows desktop as well as in the Java runtime by setting
     * a System property.<p>
     * 
     * First checks the platform, platform version and Java version. Then 
     * checks whether the desktop property <tt>win.xpstyle.themeActive</tt>
     * is set or not.
     * 
     * @return true if the Windows XP style is enabled
     */ 
    private static boolean isWindowsXPLafEnabled() {
        return IS_OS_WINDOWS_XP 
             && IS_JAVA_1_4_2_OR_LATER 
             && Boolean.TRUE.equals(Toolkit.getDefaultToolkit().
                     getDesktopProperty("win.xpstyle.themeActive"))
             && getSystemProperty("swing.noxp") == null;
    }
    
    
    /**
     * Checks and answers whether we have a true color system.
     * 
     * @param c   the component used to determine the toolkit
     * @return true if the component's toolkit has a pixel size >= 24
     */
    public static boolean isTrueColor(Component c) {
        return c.getToolkit().getColorModel().getPixelSize() >= 24;
    }
    
    
    // Working with Button Margins ******************************************

    /**
     * Installs a narrow margin, if property <code>isNarrow</code> has been set.
     * 
     * @param b    the button that shall get a narrow margin
     * @param propertyPrefix   the component type prefeix for the UIDefaults
     */
    public static void installNarrowMargin(
        AbstractButton b,
        String propertyPrefix) {
        Object value = b.getClientProperty(Options.IS_NARROW_KEY);
        boolean isNarrow = Boolean.TRUE.equals(value);
        String defaultsKey =
            propertyPrefix + (isNarrow ? "narrowMargin" : "margin");
        Insets insets = b.getMargin();
        if (insets == null || insets instanceof UIResource) {
            b.setMargin(UIManager.getInsets(defaultsKey));
        }
    }

    /**
     * Creates and answers the margin used by <code>JButton</code>
     * and <code>JToggleButton</code>. Honors the screen resolution 
     * and the global <code>isNarrowButtonsEnabled</code> property.<p>
     * 
     * Sun's L&F implementations use wide button margins.
     * @see Options#getUseNarrowButtons()
     * 
     * @param narrow    true to create a narrow margin, false for a wide margin
     * @return an Insets object used to create a button margin
     */
    public static Insets createButtonMargin(boolean narrow) {
        int pad = narrow || Options.getUseNarrowButtons() ? 4 : 14;
        return isLowRes
            ? new InsetsUIResource(2, pad, 1, pad)
            : new InsetsUIResource(3, pad, 3, pad);
    }

    // Color Modifications **************************************************

    /**
     * Computes and answers a <code>Color</code> that is slightly brighter
     * than the specified <code>Color</code>. Required for 1.3 only.
     * 
     * @param color   the color used as basis for the brightened color
     * @return a slightly brighter color
     */
    public static Color getSlightlyBrighter(Color color) {
        return getSlightlyBrighter(color, 1.1f);
    }

    /**
     * Computes and answers a <code>Color</code> that is slightly brighter
     * than the specified <code>Color</code>. Required for 1.3 only.
     * 
     * @param color   the color used as basis for the brightened color
     * @param factor  the factor used to compute the brightness
     * @return a slightly brighter color
     */
    public static Color getSlightlyBrighter(Color color, float factor) {
        float[] hsbValues = new float[3];
        Color.RGBtoHSB(
            color.getRed(),
            color.getGreen(),
            color.getBlue(),
            hsbValues);
        float hue = hsbValues[0];
        float saturation = hsbValues[1];
        float brightness = hsbValues[2];
        float newBrightness = Math.min(brightness * factor, 1.0f);
        return Color.getHSBColor(hue, saturation, newBrightness);
    }

    // Accessing Look, Theme, and Font Settings *****************************

    public static void setLookAndTheme(LookAndFeel laf, Object theme)
        throws UnsupportedLookAndFeelException {
        if ((laf instanceof PlasticLookAndFeel)
            && (theme != null)
            && (theme instanceof PlasticTheme)) {
            PlasticLookAndFeel.setMyCurrentTheme((PlasticTheme) theme);
        }
        UIManager.setLookAndFeel(laf);
    }

    public static Object getDefaultTheme(LookAndFeel laf) {
        return laf instanceof PlasticLookAndFeel
            ? PlasticLookAndFeel.createMyDefaultTheme()
            : null;
    }

    public static List getInstalledThemes(LookAndFeel laf) {
        return laf instanceof PlasticLookAndFeel
            ? PlasticLookAndFeel.getInstalledThemes()
            : Collections.EMPTY_LIST;
    }

    // Minimal logging ******************************************************

    /**
     * Enables or disables the logging.
     * 
     * @param enabled   true to enable logging, false to disable it
     */
    public static void setLoggingEnabled(boolean enabled) {
        loggingEnabled = enabled;
    }

    /**
     * Prints a new line to the console if logging is enabled.
     */
    public static void log() {
        if (loggingEnabled) {
            System.out.println();
        }
    }

    /**
     * Prints the given message to the console if logging is enabled.
     * 
     * @param message  the message to print
     */
    public static void log(String message) {
        if (loggingEnabled) {
            System.out.println("JGoodies Looks: " + message);
        }
    }

    // Private Helper Methods ***********************************************

    /**
     * Checks and answers whether the screen resolution is low or high.
     * Resolutions below 120 dpi are considere low, all others are high.
     * 
     * @return true if the screen resolution is smaller than 120 dpi
     */
    private static boolean isLowResolution() {
        return Toolkit.getDefaultToolkit().getScreenResolution() < 120;
    }

    /**
     * Checks and answers whether we are in the Netbeans IDE
     * by looking for a NetBeans buildnumber. 
     * 
     * @return true if NetBeans could be detected, false otherwise
     */
    private static boolean isNetBeans() {
        String property = getSystemProperty("netbeans.buildnumber");
        boolean hasNetBeansBuildNumber = 
             property != null && property.length() > 0;
        if (hasNetBeansBuildNumber) {
            log("NetBeans detected - dobry den!");
        }
        return hasNetBeansBuildNumber;
    }
    
    private static boolean startsWith(String str, String prefix) {
        return str != null && str.startsWith(prefix);
    }
    
    private static boolean startsWithIgnoreCase(String str, String prefix) {
        return str != null && str.toUpperCase().startsWith(prefix.toUpperCase());
    }
    
}