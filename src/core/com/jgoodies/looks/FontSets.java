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

package com.jgoodies.looks;

import java.awt.Font;

import javax.swing.plaf.FontUIResource;


/**
 * Provides predefined FontSet implementations.
 *
 * @author  Karsten Lentzsch
 * @version $Revision: 1.1 $
 * 
 * @see     FontSet
 * 
 * @since 2.0
 */ 
public final class FontSets {
	

	private FontSets() {
        // Override default constructor; prevents instantation.
    }
    
    
    // Helper Code ************************************************************
    
    public static final class DefaultFontSet implements FontSet {
        
        private final FontUIResource controlFont;
        private final FontUIResource menuFont;
        private final FontUIResource titleFont;
        private final FontUIResource messageFont;
        private final FontUIResource toolTipFont;
        private final FontUIResource windowTitleFont;
        
        public DefaultFontSet(FontUIResource controlFont, 
                FontUIResource menuFont,
                FontUIResource titleFont, 
                FontUIResource messageFont, 
                FontUIResource toolTipFont, 
                FontUIResource windowTitleFont) {
            this.controlFont = controlFont;
            this.menuFont = menuFont;
            this.titleFont = titleFont;
            this.messageFont = messageFont;
            this.toolTipFont = toolTipFont;
            this.windowTitleFont = windowTitleFont;
        }
        
        public FontUIResource getControlFont() {
            return controlFont;
        }
        
        public FontUIResource getMenuFont() {
            return menuFont;
        }
        
        public FontUIResource getTitleFont() {
            return titleFont;
        }

        public FontUIResource getWindowTitleFont() {
            return windowTitleFont;
        }
        
        public FontUIResource getSmallFont() {
            return toolTipFont;
        }
        
        public FontUIResource getMessageFont() {
            return messageFont;
        }

    }
    
    
    /**
     * Looks up and returns the logical fonts.
     */
    public static final class LogicalFontSet implements FontSet {

        private FontUIResource controlFont;
        private FontUIResource titleFont;
        private FontUIResource systemFont;
        private FontUIResource smallFont;


        public FontUIResource getTitleFont() {
            if (titleFont == null) {
                titleFont =
                    new FontUIResource(
                        getControlFont().deriveFont(Font.BOLD));
            }
            return titleFont;
        }

        public FontUIResource getControlFont() {
            if (controlFont == null) {
                controlFont = new FontUIResource(
                        Font.getFont(
                            "swing.plaf.metal.controlFont",
                            new Font("Dialog", Font.PLAIN, 12)));
                
            }
            return controlFont;
        }
        
        public FontUIResource getMenuFont() {
            return getControlFont();
        }
        
        public FontUIResource getSmallFont() {
            if (smallFont == null) {
                smallFont =
                    new FontUIResource(
                        Font.getFont(
                            "swing.plaf.metal.smallFont",
                            new Font("Dialog", Font.PLAIN, 10)));
            }
            return smallFont;
        }

        public FontUIResource getMessageFont() {
            if (systemFont == null) {
                systemFont =
                    new FontUIResource(
                        Font.getFont(
                            "swing.plaf.metal.systemFont",
                            new Font("Dialog", Font.PLAIN, 12)));
            }
            return systemFont;
        }

        public FontUIResource getWindowTitleFont() {
            return getControlFont();
        }
        

    }    
    
    
}