package com.jgoodies.swing.model;

/*
 * Copyright (c) 2003 JGoodies Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of JGoodies Karsten Lentzsch.
 * Use is subject to license terms.
 *
 */
 
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * A {@link ValueModel} that references two other value models called 
 * its subject and trigger channel. When a <code>BufferedValueHolder</code>
 * receives a new value, it holds onto the new value and does not update 
 * the subject until the trigger channel becomes true.
 *
 * @author Karsten Lentzsch
 */

public final class BufferedValueHolder extends AbstractValueModel {

    private final ValueModel subject;
    private final ValueModel triggerChannel;

    private Object bufferedValue;
    private Object readValue;
    private boolean valueHasBeenRead = false;

    // Instance Creation ****************************************************

    /**
     * Constructs a <code>BufferedValueHolder</code> on the given subject
     * using the given trigger channel. Subject changes will be observed.
     * 
     * @param subject          the underlying value model
     * @param triggerChannel   the model that initiates the write through
     * @throws NullPointerException   if subject is null
     * @throws NullPointerException   if triggerChannel is null
     */
    public BufferedValueHolder(
        ValueModel subject,
        ValueModel triggerChannel) {
        this(subject, triggerChannel, true);
    }

    /**
     * Constructs a <code>BufferedValueHolder</code> on the given subject
     * using the given <code>Trigger</code>.
     * 
     * @param subject          the underlying value model
     * @param triggerChannel   the model that initiates the write through
     * @param observeChanges   indicates whether the buffered value shall 
     *    be updated if the subject's value changes
     * @throws NullPointerException   if subject is null
     * @throws NullPointerException   if triggerChannel is null
     */
    public BufferedValueHolder(
        ValueModel subject,
        ValueModel triggerChannel,
        boolean observeChanges) {
        this.subject = subject;
        this.triggerChannel = triggerChannel;
        if (subject == null)
            throw new NullPointerException("The subject must not be null.");
        if (triggerChannel == null)
            throw new NullPointerException("The trigger channel must not be null.");
        if (observeChanges) {
            subject.addChangeListener(new SubjectChangeHandler());
        }
        triggerChannel.addChangeListener(new TriggerChangeHandler());
    }

    // Implementing the ValueModel Interface ********************************

    /**
     * Answers the buffered value. As a side effect the value will
     * be read from the underlying subject if it hasn't been read before.
     * 
     * @return Object   the observed value
     */
    public Object getValue() {
        if (!valueHasBeenRead) {
            bufferedValue = readValue = subject.getValue();
            //System.out.println("Reading value: " + bufferedValue.toString());
            valueHasBeenRead = true;
        }
        return bufferedValue;
    }

    /**
     * Sets a new value. Does nothing if it equals the old value.
     * The new value will not be written through to underlying model
     * until the trigger has been fired.
     * 
     * @param newValue   the value to set
     */
    public void setValue(Object newValue) {
        if (!equals(getValue(), newValue))
            setValue0(newValue);
    }

    // Misc *****************************************************************

    /**
     * Sets the buffered value to the given object, fires a change. The
     * subject will only be updated if the trigger has been triggered.
     * 
     * @param newValue	 the value to set
     */
    private void setValue0(Object newValue) {
        bufferedValue = newValue;
        //System.out.println("Setting buffered value to: " + o.toString());
        fireStateChanged();
        if (triggerHasBeenTriggered()) {
            //System.out.println("Writing value to: " + o.toString());
            subject.setValue(newValue);
        }
    }

    /**
     * Checks and answers whether the trigger channel returns true.
     * If so, we are going to write through value changes to the subject.
     * 
     * @return whether the trigger channel returns true.
     */
    private boolean triggerHasBeenTriggered() {
        return ((Boolean) triggerChannel.getValue()).booleanValue();
    }

    private void writeThrough() {
        if (!equals(getValue(), readValue))
            setValue0(bufferedValue);
    }

    /*
     * Listens to changes of the subject.
     */
    private class SubjectChangeHandler implements ChangeListener {

        /**
         * The subject has been changed. 
         * Read the subject value again and notify all listeners.
         */
        public void stateChanged(ChangeEvent evt) {
            valueHasBeenRead = false;
            fireStateChanged();
        }
    }

    /*
     * Listens to changes of the trigger channel.
     */
    private class TriggerChangeHandler implements ChangeListener {

        /**
         * The trigger has been changed. Writes the buffered value through.
         */
        public void stateChanged(ChangeEvent evt) {
            writeThrough();
        }
    }

}