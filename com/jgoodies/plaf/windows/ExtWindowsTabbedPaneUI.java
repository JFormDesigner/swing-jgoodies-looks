/*
 * Copyright (c) 2003 Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of Karsten Lentzsch.  
 * Use is subject to license terms.
 *
 */

package com.jgoodies.plaf.windows;

import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicGraphicsUtils;
import javax.swing.plaf.basic.BasicTabbedPaneUI;

import com.jgoodies.clearlook.ClearLookManager;
import com.jgoodies.plaf.LookUtils;
import com.jgoodies.plaf.Options;
import com.sun.java.swing.plaf.windows.WindowsTabbedPaneUI;

/**
 * The JGoodies Windows Look and Feel implementation of
 * <code>TabbedPaneUI</code>.<p>
 * 
 * The flat appearance is work in progress; currently it works only
 * for a single line of tabs and paints distored tabs for multiple lines.
 *
 * @author Karsten Lentzsch
 */
public final class ExtWindowsTabbedPaneUI extends WindowsTabbedPaneUI {

    private static final Insets EMPTY_INSETS = new Insets(0, 0, 0, 0);
    private static final Insets NORTH_INSETS = new Insets(1, 0, 0, 0);
    private static final Insets WEST_INSETS  = new Insets(0, 1, 0, 0);
    private static final Insets SOUTH_INSETS = new Insets(0, 0, 1, 0);
    private static final Insets EAST_INSETS  = new Insets(0, 0, 0, 1);


    /**
     * Describes if tabs are painted with or without icons.
     */
    private static boolean isTabIconsEnabled = Options.isTabIconsEnabled();

    /**
     * Describes if we paint no content border or not; is false by default.
     * You can disable the content border by setting the client property
     * Options.NO_CONTENT_BORDER_KEY to Boolean.TRUE;
     * <p>
     * Overrides any ClearLook considerations.
     */
    private Boolean noContentBorder;

    /**
     * Describes if we paint tabs in an embedded style that is with
     * less decoration; this is false by default.
     * You can enable the embedded tabs style by setting the client property
     * Options.EMBEDDED_TABS_KEY to Boolean.TRUE.
     * <p>
     * Overrides any ClearLook considerations.
     */
    private Boolean embeddedTabs;

    /**
     * Describes that ClearLook suggests to hide the content border.
     */
    private boolean clearLookSuggestsNoContentBorder = false;

    /**
     * Creates and answers the <code>ExtWindowsTabbedPaneUI</code>.
     * 
     * @see javax.swing.plaf.ComponentUI#createUI(JComponent)
     */
    public static ComponentUI createUI(JComponent x) {
        return new ExtWindowsTabbedPaneUI();
    }


    /**
     * Installs the UI.
     *
     * @see javax.swing.plaf.ComponentUI#installUI(JComponent)
     */
    public void installUI(JComponent c) {
        super.installUI(c);
        embeddedTabs    = (Boolean) c.getClientProperty(Options.EMBEDDED_TABS_KEY);
        noContentBorder = (Boolean) c.getClientProperty(Options.NO_CONTENT_BORDER_KEY);
    }


    /**
     * Checks and answers if content border will be painted.
     * This is controlled by the component's client property
     * Options.NO_CONTENT_BORDER or Options.EMBEDDED.
     */
    private boolean hasNoContentBorder() {
        return hasEmbeddedTabs() || (noContentBorder == null
                                        ? clearLookSuggestsNoContentBorder()
                                        : noContentBorder.booleanValue());
    }

    /**
     * Checks and answers if tabs are painted with minimal decoration.
     */
    private boolean hasEmbeddedTabs() {
        return embeddedTabs == null
                ? false
                : embeddedTabs.booleanValue();
    }

    /**
     * Checks and answers if ClearLook suggests to hide the content border.
     */
    private boolean clearLookSuggestsNoContentBorder() {
        return clearLookSuggestsNoContentBorder;
    }

    /**
     * Checks if ClearLook indicates that the current component context 
     * uses a border that is considered as visual clutter. 
     * In this case replaces the border.
     * 
     * @param tabbedPane    the tabbed pane component
     */
    private void checkBorderReplacement(JTabbedPane tabbedPane) {
        Border newBorder = ClearLookManager.replaceBorder(tabbedPane);
        clearLookSuggestsNoContentBorder = newBorder != null;
    }

    /**
     * Creates and answer a handler that listens to property changes.
     * Unlike the superclass BasicTabbedPane, the PlasticTabbedPaneUI
     * uses an extended Handler.
     * @see javax.swing.plaf.basic.BasicTabbedPaneUI#createPropertyChangeListener()
     */
    protected PropertyChangeListener createPropertyChangeListener() {
        return new MyPropertyChangeHandler();
    }

    /**
     * Updates the embedded tabs property. This message is sent by
     * my PropertyChangeHandler whenever the embedded tabs property changes.
     */
    private void embeddedTabsPropertyChanged(Boolean newValue) {
        embeddedTabs = newValue;
    }

     /**
      * Updates the no content border property. This message is sent
      * by my PropertyChangeHandler whenever the noContentBorder
      * property changes.
      */
     private void noContentBorderPropertyChanged(Boolean newValue) {
         noContentBorder = newValue;
     }



    /**
     * Answers the icon for the tab with the specified index.
     * In case, we have globally switched of the use tab icons,
     * we answer <code>null</code> if and only if we have a title.
     */
    protected Icon getIconForTab(int tabIndex) {
        String title = tabPane.getTitleAt(tabIndex);
        boolean hasTitle = (title != null) && (title.length() > 0);
        return !isTabIconsEnabled  && hasTitle
                    ? null
                    : super.getIconForTab(tabIndex);
    }

    protected Insets getContentBorderInsets(int tabPlacement) {
        if (!hasNoContentBorder())
            return contentBorderInsets;
        else if (hasEmbeddedTabs())
            return EMPTY_INSETS;
        else {
            switch (tabPlacement) {
                case RIGHT :
                    return EAST_INSETS;
                case LEFT :
                    return WEST_INSETS;
                case TOP :
                    return NORTH_INSETS;
                case BOTTOM :
                default :
                    return SOUTH_INSETS;
            }
        }
    }
    
    protected int getTabLabelShiftX(int tabPlacement, int tabIndex, boolean isSelected) {
        switch (tabPlacement) {
            case RIGHT :
                return isSelected ? 2 : 0;
            case LEFT :
                return isSelected ? -2 : 0;
            case TOP :
            case BOTTOM :
            default :
                return 0;
        }
    }

    protected int getTabLabelShiftY(int tabPlacement, int tabIndex, boolean isSelected) {
        return 0;
    }

    protected Insets getSelectedTabPadInsets(int tabPlacement) {
        if (hasEmbeddedTabs()) {
            return EMPTY_INSETS;
        } else if (hasNoContentBorder()) {
            switch (tabPlacement) {
                case LEFT:   return new Insets(1,2,1,0);
                case RIGHT:  return new Insets(1,0,1,2);
                case TOP:    return new Insets(2,2,0,2);
                case BOTTOM: return new Insets(0,2,2,2);
                default: return EMPTY_INSETS;
            }
        } else {
            Insets superInsets = super.getSelectedTabPadInsets(tabPlacement);
            int equalized = superInsets.left + superInsets.right / 2;
            superInsets.left = superInsets.right = equalized;
            return superInsets;
        }
    }


    protected Insets getTabAreaInsets(int tabPlacement) {
        return hasEmbeddedTabs() 
                ? /*new Insets(1,1,1,1)*/EMPTY_INSETS 
                : super.getTabAreaInsets(tabPlacement);
    }
    
    // Pending: obsolete in 1.4
    private Rectangle calcRect = new Rectangle();

    // [Pending:] Obsolete in 1.4
    private Rectangle getMyTabBounds(int tabIndex, Rectangle dest) {
        if (LookUtils.IS_BEFORE_14) {
            dest.width  = rects[tabIndex].width;
            dest.height = rects[tabIndex].height;
            dest.x = rects[tabIndex].x;
            dest.y = rects[tabIndex].y;
            return dest;
        }
        try {
            Method method = BasicTabbedPaneUI.class.getMethod("getTabBounds", new Class[] {Integer.TYPE, Rectangle.class});
            return (Rectangle) method.invoke(this, new Object[]{new Integer(tabIndex), dest});
        } catch (NoSuchMethodException e) {
        } catch (InvocationTargetException e) {
        } catch (IllegalAccessException e) {
        }
        return dest;
    }

    /**
     * Paints the top edge of the pane's content border 
     * 
     * @see javax.swing.plaf.basic.BasicTabbedPaneUI#paintContentBorderTopEdge(Graphics, int, int, int, int, int, int)
     */
    protected void paintContentBorderTopEdge(Graphics g, int tabPlacement,
                                         int selectedIndex, 
                                         int x, int y, int w, int h) {
        if (hasNoContentBorder() && tabPlacement != TOP) {
            return;
        }
        Rectangle selRect = selectedIndex < 0
                            ? null 
                            : getMyTabBounds(selectedIndex, calcRect);
        if (tabPlacement != TOP || selectedIndex < 0 || 
           (selRect.y + selRect.height + 1 < y) ||
           (selRect.x < x || selRect.x > x + w)) {
            // no special case, do the super thing
            super.paintContentBorderTopEdge(g, tabPlacement, selectedIndex, x, y, w, h);
        } else {
            g.setColor(lightHighlight);
            g.fillRect(x, y, selRect.x + 1-x, 1);
            g.fillRect(selRect.x + selRect.width, y, 
                       x+w-2 -selRect.x-selRect.width, 1);
        }
    }
    
    /**
     * Paints the bottom edge of the pane's content border. 
     */
    protected void paintContentBorderBottomEdge(Graphics g, int tabPlacement,
                                         int selectedIndex, 
                                         int x, int y, int w, int h) {
        if (!hasNoContentBorder()) {
            Rectangle selRect = selectedIndex < 0? null :
                           getMyTabBounds(selectedIndex, calcRect);
            if (tabPlacement != BOTTOM || selectedIndex < 0 ||
             (selRect.y - 1 > h + y) ||
         (selRect.x < x || selRect.x > x + w)) {
           // no special case, do the super thing
            super.paintContentBorderBottomEdge(g, tabPlacement, selectedIndex, x, y, w, h);
         } else {
            g.setColor(lightHighlight);
            g.fillRect(x,y+h-1,1,1);
            g.setColor(shadow);
            g.fillRect(x+1, y+h-2, selRect.x - 1-x, 1);
            g.fillRect(selRect.x + selRect.width, y+h-2, x+w-2-selRect.x-selRect.width, 1);
            g.setColor(darkShadow);
            g.fillRect(x, y+h-1, selRect.x - x, 1);
            g.fillRect(selRect.x + selRect.width -1, y+h-1, x+w-selRect.x-selRect.width, 1);
         }
        } else if (!(tabPlacement == BOTTOM)) {
            // no content border really means only one content border:
            // the one edge that touches the tabs
        } else {
            g.setColor(shadow);
            g.fillRect(x,y+h,w,1);
        }
    }

    /**
     * paints the left Edge of the pane's content border 
     * 
     * @see javax.swing.plaf.basic.BasicTabbedPaneUI#paintContentBorderTopEdge(Graphics, int, int, int, int, int, int)
     */
    protected void paintContentBorderLeftEdge(Graphics g, int tabPlacement,
                                         int selectedIndex, 
                                         int x, int y, int w, int h) {
        if (!hasNoContentBorder()) {
            Rectangle selRect = selectedIndex < 0? null :
                           getMyTabBounds(selectedIndex, calcRect);
            if (tabPlacement != LEFT || selectedIndex < 0 ||
                (selRect.x + selRect.width + 1 < x) ||
                (selRect.y < y || selRect.y > y + h)) {
                 // no special case, do the super thing
                super.paintContentBorderLeftEdge(g, tabPlacement, selectedIndex, x, y, w, h);
            } else {
               g.setColor(lightHighlight); 
               g.fillRect(x, y, 1, selRect.y + 1 - y);
               g.fillRect(x, selRect.y + selRect.height, 
                           1, y+h-1-selRect.y-selRect.height);
                
            }
        } else if (!(tabPlacement == LEFT)) {
            // no content border really means only one content border:
            // the one edge that touches the tabs
        } else {
            g.setColor(shadow);
            g.fillRect(x,y,1,h);
        }
    }

    /**
     * paints the right Edge of the pane's content border 
     * 
     * @see javax.swing.plaf.basic.BasicTabbedPaneUI#paintContentBorderTopEdge(Graphics, int, int, int, int, int, int)
     */
    protected void paintContentBorderRightEdge(Graphics g, int tabPlacement,
                                         int selectedIndex, 
                                         int x, int y, int w, int h) {
        if (!hasNoContentBorder()) {
            Rectangle selRect = selectedIndex < 0? null :
                           getMyTabBounds(selectedIndex, calcRect);
            if (tabPlacement != RIGHT || selectedIndex < 0 ||
               (selRect.x - 1 > x+w) ||
               (selRect.y < y || selRect.y > y + h)) {
                // no special case, do the super thing
                super.paintContentBorderRightEdge(g, tabPlacement, selectedIndex, x, y, w, h);
             } else {
               g.setColor(lightHighlight);
               g.fillRect(x+w-1, y,1,1);
               g.setColor(shadow);
               g.fillRect(x+w-2, y+1, 1, selRect.y - 1-y);
               g.fillRect(x+w-2, selRect.y + selRect.height, 
                           1, y+h-1-selRect.y- selRect.height);
               g.setColor(darkShadow);
               g.fillRect(x+w-1, y, 1, selRect.y - y);
               g.fillRect(x+w-1, selRect.y + selRect.height-1, 
                           1, y+h-selRect.y-selRect.height);
          
             }
        } else if (!(tabPlacement == RIGHT)) {
            // no content border really means only one content border:
            // the one edge that touches the tabs
        } else {
            g.setColor(shadow);
            g.fillRect(x+w,y,1,h);
        }
    }


   /**
     * Paints the border for a single tab; it does not paint the tab's background.
     */
    protected void paintTabBorder(
        Graphics g,
        int tabPlacement,
        int tabIndex,
        int x,
        int y,
        int w,
        int h,
        boolean isSelected) {
        if (!hasEmbeddedTabs()) {
            super.paintTabBorder(g, tabPlacement, tabIndex, x, y, w, h, isSelected);
            return;
        }
        g.translate(x - 1, y - 1);
        int w1, w2, w3;
        int h1, h2, h3;
        switch (tabPlacement) {
            case TOP :
                w1 = 1;
                w2 = w - 2;
                w3 = 1;
                h1 = 1;
                h2 = h - 1;
                h3 = 0;
                break;
            case BOTTOM :
                w1 = 1;
                w2 = w - 2;
                w3 = 1;
                h1 = 0;
                h2 = h - 1;
                h3 = 1;
                break;
            case LEFT :
                w1 = 1;
                w2 = w - 1;
                w3 = 0;
                h1 = 1;
                h2 = h - 3;
                h3 = 1;
                break;
            case RIGHT :
            default :
                w1 = 0;
                w2 = w - 1;
                w3 = 1;
                h1 = 1;
                h2 = h - 3;
                h3 = 1;
        }
        if (isSelected) {
            g.setColor(lightHighlight);
            g.drawRect(w1, h1, w1 + w2 + w3, h1 + h2 + h3);
            g.setColor(shadow);
            g.fillRect(1 + w1, 0, w2, h1);
            g.fillRect(0, 1 + h1, w1, h2);
            g.fillRect(2 * w1 + w2 + 2 * w3, 1 + h1, w3, h2);
            g.fillRect(1 + w1, 2 * h1 + h2 + 2 * h3, w2, h3);
            g.fillRect(1, 1, w1, h1);
            g.fillRect(2 * w1 + w2 + w3, 1, w3, h1);
            g.fillRect(1, 2 * h1 + h2 + h3, w1, h3);
            g.fillRect(2 * w1 + w2 + w3, 2 * h1 + h2 + h3, w3, h3);
        } else {
            g.setColor(shadow);
            g.fillRect(w1 + w2 + 2 * w3, h3 * h2 /2, w3, h2* 2 /3);
            g.fillRect(w3*w2 /2, h1 + h2 + 2 * h3, w2/2 +2, h3);
        }
        g.translate(-x + 1, -y + 1);
    }

    protected void paintFocusIndicator(
        Graphics g,
        int tabPlacement,
        Rectangle[] rectangles,
        int tabIndex,
        Rectangle iconRect,
        Rectangle textRect,
        boolean isSelected) {
        if (!hasEmbeddedTabs()) {
            super.paintFocusIndicator(g, tabPlacement, rectangles, tabIndex, iconRect, textRect, isSelected);
            return;
        }
        if (tabPane.hasFocus() && isSelected) {
            g.setColor(focus);
            BasicGraphicsUtils.drawDashedRect(g, textRect.x - 2, textRect.y, textRect.width + 3, textRect.height);
        }
    }

    protected boolean shouldRotateTabRuns(int tabPlacement) {
        return !hasEmbeddedTabs();
    }

    /**
     * Copied here from super(super)class to avoid labels being centered on 
     * vertical tab runs if they consist of icon and text
     */
    protected void layoutLabel(
        int tabPlacement,
        FontMetrics metrics,
        int tabIndex,
        String title,
        Icon icon,
        Rectangle tabRect,
        Rectangle iconRect,
        Rectangle textRect,
        boolean isSelected) {
        textRect.x = textRect.y = iconRect.x = iconRect.y = 0;
        int xNudge = getTabLabelShiftX(tabPlacement, tabIndex, isSelected);
        int yNudge = getTabLabelShiftY(tabPlacement, tabIndex, isSelected);
        if ((tabPlacement == RIGHT || tabPlacement == LEFT) && icon != null && title != null && !title.equals("")) {
            /* vertical tab runs look ugly if icons and text are centered */
            SwingUtilities.layoutCompoundLabel(
                (JComponent) tabPane,
                metrics,
                title,
                icon,
                SwingUtilities.CENTER,
                SwingUtilities.LEFT,
                SwingUtilities.CENTER,
                SwingUtilities.TRAILING,
                tabRect,
                iconRect,
                textRect,
                textIconGap);
            xNudge += 4;
        } else { /* original superclass behavior */
            SwingUtilities.layoutCompoundLabel(
                (JComponent) tabPane,
                metrics,
                title,
                icon,
                SwingUtilities.CENTER,
                SwingUtilities.CENTER,
                SwingUtilities.CENTER,
                SwingUtilities.TRAILING,
                tabRect,
                iconRect,
                textRect,
                textIconGap);
        }

        iconRect.x += xNudge;
        iconRect.y += yNudge;
        textRect.x += xNudge;
        textRect.y += yNudge;
    }
    
    
    /**
     * Catches and handles property change events. In addition to the super
     * class behavior we listen to changes of the ancestor, tab placement,
     * and JGoodies options for content border, and embedded tabs.
     */
    private class MyPropertyChangeHandler extends BasicTabbedPaneUI.PropertyChangeHandler {
        public void propertyChange(PropertyChangeEvent e) {
            super.propertyChange(e);

            String pName = e.getPropertyName();
            if (null == pName) {
                return;
            }
            if (pName.equals("ancestor")) {
               checkBorderReplacement(tabPane);
            }
            if (pName.equals(Options.EMBEDDED_TABS_KEY)) {
               embeddedTabsPropertyChanged((Boolean)e.getNewValue());
               return;
            }
            if (pName.equals(Options.NO_CONTENT_BORDER_KEY)) {
               noContentBorderPropertyChanged((Boolean)e.getNewValue());
               return;
            }

        }

    }

    

}