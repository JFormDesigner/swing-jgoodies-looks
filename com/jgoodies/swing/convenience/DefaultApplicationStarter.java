/*
 * Copyright (c) 2003 JGoodies Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of JGoodies Karsten Lentzsch.  
 * Use is subject to license terms.
 *
 */
 
package com.jgoodies.swing.convenience;

import java.awt.Image;

import javax.swing.UIManager;

import com.jgoodies.plaf.LookUtils;
import com.jgoodies.splash.Splash;
import com.jgoodies.swing.application.*;
import com.jgoodies.swing.plaf.ExtUIManager;
import com.jgoodies.util.logging.FileHandler;
import com.jgoodies.util.logging.LogManager;

/**
 * This class is used during the startup process and extends
 * the more generic superclass behavior of the 
 * <code>AbstractApplicationStarter</code>.
 *
 * @author Karsten Lentzsch
 * @see	com.jgoodies.swing.application.Globals
 * @see	com.jgoodies.swing.application.Workbench
 * @see	com.jgoodies.swing.application.ResourceManager
 * @see	com.jgoodies.swing.application.ActionManager
 * @see	com.jgoodies.splash.Splash
 * @see	com.jgoodies.swing.help.HelpBroker
 */

public abstract class DefaultApplicationStarter extends AbstractApplicationStarter {
	
	/**
	 * Configure logging to write a <tt>console.log</tt> file.
	 */
	protected void configureLogging() {
		LogManager.setHandler(new FileHandler("%h/." + globals.getPreferencesNode() + "/console.log"));
	}
	
	
	/**
	 * Configures the splash component: reads the splash image, then opens an ImageSplash.
	 */
	protected void configureSplash() {
		Image image = ResourceManager.getIcon(ResourceIDs.SPLASH_IMAGE).getImage();
		Splash.setProvider(new ImageSplash(image, true));
	}	
	

	/**
	 * Brings up the application, it therefore initializes the main frame,
	 * checks the setup process, initializes all actions, then builds 
	 * the main frame, and finally opens it.
	 */
	protected void launchApplication() {
		// Replace the simple FileHandler DebugHandler by a message oriented one.
		LogManager.setHandler(new MessageLogHandler(LogManager.getHandler(), globals.getVendorMail()));
		
	    Splash.setNote("Creating MainFrame", 30);
		AbstractMainFrame mainFrame = createMainFrame();
		Workbench.setMainFrame(mainFrame);

		Splash.setNote("Initializing Actions...", 40);
		initializeActions();
		
		checkSetup();
		
		mainFrame.build();
		mainFrame.open();
	}	
	

	/**
	 * Creates and answers the main frame.
	 */
	abstract protected AbstractMainFrame createMainFrame();
	

	/**
	 * Initializes the application's actions.
	 */
	abstract protected void initializeActions();
	

	/**
	 * Configures all UI related properties: look&amp;feel, 
     * system properties, etc.
     * By default, we set the Swing class loader to class
     * <code>LookUtils</code> of the JGoodies L&amp;F library.
	 */
	protected void configureUI() {
	    ExtUIManager.setup();
        UIManager.put("ClassLoader", LookUtils.class.getClassLoader());
	}
	

	/**
	 * Checks whether a setup is necessary. For example, one can check
	 * whether the user has accepted the license agreement.
	 */
	protected void checkSetup() {
		configureSetupManager();
		if (!SetupManager.checkLicense()) 
			exit();
	}
	
	
	/**
	 * Configures the <code>SetupManager</code>. The default does nothing.
	 * Sublcasses can, for example modify the welcome panel.
	 */
	protected void configureSetupManager() {
	}
	
	
}