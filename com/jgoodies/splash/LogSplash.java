package com.jgoodies.splash;

/*
 * Copyright (c) 2003 JGoodies Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of JGoodies Karsten Lentzsch.
 * Use is subject to license terms.
 *
 */

import com.jgoodies.util.logging.Level;
import com.jgoodies.util.logging.Logger;

/**
 * A {@link SplashProvider} implementation that logs progress and splash
 * messages to a <code>Logger</code>.
 * 
 * @author Karsten Lentzsch
 * @see Splash
 */

public final class LogSplash implements SplashProvider {

    private final Logger logger;
    private final Level  logLevel;

    /**
     * Constructs a <code>LogSplash</code> with a default <code>Logger</code>
     * logging on the <code>INFO</code> level.
     */
    public LogSplash() {
        this(Logger.getLogger("LogSplash"), Level.INFO);
    }

    /**
     * Constructs a <code>LogSplash</code> for the specified
     * <code>Logger</code> and <code>Level</code>.
     */
    public LogSplash(Logger logger, Level logLevel) {
        this.logger = logger;
        this.logLevel = logLevel;
    }

    /**
     * Implements the open splash behavior; does nothing.
     */
    public void openSplash() {}

    /**
     * Implements the close splash behavior; does nothing.
     */
    public void closeSplash() {}

    /**
     * Logs the given splash progress.
     */
    public void setProgress(int percent) {
        logger.log(logLevel, Splash.elapsedTime() + " " + percent + "%");
    }

    /**
     * Logs the given splash message.
     */
    public void setNote(String message) {
        logger.log(logLevel, Splash.elapsedTime() + " " + message);
    }
}