/*
 * Copyright (c) 2003 JGoodies Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of JGoodies Karsten Lentzsch.  
 * Use is subject to license terms.
 *
 */

package com.jgoodies.swing.model;


import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;

/**
 * An abstract class that minimizes the effort required to implement
 * the {@link ValueModel} interface. It provides convenience methods
 * to convert booleans, ints, floats and doubles to their corresponding
 * Object values.
 * <p>
 * Subclasses must implement <code>getValue()</code> and 
 * <code>setValue(Object)</code> to get and set the observable value.
 *
 * @author Karsten Lentzsch
 */

public abstract class AbstractValueModel implements ValueModel {
    
    /**
     * Only one ChangeEvent is needed per model instance since the
     * event's only state is the source property.  The source of events
     * generated is always "this".
     */
    protected transient ChangeEvent changeEvent = null;
    protected EventListenerList listenerList = new EventListenerList();


    // Change Management ****************************************************

    /**
     * Adds a ChangeListener to the button.
     *
     * @param l the listener to add
     */
    public void addChangeListener(ChangeListener l) {
        listenerList.add(ChangeListener.class, l);
    }

    /**
     * Notify all listeners that have registered interest for
     * notification on this event type.  The event instance 
     * is lazily created using the parameters passed into 
     * the fire method.
     *
     * @see EventListenerList
     */
    public void fireStateChanged() {
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == ChangeListener.class) {
                // Lazily create the event:
                if (changeEvent == null)
                    changeEvent = new ChangeEvent(this);
                ((ChangeListener) listeners[i + 1]).stateChanged(changeEvent);
            }
        }
    }

    /**
     * Removes a ChangeListener from the button.
     *
     * @param l the listener to remove
     */
    public void removeChangeListener(ChangeListener l) {
        listenerList.remove(ChangeListener.class, l);
    }
    
    
    // Abstract ValueModel Implementation *************************************

    /**
     * Answers the observed value.
     * <p>
     * Implementation Note: This is a workaround for a problem in 
     * 1.3.0 environments. The method is obsolete in Java 1.3.1 or later.
     * 
     * @return the observed value
     */
    abstract public Object getValue();
    

    // Type Conversion ******************************************************

    /**
     * Converts the observed value and answers a <code>boolean</code>.
     * It is assumed that the value is an object of type <code>Boolean</code>.
     * 
     * @return the <code>boolean</code> value
     * @throws ClassCastException  if the observed value is not of type
     *     <code>Boolean</code>
     * @throws NullPointerException if the value is <code>null</code>
     */
    public boolean booleanValue() {
        return ((Boolean) getValue()).booleanValue();
    }
    
    /**
     * Converts the observed value and answers a <code>double</code>.
     * It is assumed that the value is an object of type <code>Double</code>.
     * 
     * @return the <code>double</code> value
     * @throws ClassCastException  if the observed value is not of type
     *     <code>Double</code>
     * @throws NullPointerException if the value is <code>null</code>
     */
    public double doubleValue() {
        return ((Double) getValue()).doubleValue();
    }
    
    /**
     * Converts the observed value and answers a <code>float</code>.
     * It is assumed that the value is an object of type <code>Float</code>.
     * 
     * @return the <code>float</code> value
     * @throws ClassCastException  if the observed value is not of type
     *     <code>Float</code>
     * @throws NullPointerException if the value is <code>null</code>
     */
    public float floatValue() {
        return ((Float) getValue()).floatValue();
    }
    
    /**
     * Converts the observed value and answers an <code>int</code>.
     * It is assumed that the value is an object of type <code>Integer</code>.
     * 
     * @return the <code>int</code> value
     * @throws ClassCastException  if the observed value is not of type
     *     <code>Integer</code>
     * @throws NullPointerException if the value is <code>null</code>
     */
    public int intValue() {
        return ((Integer) getValue()).intValue();
    }
    
    /**
     * Converts the observed value and answers a <code>long</code>.
     * It is assumed that the value is an object of type <code>Long</code>.
     * 
     * @return the <code>long</code> value
     * @throws ClassCastException  if the observed value is not of type
     *     <code>Long</code>
     * @throws NullPointerException if the value is <code>null</code>
     */
    public long longValue() {
        return ((Long) getValue()).longValue();
    }
    
    /**
     * Converts the observed value and answers a <code>String</code>.
     * It is assumed that the value is an object of type <code>String</code>.
     * 
     * @return the <code>String</code> value
     * @throws ClassCastException  if the observed value is not of type
     *     <code>String</code>
     * @throws NullPointerException if the value is <code>null</code>
     */
    public String getString() {
        return (String) getValue();
    }

    /**
     * Converts the given <code>boolean</code> to an object and 
     * sets it as new value.
     * 
     * @param b   the value to be converted and set as new value
     */
    public void setValue(boolean b) {
        setValue(new Boolean(b));
    }
    
    /**
     * Converts the given <code>double</code> to an object and 
     * sets it as new value.
     * 
     * @param d   the value to be converted and set as new value
     */
    public void setValue(double d) {
        setValue(new Double(d));
    }
    
    /**
     * Converts the given <code>float</code> to an object and 
     * sets it as new value.
     * 
     * @param f   the value to be converted and set as new value
     */
    public void setValue(float f) {
        setValue(new Float(f));
    }
    
    /**
     * Converts the given <code>int</code> to an object and 
     * sets it as new value.
     * 
     * @param i   the value to be converted and set as new value
     */
    public void setValue(int i) {
        setValue(new Integer(i));
    }

    /**
     * Converts the given <code>long</code> to an object and 
     * sets it as new value.
     * 
     * @param l   the value to be converted and set as new value
     */
    public void setValue(long l) {
        setValue(new Long(l));
    }

    /**
     * Sets a new value that is ensured to be in the interval as
     * specified by the given lower and upper bounds. Uses a default
     * value if the given value is outside the interval.
     * 
     * @param f             the new value to be set
     * @param min           the lower bound
     * @param max           the upper bound
     * @param defaultValue  the value used if the value is outside the bounds
     */
    public void setBoundedValue(float f, float min, float max, float defaultValue) {
        setValue(new Float(min <= f && f <= max ? f : defaultValue));
    }

    /**
     * Sets a new value that is ensured to be in the interval as
     * specified by the given lower and upper bounds. Uses a default
     * value if the given value is outside the interval.
     * 
     * @param d             the new value to be set
     * @param min           the lower bound
     * @param max           the upper bound
     * @param defaultValue  the value used if the value is outside the bounds
     */
    public void setBoundedValue(double d, double min, double max, double defaultValue) {
        setValue(new Double(min <= d && d <= max ? d : defaultValue));
    }

    /**
     * Sets a new value that is ensured to be in the interval as
     * specified by the given lower and upper bounds. Uses a default
     * value if the given value is outside the interval.
     * 
     * @param i             the new value to be set
     * @param min           the lower bound
     * @param max           the upper bound
     * @param defaultValue  the value used if the value is outside the bounds
     */
    public void setBoundedValue(int i, int min, int max, int defaultValue) {
        setValue(new Integer(min <= i && i <= max ? i : defaultValue));
    }

    /**
     * Sets a new value that is ensured to be in the interval as
     * specified by the given lower and upper bounds. Uses a default
     * value if the given value is outside the interval.
     * 
     * @param l             the new value to be set
     * @param min           the lower bound
     * @param max           the upper bound
     * @param defaultValue  the value used if the value is outside the bounds
     */
    public void setBoundedValue(long l, long min, long max, long defaultValue) {
        setValue(new Long(min <= l && l <= max ? l : defaultValue));
    }
    

    // Misc *****************************************************************
    
    /**
     * Returns a string representation of this value model. 
     * Answers the print string of the observed value.
     * 
     * @return a string representation of this value model
     */
    public String toString() {
        Object anObject = getValue();
        return anObject == null ? "null" : anObject.toString();
    }
    
    /**
     * Checks and answers if the two given objects are 
     * both <code>null</code> or equal.
     * 
     * @param o1   one of the objects to compare
     * @param o2   the other object to compare
     * @return true iff both objects are null or equal
     */
    protected boolean equals(Object o1, Object o2) {
        return    (o1 != null && o2 != null && o1.equals(o2))
                || (o1 == null && o2 == null);
    }
    
    
}