package com.jgoodies.swing.plaf;

/*
 * Copyright (c) 2002 JGoodies Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of JGoodies Karsten Lentzsch.
 * Use is subject to license terms.
 *
 */

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;

import com.jgoodies.forms.factories.Borders;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.layout.Grid;
import com.jgoodies.plaf.Options;
import com.jgoodies.swing.ExtTable;
import com.jgoodies.swing.ExtTree;
import com.jgoodies.swing.panels.CardPanel;
import com.jgoodies.swing.util.ThinBevelBorder;

/**
 * A panel that provides a preview for a look using a bunch of sample widgets.
 *
 * @author Karsten Lentzsch
 */

final class LookAndFeelPreviewPanel extends CardPanel {

    private static final Dimension PREFERRED_SIZE = new Dimension(300, 170);
    private static final String    PREVIEW_CARD   = "preview";
    private static final String    FAKE_CARD      = "fake";

    private final JPanel delegate;
    private Image buffer;
    
    
    // Instance Creation ****************************************************

    /**
     * Constructs a <code>LookAndFeelPreviewPanel</code>.
     */
    LookAndFeelPreviewPanel() {
        delegate = buildSampleWidgetsPanel();
        add(delegate, PREVIEW_CARD);
        add(new JLabel("Hallo"), FAKE_CARD);
    }


    // Updating *************************************************************

    /**
     * Updates the UI delegates of all sample widgets.
     */
    void updateComponentTree() {
        SwingUtilities.updateComponentTreeUI(delegate);
    }

    /**
     * Updates and repaints the UI delegates of all sample widgets.
     */
    void updateAndValidate() {
        updateComponentTree();
        ensureHasBuffer();
        updateBuffer();
    }

    public void paint(Graphics g) {
        ensureHasBuffer();
        g.drawImage(buffer, 0, 0, null);
    }

    /**
     * Ensures that a background image buffer has been created.
     */
    private void ensureHasBuffer() {
        if (buffer != null)
            return;
        buffer = createImage(getWidth(), getHeight());
        updateBuffer();
    }

    /**
     * Updates the background image buffer. Therefore switches to
     * the preview card, paints to the buffer, and finally switches back 
     * to the fake card.
     */
    private void updateBuffer() {
        validate();
        showCard(PREVIEW_CARD);

        int w = buffer.getWidth(null);
        int h = buffer.getHeight(null);
        Graphics g = buffer.getGraphics();
        g.setClip(0, 0, w, h);
        super.paint(g);
        g.dispose();

        showCard(FAKE_CARD);
    }

    // Building the Sample Widgets ******************************************************

    /**
     * Builds a panel full of sample widgets.
     */
    private JPanel buildSampleWidgetsPanel() {
        JComponent textfield = new JTextField("Margin, Baseline, Space");

        FormLayout fl = new FormLayout(
                "0:grow, 7dlu, 0:grow, 7dlu, 0:grow",
                "fill:min:grow, 7dlu, pref");
        fl.setColumnGroups(new int[][] { { 1, 3, 5 } });
        JPanel panel = new JPanel(fl);
        panel.setBorder(BorderFactory.createCompoundBorder(
                            new ThinBevelBorder(ThinBevelBorder.LOWERED),
                            Borders.DIALOG_BORDER));
        panel.setPreferredSize(PREFERRED_SIZE);

        CellConstraints cc = new CellConstraints();
        panel.add(buildTree(),       cc.xy(1, 1));
        panel.add(buildTable(),      cc.xy(3, 1));
        panel.add(buildTabbedPane(), cc.xy(5, 1));

        panel.add(textfield, cc.xy(1, 3));
        panel.add(buildComboBoxes(), cc.xy(3, 3));
        panel.add(buildButtons(),    cc.xy(5, 3));

        return panel;
    }

    /**
     * Builds and answers a sample tree.
     */
    private JComponent buildTree() {
        JTree tree = new ExtTree(createPreviewTreeModel());
        tree.expandRow(2);
        JComponent result = new JScrollPane(tree);
        return result;
    }

    /**
     * Builds and answers a sample table.
     */
    private JComponent buildTable() {
        JTable table =
            new ExtTable(new String[][] { { "Ayler",   "Greenwich Village" }, {
                                            "Coltrane", "Ascension" }, {
                                            "Davis",    "In a Silent Way" }, {
                                            "Sanders",  "Karma" }, {
                                            "Shorter",  "Juju" },
                    },
                    new String[] { "Artist", "Title      " });

        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.getColumnModel().getColumn(0).setPreferredWidth(55);
        table.getColumnModel().getColumn(1).setPreferredWidth(110);
        table.setRowSelectionInterval(2, 2);
        JScrollPane scrollPane =
            new JScrollPane(
                table,
                JScrollPane.VERTICAL_SCROLLBAR_NEVER,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        return scrollPane;
    }

    /**
     * Builds and answers the sample tabbed pane.
     */
    private JComponent buildTabbedPane() {
        final int REDUCED_HEIGHT = 50;

        JRadioButton radio = new JRadioButton("Radio", true);
        JCheckBox check = new JCheckBox("Check", true);
        JLabel label = new JLabel("Label");
        JProgressBar bar = new JProgressBar(JProgressBar.VERTICAL, 0, 100) {
            public Dimension getPreferredSize() {
                return new Dimension(
                    super.getPreferredSize().width,
                    REDUCED_HEIGHT);
            }
        };
        bar.setValue(50);
        JSlider slider = new JSlider(JSlider.VERTICAL) {
            public Dimension getPreferredSize() {
                Dimension size =
                    new Dimension(
                        super.getPreferredSize().width,
                        REDUCED_HEIGHT);
                //System.out.println("pref size=" + size);				
                return size;
            }
        };

        FormLayout fl = new FormLayout(
                "pref:grow, 4dlu, pref, 4dlu, min",
                "pref, pref, 2dlu, top:pref:grow");
        JPanel panel = new JPanel(fl);
        CellConstraints cc = new CellConstraints();

        panel.add(radio,        cc.xy(1, 1));
        panel.add(check,        cc.xy(1, 2));
        panel.add(label,        cc.xy(1, 4));
        panel.add(bar,          cc.xywh(3, 1, 1, 4));
        panel.add(slider,       cc.xywh(5, 1, 1, 4));

        panel.setBorder(BorderFactory.createEmptyBorder(4, 10, 4, 10));

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.add("Tab1", panel);
        tabbedPane.add("Tab2", new JLabel("Hidden"));
        return tabbedPane;
    }

    /**
     * Builds and answers a panel with two combo boxes: one is editable the other is not.
     */
    private JComponent buildComboBoxes() {
        JComboBox combobox1 = new JComboBox(new String[] { "Margin", "Two", "Three" });
        combobox1.setEditable(true);
        JComboBox combobox2 = new JComboBox(new String[] { "Height", "Three", "Four" });

        JPanel panel = new JPanel(new GridLayout(1, 2, Grid.HPAD1, 0));
        panel.add(combobox1);
        panel.add(combobox2);
        return panel;
    }

    /**
     * Builds and answers a panel that contains two buttons: one is wide, the
     * other has the JGoodies Look narrow property set.
     */
    private JComponent buildButtons() {
        JButton wide = new JButton("Wide");
        wide.setMnemonic('W');
        JButton narrow = new JButton("Narrow");
        narrow.putClientProperty(Options.IS_NARROW_KEY, Boolean.TRUE);
        JPanel panel = new JPanel(new BorderLayout(Grid.HPAD1, 0));
        panel.add(wide, BorderLayout.CENTER);
        panel.add(narrow, BorderLayout.EAST);
        return panel;
    }

    /**
     * Creates and answers a sample tree model.
     */
    private TreeModel createPreviewTreeModel() {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Musicians");
        DefaultMutableTreeNode parent;

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
        parent.add(new DefaultMutableTreeNode("Sonny Rollins"));

        return new DefaultTreeModel(root);
    }

}