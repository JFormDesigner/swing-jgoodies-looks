package com.jgoodies.swing.model;

/*
 * Copyright (c) 2002 Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of Karsten Lentzsch.  
 * Use is subject to license terms.
 *
 */
 
import javax.swing.JToggleButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.jgoodies.util.Utilities;


/**
 * An implementation of <code>ToggleButtonModel</code> that uses 
 * a {@link ValueModel} to keep track of the current selection. 
 * The <code>choice</code> describes the value that indicates, 
 * that this model is the one (and only) radio choice that is 
 * currently selected.
 *
 * @author Karsten Lentzsch
 */

public final class RadioButtonAdaptor extends JToggleButton.ToggleButtonModel {
    
	private final ValueModel subject;
	private final Object choice;
	
	/**
     * Constructs a <code>RadioButtonAdaptor</code> on the given subject
     * for the specified choice. 
     * 
	 * @param subject   the subject that holds the value
	 * @param choice    the choice that indicates that this is selected
     * @throws NullPointerException  if the subject is <code>null</code>
	 */
	public RadioButtonAdaptor(ValueModel subject, Object choice) {
		this.subject = subject;
		this.choice = choice;
		registerListener();
	}
	
	public boolean isSelected() {
		super.isSelected();
		return isSelected0();
	}
	
	protected boolean isSelected0() {
		return choice.equals(subject.getValue());
	}
	
	protected void registerListener() {
		subject.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				setSelected0(isSelected0());
			}
		});
	}
	
	/**
	 * Sets the selected state of the button.
	 * @param b true selects the toggle button,
	 *          false deselects the toggle button.
	 */
	public void setSelected(boolean b) {
		if (!b || isSelected() == b)
			return;
		setSelected0(b);
	}
	
	protected void setSelected0(boolean b) {
		if (b)
			subject.setValue(choice);
		super.setSelected(b);
		if (!Utilities.IS_BEFORE_14) {
			//System.out.println("choice = " + choice + "; value=" + subject.getValue() + "; isSelected=" + isSelected0());
		    fireStateChanged();
		}
	}
	
}