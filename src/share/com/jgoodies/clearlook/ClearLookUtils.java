/*
 * Copyright (c) 2003 JGoodies Karsten Lentzsch. All Rights Reserved.
 *
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 * 
 *  o Redistributions of source code must retain the above copyright notice, 
 *    this list of conditions and the following disclaimer. 
 *     
 *  o Redistributions in binary form must reproduce the above copyright notice, 
 *    this list of conditions and the following disclaimer in the documentation 
 *    and/or other materials provided with the distribution. 
 *     
 *  o Neither the name of JGoodies Karsten Lentzsch nor the names of 
 *    its contributors may be used to endorse or promote products derived 
 *    from this software without specific prior written permission. 
 *     
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, 
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR 
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR 
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, 
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, 
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; 
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, 
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE 
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, 
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. 
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

    private ClearLookUtils() {
        // Override default constructor; prevents instantiation.
    }


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