package com.jgoodies.util;

/*
 * Copyright (c) 2002 JGoodies Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of JGoodies Karsten Lentzsch.
 * Use is subject to license terms.
 *
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * This class provides behavior for getting forward and backward
 * in a fixed list of recently used items. It is used as a raw model
 * for the back and forward navigation buttons.<p>
 * 
 * TODO: Consider forwarding all actions to a list instead of 
 *       subclassing Arraylist.
 *
 * @author Karsten Lentzsch
 */

public final class HistoryList extends ArrayList {

    private final int limit;
    private int indexOfNextAdd;

    /**
     * Constructs a <code>HistoryList</code> with the given capacity limit.
     */
    public HistoryList(int limit) {
        super(limit);
        this.limit = limit;
        indexOfNextAdd = 0;
    }

    /** 
     * Adds an element to the history if it is not the previous element.
     * Returns whether the history changed.
     */
    public boolean add(Object o) {
        // If the previous element equals this one, do nothing and answer false.
        if (o.equals(getLastAdded()))
            return false;

        // Trim from the indexOfNextAdd to the end, as we'll
        // never reach these elements once the new one is added.
        removeRange(indexOfNextAdd, size());
        super.add(indexOfNextAdd, o);

        // If the limit is exceeded remove the least recently added element.
        // Otherwise increase the indexOfNextAdd.
        if (size() > limit)
            remove(0);
        else
            indexOfNextAdd++;

        // Answer true to indicate that the element has been added.
        return true;
    }

    /**
     * Answers the previous element and goes back - if possible.
     */
    public Object getAndGoPrevious() {
        return (hasPrevious()) ? get(--indexOfNextAdd - 1) : null;
    }

    /**
     * Answers the next element and goes next - if possible.
     */
    public Object getAndGoNext() {
        return (hasNext()) ? get(indexOfNextAdd++) : null;
    }

    /**
     * Answers an <code>Iterator</code> for the available backward elements.
     */
    public Iterator getBackIterator() {
        List list = subListNoDuplicates(0, indexOfNextAdd - 1);
        Collections.reverse(list);
        return list.iterator();
    }

    /**
     * Answers an <code>Iterator</code> for the available next elements.
     */
    public Iterator getNextIterator() {
        return subListNoDuplicates(indexOfNextAdd, size()).iterator();
    }

    /**
     * Returns the element that was added last.
     */
    public Object getLastAdded() {
        return (indexOfNextAdd > 0) ? get(indexOfNextAdd - 1) : null;
    }

    /**
     * Returns the previous element.
     */
    public Object getPrevious() {
        return (hasPrevious()) ? get(indexOfNextAdd - 2) : null;
    }

    /**
     * Returns the next element.
     */
    public Object getNext() {
        return (hasNext()) ? get(indexOfNextAdd) : null;
    }

    /**
     * Goes back the specified number of steps.
     */
    public void goBack(int steps) {
        indexOfNextAdd = Math.max(0, indexOfNextAdd - steps);
    }

    /**
     * Goes forward the specified number of steps.
     */
    public void goNext(int steps) {
        indexOfNextAdd = Math.min(indexOfNextAdd + steps, size());
    }

    /**
     * Checks and answers if there's a previous element.
     */
    public boolean hasPrevious() {
        return indexOfNextAdd > 1;
    }

    /**
     * Checks and answer if there's a next element.
     */
    public boolean hasNext() {
        return indexOfNextAdd < size();
    }

    private List subListNoDuplicates(int startIndex, int stopIndex) {
        Object lastAdded = getLastAdded();
        ArrayList result = new ArrayList();
        for (int i = startIndex; i < stopIndex; i++) {
            Object o = get(i);
            if (o != lastAdded)
                result.add(o);
        }
        return result;
    }

    public String toString() {
        return "(Limit: "
            + limit
            + "; indexOfNextAdd: "
            + indexOfNextAdd
            + ")"
            + super.toString();
    }
}