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

import javax.swing.JComponent;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicToolBarSeparatorUI;

/**
 * A Windows tool bar separator that honors the tool bar's border.
 */
public final class ExtWindowsToolBarSeparatorUI
    extends BasicToolBarSeparatorUI {

    public static ComponentUI createUI(JComponent c) {
        return new ExtWindowsToolBarSeparatorUI();
    }

    public void paint(Graphics g, JComponent c) {
        Color temp = g.getColor();

        Color shadowColor    = UIManager.getColor("ToolBar.shadow");
        Color highlightColor = UIManager.getColor("ToolBar.highlight");

        Dimension size = c.getSize();

        if (((JSeparator) c).getOrientation() == SwingConstants.HORIZONTAL) {
            int x = (size.width / 2) - 1;
            g.setColor(shadowColor);
            g.drawLine(x, 0, x, size.height - 1);
            g.setColor(highlightColor);
            g.drawLine(x + 1, 0, x + 1, size.height - 1);
        } else {
            int y = (size.height / 2) - 1;
            g.setColor(shadowColor);
            g.drawLine(0, y, size.width - 1, y);
            g.setColor(highlightColor);
            g.drawLine(0, y + 1, size.width - 1, y + 1);
        }
        g.setColor(temp);
    }
}