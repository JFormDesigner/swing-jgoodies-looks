/*
 * Copyright (c) 2003 JGoodies Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of JGoodies Karsten Lentzsch.  
 * Use is subject to license terms.
 *
 */

package com.jgoodies.swing.model;


/**
 * This class observes the change of a boolean value. It is used 
 * to listen to boolean changes, for example the click on "Cancel".
 * <p>
 * <b>Note: This class will be removed from a future version of 
 * the UI framework - likely version 1.2.</b>.
 * 
 * @author Karsten Lentzsch
 */
public final class Trigger extends AbstractValueModel {

    private Boolean hasBeenTriggered;
    
    
    public Trigger() {
        hasBeenTriggered = Boolean.FALSE;
    }
    
    public Object getValue() {
        return hasBeenTriggered;
    }
    
    public void setValue(Object newValue) {
        if (!(newValue instanceof Boolean))
            throw new IllegalArgumentException("The new value must be a Boolean.");
        Boolean oldValue = hasBeenTriggered;
        if (equals(oldValue, newValue))
            return;
        hasBeenTriggered = (Boolean) newValue;
        fireStateChanged();
    }

    /**
     * Returns whether this trigger has been triggered.
     * 
     * @return true indicates triggered, false indicates not triggered
     * @deprecated replaced by #getValue()
     */
    public boolean hasBeenTriggered() {
        return booleanValue();
    }
    
    /**
     * Resets the trigger. Sets the value to false.
     * 
     * @deprecated replaced by #setValue()
     */
    public void reset() {
        setValue(Boolean.FALSE);
    }
    
    /**
     * Resets the trigger. Sets the value to false.
     * 
     * @deprecated replaced by #setValue()
     */
    public void trigger() {
        setValue(Boolean.TRUE);
    }


}