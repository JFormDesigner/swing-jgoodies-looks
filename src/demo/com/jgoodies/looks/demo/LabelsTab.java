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

package com.jgoodies.looks.demo;

import java.awt.BorderLayout;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/** 
 * Demonstrates the fonts used for JLabel, TitledBorder, 
 * and JGoodies Forms title and titled separator.
 * 
 * @author Karsten Lentzsch
 * @version $Revision: 1.1 $
 */
final class LabelsTab {
    

    /**
     * Builds a panel using <code>FormLayout</code> that consists
     * of rows of different Swing components all centered vertically.
     * 
     * @return the built panel
     */
    JComponent build() {
        FormLayout layout = new FormLayout(
                "0:grow, max(pref;150dlu), 0:grow", 
                "pref, 21dlu, pref, 21dlu, pref, 21dlu, fill:75dlu");
        
        PanelBuilder builder = new PanelBuilder(layout);
        builder.setDefaultDialogBorder();
        builder.getPanel().setOpaque(false);
        CellConstraints cc = new CellConstraints();
        
        builder.addLabel    ("JLabel",            cc.xy(2, 1));
        builder.addTitle    ("Title",             cc.xy(2, 3));
        builder.addSeparator("Titled Separator",  cc.xy(2, 5));
        builder.add         (buildTitledBorder(), cc.xy(2, 7));
        
        return builder.getPanel();
    }
    
    
    private JComponent buildTitledBorder() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new TitledBorder("TitledBorder"));
        panel.setOpaque(false);
        return panel;
    }
    
   
}