package com.jgoodies.looks.demo;

/*
 * Copyright (c) 2003 JGoodies Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of JGoodies Karsten Lentzsch.
 * Use is subject to license terms.
 *
 */

import java.awt.*;

import javax.swing.*;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.layout.Grid;
import com.jgoodies.swing.application.ResourceManager;
import com.jgoodies.util.Utilities;

/** 
 * Contains a bunch of buttons with in different states and configurations.
 * 
 * @author Karsten Lentzsch
 */
final class AlignmentTab {

    private JTextField  textfield;
    private JComboBox   editableCombo;
    private JComboBox   uneditableCombo;
    private JButton     button;

    private JLabel      test1Label;
    private JLabel      test2Label;
    private JLabel      test3Label;


    /**
     * Creates and configures the UI components.
     */
    private void initComponents() {
        textfield       = new JTextField("Align");
        editableCombo   = createComboBox(true);
        uneditableCombo = createComboBox(false);
        button          = new JButton("Align");
        test1Label      = new JLabel("Passed");
        test2Label      = new JLabel("Passed");
        test3Label      = new JLabel("Passed");
    }
    
    
    /**
     * Builds the panel.
     */
    JComponent build() {
        initComponents();
        
        FormLayout layout = new FormLayout(
                "7dlu, left:pref, 4dlu, 50dlu, 4dlu, 50dlu, 4dlu, 50dlu, 4dlu, 50dlu, 0:grow",
                "pref, 2dlu, pref, 4dlu, pref, 11dlu, "
              + "pref, 2dlu, pref, 11dlu, "
              + "pref, 2dlu, pref, 4dlu, pref, 4dlu, pref, 0:grow");
        PanelBuilder builder = new PanelBuilder(layout);
        builder.setDefaultDialogBorder();
        CellConstraints cc = new CellConstraints();

        builder.addSeparator("FormLayout",          cc.xywh(1, 1, 11, 1));

        builder.addLabel("Align:",                  cc.xy( 2, 3));
        builder.add(textfield,                      cc.xy( 4, 3));
        builder.add(editableCombo,                  cc.xy( 6, 3));
        builder.add(uneditableCombo,                cc.xy( 8, 3));
        builder.add(button,                         cc.xy(10, 3));

        builder.addLabel("Align:",                  cc.xy( 2, 5));
        builder.add(new JTextField("Align"),        cc.xy( 4, 5));
        builder.add(new JRadioButton("Align"),      cc.xy( 6, 5));
        builder.add(new JCheckBox("Align"),         cc.xy( 8, 5));
        builder.add(new JButton("Align"),           cc.xy(10, 5));

        // GridBagLayout
        builder.addSeparator("GridBagLayout",       cc.xywh(1, 7, 11, 1));
        builder.add(buildGridBagAlignmentPanel(),   cc.xywh(2, 9,  9, 1));

        // Test Results
        builder.addSeparator("Height Test Results", cc.xywh(1, 11, 11, 1));

        builder.addLabel("Test1",                   cc.xy(  2, 13));
        builder.add(test1Label,                     cc.xywh(4, 13, 8, 1));
        builder.addLabel("Test2",                   cc.xy(  2, 15));
        builder.add(test2Label,                     cc.xywh(4, 15, 8, 1));
        builder.addLabel("Test3",                   cc.xy(  2, 17));
        builder.add(test3Label,                     cc.xywh(4, 17, 8, 1));

        return builder.getPanel();
    }

    // GridBag Alignment ****************************************************

    private JComponent buildGridBagAlignmentPanel() {
        JPanel panel = new HeightCheckPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.fill = GridBagConstraints.HORIZONTAL;

        // First row
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 0.0;
        gbc.anchor =
            Utilities.IS_WINDOWS
                ? GridBagConstraints.WEST
                : GridBagConstraints.EAST;
        gbc.insets = new Insets(0, 0, 0, Grid.HPAD2);
        panel.add(new JLabel("Align:"), gbc);

        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(0, 0, 0, Grid.HPAD1);
        panel.add(new JTextField("Align"), gbc);
        panel.add(createComboBox(true), gbc);
        panel.add(createComboBox(false), gbc);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        gbc.insets = new Insets(0, 0, 0, 0);
        panel.add(new JButton("Align"), gbc);

        panel.add(Box.createVerticalStrut(3), gbc);

        // Second row
        gbc.gridwidth = 1;
        gbc.weightx = 0.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 0, Grid.HPAD2);
        gbc.anchor =
            Utilities.IS_WINDOWS
                ? GridBagConstraints.WEST
                : GridBagConstraints.EAST;
        panel.add(new JLabel("Align:"), gbc);

        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(0, 0, 0, Grid.HPAD1);
        panel.add(new JTextField("Align"), gbc);
        panel.add(new JRadioButton("Align"), gbc);
        panel.add(new JCheckBox("Align"), gbc);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.gridheight = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        gbc.insets = new Insets(0, 0, 0, 0);
        panel.add(new JButton("Align"), gbc);

        return panel;
    }

    // Helper Code **********************************************************

    private JComboBox createComboBox(boolean editable) {
        JComboBox box =
            new JComboBox(new String[] { "Align", "Two", "Three", "Four" });
        box.setEditable(editable);
        return box;
    }


    // Helper Class *********************************************************
    
    private class HeightCheckPanel extends JPanel {

        private boolean checked = false;

        private HeightCheckPanel(LayoutManager lm) {
            super(lm);
        }

        public void paint(Graphics g) {
            if (!checked) {
                checkHeights();
            }
            super.paint(g);
        }

        private void checkHeights() {
            Icon passedIcon =
                ResourceManager.readImageIcon("images/passed.gif");
            Icon failedIcon =
                ResourceManager.readImageIcon("images/failed.gif");
            int textHeight = textfield.getHeight();
            int editableComboHeight = editableCombo.getHeight();
            int uneditableComboHeight = uneditableCombo.getHeight();
            int buttonHeight = button.getHeight();

            boolean passed1 = textHeight == editableComboHeight;
            test1Label.setIcon(passed1 ? passedIcon : failedIcon);
            test1Label.setText(
                (passed1 ? "Passed" : "Failed")
                    + ": text field and editable combo box have "
                    + (passed1 ? "equal" : "different")
                    + " heights.");

            boolean passed2 = editableComboHeight == uneditableComboHeight;
            test2Label.setIcon(passed2 ? passedIcon : failedIcon);
            test2Label.setText(
                (passed2 ? "Passed" : "Failed")
                    + ": editable and uneditable combo boxes have "
                    + (passed2 ? "equal" : "different")
                    + " heights.");

            boolean passed3 = (uneditableComboHeight - buttonHeight) % 2 == 0;
            test3Label.setIcon(passed3 ? passedIcon : failedIcon);
            test3Label.setText(
                (passed3 ? "Passed" : "Failed")
                    + ": uneditable combo box and button have an "
                    + (passed3 ? "even" : "odd")
                    + " height difference.");

            checked = true;
        }

    }

}