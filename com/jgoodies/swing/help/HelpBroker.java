package com.jgoodies.swing.help;

/*
 * Copyright (c) 2003 JGoodies Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of JGoodies Karsten Lentzsch.  
 * Use is subject to license terms.
 *
 */

import java.net.URL;

import com.jgoodies.lazy.Preparable;
import com.jgoodies.swing.application.ResourceManager;
import com.jgoodies.util.logging.Level;
import com.jgoodies.util.logging.Logger;

/** 
 * This singleton class provides access to 
 * a {@link com.jgoodies.swing.help.HelpViewer}, which in turn uses 
 * a {@link com.jgoodies.swing.help.HelpSet}.
 * 
 * @author Karsten Lentzsch
 */

public final class HelpBroker implements Preparable {

    private static final Logger LOGGER = Logger.getLogger("HelpBroker");

    private static String helpSetPath;
    private static HelpBroker instance;

    private final HelpViewer viewer;

    /**
     * Constructs a <code>HelpBroker</code> instance and 
     * creates and builds a <code>HelpViewer</code>.
     */
    HelpBroker(HelpSet helpSet) {
        this.viewer = new HelpViewer(helpSet);
        viewer.build();
    }

    /**
     * Sets the path to the HelpSet, which will be used in the
     * lazy initialization process, see #createInstance.
     * 
     * @param path    the path used to create the <code>HelpSet</code>
     */
    public static void setHelpSetPath(String path) {
        helpSetPath = path;
    }

    /**
     * Ensures that the <code>HelpViewer</code> displays the help page
     * from the specified <code>URL</code>.
     * 
     * @param url   the <code>URL</code> of the help page to open
     */
    public static void openURL(URL url) {
        viewer().show(url);
    }
    
    /**
     * Ensures that the <code>HelpViewer</code> displays 
     * the default help page.
     */
    public static void openDefault() {
        viewer().setDisplayed(true);
    }
    
    /**
     * Returns whether this broker has already created an instance.
     * 
     * @return true if an instance has been created, false otherwise
     */
    public static boolean hasInstance() {
        return instance != null;
    }
    
    static URL getSelectedURL() {
        return viewer().getSelectedURL();
    }
    
    static void closeViewer() {
        viewer().doCloseWindow();
    }

    /**
     * Answers the single HelpBroker instance; lazily creates an instance.
     */
    public static HelpBroker getInstance() {
        if (!hasInstance()) {
            instance = createInstance(helpSetPath);
        }
        return instance;
    }

    /**
     * Finds HelpSet's URL, creates a HelpSet, and finally creates
     * a HelpBroker instance for this HelpSet.
     */
    private static HelpBroker createInstance(String aHelpSetPath) {
        ClassLoader loader = ResourceManager.class.getClassLoader();
        URL url = HelpSet.findHelpSet(loader, aHelpSetPath);
        if (null == url) {
            LOGGER.warning("HelpSet not found.");
            return null;
        }
        HelpBroker helpBroker = null;
        HelpSet hs = null;
        try {
            hs = new HelpSet(loader, url);
            helpBroker = hs.createHelpBroker();
            if (null == helpBroker)
                LOGGER.warning("Can't properly create HelpBroker.");
        } catch (HelpSetException e) {
            LOGGER.log(
                Level.WARNING,
                "Can't open HelpSet " + aHelpSetPath + ".",
                e);
        }
        return helpBroker;
    }

    /**
     *  Answers the HelpViewer.
     */
    private static HelpViewer viewer() {
        return getInstance().viewer;
    }

    /**
     * Implements the <code>Preparable</code> interface by prebuilding
     * the <code>HelpViewer</code>.
     */
    public void prepare() {
        LOGGER.info("Prebuilding the help viewer...");
        viewer();
    }

}