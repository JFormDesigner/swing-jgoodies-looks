/*
 * Copyright (c) 2003 JGoodies Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of JGoodies Karsten Lentzsch.  
 * Use is subject to license terms. 
 *
 */

package com.jgoodies.clearlook;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

/**
 * An implementation of {@link ClearLookPolicy} for use in NetBeans based
 * environments. In addition to its superclass, it detects special contexts 
 * that appear in NetBeand, and it replaces some NetBeans borders:
 * <ol>
 * <li><code>JLabel</code> used as status bar cell
 * <li><code>JPanel</code> with obsolete border
 * <li><code>JTabbedPane</code> with obsolete border
 * </ol>
 * 
 * @author Karsten Lentzsch
 * @see	ClearLookManager
 * @see	DefaultClearLookPolicy
 */
public final class NetBeansClearLookPolicy extends DefaultClearLookPolicy {

    protected static final Border ORANGE1_BORDER  = new LineBorder(Color.orange);
    protected static final Border ORANGE2_BORDER  = new LineBorder(Color.orange.brighter());

    // NetBeans Class Names Used tor Detect Special Situations ****************

    private static final String NB                  = "org.netbeans.";
    private static final String NB_CORE             = NB + "core.";
    private static final String NB_WINDOWS          = NB_CORE + "windows.";
    private static final String NB_FRAMES           = NB_WINDOWS + "frames.";
    private static final String NB_OPENIDE          = "org.openide.";
    private static final String NB_PROPERTYSHEET_PKG= NB_OPENIDE + "explorer.propertysheet";
    //private static final String NB_MAIN_EXPLORER    = NB_CORE + "NbMainExplorer";
    private static final String NB_SPLITTEDPANEL    = NB_OPENIDE + "awt.SplittedPanel";
    private static final String NB_TOP_COMPONENT    = NB_OPENIDE + "windows.TopComponent";
    private static final String NB_PROPERTYSHEET_TAB= NB_PROPERTYSHEET_PKG + "PropertySheetTab";
    private static final String NB_PROPERTYSHEET    = NB_PROPERTYSHEET_PKG + "PropertySheet";
    private static final String NB_FORM             = NB + "modules.form.";
    private static final String NB_FORM_DESIGNER    = NB_FORM + "FormDesigner";
    public  static final String NB_PERIMETER_PANE   = NB_FRAMES + "PerimeterPane";
    public  static final String NB_MULTITABBED_CONT = NB_FRAMES + "MultiTabbedContainerImpl";
    private static final String NB_STATUS_CELL      = NB + "editor.StatusBar$Cell";


    // Instance Creation ******************************************************

    /**
     * Constructs a <code>NetBeansClearLookPolicy</code>.
     */
    public NetBeansClearLookPolicy() {
    }
    
    
    public String getName() {
        return "ClearLook(tm) policy for NetBeans";
    }

    // Behavior for Replacing Borders *****************************************

    public Border replaceBorder(JComponent component) {
        Border border = component.getBorder();
        if (border != null && isParentDecoration(border)) {
            log("Decorated component = " + component.getClass().getName());
            log("Decorated border    = " + border.getClass().getName());
            //showParents(component);
        } else if (isInstanceOf(component, NB_TOP_COMPONENT)) {
            log("TopComponent        = " + component.getClass().getName());
            //showParents(component);
        } else if (isInstanceOf(component, NB_PROPERTYSHEET)) {
            log("PropertySheet		 = " + component.getClass().getName());
            //showParents(component);
        }
        return super.replaceBorder(component);
    }

    /**
     * Detects borders situations that are considered to be visual clutter.
     */
    public Border analyse(JComponent component) {
        log("NetBeansClearLookPolicy.analyse(JComponent)");
        if (isPerimeterPaneChild(component)) {
            log("Perimeter child detected.");
//            Container parent = component.getParent();
//            Component[] children = parent.getComponents();
//            for (int i = 0; i < children.length; i++) {
//                log("Perimeter child=" + children[i].getClass().getName());
//            }
            return getPerimeterChildBorder(); 
        }
        if (isTopComponentChild(component)) {
            log("TopComponent child detected.");
            return getTopComponentChildBorder();
        } else if (isInstanceOf(component.getParent(), NB_PROPERTYSHEET)) {
            log("PropertySheet child detected.");
            return getPropertySheetChildBorder();
        } else
            return super.analyse(component);
    }

    /**
     * Detects NetBeans status cells. Returns a status cell replacement border.
     */
    public Border analyse(JLabel label) {
        log("NetBeansClearLookPolicy.analyse(JLabel)");
        if (isNetBeansStatusCell(label)) {
            log("NetBeans status cell detected.");
            return getNetBeansStatusCellBorder();
        } else
            return super.analyse(label);
    }

    /**
     * Detects special NetBeans panels. and conditionally replaces the <code>Border</code> 
     * of the specified <code>JPanel</code>. Answers the original 
     * <code>Border</code>, or <code>null</code> if we did not replace it.
     */
    public Border analyse(JPanel panel) {
        log("NetBeansClearLookPolicy.analyse(JPanel)");
        if (isNetBeansSpecialPanel(panel)) {
            log("NetBeans special panel detected.");
            return getNetBeansSpecialPanelBorder();
        } else
            return super.analyse(panel);
    }

    /**
     * Detects and conditionally replaces the <code>Border</code> 
     * of the specified <code>JScrollPane</code>. Answers the original 
     * <code>Border</code>, or <code>null</code> if we did not replace it.
     */
    public Border analyse(JScrollPane scrollPane) {
        log("NetBeansClearLookPolicy.analyse(JScrollPane)");
        Container parent = scrollPane.getParent();
        if (isNetBeansWrapper(parent)) {
            log("NetBeans ScrollPane wrapper detected.");
            return getNetBeansScrollPaneBorder();
        }

        return super.analyse(scrollPane);
    }
    

    // Overriding Super Behavior **********************************************

    /**
     * Returns if the specified compoent is decorated.
     */
    protected boolean isDecoratedChild(Component c) {
        boolean b = super.isDecoratedChild(c);
        return b || isInstanceOf(c, NB_PROPERTYSHEET_TAB);
    }

    protected boolean isDecoratingParent(Component c) {
        return super.isDecoratingParent(c)
                || isInstanceOf(c, NB_PERIMETER_PANE)
                || isInstanceOf(c, NB_MULTITABBED_CONT);
    }
    
    /**
     * Answers if the specified is kind-of <code>SplitPane</code>.
     * Subclasses may override to widen the set of panels, that 
     * is considered as candidates to remove child borders.
     */
    protected boolean isKindOfSplitPane(Component component) {
        boolean b = super.isKindOfSplitPane(component);
        if (b)
            return b;
        return isInstanceOf(component, NB_SPLITTEDPANEL);
//           || isInstanceOf(component, NB_PERIMETER_PANE);
    }

    // NetBeans Detection Methods *********************************************

    /**
     * Checks and answers if the specified <code>JComponent</code>
     * is considered to be a NetBeans panel, that forces us to
     * replace its <code>Border</code>.
     */
    private boolean isPerimeterPaneChild(JComponent c) {
        return isInstanceOf(c.getParent(), NB_PERIMETER_PANE);
    }

    private boolean isTopComponentChild(JComponent c) {
        return isInstanceOf(c.getParent(), NB_TOP_COMPONENT);
    }

    /**
     * Checks and answers if the specified <code>JPanel</code>
     * is considered to be a NetBeans panel, that forces us to
     * replace its <code>Border</code>.
     */
    private boolean isNetBeansSpecialPanel(JPanel panel) {
        return isInstanceOf(panel, NB_SPLITTEDPANEL);
    }

    /**
     * Checks and answers if the specified <code>Component</code>
     * is a NetBeans status bar widget, that has been decorated with 
     * an ugly <code>BeveledBorder</code>.
     */
    private boolean isNetBeansStatusCell(Component c) {
        return isInstanceOf(c, NB_STATUS_CELL);
        // ||  className.equals(NB_MINI_STATUS_BAR)
        // ||  className.equals(NB_STATUS_LINE);
    }

    /**
     * Checks and answers if the specified <code>Component</code>
     * is considered to be a NetBeans wrapper class, that forces
     * a <code>Border</code> replacement.
     */
    private boolean isNetBeansWrapper(Component c) {
        String className = c.getClass().getName();
        return className.equals(NB_FORM_DESIGNER)
            || className.equals(NB_PROPERTYSHEET_TAB)
            || isInstanceOf(c, NB_PROPERTYSHEET);
    }
    
    
    // Misc *******************************************************************
    
    /**
     * Checks and answers if the given object is 
     * an instance of the specified class name.
     * 
     * @param object     the instance to check
     * @param className  the name of the class
     * @return true if object is an instance of the specified class
     */
    protected final boolean isInstanceOf(Object object, String className) {
        for (Class clazz = object.getClass();
            clazz != null;
            clazz = clazz.getSuperclass()) {
            if (clazz.getName().equals(className))
                return true;
        }
        return false;
    }

    

    // Accessing Replacement Borders ******************************************

    /**
     * Answers the replacement <code>Border</code> for NetBeans scroll panes.
     */
    private Border getNetBeansScrollPaneBorder() {
        return isDebug()
            ? ORANGE1_BORDER
            : UIManager.getBorder("ClearLook.NetBeansScrollPaneBorder");
    }

    /**
     * Answers the replacement <code>Border</code> for special NetBeans panels.
     */
    private Border getNetBeansSpecialPanelBorder() {
        return isDebug()
            ? ORANGE2_BORDER
            : UIManager.getBorder("ClearLook.NetBeansSpecialPanelBorder");
    }

    /**
     * Answers the replacement <code>Border</code> for NetBeans status cells.
     */
    private Border getNetBeansStatusCellBorder() {
        return isDebug()
            ? PINK2_BORDER
            : UIManager.getBorder("ClearLook.NetBeansStatusCellBorder");
    }


    private Border getPerimeterChildBorder() {
        return isDebug()
            ? ORANGE1_BORDER
            : getThinLoweredBevelBorder();
    }

    private Border getTopComponentChildBorder() {
        return isDebug()
            ? ORANGE2_BORDER
            : EMPTY_BORDER;
    }
    
    private Border getPropertySheetChildBorder() {
        return isDebug()
            ? PINK1_BORDER
            : EMPTY_BORDER;
    }
    
    
    // Debug Helper Code ******************************************************

//    private void showParents(JComponent c) {
//        int i = 1;
//        for (Container parent = c.getParent();
//            parent != null;
//            parent = parent.getParent()) {
//            log("parent (" + i + ")=" + parent.getClass().getName());
//            log("super  (" + i + ")=" + parent.getClass().getSuperclass().getName());
//            if (parent instanceof JComponent) {
//                JComponent parentC = (JComponent) parent;
//                Border parentBorder = parentC.getBorder();
//                log("border (" + i++ + ")="
//                        + (parentBorder == null
//                            ? "null"
//                            : parentBorder.getClass().getName()));
//            }
//        }
//    }


}