/*
 * Copyright (c) 2003 JGoodies Karsten Lentzsch. All Rights Reserved.
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
     * 
     * @param name     the mode's name
     * @param verbose  true indicates that replacements shall log
     * @param debug    true indicates a debug mode
     */
    private ClearLookMode(String name, boolean verbose, boolean debug) {
        this.name = name;
        this.verbose = verbose;
        this.debug = debug;
    }

    /**
     * Returns the mode's name. Also used to check whether 
     * a mode has been installed successfully.
     * 
     * @return the name of this mode
     */
    public String getName() {
        return name;
    }

    /**
     * Returns whether ClearLook will be used to analyse and replace
     * obsolete decorations.
     * 
     * @return true if ClearLook is enabled, false if disabled
     */
    public boolean isEnabled() {
        return this != OFF;
    }

    /**
     * Returns whether this mode is verbose.
     * 
     * @return true if this mode logs many message, false if it's quiet
     */
    public boolean isVerbose() {
        return verbose;
    }

    /**
     * Returns whether this mode indicates a debug state.
     * 
     * @return true if this mode is a debug mode
     */
    public boolean isDebug() {
        return debug;
    }

    /**
     * Looks up and answers a <code>ClearLookMode</code> for the specified name.
     * 
     * @param name   the name of the ClearLook mode to lookup
     * @return the associated ClearLookMode instance
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
     * 
     * @return a string representation for this mode
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