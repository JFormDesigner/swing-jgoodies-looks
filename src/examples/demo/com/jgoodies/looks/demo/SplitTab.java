/*
 * Copyright (c) 2003 JGoodies Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of JGoodies Karsten Lentzsch.
 * Use is subject to license terms.
 *
 */

package com.jgoodies.looks.demo;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.*;
import javax.swing.plaf.SplitPaneUI;
import javax.swing.plaf.basic.BasicSplitPaneUI;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;

import com.jgoodies.forms.factories.Borders;
import com.jgoodies.plaf.LookUtils;

/** 
 * Contains nested split panels and demonstrates how ClearLook
 * removes obsolete decorations.
 * 
 * @author Karsten Lentzsch
 */
final class SplitTab {

    /**
     * Builds and answers the panel.
     */
    JComponent build() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(Borders.DIALOG_BORDER);
        panel.add(buildHorizontalSplit());
        return panel;
    }

    /**
     * Builds and answers the main horizontal split using stripped split panes.<p>
     * 
     * Nesting split panes often leads to duplicate borders. 
     * However, a look&feel should not remove borders completely
     * - unless he has good knowledge about the context: the surrounding 
     * components in the component tree and the border states.
     */
    private JComponent buildHorizontalSplit() {
        JComponent left = new JScrollPane(buildTree());
        left.setPreferredSize(new Dimension(200, 100));

        JComponent upperRight = new JScrollPane(buildTextArea());
        upperRight.setPreferredSize(new Dimension(100, 100));

        JComponent lowerRight = new JScrollPane(buildTable());
        lowerRight.setPreferredSize(new Dimension(100, 100));

        JSplitPane verticalSplit =createStrippedSplitPane(
                    JSplitPane.VERTICAL_SPLIT, 
                    upperRight, 
                    lowerRight);
        return createStrippedSplitPane(
            JSplitPane.HORIZONTAL_SPLIT,
            left,
            verticalSplit);
    }

    /**
     * Creates and returns a <code>JSplitPane</code> that has empty borders.
     * Useful to avoid duplicate decorations, for example if the split pane
     * is contained by other components that already provide a border.<p>
     * 
     * The divider border will likely be restored if the look&amp;feel changes.
     * Therefore the JGoodies UI framework utilizes the class
     * <code>BorderlessSplitPane</code> that removes the divider border
     * everytime the l&amp;f changes. In this application the split pane
     * will be rebuilt and so we're fine with this limited implementation.
     * 
     * @param orientation    the split pane's orientation: horizontal or vertical
     * @param comp1          the top/left component
     * @param comp2          the bottom/right component
     * @return a split panes that has an empty border
     */
    private JSplitPane createStrippedSplitPane(int orientation,
                                                     Component comp1, Component comp2) {
        JSplitPane split = new JSplitPane(orientation, comp1, comp2);
        split.setOneTouchExpandable(false);
        split.setBorder(BorderFactory.createEmptyBorder());
        SplitPaneUI ui = split.getUI();
        if (ui instanceof BasicSplitPaneUI) {
            BasicSplitPaneUI basicUI = (BasicSplitPaneUI) ui;
            basicUI.getDivider().setBorder(BorderFactory.createEmptyBorder());
        }
        return split;
    }

    /**
     * Builds and answers a sample tree.
     */
    private JTree buildTree() {
        JTree tree = new JTree(createSampleTreeModel());
        tree.expandRow(3);
        tree.expandRow(2);
        tree.expandRow(1);
        return tree;
    }

    /**
     * Builds and answers a sample text area.
     */
    private JTextArea buildTextArea() {
        JTextArea area = new JTextArea();
        area.setText(
            "May\nI\nKindly\nRemind you that a\nMargin\nImproves a text's readability.");
        return area;
    }

    /**
     * Builds and answers a sample table.
     */
    private JTable buildTable() {
        JTable table =
            new JTable(
                createSampleTableData(),
                new String[] { "Artist", "Title      " });

        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.getColumnModel().getColumn(0).setPreferredWidth(100);
        table.getColumnModel().getColumn(1).setPreferredWidth(300);
        table.setRowSelectionInterval(2, 2);
        int tableFontSize    = UIManager.getFont("Table.font").getSize();
        int minimumRowHeight = tableFontSize + 6;
        int defaultRowHeight = LookUtils.isLowRes ? 17 : 18;
        table.setRowHeight(Math.max(minimumRowHeight, defaultRowHeight));
        return table;
    }

    /**
     * Creates and answers a sample tree model.
     */
    private TreeModel createSampleTreeModel() {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Musicians");
        DefaultMutableTreeNode parent;

        //
        parent = new DefaultMutableTreeNode("Drums");
        root.add(parent);
        parent.add(new DefaultMutableTreeNode("Elvin Jones"));
        parent.add(new DefaultMutableTreeNode("Jack DeJohnette"));
        parent.add(new DefaultMutableTreeNode("Rashied Ali"));

        //
        parent = new DefaultMutableTreeNode("Piano");
        root.add(parent);
        parent.add(new DefaultMutableTreeNode("Alexander von Schlippenbach"));
        parent.add(new DefaultMutableTreeNode("McCoy Tyner"));
        parent.add(new DefaultMutableTreeNode("Sun Ra"));

        parent = new DefaultMutableTreeNode("Saxophon");
        root.add(parent);
        parent.add(new DefaultMutableTreeNode("Albert Ayler"));
        parent.add(new DefaultMutableTreeNode("Archie Shepp"));
        parent.add(new DefaultMutableTreeNode("Charlie Parker"));
        parent.add(new DefaultMutableTreeNode("John Coltrane"));
        parent.add(new DefaultMutableTreeNode("Ornette Coleman"));
        parent.add(new DefaultMutableTreeNode("Pharoa Sanders"));
        parent.add(new DefaultMutableTreeNode("Sonny Rollins"));

        return new DefaultTreeModel(root);
    }

    /**
     * Creates and answers sample table data.
     */
    private String[][] createSampleTableData() {
        return new String[][] { 
            { "Albert Ayler",   "Greenwich Village"         }, 
            { "Carla Bley",     "Escalator Over the Hill"   }, 
            { "Frank Zappa",    "Yo' Mama"                  },
            { "John Coltrane",  "Ascension"                 }, 
            { "Miles Davis",    "In a Silent Way"           }, 
            { "Pharoa Sanders", "Karma"                     }, 
            { "Wayne Shorter",  "Juju"                      },
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