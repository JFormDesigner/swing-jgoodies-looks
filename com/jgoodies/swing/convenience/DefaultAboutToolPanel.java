package com.jgoodies.swing.convenience;

/*
 * Copyright (c) 2003 JGoodies Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of JGoodies Karsten Lentzsch.
 * Use is subject to license terms.
 *
 */

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.factories.Borders;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.layout.Resizer;
import com.jgoodies.swing.AbstractDialog;
import com.jgoodies.swing.application.ResourceIDs;
import com.jgoodies.swing.application.ResourceManager;
import com.jgoodies.swing.application.Workbench;
import com.jgoodies.swing.help.HelpBroker;
import com.jgoodies.swing.util.UIFactory;
import com.jgoodies.util.logging.Logger;

/**
 * Provides the default <i>tool</i> card in the default about dialog.
 *
 * @see	DefaultAboutDialog
 *
 * @author Karsten Lentzsch
 */

public class DefaultAboutToolPanel extends JPanel {
    
    private JComponent  productLabel;
    private JComponent  versionLabel;
    private JComponent  copyrightText;
    private JButton     licenseButton;


    /**
     * Creates and configures the UI components.
     */
    protected void initComponents() {
        productLabel  = UIFactory.createBoldLabel(Workbench.getGlobals().getProductText());
        versionLabel  = UIFactory.createMultilineLabel(getVersionText());
        copyrightText = UIFactory.createWrappedMultilineLabel(
                            ResourceManager.getString(ResourceIDs.ABOUT_COPYRIGHT_TEXT));
        licenseButton = buildShowLicenseButton();
    }
    
    
    /**
     * Builds the panel.
     */
    public void build() {
        initComponents();
        
        FormLayout fl = new FormLayout(
                "left:pref:grow",
                "pref, pref, 14dlu, min, 2dlu, pref, 14dlu, pref");
        PanelBuilder builder = new PanelBuilder(this, fl);
        builder.getPanel().setPreferredSize(Resizer.DEFAULT.fromWidth(430));
        builder.setBorder(Borders.DLU14_BORDER);
        CellConstraints cc = new CellConstraints();

        builder.add(productLabel,                  cc.xy(1, 1));
        builder.add(versionLabel,                  cc.xy(1, 2));
        builder.add(copyrightText,                 cc.xy(1, 4, "fill,  top"));
        builder.add(licenseButton,                 cc.xy(1, 6, "right, top"));
        builder.add(buildAcknowledgementsPanel(),  cc.xy(1, 8, "fill,  top"));
    }

    /**
     * Builds and answers a button, that shows the license.
     */
    protected JButton buildShowLicenseButton() {
        JButton button = new JButton("Show License");
        button.setMnemonic('S');
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ((AbstractDialog) SwingUtilities
                    .getWindowAncestor(DefaultAboutToolPanel.this))
                    .close();
                HelpBroker.openURL(getLicenseAgreementURL());
            }
        });
        return button;
    }

    /**
     * Builds and answers the acknowledgements panel.
     */
    protected JComponent buildAcknowledgementsPanel() {
        return UIFactory.createPlainLabel(getAcknowledgementsText());
    }

    // Accessing Resources **************************************************

    protected String getAcknowledgementsText() {
        return ResourceManager.getString(
            ResourceIDs.ABOUT_ACKNOWLEDGEMENTS_TEXT);
    }

    private String getVersionText() {
        String webAddress = Workbench.getGlobals().getVendorURL();
        try {
            URL url = new URL(webAddress);
            webAddress = url.getHost();
        } catch (MalformedURLException e) {
            Logger.getLogger("About").warning(
                "Malformed vendor URL" + webAddress);
        }
        String copyright = Workbench.getGlobals().getShortCopyright();
        return "Copyright " + copyright + ", " + webAddress;
    }

    /**
     * Answers the <code>URL</code> of the license agreement.
     * This implementation uses a default key to look up the URL
     * via the <code>ResourceManager</code>.
     *  
     * @return the <code>URL</code> of the license agreement
     */
    protected URL getLicenseAgreementURL() {
        String urlName =
            ResourceManager.getString(ResourceIDs.LICENSE_AGREEMENT_PATH);
        return ResourceManager.getURL(urlName);
    }

}