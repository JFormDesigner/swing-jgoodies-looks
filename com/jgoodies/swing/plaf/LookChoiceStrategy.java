package com.jgoodies.swing.plaf;

/*
 * Copyright (c) 2003 JGoodies Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of JGoodies Karsten Lentzsch.  
 * Use is subject to license terms.
 *
 */



/**
 * An interface that defines a strategy how to determine the default look.
 * A typical implementation will look up the system properties for
 * the OS type, and OS version to choose a look appropriate for the platform.
 * 
 * @author Karsten Lentzsch
 * @see    com.jgoodies.plaf.Options
 */
public interface LookChoiceStrategy {


    /**
     * Computes and answers the default look's class name.
     * 
     * @return the class name of the default look as choosen by this strategy
     */
    String getDefaultLookClassName();
 

}