package com.jgoodies.swing.model;

/*
 * Copyright (c) 2002 Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of Karsten Lentzsch.  
 * Use is subject to license terms.
 *
 */
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.TableModel;
import javax.swing.table.AbstractTableModel;

/**
 * This implementation of the <code>TableModel</code> interface just
 * delegates all requests to an underlying TableModel.
 * <p>
 * It can be used to change a <code>JTable</code>'s model while the
 * table is beeing displayed and "live".
 *
 * @author Karsten Lentzsch
 */
public final class TableAdaptor extends AbstractTableModel {

    /**
     * Holds a model that in turn holds the delegated <code>TableModel</code>.
     */
    private final ValueModel delegateChannel;


    // Instance Creation ******************************************************
    
    /**
     * Constructs a <code>TableAdaptor</code> that holds the delegated
     * <code>TableModel</code> in the given delegate channel.
     * 
     * @param delegateChannel    a <code>ValueModel</code> that holds the
     *     <code>TableModel</code>
     * @throws NullPointerException   if delegateChannel is null
     */
    public TableAdaptor(ValueModel delegateChannel) {
        this.delegateChannel = delegateChannel;
        if (delegateChannel == null) 
            throw new NullPointerException("The delegate channel must not be null.");
             
        delegateChannel.addChangeListener(new DelegateChangeHandler());
    }
    
    /**
     * Constructs a <code>TableAdaptor</code> on the given 
     * delegate <code>TableModel</code>.
     * 
     * @param delegate    a <code>TableModel</code> that is used as initial
     *     <code>TableModel</code> delegate
     */
    public TableAdaptor(TableModel delegate) {
        this(new ValueHolder(delegate));
    }

    /**
     * Constructs a <code>TableAdaptor</code> on a dummy <code>TableModel</code>.
     */
    public TableAdaptor() {
        this(new DummyTableModel());
    }
    
    
    // Accessing the Delegate ************************************************

    /**
     * Returns the current <code>TableModel</code> delegate.
     * 
     * @return the current <code>TableModel</code> delegate
     */
    public TableModel getDelegate() {
        return (TableModel) delegateChannel.getValue();
    }
    
    /**
     * Sets a new <code>TableModel</code> delegate.
     * 
     * @param newDelegate    the new <code>TableModel</code> delegate
     */
    public void setDelegate(TableModel newDelegate) {
        delegateChannel.setValue(newDelegate);
    }

    /**
     * Sets a new <code>TableModel</code> delegate.
     * 
     * @param newDelegate    the new <code>TableModel</code> delegate
     * @deprecated replaced by #setDelegate
     */
    public void setSubject(TableModel newDelegate) {
        setDelegate(newDelegate);
    }


    // TableModel Implementation **********************************************
    
    public Class getColumnClass(int columnIndex) {
        return getDelegate().getColumnClass(columnIndex);
    }

    public int getColumnCount() {
        return getDelegate().getColumnCount();
    }

    public String getColumnName(int col) {
        return getDelegate().getColumnName(col);
    }

    public int getRowCount() {
        return getDelegate().getRowCount();
    }

    public Object getValueAt(int row, int col) {
        return getDelegate().getValueAt(row, col);
    }
    
    
    // Helper Classes ********************************************************

    // Listens to changes of the subject.
    private class DelegateChangeHandler implements ChangeListener {

        /**
         * The delegate has been changed. Fire a table data change.
         */
        public void stateChanged(ChangeEvent evt) {
            fireTableDataChanged();
        }
    }


    // A dummy model that can be used by the default constructor.
    private static class DummyTableModel extends AbstractTableModel {
        public String getColumnName(int column) {
            return "Dummy";
        }
        public int getRowCount() {
            return 1;
        }
        public int getColumnCount() {
            return 0;
        }
        public Object getValueAt(int row, int col) {
            return "";
        }
    };

}