/*
 * Copyright (c) 2003 JGoodies Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of JGoodies Karsten Lentzsch.  
 * Use is subject to license terms.
 *
 */

package com.jgoodies.swing.model;


/**
 * A simple {@link com.jgoodies.swing.model.ValueModel} implementation 
 * that holds a generic value of type <code>Object</code>. 
 * If the value changes, a <code>ChangeEvent</code> is fired 
 * that can be observed using a <code>ChangeListener</code>.
 *
 * @author  Karsten Lentzsch
 * @see     ValueModel
 * @see     javax.swing.event.ChangeEvent
 * @see     javax.swing.event.ChangeListener
 */
public final class ValueHolder extends AbstractValueModel {

    /** 
     * Holds the value to be observed.
     */
    private Object value;
    

    // Instance Creation ****************************************************

    /**
     * Constructs a <code>ValueHolder</code> with <code>null</code>
     * as initial value.
     */
    public ValueHolder() {
        this(null);
    }
    
    /**
     * Constructs a <code>ValueHolder</code> with the given initial value.
     * 
     * @param initialValue   the initial value
     */
    public ValueHolder(Object initialValue) {
        value = initialValue;
    }
    
    /**
     * Constructs a <code>ValueHolder</code> with the specified initial
     * boolean value. The <code>boolean</code> value is converted to
     * a <code>Boolean</code> object.
     * 
     * @param initialValue   the initial value
     */
    public ValueHolder(boolean initialValue) {
        this(new Boolean(initialValue));
    }


    // ValueModel Implementation ********************************************
    
    /**
     * Answers the observed value.
     * 
     * @return the observed value
     */
    public Object getValue() {
        return value;
    }

    /**
     * Sets a new value. Does nothing if the new value is the same as before.
     * 
     * @param newValue  the new value
     */
    public void setValue(Object newValue) {
        if (equals(value, newValue))
            return;
        value = newValue;
        fireStateChanged();
    }

}