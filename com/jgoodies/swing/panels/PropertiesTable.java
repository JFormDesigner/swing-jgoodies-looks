package com.jgoodies.swing.panels;

/*
 * Copyright (c) 2002 Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of Karsten Lentzsch.  
 * Use is subject to license terms.
 *
 */
 
import java.awt.GridLayout;
import java.awt.event.MouseListener;
import java.util.Enumeration;
import java.util.Properties;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import com.jgoodies.swing.util.TableUtilities;
import com.jgoodies.util.Utilities;

/**
 * A panel that displays a table reflecting an instance of <code>Properties</code>. 
 *
 * @author Karsten Lentzsch
 */
public class PropertiesTable extends JPanel {
	
	protected JTable table;

	protected String[] headers;
	protected Object[][] data;

	private final MyTableModel model;


	/**
	 * Constructs a <code>Properties Table</code> for the given 
	 * <code>Properties</code> and column headers.
	 */
	public PropertiesTable(Properties properties, String[] headers) {
		super(new GridLayout(0, 1));
		this.headers = headers;
		model = new MyTableModel();
		setData(properties);
		build();
	}
	
	
	/**
	 * Creates, configures, and wraps the table.
	 */
	protected void build() {
		table = new JTable(model);
		table.setAutoResizeMode(getAutoResizeMode());

		// Resize the columns to its preferred width.
		TableUtilities.resizeColumnsToPreferredWidth(table);

		// Modify the header for the value column to be aligned left.
		TableUtilities.leftAlignColumn(table.getColumnModel().getColumn(1));

		table.setRowSelectionAllowed(true);
		table.setColumnSelectionAllowed(false);
		table.setCellSelectionEnabled(false);
		table.setEnabled(true);
		table.setOpaque(false);
		JScrollPane scrollpane = new JScrollPane(table);
		add(scrollpane);
	}
	
	
	/**
	 * Adds a mouse listener to the table.
	 */
	public void addMouseListener(MouseListener l) {
		table.addMouseListener(l);
	}
	
	
	/**
	 * Answers the table's auto resize mode.
	 */
	protected int getAutoResizeMode() {
		return JTable.AUTO_RESIZE_ALL_COLUMNS;
	}
	
	
	/**
	 * Returns the table's model.
	 */
	public MyTableModel getTableModel() {
		return model;
	}
	
	
	/**
	 * Sets a new <code>Properties</code> instance as underlying model data.
	 */
	public void setData(Properties properties) {
		data = new Object[properties.size()][2];
		int row = 0;
		Enumeration e = Utilities.sort(properties.keys());
		while (e.hasMoreElements()) {
			String key = (String) e.nextElement();
			data[row][0] = key;
			data[row][1] = properties.get(key);
			row++;
		}
		model.updateData();
	}
	
	// Helper Class *********************************************************************

	// The table model	
	private class MyTableModel extends AbstractTableModel {
		public int getColumnCount() {
			return headers.length;
		}
		public int getRowCount() {
			return data.length;
		}
		public Object getValueAt(int row, int col) {
			return row < data.length ? data[row][col] : null;
		}
		public String getColumnName(int col) {
			return headers[col];
		}
		public void updateData() {
			fireTableDataChanged();
		}
	}
	
}