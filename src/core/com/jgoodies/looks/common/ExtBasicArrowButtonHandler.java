/*
 * Copyright (c) 2001-2006 JGoodies Karsten Lentzsch. All Rights Reserved.
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

import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.Container;
import java.awt.FocusTraversalPolicy;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.AttributedCharacterIterator;
import java.text.CharacterIterator;
import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Map;

import javax.swing.*;
import javax.swing.text.InternationalFormatter;

/**
 * A handler for spinner arrow button mouse and action events.  When 
 * a left mouse pressed event occurs we look up the (enabled) spinner 
 * that's the source of the event and start the autorepeat timer.  The
 * timer fires action events until any button is released at which 
 * point the timer is stopped and the reference to the spinner cleared.
 * The timer doesn't start until after a 300ms delay, so often the 
 * source of the initial (and final) action event is just the button
 * logic for mouse released - which means that we're relying on the fact
 * that our mouse listener runs after the buttons mouse listener.<p>
 * 
 * Note that one instance of this handler is shared by all slider previous 
 * arrow buttons and likewise for all of the next buttons, 
 * so it doesn't have any state that persists beyond the limits
 * of a single button pressed/released gesture.<p>
 * 
 * Copied from javax.swing.BasicSpinnerUI
 * 
 * @version $Revision: 1.3 $
 * 
 * @see javax.swing.plaf.basic.BasicSpinnerUI
 */
public final class ExtBasicArrowButtonHandler extends AbstractAction implements
        MouseListener, FocusListener {

    private final javax.swing.Timer autoRepeatTimer;
    private final boolean isNext;

    private JSpinner spinner;
    private JButton arrowButton;


    public ExtBasicArrowButtonHandler(String name, boolean isNext) {
        super(name);
        this.isNext = isNext;
        autoRepeatTimer = new javax.swing.Timer(60, this);
        autoRepeatTimer.setInitialDelay(300);
    }


    private JSpinner eventToSpinner(AWTEvent e) {
        Object src = e.getSource();
        while ((src instanceof Component) && !(src instanceof JSpinner)) {
            src = ((Component) src).getParent();
        }
        return (src instanceof JSpinner) ? (JSpinner) src : null;
    }


    public void actionPerformed(ActionEvent e) {
        JSpinner spinner = this.spinner;

        if (!(e.getSource() instanceof javax.swing.Timer)) {
            // Most likely resulting from being in ActionMap.
            spinner = eventToSpinner(e);
            if (e.getSource() instanceof JButton) {
                arrowButton = (JButton) e.getSource();
            }
        } else {
            if (arrowButton != null && !arrowButton.getModel().isPressed()
                    && autoRepeatTimer.isRunning()) {
                autoRepeatTimer.stop();
                spinner = null;
                arrowButton = null;
            }
        }
        if (spinner != null) {
            try {
                int calendarField = getCalendarField(spinner);
                spinner.commitEdit();
                if (calendarField != -1) {
                    ((SpinnerDateModel) spinner.getModel())
                            .setCalendarField(calendarField);
                }
                Object value = (isNext) ? spinner.getNextValue() : spinner
                        .getPreviousValue();
                if (value != null) {
                    spinner.setValue(value);
                    select(spinner);
                }
            } catch (IllegalArgumentException iae) {
                UIManager.getLookAndFeel().provideErrorFeedback(spinner);
            } catch (ParseException pe) {
                UIManager.getLookAndFeel().provideErrorFeedback(spinner);
            }
        }
    }


    /**
     * If the spinner's editor is a DateEditor, this selects the field
     * associated with the value that is being incremented.
     */
    private void select(JSpinner aSpinner) {
        JComponent editor = aSpinner.getEditor();

        if (editor instanceof JSpinner.DateEditor) {
            JSpinner.DateEditor dateEditor = (JSpinner.DateEditor) editor;
            JFormattedTextField ftf = dateEditor.getTextField();
            Format format = dateEditor.getFormat();
            Object value;

            if (format != null && (value = aSpinner.getValue()) != null) {
                SpinnerDateModel model = dateEditor.getModel();
                DateFormat.Field field = DateFormat.Field.ofCalendarField(model
                        .getCalendarField());

                if (field != null) {
                    try {
                        AttributedCharacterIterator iterator = format
                                .formatToCharacterIterator(value);
                        if (!select(ftf, iterator, field)
                                && field == DateFormat.Field.HOUR0) {
                            select(ftf, iterator, DateFormat.Field.HOUR1);
                        }
                    } catch (IllegalArgumentException iae) {
                        // Should not happen
                    }
                }
            }
        }
    }


    /**
     * Selects the passed in field, returning true if it is found, false otherwise.
     */
    private boolean select(JFormattedTextField ftf,
            AttributedCharacterIterator iterator, DateFormat.Field field) {
        int max = ftf.getDocument().getLength();

        iterator.first();
        do {
            Map attrs = iterator.getAttributes();

            if (attrs != null && attrs.containsKey(field)) {
                int start = iterator.getRunStart(field);
                int end = iterator.getRunLimit(field);

                if (start != -1 && end != -1 && start <= max && end <= max) {
                    ftf.select(start, end);
                }
                return true;
            }
        } while (iterator.next() != CharacterIterator.DONE);
        return false;
    }


    /**
     * Returns the calendarField under the start of the selection, or
     * -1 if there is no valid calendar field under the selection (or
     * the spinner isn't editing dates.
     */
    private int getCalendarField(JSpinner aSpinner) {
        JComponent editor = aSpinner.getEditor();

        if (editor instanceof JSpinner.DateEditor) {
            JSpinner.DateEditor dateEditor = (JSpinner.DateEditor) editor;
            JFormattedTextField ftf = dateEditor.getTextField();
            int start = ftf.getSelectionStart();
            JFormattedTextField.AbstractFormatter formatter = ftf
                    .getFormatter();

            if (formatter instanceof InternationalFormatter) {
                Format.Field[] fields = ((InternationalFormatter) formatter)
                        .getFields(start);

                for (int counter = 0; counter < fields.length; counter++) {
                    if (fields[counter] instanceof DateFormat.Field) {
                        int calendarField;

                        if (fields[counter] == DateFormat.Field.HOUR1) {
                            calendarField = Calendar.HOUR;
                        } else {
                            calendarField = ((DateFormat.Field) fields[counter])
                                    .getCalendarField();
                        }
                        if (calendarField != -1) { return calendarField; }
                    }
                }
            }
        }
        return -1;
    }


    public void mousePressed(MouseEvent e) {
        if (SwingUtilities.isLeftMouseButton(e) && e.getComponent().isEnabled()) {
            spinner = eventToSpinner(e);
            autoRepeatTimer.start();
            focusSpinnerIfNecessary();
        }
    }


    public void mouseReleased(MouseEvent e) {
        autoRepeatTimer.stop();
        spinner = null;
        arrowButton = null;
    }


    public void mouseClicked(MouseEvent e) {
    // Do nothing
    }


    public void mouseEntered(MouseEvent e) {
        if (spinner != null && !autoRepeatTimer.isRunning()) {
            autoRepeatTimer.start();
        }
    }


    public void mouseExited(MouseEvent e) {
        if (autoRepeatTimer.isRunning()) {
            autoRepeatTimer.stop();
        }
    }


    /**
     * Requests focus on a child of the spinner if the spinner doesn't
     * have focus.
     */
    private void focusSpinnerIfNecessary() {
        Component fo = KeyboardFocusManager.getCurrentKeyboardFocusManager()
                .getFocusOwner();
        if (spinner.isRequestFocusEnabled()
                && (fo == null || !SwingUtilities.isDescendingFrom(fo, spinner))) {
            Container root = spinner;

            if (!root.isFocusCycleRoot()) {
                root = root.getFocusCycleRootAncestor();
            }
            if (root != null) {
                FocusTraversalPolicy ftp = root.getFocusTraversalPolicy();
                Component child = ftp.getComponentAfter(root, spinner);

                if (child != null
                        && SwingUtilities.isDescendingFrom(child, spinner)) {
                    child.requestFocus();
                }
            }
        }
    }


    public void focusGained(FocusEvent e) {
    // Do nothing
    }


    public void focusLost(FocusEvent e) {
        if (autoRepeatTimer.isRunning()) {
            autoRepeatTimer.stop();
        }
        spinner = null;
        if (arrowButton != null) {
            ButtonModel model = arrowButton.getModel();
            model.setPressed(false);
            model.setArmed(false);
            arrowButton = null;
        }
    }

}
