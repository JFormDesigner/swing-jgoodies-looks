/*
 * Copyright (c) 2001-2005 JGoodies Karsten Lentzsch. All Rights Reserved.
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
import com.jgoodies.forms.layout.FormLayout;

/** 
 * Contains a bunch of components with HTML labels.
 * 
 * @author Karsten Lentzsch
 * @version $Revision: 1.1 $
 */
final class HtmlTab {
    
    private static final String HTML_TEXT = 
        "<html><b>Bold</b>, <i>Italics</i>, <tt>Typewriter</tt></html>";

    private JButton       button;
    private JToggleButton toggleButton;
    private JComboBox     comboBox;
    private JRadioButton  radioButton;
    private JCheckBox     checkBox;
    private JLabel        label;
    
    
    /**
     * Creates and configures the UI components.
     */
    private void initComponents() {
        button = new JButton(HTML_TEXT);
        toggleButton = new JToggleButton(HTML_TEXT);
        radioButton = new JRadioButton(HTML_TEXT);
        radioButton.setContentAreaFilled(false);
        label = new JLabel(HTML_TEXT);
        checkBox = new JCheckBox(HTML_TEXT);
        checkBox.setContentAreaFilled(false);
        comboBox = new JComboBox(new String[] { HTML_TEXT, "Two", "Three" });
    }

    /**
     * Builds the panel.
     */
    JComponent build() {
        initComponents();
        
        FormLayout layout = new FormLayout(
                "right:max(50dlu;pref), 6dlu, pref",
                "");
        DefaultFormBuilder builder = new DefaultFormBuilder(layout);
        builder.setDefaultDialogBorder();
        builder.getPanel().setOpaque(false);
        
        builder.append("Button:",        button);
        builder.append("Toggle Button:", toggleButton);
        builder.append("Combo Box:",     comboBox);
        builder.append("Label:",         label);
        builder.append("Radio Button:",  radioButton);
        builder.append("Check Box:",     checkBox);
        return builder.getPanel();
    }


}