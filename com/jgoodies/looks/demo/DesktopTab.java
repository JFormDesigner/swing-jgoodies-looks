package com.jgoodies.looks.demo;

/*
 * Copyright (c) 2003 JGoodies Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of JGoodies Karsten Lentzsch.
 * Use is subject to license terms.
 *
 */

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.*;

import com.jgoodies.forms.factories.Borders;
import com.jgoodies.layout.Grid;
import com.jgoodies.plaf.LookUtils;
import com.jgoodies.plaf.plastic.PlasticInternalFrameUI;

/** 
 * Demos the <code>JDesktopPane</code>.
 * 
 * @author Karsten Lentzsch
 */
final class DesktopTab {
    
    private static final float SIZE_FACTOR = LookUtils.isLowRes ? 0.96f : 1.2f;

    /**
     * Builds the panel.
     */
    JComponent build() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(Borders.DIALOG_BORDER);
        panel.add(new JScrollPane(buildDesktopPane()));
        return panel;
    }

    private JComponent buildDesktopPane() {
        int gap      = (int) (40 * SIZE_FACTOR);
        int originX1 = 10;
        int extentX1 = (int) (166 * SIZE_FACTOR);
        int originX2 = originX1 + extentX1 + gap;
        int extentX2 = extentX1;
        int originX3 = originX2 + extentX2 + gap;
        int extentX3 = (int) (116 * SIZE_FACTOR);
        
        JDesktopPane desktop = new JDesktopPane();
        JInternalFrame frame;

        frame = new JInternalFrame("Navigation", true, true, true, true);
        frame.setContentPane(buildFrame1ContentPane());
        frame.setBounds(originX1, 10, extentX1, 280);
        desktop.add(frame);
        frame.setVisible(true);

        frame = new JInternalFrame("Properties", true, false, true, true);
        frame.setContentPane(buildFrame2ContentPane());
        frame.setBounds(originX2, 10, extentX2, 280);
        desktop.add(frame);
        frame.setVisible(true);

        JInternalFrame palette =
            new JInternalFrame("Palette1", true, true, true, true);
        palette.putClientProperty(
            PlasticInternalFrameUI.IS_PALETTE,
            Boolean.TRUE);
        palette.setContentPane(buildPaletteContentPane());
        palette.setBounds(originX3, 10, extentX3, 130);
        palette.setVisible(true);
        desktop.add(palette, JLayeredPane.PALETTE_LAYER);

        palette = new JInternalFrame("Palette2", true, true, true, true);
        palette.putClientProperty(
            PlasticInternalFrameUI.IS_PALETTE,
            Boolean.TRUE);
        palette.setContentPane(buildBackgroundTestContentPane());
        palette.setBounds(originX3, 160, extentX3, 130);
        palette.setVisible(true);
        desktop.add(palette, JLayeredPane.PALETTE_LAYER);

        return desktop;
    }

    private JComponent buildFrame1ContentPane() {
        JScrollPane scrollPane = new JScrollPane(new JTree());
        scrollPane.setPreferredSize(new Dimension(100, 140));
        return scrollPane;
    }

    private JComponent buildFrame2ContentPane() {
        JScrollPane scrollPane = new JScrollPane(buildTable());
        scrollPane.setPreferredSize(new Dimension(100, 140));
        return scrollPane;
    }

    private JComponent buildPaletteContentPane() {
        Box box = Box.createVerticalBox();
        box.add(new JCheckBox("be consistent", true));
        box.add(Box.createVerticalStrut(Grid.VPAD1));
        box.add(new JCheckBox("use less ink", true));

        JPanel generalTab = new JPanel(new BorderLayout());
        generalTab.add(box);
        generalTab.setBorder(Grid.CARD_DIALOG_BORDER);

        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.BOTTOM);
        tabbedPane.add(generalTab, "General");
        tabbedPane.add(new JLabel("Test1"), "Filter");
        return tabbedPane;
    }

    private JComponent buildBackgroundTestContentPane() {
        JTextArea area1 =
            new JTextArea("Background should be\nthe same as below.");
        area1.setBackground(UIManager.getColor("control"));
        JTextArea area2 =
            new JTextArea("Background should be\nthe same as above.");
        area2.setOpaque(false);
        JPanel grid = new JPanel(new GridLayout(2, 1));
        grid.add(area1);
        grid.add(area2);
        grid.setOpaque(false);
        return grid;
    }

    /**
     * Builds and answers a sample table.
     */
    private JTable buildTable() {
        JTable table = new JTable(
                createSampleTableData(),
                new String[] { "Key", "Value" });

        //table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.getColumnModel().getColumn(0).setPreferredWidth(95);
        table.getColumnModel().getColumn(1).setPreferredWidth(95);
        table.setRowSelectionInterval(2, 2);
        return table;
    }

    /**
     * Creates and answers sample table data.
     */
    private String[][] createSampleTableData() {
        return new String[][] { 
            { "Name",           "Karsten"    }, 
            { "Sex",            "Male"       }, 
            { "Date of Birth",  "5-Dec-1967" }, 
            { "Place of Birth", "Kiel"       }, 
            { "Profession",     "UI Designer"}, 
            { "Business",       "Freelancer" },
            { "",               ""           },
            { "",               ""           },
            { "",               ""           },
            { "",               ""           },
            { "",               ""           },
            { "",               ""           },
            { "",               ""           },
            { "",               ""           },
            { "",               ""           },
        };
    }

}