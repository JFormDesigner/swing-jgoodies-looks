/*
 * Copyright (c) 2003 JGoodies Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of JGoodies Karsten Lentzsch.  
 * Use is subject to license terms.
 *
 */

package com.jgoodies.plaf.plastic;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.plaf.ComponentUI;

import com.jgoodies.plaf.common.ExtBasicMenuUI;

/**
 * The JGoodies Plastic look&amp;feel implementation of <code>MenuUI</code>.
 * <p>
 * It differs from the superclass in that it uses an overhauled menu
 * rendering an aligmnent system. Furthermore, you can set a client property
 * <tt>Options.NO_ICONS_KEY</tt> to indicate that this menu has no icons.
 *
 * @author Karsten Lentzsch
 * @see	com.jgoodies.plaf.Options
 */
public final class PlasticMenuUI extends ExtBasicMenuUI {
	
	public static ComponentUI createUI(JComponent b) {
		return new PlasticMenuUI();
	}
	
	
	/**
	 * Makes the item transparent, if it is not a sub menu and the model is not selected.
	 */
	protected void paintMenuItem(Graphics g, JComponent c, Icon aCheckIcon,
		Icon anArrowIcon, Color background, Color foreground, int textIconGap) {
        JMenuItem b = (JMenuItem) c;

        if (((JMenu) menuItem).isTopLevelMenu()) {
            b.setOpaque(false);
            if (b.getModel().isSelected()) {
                int menuWidth  = menuItem.getWidth();
                int menuHeight = menuItem.getHeight();
                Color oldColor = g.getColor();
                g.setColor(background);
                g.fillRect(0, 0, menuWidth, menuHeight);
                g.setColor(oldColor);
            }
        }
		super.paintMenuItem(g, c, aCheckIcon, anArrowIcon, background, foreground, textIconGap);
	}
	

}