package com.jgoodies.plaf.plastic;

/*
 * Copyright (c) 2003 JGoodies Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of JGoodies Karsten Lentzsch.  
 * Use is subject to license terms.
 *
 */

import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicButtonListener;
import javax.swing.plaf.metal.MetalRadioButtonUI;

/**
 * The JGoodies PlasticXP Look&amp;Feel implementation of 
 * <code>RadioButtonUI</code>.
 * <p>
 * It differs from its superclass in that it uses a different icon
 * and has an active border that changes when the mouse is over.
 * 
 * @author Karsten Lentzsch
 */
public final class PlasticXPRadioButtonUI extends MetalRadioButtonUI {

    private static final PlasticXPRadioButtonUI INSTANCE =
        new PlasticXPRadioButtonUI();

    public static ComponentUI createUI(JComponent b) {
        return INSTANCE;
    }

    protected BasicButtonListener createButtonListener(AbstractButton b) {
        return new ActiveBasicButtonListener(b);
    }


}
