/*
 * Copyright (c) 2003 JGoodies Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of JGoodies Karsten Lentzsch.
 * Use is subject to license terms.
 *
 */

package com.jgoodies.clearlook;

import javax.swing.JComponent;
import javax.swing.border.Border;

/**
 * Consists only of static methods that are useful when working
 * with ClearLook.
 *  
 * @author Karsten Lentzsch
 */
public final class ClearLookUtils {

    private static final String CLEARLOOK_KEY = "ClearLook";
    
    private static final Object MARKER = "marker";

    // Override default constructor, prevents INSTANTIATION;
    private ClearLookUtils() {}


    /**
     * Checks and answers if ClearLook has stored a border
     * in the given component. 
     *  
     * @param component   the component that may hold the border
     * @return true if ClearLook has stored a border.
     */
    public static boolean hasCheckedBorder(JComponent component) {
        return component.getClientProperty(CLEARLOOK_KEY) != null;
    }
    
    /**
     * Looks up and answers the border that ClearLook has stored before.
     *  
     * @param component   the component that holds the border
     * @return the stored a border
     */
    public static Border getStoredBorder(JComponent component) {
        Object borderOrMarker = component.getClientProperty(CLEARLOOK_KEY);
        return borderOrMarker == MARKER
            ? null
            : (Border) borderOrMarker;
    }
    
    /**
     * Stores the given border in the client properties of the component.
     * If the border is <code>null</code> a marker object is stored,
     * so we can determine, wether we have attempted to store a border.
     * 
     * @param component   the component that holds the border
     * @param border      the border to be stored
     * @see #hasCheckedBorder
     */
    public static void storeBorder(JComponent component, Border border) {
        Object borderOrMarker;
        if(border == null)
            borderOrMarker = MARKER;
        else
            borderOrMarker = border;
        component.putClientProperty(CLEARLOOK_KEY, borderOrMarker);
    }

}