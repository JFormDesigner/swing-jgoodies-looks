package com.jgoodies.swing;

/*
 * Copyright (c) 2002 Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of Karsten Lentzsch.  
 * Use is subject to license terms.
 *
 */
import java.awt.Component;

import javax.swing.JSplitPane;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.BorderUIResource;
import javax.swing.plaf.basic.BasicSplitPaneUI;

/**
 * A <code>JSplitPane</code> that has no borders.
 *
 * @author Karsten Lentzsch
 */

public final class BorderlessSplitPane extends JSplitPane {

    private static final Border EMPTY_BORDER = new EmptyBorder(0, 0, 0, 0);

    /**
     * Constructs a <code>BorderlessSplitPane</code>, 
     * i.e. a <code>JSplitPane</code> that has no borders.
     */
    public BorderlessSplitPane(
        int newOrientation,
        Component newLeftComponent,
        Component newRightComponent) {
        super(newOrientation, newLeftComponent, newRightComponent);
        setBorder(EMPTY_BORDER);
        setOneTouchExpandable(false);
    }

    /**
     * Updates the UI and removes the divider border, which may be added
     * by a L&F at UI installation time.
     */
    public void updateUI() {
        super.updateUI();
        removeDividerBorder();
    }

    /**
     * Removes the divider border.
     */
    private void removeDividerBorder() {
        BasicSplitPaneUI aUI = (BasicSplitPaneUI) getUI();
        aUI.getDivider().setBorder(new BorderUIResource(EMPTY_BORDER));
    }

}