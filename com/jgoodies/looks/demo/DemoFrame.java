/*
 * Copyright (c) 2003 JGoodies Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of JGoodies Karsten Lentzsch.
 * Use is subject to license terms.
 *
 */

package com.jgoodies.looks.demo;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.metal.DefaultMetalTheme;
import javax.swing.plaf.metal.MetalLookAndFeel;

import com.jgoodies.clearlook.ClearLookManager;
import com.jgoodies.looks.launcher.Launcher;
import com.jgoodies.plaf.LookUtils;
import com.jgoodies.plaf.Options;
import com.jgoodies.plaf.plastic.PlasticLookAndFeel;
import com.jgoodies.plaf.windows.ExtWindowsLookAndFeel;
import com.jgoodies.swing.application.ResourceIDs;
import com.jgoodies.swing.application.ResourceManager;
import com.jgoodies.swing.application.Workbench;
import com.jgoodies.swing.convenience.DefaultAboutDialog;
import com.jgoodies.swing.help.HelpBroker;
import com.jgoodies.swing.panels.HeaderPanel;
import com.jgoodies.util.ScreenUtils;

/** 
 * Demonstrates how to use the jGoodies Looks. Therefore, 
 * it provides several panels, that comprise a variety of 
 * Swing widgets in different configurations.<p>
 * 
 * Also, this frame contains examples for Swing misuse,
 * that can be automatically corrected by ClearLook.
 * 
 * @author Karsten Lentzsch
 */
public final class DemoFrame extends JFrame {

    private static final Dimension PREFERRED_SIZE = LookUtils.isLowRes 
        ? new Dimension(650, 510)
        : new Dimension(730, 560);
        
    private static DemoFrame instance;

    private final Launcher launcher; // Used to request jGoodies Looks Options.

    /**
     * Constructs a <code>DemoFrame</code>, configures the UI, 
     * and builds the content.
     */
    private DemoFrame(Launcher launcher) {
        this.launcher = launcher;
        configureUI();
        build();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    /**
    * Creates and opens a <code>DemoFrame</code> that can access 
    * the given <code>Launcher</code>; uses the specified bounds,
    * if non-null.
    */
    public static DemoFrame openOn(Launcher launcher, Rectangle bounds) {
        instance = new DemoFrame(launcher);
        if (bounds == null) {
            instance.setSize(PREFERRED_SIZE);
            ScreenUtils.locateOnScreenEast(instance);
        } else
            instance.setBounds(bounds);
        instance.setVisible(true);
        return instance;
    }
    
    static DemoFrame getInstance() { return instance; }

    /**
     * Configures the user interface; requests Swing settings and 
     * jGoodies Looks options from the launcher.
     */
    private void configureUI() {
        Options.setDefaultIconSize(new Dimension(18, 18));

        // Set font options		
        UIManager.put(
            Options.USE_SYSTEM_FONTS_APP_KEY,
            launcher.useSystemFonts());
        Options.setGlobalFontSizeHints(launcher.fontSizeHints());
        Options.setUseNarrowButtons(launcher.useNarrowButtons());
        Options.setTabIconsEnabled(launcher.tabIconsEnabled());
        ClearLookManager.setMode(launcher.clearLookMode());
        ClearLookManager.setPolicy(launcher.clearLookPolicyName());

        // Swing Settings
        LookAndFeel selectedLaf = launcher.selectedLookAndFeel();
        if (selectedLaf instanceof PlasticLookAndFeel) {
            PlasticLookAndFeel.setMyCurrentTheme(launcher.selectedTheme());
            PlasticLookAndFeel.setTabStyle(launcher.plasticTabStyle());
            PlasticLookAndFeel.setHighContrastFocusColorsEnabled(
                launcher.plasticHighContrastFocusEnabled());
        } else if (selectedLaf.getClass() == MetalLookAndFeel.class) {
            MetalLookAndFeel.setCurrentTheme(new DefaultMetalTheme());
        }

        // Workaround caching in MetalRadioButtonUI
        JRadioButton radio = new JRadioButton();
        radio.getUI().uninstallUI(radio);
        JCheckBox checkBox = new JCheckBox();
        checkBox.getUI().uninstallUI(checkBox);

        try {
            UIManager.setLookAndFeel(selectedLaf);
        } catch (Exception e) {
            System.out.println("Can't change L&F: " + e);
        }

    }

    /**
     * Builds the <code>DemoFrame</code> using Options from the Launcher.
     */
    private void build() {
        setContentPane(buildContentPane());
        setTitle("Sample Frame - The JGoodies Looks Demo");
        setJMenuBar(new MenuBuilder().buildMenuBar(launcher));
        setIconImage(readImageIcon("eye_16x16.gif").getImage());
    }

    /**
     * Builds and answers the content.
     */
    private JComponent buildContentPane() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(buildToolBar(), BorderLayout.NORTH);
        panel.add(buildMainPanel(), BorderLayout.CENTER);
        return panel;
    }

    // Tool Bar *************************************************************

    /**
     * Builds, configures, and answers the toolbar. Requests
     * HeaderStyle, look-specific BorderStyles, and Plastic 3D Hint from Launcher.
     */
    private Component buildToolBar() {
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        toolBar.putClientProperty("JToolBar.isRollover", Boolean.TRUE);
        // Swing
        toolBar.putClientProperty(
            Options.HEADER_STYLE_KEY,
            launcher.toolBarHeaderStyle());
        toolBar.putClientProperty(
            PlasticLookAndFeel.BORDER_STYLE_KEY,
            launcher.toolBarPlasticBorderStyle());
        toolBar.putClientProperty(
            ExtWindowsLookAndFeel.BORDER_STYLE_KEY,
            launcher.toolBarWindowsBorderStyle());
        toolBar.putClientProperty(
            PlasticLookAndFeel.IS_3D_KEY,
            launcher.toolBar3DHint());

        AbstractButton button;

        toolBar.add(createToolBarButton("backward.gif"));
        button = createToolBarButton("forward.gif");
        button.setEnabled(false);
        toolBar.add(button);
        toolBar.add(createToolBarButton("home.gif"));
        toolBar.addSeparator();
        toolBar.add(createOpenButton());
        toolBar.add(createToolBarButton("print.gif"));
        toolBar.add(createToolBarButton("refresh.gif"));
        toolBar.addSeparator();

        ButtonGroup group = new ButtonGroup();
        button = createToolBarRadioButton("pie_mode.png");
        button.setSelectedIcon(readImageIcon("pie_mode_selected.gif"));
        group.add(button);
        button.setSelected(true);
        toolBar.add(button);

        button = createToolBarRadioButton("bar_mode.png");
        button.setSelectedIcon(readImageIcon("bar_mode_selected.gif"));
        group.add(button);
        toolBar.add(button);

        button = createToolBarRadioButton("table_mode.png");
        button.setSelectedIcon(readImageIcon("table_mode_selected.gif"));
        group.add(button);
        toolBar.add(button);
        toolBar.addSeparator();

        button = createToolBarButton("help.gif");
        button.addActionListener(createHelpActionListener());
        toolBar.add(button);

        toolBar.add(Box.createGlue());

        button = new RolloverCheckButton();
        button.setToolTipText("Shall show border when mouse is over");
        button.setMargin(new Insets(0, 0, 0, 0));
        toolBar.add(button);
        return toolBar;
    }

    private AbstractButton createOpenButton() {
        AbstractButton button = createToolBarButton("open.gif");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new JFileChooser().showOpenDialog(DemoFrame.this);
            }

        });
        return button;
    }

    /** Defines the margin used in toolbar buttons. */
    private static final Insets TOOLBAR_BUTTON_MARGIN =
        new Insets(1, 1, 1, 1);

    /**
     * Creates and answers a <code>JButton</code> 
     * configured for use in a JToolBar.<p>
     * 
     * Superceded by ToolBarButton from the jGoodies UI framework.
     */
    private AbstractButton createToolBarButton(String iconName) {
        JButton button = new JButton(readImageIcon(iconName));
        button.setFocusPainted(false);
        button.setMargin(TOOLBAR_BUTTON_MARGIN);
        return button;
    }

    /**
     * Creates and answers a <code>JToggleButton</code> 
     * configured for use in a JToolBar.<p>
     * 
     * Superceded by ToolBarToggleButton from the jGoodies UI framework.
     */
    private AbstractButton createToolBarRadioButton(String iconName) {
        JToggleButton button = new JToggleButton(readImageIcon(iconName));
        button.setFocusPainted(false);
        button.setMargin(TOOLBAR_BUTTON_MARGIN);
        return button;
    }

    // Tabbed Pane **********************************************************

    /**
     * Builds and answers the tabbed pane.
     */
    private Component buildMainPanel() {
        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        //tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

        addTab(
            tabbedPane,
            "SplitPane",
            "Demos nested split pane borders that can be removed by ClearLook.",
            new SplitTab().build());

        addTab(
            tabbedPane,
            "TabbedPane",
            "Demonstrates optional settings for tabbed panes.",
            new TabTestTab().build());

        addTab(
            tabbedPane,
            "Desktop",
            "Demos a JDesktopPane with two internal frames and a palette.",
            new DesktopTab().build());

        addTab(
            tabbedPane,
            "States",
            "Tests different widget states.",
            new StatesTab().build());

        addTab(
            tabbedPane,
            "HTML Labels",
            "Tests the use of HTML in component labels.",
            new HtmlTab().build());

        addTab(tabbedPane, "Dialogs", 
            "Consists of buttons to open different types of standard dialogs.",
            new DialogsTab().build());
                      
        addTab(
            tabbedPane,
            "Narrow Test",
            "Demos and tests the 'narrowMargin' option using "
                + "different combinations of layout managers and narrow hints.",
            new NarrowTab().build());

        addTab(
            tabbedPane,
            "Alignment",
            "This panel tests alignment of widget outlines and font base lines.",
            new AlignmentTab().build());

        addTab(
            tabbedPane,
            "ClearLook",
            "Demos and tests Swing misuse that can be improved by ClearLook.",
            new ClearLookTab().build());

        tabbedPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        return tabbedPane;
    }

    /**
     * Adds a card to the given JTabbedPane.
     * A future version may use the given description.  
     */
    private void addTab(
        JTabbedPane tabbedPane,
        String label,
        String description,
        JComponent card) {
        tabbedPane.addTab(label, card);
    }

    // Helper Code **********************************************************************

    /*
     * Looks up and answers an icon for the specified filename suffix.<p>
     */
    private ImageIcon readImageIcon(String filename) {
        return ResourceManager.readImageIcon("images/" + filename);
    }

    /**
     * Creates and answers an ActionListener that opens the help viewer.
     */
    static ActionListener createHelpActionListener() {
        return new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                HelpBroker.openDefault();
            }
        };
    }

    /**
     * Creates and answers an ActionListener that opens the about dialog.
     */
    static ActionListener createAboutActionListener() {
        return new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new AboutDialog(instance).open();
            }
        };
    }

    // A helper class that overrides the height of the header panel.
    private static class AboutDialog extends DefaultAboutDialog {

        private AboutDialog(JFrame owner) {
            super(owner);
        }

        protected JComponent buildHeader() {
            return new HeaderPanel(
                "About " + Workbench.getGlobals().getProductName(),
                "You can click on the tabs below to see information\n"
                    + "about the demo's version and the system's state.",
                ResourceManager.getIcon(ResourceIDs.ABOUT_ICON),
                80);
        }

    }

    // Checks that all tool bar buttons have a UIResource border
    private static class RolloverCheckButton extends JButton {

        private boolean checked = false;

        public void paint(Graphics g) {
            if (!checked) {
                checkAndSetResult();
            }
            super.paint(g);
        }

        private void checkAndSetResult() {
            Icon passedIcon =
                ResourceManager.readImageIcon("images/passed.gif");
            Icon failedIcon =
                ResourceManager.readImageIcon("images/failed.gif");

            boolean passed = allButtonBordersAreUIResources();
            setIcon(passed ? passedIcon : failedIcon);
            setText(passed ? "Can Swap L&F" : "Can't Swap L&F");

            checked = true;
        }

        /**
         * Checks and answers whether all button borders implement UIResource.
         */
        private boolean allButtonBordersAreUIResources() {
            JToolBar bar = (JToolBar) getParent();
            for (int i = bar.getComponentCount() - 1; i >= 0; i--) {
                Component child = (Component) bar.getComponent(i);
                if (child instanceof JButton) {
                    Border b = ((JButton) child).getBorder();
                    if (!(b instanceof UIResource))
                        return false;
                }
            }
            return true;
        }

    }

}