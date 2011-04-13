/*
 * Copyright (c) 2001-2011 JGoodies Karsten Lentzsch. All Rights Reserved.
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

import java.awt.Color;
import java.awt.Component;
import java.awt.Toolkit;
import java.util.Collections;
import java.util.List;

import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.jgoodies.common.base.SystemUtils;
import com.jgoodies.looks.plastic.PlasticLookAndFeel;
import com.jgoodies.looks.plastic.PlasticTheme;

/**
 * Provides convenience behavior used by the JGoodies Looks.
 *
 * @author  Karsten Lentzsch
 * @version $Revision: 1.18 $
 */
public final class LookUtils extends SystemUtils {

    // Basics System Properties **********************************************

    /**
     * True if this is Windows 98/ME/2000/Server 2003/XP/VISTA/Server 2008.
     */
    public static final boolean IS_OS_WINDOWS_MODERN =
        startsWith(OS_NAME, "Windows") && !startsWith(OS_VERSION, "4.0");

    /**
     * True if this is Windows 95.
     *
     * @since 2.0
     */
    public static final boolean IS_OS_WINDOWS_95 =
        startsWith(OS_NAME, "Windows 9") && startsWith(OS_VERSION, "4.0");

    /**
     * True if this is Windows NT.
     *
     * @since 2.0
     */
    public static final boolean IS_OS_WINDOWS_NT =
        startsWith(OS_NAME, "Windows NT");

    /**
     * True if the Windows XP Look&amp;Feel is enabled.
     *
     * @deprecated Replaced by {@link SystemUtils.IS_LAF_WINDOWS_XP_ENABLED}
     */
    @Deprecated
    public static final boolean IS_LAF_WINDOWS_XP_ENABLED =
        SystemUtils.IS_LAF_WINDOWS_XP_ENABLED;

    /**
     * True if if the screen resolution is smaller than 120 dpi.
     *
     * @see Toolkit#getScreenResolution()
     *
     * @deprecated Replaced by {@link SystemUtils.IS_LOW_RESOLUTION}
     */
    @Deprecated
    public static final boolean IS_LOW_RESOLUTION =
        SystemUtils.IS_LOW_RESOLUTION;

    private static boolean loggingEnabled = true;


    private LookUtils() {
        // Override default constructor; prevents instantiation.
    }


    // Accessing System Configuration *****************************************

    /**
     * Tries to look up the System property for the given key.
     * In untrusted environments this may throw a SecurityException.
     * In this case we catch the exception and answer {@code null}.
     *
     * @param key   the name of the system property
     * @return the system property's String value, or {@code null} if there's
     *     no such value, or a SecurityException has been caught
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
     *     if there's no such value, or a SecurityException has been caught
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
     * Checks if a boolean system property has been set for the given key,
     * and returns the associated Boolean, or {@code null} if no value
     * has been set. The test for the property ignores case.
     * If a Boolean value has been set, a message is logged
     * with the given prefix.
     *
     * @param key          the key used to lookup the system property value
     * @param logMessage   a prefix used when a message is logged
     * @return {@code Boolean.TRUE} if the system property has been set to
     * "true" (case ignored), {@code Boolean.FALSE} if it has been set to
     * "false", {@code null} otherwise
     */
    public static Boolean getBooleanSystemProperty(String key, String logMessage) {
        String value = getSystemProperty(key, "");
        Boolean result;
        if (value.equalsIgnoreCase("false")) {
            result = Boolean.FALSE;
        } else if (value.equalsIgnoreCase("true")) {
            result = Boolean.TRUE;
        } else {
            result = null;
        }
        if (result != null) {
            LookUtils.log(
                logMessage
                    + " have been "
                    + (result.booleanValue() ? "en" : "dis")
                    + "abled in the system properties.");
        }
        return result;
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


    /**
     * Checks and answers whether this toolkit provides native drop shadows
     * for popups such as the Mac OS X. Currently this is used to
     * determine if the Looks' popup drop shadow feature is active or not
     * - even if it's enabled.
     *
     * @return true if the toolkit provides native drop shadows
     *
     * @see Options#isPopupDropShadowActive()
     */
    public static boolean getToolkitUsesNativeDropShadows() {
        return IS_OS_MAC;
    }


    /**
     * Computes and returns a Color that is slightly brighter
     * than the specified Color.
     *
     * @param color   the color used as basis for the brightened color
     * @return a slightly brighter color
     */
    public static Color getSlightlyBrighter(Color color) {
        return getSlightlyBrighter(color, 1.1f);
    }

    /**
     * Computes and returns a Color that is slightly brighter
     * than the specified Color.
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
        if (laf instanceof PlasticLookAndFeel
            && theme != null
            && theme instanceof PlasticTheme) {
            PlasticLookAndFeel.setPlasticTheme((PlasticTheme) theme);
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
     * Enables or disables the Looks logging.
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


}
