package com.jgoodies.lazy;

/*
 * Copyright (c) 2003 JGoodies Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of JGoodies Karsten Lentzsch.
 * Use is subject to license terms.
 *
 */

import java.util.Enumeration;
import java.util.Vector;

import com.jgoodies.util.Utilities;
import com.jgoodies.util.logging.Level;
import com.jgoodies.util.logging.Logger;

/**
 * Prepares a sequence of {@link Preparable} instances.
 *
 * @author Karsten Lentzsch
 */
public final class BackgroundLoader extends Thread implements Preparable {

    private static final int DEFAULT_PRIORITY = NORM_PRIORITY - 1;
    private static final int DEFAULT_DELAY_MS = 1500;

    private final Vector preparables;

    private long delayMilliseconds;

    /**
     * Constructs a <code>BackgroundLoader</code>.
     */
    public BackgroundLoader() {
        setPriority(DEFAULT_PRIORITY);
        preparables = new Vector();
        delayMilliseconds = DEFAULT_DELAY_MS;
    }

    /**
     * Adds the specified {@link Preparable} to the sequence of 
     * all {@link Preparable}s that should be prepared.
     * 
     * @param preparable   the {@link Preparable} to add
     */
    public void add(Preparable preparable) {
        preparables.add(preparable);
    }

    /**
     * Prepares all registered {@link Preparable}s.
     * Catches runtime exceptions and logs a warning.
     * 
     * @see Preparable#prepare()
     */
    public void prepare() {
        for (Enumeration e = preparables.elements(); e.hasMoreElements();) {
            Preparable preparable = ((Preparable) e.nextElement());
            try {
                preparable.prepare();
            } catch (Throwable t) {
                Logger.getLogger("BackgroundLoader").log(
                    Level.WARNING,
                    "Could not prepare " + preparable,
                    t);
            }
        }
    }

    /**
     * Runs the preparation
     */
    public void run() {
        Utilities.sleep(delayMilliseconds);
        prepare();
    }

}
