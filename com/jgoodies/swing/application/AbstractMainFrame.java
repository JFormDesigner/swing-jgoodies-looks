package com.jgoodies.swing.application;

/*
 * Copyright (c) 2002 Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of Karsten Lentzsch.  
 * Use is subject to license terms.
 *
 */
import javax.swing.JComponent;

import java.awt.Dimension;

import com.jgoodies.layout.Resizer;
import com.jgoodies.splash.Splash;
import com.jgoodies.swing.AbstractFrame;

/**
 * A frame with extra behavior for an application's single main frame.
 *
 * @author Karsten Lentzsch
 */
public abstract class AbstractMainFrame extends AbstractFrame {

    private static final Dimension DEFAULT_MINIMUM_SIZE =
        new Dimension(200, 100);

    /**
     * Constructs an <code>AbstractMainFrame</code> with the specified title.
     */
    public AbstractMainFrame(String label) {
        super(label);
    }

    // Building *************************************************************

    /**
     * Opens the main frame.
     */
    public void open() {
        Splash.setNote("Opening...", 90);
        super.open();
    }

    /**
     * Tries to achieve a 5:4 aspect ration.
     */
    protected void resizeHook(JComponent component) {
        Resizer.FIVE2FOUR.resize(component);
    }

    // Implementing Abstract Superclass Behavior ****************************

    /**
     * Answers the frame's id; used to store and restore frame specific
     * state in the preferences.
     */
    protected final String getFrameID() {
        return "main";
    }
    
    /**
     * Answers the frame's minimum size.
     * 
     * @see com.jgoodies.swing.AbstractFrame#getFrameMinimumSize()
     */
    protected Dimension getFrameMinimumSize() {
        return DEFAULT_MINIMUM_SIZE;
    }

    // Misc *****************************************************************

    /**
     * Sets the window title. It uses the application's product name 
     * as a prefix and ensures a maximal length.
     */
    public void setTitlePrefix(String prefix) {
        String suffix = Workbench.getGlobals().getProductName();
        String trimmedPrefix = prefix.trim();
        if (trimmedPrefix.length() > 40)
            trimmedPrefix = trimmedPrefix.substring(0, 37) + "...";

        String middle = trimmedPrefix.length() == 0 ? "" : " - ";
        setTitle(trimmedPrefix + middle + suffix);
    }

    // Closing Window and Exiting the Application ***************************

    /**
     * Handles a request to close the main frame's window.
     */
    protected void doCloseWindow() {
        aboutToExitApplication();
    }

    /**
     * Leaves the application if nobody vetos against the close request.
     */
    public void aboutToExitApplication() {
        if (requestForWindowClose())
            exitApplication();
    }

    /**
     * Checks and answers whether we veto against a close request.
     * The default behavior just says: true = OK to close.
     * Some application may ask "Do you really want to close the application?".
     */
    protected boolean requestForWindowClose() {
        return true;
    }

    /**
     * Shuts down the application: closes the Workbench, which in turn 
     * does the concrete application shut down.
     */
    protected void exitApplication() {
        Workbench.close();
    }

}