package com.jgoodies.swing.convenience;

/*
 * Copyright (c) 2002 Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of Karsten Lentzsch.  
 * Use is subject to license terms.
 *
 */
 
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.Border;

import com.jgoodies.forms.factories.ButtonBarFactory;
import com.jgoodies.layout.Grid;
import com.jgoodies.layout.Resizer;
import com.jgoodies.swing.AbstractDialog;
import com.jgoodies.swing.AbstractFrame;
import com.jgoodies.swing.application.ResourceIDs;
import com.jgoodies.swing.application.ResourceManager;
import com.jgoodies.swing.panels.CardPanel;
import com.jgoodies.util.logging.Level;
import com.jgoodies.util.logging.Logger;

/**
 * Builds the default setup dialog, which consists of two pages:
 * a welcome page and the license acceptance page. 
 * 
 * @see	DefaultSetupWelcomePanel
 * @see	SetupLicensePanel
 *
 * @author Karsten Lentzsch
 */

public class DefaultSetupDialog extends AbstractDialog {

	private static final int   PREFERRED_WIDTH	= 600;
	private static final String WELCOME_PANEL		= "Welcome";
	private static final String LICENSE_PANEL		= "License";
	
	private final JComponent			welcomePanel;
	private final SetupLicensePanel	licensePanel;
	
	
	/**
	 * Constructs a default setup dialog using the given owner frame,
	 * application name, welcome panel, and license panel.
	 */
	public DefaultSetupDialog(AbstractFrame owner, String applicationName, 
		JComponent welcomePanel, SetupLicensePanel licensePanel) {
		super(owner, applicationName + " Setup");
		this.welcomePanel = welcomePanel;
		this.licensePanel = licensePanel;
	}
	

	// Building *************************************************************************
		
	/**
	 * Builds and answers the panels content.
	 */
	protected JComponent buildContent() {
		CardPanel panel = new CardPanel();
		panel.add(buildWelcomePanel(), WELCOME_PANEL);
		panel.add(buildLicensePanel(), LICENSE_PANEL);

		Dimension size = Resizer.DEFAULT.fromWidth(PREFERRED_WIDTH);
		panel.setMinimumSize(size);
		panel.setPreferredSize(size);
		panel.setSize(size);

		return panel;
	}
	
	
	/**
	 * Builds and answers the welcome panel; hands over the button bar.
	 */
	private JComponent buildWelcomePanel() {
		((SetupManager.WizardPanel) welcomePanel).build(buildWelcomeButtonBar());
		return welcomePanel;
	}
	
	
	/**
	 * Builds and answer the license panel; hand over the button bar.
	 */
	private JComponent buildLicensePanel() {
		licensePanel.build(buildLicenseButtonBar());
		return licensePanel;
	}
	
	
	/**
	 * Answers the dialog's border. Since the default is a Wizard style
	 * we use an empty border.
	 */
	protected Border getDialogBorder() {
		return Grid.EMPTY_BORDER;
	}


	/**
	 * Uses the default <code>Resizer</code>.
	 */	
	protected void resizeHook(JComponent component) {
		Resizer.DEFAULT.resize(component);
	}
	

	// Building Buttons *****************************************************************
	
	/**
	 * Creates and answers the back button.
	 */	
	private JButton createBackButton() {
		JButton button = new JButton("< Back");   // TODO: I18n
		button.setMnemonic('B');
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doBack();
			}
		});
		return button;
	}
	
	
	/**
	 * Creates and answers the next button.
	 */	
	private JButton createNextButton() {
		JButton button = new JButton("Next >");  // TODO: I18n
		button.setMnemonic('N');
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doNext();
			}
		});
		return button;
	}
	
	
	/**
	 * Builds and answers the button bar for the welcome panel.
	 */	
	private JComponent buildWelcomeButtonBar() {
		JPanel bar = ButtonBarFactory.buildRightAlignedBar(
			     createNextButton(), 
                 createCancelButton());
        bar.setOpaque(false);
        return bar;
	}
	
	
	/**
	 * Builds and answers the button bar for the license panel.
	 */	
	private JComponent buildLicenseButtonBar() {
		JButton finishButton = createAcceptButton("Finish", false);
		finishButton.setMnemonic('F');
		return ButtonBarFactory.buildRightAlignedBar(
			         createBackButton(), 
                     finishButton, 
                     createCancelButton());
	}
	
	
	// Command execution ****************************************************************
	
	/**
	 * Performs the accept operation.
	 */
	public void doAccept() {
		if (licensePanel.licenseAccepted()) {
			super.doAccept();
			return;
		}
		if (licensePanel.licenseDeclined()) {
			doDecline();
			return;
		}
		JOptionPane.showMessageDialog(this,
				"You must select to either accept or decline the license agreement.",
				//sourceManager.getString(ResourceIDs.LICENSE_MUST_SELECT_TEXT),
				getTitle(), JOptionPane.INFORMATION_MESSAGE);
	}
	
	
	/**
	 * Performs the close window operation.
	 */
	protected void doCloseWindow() {
		if (LICENSE_PANEL.equals(getCardPanel().getVisibleChildName()))
			doCancel();
		else
			doNext();
	}
	
	
	/**
	 * Performs the cancel operation.
	 */
	public void doCancel() {
		int choice = JOptionPane.showConfirmDialog(this,
				"Do you really want to cancel the setup process?",
				//ResourceManager.getString(ResourceIDs.LICENSE_CONFIRM_DECLINE_TEXT),
				getTitle(), JOptionPane.YES_NO_OPTION);

		if (JOptionPane.YES_OPTION == choice)
			super.doCancel();
	}
	

	/**
	 * Declines the license.
	 */
	private void doDecline() {
		int choice = JOptionPane.showConfirmDialog(this,
				ResourceManager.getString(ResourceIDs.LICENSE_CONFIRM_DECLINE_TEXT),
				getTitle(), JOptionPane.YES_NO_OPTION);

		if (JOptionPane.YES_OPTION == choice)
			super.doCancel();
	}
	

	/**
	 * Goes back to the previous wizard page.
	 */	
	private void doBack() {
		getCardPanel().showPreviousCard();
	}
	
	
	/**
	 * Goes forward to the next wizard page.
	 */	
	private void doNext() {
		getCardPanel().showNextCard();
		setLicensePage();
	}
	
	
	// Misc *****************************************************************************

	/**
	 * Answers the card panel that is used to hold the wizard pages.
	 */	
	private CardPanel getCardPanel() {
		return (CardPanel) getContentPane();
	}
	
	
	/**
	 * Answers the license page.
	 */	
	private void setLicensePage() {
		URL url = getLicenseAgreementURL();
		try {
			licensePanel.setPage(url);
		} catch (IOException e) {
			Logger.getLogger("LicenseAcceptanceDialog").log(
				Level.WARNING, "Can't open URL " + url + ".", e);
		}
	}
	

	/**
	 * Looks up and answers the URL of the license agreement.
	 */	
	private URL getLicenseAgreementURL() {
		String urlName = ResourceManager.getString(ResourceIDs.LICENSE_AGREEMENT_PATH);
		return ResourceManager.getURL(urlName);
	}
	
}