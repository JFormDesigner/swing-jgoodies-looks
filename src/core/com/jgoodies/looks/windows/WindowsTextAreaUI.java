/*
 * Copyright (c) 2001-2007 JGoodies Karsten Lentzsch. All Rights Reserved.
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

import java.awt.Color;
import java.beans.PropertyChangeEvent;

import javax.swing.JComponent;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;
import javax.swing.text.JTextComponent;


/**
 * The JGoodies Windows L&amp;F implementation of <code>TextAreaUI</code>.
 * In addition to its superclass WindowsTextAreaUI, it updates the
 * background colors using behavior from BasicTextFieldUI.
 * 
 * @author Karsten Lentzsch
 * @version $Revision: 1.5 $
 */
public final class WindowsTextAreaUI extends com.sun.java.swing.plaf.windows.WindowsTextAreaUI {

    /**
     * Creates a UI for a JTextArea.
     *
     * @param c the text area
     * @return the UI
     */
    public static ComponentUI createUI(JComponent c) {
        return new WindowsTextAreaUI();
    }
    

    
    public void installUI(JComponent c) {
        super.installUI(c);
        updateBackground((JTextComponent) c);
    }

    
    /**
     * This method gets called when a bound property is changed
     * on the associated JTextComponent. In addition to the superclass
     * behavior, this UI updates the background if the <em>editable</em> or
     * <em>enabled</em> property changes.
     */
    protected void propertyChange(PropertyChangeEvent evt) {
        super.propertyChange(evt);
        String propertyName = evt.getPropertyName();
        if (    "editable".equals(propertyName)
             || "enabled".equals(propertyName)) {
            updateBackground((JTextComponent) evt.getSource());
        }
    }

    
    private void updateBackground(JTextComponent c) {
        Color background = c.getBackground();
        if (!(background instanceof UIResource)) {
            return;
        }
        Color newColor = null;
        if (!c.isEnabled()) {
            newColor = UIManager.getColor("TextArea.disabledBackground");
        }
        if (newColor == null && !c.isEditable()) {
            newColor = UIManager.getColor("TextArea.inactiveBackground");
        }
        if (newColor == null) {
            newColor = UIManager.getColor("TextArea.background");
        }
        if (newColor != null && newColor != background) {
            c.setBackground(newColor);
        }
    }
    
    
}
