/*
 * Copyright (c) 2003 JGoodies Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of JGoodies Karsten Lentzsch.
 * Use is subject to license terms.
 *
 */

package com.jgoodies.looks.demo;

import javax.swing.*;

import com.jgoodies.forms.extras.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.plaf.Options;

/** 
 * Contains a bunch of components with HTML labels.
 * 
 * @author Karsten Lentzsch
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
        button.putClientProperty(Options.IS_NARROW_KEY, Boolean.TRUE);
        toggleButton = new JToggleButton(HTML_TEXT);
        toggleButton.putClientProperty(Options.IS_NARROW_KEY, Boolean.TRUE);
        radioButton = new JRadioButton(HTML_TEXT);
        label = new JLabel(HTML_TEXT);
        checkBox = new JCheckBox(HTML_TEXT);
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
        builder.setRowGroupingEnabled(true);
        builder.setDefaultDialogBorder();
        
        builder.append("Button:",        button);
        builder.append("Toggle Button:", toggleButton);
        builder.append("Combo Box:",     comboBox);
        builder.append("Label:",         label);
        builder.append("Radio Button:",  radioButton);
        builder.append("Check Box:",     checkBox);
        return builder.getPanel();
    }


}