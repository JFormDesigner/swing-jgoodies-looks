/*
 * Copyright (c) 2001-2005 JGoodies Karsten Lentzsch. All Rights Reserved.
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

package com.jgoodies.looks.plastic;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;

import javax.swing.JComponent;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;

import com.jgoodies.looks.common.ExtBasicArrowButtonHandler;


/**
 * The JGoodies PlasticXP Look&amp;Feel implementation of <code>SpinnerUI</code>.
 * Configures the default editor to adjust font baselines and component
 * bounds. Also, changes the border of the buttons and the size of the arrows. 
 * 
 * @author Karsten Lentzsch
 * @version $Revision: 1.3 $
 */
public final class PlasticXPSpinnerUI extends PlasticSpinnerUI {
	
	public static ComponentUI createUI(JComponent b) {
		return new PlasticXPSpinnerUI();
	}


    /**
     * The mouse/action listeners that are added to the spinner's 
     * arrow buttons.  These listeners are shared by all 
     * spinner arrow buttons.
     * 
     * @see #createNextButton
     * @see #createPreviousButton
     */
    private static final ExtBasicArrowButtonHandler nextButtonHandler     
    							= new ExtBasicArrowButtonHandler("increment", true);
    private static final ExtBasicArrowButtonHandler previousButtonHandler 
    							= new ExtBasicArrowButtonHandler("decrement", false);


    /**
     * Create a component that will replace the spinner models value
     * with the object returned by <code>spinner.getPreviousValue</code>.
     * By default the <code>previousButton</code> is a JButton
     * who's <code>ActionListener</code> updates it's <code>JSpinner</code>
     * ancestors model.  If a previousButton isn't needed (in a subclass)
     * then override this method to return null.
     *
     * @return a component that will replace the spinners model with the
     *     next value in the sequence, or null
     * @see #installUI
     * @see #createNextButton
     */
    protected Component createPreviousButton() {
        return new SpinnerXPArrowButton(SwingConstants.SOUTH, previousButtonHandler);
    }


    /**
     * Create a component that will replace the spinner models value
     * with the object returned by <code>spinner.getNextValue</code>.
     * By default the <code>nextButton</code> is a JButton
     * who's <code>ActionListener</code> updates it's <code>JSpinner</code>
     * ancestors model.  If a nextButton isn't needed (in a subclass)
     * then override this method to return null.
     *
     * @return a component that will replace the spinners model with the
     *     next value in the sequence, or null
     * @see #installUI
     * @see #createPreviousButton
     */
    protected Component createNextButton() {
        return new SpinnerXPArrowButton(SwingConstants.NORTH, nextButtonHandler);
    }


    /**
     * It differs from its superclass in that it uses the same formula as JDK
     * to calculate the arrow height.
     */
    private static final class SpinnerXPArrowButton extends PlasticArrowButton {

        private SpinnerXPArrowButton(int direction,
                ExtBasicArrowButtonHandler handler) {
            super(direction, UIManager.getInt("ScrollBar.width") - 2, false);
            addActionListener(handler);
            addMouseListener(handler);
        }

        protected int calculateArrowHeight(int height, int width) {
            int arrowHeight = Math.min((height - 4) / 3, (width - 4) / 3);
            return Math.max(arrowHeight, 3);
        }
                
        protected boolean isPaintingNorthBottom() {
            return true;
        }
        
        protected void paintNorth(Graphics g, boolean leftToRight, boolean isEnabled, 
                Color arrowColor, boolean isPressed, 
                int width, int height, int w, int h, int arrowHeight, int arrowOffset,
                boolean paintBottom) {
                if (!isFreeStanding) {
                    height += 1;
                    g.translate(0, -1);
                    if (!leftToRight) {
                        width += 1;
                        g.translate(-1, 0);
                    } else {
                        width += 2;
                    }
                }
                
                // Draw the arrow
                g.setColor(arrowColor);
                int startY = ((h + 1) - arrowHeight) / 2;  // KL was (h + 1)
                int startX = w / 2;
                // System.out.println( "startX :" + startX + " startY :"+startY);
                for (int line = 0; line < arrowHeight; line++) {
                    g.fillRect(startX - line - arrowOffset, startY + line, 2*(line + 1), 1);
                }
                
                if (isEnabled) {
                    Color shadowColor = UIManager.getColor("ScrollBar.darkShadow");

                    g.setColor(shadowColor);
                    g.drawLine(0, 0, width - 2, 0);
                    g.drawLine(0, 0, 0, height - 1);
                    g.drawLine(width - 2, 1, width - 2, height - 1);
                    if (paintBottom) {
                        g.fillRect(0, height - 1, width - 1, 1);
                    }
                } else {
                    PlasticUtils.drawDisabledBorder(g, 0, 0, width, height + 1);
                    if (paintBottom) {
                        g.setColor(PlasticLookAndFeel.getControlShadow());
                        g.fillRect(0, height - 1, width - 1, 1);
                    }
                }
                if (!isFreeStanding) {
                    height -= 1;
                    g.translate(0, 1);
                    if (!leftToRight) {
                        width -= 1;
                        g.translate(1, 0);
                    } else {
                        width -= 2;
                    }
                }
            }

        protected void paintSouth(Graphics g, boolean leftToRight, boolean isEnabled,
                Color arrowColor, boolean isPressed, 
                int width, int height, int w, int h, int arrowHeight, int arrowOffset) {
                    
                if (!isFreeStanding) {
                    height += 1;
                    if (!leftToRight) {
                        width += 1;
                        g.translate(-1, 0);
                    } else {
                        width += 2;
                    }
                }
                
                // Draw the arrow
                g.setColor(arrowColor);
                
                int startY = (((h + 0) - arrowHeight) / 2) + arrowHeight - 1; // KL was h + 1
                int startX = w / 2;
                
                //      System.out.println( "startX2 :" + startX + " startY2 :"+startY);
                
                for (int line = 0; line < arrowHeight; line++) {
                    g.fillRect(startX - line - arrowOffset, startY - line, 2*(line + 1), 1);
                }
                
                if (isEnabled) {
                    Color shadowColor = UIManager.getColor("ScrollBar.darkShadow");
                    g.setColor(shadowColor);
                    g.drawLine(0, 0, 0, height - 2);
                    g.drawLine(width - 2, 0, width - 2, height - 2);
                    //g.drawLine(1, height - 2, width - 2, height - 2);
                } else {
                    PlasticUtils.drawDisabledBorder(g, 0, -1, width, height + 1);
                }
                
                if (!isFreeStanding) {
                    height -= 1;
                    if (!leftToRight) {
                        width -= 1;
                        g.translate(1, 0);
                    } else {
                        width -= 2;
                    }
                }
            }
    
    }

}