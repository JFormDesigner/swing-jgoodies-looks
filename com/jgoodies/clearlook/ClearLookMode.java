/*
 * Copyright (c) 2003 JGoodies Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of JGoodies Karsten Lentzsch.  
 * Use is subject to license terms. 
 *
 */

package com.jgoodies.clearlook;

/**
 * Describes a ClearLook(tm) mode as used by the {@link ClearLookManager}.
 * Can be on or off; quiet, verbose, debug, or both.
 *
 * @author Karsten Lentzsch
 * @see	ClearLookManager
 */
public final class ClearLookMode {

    // Public ClearLook Modes *************************************************	

    public static final ClearLookMode OFF =
        new ClearLookMode("Off", false, false);
        
    public static final ClearLookMode ON =
        new ClearLookMode("On", false, false);
        
    public static final ClearLookMode VERBOSE =
        new ClearLookMode("Verbose", true, false);
        
    public static final ClearLookMode DEBUG =
        new ClearLookMode("Debug", true, true);



    private final String   name;
    private final boolean verbose;
    private final boolean debug;


    /**
     * Constructs a <code>ClearLookMode</code> with the specified name, 
     * verbose and debug switches.
     */
    private ClearLookMode(String name, boolean verbose, boolean debug) {
        this.name = name;
        this.verbose = verbose;
        this.debug = debug;
    }

    /**
     * Returns the mode's name. Also used to check whether 
     * a mode has been installed successfully.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns whether ClearLook will be used to analyse and replace
     * obsolete decorations.
     */
    public boolean isEnabled() {
        return this != OFF;
    }

    /**
     * Returns whether this mode is verbose.
     */
    public boolean isVerbose() {
        return verbose;
    }

    /**
     * Returns whether this mode indicates a debug state.
     */
    public boolean isDebug() {
        return debug;
    }

    /**
     * Looks up and answers a <code>ClearLookMode</code> for the specified name.
     */
    public static ClearLookMode valueOf(String name) {
        if (name.equalsIgnoreCase(OFF.name))
            return OFF;
        else if (name.equalsIgnoreCase(ON.name))
            return ON;
        else if (name.equalsIgnoreCase(VERBOSE.name))
            return VERBOSE;
        else if (name.equalsIgnoreCase(DEBUG.name))
            return DEBUG;
        else
            throw new IllegalArgumentException(
                "Invalid ClearLook(tm) mode " + name);
    }

    /**
     * Returns a string representation for this mode.
     */
    public String toString() {
        StringBuffer buffer = new StringBuffer(getClass().getName());
        buffer.append(": ");
        buffer.append(name);
        buffer.append("; enabled=");
        buffer.append(isEnabled());
        buffer.append("; verbose=");
        buffer.append(verbose ? "on" : "off");
        buffer.append("; debug=");
        buffer.append(verbose ? "on" : "off");
        return buffer.toString();
    }
    
}