package com.jgoodies.swing.help;

/*
 * Copyright (c) 2002 Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of Karsten Lentzsch.  
 * Use is subject to license terms.
 *
 */
 
/**
 * Instances of this class describe exceptions thrown in the <code>HelpSet</code> class.
 *
 * @author Karsten Lentzsch
 */

final class HelpSetException extends Exception {
    
    /**
     * Constructs a default <code>HelpSetException</code> with no message.
     */
    public HelpSetException() {}
    
    
    /**
     * Constructs a <code>HelpSetException</code> for the given String message.
     */
    public HelpSetException(String message) { super(message);  }
}