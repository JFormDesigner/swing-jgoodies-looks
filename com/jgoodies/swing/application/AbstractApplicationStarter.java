/*
 * Copyright (c) 2003 JGoodies Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of JGoodies Karsten Lentzsch.  
 * Use is subject to license terms.
 *
 */

package com.jgoodies.swing.application;

import java.util.Locale;

import com.jgoodies.splash.Splash;
import com.jgoodies.swing.help.HelpBroker;
import com.jgoodies.util.Utilities;
import com.jgoodies.util.logging.Level;
import com.jgoodies.util.logging.LogManager;
import com.jgoodies.util.logging.Logger;

/**
 * An abstract class that organizes the application startup process.
 * It therefore defines a default startup sequence and provides some 
 * convenience code, useful for this task.<p>
 * 
 * This class uses the JGoodies foundation classes only. In contrast,
 * the <code>DefaultApplicationStarter</code> uses more of the JGoodies
 * convenience classes.
 * 
 * @see	Globals
 * @see	Workbench
 * @see	ResourceManager
 * @see	ActionManager
 * @see	Splash
 * @see	HelpBroker
 *
 * @author Karsten Lentzsch
 */

public abstract class AbstractApplicationStarter {

    protected Logger logger;
    protected Globals globals;

    /**
     * Does a minimal preparation before running the application load process:
     * initializes fields and catches errors.
     */
    public void boot(Globals newGlobals) {
        try {
            Splash.resetStartTime();
            this.globals = newGlobals;
            this.logger = Logger.getLogger("ApplicationStarter");
            load();
        } catch (Throwable t) {
            logger.log(Level.SEVERE, "Load failed", t);
            exit();
        } finally {
            Splash.ensureClosed();
        }
    }

    /**
     * Ensures a closed splash, flushes logs, and exits the system 
     * - if allowed.
     */
    protected void exit() {
        Splash.ensureClosed();
        LogManager.getHandler().flush();
        if (Utilities.systemExitAllowed())
            System.exit(0); // exit the application
    }

    /**
     * Loads, configures, initializes, and brings up the application.
     */
    protected void load() {
        // Set the locale, so all output will be in the correct locale.
        setDefaultLocale();

        // Add some properties to get more precise environment info 
        // in case of an error.
        addApplicationProperties();

        // Configure logging, for example set up a file log handler.
        configureLogging();

        // Do a minimal configuration, so we can bring up the splash.
        Workbench.setGlobals(globals);
        ResourceManager.setBundlePath(globals.getResourcesBundlePath());

        configureSplash();
        Splash.open();
        //System.out.println(Splash.elapsedTime());

        // Continue configuration: set paths for the ActionManager and HelpBroker.
        // TODO: may be moved up to ResourceManager.setBundlePath
        ActionManager.setBundlePath(globals.getActionsBundlePath());
        HelpBroker.setHelpSetPath(globals.getHelpSetPath());

        // Configure all UI related properties: look&feel, system properties, etc.
        Splash.setNote("Configuring UI");
        configureUI();

        launchApplication();
    }

    /**
     * Configures logging.
     */
    abstract protected void configureLogging();

    /**
     * Configures the splash component.
     */
    abstract protected void configureSplash();

    /**
     * Brings up the application, it therefore initializes the main frame,
     * checks the setup process, initializes all actions, then builds 
     * the main frame, and finally opens it.
     */
    abstract protected void launchApplication();

    /**
     * Configures all UI related properties: look&feel, system properties, etc.
     */
    abstract protected void configureUI();

    /**
     * Checks whether the default locale should be modified
     * to the one described by the runtime argument "locale".
     * This description is in format: <language>.<country>.
     */
    private void setDefaultLocale() {
        String encodedLocaleName = System.getProperty("locale");
        if (null == encodedLocaleName) {
            return;
        }

        encodedLocaleName = encodedLocaleName.trim();

        if (encodedLocaleName.length() == 0) {
            return;
        }

        int dotIndex = encodedLocaleName.indexOf('.');
        String language;
        String country;
        if (-1 == dotIndex) {
            language = encodedLocaleName;
            country = "";
        } else {
            language = encodedLocaleName.substring(0, dotIndex);
            country = encodedLocaleName.substring(dotIndex + 1);
        }
        Locale.setDefault(new Locale(language, country));
    }

    /** 
     * Adds properties about the application and environment that 
     * help us identify problems in case of a runtime error.
     */
    private void addApplicationProperties() {
        System.setProperty("application.name",        globals.getProductName());
        System.setProperty("application.vendor",      globals.getVendor());
        System.setProperty("application.vendor.url",  globals.getVendorURL());
        System.setProperty("application.vendor.mail", globals.getVendorMail());
        System.setProperty("application.fullversion", globals.getFullVersion());
    }

}