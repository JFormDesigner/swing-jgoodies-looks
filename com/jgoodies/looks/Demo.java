/*
 * Copyright (c) 2003 JGoodies Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of JGoodies Karsten Lentzsch.
 * Use is subject to license terms.
 *
 */

package com.jgoodies.looks;

import java.awt.Color;

import com.jgoodies.looks.launcher.Launcher;
import com.jgoodies.plaf.FontSizeHints;
import com.jgoodies.plaf.Options;
import com.jgoodies.splash.Splash;
import com.jgoodies.swing.application.AbstractMainFrame;
import com.jgoodies.swing.application.Globals;
import com.jgoodies.swing.convenience.DefaultApplicationStarter;
import com.jgoodies.swing.convenience.ImageSplash;

/**
 * The main class of the JGoodies Looks Demo application.
 *
 * @author Karsten Lentzsch
 */

public final class Demo extends DefaultApplicationStarter {

    protected void configureSplash() {
        super.configureSplash();
        ImageSplash blueSplash = (ImageSplash) Splash.getProvider();
        blueSplash.setForeground(new Color(82, 82, 140));
        blueSplash.setBackground(new Color(189, 193, 205));
    }

    /**
     * Configures the user interface.
     */
    protected void configureUI() {
        Options.setGlobalFontSizeHints(FontSizeHints.MIXED);
        super.configureUI();
    }

    /**
     * Writes several font desktop properties to the console.
     */
    /*
    private void logDesktopFontProperties() {
    	Toolkit toolkit = Toolkit.getDefaultToolkit();
    	System.out.println("Menu font      =" + toolkit.getDesktopProperty("win.menu.font"));		
    	System.out.println("Fixed ctrl.font=" + toolkit.getDesktopProperty("win.ansi.font"));		
    	System.out.println("Control font   =" + toolkit.getDesktopProperty("win.ansiVar.font"));		
    	System.out.println("Messagebox font=" + toolkit.getDesktopProperty("win.messagebox.font"));		
    }
    */

    /**
     * Creates and answers the application's main frame.
     */
    protected AbstractMainFrame createMainFrame() {
        return new Launcher();
    }

    /**
     * Initializes the actions used in this demo.
     */
    protected void initializeActions() {}

    public static void main(String[] arguments) {
        new Demo().boot(
            new Globals(
                "The JGoodies Looks Demo",
                "looksdemo",
                "1.1 pre",
                "1.1 preview build 30",
                "Test Drive the JGoodies Looks",
                "\u00a9 2003",
                "JGoodies Karsten Lentzsch",
                "com/jgoodies/looks/",
                "docs/help/Help.hs",
                "",
                "http://www.JGoodies.com/",
                "info@JGoodies.com"));
    }

}