/*
 * Copyright (c) 2003 JGoodies Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of JGoodies Karsten Lentzsch.  
 * Use is subject to license terms.
 *
 */

package com.jgoodies.plaf.windows;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;

import com.sun.java.swing.plaf.windows.WindowsMenuUI;

import com.jgoodies.plaf.common.MenuItemRenderer;

/**
 * The JGoodies Windows look&amp;feel implementation of <code>MenuUI</code>.<p>
 * 
 * It differs from the superclass in that it uses an overhauled menu
 * rendering an aligmnent system. Furthermore, you can set a client property
 * <tt>Options.NO_ICONS_KEY</tt> to indicate that this menu has no icons.
 *
 * @author Karsten Lentzsch
 * @see	com.jgoodies.plaf.Options
 */

public final class ExtWindowsMenuUI extends WindowsMenuUI {

    private static final String MENU_PROPERTY_PREFIX    = "Menu";
    private static final String SUBMENU_PROPERTY_PREFIX = "MenuItem";

    // May be changed to SUBMENU_PROPERTY_PREFIX later
    private String propertyPrefix = MENU_PROPERTY_PREFIX;

    private MenuItemRenderer renderer;

    public static ComponentUI createUI(JComponent b) {
        return new ExtWindowsMenuUI();
    }

    // Install and Uninstall **************************************************

    protected void installDefaults() {
        super.installDefaults();
        if (arrowIcon == null || arrowIcon instanceof UIResource) {
            arrowIcon = UIManager.getIcon("Menu.arrowIcon");
        }
        renderer =
            new MenuItemRenderer(
                menuItem,
                false,
                acceleratorFont,
                selectionForeground,
                disabledForeground,
                acceleratorForeground,
                acceleratorSelectionForeground);
        Integer gap =
            (Integer) UIManager.get(getPropertyPrefix() + ".textIconGap");
        defaultTextIconGap = gap != null ? gap.intValue() : 2;
    }

    protected void uninstallDefaults() {
        super.uninstallDefaults();
        renderer = null;
    }

    protected String getPropertyPrefix() {
        return propertyPrefix;
    }

    protected Dimension getPreferredMenuItemSize(
        JComponent c,
        Icon aCheckIcon,
        Icon anArrowIcon,
        int textIconGap) {

        if (isSubMenu(menuItem)) {
            ensureSubMenuInstalled();
            return renderer.getPreferredMenuItemSize(
                c,
                aCheckIcon,
                anArrowIcon,
                textIconGap);
        } else {
            Dimension size =
                super.getPreferredMenuItemSize(
                    c,
                    aCheckIcon,
                    anArrowIcon,
                    textIconGap);
            int width = size.width;
            int height = size.height;
            if (height % 2 == 1)
                height--;
            return new Dimension(width, height);
        }
    }

    protected void paintMenuItem(
        Graphics g,
        JComponent c,
        Icon aCheckIcon,
        Icon anArrowIcon,
        Color background,
        Color foreground,
        int textIconGap) {
        if (isSubMenu(menuItem)) {
            renderer.paintMenuItem(
                g,
                c,
                aCheckIcon,
                anArrowIcon,
                background,
                foreground,
                textIconGap);
        } else {
            super.paintMenuItem(
                g,
                c,
                aCheckIcon,
                anArrowIcon,
                background,
                foreground,
                textIconGap);
        }
    }

    /**
     * Checks if we have already detected the correct menu type,
     * menu in menu bar vs. sub menu; reinstalls if necessary.
     */
    private void ensureSubMenuInstalled() {
        if (propertyPrefix.equals(SUBMENU_PROPERTY_PREFIX))
            return;
        uninstallDefaults();
        propertyPrefix = SUBMENU_PROPERTY_PREFIX;
        installDefaults();
    }


    // Helper Code **********************************************************

    private boolean isSubMenu(JMenuItem aMenuItem) {
        return !((JMenu) aMenuItem).isTopLevelMenu();
    }


}