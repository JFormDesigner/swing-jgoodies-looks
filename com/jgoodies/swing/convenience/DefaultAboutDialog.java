package com.jgoodies.swing.convenience;

/*
 * Copyright (c) 2002 JGoodies Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of JGoodies Karsten Lentzsch.
 * Use is subject to license terms.
 *
 */

import java.awt.BorderLayout;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import com.jgoodies.forms.factories.Borders;
import com.jgoodies.forms.layout.Sizes;
import com.jgoodies.layout.Resizer;
import com.jgoodies.swing.AbstractDialog;
import com.jgoodies.swing.application.ResourceIDs;
import com.jgoodies.swing.application.ResourceManager;
import com.jgoodies.swing.application.Workbench;
import com.jgoodies.swing.panels.HeaderPanel;

/**
 * Provides the layout and actions for a generic about dialog. 
 * 
 * @see	DefaultAboutToolPanel
 *
 * @author Karsten Lentzsch
 */

public class DefaultAboutDialog extends AbstractDialog {

    private final JComponent toolTab;

    /**
     * Constructs a default about dialog using the given owner.
     */
    public DefaultAboutDialog(JFrame owner) {
        this(owner, createDefaultToolTab());
    }

    /**
     * Constructs a default about dialog using the given owner and tool tab.
     */
    public DefaultAboutDialog(JFrame owner, JComponent toolTab) {
        super(owner);
        this.toolTab = toolTab;
    }

    /**
     * Creates and answers the default tool tab.
     */
    private static JComponent createDefaultToolTab() {
        DefaultAboutToolPanel panel = new DefaultAboutToolPanel();
        panel.build();
        return panel;
    }

    /**
     * Builds and answers the dialog's content.
     */
    protected JComponent buildContent() {
        JPanel content = new JPanel(new BorderLayout());
        content.add(buildTabbedPane(),         BorderLayout.CENTER);
        content.add(buildButtonBarWithClose(), BorderLayout.SOUTH);
        return content;
    }

    /**
     * Builds and answers the dialog's header.
     */
    protected JComponent buildHeader() {
        return new HeaderPanel(
            "About " + Workbench.getGlobals().getProductName(),
            "You can click on the tabs below to see information\n"
                + "about the tool's version and the system's state.",
            ResourceManager.getIcon(ResourceIDs.ABOUT_ICON));
    }

    /**
     * Builds and answers the dialog's tabbed pane.
     */
    protected JComponent buildTabbedPane() {
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.add("Tool",   toolTab);
        tabbedPane.add("System", buildSystemTab());
        return tabbedPane;
    }

    /**
     * Resizes the given component to give it a quadratic aspect ratio.
     */
    protected void resizeHook(JComponent component) {
        Resizer.ONE2ONE.resize(component);
    }

    /**
     * Builds and answers the dialog's system tab.
     */
    protected JComponent buildSystemTab() {
        JPanel systemTable = new SystemTable();
        int width = Sizes.dialogUnitXAsPixel(100, systemTable);
        systemTable.setPreferredSize(Resizer.DEFAULT.fromWidth(width));
        systemTable.setBorder(Borders.DIALOG_BORDER);
        return systemTable;
    }

}