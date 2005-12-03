/*
 * Copyright (c) 2001-2005 JGoodies Karsten Lentzsch. All Rights Reserved.
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

package com.jgoodies.looks.common;

import java.awt.Graphics;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.swing.JComponent;
import javax.swing.plaf.basic.BasicGraphicsUtils;

import com.jgoodies.looks.LookUtils;

/**
 * Provides convenience behavior used by the JGoodies Looks.<p>
 * 
 * <strong>Note:</strong> This class is not part of the public Looks API.
 * It should be treated as library internal and should not be used by
 * API users. It may be removed or changed with the Looks version
 * without further notice.
 * 
 * TODO: Remove the drawing methods if we require Java 5.
 *
 * @author  Karsten Lentzsch
 * @version $Revision: 1.2 $
 * 
 * @since 2.0
 */
public final class RenderingUtils {
    
    private static final String SWING_UTILITIES2_NAME =
        LookUtils.IS_JAVA_6_OR_LATER
             ? "sun.swing.SwingUtilities2"
             : "com.sun.java.swing.SwingUtilities2";
    
    /**
     * In Java 5 or later, this field holds the public static method 
     * <code>SwingUtilities2#drawString</code> that has been added
     * for Java 5. 
     */
    private static Method drawStringMethod = null;
    
    /**
     * In Java 5 or later, this field holds the public static method 
     * <code>SwingUtilities2#drawStringUnderlinedAt</code> that has been added
     * for Java 5. 
     */
    private static Method drawStringUnderlineCharAtMethod = null;
    
    /**
     * In Java 5, this field holds the private static method 
     * <code>SwingUtilities2#drawTextAntialiased(JComponent)</code>
     * has been replaced by a public method in Java 6. 
     */
    private static Method drawTextAntialiasedJava5Method;
    
    /**
     * In Java 6 or later, this field holds the public static method 
     * <code>SwingUtilities2#drawTextAntialiased(JComponent)</code> 
     * that has been added in Java 6. 
     */
    private static Method drawTextAntialiasedJava6Method;
    
    /**
     * In Java 6 or later, this field holds the field
     * <code>SwingUtilitites2$AATextInfo.aaHint</code>.
     */
    private static Field aaHintField;
    
    /**
     * Describes if the <code>aaHintField</code> can be accessed or not.
     * Will be disabled if the field cannot be looked up, or if the
     * access to the field failed.
     */
    private static boolean canAccessAAHintField = true;


    static {
        if (LookUtils.IS_JAVA_5_OR_LATER) {
            drawStringMethod = getMethodDrawString();
            drawStringUnderlineCharAtMethod = getMethodDrawStringUnderlineCharAt();
        }
        if (LookUtils.IS_JAVA_5) {
            drawTextAntialiasedJava5Method = getMethodDrawTextAntialiasedJava5();
        } else if (LookUtils.IS_JAVA_6_OR_LATER) {
            drawTextAntialiasedJava6Method = getMethodDrawTextAntialiasedJava6();
        }
        aaHintField = getFieldAAHint();
    }
    
    
    private RenderingUtils() {
        // Overrides default constructor; prevents instantiation.
    }
    
    /**
     * Draws the string at the specified location underlining the specified
     * character.
     *
     * @param c JComponent that will display the string, may be null
     * @param g Graphics to draw the text to
     * @param text String to display
     * @param underlinedChar the char to be underlined
     * @param x X coordinate to draw the text at
     * @param y Y coordinate to draw the text at
     */
    public static void drawString(JComponent c, Graphics g, String text, 
            int underlinedChar, int x, int y) {
        if (LookUtils.IS_JAVA_5_OR_LATER && (drawStringMethod != null)) {
            try {
                drawStringMethod.invoke(null, new Object[]{c, g, text, new Integer(x), new Integer(y)});
                return;
            } catch (IllegalArgumentException e) {
                // Use the BasicGraphicsUtils as fallback
            } catch (IllegalAccessException e) {
                // Use the BasicGraphicsUtils as fallback
            } catch (InvocationTargetException e) {
                // Use the BasicGraphicsUtils as fallback
            }
        }
        BasicGraphicsUtils.drawString(g, text, underlinedChar, x, y);
    }
    

    /**
     * Draws the string at the specified location underlining the specified
     * character.
     *
     * @param c JComponent that will display the string, may be null
     * @param g Graphics to draw the text to
     * @param text String to display
     * @param underlinedIndex Index of a character in the string to underline
     * @param x X coordinate to draw the text at
     * @param y Y coordinate to draw the text at
     */
    public static void drawStringUnderlineCharAt(JComponent c,Graphics g,
                           String text, int underlinedIndex, int x,int y) {
        if (LookUtils.IS_JAVA_5_OR_LATER && (drawStringUnderlineCharAtMethod != null)) {
            try {
                drawStringUnderlineCharAtMethod.invoke(null, new Object[]{c, g, text, new Integer(underlinedIndex), new Integer(x), new Integer(y)});
                return;
            } catch (IllegalArgumentException e) {
                // Use the BasicGraphicsUtils as fallback
            } catch (IllegalAccessException e) {
                // Use the BasicGraphicsUtils as fallback
            } catch (InvocationTargetException e) {
                // Use the BasicGraphicsUtils as fallback
            }
        }
        BasicGraphicsUtils.drawStringUnderlineCharAt(g, text, underlinedIndex, x, y);
    }
    
    
    public static Object getTextAntialiasingHint(JComponent c) {
        if (LookUtils.IS_JAVA_1_4) {
            return null;
        } else if (LookUtils.IS_JAVA_5 && (drawTextAntialiasedJava5Method != null)) {
            try {
                return drawTextAntialiasedJava5Method.invoke(null, new Object[]{c});
            } catch (IllegalArgumentException e) {
                // Use the BasicGraphicsUtils as fallback
            } catch (IllegalAccessException e) {
                // Use the BasicGraphicsUtils as fallback
            } catch (InvocationTargetException e) {
                // Use the BasicGraphicsUtils as fallback
            }
            return null;
        } else if (LookUtils.IS_JAVA_6_OR_LATER) {
            Object aaTextInfo = drawTextAntialiasedJava6(c);
            return aaTextInfo == null
                ? null
                : aaHint(aaTextInfo);
        } else {
            return null;
        }
    }
    
    
    private static Object drawTextAntialiasedJava6(JComponent c) {
        if (drawTextAntialiasedJava6Method != null) {
            try {
                return drawTextAntialiasedJava6Method.invoke(null, new Object[]{c});
            } catch (IllegalArgumentException e) {
                // Use the BasicGraphicsUtils as fallback
            } catch (IllegalAccessException e) {
                // Use the BasicGraphicsUtils as fallback
            } catch (InvocationTargetException e) {
                // Use the BasicGraphicsUtils as fallback
            }
        }
        return null;    
    }
    
    private static Object aaHint(Object aaTextInfo) {
        if (aaHintField != null && canAccessAAHintField) {
            try {
                return aaHintField.get(aaTextInfo);
            } catch (Exception e) {
                // Return null as fallback
                canAccessAAHintField = false;
            }
        }
        return null;
    }

    
    private static Field getFieldAAHint() {
        try {
            Class clazz = Class.forName("com.sun.java.swing.SwingUtilities2$AATextInfo");
            Field field = clazz.getDeclaredField("aaHint");
            field.setAccessible(true);
            return field;
        } catch (ClassNotFoundException e) {
            // returns null as fallback
        } catch (SecurityException e) {
            // returns null as fallback
        } catch (NoSuchFieldException e) {
            // returns null as fallback
        }
        canAccessAAHintField = false;
        return null;
    }

        
    // Private Helper Code ****************************************************
    
    
    private static Method getMethodDrawString() {
        try {
            Class clazz = Class.forName(SWING_UTILITIES2_NAME);
            return clazz.getMethod(
                    "drawString",
                    new Class[] {JComponent.class, Graphics.class, String.class, Integer.TYPE, Integer.TYPE}
                    );
        } catch (ClassNotFoundException e) {
            // returns null
        } catch (SecurityException e) {
            // returns null
        } catch (NoSuchMethodException e) {
            // returns null
        }
        return null;
    }
    
    private static Method getMethodDrawStringUnderlineCharAt() {
        try {
            Class clazz = Class.forName(SWING_UTILITIES2_NAME);
            return clazz.getMethod(
                    "drawStringUnderlineCharAt",
                    new Class[] {JComponent.class, Graphics.class, String.class, Integer.TYPE, Integer.TYPE, Integer.TYPE}
                    );
        } catch (ClassNotFoundException e) {
            // returns null
        } catch (SecurityException e) {
            // returns null
        } catch (NoSuchMethodException e) {
            // returns null
        }
        return null;
    }
    
    
    private static Method getMethodDrawTextAntialiasedJava5() {
        try {
            Class clazz = Class.forName(SWING_UTILITIES2_NAME);
            Method method = clazz.getMethod(
                    "drawTextAntialiased",
                    new Class[] {JComponent.class}
                    );
            method.setAccessible(true);
            return method;
        } catch (ClassNotFoundException e) {
            // returns null
        } catch (SecurityException e) {
            // returns null
        } catch (NoSuchMethodException e) {
            // returns null
        }
        return null;
    }

   
    private static Method getMethodDrawTextAntialiasedJava6() {
        try {
            Class clazz = Class.forName(SWING_UTILITIES2_NAME);
            return clazz.getMethod(
                    "drawTextAntialiased",
                    new Class[] {JComponent.class}
                    );
        } catch (ClassNotFoundException e) {
            // returns null
        } catch (SecurityException e) {
            // returns null
        } catch (NoSuchMethodException e) {
            // returns null
        }
        return null;
    }

   
}