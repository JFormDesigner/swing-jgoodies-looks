package com.jgoodies.swing.convenience;

/*
 * Copyright (c) 2002 JGoodies Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of JGoodies Karsten Lentzsch.
 * Use is subject to license terms.
 *
 */
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

import com.jgoodies.forms.builder.ButtonBarBuilder;
import com.jgoodies.layout.Resizer;
import com.jgoodies.swing.AbstractDialog;
import com.jgoodies.swing.AbstractFrame;
import com.jgoodies.swing.application.ResourceManager;
import com.jgoodies.swing.application.Workbench;
import com.jgoodies.swing.model.BufferedValueHolder;
import com.jgoodies.swing.model.PreferencesAdaptor;
import com.jgoodies.swing.model.ToggleButtonAdaptor;
import com.jgoodies.swing.model.ValueModel;
import com.jgoodies.swing.util.UIFactory;
import com.jgoodies.util.logging.Level;
import com.jgoodies.util.logging.Logger;

/**
 * This class provides a dialog and managing behavior for
 * a tip of the day functionality. 
 *
 * @see	PreferencesAdaptor
 * 
 * @author Karsten Lentzsch
 */
public final class TipOfTheDayDialog extends AbstractDialog {

    private static final String  IS_SHOWING_KEY     = "tipOfTheDay.isShowing";
    private static final Boolean IS_SHOWING_DEFAULT = Boolean.TRUE;

    private static final String  TIP_INDEX_KEY      = "tipOfTheDay.index";
    private static final Integer TIP_INDEX_DEFAULT  = new Integer(0);

    private static final Logger  LOGGER =
        Logger.getLogger("TipOfTheDayDialog");

    private static PreferencesAdaptor showingTipsModel;
    private static PreferencesAdaptor tipIndexModel;

    private JEditorPane htmlPane;
    private List        tipPaths;

    // Static Access to the showingTips Property ****************************************

    /**
     * Answers if we shall show tips at application startup.
     */
    public static boolean isShowingTips() {
        return showingTipsModel().getBoolean();
    }

    /**
     * Sets if we shall show tips at application startup.
     */
    public static void setShowingTips(boolean b) {
        showingTipsModel().setBoolean(b);
    }

    /**
     * Answers an adaptor for the showing tips boolean property, 
     * backed up by user preferences.
     */
    public static PreferencesAdaptor showingTipsModel() {
        if (showingTipsModel == null) {
            showingTipsModel =
                new PreferencesAdaptor(
                    Workbench.userPreferences(),
                    IS_SHOWING_KEY,
                    IS_SHOWING_DEFAULT);
        }
        return showingTipsModel;
    }

    // Instance Creation ****************************************************

    /**
     * Constructs the dialog for the given ownwer and path to the tip index file.
     */
    public TipOfTheDayDialog(AbstractFrame owner, String tipIndexPath) {
        super(owner, "Tip of the Day");
        tipIndexModel =
            new PreferencesAdaptor(
                Workbench.userPreferences(),
                TIP_INDEX_KEY,
                TIP_INDEX_DEFAULT);
        readTipPaths(tipIndexPath);
    }

    // Building *************************************************************

    /**
     * Builds and answers the dialog's content.
     */
    protected JComponent buildContent() {
        JPanel content = new JPanel(new BorderLayout());
        content.add(buildMainPanel(), BorderLayout.CENTER);
        content.add(buildButtonBar(), BorderLayout.SOUTH);
        return content;
    }

    /**
     * Builds and answers the main panel.
     */
    private JPanel buildMainPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        htmlPane = UIFactory.createHTMLPane(false, false);
        goToNextTip();
        panel.add(new JScrollPane(htmlPane));
        panel.setPreferredSize(Resizer.DEFAULT.fromWidth(320));
        return panel;
    }

    /** 
     * Creates and answrs the button bar, which consists of:
     * CheckBox, glue, Back, Next, and Close buttons.
     */
    private JComponent buildButtonBar() {
        JCheckBox showTipsCheckBox = new JCheckBox("Show tips on startup");
        showTipsCheckBox.setModel(
            createShowTipsButtonModel(showingTipsModel()));

        ButtonBarBuilder builder = new ButtonBarBuilder();
        builder.setDefaultButtonBarGapBorder();
        builder.addFixed(showTipsCheckBox);
        builder.addUnrelatedGap();
        builder.addGlue();
        builder.addGridded(createBackButton());
        builder.addGriddedButtons(
            new JButton[] { createNextButton(), createCloseButton(true)});
        return builder.getPanel();
    }

    /**
     * Use the default resizer to get an aesthetic aspect ratio.
     */
    protected void resizeHook(JComponent component) {
        Resizer.DEFAULT.resize(component);
    }

    /**
     * Creates and answers a <code>ButtonModel</code> for the 
     * show tips check box, using the given <code>ValueModel</code>.
     */
    private ButtonModel createShowTipsButtonModel(ValueModel model) {
        return new ToggleButtonAdaptor(
            new BufferedValueHolder(model, applyTrigger()));
    }

    /**
     * Creates and answers the back button.
     */
    private JButton createBackButton() {
        JButton button = new JButton("< Back");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                goToPreviousTip();
            }
        });
        return button;
    }

    /**
     * Creates and answers the next button.
     */
    private JButton createNextButton() {
        JButton button = new JButton("Next >");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                goToNextTip();
            }
        });
        return button;
    }

    // Behavior *************************************************************************

    /**
     * Performs the close operation.
     */
    public void close() {
        applyTrigger().setValue(Boolean.TRUE);
        super.close();
    }

    /**
     * Loads and displays the next tip.
     */
    private void goToNextTip() {
        int tipIndex = tipIndexModel.getInt();
        if (++tipIndex >= tipPaths.size())
            tipIndex = 0;
        tipIndexModel.setInt(tipIndex);
        showTip((String) tipPaths.get(tipIndex));
    }

    /**
     * Loads and displays the previous tip.
     */
    private void goToPreviousTip() {
        int tipIndex = tipIndexModel.getInt();
        if (--tipIndex < 0)
            tipIndex = tipPaths.size() - 1;
        tipIndexModel.setInt(tipIndex);
        showTip((String) tipPaths.get(tipIndex));
    }

    // Misc *****************************************************************************

    /**
     * Answers the directory name for the given file name.
     */
    private String getDirectoryName(String filename) {
        final char ZIP_SEPARATOR = '/';
        int lastSeparatorIndex = filename.lastIndexOf(ZIP_SEPARATOR);
        return (lastSeparatorIndex == -1)
            ? ""
            : filename.substring(0, lastSeparatorIndex + 1);
    }

    /**
     * Reads and parses the tip index file to get all contained paths.
     */
    private void readTipPaths(String filename) {
        tipPaths = new ArrayList();
        String directoryName = getDirectoryName(filename);
        InputStream in = ResourceManager.getInputStream(filename);
        if (null == in)
            return;
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        try {
            String line;
            do {
                line = reader.readLine();
                if (line != null) {
                    line = line.trim();
                    if (line.length() > 0)
                        tipPaths.add(directoryName + line);
                }
            } while (line != null);
        } catch (IOException e) {
            LOGGER.log(
                Level.WARNING,
                "Error while reading tip index file: " + filename,
                e);
        }
    }

    /**
     * Shows the tip for the specified tip path.
     */
    private void showTip(String tipPath) {
        URL tipURL = ResourceManager.getURL(tipPath);
        try {
            htmlPane.setPage(tipURL);
        } catch (IOException e) {
            if (tipURL != null)
                LOGGER.warning("Can't show tip URL: " + tipURL);
        }
    }

}