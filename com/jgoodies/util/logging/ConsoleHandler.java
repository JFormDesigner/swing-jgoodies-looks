package com.jgoodies.util.logging;

/*
 * Copyright (c) 2002 Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of Karsten Lentzsch.  
 * Use is subject to license terms.
 *
 */
 
import java.util.Date;

/** 
 * An implementation of the Handler interface that writes
 * log messages to the console.
 * 
 * @author Karsten Lentzsch
 */

public class ConsoleHandler implements Handler {
	
	public void log(Level level, String catalogName, String msg, Throwable thrown) {
		System.out.print(new Date(System.currentTimeMillis()));
		System.out.print(' ');
		System.out.println(catalogName);
		System.out.print(level);
		System.out.print(':');
		System.out.println(msg);
		if (thrown != null) {
			thrown.printStackTrace(System.out);
		}
	}
	
	public void flush() {}
}