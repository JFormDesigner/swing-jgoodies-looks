package com.jgoodies.swing.plaf;

/*
 * Copyright (c) 2002 Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of Karsten Lentzsch.  
 * Use is subject to license terms.
 *
 */

import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.jgoodies.plaf.LookUtils;
import com.jgoodies.plaf.Options;
import com.jgoodies.swing.application.Workbench;
import com.jgoodies.swing.model.ValueHolder;
import com.jgoodies.swing.model.ValueModel;
import com.jgoodies.swing.util.MySwingUtilities;
import com.jgoodies.util.logging.Level;
import com.jgoodies.util.logging.Logger;
import com.jgoodies.util.prefs.Preferences;

/**
 * A helper class that configures Swing related look issues:<ol>
 * <li>installs new look and feels.</li>
 * <li>sets system properties,</li>
 * <li>restores a look and feel with an optional theme, and</li>
 * <li>overrides UI defaults.</li>
 * </ol>
 * 
 * @see     LookChoiceStrategy
 * @see     UIManager
 * 
 * @author  Karsten Lentzsch
 */

public final class ExtUIManager {

    private static final Logger LOGGER = Logger.getLogger("ExtUIManager");

    private static ValueModel lookConfigurationsModel;
    private static LookChoiceStrategy lookChoiceStrategy =
        LookChoiceStrategies.DEFAULT;

    // Override the default constructor - prevents instantiation.
    private ExtUIManager() {}

    // Public and Package API ***********************************************

    /**
     * Answers the current <code>LookChoiceStrategy</code>.
     */
    public static LookChoiceStrategy getLookChoiceStrategy() {
        return lookChoiceStrategy;
    }

    /**
     * Sets a <code>LookChoiceStrategy</code>, that will be used
     * as the very first look, if no look has been stored.
     */
    public static void setLookChoiceStrategy(LookChoiceStrategy strategy) {
        lookChoiceStrategy = strategy;
    }

    /**
     * Sets up UI related properties: installs a look, 
     * sets Swing system properties, restores the look and theme 
     * from the user <code>Preferences</code>, and overrides 
     * <code>UIManager</code> settings.
     */
    public static void setup() {
        // Install extra looks
        UIManager.installLookAndFeel("JGoodies Plastic",
                                     Options.PLASTIC_NAME);
        UIManager.installLookAndFeel("JGoodies Plastic XP",
                                     Options.PLASTICXP_NAME);
                                     
        UIManager.put("Application.useSystemFontSettings", Boolean.TRUE);

        LookConfigurations lafConfigs = restoreLookConfigurations();
        lookConfigurationsModel = new ValueHolder();
        lookConfigurationsModel.setValue(lafConfigs);
        LookConfiguration selection = lafConfigs.getSelection();
        setLookAndTheme(selection.lookAndFeel(), selection.getTheme());

        lookConfigurationsModel().addChangeListener(
            createLookChangeListener());
    }

    /**
     * Answers the <code>lookConfigurationModel</code> which is lazily 
     * initialized with default values. 
     */
    static ValueModel lookConfigurationsModel() {
        if (lookConfigurationsModel == null) {
            LookConfigurations lafConfigs = restoreLookConfigurations();
            lookConfigurationsModel = new ValueHolder();
            lookConfigurationsModel.setValue(lafConfigs);
        }
        return lookConfigurationsModel;
    }

    // Setting the Look And Theme *******************************************

    /**
     * Sets a look and theme. Wraps <code>LookUtils.setLookAndTheme</code>
     * with an exception handling.
     */
    private static LookConfiguration setLookAndTheme(
        LookAndFeel laf,
        Object theme) {
        try {
            LOGGER.info("Setting L&F: " + laf.getName());
            LookUtils.setLookAndTheme(laf, theme);
            return new LookConfiguration(laf, theme); // successful
        } catch (UnsupportedLookAndFeelException e) {
            LOGGER.warning(
                "Can't set unsupported look and feel:" + laf.getName());
            return null; // failed
        }
    }

    // Private Helper Methods ***********************************************

    /**
     * Creates and answers a <code>ChangeListener</code> that updates the
     * look and theme model, updates all UIs and stores the look and theme
     * in the user preferences.
     */
    private static ChangeListener createLookChangeListener() {
        return new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                LookConfigurations configs =
                    (LookConfigurations) lookConfigurationsModel().getValue();
                LookConfiguration selection = configs.getSelection();
                setLookAndTheme(
                    selection.lookAndFeel(),
                    selection.getTheme());
                MySwingUtilities.updateAllUIs();
                storeLookConfigurations(Workbench.userPreferences());
            }
        };
    }

    // Storing and Restoring Look Info **************************************

    /**
     * Stores the current look configurations with the selected configuration
     * in the specified <code>Preferences</code>.
     */
    private static void storeLookConfigurations(Preferences prefs) {
        ((LookConfigurations) lookConfigurationsModel().getValue()).storeIn(
            prefs);
    }

    /**
     * Restores all look configurations from the user preferences. 
     */
    private static LookConfigurations restoreLookConfigurations() {
        return LookConfigurations.restoreFrom(Workbench.userPreferences());
    }

    // Look and Feel Instantiation ******************************************

    /**
     * Creates and answers an instance of <code>LookAndFeel</code> for the 
     * specified class name. 
     */
    public static LookAndFeel createLookAndFeelInstance(String className) {
        String replacementClassName =
            Options.getReplacementClassNameFor(className);
        try {
            Class clazz = Class.forName(replacementClassName);
            return (LookAndFeel) clazz.newInstance();
        } catch (Throwable t) {
            String message =
                "Class name="
                    + className
                    + "\nReplacement class name="
                    + replacementClassName;
            LOGGER.log(Level.WARNING, message, t);
        }
        return null;
    }

}