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

import javax.swing.UIDefaults;
import javax.swing.plaf.FontUIResource;

import com.jgoodies.looks.LookUtils;
import com.jgoodies.looks.Options;
import com.jgoodies.looks.plastic.FontSizeHints;

/**
 * Looks up and returns the fonts required for a PlasticTheme.
 * 
 * @author  Karsten Lentzsch
 * @version $Revision: 1.2 $
 */
public class PlasticFontDelegate {

    protected FontUIResource titleFont;
    protected FontUIResource controlFont;
    protected FontUIResource systemFont;
    protected FontUIResource userFont;
    protected FontUIResource smallFont;


    // Accessing Fonts ******************************************************

    public FontUIResource getTitleTextFont() {
//        return getControlTextFont();
        
        if (titleFont == null) {
            titleFont =
                new FontUIResource(
                    Font.getFont(
                        "swing.plaf.metal.controlFont",
                        new Font("Dialog", Font.BOLD, 12)));
        }
        return titleFont;
    }

    public FontUIResource getControlTextFont() {
        return getFont();
    }
    
    public FontUIResource getMenuTextFont() {
        return getFont();
    }
    
    public FontUIResource getSubTextFont() {
        if (smallFont == null) {
            smallFont =
                new FontUIResource(
                    Font.getFont(
                        "swing.plaf.metal.smallFont",
                        new Font("Dialog", Font.PLAIN, 10)));
        }
        return smallFont;
    }

    public FontUIResource getSystemTextFont() {
        if (systemFont == null) {
            systemFont =
                new FontUIResource(
                    Font.getFont(
                        "swing.plaf.metal.systemFont",
                        new Font("Dialog", Font.PLAIN, 12)));
        }
        return systemFont;
    }

    public FontUIResource getUserTextFont() {
        if (userFont == null) {
            userFont =
                new FontUIResource(
                    Font.getFont(
                        "swing.plaf.metal.userFont",
                        new Font("Dialog", Font.PLAIN, 12)));
        }
        return userFont;
    }

    public FontUIResource getWindowTitleFont() {
        return getFont();
    }
    

    // Helper Code **********************************************************

    protected FontUIResource getFont() {
        if (null == controlFont)
            controlFont = new FontUIResource(getFont0());

        return controlFont;
    }

    protected Font getFont0() {
        Font font = Font.getFont("swing.plaf.metal.controlFont");
        return font != null
            ? font.deriveFont(Font.PLAIN)
            : new Font("Dialog", Font.PLAIN, 12);
    }

    /**
     * Computes and answers the control font using the specified
     * <code>UIDefaults</code> and <code>FontSizeHints</code>.<p>
     * 
     * The defaults can be overriden using the system property "jgoodies.controlFont".
     * You can set this property either by setting VM runtime arguments, e.g.
     * <pre>
     *   -Dplastic.controlFont=Tahoma-PLAIN-14
     * </pre>
     * or by setting them during the application startup process, e.g.
     * <pre>
     *   System.setProperty(Options.PLASTIC_CONTROL_FONT_KEY, "Arial-ITALIC-12");
     * </pre>
     * 
     * @param table   the UIDefaults table to work with
     * @param hints   the FontSizeHints used to determine the control font
     * @return the control font for the given defaults and hints
     */
    protected final Font getControlFont(UIDefaults table, FontSizeHints hints) {
    	// Check whether a concrete font has been specified in the system properties.
    	String fontDescription = LookUtils.getSystemProperty(Options.PLASTIC_CONTROL_FONT_KEY);
    	if (fontDescription != null) {
    		return Font.decode(fontDescription);
    	}
    	
    	Font controlFont;
    		//LookUtils.log("Label.font     =" + table.getFont("Label.font"));			
    		//LookUtils.log("Button.font    =" + table.getFont("Button.font"));	
    		//LookUtils.log("OptionPane.font=" + table.getFont("OptionPane.font"));	
    	
    		String fontKey = LookUtils.IS_JAVA_1_4_0 
                ? "Label.font" 
                : "OptionPane.font";
    		controlFont		= table.getFont(fontKey);
    		if (controlFont.getName().equals("Tahoma")) {
    			float oldSize	= controlFont.getSize();
    			float minSize	= hints.controlFontSize();
    			float size = oldSize + hints.controlFontSizeDelta();
    			controlFont = controlFont.deriveFont(Math.max(minSize, size));
    		}
    	//System.out.println("Hints font size =" + hints.controlFontSize());
    	//System.out.println("Hints size delta =" + hints.controlFontSizeDelta());
    	//System.out.println("Control font size=" + controlFont.getSize());		
    	return new FontUIResource(controlFont);
    }

}