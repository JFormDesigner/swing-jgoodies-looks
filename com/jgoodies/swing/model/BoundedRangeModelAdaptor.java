/*
 * Copyright (c) 2003 JGoodies Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of JGoodies Karsten Lentzsch.  
 * Use is subject to license terms.
 *
 */
package com.jgoodies.swing.model;

import java.io.Serializable;

import javax.swing.BoundedRangeModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;

/**
 * This implementation of <code>BoundedRangeModel</code> uses an underlying 
 * (subject) <code>ValueModel</code> to store an Integer value that is bound
 * by both, a minimum and maximum int value.
 *
 * @author Karsten Lentzsch
 */

public final class BoundedRangeModelAdaptor
    implements BoundedRangeModel, Serializable {
        
    /**
     * Only one ChangeEvent is needed per model instance since the
     * event's only (read-only) state is the source property.  The source
     * of events generated here is always "this".
     */
    protected transient ChangeEvent changeEvent = null;

    /** The listeners waiting for model changes. */
    protected EventListenerList listenerList = new EventListenerList();

    private final ValueModel subject;

    private int extent = 0;
    private int minimum = 0;
    private int maximum = 100;
    private boolean isAdjusting = false;
    
    
    // Instance Creation ***************************************************

    /**
     * Constructs a <code>BoundedRangeModelAdaptor</code> on the given
     * subject using the specified extend, minimum and maximum values.

     * @param subject ValueModel
     * @param extent int
     * @param min int
     * @param max int
     */
    public BoundedRangeModelAdaptor(
        ValueModel subject,
        int extent,
        int min,
        int max) {
        this.subject = subject;
        initialize(((Integer) subject.getValue()).intValue(), extent, min, max);
    }

    /**
     * Adds a ChangeListener.  The change listeners are run each
     * time any one of the Bounded Range model properties changes.
     *
     * @param l the ChangeListener to add
     * @see #removeChangeListener
     * @see BoundedRangeModel#addChangeListener
     */
    public void addChangeListener(ChangeListener l) {
        listenerList.add(ChangeListener.class, l);
    }

    /** 
     * Run each ChangeListeners stateChanged() method.
     * 
     * @see #setRangeProperties
     * @see EventListenerList
     */
    protected void fireStateChanged() {
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == ChangeListener.class) {
                if (changeEvent == null) {
                    changeEvent = new ChangeEvent(this);
                }
                ((ChangeListener) listeners[i + 1]).stateChanged(changeEvent);
            }
        }
    }

    /**
     * Return the model's extent.
     * @return the model's extent
     * @see #setExtent
     * @see BoundedRangeModel#getExtent
     */
    public int getExtent() {
        return extent;
    }

    /**
     * Return the model's maximum.
     * @return  the model's maximum
     * @see #setMaximum
     * @see BoundedRangeModel#getMaximum
     */
    public int getMaximum() {
        return maximum;
    }

    /**
     * Return the model's minimum.
     * @return the model's minimum
     * @see #setMinimum
     * @see BoundedRangeModel#getMinimum
     */
    public int getMinimum() {
        return minimum;
    }

    /**
     * Return the model's current value.
     * @return the model's current value
     * @see #setValue
     * @see BoundedRangeModel#getValue
     */
    public int getValue() {
        return ((Integer) subject.getValue()).intValue();
    }

    /**
     * Returns true if the value is in the process of changing
     * as a result of actions being taken by the user.
     *
     * @return the value of the valueIsAdjusting property
     * @see #setValue
     * @see BoundedRangeModel#getValueIsAdjusting
     */
    public boolean getValueIsAdjusting() {
        return isAdjusting;
    }

    /**
     * Initializes value, extent, minimum and maximum. Adjusting is false.
     * Throws an IllegalArgumentException if the following constraints
     * aren't satisfied:
     * <pre>
     * min <= value <= value+ext <= max
     * </pre>
     */
    private void initialize(int initialValue, int ext, int min, int max) {
        if ((max >= min)
            && (initialValue >= min)
            && ((initialValue + ext) >= initialValue)
            && ((initialValue + ext) <= max)) {
            this.extent = ext;
            this.minimum = min;
            this.maximum = max;
        } else {
            throw new IllegalArgumentException("invalid range properties");
        }
    }

    /**
     * Removes a ChangeListener.
     *
     * @param l the ChangeListener to remove
     * @see #addChangeListener
     * @see BoundedRangeModel#removeChangeListener
     */
    public void removeChangeListener(ChangeListener l) {
        listenerList.remove(ChangeListener.class, l);
    }

    /** 
     * Sets the extent to <I>n</I> after ensuring that <I>n</I> 
     * is greater than or equal to zero and falls within the model's 
     * constraints:
     * <pre>
     *     minimum <= value <= value+extent <= maximum
     * </pre>
     * @see BoundedRangeModel#setExtent
     */
    public void setExtent(int n) {
        int newExtent = Math.max(0, n);
        int value = getValue();
        if (value + newExtent > maximum) {
            newExtent = maximum - value;
        }
        setRangeProperties(value, newExtent, minimum, maximum, isAdjusting);
    }

    /** 
     * Sets the maximum to <I>n</I> after ensuring that <I>n</I> 
     * that the other three properties obey the model's constraints:
     * <pre>
     *     minimum <= value <= value+extent <= maximum
     * </pre>
     * @see BoundedRangeModel#setMaximum
     */
    public void setMaximum(int n) {
        int newMin = Math.min(n, minimum);
        int newValue = Math.min(n, getValue());
        int newExtent = Math.min(n - newValue, extent);
        setRangeProperties(newValue, newExtent, newMin, n, isAdjusting);
    }

    /** 
     * Sets the minimum to <I>n</I> after ensuring that <I>n</I> 
     * that the other three properties obey the model's constraints:
     * <pre>
     *     minimum <= value <= value+extent <= maximum
     * </pre>
     * @see #getMinimum
     * @see BoundedRangeModel#setMinimum
     */
    public void setMinimum(int n) {
        int newMax = Math.max(n, maximum);
        int newValue = Math.max(n, getValue());
        int newExtent = Math.min(newMax - newValue, extent);
        setRangeProperties(newValue, newExtent, n, newMax, isAdjusting);
    }

    /**
     * Sets all of the BoundedRangeModel properties after forcing
     * the arguments to obey the usual constraints:
     * <pre>
     *     minimum <= value <= value+extent <= maximum
     * </pre>
     * <p>
     * At most, one ChangeEvent is generated.
     * 
     * @see BoundedRangeModel#setRangeProperties
     * @see #setValue
     * @see #setExtent
     * @see #setMinimum
     * @see #setMaximum
     * @see #setValueIsAdjusting
     */
    public void setRangeProperties(
        int newValue,
        int newExtent,
        int newMin,
        int newMax,
        boolean adjusting) {
        if (newMin > newMax) {
            newMin = newMax;
        }
        if (newValue > newMax) {
            newMax = newValue;
        }
        if (newValue < newMin) {
            newMin = newValue;
        }

        /* Convert the addends to long so that extent can be 
         * Integer.MAX_VALUE without rolling over the sum.
         * A JCK test covers this, see bug 4097718.
         */
        if (((long) newExtent + (long) newValue) > newMax) {
            newExtent = newMax - newValue;
        }
        if (newExtent < 0) {
            newExtent = 0;
        }
        boolean isChange =
            (newValue != getValue())
                || (newExtent != extent)
                || (newMin != minimum)
                || (newMax != maximum)
                || (adjusting != isAdjusting);
        if (isChange) {
            setValue0(newValue);
            extent = newExtent;
            minimum = newMin;
            maximum = newMax;
            isAdjusting = adjusting;
            fireStateChanged();
        }
    }

    /** 
     * Sets the current value of the model. For a slider, that
     * determines where the knob appears. Ensures that the new 
     * value, <I>n</I> falls within the model's constraints:
     * <pre>
     *     minimum <= value <= value+extent <= maximum
     * </pre>
     * 
     * @see BoundedRangeModel#setValue
     */
    public void setValue(int n) {
        int newValue = Math.max(n, minimum);
        if (newValue + extent > maximum) {
            newValue = maximum - extent;
        }
        setRangeProperties(newValue, extent, minimum, maximum, isAdjusting);
    }

    private void setValue0(int newValue) {
        subject.setValue(new Integer(newValue));
    }

    /**
     * Sets the valueIsAdjusting property.
     * 
     * @see #getValueIsAdjusting
     * @see #setValue
     * @see BoundedRangeModel#setValueIsAdjusting
     */
    public void setValueIsAdjusting(boolean b) {
        setRangeProperties(getValue(), extent, minimum, maximum, b);
    }

    /**
     * Returns a string that displays all of the BoundedRangeModel properties.
     */
    public String toString() {
        String modelString =
            "value="
                + getValue()
                + ", "
                + "extent="
                + getExtent()
                + ", "
                + "min="
                + getMinimum()
                + ", "
                + "max="
                + getMaximum()
                + ", "
                + "adj="
                + getValueIsAdjusting();

        return getClass().getName() + "[" + modelString + "]";
    }
}