package com.jgoodies.swing.util;

/*
 * Copyright (c) 2003 JGoodies Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of Karsten Lentzsch.  
 * Use is subject to license terms.
 *
 */

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;

import com.jgoodies.swing.model.ValueModel;
import com.jgoodies.util.HistoryList;
import com.jgoodies.util.Utilities;
import com.jgoodies.util.logging.Logger;

/**
 * A class that provides the state and behavior for a &quote;browser back button&quote;.
 *
 * @author Karsten Lentzsch
 */
public class History implements ChangeListener {

    private static final int MAX_TOOLTIP_LENGTH = 60;

    private final Action     goBackAction;
    private final Action     goNextAction;
    private final Action     goHomeAction;
    private final JPopupMenu backPopupMenu;
    private final JPopupMenu nextPopupMenu;

    private Object      home;
    private int         capacity;
    private HistoryList list;
    private ChangeEvent changeEvent;
    private EventListenerList listenerList = new EventListenerList();

    /**
     * Constructs a <code>History</code> with the given maximum capacity.
     */
    public History(int capacity) {
        this.capacity = capacity;
        goBackAction = new NavigationAction(NavigationAction.BACK);
        goNextAction = new NavigationAction(NavigationAction.NEXT);
        goHomeAction = new NavigationAction(NavigationAction.HOME);
        backPopupMenu = new JPopupMenu();
        backPopupMenu.putClientProperty("jgoodies.noIcons", Boolean.TRUE);
        nextPopupMenu = new JPopupMenu();
        nextPopupMenu.putClientProperty("jgoodies.noIcons", Boolean.TRUE);
        reset();
    }

    /**
     * Creates and answer a tool tip for the given object;
     * clippes the string's center, if it exceeds a maximum length.
     */
    protected String createToolTip(Object anObject) {
        return Utilities.centerClippedString(
            anObject.toString(),
            MAX_TOOLTIP_LENGTH);
    }

    /**
     * Adds a ChangeListener to the history.
     */
    public void addChangeListener(ChangeListener l) {
        listenerList.add(ChangeListener.class, l);
    }

    /**
     * Removes a ChangeListener from the history.
     */
    public void removeChangeListener(ChangeListener l) {
        listenerList.remove(ChangeListener.class, l);
    }

    public Action getGoBackAction() {
        return goBackAction;
    }
    
    public Action getGoHomeAction() {
        return goHomeAction;
    }
    
    public Action getGoNextAction() {
        return goNextAction;
    }
    
    public Object getSelection() {
        return list.getLastAdded();
    }
    
    public Object getHome() {
        return home;
    }
    
    public void setHome(Object home) {
        this.home = home;
    }

    /**
     * Creates and answers the go back button.
     */
    public PopupButton createGoBackButton() {
        return new PopupButton(
            new ToolBarButton(getGoBackAction()),
            backPopupMenu);
    }

    /**
     * Creates and answers the go next button.
     */
    public PopupButton createGoNextButton() {
        return new PopupButton(
            new ToolBarButton(getGoNextAction()),
            nextPopupMenu);
    }

    private String getBackText() {
        Object previous = list.getPrevious();
        return null == previous
            ? "Back"
            : "Back to " + createToolTip(previous);
    }

    private String getNextText() {
        Object next = list.getNext();
        return null == next ? "Next" : "Next to " + createToolTip(next);
    }

    private class NavigationAction extends AbstractAction {

        private static final String BACK = "back";
        private static final String NEXT = "next";
        private static final String HOME = "home";

        private final String id;

        private NavigationAction(String id) {
            this.id = id;
            setEnabled(false);
        }

        public void actionPerformed(ActionEvent event) {
            Object newSelection;
            if (id.equals(BACK))
                newSelection = list.getAndGoPrevious();
            else if (id.equals(NEXT))
                newSelection = list.getAndGoNext();
            else if (id.equals(HOME))
                newSelection = home;
            else {
                Logger.getLogger("History.NavigationAction").warning(
                    "Unknown id: " + id);
                return;
            }
            setSelection0(newSelection);
            fireStateChanged();
        }
    }

    /**
     * Resets the history: clears the list, and disables all buttons.
     */
    public void reset() {
        list = new HistoryList(capacity);
        goBackAction.setEnabled(false);
        goNextAction.setEnabled(false);
        goHomeAction.setEnabled(false);
    }

    /**
     * Sets the given object as new selection
     */
    public void setSelection(Object selection) {
        if (null == selection || selection.equals(getSelection()))
            return;

        setSelection0(selection);
    }

    private void setSelection0(Object selection) {
        if (null == selection)
            return;

        list.add(selection);
        updateMenu(backPopupMenu, list.getBackIterator(), true);
        updateMenu(nextPopupMenu, list.getNextIterator(), false);
        updateActions();
    }

    public void stateChanged(ChangeEvent event) {
        ValueModel model = (ValueModel) event.getSource();
        setSelection(model.getValue());
    }

    private void updateActions() {
        Object selection = getSelection();
        Object previous = list.getPrevious();
        Object next = list.getNext();

        String goBackToolTip = getBackText();
        String goNextToolTip = getNextText();

        goBackAction.putValue(Action.SHORT_DESCRIPTION, goBackToolTip);
        goBackAction.putValue(Action.NAME, goBackToolTip);
        goNextAction.putValue(Action.SHORT_DESCRIPTION, goNextToolTip);
        goNextAction.putValue(Action.NAME, goNextToolTip);

        goBackAction.setEnabled(previous != null);
        goNextAction.setEnabled(next != null);
        goHomeAction.setEnabled(!home.equals(selection));
    }

    private void updateMenu(
        JPopupMenu menu,
        Iterator items,
        final boolean goBack) {
        menu.removeAll();

        int i = 1;
        while (items.hasNext()) {
            final int steps = i;
            String label = createToolTip(items.next());
            JMenuItem menuItem = new JMenuItem(label);
            menuItem.putClientProperty("noIcon", Boolean.TRUE);
            menuItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (goBack)
                        list.goBack(steps);
                    else
                        list.goNext(steps);
                    updateState();
                    fireStateChanged();
                }
            });
            menu.add(menuItem);
            i++;
        }
    }

    private void updateState() {
        updateMenu(backPopupMenu, list.getBackIterator(), true);
        updateMenu(nextPopupMenu, list.getNextIterator(), false);
        updateActions();
    }

    /*
     * Notify all listeners that have registered interest for
     * notification on this event type.  The event instance 
     * is lazily created using the parameters passed into 
     * the fire method.
     * @see EventListenerList
     */
    private void fireStateChanged() {
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == ChangeListener.class) {
                // Lazily create the event:
                if (changeEvent == null)
                    changeEvent = new ChangeEvent(this);
                ((ChangeListener) listeners[i + 1]).stateChanged(changeEvent);
            }
        }
    }

}