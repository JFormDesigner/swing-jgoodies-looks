package com.jgoodies.swing.plaf;

/*
 * Copyright (c) 2003 JGoodies Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of JGoodies Karsten Lentzsch.
 * Use is subject to license terms.
 *
 */

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.LookAndFeel;
import javax.swing.UIManager;

import com.jgoodies.plaf.Options;
import com.jgoodies.util.Utilities;
import com.jgoodies.util.logging.Logger;
import com.jgoodies.util.prefs.Preferences;

/**
 * Describes the set of available instances of <code>LookConfiguration</code>
 * and provides a selected <code>LookAndFeel</code>.
 * 
 * @author Karsten Lentzsch
 * @see	LookAndFeel
 * @see	LookConfiguration
 * @see	Preferences
 */

final class LookConfigurations implements Cloneable {

    private static final Logger LOGGER =
        Logger.getLogger("LookConfigurations");
    private static final String LAF_CLASS_KEY = "laf.class";
    private static final String[] LOOK_NAMES =
        Utilities.IS_WINDOWS
            ? new String[] {
                Options.PLASTIC_NAME,
                Options.PLASTIC3D_NAME,
                Options.PLASTICXP_NAME,
                Options.EXT_WINDOWS_NAME }
            : new String[] {
                Options.PLASTIC_NAME,
                Options.PLASTICXP_NAME,
                Options.PLASTIC3D_NAME };

    private LookAndFeel selectedLook;

    /** 
     * Maps look and feel class names to instances of
     * <code>LookConfiguration</code>.
     */
    private Map configurations;

    // Instance Creation ****************************************************

    /**
     * Constructs an instance <code>LookConfigurations</code> with
     * the specified selection.
     */
    LookConfigurations(LookAndFeel selectedLook) {
        this.selectedLook = selectedLook;
        this.configurations = new HashMap();
    }

    /**
     * Looks up and answers the look configurations from the 
     * given <code>Preferences</code>.
     */
    static LookConfigurations restoreFrom(Preferences prefs) {
        LookAndFeel laf = restoreInitialLookFrom(prefs);
        LookConfigurations result = new LookConfigurations(laf);

        for (int i = 0; i < LOOK_NAMES.length; i++) {
            LookAndFeel lookAndFeel =
                ExtUIManager.createLookAndFeelInstance(LOOK_NAMES[i]);
            if (lookAndFeel != null) {
                result.putConfiguration(
                    LookConfiguration.restoreFrom(lookAndFeel, prefs));
            }
        }

        return result;
    }

    /**
     * Restores the initial look from the given <code>Preferences</code>.
     * If no look has been stored before, it answers a default look
     * as determined by the current <code>LookChoiceStrategy</code>.
     * 
     * @param prefs     Preferences to look up the laf class name
     * @return LookAndFeel	an instance of the initial look and feel
     */
    private static LookAndFeel restoreInitialLookFrom(Preferences prefs) {
        String lafClassName = prefs.get(LAF_CLASS_KEY, null);
        LookAndFeel laf = null;
        if (lafClassName != null) {
            laf = ExtUIManager.createLookAndFeelInstance(lafClassName);
        }

        if (laf != null)
            return laf;

        LookChoiceStrategy strategy = ExtUIManager.getLookChoiceStrategy();
        String defaultName = strategy.getDefaultLookClassName();
        laf = ExtUIManager.createLookAndFeelInstance(defaultName);
        if (laf != null)
            return laf;

        LOGGER.severe(
            "Could not create the default look and feel " + defaultName);
        return UIManager.getLookAndFeel();
    }

    /**
     * Answers the selected <code>LookConfiguration</code>.
     */
    LookAndFeel getSelectedLook() {
        return selectedLook;
    }

    /**
     * Answers the selected <code>LookConfiguration</code>.
     */
    LookConfiguration getSelection() {
        return getConfiguration(getSelectedLook());
    }

    /**
     * Sets a new selection.
     */
    void setSelectedLook(LookAndFeel laf) {
        selectedLook = laf;
    }

    /**
     * Looks up and answers the <code>LookConfiguration</code>
     * for the specified <code>LookAndFeel</code>.
     */
    LookConfiguration getConfiguration(LookAndFeel laf) {
        LookConfiguration config =
            (LookConfiguration) configurations.get(getKey(laf));
        return config != null ? config : new LookConfiguration(laf);
    }

    /**
     * Puts the given <code>LookConfiguration</code> in the map of 
     * all configurations using the config's look and feel as key.
     */
    void putConfiguration(LookConfiguration config) {
        configurations.put(getKey(config), config);
    }

    private String getKey(LookConfiguration config) {
        return getKey(config.lookAndFeel());
    }

    private String getKey(LookAndFeel laf) {
        return laf.getClass().getName();
    }

    /**
     * Stores the selection and all configurations in 
     * the specified <code>Preferences</code>.
     */
    void storeIn(Preferences prefs) {
        if (selectedLook != null) {
            prefs.put(LAF_CLASS_KEY, selectedLook.getClass().getName());
        }

        for (Iterator i = configurations.values().iterator(); i.hasNext();) {
            LookConfiguration configuration = (LookConfiguration) i.next();
            configuration.storeIn(prefs);
        }
    }

    /**
     * Answers a clone of this <code>LookConfigurations</code>.
     */
    public Object clone() throws CloneNotSupportedException {
        // Safe, since this is a final class
        LookConfigurations result = new LookConfigurations(selectedLook);
        for (Iterator i = configurations.values().iterator(); i.hasNext();) {
            LookConfiguration config = (LookConfiguration) i.next();
            result.putConfiguration(config);
        }
        return result;
    }

    // Custom Equals Implementation *****************************************

    public boolean equals(Object o) {
        if (!(o instanceof LookConfigurations))
            return false;

        LookConfigurations other = (LookConfigurations) o;
        if (!getSelection().equals(other.getSelection()))
            return false;

        for (Iterator i = configurations.values().iterator(); i.hasNext();) {
            LookConfiguration config1 = (LookConfiguration) i.next();
            LookConfiguration config2 = other.getConfiguration(config1.lookAndFeel());
            if (config2 == null || !config1.equals(config2))
                return false;
        }
        return true;
    }

    public int hashCode() {
        return selectedLook.hashCode();
    }

}