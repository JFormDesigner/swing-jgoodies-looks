package com.jgoodies.plaf.plastic;

/*
 * Copyright (c) 2003 JGoodies Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of JGoodies Karsten Lentzsch.  
 * Use is subject to license terms.
 *
 */

import java.awt.event.MouseEvent;

import javax.swing.AbstractButton;
import javax.swing.plaf.basic.BasicButtonListener;

/**
 * Unlike its superclass this class sets the armed state when the mouse
 * is over the button, where the 1.4 superclass sets the armed state only
 * if the button is pressed.
 * 
 * @author Karsten Lentzsch
 */

final class ActiveBasicButtonListener extends BasicButtonListener {
    
    private boolean mouseOver;
    
    ActiveBasicButtonListener(AbstractButton b) {
        super(b);
        mouseOver = false;
    }
    
    public void mouseEntered(MouseEvent e){
        super.mouseEntered(e);
        AbstractButton button = (AbstractButton) e.getSource();
        button.getModel().setArmed(mouseOver = true);
    }
    
    public void mouseExited(MouseEvent e){
        super.mouseExited(e);
        AbstractButton button = (AbstractButton) e.getSource();
        button.getModel().setArmed(mouseOver = false);
    }

    public void mouseReleased(MouseEvent e){
        super.mouseReleased(e);
        AbstractButton button = (AbstractButton) e.getSource();
        button.getModel().setArmed(mouseOver);
    }
    
}