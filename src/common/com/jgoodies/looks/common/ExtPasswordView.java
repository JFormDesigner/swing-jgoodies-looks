/*
 * Copyright (c) 2001-2005 JGoodies Karsten Lentzsch. All Rights Reserved.
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
 
package com.jgoodies.looks.common;

import java.awt.Container;
import java.awt.Graphics;
import java.awt.Shape;

import javax.swing.JPasswordField;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.PasswordView;
import javax.swing.text.Position;

/**
 * Differs from its superclass in that it uses a dot (\u25CF), 
 * not a star (&quot;*&quot;) as echo character.
 * 
 * @author Karsten Lentzsch
 * @version $Revision: 1.6 $
 */
public final class ExtPasswordView extends PasswordView {
    
    private static final char DOT_CHAR = '\u25CF'; 

    public ExtPasswordView(Element element) {
        super(element);
    }

    public float getPreferredSpan(int axis) {
        overrideEchoChar();
        return super.getPreferredSpan(axis);
    }
    
    public Shape modelToView(int pos, Shape a, Position.Bias b) throws BadLocationException {
        overrideEchoChar();
        return super.modelToView(pos, a, b);
    }
    
    public int viewToModel(float fx, float fy, Shape a, Position.Bias[] bias) {
        overrideEchoChar();
        return super.viewToModel(fx, fy, a, bias);
    }
    
    /*
     * Overrides the superclass behavior to paint a filled circle,
     * not the star (&quot;*&quot;) character.
     */
    protected int drawEchoCharacter(Graphics g, int x, int y, char c) {
        Container container = getContainer();
        if (!(container instanceof JPasswordField))
            return super.drawEchoCharacter(g, x, y, c);
        return super.drawEchoCharacter(g, x, y, DOT_CHAR);
    }
    
    private void overrideEchoChar() {
        Container container = getContainer();
        if (container instanceof JPasswordField) {
            JPasswordField field = (JPasswordField) container;
            if (field.echoCharIsSet()) {
                field.setEchoChar(DOT_CHAR);
            }
        }
    }
    
    
}