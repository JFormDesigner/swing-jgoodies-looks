package com.jgoodies.util.logging;

/*
 * Copyright (c) 2003 Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of Karsten Lentzsch.  
 * Use is subject to license terms.
 *
 */

/**
 * This class stores the current logging handler and level.
 *
 * @author Karsten Lentzsch
 */

public final class LogManager {

    private static Handler handler = new ConsoleHandler();
    private static Level   level   = defaultLevel();

    public static Handler getHandler() {
        return handler;
    }
    
    public static void setHandler(Handler newHandler) {
        handler = newHandler;
    }

    public static Level getLevel() {
        return level;
    }
    
    public static void setLevel(Level newLevel) {
        level = newLevel;
    }

    private static Level defaultLevel() {
        String levelName =
            System.getProperty("logLevel", "WARNING").toUpperCase();
        return Level.parse(levelName.toUpperCase());
    }
}