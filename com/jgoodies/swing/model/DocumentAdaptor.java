package com.jgoodies.swing.model;

/*
 * Copyright (c) 2003 JGoodies Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of JGoodies Karsten Lentzsch.  
 * Use is subject to license terms.
 *
 */
import java.awt.Toolkit;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 * Converts the generic {@link ValueModel} interface into the model interface
 * as required by Swing text components. It is used to bind String values
 * to Swing text components, for example a a <code>JTextField</code>.
 * 
 * @author Karsten Lentzsch
 * @see	ValueModel
 * @see    javax.swing.text.Document
 */
public final class DocumentAdaptor extends PlainDocument implements ChangeListener {
	
	private final ValueModel subject;
    private final boolean   observeChanges;
	
	
	/**
	 * Constructs a <code>DocumentAdaptor</code> on the specified subject.
	 * The subject must answer values of type <code>String</code>.
     * 
     * @param subject  a <code>ValueModel</code> that answers Strings
     * @throws NullPointerException  if the subject is <code>null</code>
	 */
	public DocumentAdaptor(ValueModel subject) {
		this(subject, false);
	}
	
	
	/**
	 * Constructs a <code>DocumentAdaptor</code> on the specified subject.
     * The subject must answer values of type <code>String</code>.
     * Observes changes in the underlying subject if specified.
     * 
     * @param subject         a <code>ValueModel</code> that answers Strings
     * @param observeChanges  true to listen to subject changes
     * @throws NullPointerException  if the subject is <code>null</code>
	 */
	public DocumentAdaptor(ValueModel subject, boolean observeChanges) {
        if (subject == null)
            throw new NullPointerException("The subject must not be null.");
		this.subject = subject;
        this.observeChanges = observeChanges;
		setText0(getSubjectString());
		if (observeChanges)
			subject.addChangeListener(this);
	}
	
	
	/**
	 * ChangeListener implementation:
	 */
	public void stateChanged(ChangeEvent e) {
		String oldText = getText();
		String newText = getSubjectString();
		if (!newText.equals(oldText))
			setText0(newText);
	}
	
	
	/**
	 * Returns the text contained in this Document.
	 *
	 * @return the text
	 * @see #setText
	 */
	public String getText() {
		try {
			return getText(0, getLength());
		} catch (BadLocationException e) {
			return null;
		}
	}
	
	
	public void insertString(int offs, String str, AttributeSet attribute)
		throws BadLocationException {
		insertString0(offs, str, attribute);
		synchSubject();
	}
	
    private void insertString0(int offs, String str, AttributeSet attribute)
        throws BadLocationException {
        super.insertString(offs, str, attribute);
    }
    
	
	public void remove(int offs, int len) throws BadLocationException {
		super.remove(offs, len);
		synchSubject();
	}
	
    private void remove0(int offs, int len) throws BadLocationException {
        super.remove(offs, len);
    }
    
	
	/**
	 * Sets the text of this TextComponent to the specified text.  If the
	 * text is null or empty, has the effect of simply deleting the old text.
	 * <p>
	 * This method is thread safe, although most Swing methods
	 * are not. Please see 
	 * <A HREF="http://java.sun.com/products/jfc/swingdoc-archive/threads.html">Threads
	 * and Swing</A> for more information.     
	 *
	 * @param t the new text to be set
	 * @see #getText
	 */
	public void setText(String t) {
		try {
			remove(0, getLength());
			insertString(0, t, null);
		} catch (BadLocationException e) {
			Toolkit.getDefaultToolkit().beep();
		}
	}
	
    
    private void setText0(String t) {
        try {
            remove0(0, getLength());
            insertString0(0, t, null);
        } catch (BadLocationException e) {
            Toolkit.getDefaultToolkit().beep();
        }
    }
	
	private void synchSubject() throws BadLocationException {
		String newText = getText(0, getLength());
        if (observeChanges) {
            subject.removeChangeListener(this);
        }
		subject.setValue(newText);
        if (observeChanges) {
            subject.addChangeListener(this);
        }
	}
    
    private String getSubjectString() {
        String str = (String) subject.getValue();
        return str == null ? "" : str;
    }
    
    
}