package com.jgoodies.looks.launcher;

/*
 * Copyright (c) 2003 JGoodies Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of JGoodies Karsten Lentzsch.
 * Use is subject to license terms.
 *
 */
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.plaf.metal.MetalLookAndFeel;

import com.jgoodies.clearlook.ClearLookMode;
import com.jgoodies.layout.Resizer;
import com.jgoodies.looks.demo.DemoFrame;
import com.jgoodies.plaf.BorderStyle;
import com.jgoodies.plaf.FontSizeHints;
import com.jgoodies.plaf.HeaderStyle;
import com.jgoodies.plaf.LookUtils;
import com.jgoodies.plaf.Options;
import com.jgoodies.plaf.plastic.Plastic3DLookAndFeel;
import com.jgoodies.plaf.plastic.PlasticLookAndFeel;
import com.jgoodies.plaf.plastic.PlasticTheme;
import com.jgoodies.plaf.plastic.PlasticXPLookAndFeel;
import com.jgoodies.plaf.windows.ExtWindowsLookAndFeel;
import com.jgoodies.swing.application.AbstractMainFrame;
import com.jgoodies.swing.application.Workbench;
import com.jgoodies.swing.plaf.LookChoiceStrategies;
import com.jgoodies.swing.util.MySwingUtilities;
import com.jgoodies.util.ScreenUtils;
import com.jgoodies.util.Utilities;
import com.sun.java.swing.plaf.windows.WindowsLookAndFeel;

/** 
 * Launcher for the jGoodies Looks Demo. Provides an interface
 * to choose a look and theme and many settings.
 * Launches the <code>DemoFrame</code>.
 * 
 * @author Karsten Lentzsch
 */
public final class Launcher extends AbstractMainFrame {

    private static final String KUNSTSTOFF_CLASS_NAME =
        "com.incors.plaf.kunststoff.KunststoffLookAndFeel";

    private static final String FROG_CLASS_NAME =
        "com.sap.plaf.frog.FrogLookAndFeel";

//    private static final String ALLOY_OBFUSCATED_CLASS_NAME =
//        "com.incors.plaf.alloy.bv";

    private LauncherPage   launcherPanel;
    private DemoFrame      demoFrame;
    private WindowListener restoreListener;

    /**
     * Constructs the launcher (main) frame.
     */
    public Launcher() {
        super(Workbench.getGlobals().getWindowTitle());
        setTitlePrefix("Launcher");
        restoreListener = new WindowAdapter() {
            public void windowClosed(WindowEvent e) {
                restoreLauncher();
            }
        };
    }

    /**
     * Opens the frame. In addition to the superclass behavior we ensure that
     * the selection in the theme panel is visible.
     */
    public void open() {
        super.open();
        launcherPanel.ensureThemeIsVisible();
    }
    
    
    /**
     * Builds and answers the content pane; delegated to 
     * the <code>LauncherPanel</code> to enable Applet support later.
     */
    public JComponent buildContentPane() {
        configureUI();
        launcherPanel = new LauncherPage(this);
        return launcherPanel;
    }

    /**
     * Resizes the specified component and  is called during the #build process.
     * Unlike the default, we try to resize to a heigh/width ratio of 1.
     */
    protected void resizeHook(JComponent component) {
        Resizer.ONE2ONE.resize(component);
    }

    /**
     * Locates the frame on the screen. Unlike the default,
     * we locate the launcher on the left side.
     */
    protected void locateOnScreen() {
        ScreenUtils.locateOnScreenWest(this);
    }

    // Launch Process *******************************************************************

    /**
     * Launches the demo frame: stores bounds of an existing frame,
     * configures the demo, opens the demo frame, iconifies the launcher,
     * and finally listens to demo frame close events.
     */
    void doLaunch() {
        Rectangle bounds = null;
        if (demoFrame != null) {
            bounds = demoFrame.getBounds();
            demoFrame.dispose();
        }
        // Simulate screen resolution
        setSimulatedScreenResolution(launcherPanel.isLowResolution());
        demoFrame = DemoFrame.openOn(this, bounds);
        setState(JFrame.ICONIFIED);
        demoFrame.addWindowListener(restoreListener);
    }

    private void setSimulatedScreenResolution(Boolean isLow) {
        boolean systemIsLowRes =
            Toolkit.getDefaultToolkit().getScreenResolution() < 120;
        LookUtils.isLowRes =
            isLow != null ? isLow.booleanValue() : systemIsLowRes;
    }

    /**
     * Restores the launcher to reset the original look&feel.
     */
    private void restoreLauncher() {
        demoFrame.removeWindowListener(restoreListener);
        setSimulatedScreenResolution(null);
        PlasticLookAndFeel.setMyCurrentTheme(
            PlasticLookAndFeel.createMyDefaultTheme());
        configureUI();
        MySwingUtilities.updateAllUIs();
        setState(JFrame.NORMAL);
    }

    // Getters Used By the DemoFrame ****************************************************

    public LookAndFeel selectedLookAndFeel() {
        return launcherPanel.selectedLookAndFeel();
    }

    public PlasticTheme selectedTheme() {
        return launcherPanel.selectedTheme();
    }

    public Boolean useSystemFonts() {
        return launcherPanel.useSystemFonts();
    }

    public FontSizeHints fontSizeHints() {
        return launcherPanel.fontSizeHints();
    }

    public boolean useNarrowButtons() {
        return launcherPanel.useNarrowButtons();
    }

    public boolean tabIconsEnabled() {
        return launcherPanel.tabIconsEnabled();
    }
    
    public String plasticTabStyle() {
        return launcherPanel.plasticTabStyle();
    }
    
    public boolean plasticHighContrastFocusEnabled() {
        return launcherPanel.plasticHighContrastFocusEnabled();
    }

    public HeaderStyle menuBarHeaderStyle() {
        return launcherPanel.menuBarHeaderStyle();
    }

    public BorderStyle menuBarPlasticBorderStyle() {
        return launcherPanel.menuBarPlasticBorderStyle();
    }

    public BorderStyle menuBarWindowsBorderStyle() {
        return launcherPanel.menuBarWindowsBorderStyle();
    }

    public Boolean menuBar3DHint() {
        return launcherPanel.menuBar3DHint();
    }

    public HeaderStyle toolBarHeaderStyle() {
        return launcherPanel.toolBarHeaderStyle();
    }

    public BorderStyle toolBarPlasticBorderStyle() {
        return launcherPanel.toolBarPlasticBorderStyle();
    }

    public BorderStyle toolBarWindowsBorderStyle() {
        return launcherPanel.toolBarWindowsBorderStyle();
    }

    public Boolean toolBar3DHint() {
        return launcherPanel.toolBar3DHint();
    }

    public ClearLookMode clearLookMode() {
        return launcherPanel.clearLookMode();
    }

    public String clearLookPolicyName() {
        return launcherPanel.clearLookPolicyName();
    }

    // UI Configuration for the Launcher Itself *****************************************

    /**
     * Configures the UI before we try to install the look and feel.
     */
    private void configureUI() {
        UIManager.put(Options.USE_SYSTEM_FONTS_APP_KEY, Boolean.TRUE);
        Options.setGlobalFontSizeHints(FontSizeHints.MIXED);
        try {
            UIManager.setLookAndFeel(defaultLookAndFeelClassName());
        } catch (Exception e) {
            System.out.println("Can't change L&F: " + e);
        }
    }

    /**
     * Computes and answers the class name for the default look and feel.
     */
    String defaultLookAndFeelClassName() {
        return LookChoiceStrategies.DEFAULT.getDefaultLookClassName();
    }

    // Helper Methods *******************************************************************

    /**
     * Computes and answers an array of available look&feel implementations.
     */
    LookAndFeel[] computeLooks() {
        List looks = new LinkedList();
        looks.add(new PlasticXPLookAndFeel());
        looks.add(new Plastic3DLookAndFeel());
        looks.add(new PlasticLookAndFeel());
        looks.add(new MetalLookAndFeel());
        if (Utilities.IS_WINDOWS) {
            looks.add(new ExtWindowsLookAndFeel());
            looks.add(new WindowsLookAndFeel());
        }
        try {
            Class kunststoffClass = Class.forName(KUNSTSTOFF_CLASS_NAME);
            looks.add(kunststoffClass.newInstance());
        } catch (Exception e) {}
        try {
            Class frogClass = Class.forName(FROG_CLASS_NAME);
            looks.add(frogClass.newInstance());
        } catch (Exception e) {}
        /*
        try {
        	Class alloyClass = Class.forName(ALLOY_OBFUSCATED_CLASS_NAME);
        	looks.add(alloyClass.newInstance());
        } catch (Exception e) {}
        */
        return (LookAndFeel[]) looks.toArray(new LookAndFeel[looks.size()]);
    }

    /**
     * Computes and answers an array of available Plastic themes.
     */
    PlasticTheme[] computeThemes() {
        List themes = PlasticLookAndFeel.getInstalledThemes();
        return (PlasticTheme[]) themes.toArray(
            new PlasticTheme[themes.size()]);
    }

}