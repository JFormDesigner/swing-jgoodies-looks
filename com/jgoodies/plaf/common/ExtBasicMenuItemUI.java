/*
 * Copyright (c) 2003 JGoodies Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of JGoodies Karsten Lentzsch.  
 * Use is subject to license terms.
 *
 */

package com.jgoodies.plaf.common;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicMenuItemUI;

/**
 * An implementation of <code>MenuItemUI</code> used by the 
 * JGoodies Windows and Plastic looks. 
 * Unlike it's superclass it aligns menu items, uses a slightly
 * smaller gap between text and icon, which you can override
 * in the UI defaults.
 * 
 * @author Karsten Lentzsch
 */

public class ExtBasicMenuItemUI extends BasicMenuItemUI {

    private static final int MINIMUM_WIDTH = 80;

    private MenuItemRenderer renderer;

    public static ComponentUI createUI(JComponent b) {
        return new ExtBasicMenuItemUI();
    }

    protected void installDefaults() {
        super.installDefaults();
        renderer =
            new MenuItemRenderer(
                menuItem,
                iconBorderEnabled(),
                acceleratorFont,
                selectionForeground,
                disabledForeground,
                acceleratorForeground,
                acceleratorSelectionForeground);
        Integer gap =
            (Integer) UIManager.get(getPropertyPrefix() + ".textIconGap");
        defaultTextIconGap = gap != null ? gap.intValue() : 2;
    }

    // RadioButtonMenuItems and CheckBoxMenuItems will override
    protected boolean iconBorderEnabled() {
        return false;
    }

    protected void uninstallDefaults() {
        super.uninstallDefaults();
        renderer = null;
    }

    protected Dimension getPreferredMenuItemSize(
        JComponent c,
        Icon aCheckIcon,
        Icon anArrowIcon,
        int textIconGap) {
        //if (storedCheckIcon == null) replaceIcons();
        Dimension size =
            renderer.getPreferredMenuItemSize(
                c,
                aCheckIcon,
                anArrowIcon,
                textIconGap);
        int width = Math.max(MINIMUM_WIDTH, size.width);
        int height = size.height;
        return new Dimension(width, height);
    }

    protected void paintMenuItem(
        Graphics g,
        JComponent c,
        Icon aCheckIcon,
        Icon anArrowIcon,
        Color background,
        Color foreground,
        int textIconGap) {
        renderer.paintMenuItem(
            g,
            c,
            aCheckIcon,
            anArrowIcon,
            background,
            foreground,
            textIconGap);
    }

}