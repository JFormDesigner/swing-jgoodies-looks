package com.jgoodies.swing;

/*
 * Copyright (c) 2002 Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of Karsten Lentzsch.  
 * Use is subject to license terms.
 *
 */
 
import java.awt.Component;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

import com.jgoodies.swing.util.TableUtilities;
import com.jgoodies.util.Utilities; 

/**
 * A <code>JTable</code> subclass that can align and size columns.
 * Therefore it utilizes two arrays and a <code>List</code> for 
 * the column alignment, header alignment, and the column size hints.
 *
 * @author Karsten Lentzsch
 */

public class ExtTable extends JTable {
	
	private int[]    columnAlignments	= null;
	private int[]	  headerAlignments	= null;
	private List	  columnSizeHints;


	/**
	 * Constructs an <code>ExtTable</code>.
	 */	
	public ExtTable() {
		configureTable();
	}

	/**
	 * Constructs an <code>ExtTable</code> for the specified row data and
	 * column names.
	 */	
    public ExtTable(Object[][] rowData, Object[] columnNames) {
    	super(rowData, columnNames);
    	configureTable();
    }
    
    
    /**
     * Configures the table.
     */
    protected void configureTable() {
		setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

		// Modify the cell renderer.
		setDefaultRenderer(Object.class, createDefaultTableCellRenderer());
		setDefaultRenderer(Boolean.class, new BooleanRenderer());
		setDefaultEditor  (Boolean.class, new BooleanEditor());		

		// Set the selection configuration.
		setRowSelectionAllowed(false);
		setColumnSelectionAllowed(false);
		setCellSelectionEnabled(false);
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		setEnabled(false);
		//setOpaque(false);
    }

	
	/**
	 * Configures the column headers: set the alignment if available.
	 */
	private void configureColumnHeaders() {
		if (null == headerAlignments)
			return;

		for (int col = 0; col < getModel().getColumnCount(); col++) {
			if (headerAlignments[col] == JLabel.LEFT)
				TableUtilities.leftAlignColumn(getColumnModel().getColumn(col));
		}
	}
	
	
	/**
	 * Configures the column sizes - if available.
	 */
	private void configureColumnSizes() {
		if (null == columnSizeHints)
			return;
			
		for (int col = 0; col < getModel().getColumnCount(); col++) {
			int sizeHint = ((Integer) columnSizeHints.get(col)).intValue();
			if (sizeHint != -1)
				getColumnModel().getColumn(col).setPreferredWidth(sizeHint);
		}
	}
	
	
	/**
	 * Creates and answer the default table cell renderer.
	 */
	protected TableCellRenderer createDefaultTableCellRenderer() {
		return new MyDefaultTableCellRenderer();
	}
	
	
	/**
	 * Answers the column alignment for the given column index.
	 */
	public int getColumnAlignment(int col) {
		return null == columnAlignments ? JLabel.LEFT : columnAlignments[col];
	}
	
	
	/**
	 * Answers an array of all column alignments.
	 */
	public int[] getColumnAlignments() {
		return columnAlignments;
	}
	

	/**
	 * Sets all column alignments.
	 */
	public void setColumnAlignments(int[] alignments) {
		this.columnAlignments = alignments;
	}
	
	
	/**
	 * Eagerly releases resources, here: the model.
	 */
	public void release() {
		setModel(new DefaultTableModel(2, 2));
	}
	
	
	/**
	 * Resets the selection.
	 */
	public void resetSelection() {
		getSelectionModel().clearSelection();
	}
	
	
	/**
	 * Sets all columns sizes using the given array of column size hints.
	 */
	public void setColumnSizes(int[] columnSizeHints) {
		List hints = new ArrayList();
		for (int i = 0; i < columnSizeHints.length; i++)
			hints.add(new Integer(columnSizeHints[i]));
		setColumnSizes(hints);
	}
	
	
	/**
	 * Sets all columns sizes using the given <code>List</code> of column size
	 * hints.
	 */
	public void setColumnSizes(List columnSizeHints) {
		this.columnSizeHints = columnSizeHints;
	}
	
	
	/**
	 * Sets all header alignments using the given array of alignments.
	 */
	public void setHeaderAlignments(int[] alignments) {
		this.headerAlignments = alignments;
	}
	
	
	/**
	 * Sets a new model. In addition to the superclass, 
	 * we have to update the alignment and column sizes.
	 */
	public void setModel(TableModel model) {
		super.setModel(model);
		configureColumnHeaders();
		configureColumnSizes();
		revalidate();
	}
	
	
	/**
	 * Table has changed; perform appropriate actions.
	 */
	public void tableChanged(TableModelEvent e) {
		super.tableChanged(e);
		if (e.getType() == TableModelEvent.UPDATE) {
			resetSelection();
		}
	}
	
	
	/**
	 * Updates the UI. In addition to the superclass behavior, 
	 * we set the font size and row height.
	 */
	public void updateUI() {
		configureEnclosingScrollPane();
		super.updateUI();
		int tableFontSize    = UIManager.getFont("Table.font").getSize();
		int minimumRowHeight = tableFontSize + 4;
		int defaultRowHeight = Utilities.IS_LOW_RES ? 17 : 18;
		setRowHeight(Math.max(minimumRowHeight, defaultRowHeight));
	}
	
	
	/*
	 * A default renderer that honors the columns alignment.
	 */
	public static class MyDefaultTableCellRenderer extends DefaultTableCellRenderer {

		private static final Border EMPTY_BORDER = BorderFactory.createEmptyBorder(0, 4, 0, 4);

		public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
			JLabel label =
				(JLabel) super.getTableCellRendererComponent(table, value, isSelected, false, row, column);
			ExtTable decTable = (ExtTable) table;
			label.setHorizontalAlignment(decTable.getColumnAlignment(table.convertColumnIndexToModel(column)));
			label.setBorder(EMPTY_BORDER);
			return label;
		}
	}
	

	
	
    // Unlike the default BooleanEditor, we set the "border painted flat" property.
	public static class BooleanEditor extends DefaultCellEditor {
		public BooleanEditor() {
		    super(new JCheckBox());
		    JCheckBox checkBox = (JCheckBox)getComponent();
		    checkBox.setHorizontalAlignment(JCheckBox.CENTER);
		    checkBox.setBorderPaintedFlat(true);
		}
    }
    
    
    // Unlike the default BooleanRenderer, we set the "border painted flat" property.
    public static class BooleanRenderer extends JCheckBox implements TableCellRenderer {
		public BooleanRenderer() {
		    setHorizontalAlignment(JLabel.CENTER);
		    setBorderPaintedFlat(true);
		}

        public Component getTableCellRendererComponent(JTable table, Object value,
						       boolean isSelected, boolean hasFocus, int row, int column) {
		    if (isSelected) {
		        setForeground(table.getSelectionForeground());
		        super.setBackground(table.getSelectionBackground());
		    } else {
		        setForeground(table.getForeground());
		        setBackground(table.getBackground());
		    }
	        setSelected((value != null && ((Boolean)value).booleanValue()));
	        return this;
	    }
	    
	    /**
	     * Overridden for performance reasons.
	     * See the <a href="#override">Implementation Note</a> 
	     * for more information.
	     */
	    public void validate() {}
	
	    /**
	     * Overridden for performance reasons.
	     * See the <a href="#override">Implementation Note</a> 
	     * for more information.
	     */
	    public void revalidate() {}
	
	    /**
	     * Overridden for performance reasons.
	     * See the <a href="#override">Implementation Note</a> 
	     * for more information.
	     */
	    public void repaint(long tm, int x, int y, int width, int height) {}
	
	    /**
	     * Overridden for performance reasons.
	     * See the <a href="#override">Implementation Note</a> 
	     * for more information.
	     */
	    public void repaint(Rectangle r) { }
	
	    /**
	     * Overridden for performance reasons.
	     * See the <a href="#override">Implementation Note</a> 
	     * for more information.
	     */
	    protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {	
			// Strings get interned...
			if (propertyName=="text") {
		    	super.firePropertyChange(propertyName, oldValue, newValue);
			}
	    }
	
	    /**
	     * Overridden for performance reasons.
	     * See the <a href="#override">Implementation Note</a> 
	     * for more information.
	     */
	    public void firePropertyChange(String propertyName, boolean oldValue, boolean newValue) { }
	
    }
    
	
}