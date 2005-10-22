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

package com.jgoodies.looks.plastic.theme;

import java.awt.Font;

import javax.swing.plaf.FontUIResource;

import com.jgoodies.looks.LookUtils;

/**
 * Looks up and returns the fonts required for a PlasticTheme.
 * 
 * @author  Karsten Lentzsch
 * @version $Revision: 1.2 $
 */
public final class PlasticWindowsFontDelegate extends PlasticFontDelegate {


//    private void initializeSystemFonts() {
//        Toolkit toolkit = Toolkit.getDefaultToolkit();
//        MenuFont = getDesktopFontValue("win.menu.font", MenuFont, toolkit);
//        FixedControlFont = getDesktopFontValue("win.ansiFixed.font",
//                                               FixedControlFont, toolkit);
//        ControlFont = getDesktopFontValue("win.defaultGUI.font",
//                                          ControlFont, toolkit);
//        MessageFont = getDesktopFontValue("win.messagebox.font",
//                                          MessageFont, toolkit);
//        WindowFont = getDesktopFontValue("win.frame.captionFont",
//                                         WindowFont, toolkit);
//    IconFont    = getDesktopFontValue("win.icon.font",
//                      IconFont, toolkit);
//        ToolTipFont = getDesktopFontValue("win.tooltip.font", ToolTipFont,
//                                          toolkit);
//    }


    // Accessing Fonts ******************************************************

    public FontUIResource getTitleTextFont() {
        if (titleFont == null) {
            titleFont = new FontUIResource(getControlTextFont().deriveFont(Font.BOLD));
        }
        return titleFont;
    }

    public FontUIResource getSubTextFont() {
        if (smallFont == null) {
            Font controlFont = getControlTextFont();
            smallFont = new FontUIResource(
                    controlFont.deriveFont(Font.PLAIN, controlFont.getSize() - 2));
        }
        return smallFont;
    }

    public FontUIResource getSystemTextFont() {
        return getFont();
    }
    
    public FontUIResource getUserTextFont() {
        return getFont();
    }
    

    // Helper Code **********************************************************

    protected FontUIResource getFont() {
        if (null == controlFont)
            controlFont = new FontUIResource(getFont0());

        return controlFont;
    }

    protected Font getFont0() {
        return LookUtils.getWindowsControlFont();
    }
    
    
}