package com.jgoodies.lazy;

/*
 * Copyright (c) 2003 JGoodies Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of JGoodies Karsten Lentzsch.
 * Use is subject to license terms.
 *
 */

import com.jgoodies.util.logging.Logger;

/**
 * An implementation of {@link Preparable} that loads a set of classes in a
 * background thread.
 *
 * @author Karsten Lentzsch
 */
public final class BackgroundClassLoader implements Preparable {

    private final String[] classNames;

    /**
     * Constructs a <code>BackgroundClassLoader</code> for the specified
     * array of class names.
     * 
     * @param classNames   the fully qualified names of the classes to load
     */
    public BackgroundClassLoader(String[] classNames) {
        this.classNames = classNames;
    }

    /**
     * Loads the classes for all given class names.
     * 
     * @see Preparable#prepare()
     */
    public void prepare() {
        Logger logger = Logger.getLogger("BackgroundClassLoader");
        logger.info("Loading " + classNames.length + " classes...");
        long start = System.currentTimeMillis();
        for (int i = 0; i < classNames.length; i++) {
            try {
                Class.forName(classNames[i]);
            } catch (ClassNotFoundException e) {
                logger.info("Class not found: " + classNames[i]);
            }
        }
            long stop = System.currentTimeMillis();
            logger.info(
                "Loaded in " + (stop - start) + "ms.");
    }

}
