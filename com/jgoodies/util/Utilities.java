/*
 * Copyright (c) 2003 JGoodies Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of JGoodies Karsten Lentzsch.
 * Use is subject to license terms.
 *
 */

package com.jgoodies.util;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.PrintStream;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

import javax.swing.UIManager;

/**
 * Consists exclusively of static methods that provide
 * convenience behavior, e. g. determining the platform,
 * the current version, and a few other odds and ends.
 *
 * @author Karsten Lentzsch
 */

public final class Utilities {

    public static final boolean IS_130         = is130();
    public static final boolean IS_BEFORE_14   = isBefore14();
    public static final boolean IS_MAC         = isMac();
    public static final boolean IS_MAC_LOOK    = isMacLook();
    public static final boolean IS_WINDOWS     = isWindows();
    public static final boolean IS_WINDOWS2000 = isWindows2000();
    public static final boolean IS_WINDOWS_XP  = isWindowsXP();
    public static final boolean IS_LOW_RES     = isLowResolution();

    // Override default constructor; prevents instantiation.
    private Utilities() {
    }

    /**
     * Answers a string that has a maximum size and contains <i>...</i>
     * if the given string was too large to fit into the maximum size.
     */
    public static String centerClippedString(String str, int maxSize) {
        int length = str.length();
        if (length <= maxSize)
            return str;

        int headLength = maxSize / 2;
        int tailLength = maxSize - headLength - 3;
        String head = str.substring(0, headLength);
        String tail = str.substring(length - tailLength, length - 1);
        return head + "..." + tail;
    }

    /**
     * Dumps the given <code>Properties</code> to the specified <code>PrintStream</code>.
     */
    public static void dumpProperties(
        PrintStream stream,
        Properties properties) {
        for (Enumeration e = properties.propertyNames();
            e.hasMoreElements();
            ) {
            String key = (String) e.nextElement();
            if (key.equals("line.separator"))
                continue;
            stream.println(key + " = " + properties.getProperty(key));
        }
    }

    /**
     * Computes and answers the extended system properties.
     */
    public static Properties getExtendedSystemProperties() {
        Properties properties = (Properties) System.getProperties().clone();
        addSpecialProperties(properties);
        return properties;
    }

    /**
     * Sleeps for the given <code>milliseconds</code> and catches all exceptions.
     */
    public static void sleep(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
        }
    }

    /**
     * Sorts an <code>Enumeration</code> and answers the sorted Enumeration.
     */
    public static final Enumeration sort(Enumeration enum) {
        List list = new ArrayList(20);
        Enumeration e = enum;
        while (e.hasMoreElements())
            list.add(e.nextElement());
        Collections.sort(list);
        return Collections.enumeration(list);
    }

    /**
     * Checks and answer if a system exit is allowed.
     * For example, as a JBuilder plug-in you should not shutdown the system 
     * but silently end your application threads.
     */
    public static boolean systemExitAllowed() {
        return !"false".equals(
            System.getProperty("jgoodies.SystemExitAllowed"));
    }

    // Private Helper Methods ***********************************************************

    /**
     * Checks and answers whether this is a J2RE 1.3.0x
     */
    private static boolean is130() {
        return System.getProperty("java.version").startsWith("1.3.0");
    }

    /**
     * Checks and answers whether this is a J2RE 1.2 or 1.3.
     */
    private static boolean isBefore14() {
        String version = System.getProperty("java.version");
        return version.startsWith("1.2") || version.startsWith("1.3");
    }

    /**
     * Checks and answers whether we're on Mac OS.
     */
    private static boolean isMac() {
        return System.getProperty("os.name").startsWith("Mac OS");
    }

    /**
     * Checks and answers whether we're using a Mac OS Look.
     */
    private static boolean isMacLook() {
        return UIManager.getLookAndFeel().getDescription().startsWith(
            "The Mac");
    }

    /**
     * Checks and answers whether we're on Windows.
     */
    private static boolean isWindows() {
        return System.getProperty("os.name").startsWith("Windows");
    }

    /**
     * Checks and answers whether we're on Windows.
     */
    private static boolean isWindows2000() {
        return System.getProperty("os.name").startsWith("Windows 2000");
    }

    /**
     * Checks and answers if we are on Windows XP.
     */
    private static boolean isWindowsXP() {
        String osName = System.getProperty("os.name");
        String osVersion = System.getProperty("os.version");
        return osName.startsWith("Windows") && osVersion.startsWith("5.1");
    }

    /**
     * Checks and answers whether the screen is on low resolution.
     */
    private static boolean isLowResolution() {
        return Toolkit.getDefaultToolkit().getScreenResolution() < 120;
    }

    /**
     * Adds some properties the the specified <code>Properties</code>
     */
    private static void addSpecialProperties(Properties properties) {
        // Add AWT properties that we are interested in.
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        properties.put(
            "awt.screen.resolution",
            new Integer(toolkit.getScreenResolution()).toString());
        Dimension screenSize = toolkit.getScreenSize();
        properties.put(
            "awt.screen.size",
            screenSize.width + " x " + screenSize.height);
        properties.put(
            "awt.colormodel",
            toolkit.getColorModel().getClass().getName());

        // Add memory data
        long free = Runtime.getRuntime().freeMemory();
        long total = Runtime.getRuntime().totalMemory();
        long used = total - free;
        NumberFormat format = NumberFormat.getNumberInstance();
        properties.put("runtime.memory.used", format.format(used));
        properties.put("runtime.memory.free", format.format(free));
        properties.put("runtime.memory.total", format.format(total));
    }

}