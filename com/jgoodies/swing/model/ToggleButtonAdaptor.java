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
 * 
 *
 * @author Karsten Lentzsch
 */

public final class ToggleButtonAdaptor extends JToggleButton.ToggleButtonModel {
	
	protected final ValueModel subject;
	protected final Object selectedValue;
	protected final Object deselectedValue;
	
	
    /**
     * Constructs a <code>ToggleButtonAdaptor</code> on the given subject. 
     * 
     * @param subject   the subject that holds the value
     * @throws NullPointerException  if the subject is <code>null</code>
     */
	public ToggleButtonAdaptor(ValueModel subject) {
		this(subject, new Boolean(true), new Boolean(false));
	}
	
    /**
     * Constructs a <code>ToggleButtonAdaptor</code> on the given subject. 
     * 
     * @param subject          the subject that holds the value
     * @param selectedValue    the value that will be set if this is selected
     * @param deselectedValue  the value that will be set if this is deselected
     * @throws NullPointerException  if the subject is <code>null</code>
     */
	public ToggleButtonAdaptor(ValueModel subject, Object selectedValue, Object deselectedValue) {
		this.subject = subject;
		this.selectedValue   = selectedValue;
		this.deselectedValue = deselectedValue;
		registerListener();
	}
	
	
	public boolean isSelected() {
		super.isSelected();
		return isSelected0();
	}
	
	
	protected boolean isSelected0() {
		return selectedValue.equals(subject.getValue());
	}
	
	
	private void registerListener() {
		subject.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				boolean isSelected = isSelected0();
				setSelected0(isSelected, isSelected);
			}
		});
	}
	
	
	/**
	 * Sets the selected state of the button.
	 * @param b true selects the toggle button,
	 *          false deselects the toggle button.
	 */
	public void setSelected(boolean b) {
		if (isSelected() == b)
			return;
		setSelected0(b, true);
	}
	
	
	protected void setSelected0(boolean b, boolean updateSubject) {
		if (updateSubject)
			subject.setValue(b ? selectedValue : deselectedValue);
		super.setSelected(b);
		if (!Utilities.IS_BEFORE_14) 
		    fireStateChanged();
	}
}