package com.jgoodies.swing;

/*
 * Copyright (c) 2003 JGoodies Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of JGoodies Karsten Lentzsch.  
 * Use is subject to license terms.
 *
 */

import java.awt.Component;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;

/**
 * Extends <code>JList</code> to provide better cell tooltip support.
 * The #getToolTipText requests are delegated to the cell components.
 * <p>
 * Pending 1.4: This class is obsolete in 1.4 environments and will
 * be removed from a future release of the UI framework.
 * 
 * @author  Karsten Lentzsch
 * @see     javax.swing.ToolTipManager
 */

public final class ExtList extends JList {
    
    
    // Instance Creation ****************************************************

    /**
     * Constructs a <code>ExtList</code> that displays the elements in the
     * specified, non-<code>null</code> model. 
     * All <code>JList</code> constructors delegate to this one.
     *
     * @param dataModel   the data model for this list
     * @exception IllegalArgumentException   if <code>dataModel</code>
     *                      is <code>null</code>
     */
    public ExtList(ListModel dataModel) {
        super(dataModel);
    }

    /**
     * Constructs a <code>ExtList</code> that displays the elements in
     * the specified array.  This constructor just delegates to the
     * <code>ListModel</code> constructor.
     *
     * @param  listData  the array of Objects to be loaded into the data model
     */
    public ExtList(Object[] listData) {
        super(listData);
    }
    
    /**
     * Constructs a <code>ExtList</code> that displays the elements in
     * the specified <code>Vector</code>.  This constructor just
     * delegates to the <code>ListModel</code> constructor.
     *
     * @param  listData  the <code>Vector</code> to be loaded into the
     *      data model
     */
    public ExtList(Vector listData) {
        super(listData);
    }

    /**
     * Constructs a <code>ExtList</code> with an empty model.
     */
    public ExtList() {}
    
    
    // Additional Behavior **************************************************
    
    /**
     * Overrides <code>JComponent</code>'s #getToolTipText method in order 
     * to allow renderer's tips to be used if it has text set.
     * <p>
     * NOTE: For <code>ExtList</code> to properly display tooltips of its 
     * renderers ExtList must be a registered component with the 
     * <code>ToolTipManager</code>.
     * This is not done automatically but can be achieved by invoking
     * <pre>ToolTipManager.sharedInstance().registerComponent(list)</pre>.
     *
     * @param event the MouseEvent that initiated the ToolTip display
     */
    public String getToolTipText(MouseEvent event) {
        if (event != null) {
            Point p = event.getPoint();
            int index = locationToIndex(p);
            ListCellRenderer r = getCellRenderer();
            if (index != -1 && r != null) {
                Object selection = getModel().getElementAt(index);
                Component rComponent =
                    r.getListCellRendererComponent(
                        this,
                        selection,
                        index,
                        getSelectionModel().isSelectedIndex(index),
                        true);
                if (rComponent instanceof JComponent) {
                    MouseEvent newEvent;
                    Rectangle cellBounds = getCellBounds(index, index);
                    p.translate(-cellBounds.x, -cellBounds.y);
                    newEvent =
                        new MouseEvent(
                            rComponent,
                            event.getID(),
                            event.getWhen(),
                            event.getModifiers(),
                            p.x,
                            p.y,
                            event.getClickCount(),
                            event.isPopupTrigger());
                    return ((JComponent) rComponent).getToolTipText(newEvent);
                }
            }
        }
        return null;
    }
    
}