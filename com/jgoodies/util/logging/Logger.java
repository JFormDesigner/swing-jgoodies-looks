package com.jgoodies.util.logging;

/*
 * Copyright (c) 2002 Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of Karsten Lentzsch.  
 * Use is subject to license terms.
 *
 */
 
/**
 * Instances of class Logger are used to log messages of
 * different log levels.
 * 
 * [Pending 1.4:] Most parts of this implementation are 
 * superseeded by the java.util.logging package of the J2SE 1.4.
 *
 * @author Karsten Lentzsch
 */

public final class Logger {

	private static final int offValue = Level.OFF.intValue();

	private final String	catalogName;
	private final int		levelValue;

	private Logger(String name) {
		this.catalogName = name;
		this.levelValue = LogManager.getLevel().intValue();
	}

	public static Logger getLogger(String name) {
		return new Logger(name);
	}

	public void log(Level level, String msg) {
		log(level, msg, null);
	}

	public void log(Level level, String msg, Throwable thrown) {
		if (level.intValue() < levelValue || levelValue == offValue) {
			return;
		}
		LogManager.getHandler().log(level, catalogName, msg, thrown);
	}

	public void severe(String msg) {
		log(Level.SEVERE, msg);
	}

	public void warning(String msg) {
		log(Level.WARNING, msg);
	}

	public void info(String msg) {
		log(Level.INFO, msg);
	}

	public void config(String msg) {
		log(Level.CONFIG, msg);
	}


}