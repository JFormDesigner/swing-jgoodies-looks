package com.jgoodies.swing.plaf;

/*
 * Copyright (c) 2003 JGoodies Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of JGoodies Karsten Lentzsch.
 * Use is subject to license terms.
 *
 */

import java.awt.Component;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.plaf.metal.MetalTheme;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.plaf.plastic.PlasticLookAndFeel;
import com.jgoodies.plaf.plastic.PlasticTheme;
import com.jgoodies.swing.model.SimpleListModel;
import com.jgoodies.swing.model.Trigger;
import com.jgoodies.util.logging.Logger;
import com.jgoodies.lazy.Preparable;

/**
 * A panel for choosing a <code>LookAndFeel</code> and other
 * look configuration settings, e.g. a color theme.
 *
 * @author Karsten Lentzsch
 */

public final class LookAndFeelPanel extends JPanel {

    private static final ListModel PLASTIC_THEMES_MODEL =
        new SimpleListModel(PlasticLookAndFeel.getInstalledThemes());

    private static final ListModel NO_THEMES_MODEL =
        new SimpleListModel(new ArrayList());


    private static List supportedLookAndFeelInstances;

    private final LookConfigurations configurations;

    private JList lafList;
    private JList themesList;
    private ListSelectionListener themeListener;
    private LookAndFeelPreviewPanel previewPanel;

    /**
     * Constructs a <code>LookAndFeelPanel</code> using the given
     * apply trigger, which triggers the UI update.
     */
    public LookAndFeelPanel(Trigger applyTrigger) {
        this.configurations = getClonedLookConfigurations();
        initComponents();
        build();
        registerListeners();
        applyTrigger.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                ExtUIManager.lookConfigurationsModel().setValue(
                    configurations);
            }
        });
    }

    private static LookConfigurations getClonedLookConfigurations() {
        Object storedValue =
            ExtUIManager.lookConfigurationsModel().getValue();
        LookConfigurations storedConfigs = (LookConfigurations) storedValue;
        try {
            return (LookConfigurations) storedConfigs.clone();
        } catch (CloneNotSupportedException e) {
            return new LookConfigurations(UIManager.getLookAndFeel());
        }
    }

    // Public API ***********************************************************

    /**
     * Ensures that the preselections in the look-and-feel list
     * and in the themes list are visible.
     */
    public void ensureSelectionsAreVisible() {
        lafList.ensureIndexIsVisible(lafList.getSelectedIndex());
        themesList.ensureIndexIsVisible(themesList.getSelectedIndex());
    }

    // Building *************************************************************

    private void initComponents() {
        lafList = new JList();
        lafList.setModel(new SimpleListModel(getSupportedLookAndFeelInstances()));
        lafList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        lafList.setCellRenderer(new LooksListCellRenderer());
        selectLook(configurations.getSelectedLook());

        themesList = new JList();
        updateThemesModel();
        selectTheme(getSelectedConfiguration().getTheme());
        themesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        themesList.setCellRenderer(new ThemesListCellRenderer());

        previewPanel = new LookAndFeelPreviewPanel();
    }
    
    /**
     * Builds the panel: look and feel panel on top, and the preview at the bottom.
     */
    private void build() {
        FormLayout fl = new FormLayout(
                "fill:default:grow, 4dlu, fill:default:grow", 
                "pref, 1dlu, fill:max(50dlu;min):grow, 9dlu, pref, 1dlu, pref");
        fl.setColumnGroups(new int[][]{{1, 3}});
        PanelBuilder builder = new PanelBuilder(this, fl);
        builder.setDefaultDialogBorder();
        CellConstraints cc = new CellConstraints();

        builder.addTitle("Look & Feel",          cc.xy  (1, 1));
        builder.addTitle("Themes",               cc.xy  (3, 1));
        builder.add(new JScrollPane(lafList),    cc.xy  (1, 3));
        builder.add(new JScrollPane(themesList), cc.xy  (3, 3));
        builder.addTitle("Preview",              cc.xywh(1, 5, 3, 1));
        builder.add(previewPanel,                cc.xywh(1, 7, 3, 1));
    }

    /**
     * Registers listeners to respond to selections in the
     * look-and-feel and themes lists.
     */
    private void registerListeners() {
        // Listen to selection events.
        lafList.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting())
                    changedLookAndFeel();
            }
        });
        themeListener = new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting())
                    changedTheme();
            }
        };
        // Listen to selection changes
        themesList.addListSelectionListener(themeListener);
    }

    // Updating *************************************************************

    /**
     * The look and feel selection changed; performs the appropriate update actions.
     */
    private void changedLookAndFeel() {
        // Get the (new) selected look and feel.
        LookAndFeel laf = getSelectedLookAndFeel();
        // Make it the selected configuration.
        configurations.setSelectedLook(laf);

        // Update the themes list without firing the change listener.
        themesList.removeListSelectionListener(themeListener);
        updateThemesModel();
        selectTheme(getSelectedConfiguration().getTheme());
        themesList.addListSelectionListener(themeListener);

        updatePreviewPanel();
    }

    /**
     * The theme selection changed; performs the appropriate update actions.
     */
    private void changedTheme() {
        LookAndFeel laf = getSelectedLookAndFeel();
        if (!(laf instanceof PlasticLookAndFeel))
            return;
        putConfiguration(createLookConfiguration());
        updatePreviewPanel();
    }

    /**
     * Selects the specified <code>LookAndFeel</code>.
     */
    private void selectLook(LookAndFeel selectedLook) {
        String lafClassName = selectedLook.getClass().getName();
        ListModel model = lafList.getModel();
        for (int i = 0; i < model.getSize(); i++) {
            LookAndFeel laf = (LookAndFeel) model.getElementAt(i);
            if (lafClassName.equals(laf.getClass().getName())) {
                lafList.setSelectedIndex(i);
                lafList.ensureIndexIsVisible(i);
                break;
            }
        }
    }

    /**
     * Selects the specified theme.
     */
    private void selectTheme(Object selectedTheme) {
        if (selectedTheme == null)
            return;
        String themeClassName = selectedTheme.getClass().getName();
        ListModel model = themesList.getModel();
        for (int i = 0; i < model.getSize(); i++) {
            MetalTheme theme = (MetalTheme) model.getElementAt(i);
            if (themeClassName.equals(theme.getClass().getName())) {
                themesList.setSelectedIndex(i);
                themesList.ensureIndexIsVisible(i);
                break;
            }
        }
    }

    /**
     * Updates the preview panel.
     */
    private void updatePreviewPanel() {
        try {
            // Save the old L&F.
            LookAndFeel oldLaf = UIManager.getLookAndFeel();
            LookAndFeel selectedLaF = getSelectedLookAndFeel();
            PlasticTheme oldTheme = null;

            if (selectedLaF instanceof PlasticLookAndFeel) {
                oldTheme = PlasticLookAndFeel.getMyCurrentTheme();
                PlasticTheme theme = getSelectedTheme();
                if (theme != null)
                    PlasticLookAndFeel.setMyCurrentTheme(theme);
            }

            UIManager.setLookAndFeel(selectedLaF);
            previewPanel.updateAndValidate();

            if (selectedLaF instanceof PlasticLookAndFeel)
                PlasticLookAndFeel.setMyCurrentTheme(oldTheme);

            // Restore the old L&F.
            UIManager.setLookAndFeel(oldLaf);
            previewPanel.updateComponentTree();

        } catch (UnsupportedLookAndFeelException e) {}
    }

    /**
     * Updates the themes model.
     */
    private void updateThemesModel() {
        boolean supportsThemes =
            getSelectedLookAndFeel() instanceof PlasticLookAndFeel;

        ListModel newModel = supportsThemes 
                                ? PLASTIC_THEMES_MODEL 
                                : NO_THEMES_MODEL;
        if (themesList.getModel() != newModel)
            themesList.setModel(newModel);

        themesList.setEnabled(supportsThemes);
    }

    // Accessing the look and feel configurations ***************************

    private LookConfiguration getSelectedConfiguration() {
        return configurations.getSelection();
    }

    private void putConfiguration(LookConfiguration config) {
        configurations.putConfiguration(config);
    }

    private LookConfiguration createLookConfiguration() {
        LookAndFeel laf = getSelectedLookAndFeel();
        Object theme =
            laf instanceof PlasticLookAndFeel ? getSelectedTheme() : null;
        return new LookConfiguration(laf, theme);
    }

    private LookAndFeel getSelectedLookAndFeel() {
        return (LookAndFeel) lafList.getSelectedValue();
    }

    private PlasticTheme getSelectedTheme() {
        return (PlasticTheme) themesList.getSelectedValue();
    }

    // Computing the Supported Looks ****************************************

    /**
     * Answers a cached and lazily instantiated list of supported look and feels.
     */
    private synchronized static java.util.List getSupportedLookAndFeelInstances() {
        if (supportedLookAndFeelInstances == null) {
            supportedLookAndFeelInstances =
                computeSupportedLookAndFeelInstances();
        }
        return supportedLookAndFeelInstances;
    }

    /**
     * Computes the list of supported look and feels.
     */
    private static java.util.List computeSupportedLookAndFeelInstances() {
        UIManager.LookAndFeelInfo[] lafInfos =
            UIManager.getInstalledLookAndFeels();
        java.util.List result = new ArrayList(lafInfos.length);
        for (int i = 0; i < lafInfos.length; i++) {
            String className = lafInfos[i].getClassName();
            LookAndFeel laf =
                ExtUIManager.createLookAndFeelInstance(className);
            if ((laf != null) && (laf.isSupportedLookAndFeel()))
                result.add(laf);
        }
        Collections.sort(result, new Comparator() {
            public int compare(Object o1, Object o2) {
                LookAndFeel laf1 = (LookAndFeel) o1;
                LookAndFeel laf2 = (LookAndFeel) o2;
                return laf1.getName().toUpperCase().compareTo(
                    laf2.getName().toUpperCase());
            }
        });
        return result;
    }

    // Inner Helper Class ***************************************************

    // Renders instances of LookAndFeel displaying their name
    private static class LooksListCellRenderer extends DefaultListCellRenderer {
        public Component getListCellRendererComponent(
                JList list, Object value, int index,
                boolean isSelected,
                boolean cellHasFocus) {
            return super.getListCellRendererComponent(
                list,
                ((LookAndFeel) value).getName(),
                index, isSelected, cellHasFocus);
        }
    }
    
    // Renders instances of MetalTheme
    private static class ThemesListCellRenderer extends DefaultListCellRenderer {
        public Component getListCellRendererComponent(
                JList list, Object value, int index,
                boolean isSelected,
                boolean cellHasFocus) {
            return super.getListCellRendererComponent(
                list,
                (value instanceof MetalTheme
                    ? ((MetalTheme) value).getName()
                    : value),
                index, isSelected, cellHasFocus);
        }
    }
    
    /**
    * An implementation of the <code>Preparable</code> interface 
    * that computes a list of supported <code>LookAndFeel</code> instances.
    */
    public static class EagerInitializer implements Preparable {

        /**
         * Ensures that the supported look and feels are computed and that 
         * the look and feel preview panel has been loaded once.
         */
        public void prepare() {
            Logger.getLogger("LookAndFeelPanel").info(
                "Computing supported look and feels...");
            getSupportedLookAndFeelInstances();
            Logger.getLogger("LookAndFeelPanel").info(
                "Preloading preview panel classes...");
            new LookAndFeelPreviewPanel();
        }
    }

}