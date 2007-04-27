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

package com.jgoodies.looks.plastic;

import java.awt.EventQueue;
import java.awt.event.FocusEvent;
import java.awt.event.MouseEvent;

import javax.swing.JFormattedTextField;
import javax.swing.SwingUtilities;
import javax.swing.plaf.UIResource;
import javax.swing.text.DefaultCaret;
import javax.swing.text.JTextComponent;

/**
 * PlasticFieldCaret is visible in non-editable fields,
 * and the text is selected after a keyboard focus gained event.
 * For the latter see also issue #4337647 in Sun's bug database.
 *
 * @author Karsten Lentzsch
 * @version $Revision: 1.5 $
 */
final class PlasticFieldCaret extends DefaultCaret implements UIResource {

    PlasticFieldCaret() {
        super();
    }


    private boolean isKeyboardFocusEvent = true;


    public void focusGained(FocusEvent e) {
        if (getComponent().isEnabled()) {
            setVisible(true);
            setSelectionVisible(true);
        }

        final JTextComponent c = getComponent();
        if (c.isEnabled() && isKeyboardFocusEvent) {
            if (c instanceof JFormattedTextField) {
                EventQueue.invokeLater(new Runnable() {
                    public void run() {
                        PlasticFieldCaret.super.setDot(0);
                        PlasticFieldCaret.super.moveDot(c.getDocument().getLength());
                    }
                });
            } else {
                super.setDot(0);
                super.moveDot(c.getDocument().getLength());
            }
        }
    }


    public void focusLost(FocusEvent e) {
        super.focusLost(e);
        if (!e.isTemporary()) {
            isKeyboardFocusEvent = true;
        }
    }


    public void mousePressed(MouseEvent e) {
        if (SwingUtilities.isLeftMouseButton(e) || e.isPopupTrigger()) {
            isKeyboardFocusEvent = false;
        }
        super.mousePressed(e);

    }


    public void mouseReleased(MouseEvent e) {
        super.mouseReleased(e);
        if (e.isPopupTrigger()) {
            isKeyboardFocusEvent = false;
            if ((getComponent() != null) && getComponent().isEnabled()
                    && getComponent().isRequestFocusEnabled()) {
                getComponent().requestFocus();
            }
        }
    }

}