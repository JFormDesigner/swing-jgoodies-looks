/*
 * Copyright (c) 2003 JGoodies Karsten Lentzsch. All Rights Reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * o Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * 
 * o Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * 
 * o Neither the name of JGoodies Karsten Lentzsch nor the names of its
 * contributors may be used to endorse or promote products derived from this
 * software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

package com.jgoodies.plaf.windows;

import java.awt.Component;
import java.awt.LayoutManager;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JSpinner;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.BorderUIResource;
import javax.swing.plaf.ComponentUI;

import com.sun.java.swing.plaf.windows.WindowsSpinnerUI;

import com.jgoodies.plaf.LookUtils;
import com.jgoodies.plaf.common.ExtBasicArrowButtonHandler;
import com.jgoodies.plaf.common.ExtBasicSpinnerLayout;

/**
 * The JGoodies Windows Look and Feel implementation of <code>SpinnerUI</code>.
 * 
 * @author Karsten Lentzsch
 */
public final class ExtWindowsSpinnerUI extends WindowsSpinnerUI {

    private static final Border EMPTY_BORDER = new BorderUIResource(
                                                     new EmptyBorder(2, 2, 2, 2));


    public static ComponentUI createUI(JComponent b) {
        return new ExtWindowsSpinnerUI();
    }

    /**
     * The mouse/action listeners that are added to the spinner's arrow
     * buttons. These listeners are shared by all spinner arrow buttons.
     * 
     * @see #createNextButton
     * @see #createPreviousButton
     */
    private static final ExtBasicArrowButtonHandler nextButtonHandler     = new ExtBasicArrowButtonHandler(
                                                                                  "increment",
                                                                                  true);
    private static final ExtBasicArrowButtonHandler previousButtonHandler = new ExtBasicArrowButtonHandler(
                                                                                  "decrement",
                                                                                  false);


    /**
     * Create a component that will replace the spinner models value with the
     * object returned by <code>spinner.getPreviousValue</code>. By default
     * the <code>previousButton</code> is a JButton who's <code>ActionListener</code>
     * updates it's <code>JSpinner</code> ancestors model. If a
     * previousButton isn't needed (in a subclass) then override this method to
     * return null.
     * 
     * @return a component that will replace the spinners model with the next
     *         value in the sequence, or null
     * @see #installUI
     * @see #createNextButton
     */
    protected Component createPreviousButton() {
        if (LookUtils.IS_LAF_WINDOWS_XP_ENABLED)
            return super.createPreviousButton();
        
        JButton b = new ExtWindowsArrowButton(SwingConstants.SOUTH);
        b.addActionListener(previousButtonHandler);
        b.addMouseListener(previousButtonHandler);
        return b;
    }


    /**
     * Create a component that will replace the spinner models value with the
     * object returned by <code>spinner.getNextValue</code>. By default the
     * <code>nextButton</code> is a JButton who's <code>ActionListener</code>
     * updates it's <code>JSpinner</code> ancestors model. If a nextButton
     * isn't needed (in a subclass) then override this method to return null.
     * 
     * @return a component that will replace the spinners model with the next
     *         value in the sequence, or null
     * @see #installUI
     * @see #createPreviousButton
     */
    protected Component createNextButton() {
        if (LookUtils.IS_LAF_WINDOWS_XP_ENABLED)
            return super.createNextButton();
        
        JButton b = new ExtWindowsArrowButton(SwingConstants.NORTH);
        b.addActionListener(nextButtonHandler);
        b.addMouseListener(nextButtonHandler);
        return b;
    }


    /**
     * This method is called by installUI to get the editor component of the
     * <code>JSpinner</code>. By default it just returns <code>JSpinner.getEditor()</code>.
     * Subclasses can override <code>createEditor</code> to return a
     * component that contains the spinner's editor or null, if they're going
     * to handle adding the editor to the <code>JSpinner</code> in an <code>installUI</code>
     * override.
     * <p>
     * Typically this method would be overridden to wrap the editor with a
     * container with a custom border, since one can't assume that the editors
     * border can be set directly.
     * <p>
     * The <code>replaceEditor</code> method is called when the spinners
     * editor is changed with <code>JSpinner.setEditor</code>. If you've
     * overriden this method, then you'll probably want to override <code>replaceEditor</code>
     * as well.
     * 
     * @return the JSpinners editor JComponent, spinner.getEditor() by default
     * @see #installUI
     * @see #replaceEditor
     * @see JSpinner#getEditor
     */
    protected JComponent createEditor() {
        JComponent editor = spinner.getEditor();
        configureEditor(editor);
        return editor;
    }


    /**
     * Create a <code>LayoutManager</code> that manages the <code>editor</code>,
     * <code>nextButton</code>, and <code>previousButton</code> children
     * of the JSpinner. These three children must be added with a constraint
     * that identifies their role: "Editor", "Next", and "Previous". The
     * default layout manager can handle the absence of any of these children.
     * 
     * @return a LayoutManager for the editor, next button, and previous
     *         button.
     * @see #createNextButton
     * @see #createPreviousButton
     * @see #createEditor
     */
    protected LayoutManager createLayout() {
        return new ExtBasicSpinnerLayout();
    }


    /**
     * Called by the <code>PropertyChangeListener</code> when the <code>JSpinner</code>
     * editor property changes. It's the responsibility of this method to
     * remove the old editor and add the new one. By default this operation is
     * just:
     * 
     * <pre>
     *  spinner.remove(oldEditor); spinner.add(newEditor, "Editor");
     * </pre>
     * 
     * 
     * The implementation of <code>replaceEditor</code> should be coordinated
     * with the <code>createEditor</code> method.
     * 
     * @see #createEditor
     * @see #createPropertyChangeListener
     */
    protected void replaceEditor(JComponent oldEditor, JComponent newEditor) {
        spinner.remove(oldEditor);
        configureEditor(newEditor);
        spinner.add(newEditor, "Editor");
    }


    /**
     * Removes an obsolete Border from Default editors.
     */
    private void configureEditor(JComponent editor) {
        if ((editor instanceof JSpinner.DefaultEditor)) {
            JSpinner.DefaultEditor defaultEditor = (JSpinner.DefaultEditor) editor;
            defaultEditor.getTextField().setBorder(EMPTY_BORDER);
        }
    }

}