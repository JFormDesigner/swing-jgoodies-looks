package com.jgoodies.splash;

/*
 * Copyright (c) 2003 JGoodies Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of JGoodies Karsten Lentzsch.
 * Use is subject to license terms.
 *
 */

import com.jgoodies.util.logging.Logger;

/**
 * A singleton that provides access to a pluggable splash,
 * to improve the application's feedback during startup.
 * You can open and close it, set messages and indicate progress.<p>
 * 
 * The concrete work is done by pluggable splash providers, which you 
 * can set via #setProvider, for example a splash window or panel.
 * The default provider is <code>NullSplash</code>, other providers 
 * are <code>ImageSplash</code> and <code>JWindowSplash</code>.<p>
 * 
 * This class logs splash events on <code>CONFIG</code> level with  
 * the elapsed time. You should invoke #initStartTime in your 
 * application's #main method to get accurate timing information.<p>
 * 
 * I have experimented with switching off the compiler until
 * the splash is up. It seems to have no significant effect
 * if you use an AWT splash screen.
 * 
 * @author	Karsten Lentzsch
 * @see	SplashProvider
 */

public final class Splash {

    public  static final String FAST_SPLASH_KEY = "fastSplash";
    private static final Logger LOGGER          = Logger.getLogger("Splash");

    private static SplashProvider provider;
    private static long     t0 = System.currentTimeMillis();
    private static boolean  splashOpen = false;

    // Suppresses default constructor, ensuring non-instantiability.
    private Splash() {}

    /**
     * Reset the start time that is used to compute the elapsed time.
     */
    public static void resetStartTime() {
        t0 = System.currentTimeMillis();
    }

    /**
     * Computes and answers the elapsed time since the call to
     * #resetStartTime().
     */
    public static long elapsedTime() {
        return System.currentTimeMillis() - t0;
    }

    /**
     * Ensures that the splash is open, calls #open if necessary.
     */
    public synchronized static void ensureOpen() {
        if (!splashOpen)
            open();
    }

    /**
     * Ensures that the splash is closed, calls #close if necessary.
     */
    public synchronized static void ensureClosed() {
        if (splashOpen)
            close();
    }

    /**
     * Opens the splash by calling the providers #openSplash method.
     */
    public synchronized static void open() {
        if (splashOpen)
            throw new IllegalStateException("Splash is already open.");

        getProvider().openSplash();
        splashOpen = true;
        LOGGER.config(Splash.elapsedTime() + "ms - opened");

        // Disabling the compiler may reduce the startup time.
        // Now that the splash is up, we reenable it.
        if (System.getProperty(Splash.FAST_SPLASH_KEY) != null) {
            Compiler.enable();
            LOGGER.config("Compiler enabled");
        }
    }

    /**
     * Closes the splash by calling the providers #closeSplash method.
     */
    public synchronized static void close() {
        if (!splashOpen)
            throw new IllegalStateException("Splash has been closed before.");

        LOGGER.config(Splash.elapsedTime() + "ms - closed");
        getProvider().closeSplash();
        splashOpen = false;
    }

    /**
     * Sets the splash note.
     */
    public static void setNote(String message) {
        LOGGER.config(Splash.elapsedTime() + "ms - " + message);
        getProvider().setNote(message);
    }

    /**
     * Sets the splash progress percent value.
     */
    public static void setProgress(int percent) {
        LOGGER.config(Splash.elapsedTime() + "ms " + percent + "%");
        getProvider().setProgress(percent);
    }

    /**
     * A convinience method that sets the splash note and progress.
     */
    public static void setNote(String message, int percent) {
        setNote(message);
        setProgress(percent);
    }

    /**
     * Answers the current {@link SplashProvider}.
     */
    public static SplashProvider getProvider() {
        if (provider == null) {
            provider = new NullSplash();
        }
        return provider;
    }

    /**
     * Sets a {@link SplashProvider}.
     */
    public static void setProvider(SplashProvider newProvider) {
        provider = newProvider;
    }

    // Helper Class *********************************************************
    
    // This default splash provider does nothing.
    private static class NullSplash implements SplashProvider {
        public void openSplash() {}
        public void closeSplash() {}
        public void setNote(String message) {}
        public void setProgress(int percent) {}
    }

}