package com.jgoodies.swing.util;

/*
 * Copyright (c) 2003 JGoodies Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of JGoodies Karsten Lentzsch.  
 * Use is subject to license terms.
 *
 */
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

/**
 * This class consists of static methods that provide useful code for handling tables.
 *
 * @author Karsten Lentzsch
 */
public final class TableUtilities {
    
	
 	/**
 	 * Modify the column's header to be aligned left.
 	 */
	public static void leftAlignColumn(TableColumn column) {
		TableCellRenderer headerRenderer = column.getHeaderRenderer();
		if (headerRenderer instanceof DefaultTableCellRenderer)
			 ((DefaultTableCellRenderer) headerRenderer).setHorizontalAlignment(JLabel.LEFT);
		else
			column.setHeaderRenderer(UIFactory.createLeftAlignedDefaultHeaderRenderer());
	}
	
	
	/**
	 *  Guess the maximum cell width in column.
	 */
	private static int getMaximumColumnWidth(TableModel model, int column) {
		int maximumCellWidth = 0;
		int rows = model.getRowCount();
		for (int i = 0; i < rows; i++) {
			Object value = model.getValueAt(i, column);
			String cellString = (value == null) ? "" : value.toString();
			Component cell = new JLabel(cellString);
			int cellWidth = cell.getPreferredSize().width;
			if (cellWidth > maximumCellWidth)
				maximumCellWidth = cellWidth;
		}
		return maximumCellWidth + 6;
	}
	
	
	/**
	 * Try to resize the table's columns to get the preferred size of their contents.
	 */
	public static void resizeColumnsToPreferredWidth(JTable table) {
		TableColumnModel columnModel = table.getColumnModel();
		TableModel model = table.getModel();
		int columnCount = model.getColumnCount();
		for (int i = 0; i < columnCount; i++) {
			TableColumn column = columnModel.getColumn(i);
			int preferredWidth = getMaximumColumnWidth(model, i);
			column.setPreferredWidth(Math.max(50, preferredWidth));
		}
	}
	
	
	/** 
	 *  Creates and answers a comma separated string from the details table.
	 */
	public static StringBuffer writeAsExportString(TableModel model) {
		final String columnSeparator = "\t";
		final String rowSeparator = "\n";
		//
		StringBuffer buffer = new StringBuffer(500);
		int numRows = model.getRowCount();
		int numCols = model.getColumnCount();

		buffer.append(rowSeparator);
		buffer.append(rowSeparator);

		// Append column names
		for (int col = 0; col < numCols; col++) {
			buffer.append(model.getColumnName(col));
			if (col < numCols - 1)
				buffer.append(columnSeparator);
		}
		buffer.append(rowSeparator);

		// Append table contents
		for (int row = 0; row < numRows; row++) {
			for (int col = 0; col < numCols; col++) {
				buffer.append(model.getValueAt(row, col));
				if (col < numCols - 1)
					buffer.append(columnSeparator);
			}
			buffer.append(rowSeparator);
		}
		return buffer;
	}
	

	
}