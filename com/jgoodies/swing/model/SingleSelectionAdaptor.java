/*
 * Copyright (c) 2003 JGoodies Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of JGoodies Karsten Lentzsch.  
 * Use is subject to license terms.
 *
 */

package com.jgoodies.swing.model;

import javax.swing.DefaultSingleSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * This implementation of <code>SingleSelectionModel</code> also 
 * keeps track of the selection utilizing a <code>ValueModel</code>. 
 * It can be used to easily backup a selection.
 * <p>
 * <b>Note: This class has been moved to the JDiskReport
 * sample applications under class name <code>PercentConverter</code>.
 * It will be removed from a future version of this library 
 * - likely in version 1.2 of the Swing Suite.</b>
 *
 * @author Karsten Lentzsch
 * @deprecated replaced by the JDiskReport <code>SingleSelectionAdapter</code>
 */
public final class SingleSelectionAdaptor extends DefaultSingleSelectionModel {
	
	private final ValueModel subject;
	private final Object[]   values;
	
	
	public SingleSelectionAdaptor(ValueModel subject, Object[] values) {
		this.subject = subject;
		this.values = values;
		registerListener();
	}
	
	
	public int getSelectedIndex() {
		Object selectedValue = subject.getValue();

		for (int i = 0; i < values.length; i++)
			if (values[i].equals(selectedValue))
				return i;

		return -1;
	}
	
	
	public Object getSelectedValue() {
		return getSelectedValue(super.getSelectedIndex());
	}
	
	
	private Object getSelectedValue(int index) {
		return (0 <= index) && (index < values.length) ? values[index] : null;
	}
	
	
	protected void registerListener() {
		subject.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				setSelectedIndex(getSelectedIndex());
			}
		});
	}
	
	
	public void setSelectedIndex(int index) {
		super.setSelectedIndex(index);
		setSelectedIndex0(index);
	}
	
	
	private void setSelectedIndex0(int index) {
		subject.setValue(getSelectedValue(index));
	}
}