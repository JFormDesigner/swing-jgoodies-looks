/*
 * Copyright (c) 2001-2011 JGoodies Karsten Lentzsch. All Rights Reserved.
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

package com.jgoodies.looks.windows;

import java.awt.Component;
import java.awt.Insets;
import java.awt.LayoutManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.ComponentUI;

import com.jgoodies.looks.LookUtils;
import com.jgoodies.looks.common.ExtBasicSpinnerLayout;

/**
 * The JGoodies Windows L&amp;F implementation of {@code SpinnerUI}.
 * Configures the default editor to adjust font baselines and component
 * bounds, by setting an empty border with the default text insets.
 *
 * @author Karsten Lentzsch
 * @version $Revision: 1.13 $
 */
public final class WindowsSpinnerUI extends com.sun.java.swing.plaf.windows.WindowsSpinnerUI {


    public static ComponentUI createUI(JComponent b) {
        return new WindowsSpinnerUI();
    }


    /**
     * Create a component that will replace the spinner models value with the
     * object returned by {@code spinner.getPreviousValue}. By default
     * the {@code previousButton} is a JButton who's {@code ActionListener}
     * updates it's {@code JSpinner} ancestors model. If a
     * previousButton isn't needed (in a subclass) then override this method to
     * return null.
     *
     * @return a component that will replace the spinners model with the next
     *         value in the sequence, or null
     * @see #installUI
     * @see #createNextButton
     */
    @Override
    protected Component createPreviousButton() {
        if (LookUtils.IS_LAF_WINDOWS_XP_ENABLED) {
            return super.createPreviousButton();
        }

        Component c = new WindowsArrowButton(SwingConstants.SOUTH);
        installPreviousButtonListeners(c);
        return c;
    }


    /**
     * Create a component that will replace the spinner models value with the
     * object returned by {@code spinner.getNextValue}. By default the
     * {@code nextButton} is a JButton who's {@code ActionListener}
     * updates it's {@code JSpinner} ancestors model. If a nextButton
     * isn't needed (in a subclass) then override this method to return null.
     *
     * @return a component that will replace the spinners model with the next
     *         value in the sequence, or null
     * @see #installUI
     * @see #createPreviousButton
     */
    @Override
    protected Component createNextButton() {
        if (LookUtils.IS_LAF_WINDOWS_XP_ENABLED) {
            return super.createNextButton();
        }

        Component c = new WindowsArrowButton(SwingConstants.NORTH);
        installNextButtonListeners(c);
        return c;
    }


    /**
     * This method is called by installUI to get the editor component of the
     * {@code JSpinner}. By default it just returns {@code JSpinner.getEditor()}.
     * Subclasses can override {@code createEditor} to return a
     * component that contains the spinner's editor or null, if they're going
     * to handle adding the editor to the {@code JSpinner} in an {@code installUI}
     * override.
     * <p>
     * Typically this method would be overridden to wrap the editor with a
     * container with a custom border, since one can't assume that the editors
     * border can be set directly.
     * <p>
     * The {@code replaceEditor} method is called when the spinners
     * editor is changed with {@code JSpinner.setEditor}. If you've
     * overriden this method, then you'll probably want to override {@code replaceEditor}
     * as well.
     *
     * @return the JSpinners editor JComponent, spinner.getEditor() by default
     * @see #installUI
     * @see #replaceEditor
     * @see JSpinner#getEditor
     */
    @Override
    protected JComponent createEditor() {
        JComponent editor = spinner.getEditor();
        configureEditorBorder(editor);
        return editor;
    }


    /**
     * Create a {@code LayoutManager} that manages the {@code editor},
     * {@code nextButton}, and {@code previousButton} children
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
    @Override
    protected LayoutManager createLayout() {
        return new ExtBasicSpinnerLayout();
    }


    /**
     * Called by the {@code PropertyChangeListener} when the {@code JSpinner}
     * editor property changes. It's the responsibility of this method to
     * remove the old editor and add the new one. By default this operation is
     * just:
     *
     * <pre>
     *  spinner.remove(oldEditor); spinner.add(newEditor, "Editor");
     * </pre>
     *
     *
     * The implementation of {@code replaceEditor} should be coordinated
     * with the {@code createEditor} method.
     *
     * @see #createEditor
     * @see #createPropertyChangeListener
     */
    @Override
    protected void replaceEditor(JComponent oldEditor, JComponent newEditor) {
        spinner.remove(oldEditor);
        configureEditorBorder(newEditor);
        spinner.add(newEditor, "Editor");
    }


    /**
     * Sets an empty border with consistent insets.
     */
    private static void configureEditorBorder(JComponent editor) {
        if (editor instanceof JSpinner.DefaultEditor) {
            JSpinner.DefaultEditor defaultEditor = (JSpinner.DefaultEditor) editor;
            JTextField editorField = defaultEditor.getTextField();
            Insets insets = UIManager.getInsets("Spinner.defaultEditorInsets");
            editorField.setBorder(new EmptyBorder(insets));
        } else if (   editor instanceof JPanel
                && editor.getBorder() == null
                && editor.getComponentCount() > 0) {
            JComponent editorField = (JComponent) editor.getComponent(0);
            Insets insets = UIManager.getInsets("Spinner.defaultEditorInsets");
            editorField.setBorder(new EmptyBorder(insets));
        }
    }

}
