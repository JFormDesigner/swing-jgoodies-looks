/*
 * Copyright (c) 2003 JGoodies Karsten Lentzsch. All Rights Reserved.
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

import javax.swing.*;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.Sizes;
import com.jgoodies.plaf.Options;

/** 
 * Consists of rows of centered components to check alignment
 * of font baselines and centered perceived bounds.
 * 
 * @author Karsten Lentzsch
 * @version $Revision: 1.4 $
 */
final class AlignmentTab {
    
    private static final String TEST_STR = "EEEEE";

    /**
     * Builds a panel using <code>FormLayout</code> that consists
     * of rows of different Swing components all centered vertically.
     * 
     * @return the built panel
     */
    JComponent build() {
        FormLayout layout = new FormLayout(
                "p, 2px, 35dlu, 2px, 35dlu, 2px, 35dlu, 2px, max(35dlu;p)");
        
        DefaultFormBuilder builder = new DefaultFormBuilder(layout);
        builder.setDefaultDialogBorder();
        builder.setLineGapSize(Sizes.pixel(1));
        
        builder.append(createHelpLabel(), 9);

        builder.appendRow(FormFactory.PARAGRAPH_GAP_ROWSPEC);
        builder.nextLine(2);
        
        builder.append(createCenteredLabel("Label"));
        builder.append(createCenteredLabel("Field"));
        builder.append(createCenteredLabel("Edit"));
        builder.append(createCenteredLabel("Choice"));
        builder.append(createCenteredLabel("Button"));
        builder.append(TEST_STR);
        builder.append(new JTextField(TEST_STR));
        builder.append(createComboBox(TEST_STR, true));
        builder.append(createComboBox(TEST_STR, false));
        builder.append(createNarrowButton(TEST_STR));
        
        builder.appendRow(FormFactory.PARAGRAPH_GAP_ROWSPEC);
        builder.nextLine(2);
        
        builder.append(createCenteredLabel("Label"));
        builder.append(createCenteredLabel("Field"));
        builder.append(createCenteredLabel("Area"));
        builder.append(createCenteredLabel("Pane"));
        builder.append(createCenteredLabel("Button"));
        builder.append(TEST_STR);
        builder.append(new JTextField(TEST_STR));
        builder.append(createWrappedTextArea(TEST_STR));
        builder.append(createWrappedEditorPane(TEST_STR));
        builder.append(createNarrowButton(TEST_STR));
        
        builder.appendRow(FormFactory.PARAGRAPH_GAP_ROWSPEC);
        builder.nextLine(2);
        
        builder.append(createCenteredLabel("Label"));
        builder.append(createCenteredLabel("Field"));
        builder.append(createCenteredLabel("Format"));
        builder.append(createCenteredLabel("Pass"));
        builder.append(createCenteredLabel("Button"));
        builder.append(TEST_STR);
        builder.append(new JTextField(TEST_STR));
        builder.append(new JFormattedTextField(TEST_STR));
        builder.append(new JPasswordField(TEST_STR));
        builder.append(createNarrowButton(TEST_STR));
        
        builder.appendRow(FormFactory.PARAGRAPH_GAP_ROWSPEC);
        builder.nextLine(2);
        
        builder.append(createCenteredLabel("Label"));
        builder.append("Field");
        builder.append("Combo");
        builder.append("Spinner");
        builder.append("Button");
        builder.append(TEST_STR);
        builder.append(new JTextField(TEST_STR));
        builder.append(createComboBox(TEST_STR, true));
        builder.append(createSpinner(TEST_STR));
        builder.append(createNarrowButton(TEST_STR));

        return builder.getPanel();
    }


    // Helper Code **********************************************************
    
    private JComponent createHelpLabel() {
        return new JLabel("Texts shall be aligned, perceived bounds centered.");
    }
    
    private JLabel createCenteredLabel(String text) {
        return new JLabel(text, JLabel.CENTER);
    }

    private JComboBox createComboBox(String selectedText, boolean editable) {
        JComboBox box = new JComboBox(new String[] { 
            selectedText, "1", "2", "3", "4", "5", "Two", "Three", "Four", /* "This is a quite long label"*/ });
        box.setEditable(editable);
        return box;
    }
    
    private JButton createNarrowButton(String text) {
        JButton button = new JButton(text);
        button.putClientProperty(Options.IS_NARROW_KEY, Boolean.TRUE);
        return button;
    }
    
    private JComponent createWrappedTextArea(String text) {
        JTextArea area = new JTextArea(text);
        return new JScrollPane(area, 
                JScrollPane.VERTICAL_SCROLLBAR_NEVER,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    }

    private JComponent createWrappedEditorPane(String text) {
        JEditorPane pane = new JEditorPane();
        pane.setText(text);
        return new JScrollPane(pane, 
                JScrollPane.VERTICAL_SCROLLBAR_NEVER,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    }
    
    private JSpinner createSpinner(String choice) {
        JSpinner spinner = new JSpinner();
        spinner.setModel(new SpinnerListModel(new String[] {
                choice, choice + "1", choice + "2"}));
        return spinner;
    }


}