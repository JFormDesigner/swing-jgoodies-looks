package com.jgoodies.looks.launcher;

/*
 * Copyright (c) 2003 JGoodies Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of JGoodies Karsten Lentzsch.
 * Use is subject to license terms.
 *
 */
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.jgoodies.clearlook.ClearLookMode;
import com.jgoodies.forms.builder.ButtonBarBuilder;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.plaf.BorderStyle;
import com.jgoodies.plaf.FontSizeHints;
import com.jgoodies.plaf.HeaderStyle;
import com.jgoodies.plaf.plastic.Plastic3DLookAndFeel;
import com.jgoodies.plaf.plastic.PlasticLookAndFeel;
import com.jgoodies.plaf.plastic.PlasticTheme;
import com.jgoodies.swing.application.ResourceIDs;
import com.jgoodies.swing.application.ResourceManager;
import com.jgoodies.swing.help.HelpBroker;
import com.jgoodies.swing.panels.HeaderPanel;
import com.jgoodies.swing.plaf.ExtUIManager;
import com.jgoodies.util.Utilities;

/** 
 * Main page of the JGoodies Looks Demo Launcher. Provides an interface to 
 * choose a look, theme and several optional settings.
 * 
 * @author Karsten Lentzsch
 */
final class LauncherPage extends JPanel {

    private final Launcher launcher;

    private JList lookAndFeelList;
    private JList themeList;
    private JCheckBox systemFontsCheckBox;

    private ButtonGroup fontSizeHintsGroup;
    private JCheckBox useNarrowMarginsCheckBox;
    private JCheckBox tabIconsEnabledCheckBox;
    private ButtonGroup plasticTabStyleGroup;
    private ButtonGroup plasticFocusContrastGroup;
    private ButtonGroup screenResolutionGroup;

    private ButtonGroup menuBarHeaderStyleGroup;
    private ButtonGroup menuBarWindowsBorderStyleGroup;
    private ButtonGroup menuBarPlasticBorderStyleGroup;
    private ButtonGroup menuBarPlastic3DHintGroup;
    private ButtonGroup toolBarHeaderStyleGroup;
    private ButtonGroup toolBarWindowsBorderStyleGroup;
    private ButtonGroup toolBarPlasticBorderStyleGroup;
    private ButtonGroup toolBarPlastic3DHintGroup;
    private ButtonGroup clearLookModeGroup;
    private JComboBox clearLookPolicyComboBox;

    private JTabbedPane tabbedPane;
    private Map helpMap;
    private JButton helpButton;
    private JButton launchButton;

    // Instance Creation ****************************************************

    /**
     * Constructs and builds the <code>LauncherPage</code>.
     */
    LauncherPage(Launcher launcher) {
        this.launcher = launcher;
        initComponents();
        build();
    }

    // Getters Used By the DemoFrame ****************************************

    HeaderStyle menuBarHeaderStyle() {
        return getHeaderStyle(menuBarHeaderStyleGroup);
    }

    BorderStyle menuBarPlasticBorderStyle() {
        return getBorderStyle(menuBarPlasticBorderStyleGroup);
    }

    BorderStyle menuBarWindowsBorderStyle() {
        return getBorderStyle(menuBarWindowsBorderStyleGroup);
    }

    Boolean menuBar3DHint() {
        String name =
            menuBarPlastic3DHintGroup.getSelection().getActionCommand();
        if (name.equals("None"))
            return null;
        else if (name.equals("On"))
            return Boolean.TRUE;
        else
            return Boolean.FALSE;
    }

    HeaderStyle toolBarHeaderStyle() {
        return getHeaderStyle(toolBarHeaderStyleGroup);
    }

    BorderStyle toolBarPlasticBorderStyle() {
        return getBorderStyle(toolBarPlasticBorderStyleGroup);
    }

    BorderStyle toolBarWindowsBorderStyle() {
        return getBorderStyle(toolBarWindowsBorderStyleGroup);
    }

    Boolean toolBar3DHint() {
        String name =
            toolBarPlastic3DHintGroup.getSelection().getActionCommand();
        if (name.equals("None"))
            return null;
        else if (name.equals("On"))
            return Boolean.TRUE;
        else
            return Boolean.FALSE;
    }

    // Getters Used by the Launcher *****************************************

    LookAndFeel selectedLookAndFeel() {
        LookAndFeel laf = (LookAndFeel) lookAndFeelList.getSelectedValue();
        if (laf != null) {
            return laf;
        };
        String className = launcher.defaultLookAndFeelClassName();
        laf = ExtUIManager.createLookAndFeelInstance(className);
        return laf != null ? laf : new Plastic3DLookAndFeel();
    }

    PlasticTheme selectedTheme() {
        return (PlasticTheme) themeList.getSelectedValue();
    }

    Boolean useSystemFonts() {
        return new Boolean(systemFontsCheckBox.isSelected());
    }

    FontSizeHints fontSizeHints() {
        String name = fontSizeHintsGroup.getSelection().getActionCommand();
        return FontSizeHints.valueOf(name);
    }

    boolean useNarrowButtons() {
        return useNarrowMarginsCheckBox.isSelected();
    }

    boolean tabIconsEnabled() {
        return tabIconsEnabledCheckBox.isSelected();
    }

    String plasticTabStyle() {
        return plasticTabStyleGroup.getSelection().getActionCommand();
    }
    
    boolean plasticHighContrastFocusEnabled() {
        return plasticFocusContrastGroup.getSelection().getActionCommand().
        equalsIgnoreCase("high");
    }
    
    Boolean isLowResolution() {
        String name = screenResolutionGroup.getSelection().getActionCommand();
        if (name.equals("System"))
            return null;
        else if (name.equals("Low"))
            return Boolean.TRUE;
        else
            return Boolean.FALSE;
    }

    HeaderStyle getHeaderStyle(ButtonGroup group) {
        String name = group.getSelection().getActionCommand();
        if (name.equals("None"))
            return null;
        else if (name.equals("Single"))
            return HeaderStyle.SINGLE;
        else
            return HeaderStyle.BOTH;
    }

    BorderStyle getBorderStyle(ButtonGroup group) {
        String name = group.getSelection().getActionCommand();
        if (name.equals("None"))
            return null;
        else if (name.equals("Empty"))
            return BorderStyle.EMPTY;
        else if (name.equals("Etched"))
            return BorderStyle.ETCHED;
        else
            return BorderStyle.SEPARATOR;
    }

    ClearLookMode clearLookMode() {
        String name = clearLookModeGroup.getSelection().getActionCommand();
        return ClearLookMode.valueOf(name);
    }

    String clearLookPolicyName() {
        return (String) clearLookPolicyComboBox.getSelectedItem();
    }

    // Building *************************************************************

    /**
     * Creates and configures UI components.
     */
    private void initComponents() {
        tabbedPane = new JTabbedPane();

        lookAndFeelList = new JList(launcher.computeLooks());
        lookAndFeelList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        lookAndFeelList.addListSelectionListener(createLookListener());
        lookAndFeelList.setCellRenderer(createLookRenderer());

        themeList = new JList(launcher.computeThemes());
        themeList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        themeList.setCellRenderer(createThemeRenderer());
        //updateThemeEnablement();

        systemFontsCheckBox = new JCheckBox("Use system fonts");
        systemFontsCheckBox.setSelected(true);

        fontSizeHintsGroup = new ButtonGroup();

        useNarrowMarginsCheckBox = new JCheckBox("Use narrow margins");
        tabIconsEnabledCheckBox =
            new JCheckBox("Enable icons in JTabbedPane");
        tabIconsEnabledCheckBox.setSelected(true);
        plasticTabStyleGroup = new ButtonGroup();
        plasticFocusContrastGroup = new ButtonGroup();
        screenResolutionGroup = new ButtonGroup();


        menuBarHeaderStyleGroup = new ButtonGroup();
        menuBarWindowsBorderStyleGroup = new ButtonGroup();
        menuBarPlasticBorderStyleGroup = new ButtonGroup();
        menuBarPlastic3DHintGroup = new ButtonGroup();
        toolBarHeaderStyleGroup = new ButtonGroup();
        toolBarWindowsBorderStyleGroup = new ButtonGroup();
        toolBarPlasticBorderStyleGroup = new ButtonGroup();
        toolBarPlastic3DHintGroup = new ButtonGroup();
        clearLookModeGroup = new ButtonGroup();

        clearLookPolicyComboBox =
            new JComboBox(
                new String[] {
                    "com.jgoodies.clearlook.DefaultClearLookPolicy",
                    "com.jgoodies.clearlook.NetBeansClearLookPolicy",
                    });
        clearLookPolicyComboBox.setEditable(true);

        helpButton = new JButton("Help");
        helpButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Object tab = tabbedPane.getSelectedComponent();
                URL url = (URL) helpMap.get(tab);
                HelpBroker.openURL(url);
            }
        });
        launchButton = new JButton("Launch Demo");
        launchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                launcher.doLaunch();
            }
        });
    }

    /**
     * Builds and answers the content: 
     */
    private void build() {
        FormLayout layout = 
            new FormLayout(
                "4dlu, fill:pref:grow, 4dlu",
                "pref, 4dlu, fill:pref:grow, 6dlu, pref, 5dlu");
        PanelBuilder builder = new PanelBuilder(this, layout);
        CellConstraints cc = new CellConstraints();
        builder.add(buildHeader(),     cc.xywh(1, 1, 3, 1));
        builder.add(buildTabbedPane(), cc.xy  (2, 3));
        builder.add(buildButtonBar(),  cc.xy  (2, 5));
    }

    private Component buildHeader() {
        return new HeaderPanel(
            "The JGoodies Looks Demo",
            "Choose a look, set options, then launch the demo."
                + "\nEach tab provides a context-sensitive help button.",
            ResourceManager.getIcon(ResourceIDs.ABOUT_ICON),
            80);
    }

    private Component buildTabbedPane() {
        helpMap = new HashMap();
        addTab(
            tabbedPane,
            buildSwingSettingsTab(),
            "Swing Settings",
            "swing");
        addTab(
            tabbedPane,
            buildGlobalOptionsTab(),
            "Global Options",
            "globals");
        addTab(tabbedPane, buildBarOptionsTab(), "Menu- & Tool Bar", "bars");
        addTab(
            tabbedPane,
            buildClearLookOptionsTab(),
            "ClearLook",
            "clearlook");
        return tabbedPane;
    }

    /**
     * Adds a card to the given JTabbedPane using the specified data.
     */
    private void addTab(
        JTabbedPane parent,
        JComponent card,
        String label,
        String help) {
        parent.add(card, label);
        String fullPath = "docs/help/launcher/" + help + ".html";
        URL url = ResourceManager.getURL(fullPath);
        helpMap.put(card, url);
    }

    /**
     * Creates and answers the button bar with help and launch button.
     */
    private JComponent buildButtonBar() {
        ButtonBarBuilder builder = new ButtonBarBuilder();
        builder.addGridded(helpButton);
        builder.addUnrelatedGap();
        builder.addGlue();
        builder.addGridded(launchButton);
        return builder.getPanel();
    }

    // Building Tabs ********************************************************************

    private JComponent buildSwingSettingsTab() {
        FormLayout fl =
            new FormLayout(
                "7dlu, left:max(50dlu;pref), 4dlu, pref:grow",
                "pref, 3dlu, top:40dlu:grow, 4dlu, top:40dlu:grow, 4dlu, pref");

        PanelBuilder builder = new PanelBuilder(fl);
        builder.setDefaultDialogBorder();

        builder.addSeparator("General Swing Settings", "1, 1, 4, 1");
        builder.addLabel("Look & Feel:", "2, 3");
        builder.add(new JScrollPane(lookAndFeelList), "4, 3, fill, fill");

        builder.addLabel("Plastic Theme:", "2, 5");
        builder.add(new JScrollPane(themeList), "4, 5, fill, fill");
        preselectLookAndTheme();

        builder.addLabel("System Fonts:", "2, 7");
        builder.add(systemFontsCheckBox, "4, 7");
        return builder.getPanel();
    }

    private JComponent buildGlobalOptionsTab() {
        FormLayout fl =
            new FormLayout(
                "7dlu, left:max(50dlu;pref), 4dlu, max(36dlu;p), 1dlu, p, 1dlu, p, 1dlu, p, 4dlu, pref:grow",
                "pref, 2dlu, pref, 4dlu, pref, 4dlu, pref, 11dlu, "
              + "pref, 2dlu, pref, 4dlu, pref, 11dlu, "
              + "pref, 2dlu, pref, 0:grow");
        fl.setColumnGroups(new int[][] { { 4, 6, 8, 10, 12 }
        });
        PanelBuilder builder = new PanelBuilder(fl);
        builder.setDefaultDialogBorder();

        builder.addSeparator("Global Options", "1, 1, 12, 1");

        builder.addLabel("Font Size Hints:", "2, 3");
        addRadio("System", fontSizeHintsGroup, builder, 4, 3);
        addRadio("Large", fontSizeHintsGroup, builder, 6, 3);
        addRadio("Mixed", fontSizeHintsGroup, builder, 8, 3).setSelected(
            true);
        addRadio("Fixed", fontSizeHintsGroup, builder, 10, 3);
        addRadio("Small", fontSizeHintsGroup, builder, 12, 3);

        builder.addLabel("Button Margins:", "2, 5");
        builder.add(useNarrowMarginsCheckBox, "4, 5, 5, 1");

        builder.addLabel("Tab Icons:", "2, 7");
        builder.add(tabIconsEnabledCheckBox, "4, 7, 5, 1");


        builder.addSeparator("Plastic Options", "1, 9, 12, 1");

        boolean useMetalTabs =
            PlasticLookAndFeel.getTabStyle().equalsIgnoreCase("Metal");
        builder.addLabel("Tab Style:", "2, 11");
        addRadio("Default", plasticTabStyleGroup, builder, 4, 11).setSelected(
            !useMetalTabs);
        addRadio("Metal", plasticTabStyleGroup, builder, 6, 11).setSelected(
            useMetalTabs);

        boolean highContrastEnabled =
            PlasticLookAndFeel.getHighContrastFocusColorsEnabled();
        builder.addLabel("Focus Contrast:", "2, 13");
        addRadio("Low", plasticFocusContrastGroup, builder, 4, 13).setSelected(
            !highContrastEnabled);
        addRadio(
            "High",
            plasticFocusContrastGroup,
            builder,
            6,
            13).setSelected(
            highContrastEnabled);


        builder.addSeparator(
            "Simulate Screen Resolution (1.3 only)",
            "1, 15, 12, 1");

        builder.addLabel("Resolution:", "2, 17");
        JRadioButton radio;
        radio = addRadio("System", screenResolutionGroup, builder, 4, 17);
        radio.setSelected(true);
        radio.setEnabled(Utilities.IS_BEFORE_14);
        radio = addRadio("Low", screenResolutionGroup, builder, 6, 17);
        radio.setEnabled(Utilities.IS_BEFORE_14);
        radio = addRadio("High", screenResolutionGroup, builder, 8, 17);
        radio.setEnabled(Utilities.IS_BEFORE_14);

        return builder.getPanel();
    }

    private JComponent buildBarOptionsTab() {
        FormLayout fl =
            new FormLayout(
                "7dlu, left:max(50dlu;pref), 4dlu, max(36dlu;p), 1dlu, p, 1dlu, p, 1dlu, p, 4dlu, pref:grow",
                "pref, 2dlu, pref, 4dlu, pref, 4dlu, pref, 4dlu, pref, 11dlu, "
                    + "pref, 2dlu, pref, 4dlu, pref, 4dlu, pref, 4dlu, pref, 0:grow");
        fl.setColumnGroups(new int[][] { { 4, 6, 8, 10, 12 }
        });
        PanelBuilder builder = new PanelBuilder(fl);
        builder.setDefaultDialogBorder();

        // Menu Bar Options -------------------------------------------------------------
        builder.addSeparator("Menu Bar Style Options", "1, 1, 12, 1");

        builder.addLabel("Header:", "2, 3");
        addHeaderStyleGroup(builder, 3, menuBarHeaderStyleGroup);

        builder.addLabel("Windows Border:", "2, 5");
        addBorderStyleGroup(builder, 5, menuBarWindowsBorderStyleGroup);

        builder.addLabel("Plastic Border:", "2, 7");
        addBorderStyleGroup(builder, 7, menuBarPlasticBorderStyleGroup);

        builder.addLabel("Plastic 3D Hint:", "2, 9");
        addMenuBar3DHintGroup(builder, 9);

        // Tool Bar Options -------------------------------------------------------------
        builder.addSeparator("Tool Bar Style Options", "1, 11, 12, 1");

        builder.addLabel("Header:", "2, 13");
        addHeaderStyleGroup(builder, 13, toolBarHeaderStyleGroup);

        builder.addLabel("Windows Border:", "2, 15");
        addBorderStyleGroup(builder, 15, toolBarWindowsBorderStyleGroup);

        builder.addLabel("Plastic Border:", "2, 17");
        addBorderStyleGroup(builder, 17, toolBarPlasticBorderStyleGroup);

        builder.addLabel("Plastic 3D Hint:", "2, 19");
        addToolBar3DHintGroup(builder, 19);

        return builder.getPanel();
    }

    private JComponent buildClearLookOptionsTab() {
        FormLayout fl =
            new FormLayout(
                "7dlu, left:max(50dlu;pref), 4dlu, max(36dlu;p), 1dlu, p, 1dlu, p, 1dlu, p, 4dlu, pref:grow",
                "pref, 2dlu, pref, 4dlu, pref, 0:grow");
        fl.setColumnGroups(new int[][] { { 4, 6, 8, 10, 12 }
        });
        PanelBuilder builder = new PanelBuilder(fl);
        builder.setDefaultDialogBorder();

        builder.addSeparator("ClearLook Options", "1, 1, 12, 1");

        builder.addLabel("ClearLook Mode:", "2, 3");
        addClearLookModeGroup(builder, 3);

        builder.addLabel("ClearLook Policy:", "2, 5");
        builder.add(clearLookPolicyComboBox, "4, 5, 7, 1");

        return builder.getPanel();
    }

    private void preselectLookAndTheme() {
        LookAndFeel defaultLaf = UIManager.getLookAndFeel();
        ListModel model = lookAndFeelList.getModel();
        int size = model.getSize();
        for (int i = 0; i < size; i++) {
            LookAndFeel laf = (LookAndFeel) model.getElementAt(i);
            if (laf.getClass().equals(defaultLaf.getClass())) {
                lookAndFeelList.setSelectedIndex(i);
                break;
            }
        }
        // Preselect the default theme.
        PlasticTheme defaultTheme =
            Plastic3DLookAndFeel.createMyDefaultTheme();
        model = themeList.getModel();
        size = model.getSize();
        for (int i = 0; i < size; i++) {
            PlasticTheme theme = (PlasticTheme) model.getElementAt(i);
            if (theme.getClass().equals(defaultTheme.getClass())) {
                themeList.setSelectedIndex(i);
                break;
            }
        }

    }

    private void addMenuBar3DHintGroup(PanelBuilder builder, int row) {
        addRadio(
            "None",
            menuBarPlastic3DHintGroup,
            builder,
            4,
            row).setSelected(
            true);
        addRadio("On", menuBarPlastic3DHintGroup, builder, 6, row);
        addRadio("Off", menuBarPlastic3DHintGroup, builder, 8, row);
    }

    private void addToolBar3DHintGroup(PanelBuilder builder, int row) {
        addRadio(
            "None",
            toolBarPlastic3DHintGroup,
            builder,
            4,
            row).setSelected(
            true);
        addRadio("On", toolBarPlastic3DHintGroup, builder, 6, row);
        addRadio("Off", toolBarPlastic3DHintGroup, builder, 8, row);
    }

    private void addHeaderStyleGroup(
        PanelBuilder builder,
        int row,
        ButtonGroup group) {
        addRadio("None", group, builder, 4, row).setSelected(true);
        addRadio("Single", group, builder, 6, row);
        addRadio("Both", group, builder, 8, row);
    }

    private void addBorderStyleGroup(
        PanelBuilder builder,
        int row,
        ButtonGroup group) {
        addRadio("None", group, builder, 4, row).setSelected(true);
        addRadio("Empty", group, builder, 6, row);
        addRadio("Etched", group, builder, 8, row);
        addRadio("Separator", group, builder, 10, row);
    }

    private void addClearLookModeGroup(PanelBuilder builder, int row) {
        addRadio("Off", clearLookModeGroup, builder, 4, row).setSelected(
            true);
        addRadio("On", clearLookModeGroup, builder, 6, row);
        addRadio("Verbose", clearLookModeGroup, builder, 8, row);
        addRadio("Debug", clearLookModeGroup, builder, 10, row);
    }

    // Helper Methods *******************************************************

    private ListSelectionListener createLookListener() {
        return new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting())
                    return;
                updateThemeEnablement();
            }
        };
    }

    private ListCellRenderer createLookRenderer() {
        return new DefaultListCellRenderer() {
            public Component getListCellRendererComponent(
                JList list,
                Object value,
                int index,
                boolean isSelected,
                boolean cellHasFocus) {
                JLabel label =
                    (JLabel) super.getListCellRendererComponent(
                        list,
                        value,
                        index,
                        isSelected,
                        cellHasFocus);
                LookAndFeel laf = (LookAndFeel) value;
                label.setText(laf.getName());
                return label;
            }
        };
    }

    private ListCellRenderer createThemeRenderer() {
        return new DefaultListCellRenderer() {
            public Component getListCellRendererComponent(
                JList list,
                Object value,
                int index,
                boolean isSelected,
                boolean cellHasFocus) {
                JLabel label =
                    (JLabel) super.getListCellRendererComponent(
                        list,
                        value,
                        index,
                        isSelected,
                        cellHasFocus);
                PlasticTheme theme = (PlasticTheme) value;
                label.setText(theme.getName());
                return label;
            }
        };
    }

    /**
     * Updates the theme comboBox enablement according to the look and feel selection.
     */
    private void updateThemeEnablement() {
        themeList.setEnabled(
            selectedLookAndFeel() instanceof PlasticLookAndFeel);
    }
    
    void ensureThemeIsVisible() {
        int index = themeList.getSelectedIndex();
        if (!themeList.isEnabled() || index == -1)
            return;
        themeList.ensureIndexIsVisible(index);
    }

    // Builder Methods ******************************************************

    /**
     * Creates a JRadioButton for the give label and adds it to the button group.
     */
    private JRadioButton addRadio(
        String label,
        ButtonGroup group,
        PanelBuilder builder,
        int col,
        int row) {
        JRadioButton radio = new JRadioButton(label);
        radio.setActionCommand(label);
        group.add(radio);
        CellConstraints cc = new CellConstraints();
        builder.add(radio, cc.xy(col, row, "l, c"));
        return radio;
    }

}