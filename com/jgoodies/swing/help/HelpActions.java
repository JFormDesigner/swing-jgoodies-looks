package com.jgoodies.swing.help;

/*
 * Copyright (c) 2002 Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of Karsten Lentzsch.  
 * Use is subject to license terms.
 *
 */

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import com.jgoodies.swing.application.ActionManager;
import com.jgoodies.swing.printing.PrintManager;
import com.jgoodies.swing.printing.PrintableDocument;
import com.jgoodies.swing.util.History;

/**
 * This class provides all UI actions and their ids used in the tiny help.
 * Therefore it declares static fields for action ids
 * and concrete subclasses of class <code>AbstractAction</code>.
 * 
 * @see	AbstractAction
 * @see	ActionManager
 *
 * @author Karsten Lentzsch
 */

final class HelpActions {
	
	static final String HELP_GO_BACK_ID		= "com.jgoodies.help.goBack";
	static final String HELP_GO_NEXT_ID		= "com.jgoodies.help.goNext";
	static final String HELP_GO_HOME_ID		= "com.jgoodies.help.goHome";
	static final String HELP_PAGE_SETUP_ID	= "com.jgoodies.help.pageSetup";
	static final String HELP_PRINT_ID			= "com.jgoodies.help.print";
	static final String HELP_CLOSE_ID			= "com.jgoodies.help.close";
	
	
	/**
	 * Registers all available actions in the ActionManager.
	 */
	static void registerActions(History history) {
		ActionManager.register(HELP_CLOSE_ID, new AbstractAction() {
			public void actionPerformed(ActionEvent event) {
				HelpBroker.closeViewer();
			}
		});

		ActionManager.register(HELP_PRINT_ID, new AbstractAction() { 
			public void actionPerformed(ActionEvent event) {
				PrintableDocument.createFrom(
					HelpBroker.getSelectedURL()).printWithDialog();
			}
		});

		ActionManager.register(HELP_PAGE_SETUP_ID, new AbstractAction() {
			public void actionPerformed(ActionEvent event) {
				PrintManager.openPageSetupDialog();
			}
		});
		
		// Eagerly initialize the help actions; they won't be requested through the #get.
		ActionManager.InitializationType eager = ActionManager.EAGER;
		ActionManager.register(HELP_GO_BACK_ID, history.getGoBackAction(), eager); 
		ActionManager.register(HELP_GO_NEXT_ID, history.getGoNextAction(), eager); 
		ActionManager.register(HELP_GO_HOME_ID, history.getGoHomeAction(), eager); 
	}
	
}
