package com.jgoodies.swing.convenience;

/*
 * Copyright (c) 2002 JGoodies Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of JGoodies Karsten Lentzsch.
 * Use is subject to license terms.
 *
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Frame;
import java.awt.Image;

import javax.swing.*;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.splash.SplashProvider;
import com.jgoodies.swing.util.UIFactory;
import com.jgoodies.util.ScreenUtils;

/**
 * An implementation of <code>SplashProvider</code> that uses a Swing
 * <code>JWindow</code> to display a splash image, messages, and a progress
 * bar.<p>
 * 
 * @author Karsten Lentzsch
 * @see	com.jgoodies.splash.Splash
 * @see	com.jgoodies.splash.SplashProvider
 */

public final class JWindowSplash extends JWindow implements SplashProvider {

    private JLabel       imageLabel;
    private JProgressBar progressBar;
    private JLabel       noteLabel;


    /**
     * Creates a Swing based splash for the given <code>Frame</code> 
     * and <code>Image</code>.
     */
    public JWindowSplash(Frame owner, Image image) {
        super(owner);
        imageLabel = new JLabel(new ImageIcon(image));
        initComponents();
        build();
    }

    /**
     * Opens the splash window.
     */
    public void openSplash() {
        setVisible(true);
    }

    /**
     * Closes and disposes the splash window.
     */
    public void closeSplash() {
        dispose();
    }

    /**
     * Sets a new progress value.
     */
    public void setProgress(int percent) {
        progressBar.setValue(percent);
    }

    /**
     * Sets a new progress message.
     */
    public void setNote(String message) {
        noteLabel.setText(message);
    }

    // Building *************************************************************

    /**
     * Creates and configures the UI components.
     */
    private void initComponents() {
        progressBar = new JProgressBar(0, 100);
        progressBar.setForeground(Color.gray);
        progressBar.setBackground(Color.black);
        progressBar.setBorderPainted(false);

        noteLabel = UIFactory.createPlainLabel("Loading...");
        noteLabel.setHorizontalAlignment(SwingConstants.CENTER);
        noteLabel.setForeground(Color.lightGray);
    }

    /**
     * Builds the splash.
     */
    private void build() {
        setContentPane(buildContentPane());
        pack();
        ScreenUtils.locateOnScreenCenter(this);
    }

    /**
     * Builds and answers the content pane.
     */
    private JComponent buildContentPane() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(imageLabel,         BorderLayout.CENTER);
        panel.add(buildBottomPanel(), BorderLayout.SOUTH);
        return panel;
    }

    /**
     * Builds and answers the bottom panel.
     */
    private Component buildBottomPanel() {
        JPanel progressPanel = new JPanel(new BorderLayout());
        progressPanel.add(progressBar);
        progressPanel.setBorder(BorderFactory.createLineBorder(Color.gray));

        FormLayout layout = new FormLayout(
                "2dlu, center:pref:grow, 2dlu",
                "2dlu, pref, 2dlu, pref, 4dlu");
        JPanel panel = new JPanel(layout);
        panel.setBackground(Color.black);
        panel.setBorder(BorderFactory.createLineBorder(Color.gray));
        panel.add(noteLabel,     "2, 2");
        panel.add(progressPanel, "2, 4");
        return panel;
    }

}