package com.jgoodies.swing.model;

/*
 * Copyright (c) 2003 JGoodies Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of JGoodies Karsten Lentzsch.  
 * Use is subject to license terms.
 *
 */
import java.util.List;

import javax.swing.AbstractListModel;

/**
 * A simple implementation of the {@link javax.swing.ListModel} interface 
 * that utilizes a {@link java.util.List} to hold elements.
 *
 * @author Karsten Lentzsch
 */

public final class SimpleListModel extends AbstractListModel {

    /**
     * Holds the elements.
     */
    private final List list;

    
    // Instance Creation ****************************************************

    /**
     * Constructs a <code>SimpleListModel</code> on the given <code>List</code>.
     * @param list   the underlying <code>List</code>
     */
    public SimpleListModel(List list) {
        this.list = list;
    }


    // ListModel Implementation *********************************************

   /** 
    * Returns the length of the list.
    * @return the length of the list
    */
    public int getSize() {
        return list.size();
    }

    /**
     * Returns the value at the specified index.  
     * @param index the requested index
     * @return the value at <code>index</code>
     */
    public Object getElementAt(int index) {
        return list.get(index);
    }
 
    
    // List Manipulation ****************************************************

    /**
     * Inserts the specified element at the specified position in this list.
     *
     * @param index    index at which the specified element is to be inserted
     * @param element  element to be inserted
     * @throws <code>ArrayIndexOutOfBoundsException</code> if the
     * index is out of range
     * (<code>index &lt; 0 || index &gt; size()</code>).
     */
    public void add(int index, Object element) {
        list.add(index, element);
        fireIntervalAdded(this, index, index);
    }

    /**
     * Removes the element at the specified position in this list.
     * Returns the element that was removed from the list.
     *
     * @param index   the index of the element to removed
     * @throws <code>ArrayIndexOutOfBoundsException</code>
     * if the index is out of range
     * (<code>index &lt; 0 || index &gt;= size()</code>).
     */
    public Object remove(int index) {
        Object rv = list.get(index);
        list.remove(index);
        fireIntervalRemoved(this, index, index);
        return rv;
    }

    /**
     * Notifies listeners that the list changed.
     */
    public void fireContentsChanged() {
        fireContentsChanged(this, 0, getSize() - 1);
    }
}