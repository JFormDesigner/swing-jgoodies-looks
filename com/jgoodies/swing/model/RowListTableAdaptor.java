package com.jgoodies.swing.model;

/*
 * Copyright (c) 2003 JGoodies Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of JGoodies Karsten Lentzsch.  
 * Use is subject to license terms.
 *
 */
import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

/**
 * This implementation of <code>TableModel</code> uses a List of rows,
 * where each row is an equally sized List of values.
 * In addition, of List of headers is utilized.
 *
 * @author Karsten Lentzsch
 */

public final class RowListTableAdaptor extends AbstractTableModel {

    private final List headers;
    private List rows = new ArrayList();

    public RowListTableAdaptor(List headers) {
        this.headers = headers;
    }

    public int getColumnCount() {
        return headers.size();
    }

    public String getColumnName(int col) {
        return (String) headers.get(col);
    }

    public int getRowCount() {
        return rows.size();
    }

    public Object getValueAt(int row, int col) {
        return ((List) rows.get(row)).get(col);
    }

    public void setRows(List rows) {
        this.rows = rows;
        fireTableDataChanged();
    }
}