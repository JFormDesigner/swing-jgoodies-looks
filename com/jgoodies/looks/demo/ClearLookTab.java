/*
 * Copyright (c) 2003 JGoodies Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of JGoodies Karsten Lentzsch.
 * Use is subject to license terms.
 *
 */

package com.jgoodies.looks.demo;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.beans.PropertyVetoException;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/** 
 * Demos ClearLook removing or replacing obsolete decorations.
 * 
 * @author Karsten Lentzsch
 */
final class ClearLookTab {

    /**
     * Builds the panel.
     */
    JComponent build() {
        FormLayout fl = new FormLayout(
                "pref:grow, 11dlu, pref:grow, 11dlu, pref:grow",
                "fill:0:grow, 2dlu, pref, 11dlu, " + 
                "fill:0:grow, 2dlu, pref, 11dlu, " +
                "fill:0:grow, 2dlu, pref, 11dlu, " +
                "pref");
        fl.setColumnGroups(new int[][] { { 1, 3, 5 } });
        fl.setRowGroups(new int[][] { { 1, 5, 9 } });
        PanelBuilder builder = new PanelBuilder(fl);
        builder.setDefaultDialogBorder();
        CellConstraints cc = new CellConstraints();

        builder.add(buildScrollPaneInSplitPane(),         cc.xy(1, 1));
        builder.addLabel("ScrollPane in SplitPane",       cc.xy(1, 3, "c, c"));

        builder.add(buildScrollPaneInEtchedBorder(),      cc.xy(3, 1));
        builder.addLabel("ScrollPane in EtchedBorder",    cc.xy(3, 3, "c, c"));

        builder.add(buildScrollPaneInTabbedPane(),        cc.xy(5, 1));
        builder.addLabel("ScrollPane in TabbedPane",      cc.xy(5, 3, "c, c"));


        builder.add(buildTabbedPaneInSplitPane(),         cc.xy(1, 5));
        builder.addLabel("TabbedPane in SplitPane",       cc.xy(1, 7, "c, c"));

        builder.add(buildTabbedPaneInEtchedBorder(),      cc.xy(3, 5));
        builder.addLabel("TabbedPane in EtchedBorder",    cc.xy(3, 7, "c, c"));

        builder.add(buildTabbedPaneInInternalFrame(),     cc.xy(5, 5));
        builder.addLabel("TabbedPane in InternalFrame",   cc.xy(5, 7, "c, c"));


        builder.add(buildNestedSplitPanes(),              cc.xy(1,  9));
        builder.addLabel("Nested SplitPanes",             cc.xy(1, 11, "c, c"));

        builder.add(buildEtchedInEtchedBorder(),          cc.xy(3,  9));
        builder.addLabel("EtchedBorder in EtchedBorder",  cc.xy(3, 11, "c, c"));

        builder.add(buildCompoundBorderInBevelBorder(),   cc.xy(5,  9));
        builder.addLabel("CompoundBorder in BevelBorder", cc.xy(5, 11, "c, c"));


        builder.add(buildStatusBarWithBevelBorder(),    cc.xywh(1, 13, 5, 1));

        return builder.getPanel();
    }


    // ScrollPane Test Component Hierachies ***********************************
    
    private JComponent buildScrollPaneInSplitPane() {
        JPanel left  = new JPanel(new BorderLayout());
        left.add(new JLabel("Test"));
        JComponent textPanel = createTextPanel("Remove the scroll pane border", true);
        JScrollPane right = new JScrollPane(textPanel);
        right.setPreferredSize(new Dimension(40, 40));

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, left, right);
        splitPane.setDividerLocation(0.5d);
        return splitPane;
    }

    private JComponent buildScrollPaneInEtchedBorder() {
        JComponent textPanel = createTextPanel("Remove the scroll pane border", true);
        JScrollPane scrollPane = new JScrollPane(textPanel);
        scrollPane.setPreferredSize(new Dimension(40, 40));
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(scrollPane);
        panel.setBorder(new EtchedBorder());
        return panel;
    }

    private JComponent buildScrollPaneInTabbedPane() {
        JComponent textPanel = createTextPanel("Remove the inner border", true);
        JScrollPane scrollPane = new JScrollPane(textPanel);
        scrollPane.setPreferredSize(new Dimension(40, 40));
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.add(scrollPane, "General");
        return tabbedPane;
    }

    // TabbedPane Test Component Hierarchies **********************************
    
    private JComponent buildTabbedPaneInSplitPane() {
        JPanel left  = new JPanel(new BorderLayout());
        left.add(new JLabel("Test"));
        JTabbedPane right = new JTabbedPane();
        right.addTab("1", createTextPanel("Consider hiding the\ntab content border", false));
        right.addTab("2", new JLabel());
        right.addTab("3", new JLabel());
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, left, right);
        splitPane.setDividerLocation(0.5d);
        return splitPane;
    }

    private JComponent buildTabbedPaneInEtchedBorder() {
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("1", createTextPanel("Hide the content border", false));
        tabbedPane.addTab("2", new JLabel());
        tabbedPane.addTab("3", new JLabel());
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(tabbedPane);
        panel.setBorder(new EtchedBorder());
        return panel;
    }

    private JComponent buildTabbedPaneInInternalFrame() {
        JComponent textPanel = createTextPanel("Hide the content border", false);
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("1", textPanel);
        tabbedPane.addTab("2", new JLabel());
        tabbedPane.addTab("3", new JLabel());
        
        JDesktopPane desktop = new JDesktopPane();
        JInternalFrame frame = new JInternalFrame("Test", false, false, false, false);
        frame.setContentPane(tabbedPane);
        desktop.add(frame);
        frame.setBounds(0, 0, 50, 50);
        frame.setVisible(true);
        try {
            frame.setSelected(true);
            frame.setMaximum(true);
        } catch (PropertyVetoException e) {
        }
        return desktop;
    }

    // Misc Test Component Hierarchies ****************************************
    
    private JComponent buildNestedSplitPanes() {
        JPanel left  = new JPanel(new BorderLayout());
        left.add(new JLabel("Test"));
        JTextArea upperRight = new JTextArea("Upper right");
        JComponent lowerRight = createTextPanel("Remove the\nsplit pane border", true);

        JSplitPane right =
            new JSplitPane(JSplitPane.VERTICAL_SPLIT, upperRight, lowerRight);
        JSplitPane main =
            new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, left, right);
        return main;
    }

    private JComponent buildEtchedInEtchedBorder() {
        JComponent textPanel = createTextPanel("Remove the inner border", false);
        JPanel inner = new JPanel();
        inner.add(textPanel);
        inner.setBorder(new EtchedBorder());
        JPanel outer = new JPanel(new BorderLayout());
        outer.add(inner);
        outer.setBorder(new EtchedBorder());
        return outer;
    }

    private JComponent buildCompoundBorderInBevelBorder() {
        JComponent textPanel = createTextPanel("Remove the inner border", false);
        JPanel inner = new JPanel();
        inner.add(textPanel);
        inner.setBorder(new CompoundBorder(
                            new EtchedBorder(),
                            new EmptyBorder(0, 1, 1, 1)));
        JPanel outer = new JPanel(new BorderLayout());
        outer.add(inner);
        outer.setBorder(new EtchedBorder());
        return outer;
    }

    /**
     * Builds and answers the status bar, which demos a typical 
     * Swing misuse: it uses a <code>BevelBorder</code> to implement 
     * a status bar cell. ClearLook can correct this.
     */
    private JComponent buildStatusBarWithBevelBorder() {
        JLabel statusBar = new JLabel(" Status bar with BevelBorder ");
        statusBar.setBorder(new BevelBorder(BevelBorder.LOWERED));
        return statusBar;
    }
    
    // Helper Code ************************************************************
    
    private JComponent createTextPanel(String text, boolean editable) {
        JTextArea area = new JTextArea(text);
        area.setBorder(new EmptyBorder(15, 10, 0, 10));
        area.setEditable(editable);
        area.setOpaque(editable);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(area);
        return panel;
    }

}