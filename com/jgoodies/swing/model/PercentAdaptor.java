/*
 * Copyright (c) 2003 JGoodies Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of JGoodies Karsten Lentzsch.  
 * Use is subject to license terms.
 *
 */

package com.jgoodies.swing.model;


/**
 * This implementation of <code>ValueModel</code> converts floats 
 * to their corresponding percent values as integers and vice versa.
 * <p>
 * <b>Note: This class has been moved to the JDiskReport
 * sample applications under class name <code>PercentConverter</code>.
 * It will be removed from a future version of this library 
 * - likely in version 1.2 of the Swing Suite.</b>
 *
 * @author Karsten Lentzsch
 * @deprecated replaced by the JDiskReport <code>PercentConverter</code>
 */
public final class PercentAdaptor extends AbstractValueModel {

    private final ValueModel subject;

    /**
     * Constructs a <code>PercentAdaptor</code> on the given subject.
     * 
     * @param subject     the underlying <code>ValueModel</code>
     */
    public PercentAdaptor(ValueModel subject) {
        this.subject = subject;
    }

    /** 
     * Converts the value and answers a percent value.
     * Converts the subject's current <code>Float</code> value as 
     * corresponding <code>Integer</code> percent value.
     * 
     * @return the subject's value as percent
     * @throws ClassCastException if the subject value is not of type
     * <code>Float</code>
     */
    public Object getValue() {
        float factor = ((Float) subject.getValue()).floatValue();
        return new Integer(Math.round(factor * 100));
    }

    /** 
     * Converts a percent to <code>Float</code> and sets it as new value.
     * 
     * @param newValue  the <code>Integer</code> object that represents
     * the percent value
     * @throws ClassCastException if the new value is not of type
     * <code>Integer</code>
     */
    public void setValue(Object newValue) {
        int percent = ((Integer) newValue).intValue();
        subject.setValue(new Float(percent / 100.0f));
    }
}