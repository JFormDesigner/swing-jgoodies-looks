/*
 * Copyright (c) 2003 JGoodies Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of JGoodies Karsten Lentzsch.  
 * Use is subject to license terms.
 *
 */

package com.jgoodies.looks.demo;

import java.awt.FileDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import com.jgoodies.forms.builder.ButtonBarBuilder;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/** 
 * Contains a bunch of buttons to open a bunch of standard dialogs.
 * 
 * @author Karsten Lentzsch
 */
final class DialogsTab {
    
    private JButton informationButton;
    private JButton warningButton;
    private JButton questionButton;
    private JButton errorButton;
    private JButton chooseFileNativeButton;
    private JButton chooseFileSwingButton;
    
    
    /**
     * Creates and configures the UI components.
     */
    private void initComponents() {
        informationButton = new JButton("Information");
        informationButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(
                    DemoFrame.getInstance(),
                    "We just wanted to let you know that you have pressed\n" +
                    "the Information button to open this sample message dialog.\n\n",
                    "Information",
                    JOptionPane.INFORMATION_MESSAGE);
            }
        });
        warningButton = new JButton("Warning");
        warningButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(
                    DemoFrame.getInstance(),
                    "We just wanted to let you know that you have pressed\n" +
                    "the Warning button to open this sample message dialog.\n\n",
                    "Warning",
                    JOptionPane.WARNING_MESSAGE);
            }
        });
        questionButton = new JButton("Question");
        questionButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showConfirmDialog(
                    DemoFrame.getInstance(),
                    "We just wanted to let you know that you have pressed\n" +
                    "the Question button to open this sample question dialog.\n\n" +
                    "Are you satisfied with the dialog's appearance?\n\n",
                    "Question",
                    JOptionPane.YES_NO_OPTION
                    );
            }
        });
        errorButton = new JButton("Error");
        errorButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(
                    DemoFrame.getInstance(),
                    "We just wanted to let you know that you have pressed\n" +
                    "the Error button to open this error message dialog.\n\n" +
                    "Just go ahead and proceed.\n\n",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        });
        chooseFileNativeButton = new JButton("Open...");
        chooseFileNativeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new FileDialog(DemoFrame.getInstance(), "Open File (Native)")
                    .show();
            }
        });
        chooseFileSwingButton = new JButton("Open...");
        chooseFileSwingButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new JFileChooser("Open File (Swing)").showOpenDialog(
                    DemoFrame.getInstance());
            }
        });
    }
	
	/**
	 * Builds and answers the panel.
	 */
    JComponent build() {
        initComponents();

        FormLayout layout =
            new FormLayout(
                "0:grow, left:pref, 0:grow",
                "0:grow, pref, 4dlu, pref, 14dlu, pref, 4dlu, pref, 14dlu, pref, 4dlu, pref, 0:grow");
        PanelBuilder builder = new PanelBuilder(layout);
        builder.setDefaultDialogBorder();

        CellConstraints cc = new CellConstraints();

        builder.addLabel("Press a button to open a message dialog.", cc.xy(2,  2));
        builder.add(buildButtonBar(),                                cc.xy(2,  4));
        builder.addLabel("This opens the native file chooser.",      cc.xy(2,  6));
        builder.add(chooseFileNativeButton,                          cc.xy(2,  8));
        builder.addLabel("This opens the Swing file chooser.",       cc.xy(2, 10));
        builder.add(chooseFileSwingButton,                           cc.xy(2, 12));

        return builder.getPanel();
    }
	
    /**
     * Builds and answers the message dialog button bar.
     * 
     * @return the message dialog button bar
     */
    private JPanel buildButtonBar() {
        ButtonBarBuilder builder = new ButtonBarBuilder();
        builder.addGriddedButtons(new JButton[]{
            informationButton, warningButton, questionButton, errorButton});
        return builder.getPanel();
    }	

}