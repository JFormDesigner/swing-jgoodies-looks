package com.jgoodies.plaf.plastic;

/*
 * Copyright (c) 2003 JGoodies Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of JGoodies Karsten Lentzsch.  
 * Use is subject to license terms.
 *
 */

import java.awt.Container;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicOptionPaneUI;

import com.jgoodies.plaf.common.ExtButtonAreaLayout;

/**
 * The JGoodies Plastic Look&amp;Feel implementation of 
 * <code>OptionPaneUI</code>. Honors the screen resolution and
 * uses a minimum button with that complies better with the Mac and Windows
 * UI style guides.
 * 
 * @author Karsten Lentzsch
 */
public final class PlasticOptionPaneUI extends BasicOptionPaneUI {

    public static ComponentUI createUI(JComponent b) {
        return new PlasticOptionPaneUI();
    }

    /**
     * Creates and returns a Container containin the buttons. The buttons
     * are created by calling <code>getButtons</code>.
     */
    protected Container createButtonArea() {
        JPanel bottom = new JPanel();
        bottom.setBorder(UIManager.getBorder("OptionPane.buttonAreaBorder"));
        bottom.setLayout(new ExtButtonAreaLayout(true, 6));
        addButtonComponents(bottom, getButtons(), getInitialValueIndex());
        return bottom;
    }

}