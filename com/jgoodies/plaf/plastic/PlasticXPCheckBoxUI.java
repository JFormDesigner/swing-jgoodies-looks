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
import javax.swing.plaf.metal.MetalCheckBoxUI;

/**
 * The JGoodies PlasticXP l&amp;l implementation of <code>CheckBoxUI</code>.
 * Unlike its superclass, it uses a button listener that sets the armed state
 * if the mouse is over.
 * 
 * @author Karsten Lentzsch
 */
public final class PlasticXPCheckBoxUI extends MetalCheckBoxUI {

    private static final PlasticXPCheckBoxUI INSTANCE = new PlasticXPCheckBoxUI();
	
    public static ComponentUI createUI(JComponent b){
        return INSTANCE;
    }
    
    protected BasicButtonListener createButtonListener(AbstractButton b) {
        return new ActiveBasicButtonListener(b);
    }

}
