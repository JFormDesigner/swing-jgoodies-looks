package com.jgoodies.swing.convenience;

/*
 * Copyright (c) 2002 JGoodies Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of JGoodies Karsten Lentzsch.
 * Use is subject to license terms.
 *
 */

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JComponent;
import javax.swing.JLabel;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.factories.Borders;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.swing.application.ResourceIDs;
import com.jgoodies.swing.application.ResourceManager;
import com.jgoodies.swing.application.Workbench;
import com.jgoodies.swing.panels.GradientBackgroundPanel;
import com.jgoodies.swing.util.UIFactory;

/**
 * Builds the default welcome panel for the setup dialog.
 *
 * @author	Karsten Lentzsch
 * @see	DefaultSetupDialog
 * @see	GradientBackgroundPanel
 */

public class DefaultSetupWelcomePanel extends GradientBackgroundPanel
    implements SetupManager.WizardPanel {
     
    private JLabel      welcome;
    private JLabel      logo;
    private JLabel      description;
    private JComponent  welcomeText;
    
    
    /**
     * Creates and configures the UI components.
     */   
    private void initComponents() {
        Color foreground = Color.darkGray;
        welcome     = UIFactory.createBoldLabel("Welcome to", 0, foreground);
        logo        = new JLabel(ResourceManager.getIcon(ResourceIDs.LOGO_ICON));
        description = UIFactory.createBoldLabel(
                Workbench.getGlobals().getDescription(),
                0, foreground);
        String text = ResourceManager.getString(ResourceIDs.LICENSE_WELCOME_TEXT);
        welcomeText = UIFactory.createWrappedMultilineLabel(text);
        welcomeText.setForeground(Color.black);
    }

    /**
     * Builds the panel using the specified button bar.
     */
    public void build(JComponent buttonBar) {
        initComponents();
        buttonBar.setBorder(Borders.createEmptyBorder("6dlu, 6dlu, 6dlu, 6dlu"));

        FormLayout fl = new FormLayout(
                "left:min, 7dlu, center:pref:grow", 
                "pref, pref, pref, 7dlu, top:pref:grow, pref");
        PanelBuilder builder = new PanelBuilder(this, fl);
        builder.setBorder(Borders.createEmptyBorder("20dlu, 20dlu, 0, 0"));
        CellConstraints cc = new CellConstraints();
        
        builder.add(welcome,        cc.xy  (1, 1));
        builder.add(logo,           cc.xy  (1, 2));
        builder.add(description,    cc.xy  (1, 3));
        builder.add(welcomeText,    cc.xywh(1, 5, 3, 1));
        builder.add(buttonBar,      cc.xywh(1, 6, 3, 1));
    }

    /**
     * Paints the component's background; uses the <code>GradientBackgroundPanel</code>.
     */
    public void paintComponent(Graphics g) {
        GradientBackgroundPanel.paintBackground(
            g,
            this,
            getWidth(),
            getHeight(),
            false);
    }

}