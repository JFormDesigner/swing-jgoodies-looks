package com.jgoodies.swing;

/*
 * Copyright (c) 2002 Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of Karsten Lentzsch.  
 * Use is subject to license terms.
 *
 */
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ButtonModel;

import com.jgoodies.swing.model.RadioButtonAdaptor;
import com.jgoodies.swing.model.ToggleButtonAdaptor;
import com.jgoodies.swing.model.ValueModel;

/**
 * This implementation of the <code>Action</code> interface is used 
 * to create toggle components in action containers, for example, 
 * instances of <code>JCheckBox</code>, or <code>JRadioButtonMenuItem</code>.
 *
 * @see	ButtonModel
 * 
 * @author Karsten Lentzsch
 */
public final class ToggleAction extends AbstractAction {

    private final ButtonModel model;

    private boolean isListener = false;

    /**
     * Constructs a <code>ToggleAction</code> for the given
     * <code>ButtonModel</code>.
     */
    public ToggleAction(ButtonModel model) {
        this.model = model;
    }

    /**
     * Creates and answer a <code>ToggleAction</code> for the specified
     * <code>ValueModel</code>; useful for <code>JCheckButtons</code>.
     */
    public static ToggleAction createCheck(ValueModel subject) {
        return new ToggleAction(new ToggleButtonAdaptor(subject));
    }

    /**
     * Creates and answer a <code>ToggleAction</code> for the given
     * <code>ValueModel</code> and selection values; useful for
     * <code>JCheckButtons</code>.
     */
    public static ToggleAction createCheck(
        ValueModel subject,
        Object selectedValue,
        Object deselectedValue) {
        return new ToggleAction(
            new ToggleButtonAdaptor(subject, selectedValue, deselectedValue));
    }

    /**
     * Creates and answer a <code>ToggleAction</code> for the given
     * <code>ValueModel</code> and choice useful for <code>JRadioButtons</code>.
     */
    public static ToggleAction createRadio(
        ValueModel subject,
        Object choice) {
        return new ToggleAction(new RadioButtonAdaptor(subject, choice));
    }

    /**
     * Answers if a listener has already been added to this action.
     * @return boolean
     */
    public boolean isListener() {
        return isListener;
    }
    
    /**
     * Sets the listener boolean property; used to avoid duplicate listeners.
     */
    public void setListener(boolean b) {
        this.isListener = b;
    }
    
    /**
     * Answers the button model.
     * @return ButtonModel
     */
    public ButtonModel model() {
        return model;
    }

    /**
     * Checks and answers if this is an action for a radio or check component,
     * e.g. <code>JRadioButton</code> or <code>JCheckBox</code>.
     */
    public boolean isRadio() {
        return model() instanceof RadioButtonAdaptor;
    }

    /**
     * Do nothing.
     */
    public void actionPerformed(ActionEvent e) {}
}