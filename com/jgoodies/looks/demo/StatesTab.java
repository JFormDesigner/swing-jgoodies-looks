/*
 * Copyright (c) 2003 JGoodies Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of JGoodies Karsten Lentzsch.
 * Use is subject to license terms.
 *
 */

package com.jgoodies.looks.demo;

import java.awt.Component;

import javax.swing.*;
import javax.swing.text.JTextComponent;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.extras.DefaultFormBuilder;
import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.Sizes;
import com.jgoodies.util.Utilities;

/** 
 * Contains a bunch of buttons with in different states and configurations.
 * 
 * @author Karsten Lentzsch
 */
final class StatesTab {

    /**
     * Builds the panel.
     */
    JComponent build() {
        FormLayout layout = new FormLayout(
                "right:max(50dlu;pref), 6dlu, pref",
                "");
        DefaultFormBuilder builder = new DefaultFormBuilder(layout);
        builder.setRowGroupingEnabled(true);
        builder.setDefaultDialogBorder();

        // Buttons	
        builder.append("Standard:",      buildButtonRow(true, true));
        builder.append("No Content:",    buildButtonRow(true, false));
        builder.append("No Border:",     buildButtonRow(false, true));
        builder.append("Radio Button:",  buildRadioButtonRow());
        builder.append("Check Box:",     buildCheckBoxRow());
        builder.append("Combo Box:",     buildComboBoxRow());
        builder.append("Text Field:",    buildTextRow(JTextField.class, false));
        if (!Utilities.IS_BEFORE_14) {
            builder.append("Formatted Field:",   buildTextRow(getFormattedTextFieldClass(), false));
        }
        builder.append("Password:",      buildTextRow(JPasswordField.class, false));
        builder.append("Text Area:",     buildTextRow(JTextArea.class, true));
        if (!Utilities.IS_BEFORE_14) {
            builder.append("Spinner:",   buildSpinnerRow());
        }
        return builder.getPanel();
    }


    // Button Rows **********************************************************

    private JComponent buildButtonRow(
        boolean borderPainted,
        boolean contentAreaFilled) {
        JButton button = new JButton("Standard");
        button.setDefaultCapable(true);

        return buildButtonRow(
            new AbstractButton[] {
                button,
                new JToggleButton("Selected"),
                new JButton("Disabled"),
                new JToggleButton("Selected")},
            borderPainted,
            contentAreaFilled);
    }

    private JComponent buildCheckBoxRow() {
        return buildButtonRow(
            new AbstractButton[] {
                new JCheckBox("Deselected"),
                new JCheckBox("Selected"),
                new JCheckBox("Disabled"),
                new JCheckBox("Selected")},
            false,
            false);
    }

    private JComponent buildRadioButtonRow() {
        return buildButtonRow(
            new AbstractButton[] {
                new JRadioButton("Deselected"),
                new JRadioButton("Selected"),
                new JRadioButton("Disabled"),
                new JRadioButton("Selected")},
            false,
            false);
    }

    private JComponent buildButtonRow(
        AbstractButton[] buttons,
        boolean borderPainted,
        boolean contentAreaFilled) {
        buttons[1].setSelected(true);
        buttons[2].setEnabled(false);
        buttons[3].setEnabled(false);
        buttons[3].setSelected(true);
        for (int i = 0; i < buttons.length; i++) {
            buttons[i].setBorderPainted(borderPainted);
            buttons[i].setContentAreaFilled(contentAreaFilled);
        }

        return buildGrid(buttons[0],
                          buttons[1],
                          buttons[2],
                          buttons[3],
                          FormFactory.BUTTON_COLSPEC);
    }

    // Text Rows ************************************************************

    /**
     * Creates and answers a bar with 4 text components.
     * These are created using the given class;
     * they are wrapped in a <code>JScrollpane</code> iff the
     * wrap flag is set. 
     */
    private JComponent buildTextRow(Class textComponentClass, boolean wrap) {
        JTextComponent[] components = new JTextComponent[4];
        for (int i = 0; i < 4; i++) {
            try {
                components[i] =
                    (JTextComponent) textComponentClass.newInstance();
            } catch (InstantiationException e) {
            } catch (IllegalAccessException e) {
            }
        }
        components[0].setText("Standard");

        components[1].setText("Uneditable");
        components[1].setEditable(false);

        components[2].setText("Disabled");
        components[2].setEnabled(false);

        components[3].setText("Uneditable");
        components[3].setEditable(false);
        components[3].setEnabled(false);

        return wrap
            ? buildGrid(
                wrapWithScrollPane(components[0]),
                wrapWithScrollPane(components[1]),
                wrapWithScrollPane(components[2]),
                wrapWithScrollPane(components[3]))
            : buildGrid(
                components[0],
                components[1],
                components[2],
                components[3]);
    }
    
    private Component wrapWithScrollPane(Component c) {
        return new JScrollPane(
            c,
            JScrollPane.VERTICAL_SCROLLBAR_NEVER,
            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    }

    // Misc *****************************************************************

    private JComponent buildComboBoxRow() {
        return buildGrid(
            createComboBox("Standard", true, false),
            createComboBox("Editable", true, true),
            createComboBox("Disabled", false, false),
            createComboBox("Editable", false, true));
    }

    private JComboBox createComboBox(
        String text,
        boolean enabled,
        boolean editable) {
        JComboBox box =
            new JComboBox(new String[] { text, "Two", "Three", "Four" });
        box.setEnabled(enabled);
        box.setEditable(editable);
        box.setRenderer(new CustomComboBoxRenderer());
        //Dimension prefSize = box.getPreferredSize();
        //box.setPreferredSize(new Dimension(80, prefSize.height));
        return box;
    }

    private Class getFormattedTextFieldClass() {
        try {
            return Class.forName("javax.swing.JFormattedTextField");
        } catch (Exception e) {
            return null;
        }
    }

    private JComponent buildSpinnerRow() {
        return buildGrid(createSpinner(true),
                         new JPanel(),
                         createSpinner(false),
                         new JPanel());
    }

    private JComponent createSpinner(boolean enabled) {
        JComponent spinner = new JPanel();
        try {
            Class clazz = Class.forName("javax.swing.JSpinner");
            spinner = (JComponent) clazz.newInstance();
        } catch (Exception e) {
            System.out.println(e);
        }

        spinner.setEnabled(enabled);
        return spinner;
    }


    // Custom ComboBox Editor ***********************************************

    private static class CustomComboBoxRenderer implements ListCellRenderer {

        private final JLabel label = new JLabel();

        public Component getListCellRendererComponent(
            JList list,
            Object value,
            int index,
            boolean isSelected,
            boolean cellHasFocus) {
            if (isSelected) {
                label.setBackground(list.getSelectionBackground());
                label.setForeground(list.getSelectionForeground());
            } else {
                label.setBackground(list.getBackground());
                label.setForeground(list.getForeground());
            }

            label.setFont(list.getFont());

            if (value instanceof Icon) {
                label.setIcon((Icon) value);
            } else {
                label.setText((value == null) ? "" : value.toString());
            }
            label.setOpaque(true);
            return label;
        }

    }
    
    
    // Helper Code **********************************************************

    private JComponent buildGrid(
                Component c1, 
                Component c2, 
                Component c3, 
                Component c4) {
         return buildGrid(c1, c2, c3, c4, 
                            new ColumnSpec(ColumnSpec.DEFAULT,
                                            Sizes.dluX(20),
                                            ColumnSpec.DEFAULT_GROW));
    }

    private JComponent buildGrid(
                Component c1, 
                Component c2, 
                Component c3, 
                Component c4,
                ColumnSpec colSpec) {
        FormLayout layout = new FormLayout("", "pref");
        for (int i = 0; i < 4; i++) {
            layout.appendColumn(colSpec);
            layout.appendColumn(FormFactory.RELATED_GAP_COLSPEC);
        }
        PanelBuilder builder = new PanelBuilder(layout);
        CellConstraints cc = new CellConstraints();
        builder.add(c1, cc.xy(1, 1));
        builder.add(c2, cc.xy(3, 1));
        builder.add(c3, cc.xy(5, 1));
        builder.add(c4, cc.xy(7, 1));
        return builder.getPanel();
    }

}