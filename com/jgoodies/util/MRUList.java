package com.jgoodies.util;

/*
 * Copyright (c) 2002 JGoodies Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of JGoodies Karsten Lentzsch.
 * Use is subject to license terms.
 *
 */

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This class models a List with a limited capacity.
 * If the limit is exceeded, the most recently used (MRU)
 * elements is beeing removed from the list.
 *
 * @author Karsten Lentzsch
 */

public final class MRUList {

    private final int limit;
    private final List list;

    /**
     * Constructs a list of most recently used elements with the given maximum capacity.
     */
    public MRUList(int limit) {
        this.limit = limit;
        this.list = new ArrayList(limit);
    }

    /**
     * Adds an element to the list.
     */
    public boolean add(Object o) {
        boolean contained = list.contains(o);
        if (contained)
            list.remove(o);
        list.add(0, o);
        if (list.size() > limit)
            list.remove(list.size() - 1);
        return contained;
    }

    /**
     * Adds all elements from the given list to this MRU list.
     */
    public void addAll(List aList) {
        for (Iterator i = aList.iterator(); i.hasNext();) {
            add(i.next());
        }
    }

    public boolean remove(Object o) {
        return list.remove(o);
    }
    
    public Object get(int index) {
        return list.get(index);
    }
    
    public Iterator iterator() {
        return list.iterator();
    }
    
    public List asList() {
        return new ArrayList(list);
    }

}