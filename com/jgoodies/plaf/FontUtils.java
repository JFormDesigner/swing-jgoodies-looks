package com.jgoodies.plaf;

/*
 * Copyright (c) 2002 Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of Karsten Lentzsch.  
 * Use is subject to license terms.
 *
 */

import java.awt.Font;

import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;
import sun.security.action.GetPropertyAction;

/**
 * Provides convenience behavior to set font defaults.
 * Used by the JGoodies look&amp;feel implementations.
 *
 * @author Karsten Lentzsch
 */
 
public final class FontUtils {
	
	// Override default constructor;
	private FontUtils() {}
	

	/**
	 * Checks and answers if we shall use system font settings.
	 * In 1.3 environments we need to guess the system fonts.<p>
	 * 
	 * Using the fonts set by the user can potentially cause
	 * performance and compatibility issues, so allow this feature
	 * to be switched off either at runtime or programmatically
	 *
	 */	
	public static boolean useSystemFontSettings() {
		String systemFonts = (String) java.security.AccessController.doPrivileged(
            new GetPropertyAction(Options.USE_SYSTEM_FONTS_KEY));
		boolean useSystemFontSettings = 
			systemFonts == null || systemFonts.equals("true");

        if (useSystemFontSettings) {
            Object value = UIManager.get(Options.USE_SYSTEM_FONTS_APP_KEY);
            useSystemFontSettings = 
            	value == null || Boolean.TRUE.equals(value);
        }
		return useSystemFontSettings;
	}
	
	
	/**
	 * Sets different fonts to all known widget defaults.
	 * If the specified <code>menuFont</code> is null,
	 * the given defaults won't be overriden.
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
	 */
	public static Font getMenuFont(UIDefaults table, FontSizeHints hints) {
		// Check whether a concrete font has been specified in the system properties.
		String fontDescription = System.getProperty(Options.MENU_FONT_KEY, null);
		if (fontDescription != null) {
			return Font.decode(fontDescription);
		}
		
		Font menuFont;
		if (LookUtils.IS_BEFORE_14) {
			menuFont = guessFont(hints.menuFontSize());
		} else {
			menuFont = table.getFont("Menu.font");
			if (menuFont.getName().equals("Tahoma")) {
				float size		= menuFont.getSize() + hints.menuFontSizeDelta();
				float minSize	= hints.menuFontSize();
				menuFont		= menuFont.deriveFont(Math.max(minSize, size));
			}
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
	 */
	public static Font getControlFont(UIDefaults table, FontSizeHints hints) {
		// Check whether a concrete font has been specified in the system properties.
		String fontDescription = System.getProperty(Options.CONTROL_FONT_KEY, null);
		if (fontDescription != null) {
			return Font.decode(fontDescription);
		}
		
		Font controlFont;
		if (LookUtils.IS_BEFORE_14) {
			controlFont = guessFont(hints.controlFontSize());
		} else {
			//LookUtils.log("Label.font     =" + table.getFont("Label.font"));			
			//LookUtils.log("Button.font    =" + table.getFont("Button.font"));	
			//LookUtils.log("OptionPane.font=" + table.getFont("OptionPane.font"));	
		
			String fontKey = LookUtils.IS_140 ? "Label.font" : "OptionPane.font";
			controlFont		= table.getFont(fontKey);
			if (controlFont.getName().equals("Tahoma")) {
				float oldSize	= controlFont.getSize();
				float minSize	= hints.controlFontSize();
				float size = oldSize + hints.controlFontSizeDelta();
				controlFont = controlFont.deriveFont(Math.max(minSize, size));
			}
		}
		//System.out.println("Hints font size =" + hints.controlFontSize());
		//System.out.println("Hints size delta =" + hints.controlFontSizeDelta());
		//System.out.println("Control font size=" + controlFont.getSize());		
		return new FontUIResource(controlFont);
	}
	
	
	/**
	 * Makes a good guess for the platform's system font using the specified font size.
	 * Tries to get the MS Tahoma font, that should be present on most Windows platforms; 
	 * it comes with all recent Windows releases and with the MS Office.<p>
	 */
	public static Font guessFont(int size) {
		// Guess the system font name;
		String fontName = LookUtils.isModernWindows() ? "Tahoma" : "dialog";
		
		// Get the font - may fail if the font is absent.
		Font font = new Font(fontName, Font.PLAIN, size);
		
		// Use a fall back, if the desired font is not available.
		if (font == null) {
			font = new Font("dialog", Font.PLAIN, size);
		}
		return font;
	}
	
}