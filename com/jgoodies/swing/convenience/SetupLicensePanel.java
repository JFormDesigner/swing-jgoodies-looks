package com.jgoodies.swing.convenience;

/*
 * Copyright (c) 2002 JGoodies Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of JGoodies Karsten Lentzsch.
 * Use is subject to license terms.
 *
 */

import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;

import javax.swing.*;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.factories.Borders;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.swing.application.ResourceIDs;
import com.jgoodies.swing.application.ResourceManager;
import com.jgoodies.swing.panels.HeaderPanel;
import com.jgoodies.swing.util.UIFactory;

/**
 * Builds the license acceptance page of the default setup dialog.
 *
 * @author Karsten Lentzsch
 */

public final class SetupLicensePanel extends JPanel {

    private JEditorPane licensePane;
    private JRadioButton acceptRadio;
    private JRadioButton declineRadio;


    // Instance Creation ****************************************************
    
    /**
     * Constructs the license acceptance panel.
     */
    public SetupLicensePanel() {
        initComponents();
    }


    // Accessing State ******************************************************

    /**
     * Sets a new license page.
     */
    public void setPage(URL url) throws IOException {
        licensePane.setPage(url);
    }

    public boolean licenseAccepted() {
        return acceptRadio.isSelected();
    }
    
    public boolean licenseDeclined() {
        return declineRadio.isSelected();
    }


    // Building *************************************************************

    /**
     * Creates and configures the UI components.
     */
    private void initComponents() {
        licensePane = UIFactory.createHTMLPane(false, true);
        licensePane.setBackground(UIFactory.getLightBackground());
        acceptRadio =
            new JRadioButton("I accept the terms in the license agreement");
        declineRadio =
            new JRadioButton("I do not accept the terms in the license agreement");
        ButtonGroup group = new ButtonGroup();
        group.add(acceptRadio);
        group.add(declineRadio);
    }

    /**
     * Builds the panel.
     */
    public void build(JComponent buttonBar) {
        buttonBar.setBorder(Borders.createEmptyBorder("6dlu, 6dlu, 6dlu, 6dlu"));
        
        FormLayout fl = new FormLayout(
                "15dlu, left:min:grow, 15dlu", 
                "pref, 17dlu, min:grow, 4dlu, pref, 2dlu, pref, 2dlu, pref");
        PanelBuilder builder = new PanelBuilder(this, fl);
        CellConstraints cc = new CellConstraints();
        
        builder.add(buildHeader(),          cc.xywh(1, 1, 3, 1));
        builder.add(buildLicensePanel(),    cc.xy  (2, 3, "f, f"));
        builder.add(acceptRadio,            cc.xy  (2, 5));
        builder.add(declineRadio,           cc.xy  (2, 7));
        builder.add(buttonBar,              cc.xywh(1, 9, 3, 1));
    }

    /**
     * Creates the panel's header.
     */
    private JComponent buildHeader() {
        return new HeaderPanel(
            "License Agreement",
            ResourceManager.getString(ResourceIDs.LICENSE_HEADER_TEXT),
            ResourceManager.getIcon(ResourceIDs.LICENSE_ICON),
            80);
    }

    /**
     * Builds and configures the license panel.
     */
    private JComponent buildLicensePanel() {
        JScrollPane scrollPane = new JScrollPane(licensePane);
        scrollPane.putClientProperty("jgoodies.isEtched", Boolean.TRUE);

        // Register license keyboard actions.
        KeyStroke[] registeredKeystrokes =
            scrollPane.getRegisteredKeyStrokes();
        for (int i = 0; i < registeredKeystrokes.length; i++) {
            KeyStroke keyStroke = registeredKeystrokes[i];
            ActionListener keyboardAction =
                scrollPane.getActionForKeyStroke(keyStroke);
            scrollPane.unregisterKeyboardAction(keyStroke);
            scrollPane.registerKeyboardAction(
                keyboardAction,
                keyStroke,
                JComponent.WHEN_IN_FOCUSED_WINDOW);
        }
        return scrollPane;
    }

}