package com.jgoodies.swing.application;

/*
 * Copyright (c) 2002 Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of Karsten Lentzsch.  
 * Use is subject to license terms.
 *
 */
import java.awt.Frame;

import com.jgoodies.swing.AbstractFrame;
import com.jgoodies.util.Utilities;
import com.jgoodies.util.logging.Level;
import com.jgoodies.util.logging.Logger;
import com.jgoodies.util.prefs.Preferences;

/**
 * The <code>Workbench</code> is the application central. 
 * It references the main frame, globals, and user preferences.<p>
 * 
 * It is mandatory to properly initialize the <code>Workbench</code> 
 * during the startup process. This can be done by the
 * <code>ApplicationStarter</code>.<p>
 * 
 * @see	Globals
 * @see	AbstractMainFrame
 * @see	Preferences
 * 
 * @author Karsten Lentzsch
 */

public final class Workbench {

    private static Globals           globals;
    private static AbstractMainFrame mainFrame;

    // Suppresses default constructor, ensuring non-instantiability.
    private Workbench() {}

    /**
     * Returns the application wide global values.
     */
    public static Globals getGlobals() {
        return globals;
    }

    /**
     * Sets the application wide global values.
     */
    public static void setGlobals(Globals g) {
        globals = g;
    }

    /**
     * Returns the applications single main frame.
     */
    public static AbstractMainFrame getMainFrame() {
        return mainFrame;
    }

    /**
     * Sets the application's single main frame.
     */
    public static void setMainFrame(AbstractMainFrame f) {
        mainFrame = f;
    }

    /**
     * Answers the user preferences.
     */
    public static Preferences userPreferences() {
        return Preferences.userRoot().node(getGlobals().getPreferencesNode());
    }

    /**
     * Does everything necessary to close the application:
     * stores the application state and the states of all RichFrames
     * in the user preferences; closes and disposes all frames;
     * flushes the user preferences; exits the system.
     */
    public static void close() {
        // Close and dispose all registered frames.
        // RichFrames can store their state.
        Frame[] frames = Frame.getFrames();
        for (int i = 0; i < frames.length; i++) {
            Frame frame = frames[i];
            if (frame instanceof AbstractFrame) {
                try {
                    ((AbstractFrame) frame).storeState();
                } catch (Exception e) {
                    Logger.getLogger("Workbench").log(
                        Level.INFO,
                        "Can't store state for " + frame,
                        e);
                }
            }
            frame.dispose();
        }

        // Save the preferences if available.
        userPreferences().flush();

        // Exit the system if allowed.
        if (Utilities.systemExitAllowed())
            System.exit(0);
    }

}