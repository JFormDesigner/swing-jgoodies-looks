package com.jgoodies.swing.convenience;

/*
 * Copyright (c) 2002 Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of Karsten Lentzsch.  
 * Use is subject to license terms.
 *
 */
import javax.swing.JTable;

import com.jgoodies.swing.panels.PropertiesTable;
import com.jgoodies.util.Utilities;

/**
 * A <code>PropertiesTable</code> for the <code>System</code> properties. 
 *
 * @see	PropertiesTable
 * @see	System
 * @see	Utilities
 * 
 * @author Karsten Lentzsch
 */

public final class SystemTable extends PropertiesTable {
	
	/**
	 * Constructs a <code>SystemTable</code>.
	 */
	public SystemTable() {
		super(Utilities.getExtendedSystemProperties(), new String[] { "Property", "   Value" });
	}
	
	
	/**
	 * Answers the table's auto resize mode; is off by default.
	 */
	protected int getAutoResizeMode() {
		return JTable.AUTO_RESIZE_OFF;
	}
	
	
	/**
	 * Answers the table's name.
	 */
	public String getTableName() {
		return "System Properties Table";
	}
	
	
	/**
	 * Updates the table by setting new data.
	 */
	public void update() {
		setData(Utilities.getExtendedSystemProperties());
	}
}