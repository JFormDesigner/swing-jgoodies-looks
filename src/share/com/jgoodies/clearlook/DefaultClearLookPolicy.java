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

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.swing.*;
import javax.swing.border.*;

/**
 * The default <code>ClearLookPolicy</code>; detects visual clutter and 
 * installs better alternatives.<p>
 * 
 * The following situations are detected:
 * <ol>
 * <li><code>JComponent</code>  with a <code>BevelBorder</code>
 * <li><code>JScrollPane</code> nested in a <code>JTabbedPane</code>
 * <li><code>JScrollPane</code> nested in a <code>JSplitPane</code>
 * <li><code>JSplitPane</code>  nested in a <code>JSplitPane</code>
 * <li><code>JTabbedPane</code> nested in a <code>JSplitPane</code>
 * </ol>
 * 
 * @author Karsten Lentzsch
 * @see	ClearLookManager
 * @see	ClearLookPolicy
 */

public class DefaultClearLookPolicy implements ClearLookPolicy {

    // Debug Replacement Borders **********************************************

    protected static final Border EMPTY_BORDER = 
        new EmptyBorder(0, 0, 0, 0);
        
    protected static final Border MARKER_BORDER = 
        EMPTY_BORDER;

    protected static final Border RED1_BORDER = 
        new LineBorder(Color.red);
        
    protected static final Border RED2_BORDER =
        new LineBorder(Color.red.brighter());
        
    protected static final Border PINK1_BORDER = 
        new LineBorder(Color.pink);
        
    protected static final Border PINK2_BORDER =
        new LineBorder(Color.pink.brighter());


    public String getName() {
        return "Default ClearLook(tm) policy";
    }
    

    // Common Behavior for Analysing and Replacing Borders ********************

    /**
     * Detects and conditionally replaces the <code>Border</code> 
     * of the specified <code>JComponent</code>. Answers the original 
     * <code>Border</code>, or <code>null</code> if we did not replace it.
     */
    public Border replaceBorder(JComponent component) {
        // Do nothing if we detect an empty border.
        if (hasEmptyBorder(component))
            return null;

        Border originalBorder = component.getBorder();
        Container parent = component.getParent();
        log("");
        log("Component =" + component.getClass().getName());
        log("Parent    =" + parent.getClass().getName());
        if (originalBorder != null) {
            log("Border    =" + originalBorder.getClass().getName());
            if (originalBorder instanceof CompoundBorder) {
                CompoundBorder compoundBorder = (CompoundBorder) originalBorder;
                log("   outside=" + compoundBorder.getOutsideBorder());
                log("   inside =" + compoundBorder.getInsideBorder());
            }
        }

        Border newBorder = null;
        Method method = findAnalyseMethod(component);
        if (method != null) {
            try {
                newBorder =
                    (Border) method.invoke(this, new Object[] { component });
            } catch (InvocationTargetException e) {
                log(e.getLocalizedMessage());
            } catch (IllegalAccessException e) {
                log(e.getLocalizedMessage());
            }
        } else {
            log(
                "Could not find an analyse method for "
                    + component.getClass().getName());
            newBorder = analyse(component);
        }

        return assignBorder(component, newBorder);
    }

    /**
     * Assigns the new border to the component - unless the component
     * is a tabbed pane. In the latter case, the new border is returned,
     * which in turn will be used by tabbed panes to recognize that
     * the content border shall be hidden.
     */
    protected Border assignBorder(JComponent c, Border newBorder) {
        if (newBorder == null)
            return null;

        log("New border=" + newBorder.getClass().getName());
        if (c instanceof JTabbedPane) {
            return newBorder;
        } else {
            c.setBorder(newBorder);
            return c.getBorder();
        }
    }
    
    
    // Component Tree Analysis ************************************************

    /**
     * Detects decorators that are considered to be visual clutter.
     */
    public Border analyse(JComponent component) {
        log("DefaultClearLookPolicy.analyse(JComponent)");
        if (isDoubleDecorated(component)) {
            log("Double decoration detected.");
            return getDoubleDecorationBorder();
        } else if (hasBevelBorder(component)) {
            log("BevelBorder detected.");
            return getThinBevelBorder((BevelBorder) component.getBorder());
        } else
            return null;
    }

    /**
     * Detects if the specified <code>JScrollPane</code> is nested 
     * in a <code>JTabbedPane</code> or in a <code>JSplitPane</code>
     * with a non-empty <code>Border</code>.
     * In both cases, returns a replacement border.
     */
    public Border analyse(JScrollPane scrollPane) {
        log("DefaultClearLookPolicy.analyse(JScrollPane)");
        if (isDecoratingParent(scrollPane.getParent())) {
            log("ScrollPane nested in decorating parent detected.");
            return getScrollPaneReplacementBorder();
        } else
            return null;
    }

    /**
     * Detects if the specified <code>JSplitPane</code> is nested in another
     * <code>JSplitPane</code> that has a non-empty border.
     */
    public Border analyse(JSplitPane splitPane) {
        log("DefaultClearLookPolicy.analyse(JSplitPane)");
        if (isDecoratingParent(splitPane.getParent())) {
            log("SplitPane nested in decorating parent detected.");
            return getSplitPaneReplacementBorder();
        } else
            return null;
    }

    /**
     * Detects if the specified <code>JTabbedPane</code> is a special
     * NetBeans component that is considered to have visual clutter.
     */
    public Border analyse(JTabbedPane tab) {
        log("DefaultClearLookPolicy.analyse(JTabbedPane)");
        if (isDecoratingParent(tab.getParent())) {
            log("TabbedPane in decorating parent detected.");
            return MARKER_BORDER;
        } else
            return null;
    }
    

    // Border Classification **************************************************

    /**
     * Checks and answers if the specified <code>Border</code> is kind-of empty.
     */
    protected boolean isEmptyBorder(Border b) {
        return b == null || b instanceof EmptyBorder;
    }

    /**
     * Checks and answers whether the specified <code>Border</code> 
     * provides a visual decoration, for example a wrapping line.
     * <p>
     * Since the detected border classes are not final, we don't use 
     * the #instanceof operator to avoid replacing subclasses,
     * that could be visually improved.
     * 
     * @param border    the border to check
     * @return true if the border is considered a decoration, 
     *     otherwise false
     */
    protected boolean isDecoration(Border border) {
        Class clazz = border.getClass();
        return clazz.equals(BevelBorder.class)
            || clazz.equals(EtchedBorder.class)
            || clazz.equals(LineBorder.class);
    }

    /**
     * Checks and answers whether the specified <code>Border</code> 
     * wraps childs with a decoration.
     * 
     * @param border    the border to check
     * @return true if the border decorates childs, otherwise false
     */
    protected boolean isChildDecoration(Border border) {
        if (isDecoration(border))
            return true;
        else if (border instanceof CompoundBorder) {
            CompoundBorder compound = (CompoundBorder) border;
            return isDecoration(compound.getOutsideBorder());
        } else
            return false;
    }

    /**
     * Checks and answers whether the specified <code>Border</code> 
     * wraps childs with a decoration.
     * 
     * @param border    the border to check
     * @return true if the border decorates childs, otherwise false
     */
    protected boolean isParentDecoration(Border border) {
        if (isDecoration(border))
            return true;
        else if (border instanceof CompoundBorder) {
            CompoundBorder compound = (CompoundBorder) border;
            return isDecoration(compound.getInsideBorder());
        } else
            return false;
    }

    // Component Checking *****************************************************

    /**
     * Checks and answers if the given component has an empty border.
     */
    protected boolean hasEmptyBorder(JComponent component) {
        return isEmptyBorder(component.getBorder())
            && !(component instanceof JTabbedPane);
    }

    /**
     * Checks and answers if the component has a <code>BevelBorder</code>.
     */
    private boolean hasBevelBorder(JComponent component) {
        Border border = component.getBorder();
        return border instanceof BevelBorder;
    }

    /**
     * Returns if the specified <code>JComponent</code> is decorated.
     * This default implementation checks, if the component's border
     * is decorated.
     * <p>
     * Subclasses may check for special decorators.
     */
    protected boolean isDecoratedChild(Component c) {
        if (c instanceof JScrollPane)
            return true;
        else if (!(c instanceof JComponent))
            return false;
        else {
            JComponent comp = (JComponent) c;
            return isChildDecoration(comp.getBorder());
        }
    }

    /**
     * Returns if the specified <code>JComponent</code> is decorated.
     * This default implementation checks, if the component's border
     * is decorated.
     * <p>
     * Subclasses may check for special decorators.
     */
    protected boolean isDecoratingParent(Component c) {
        if (c instanceof JScrollPane)
            return true;
        else if (c instanceof JTabbedPane)
            return true;
        else if (isDecoratedSplitPane(c))
            return true;
        else if (isInternalFrameContent(c))
            return true;
        else if (!(c instanceof JComponent))
            return false;
        else {
            JComponent comp = (JComponent) c;
            return comp.getComponentCount() == 1
                && isParentDecoration(comp.getBorder());
        }
    }

    /**
     * Checks and answer whether the given component is the content pane
     * of a <code>JInternalFrame</code>.
     * 
     * @param c  the component to check
     * @return true if the component is frame content, false otherwise
     */
    protected boolean isInternalFrameContent(Component c) {
        Container parent = c.getParent();
        return parent instanceof JRootPane;
    }

    /**
     * Answers whether the specified is kind-of <code>SplitPane</code>.
     * Subclasses may override to widen the set of panels, that 
     * is considered as candidates to remove child borders.
     */
    protected boolean isKindOfSplitPane(Component component) {
        return component instanceof JSplitPane;
    }

    /**
     * Returns whether the specified <code>Component</code> is a
     * <code>JSplitPane</code> that has a non-empty border.
     */
    protected boolean isDecoratedSplitPane(Component c) {
        return isKindOfSplitPane(c)
            && !(c instanceof JSplitPane
                && isEmptyBorder(((JSplitPane) c).getBorder()));
    }

    /**
     * Detects and returns if the specified component has
     * double decoration, that is the component has a decorated border
     * and is nested in a container that has a decorating border.
     */
    protected boolean isDoubleDecorated(JComponent c) {
        return isDecoratedChild(c) && isDecoratingParent(c.getParent());
    }

    // Providing Replacement Borders ******************************************

    /**
     * Answers the <code>Border</code> that will be used to replace
     * a double decorated component.
     */
    protected Border getDoubleDecorationBorder() {
        return isDebug() ? RED1_BORDER : EMPTY_BORDER;
    }

    /**
     * Answers the <code>Border</code> that will be used to replace
     * the <code>Border</code> of a <code>JScrollPane</code> that is
     * contained in a <code>JSplitPane</code>.
     */
    protected Border getScrollPaneReplacementBorder() {
        return isDebug()
            ? RED1_BORDER
            : UIManager.getBorder("ClearLook.ScrollPaneReplacementBorder");
    }

    /**
     * Answers the replacement <code>Border</code> for SplitPane in SplitPane.
     */
    protected Border getSplitPaneReplacementBorder() {
        return isDebug()
            ? RED2_BORDER
            : UIManager.getBorder("ClearLook.SplitPaneReplacementBorder");
    }

    /**
     * Answers the <code>Border</code> that will be used to replace
     * a BevelBorder.
     */
    protected Border getThinBevelBorder(BevelBorder bevelBorder) {
        return bevelBorder.getBevelType() == BevelBorder.RAISED
            ? getThinRaisedBevelBorder()
            : getThinLoweredBevelBorder();
    }

    /**
     * Answers the replacement <code>Border</code> for lowered <code>BevelBorder</code>s.
     */
    protected Border getThinLoweredBevelBorder() {
        return isDebug()
            ? PINK1_BORDER
            : UIManager.getBorder("ClearLook.ThinLoweredBorder");
    }

    /**
     * Answers the replacement <code>Border</code> for raised <code>BevelBorder</code>s.
     */
    protected Border getThinRaisedBevelBorder() {
        return isDebug()
            ? PINK2_BORDER
            : UIManager.getBorder("ClearLook.ThinRaisedBorder");
    }

    // Helper Code ************************************************************

    /**
     * Logs a message if we are in verbose mode.
     */
    protected final void log(String message) {
        ClearLookManager.log(message);
    }

    /**
     * Checks and answers if we are in debug mode.
     */
    protected final boolean isDebug() {
        return ClearLookManager.getMode().isDebug();
    }

    /**
     * Looks up and returns the appropriate analyse method.
     */
    private Method findAnalyseMethod(JComponent c) {
        for (Class clazz = c.getClass();
            clazz != null;
            clazz = clazz.getSuperclass()) {
            try {
                return getClass().getMethod("analyse", new Class[] { clazz });
            } catch (NoSuchMethodException e) {
                log("parameter=" + clazz + "; Receiver=" + getClass());
            }
        }
        return null;
    }

}