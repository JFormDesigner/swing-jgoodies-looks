/*
 * Copyright (c) 2001-2007 JGoodies Karsten Lentzsch. All Rights Reserved.
 *
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 * 
 *  o Redistributions of source code must retain the above copyright notice, 
 *    this list of conditions and the following disclaimer. 
 *     
 *  o Redistributions in binary form must reproduce the above copyright notice, 
 *    this list of conditions and the following disclaimer in the documentation 
 *    and/or other materials provided with the distribution. 
 *     
 *  o Neither the name of JGoodies Karsten Lentzsch nor the names of 
 *    its contributors may be used to endorse or promote products derived 
 *    from this software without specific prior written permission. 
 *     
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, 
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR 
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR 
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, 
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, 
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; 
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, 
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE 
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, 
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. 
 */

package com.jgoodies.looks.common;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
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
 * @author  Karsten Lentzsch
 * @version $Revision: 1.4 $
 */

public class ExtBasicMenuItemUI extends BasicMenuItemUI {

    private static final int MINIMUM_WIDTH = 80;

    private MenuItemRenderer renderer;

    public static ComponentUI createUI(JComponent b) {
        return new ExtBasicMenuItemUI();
    }

    protected void installDefaults() {
        super.installDefaults();
        renderer = createRenderer(
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
    
    protected MenuItemRenderer createRenderer(
            JMenuItem menuItem, 
            boolean iconBorderEnabled,
            Font    acceleratorFont,
            Color   selectionForeground,
            Color   disabledForeground,
            Color   acceleratorForeground,
            Color   acceleratorSelectionForeground) {
        return new MenuItemRenderer(
                menuItem,
                iconBorderEnabled(),
                acceleratorFont,
                selectionForeground,
                disabledForeground,
                acceleratorForeground,
                acceleratorSelectionForeground);
    }

}