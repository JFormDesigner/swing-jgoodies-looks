package com.jgoodies.swing;

/*
 * Copyright (c) 2002 Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of Karsten Lentzsch.  
 * Use is subject to license terms.
 *
 */
import java.awt.Dimension;

import javax.swing.Action;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JToolBar;

import com.jgoodies.swing.util.PopupButton;
import com.jgoodies.swing.util.ToolBarButton;

/**
 * Uses instances of <code>ToolBarButton</code> as action components. 
 * In addition the rollover property is switched on by default.
 *
 * @see	ToolBarButton
 * 
 * @author Karsten Lentzsch
 */

public final class ExtToolBar extends JToolBar {

    public static final String TOOLBAR_ONLY_STYLE = "Single";
    public static final String BOTH_STYLE         = "Both";

    /**
     * Constructs an <code>ExtToolBar</code> for the given <code>name</code>,
     * that has the rollover property set and is not floatable.
     */
    public ExtToolBar(String name) {
        this(name, null);
    }

    /**
     * Constructs an <code>ExtToolBar</code> for the given 
     * <code>name</code> and <code>headerStyleName</code>.
     * The tool bar has the rollover property set and is not floatable.
     */
    public ExtToolBar(String name, Object headerStyleOrName) {
        super(name);
        setRollover(true);
        setFloatable(false);
        setHeaderStyle(headerStyleOrName);
    }

    /**
     * Sets the JGoodies Looks header style by name.
     */
    public void setHeaderStyle(Object headerStyleOrName) {
        putClientProperty("jgoodies.headerStyle", headerStyleOrName);
    }

    // [Pending] 1.4 Use: setRollover(true);	
    public void setRollover(boolean b) {
        putClientProperty("JToolBar.isRollover", new Boolean(b));
    }

    /**
     * Adds a gap to the tool bar.
     */
    public void addGap() {
        addGap(6);
    }

    /**
     * Adds a gap to the tool bar.
     */
    public void addGap(int size) {
        add(Box.createRigidArea(new Dimension(size, size)));
    }

    /**
     * Adds a glue to the tool bar.
     */
    public void addGlue() {
        add(Box.createGlue());
    }
    
    public void add(PopupButton button) {
        button.addTo(this);
    }

    /**
     * Creates a ToolBarButton instance for the given <code>Action</code>,
     * which in turn is pre-configurated to be used in a tool bar.
     */
    protected JButton createActionComponent(Action a) {
        return new ToolBarButton(a);
        //button.setEnabled(a.isEnabled());
        //button.setToolTipText((String)a.getValue(Action.SHORT_DESCRIPTION));	
    }

}