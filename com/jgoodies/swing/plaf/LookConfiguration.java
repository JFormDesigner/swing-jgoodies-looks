package com.jgoodies.swing.plaf;

/*
 * Copyright (c) 2003 JGoodies Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of JGoodies Karsten Lentzsch.
 * Use is subject to license terms.
 *
 */

import javax.swing.LookAndFeel;

import com.jgoodies.plaf.LookUtils;
import com.jgoodies.util.logging.Level;
import com.jgoodies.util.logging.Logger;
import com.jgoodies.util.prefs.Preferences;

/**
 * Describes a Look and Feel with its optional color theme.
 * Future versions may support font hints and font size hints.
 *
 * @author Karsten Lentzsch
 * @see	LookAndFeel
 * @see	LookConfigurations
 * @see	Preferences
 * 
 */

final class LookConfiguration {

    private static final String THEME_KEY_PREFIX = "laf.theme.";
    private static final Logger LOGGER =
        Logger.getLogger("LookConfiguration");

    private final LookAndFeel laf;
    private final Object theme;
    private Object fontHints;
    private Object fontSizeHints;

    /**
     * Constructs a <code>LookConfiguration</code> for the specified 
     * <code>LookAndFeel</code>.
     */
    LookConfiguration(LookAndFeel laf) {
        this(laf, null);
    }

    /**
     * Constructs a <code>LookConfiguration</code> for the specified 
     * <code>LookAndFeel</code> and theme.
     */
    LookConfiguration(LookAndFeel laf, Object theme) {
        if (laf == null)
            throw new NullPointerException("LookAndFeel must not be null.");
        this.laf = laf;
        this.theme = theme != null ? theme : LookUtils.getDefaultTheme(laf);
    }

    /**
     * Constructs a <code>LookConfiguration</code> for the specified 
     * <code>LookAndFeel</code>; reads the theme from the <code>Preferences</code>.
     */
    static LookConfiguration restoreFrom(
        LookAndFeel laf,
        Preferences prefs) {
        return new LookConfiguration(laf, restoreThemeFrom(laf, prefs));
    }

    LookAndFeel lookAndFeel() {
        return laf;
    }
    
    Object getTheme() {
        return theme;
    }
    Object fontHints() {
        return fontHints;
    }
    
    Object fontSizeHints() {
        return fontSizeHints;
    }

    /**
     * Stores the configuration in the specified <code>Preferences</code>.
     */
    void storeIn(Preferences prefs) {
        LOGGER.config("Storing the LookConfiguration for " + laf);

        if (getTheme() != null) {
            prefs.put(themeKey(laf), themeClassName());
        }
    }

    /**
     * Restores the configuration for the specified <code>LookAndFeel</code>
     * from the given <code>Preferences</code>.
     */
    private static Object restoreThemeFrom(
        LookAndFeel laf,
        Preferences prefs) {
        String themeName = prefs.get(themeKey(laf), null);
        if (themeName != null) {
            LOGGER.config(
                "Restoring theme for " + laf.getName() + ": " + themeName);
        }
        return themeName == null ? null : createThemeInstance(themeName);
    }

    /**
     * Creates and answers a theme instance for the specified class name.
     * Return <code>null</code> in case of an instantiation exception.
     */
    private static Object createThemeInstance(String themeClassName) {
        try {
            Class theClass = Class.forName(themeClassName);
            return theClass.newInstance();
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Class name=" + themeClassName, e);
            return null;
        }
    }

    private static String themeKey(LookAndFeel laf) {
        return THEME_KEY_PREFIX + laf.getName();
    }

    private String lafClassName() {
        return laf.getClass().getName();
    }
    
    private String themeClassName() {
        return theme.getClass().getName();
    }

    // Custom Equals Implementation *****************************************

    public boolean equals(Object o) {
        if (!(o instanceof LookConfiguration))
            return false;

        LookConfiguration config = (LookConfiguration) o;
        return lafClassName().equals(config.lafClassName())
            && (((theme == null) && (config.theme == null))
                || ((theme != null) && (theme.equals(config.theme))));
    }

    public int hashCode() {
        return laf.hashCode();
    }

}