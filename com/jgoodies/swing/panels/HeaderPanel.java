package com.jgoodies.swing.panels;

/*
 * Copyright (c) 2003 JGoodies Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of JGoodies Karsten Lentzsch.
 * Use is subject to license terms.
 *
 */

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;

import javax.swing.*;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.swing.util.UIFactory;

/**
 * A panel that shows a a bold title, a description, and an optional icon in
 * front of a gradient background.
 *
 * @author Karsten Lentzsch
 */

public class HeaderPanel extends GradientBackgroundPanel {

    public static final int DEFAULT_HEIGHT = 70;

    private final int height;

    private JLabel    titleLabel;
    private JLabel    iconLabel;
    private JTextArea descriptionArea;
    

    // Instance Creation ****************************************************

    /**
     * Constructs a <code>HeaderPanel</code> for the given title, 
     * description, and icon.
     */
    public HeaderPanel(String title, String description, Icon icon) {
        this(title, description, icon, DEFAULT_HEIGHT);
    }

    /**
     * Constructs a <code>HeaderPanel</code> for the given title, 
     * description, icon, and panel height.
     */
    public HeaderPanel(String title, String description, Icon icon, int height) {
        super(true);
        this.height = height;
        initComponents();
        build();
        setTitle(title);
        setDescription(description);
        setIcon(icon);
    }

    // Accessors ************************************************************

    /** 
     * Returns the title text. 
     */
    public String getTitle() {
        return titleLabel.getText();
    }

    /** 
     * Sets the title text. 
     */
    public void setTitle(String title) {
        titleLabel.setText(title);
    }

    /** 
     * Returns the description text. 
     */
    public String getDescription() {
        return descriptionArea.getText();
    }

    /** 
     * Sets the description text. 
     */
    public void setDescription(String description) {
        descriptionArea.setText(description);
    }

    /** 
     * Returns the icon. 
     */
    public Icon getIcon() {
        return iconLabel.getIcon();
    }

    /** 
     * Sets the icon. 
     */
    public void setIcon(Icon icon) {
        if (null == icon) {
            iconLabel.setIcon(null);
            return;
        }
        if (icon.getIconWidth() > 20 || !(icon instanceof ImageIcon)) {
            iconLabel.setIcon(icon);
            return;
        }
        Image image   = ((ImageIcon) icon).getImage();
        int newWidth  = 2 * icon.getIconWidth();
        int newHeight = 2 * icon.getIconHeight();
        image = image.getScaledInstance(newWidth, newHeight, 0);
        iconLabel.setIcon(new ImageIcon(image));
    }

    // Building *************************************************************

    /**
     * Creates and configures the UI components.
     */
    private void initComponents() {
        titleLabel = UIFactory.createBoldLabel("", 0, Color.black);
        descriptionArea = UIFactory.createMultilineLabel("");
        descriptionArea.setForeground(Color.black);
        iconLabel = new JLabel();
    }
    
    /**
     * Builds the panel.
     */
    private void build() {
        FormLayout fl = new FormLayout(
                "pref:grow", 
                "pref, pref");
        setLayout(fl);
        CellConstraints cc = new CellConstraints();
        add(buildCenterComponent(), cc.xy(1, 1));
        add(buildBottomComponent(), cc.xy(1, 2));
    }

    /**
     * Builds and answers the panel's center component.
     */
    protected JComponent buildCenterComponent() {
        FormLayout fl = new FormLayout(
                "7dlu, 9dlu, left:pref, 14dlu:grow, pref, 4dlu", 
                "7dlu, pref, 2dlu, pref, 0:grow");
        JPanel panel = new JPanel(fl);
        Dimension size = new Dimension(300, height);
        panel.setMinimumSize(size);
        panel.setPreferredSize(size);
        panel.setOpaque(false);

        CellConstraints cc = new CellConstraints();
        panel.add(titleLabel,       cc.xywh(2, 2, 2, 1));
        panel.add(descriptionArea,  cc.xy  (3, 4));
        panel.add(iconLabel,        cc.xywh(5, 1, 1, 5));

        return panel;
    }

    /**
     * Builds and answers the panel's bottom component, a separator by default.
     */
    protected JComponent buildBottomComponent() {
        return new JSeparator();
    }


}