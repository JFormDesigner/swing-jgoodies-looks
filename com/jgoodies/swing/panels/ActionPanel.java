package com.jgoodies.swing.panels;

/*
 * Copyright (c) 2003 JGoodies Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of JGoodies Karsten Lentzsch.
 * Use is subject to license terms.
 *
 */
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;

import com.jgoodies.forms.factories.Borders;
import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.swing.application.ResourceIDs;
import com.jgoodies.swing.application.ResourceManager;
import com.jgoodies.swing.util.CompoundIcon;
import com.jgoodies.swing.util.NullIcon;
import com.jgoodies.swing.util.ToolBarButton;
import com.jgoodies.swing.util.UIFactory;

/**
 * A panel that stacks action components generated from
 * <code>Action</code> instances as decorated components.
 *
 * @author Karsten Lentzsch
 */
public final class ActionPanel extends JPanel {

    private final Icon     defaultIcon;
    private final boolean forceDefaultIcon;


    /**
     * Constructs an <code>ActionPanel</code>.
     */
    public ActionPanel() {
        this(null, false);
    }

    /**
     * Constructs an <code>ActionPanel</code> using the specified default icon
     * and hint, whether we will always force to use the default icon instead
     * of the individual icons as provided by the actions.
     */
    public ActionPanel(Icon defaultIcon, boolean forceDefaultIcon) {
        super(new FormLayout("fill:pref:grow", ""));
        this.forceDefaultIcon = forceDefaultIcon;
        setOpaque(false);
        if (defaultIcon != null)
            this.defaultIcon = defaultIcon;
        else {
            Icon rawDefaultIcon =
                ResourceManager.getIcon(ResourceIDs.ACTIONPANEL_DEFAULT_ICON);
            this.defaultIcon =
                new CompoundIcon(
                    rawDefaultIcon,
                    new NullIcon(new Dimension(14, 10)));
        }
    }

    /**
     * Creates and action component and adds it to the panel.
     */
    public JComponent add(Action action) {
        if (!action.isEnabled())
            return null;
        JComponent actionComponent = createActionComponent(action);
        addAction(actionComponent);
        return actionComponent;
    }

    /**
     * Adds an action component to the panel.
     */
    private void addAction(JComponent actionComponent) {
        FormLayout layout = (FormLayout) getLayout();
        layout.appendRow(FormFactory.UNRELATED_GAP_ROWSPEC);
        layout.appendRow(FormFactory.PREF_ROWSPEC);
        add(actionComponent, new CellConstraints(1, layout.getRowCount()));
    }

    /**
     * Adds a separator component to the panel.
     */
    public void addSeparator() {
        FormLayout layout = (FormLayout) getLayout();
        layout.appendRow(FormFactory.UNRELATED_GAP_ROWSPEC);
    }

    /**
     * Creates and answers an action component.
     */
    private JComponent createActionComponent(Action action) {
        String name     = (String) action.getValue(Action.SHORT_DESCRIPTION);
        String helptext = (String) action.getValue(Action.LONG_DESCRIPTION);

        JButton button = new ToolBarButton(action);
        button.setContentAreaFilled(false);
        button.setIcon(getIconFor(action));
        if (forceDefaultIcon) {
            button.setRolloverIcon(null);
        }
        button.setBorder(Borders.EMPTY_BORDER);

        FormLayout layout = new FormLayout(
                "pref, 3dlu, fill:pref:grow", 
                "pref, 1dlu, pref");
        JPanel panel = new JPanel(layout);
        panel.setOpaque(false);
        CellConstraints cc = new CellConstraints();
        panel.add(button,                           cc.xy(1, 1));
        panel.add(createActionLabel(name, action),  cc.xy(3, 1));
        cc.insets = new Insets(0, 1, 0, 0);  // Optical align fonts
        panel.add(UIFactory.createPlainLabel(helptext, Color.black), 
                  cc.xy(3, 3));

        return panel;
    }

    /**
     * Creates and answers an action label for the given anchor and action.
     */
    private JLabel createActionLabel(String anchor, final Action action) {
        Color foreground = action.isEnabled()
                ? Color.darkGray.brighter()
                : Color.lightGray.darker();
        JLabel label = UIFactory.createBoldLabel(anchor, 6, foreground, true);
        if (!action.isEnabled())
            return label;
        label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        label.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent event) {
                action.actionPerformed(
                    new ActionEvent(
                        ActionPanel.this,
                        ActionEvent.ACTION_PERFORMED,
                        null));
            }
        });
        return label;
    }

    /**
     * Looks up the icon from the given action and return it, or
     * the default icon, in case the action has no individual icon set.
     */
    private Icon getIconFor(Action action) {
        Icon icon = (Icon) action.getValue(Action.SMALL_ICON);
        return forceDefaultIcon || null == icon ? defaultIcon : icon;
    }

}