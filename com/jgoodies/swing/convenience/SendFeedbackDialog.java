/*
 * Copyright (c) 2003 JGoodies Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of JGoodies Karsten Lentzsch.
 * Use is subject to license terms.
 *
 */

package com.jgoodies.swing.convenience;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.MessageFormat;

import javax.swing.*;
import javax.swing.border.Border;

import com.jgoodies.forms.builder.ButtonBarBuilder;
import com.jgoodies.forms.factories.Borders;
import com.jgoodies.layout.Grid;
import com.jgoodies.layout.Resizer;
import com.jgoodies.swing.AbstractDialog;
import com.jgoodies.swing.application.ResourceIDs;
import com.jgoodies.swing.application.ResourceManager;
import com.jgoodies.swing.panels.HeaderPanel;
import com.jgoodies.swing.util.UIFactory;

/**
 * A dialog that is used to send feedback. Consists of a tabbed pane
 * with two tabs: help and mail contents.
 *
 * @author Karsten Lentzsch
 */

public final class SendFeedbackDialog extends AbstractDialog {
    
    private static final int    PANEL_WIDTH = 280;
    private static final String COPY_TO_CLIPBOARD_LABEL = "Copy to Clipboard";

    private final String receiver;
    private final String subject;
    private final String mailContents;

    /**
     * Constructs a <code>SendFeedbackDialog</code> using the given
     * owner frame, mail receiver, mail subject, and mail contents.
     */
    public SendFeedbackDialog(
        Frame owner,
        String receiver,
        String subject,
        String mailContents) {
        super(owner);
        this.receiver = receiver;
        this.subject = subject;
        this.mailContents = mailContents;
    }

    // Building *************************************************************

    /**
     * Builds and answer the dialog's header.
     */
    protected JComponent buildHeader() {
        return new HeaderPanel(
            "Send Feedback: " + subject,
            "You can send feedback to help improve this tool."
                + "\nClick on the 'Contents' tab to see the mail contents.",
            ResourceManager.getIcon(ResourceIDs.FEEDBACK_ICON),
            80);
    }

    /**
     * Builds and answers the content.
     */
    protected JComponent buildContent() {
        JPanel content = new JPanel(new BorderLayout());
        content.add(buildTabbedPane(), BorderLayout.CENTER);
        content.add(buildButtonBar(),  BorderLayout.SOUTH);
        return content;
    }

    /**
     * Builds and answers the tabbed pane with two tabs.
     */
    protected JComponent buildTabbedPane() {
        JTabbedPane pane = new JTabbedPane();
        pane.add("Help", buildHelpTab());
        pane.add("Mail Contents", buildMailContentsTab());
        return pane;
    }

    /**
     * Builds and answers the help tab.
     */
    private JPanel buildHelpTab() {
        JPanel panel = new JPanel(new BorderLayout());
        JTextArea area =
            UIFactory.createWrappedMultilineLabel(getHelpContents());
        panel.add(area);
        panel.setPreferredSize(Resizer.DEFAULT.fromWidth(PANEL_WIDTH));
        panel.setBorder(Borders.DIALOG_BORDER);
        return panel;
    }

    /**
     * Builds and answers the mail contents tab.
     */
    private JPanel buildMailContentsTab() {
        JPanel panel = new JPanel(new BorderLayout());
        JTextArea area = new JTextArea(getContentsWithHeader());
        area.setEditable(false);
        panel.add(new JScrollPane(area));
        panel.setPreferredSize(Resizer.DEFAULT.fromWidth(PANEL_WIDTH));
        panel.setBorder(Borders.DIALOG_BORDER);
        return panel;
    }

    /** 
     * Builds and answers the button bar with "Copy to Clipboard" and "Close"  buttons.
     */
    private JComponent buildButtonBar() {
        ButtonBarBuilder builder = new ButtonBarBuilder();
        builder.setDefaultButtonBarGapBorder();
        builder.addGlue();
        builder.addFixed(createCopyToClipboardButton());
        builder.addUnrelatedGap();
        builder.addGridded(createCloseButton(true));
        return builder.getPanel();
    }

    /**
     * Creates and answers the copy-to-clipboard button.
     */
    private JButton createCopyToClipboardButton() {
        JButton button = new JButton(COPY_TO_CLIPBOARD_LABEL);
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                StringSelection selection =
                    new StringSelection(getContentsWithHeader());
                getToolkit().getSystemClipboard().setContents(
                    selection,
                    selection);
                //copyButton.setEnabled(false);
            }
        });
        return button;
    }

    /**
     * Returns the dialog's border, here, the default card dialog border.
     */
    protected Border getDialogBorder() {
        return Grid.CARD_DIALOG_BORDER;
    }

    /**
     * Use the default resizer to get an aesthetic aspect ratio.
     */
    protected void resizeHook(JComponent component) {
        Resizer.DEFAULT.resize(component);
    }

    // Helper Code **********************************************************

    /**
     * Returns the help contents.
     */
    private String getHelpContents() {
        String pattern =
            ResourceManager.getString(ResourceIDs.FEEDBACK_DIALOG_HELP);
        return MessageFormat.format(
            pattern,
            new Object[] { receiver, subject });
    }

    /**
     * Returns the mail contents as concatenated string.
     */
    private String getContentsWithHeader() {
        return "To: "
            + receiver
            + "\n"
            + "Subject: "
            + subject
            + "\n\n"
            + mailContents;
    }

}