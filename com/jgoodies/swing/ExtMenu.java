package com.jgoodies.swing;

/*
 * Copyright (c) 2002 Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of Karsten Lentzsch.  
 * Use is subject to license terms.
 *
 */
import java.awt.Component;
import java.awt.Container;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;

import com.jgoodies.swing.application.ActionManager;
import com.jgoodies.util.Utilities;

/**
 * Creates special action components for <code>ToggleAction</code>s,
 * honors the displayed mnemonic index, and works around a bug 
 * in the J2RE 1.3.0.
 *
 * @see	Action
 * @see	ActionManager
 * @see	ToggleAction
 *
 * @author Karsten Lentzsch
 */

public final class ExtMenu extends JMenu {
	
	/**
	 * Constructs a menu for for the given label and mnemonic.
	 */
	public ExtMenu(String label, char mnemonic) {
		super(label);
		setMnemonic(mnemonic);
	}
	
	
    /**
     * Factory method which creates the <code>JMenuItem</code> for 
     * <code>Action</code>s added to the <code>JMenu</code>.
     * As of 1.3, this is no longer the preferred method. 
     * Instead it is recommended to configure
     * a control with an action using <code>setAction</code>,
     * and then adding that
     * control directly to the <code>Container</code>.
     *
     * @param action the <code>Action</code> for the menu item to be added
     * @return the new menu item
     * @see Action
     */
    protected JMenuItem createActionComponent(Action action) {
    	JMenuItem menuItem;
        if (!(action instanceof ToggleAction)) {
        	menuItem = new JMenuItem(action);
        } else {
	        ToggleAction toggleAction = (ToggleAction) action;
	        if (toggleAction.isRadio())
				menuItem = new JRadioButtonMenuItem(toggleAction);
			else
				menuItem = new JCheckBoxMenuItem(toggleAction);
			int mnemonic = menuItem.getMnemonic();	// TODO: Check if obsolete
			menuItem.setModel(toggleAction.model());
			menuItem.setMnemonic(mnemonic);			// TODO: Check if obsolete
			menuItem.setEnabled(action.isEnabled());
        }
        	
        setDisplayedMnemonicIndex(menuItem, action);
		return menuItem;
    }

	
	/**
	 * Extends the superclass behavior to work around a bug in the J2RE 1.3.0.
	 */
    public void setPopupMenuVisible(boolean vis) {
        super.setPopupMenuVisible(vis);

        // [Pending:] This bug has been fixed in 1.3.1.
        if (vis || !Utilities.IS_130) 
            return;
        //
        Component component = getPopupMenu().getInvoker();
        if (!(component instanceof JComponent))
            return;
        //
        Container parent = (Container) component;
        while (parent instanceof JComponent) {
            component = parent;
            parent = component.getParent();
        }
        JComponent cToPaint = (JComponent) component;
        cToPaint.paintImmediately(0, 0,
            cToPaint.getWidth(),
            cToPaint.getHeight());
    }
	
	// Helper Code **********************************************************
	
	/**
	 * Sets the menu item's display mnemonic index using the index value
	 * as provided by the given action.
	 * <p>
	 * TODO 1.4, 1.5: The use of reflection becomes obsolete in 1.4;
	 * 1.5 hopefully has the Action key included.
	 */
    private void setDisplayedMnemonicIndex(JMenuItem menuItem, Action action) {
        Integer index =
            (Integer) action.getValue(ActionManager.DISPLAYED_MNEMONIC_INDEX);
        if (index == null)
            return;

        if (Utilities.IS_BEFORE_14) {
            menuItem.putClientProperty("displayedMnemonicIndex", index);
            return;
        }
        try {
            Method method =
                AbstractButton.class.getMethod(
                    "setDisplayedMnemonicIndex",
                    new Class[] { Integer.TYPE });
            method.invoke(menuItem, new Object[] { index });
        } catch (NoSuchMethodException e) {
        } catch (InvocationTargetException e) {
        } catch (IllegalAccessException e) {}
    }
	
}