/*
 * Copyright (c) 2003 JGoodies Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of JGoodies Karsten Lentzsch.  
 * Use is subject to license terms.
 *
 */

package com.jgoodies.swing.model;

import javax.swing.event.ChangeListener;

/**
 * Describes models with a generic access to a single value that allow
 * to observe value changes. The value can be accessed using the 
 * <code>#getValue()</code>/<code>#setValue(Object)</code> methods.
 * Observers can register <code>ChangeListeners</code> to be notified
 * if the value changes. 
 *
 * @author Karsten Lentzsch
 */
public interface ValueModel {
    
    /**
     * Answers the observed value.
     * 
     * @return the observed value
     */
	Object getValue();
    
    /**
     * Sets a new value.
     * 
     * @param newValue	the new value
     */
	void setValue(Object newValue);
	
    /**
     * Adds the given <code>ChangeListener</code> that will be notified
     * when the value changes.
     * 
     * @param listener	  the listener to be added
     */
	void addChangeListener(ChangeListener listener);
    
    /**
     * Removes the given <code>ChangeListener</code>.
     * 
     * @param listener	  the listener to be removed
     */
	void removeChangeListener(ChangeListener listener);
    
    /**
     * Notifies all registered listeners that the value has been changed.
     */
	void fireStateChanged();
    
}