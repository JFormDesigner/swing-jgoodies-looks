/*
 * Copyright (c) 2001-2004 JGoodies Karsten Lentzsch. All Rights Reserved.
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
import java.awt.Dimension;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.factories.Borders;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.plaf.Options;
import com.jgoodies.uif_lite.component.Factory;
import com.jgoodies.uif_lite.panel.SimpleInternalFrame;

/** 
 * Demonstrates optionals settings for the JGoodies
 * tabbed panes using two <code>SimpleInternalFrame</code>.
 * 
 * @author Karsten Lentzsch
 * @version $Revision: 1.3 $
 */
final class TabTestTab {

    /**
     * Builds and returns the panel.
     */
    JComponent build() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(Borders.DIALOG_BORDER);
        panel.add(buildHorizontalSplit());
        return panel;
    }
    

    /**
     * Builds and returns the horizontal split using stripped split panes.<p>
     * 
     * Nesting split panes often leads to duplicate borders. 
     * However, a look&feel should not remove borders completely
     * - unless he has good knowledge about the context: the surrounding 
     * components in the component tree and the border states.
     */
    private JComponent buildHorizontalSplit() {
        return Factory.createStrippedSplitPane(
            JSplitPane.HORIZONTAL_SPLIT,
            buildNavigationPanel(),
            buildEditor(),
            0.2f);
    }
    
    
    /**
     * Builds and returns a panel that uses a tabbed pane with embedded tabs
     * enabled.
     */
    private JComponent buildNavigationPanel() {
        JTabbedPane tabbedPane = new JTabbedPane(SwingConstants.BOTTOM);
        tabbedPane.putClientProperty(Options.EMBEDDED_TABS_KEY, Boolean.TRUE);
        tabbedPane.addTab("Tree", Factory.createStrippedScrollPane(buildTree()));
        tabbedPane.addTab("Help", Factory.createStrippedScrollPane(buildHelp()));
        
        SimpleInternalFrame sif = new SimpleInternalFrame("Embedded Tabs");
        sif.setPreferredSize(new Dimension(150, 100));
        sif.add(tabbedPane);
        return sif;
    }
    

    /**
     * Builds and returns a sample tree.
     */
    private JTree buildTree() {
        JTree tree = new JTree(createSampleTreeModel());
        tree.putClientProperty(Options.TREE_LINE_STYLE_KEY, 
                               Options.TREE_LINE_STYLE_NONE_VALUE);
        tree.expandRow(3);
        tree.expandRow(2);
        tree.expandRow(1);
        return tree;
    }
    
    
    private JComponent buildHelp() {
        JTextArea area = new JTextArea("\n This tabbed pane uses\n embedded tabs.");
        return area;
    }
    

    /**
     * Builds and returns a tabbed pane with the no-content-border enabled.
     */
    private JComponent buildEditor() {
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.putClientProperty(Options.NO_CONTENT_BORDER_KEY, Boolean.TRUE);
        tabbedPane.addTab("Overview",  buildOverviewTab());
        tabbedPane.addTab("Addresses", buildAddressesTab());

        SimpleInternalFrame sif = new SimpleInternalFrame("Tabbed Pane without Content Border");
        sif.setPreferredSize(new Dimension(300, 100));
        sif.add(tabbedPane);
        return sif;
    }
    
    
    /**
     * Builds and returns the editor's overview tab.
     */
    private JComponent buildOverviewTab() {
        FormLayout layout = new FormLayout(
                "right:max(50dlu;pref), 4dlu, max(35dlu;min), 2dlu, min, 2dlu, min, 2dlu, min, ",
                "p, 3dlu, p, 3dlu, p, 3dlu, p, 3dlu, p, 3dlu, p, 3dlu, p, 3dlu, p, 3dlu, p");
        layout.setColumnGroups(new int[][] { { 3, 5, 7, 9 } });

        PanelBuilder builder = new PanelBuilder(layout);
            
        builder.setDefaultDialogBorder();
        CellConstraints cc = new CellConstraints();

        builder.addLabel("File number:",        cc.xy (1,  1));
        builder.add(new JTextField(),           cc.xyw(3,  1, 7));
        builder.addLabel("RFQ number:",         cc.xy (1,  3));
        builder.add(new JTextField(),           cc.xyw(3,  3, 7));
        builder.addLabel("Entry date:",         cc.xy (1,  5));
        builder.add(new JTextField(),           cc.xy (3,  5));
        builder.addLabel("Sales Person:",       cc.xy (1,  7));
        builder.add(new JTextField(),           cc.xyw(3,  7, 7));
        builder.addLabel("BL/MBL number:",      cc.xy (1,  9));
        builder.add(new JTextField(),           cc.xy (3,  9));
        builder.add(new JTextField(),           cc.xy (5,  9));
        builder.addLabel("Shipper:",            cc.xy (1, 11));
        builder.add(new JTextField(),           cc.xy (3, 11));
        builder.add(new JTextField(),           cc.xyw(5, 11, 5));
        builder.addLabel("Consignee:",          cc.xy (1, 13));
        builder.add(new JTextField(),           cc.xy (3, 13));
        builder.add(new JTextField(),           cc.xyw(5, 13, 5));
        builder.addLabel("Port of loading:",    cc.xy (1, 15));
        builder.add(new JTextField(),           cc.xy (3, 15));
        builder.add(new JTextField(),           cc.xyw(5, 15, 5));
        builder.addLabel("Final destination:",  cc.xy (1, 17));
        builder.add(new JTextField(),           cc.xy (3, 17));
        builder.add(new JTextField(),           cc.xyw(5, 17, 5));
        
        return builder.getPanel();
    }
    
    
    /**
     * Builds and returns the addresses tab.
     */
    private JComponent buildAddressesTab() {
        FormLayout layout = new FormLayout(
                "12dlu, max(85dlu;default), 25dlu, max(85dlu;default)",
                "p, 3dlu, p, 3dlu, p, 3dlu, p, 3dlu, p, 3dlu, p, 21dlu, "
              + "p, 3dlu, p, 3dlu, p, 3dlu, p, 3dlu, p, 3dlu, p");

        PanelBuilder builder = new PanelBuilder(layout);
        builder.setDefaultDialogBorder();
        CellConstraints cc = new CellConstraints();

        builder.addTitle("Customer",        cc.xy(2,  1));
        builder.add(new JTextField(),       cc.xy(2,  3));
        builder.add(new JTextField(),       cc.xy(2,  5));
        builder.add(new JTextField(),       cc.xy(2,  7));
        builder.add(new JTextField(),       cc.xy(2,  9));

        builder.addTitle("Shipper",         cc.xy(4,  1));
        builder.add(new JTextField(),       cc.xy(4,  3));
        builder.add(new JTextField(),       cc.xy(4,  5));
        builder.add(new JTextField(),       cc.xy(4,  7));
        builder.add(new JTextField(),       cc.xy(4,  9));
        
        builder.addTitle("Consignee",       cc.xy(2, 13));
        builder.add(new JTextField(),       cc.xy(2, 15));
        builder.add(new JTextField(),       cc.xy(2, 17));
        builder.add(new JTextField(),       cc.xy(2, 19));
        builder.add(new JTextField(),       cc.xy(2, 21));

        builder.addTitle("Notify",          cc.xy(4,  13));
        builder.add(new JTextField(),       cc.xy(4,  15));
        builder.add(new JTextField(),       cc.xy(4,  17));
        builder.add(new JTextField(),       cc.xy(4,  19));
        builder.add(new JTextField(),       cc.xy(4,  21));

        return builder.getPanel();
    }
    

    /**
     * Creates and returns a sample tree model.
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


}