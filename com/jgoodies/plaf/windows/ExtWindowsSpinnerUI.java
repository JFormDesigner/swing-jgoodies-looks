package com.jgoodies.plaf.windows;

/*
 * Copyright (c) 2002 Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of Karsten Lentzsch.  
 * Use is subject to license terms.
 *
 */

import java.awt.Component;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JSpinner;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.BorderUIResource;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicSpinnerUI;

import com.jgoodies.plaf.ExtBasicArrowButtonHandler;


/**
 * The JGoodies Windows Look and Feel implementation of
 * <code>SpinnerUI</code>.
 * 
 * @author Karsten Lentzsch
 */
public final class ExtWindowsSpinnerUI extends BasicSpinnerUI {
	
	private static final Border EMPTY_BORDER 
									= new BorderUIResource(new EmptyBorder(0, 0, 0, 0));
	
	
	
	public static ComponentUI createUI(JComponent b) {
		return new ExtWindowsSpinnerUI();
	}
	

    /**
     * The mouse/action listeners that are added to the spinner's 
     * arrow buttons.  These listeners are shared by all 
     * spinner arrow buttons.
     * 
     * @see #createNextButton
     * @see #createPreviousButton
     */
    private static final ExtBasicArrowButtonHandler nextButtonHandler     
    							= new ExtBasicArrowButtonHandler("increment", true);
    private static final ExtBasicArrowButtonHandler previousButtonHandler 
    							= new ExtBasicArrowButtonHandler("decrement", false);


    /**
     * Create a component that will replace the spinner models value
     * with the object returned by <code>spinner.getPreviousValue</code>.
     * By default the <code>previousButton</code> is a JButton
     * who's <code>ActionListener</code> updates it's <code>JSpinner</code>
     * ancestors model.  If a previousButton isn't needed (in a subclass)
     * then override this method to return null.
     *
     * @return a component that will replace the spinners model with the
     *     next value in the sequence, or null
     * @see #installUI
     * @see #createNextButton
     */
    protected Component createPreviousButton() {
		JButton b = new ExtWindowsArrowButton(SwingConstants.SOUTH);
		b.addActionListener(previousButtonHandler);
		b.addMouseListener(previousButtonHandler);
		return b;
    }


    /**
     * Create a component that will replace the spinner models value
     * with the object returned by <code>spinner.getNextValue</code>.
     * By default the <code>nextButton</code> is a JButton
     * who's <code>ActionListener</code> updates it's <code>JSpinner</code>
     * ancestors model.  If a nextButton isn't needed (in a subclass)
     * then override this method to return null.
     *
     * @return a component that will replace the spinners model with the
     *     next value in the sequence, or null
     * @see #installUI
     * @see #createPreviousButton
     */
    protected Component createNextButton() {
		JButton b = new ExtWindowsArrowButton(SwingConstants.NORTH);
		b.addActionListener(nextButtonHandler);
		b.addMouseListener(nextButtonHandler);
		return b;
    }


    /**
     * This method is called by installUI to get the editor component
     * of the <code>JSpinner</code>.  By default it just returns 
     * <code>JSpinner.getEditor()</code>.  Subclasses can override
     * <code>createEditor</code> to return a component that contains 
     * the spinner's editor or null, if they're going to handle adding 
     * the editor to the <code>JSpinner</code> in an 
     * <code>installUI</code> override.
     * <p>
     * Typically this method would be overridden to wrap the editor
     * with a container with a custom border, since one can't assume
     * that the editors border can be set directly.  
     * <p>
     * The <code>replaceEditor</code> method is called when the spinners
     * editor is changed with <code>JSpinner.setEditor</code>.  If you've
     * overriden this method, then you'll probably want to override
     * <code>replaceEditor</code> as well.
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
     * Called by the <code>PropertyChangeListener</code> when the 
     * <code>JSpinner</code> editor property changes.  It's the responsibility 
     * of this method to remove the old editor and add the new one.  By
     * default this operation is just:
     * <pre>
     * spinner.remove(oldEditor);
     * spinner.add(newEditor, "Editor");
     * </pre>
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