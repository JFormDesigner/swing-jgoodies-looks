/*
 * Copyright (c) 2001-2004 JGoodies Karsten Lentzsch. All Rights Reserved.
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

package com.jgoodies.plaf;

import java.awt.Font;

import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;

/**
 * Provides convenience behavior to set font defaults.
 * Used by the JGoodies look&amp;feel implementations.
 *
 * @author Karsten Lentzsch
 * @version $Revision: 1.9 $
 * 
 * @see FontSizeHints
 */
 
public final class FontUtils {
	
	private FontUtils() {
        // Override default constructor; prevents instantiation.
    }
	

	/**
	 * Checks and answers if we shall use system font settings.
	 * Using the fonts set by the user can potentially cause
	 * performance and compatibility issues, so we allow this feature
	 * to be switched off either at runtime or programmatically.<p>
	 * 
	 * First checks whether system fonts have been explicitly turned
	 * off in the system properties. Then checks whether a property
	 * has been set in the UIManager. 
	 *
     * @return true if system fonts shall be used
	 */	
    public static boolean useSystemFontSettings() {
        String systemFonts = LookUtils
                .getSystemProperty(Options.USE_SYSTEM_FONTS_KEY);
        if ("false".equalsIgnoreCase(systemFonts))
            return false;

        Object value = UIManager.get(Options.USE_SYSTEM_FONTS_APP_KEY);
        return !Boolean.FALSE.equals(value);
    }

	
	
	/**
	 * Sets different fonts to all known widget defaults.
	 * If the specified <code>menuFont</code> is null,
	 * the given defaults won't be overriden.
     * 
     * @param table   the UIDefaults table used to set fonts
     * @param controlFont       the control font to be set
     * @param controlBoldFont   a bold version of the control font
     * @param fixedControlFont  a fixed size control font
     * @param menuFont          the font used for menus
     * @param messageFont       the font used in message
     * @param toolTipFont       the font used in tool tips
     * @param windowFont        the general dialog font
	 */
	public static void initFontDefaults(UIDefaults table, 
		Object controlFont, Object controlBoldFont, Object fixedControlFont, 
		Object menuFont, Object messageFont, Object toolTipFont, Object windowFont) {
			
//		LookUtils.log("Menu font   =" + menuFont);			
//		LookUtils.log("Control font=" + controlFont);	
//		LookUtils.log("Message font=" + messageFont);	
		
		Object[] defaults = {
				"Button.font",							controlFont,
				"CheckBox.font",						controlFont,
				"ColorChooser.font",					controlFont,
				"ComboBox.font",						controlFont,
				"EditorPane.font",						controlFont,
                "FormattedTextField.font",              controlFont,
				"Label.font",							controlFont,
				"List.font",							controlFont,
				"Panel.font",							controlFont,
				"PasswordField.font",					controlFont,
				"ProgressBar.font",						controlFont,
				"RadioButton.font",						controlFont,
				"ScrollPane.font",						controlFont,
				"Spinner.font",							controlFont,
				"TabbedPane.font",						controlFont,
				"Table.font",							controlFont,
				"TableHeader.font",						controlFont,
				"TextField.font",						controlFont,
				"TextPane.font",						controlFont,
				"ToolBar.font",							controlFont,
				"ToggleButton.font",					controlFont,
				"Tree.font",							controlFont,
				"Viewport.font", 						controlFont,

            	"InternalFrame.titleFont", 				windowFont, // controlBold
	    		"OptionPane.font", 						messageFont,
	    		"OptionPane.messageFont", 				messageFont,
	    		"OptionPane.buttonFont", 				messageFont,
				"Spinner.font",							fixedControlFont,
				"TextArea.font",						fixedControlFont,  
				"TitledBorder.font",					controlBoldFont,
				"ToolTip.font",							toolTipFont,
				};
		table.putDefaults(defaults);
		
		if (menuFont != null) {
			Object[] menuDefaults = {
				"CheckBoxMenuItem.font",				menuFont,
				"CheckBoxMenuItem.acceleratorFont",		menuFont,  // 1.3 only ?
				"Menu.font",							menuFont,
				"Menu.acceleratorFont",					menuFont,
				"MenuBar.font",							menuFont,
				"MenuItem.font",						menuFont,
				"MenuItem.acceleratorFont",				menuFont,
				"PopupMenu.font",						menuFont,
				"RadioButtonMenuItem.font",				menuFont,
				"RadioButtonMenuItem.acceleratorFont",	menuFont,   // 1.3 only ?
			};
			table.putDefaults(menuDefaults);
		}
	}
	
	
	/**
	 * Computes and answers the menu font using the specified
	 * <code>UIDefaults</code> and <code>FontSizeHints</code>.<p>
	 * 
	 * The defaults can be overriden using the system property "jgoodies.menuFont".
	 * You can set this property either by setting VM runtime arguments, e.g.
	 * <pre>
	 *   -Djgoodies.menuFont=Tahoma-PLAIN-11
	 * </pre>
	 * or by setting them during the application startup process, e.g.
	 * <pre>
	 *   System.setProperty(Options.MENU_FONT_KEY, "dialog-BOLD-12");
	 * </pre>
     * 
     * @param table   the UIDefaults table to work with
     * @param hints   the FontSizeHints used to determine the menu font
     * @return the menu font for the given defaults and hints
	 */
	public static Font getMenuFont(UIDefaults table, FontSizeHints hints) {
		// Check whether a concrete font has been specified in the system properties.
		String fontDescription = LookUtils.getSystemProperty(Options.MENU_FONT_KEY);
		if (fontDescription != null) {
			return Font.decode(fontDescription);
		}
		
		Font menuFont= table.getFont("Menu.font");
		if (menuFont.getName().equals("Tahoma")) {
			float size		= menuFont.getSize() + hints.menuFontSizeDelta();
			float minSize	= hints.menuFontSize();
			menuFont		= menuFont.deriveFont(Math.max(minSize, size));
		}
		
		return new FontUIResource(menuFont);
	}
	
	
	/**
	 * Computes and answers the control font using the specified
	 * <code>UIDefaults</code> and <code>FontSizeHints</code>.<p>
	 * 
	 * The defaults can be overriden using the system property "jgoodies.controlFont".
	 * You can set this property either by setting VM runtime arguments, e.g.
	 * <pre>
	 *   -Djgoodies.controlFont=Tahoma-PLAIN-14
	 * </pre>
	 * or by setting them during the application startup process, e.g.
	 * <pre>
	 *   System.setProperty(Options.CONTROL_FONT_KEY, "Arial-ITALIC-12");
	 * </pre>
     * 
     * @param table   the UIDefaults table to work with
     * @param hints   the FontSizeHints used to determine the control font
     * @return the control font for the given defaults and hints
	 */
	public static Font getControlFont(UIDefaults table, FontSizeHints hints) {
		// Check whether a concrete font has been specified in the system properties.
		String fontDescription = LookUtils.getSystemProperty(Options.CONTROL_FONT_KEY);
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