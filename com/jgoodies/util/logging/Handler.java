package com.jgoodies.util.logging;

/*
 * Copyright (c) 2002 Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of Karsten Lentzsch.  
 * Use is subject to license terms.
 *
 */
 
/**
 * This interface describes logging handlers. 
 *
 * @author Karsten Lentzsch
 */

public interface Handler {

	void log(Level level, String catalog, String msg, Throwable thrown);
    void flush();

}