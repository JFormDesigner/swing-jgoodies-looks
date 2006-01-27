/*
 * Copyright (c) 2001-2006 JGoodies Karsten Lentzsch. All Rights Reserved.
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

package com.jgoodies.looks.windows;

import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;
import java.lang.reflect.Method;

import javax.swing.Icon;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.DimensionUIResource;
import javax.swing.plaf.InsetsUIResource;
import javax.swing.plaf.basic.BasicBorders;

import com.jgoodies.looks.*;
import com.jgoodies.looks.common.MinimumSizedIcon;
import com.jgoodies.looks.common.ShadowPopupFactory;

/**
 * The main class of the JGoodies Windows Look&amp;Feel.
 * This look provides several corrections and extensions to Sun's Windows L&F.
 * In addition it tries to provide a unified look for the J2SE 1.4.0x, 1.4.1x,
 * 1.4.2, and 1.5 environments.
 * 
 * @author Karsten Lentzsch
 * @version $Revision: 1.18 $
 */
public final class WindowsLookAndFeel extends com.sun.java.swing.plaf.windows.WindowsLookAndFeel {

    /** 
     * An optional client property key for JMenu and JToolBar
     * to set a border style - shadows the header style. 
     */
    public static final String BORDER_STYLE_KEY = "jgoodies.windows.borderStyle";
    
    
    public String getID() {
        return "JGoodies Windows";
    }

    
    public String getName() {
        return "JGoodies Windows";
    }

    
    public String getDescription() {
        return "The JGoodies Windows Look and Feel"
                + " - \u00a9 2001-2006 JGoodies Karsten Lentzsch";
    }
    
    
    // Optional Settings ******************************************************
    
    /**
     * Looks up and retrieves the FontChoicePolicy used
     * by the JGoodies Windows Look&amp;Feel.
     * If a FontChoicePolicy has been set for this look, it'll be returned.
     * Otherwise, this method checks if a FontChoicePolicy or FontSet is defined 
     * in the system properties or UIDefaults. If so, it is returned.
     * If no FontChoicePolicy has been set for this look, in the system
     * properties or UIDefaults, the default Windows font choice policy 
     * will be returned.
     * 
     * @return the FontChoicePolicy set for this Look&amp;feel - if any,
     *     the FontChoicePolicy specified in the system properties or UIDefaults
     *     - if any, or the default Windows font choice policy. 
     * 
     * @see #setFontChoicePolicy
     * @see Options#WINDOWS_FONT_CHOICE_POLICY_KEY
     * @see FontChoicePolicies
     * @see FontChoicePolicies#customSettingsPolicy(FontChoicePolicy)
     * @see FontChoicePolicies#getDefaultWindowsPolicy()
     */
    public static FontChoicePolicy getFontChoicePolicy() {
        FontChoicePolicy policy = 
            (FontChoicePolicy) UIManager.get(Options.WINDOWS_FONT_CHOICE_POLICY_KEY);
        if (policy != null)
            return policy;
        
        FontChoicePolicy defaultPolicy = FontChoicePolicies.getDefaultWindowsPolicy();
        return FontChoicePolicies.customSettingsPolicy(defaultPolicy);
    }
    
    
    /**
     * Sets the FontChoicePolicy to be used with the JGoodies Windows L&amp;F.
     * If the specified policy is <code>null</code>, the default will be reset.
     * 
     * @param fontChoicePolicy   the FontChoicePolicy to be used with 
     *     the JGoodies Windows L&amp;F, or <code>null</code> to reset
     *     to the default
     *     
     * @see #getFontChoicePolicy()
     * @see Options#WINDOWS_FONT_CHOICE_POLICY_KEY
     */
    public static void setFontChoicePolicy(FontChoicePolicy fontChoicePolicy) {
        UIManager.put(Options.WINDOWS_FONT_CHOICE_POLICY_KEY, fontChoicePolicy);
    }
    

    // Overriding Superclass Behavior ***************************************

    /**
     * Invoked during <code>UIManager#setLookAndFeel</code>. In addition 
     * to the superclass behavior, we install the ShadowPopupFactory.
     * 
     * @see #uninitialize
     */
    public void initialize() {
        super.initialize();
        ShadowPopupFactory.install();
    }
    
    
    /**
     * Invoked during <code>UIManager#setLookAndFeel</code>. In addition 
     * to the superclass behavior, we uninstall the ShadowPopupFactory.
     * 
     * @see #initialize
     */
    public void uninitialize() {
        super.uninitialize();
        ShadowPopupFactory.uninstall();
    }
    
    
    /**
     * Initializes the class defaults, that is, overrides some UI delegates
     * with JGoodies Windows implementations.
     */
    protected void initClassDefaults(UIDefaults table) {
        super.initClassDefaults(table);
        final String windowsPrefix = "com.jgoodies.looks.windows.Windows";
        final String commonPrefix  = "com.jgoodies.looks.common.ExtBasic";

        String menuUIPrefix = LookUtils.IS_LAF_WINDOWS_XP_ENABLED
                ? windowsPrefix
                : commonPrefix;

        // Overwrite some of the uiDefaults.
        Object[] uiDefaults = {
            // Modified size 
            "ComboBoxUI",            windowsPrefix + "ComboBoxUI", 

            // Modified preferred height: can be even or odd 
            "ButtonUI",              windowsPrefix + "ButtonUI", 

            // Can installs an optional etched border
			"ScrollPaneUI",          windowsPrefix + "ScrollPaneUI", 

            // Optional style and optional special borders
            "MenuBarUI",             windowsPrefix + "MenuBarUI", 

            // Aligned menu items
            "MenuUI",                menuUIPrefix + "MenuUI", 
			"MenuItemUI",            commonPrefix + "MenuItemUI", 
			"CheckBoxMenuItemUI",    commonPrefix + "CheckBoxMenuItemUI", 
			"RadioButtonMenuItemUI", commonPrefix + "RadioButtonMenuItemUI", 

            // Provides an option for a no margin border              
            "PopupMenuUI",           windowsPrefix + "PopupMenuUI",
           
            // Has padding above and below the separator lines				
            "PopupMenuSeparatorUI",  commonPrefix + "PopupMenuSeparatorUI", 

            // Honors the screen resolution and uses a minimum button width             
            "OptionPaneUI",          windowsPrefix + "OptionPaneUI", 

            // 1.4.1 has ugly one touch triangles
            "SplitPaneUI",           windowsPrefix + "SplitPaneUI", 

            // Work in progress: Can have a flat presentation
            "TabbedPaneUI",          windowsPrefix + "TabbedPaneUI", 
            
            // Updates the disabled and inactive background
            "TextAreaUI",            windowsPrefix + "TextAreaUI",

            // Corrected position of the tree button icon
            "TreeUI",                windowsPrefix + "TreeUI",
            
            // Just to use shared UI delegate
            "SeparatorUI",           windowsPrefix + "SeparatorUI"};

        if (LookUtils.IS_JAVA_1_4_2_OR_LATER) {
            // Modified Border
            uiDefaults = append(uiDefaults, 
            "SpinnerUI",             windowsPrefix + "SpinnerUI"); 
        }
        
   
        if (LookUtils.IS_LAF_WINDOWS_XP_ENABLED) {
            // Renders a dot, not the star ("*") character                       
            uiDefaults = append(uiDefaults, 
                "PasswordFieldUI",    windowsPrefix + "XPPasswordFieldUI"); 

            // Optional style and optional special borders; 
            // rollover borders for compound buttons
            uiDefaults = append(uiDefaults, 
                "ToolBarUI",          windowsPrefix + "XPToolBarUI");
            
            // Honors XP table header style for custom user renderers. 
            uiDefaults = append(uiDefaults,
                "TableHeaderUI",      windowsPrefix + "XPTableHeaderUI");            
        } else {
            // Optional style and optional special borders; 
            // rollover borders corrected
            uiDefaults = append(uiDefaults, 
                "ToolBarUI",          windowsPrefix + "ToolBarUI"); 

            // Black arrows
            uiDefaults = append(uiDefaults, 
                "ScrollBarUI",        windowsPrefix + "ScrollBarUI"); 

            if (!LookUtils.IS_JAVA_1_4_2_OR_LATER) {
                // Uses unmodified size specified by "ToolBar.separatorSize"
                uiDefaults = append(uiDefaults, 
                        "ToolBarSeparatorUI", windowsPrefix + "ToolBarSeparatorUI");
            }
        }
        table.putDefaults(uiDefaults);
    }

    /**
     * Initializes the component defaults.
     */
    protected void initComponentDefaults(UIDefaults table) {
        super.initComponentDefaults(table);
        
        final boolean isXP = LookUtils.IS_LAF_WINDOWS_XP_ENABLED;
        final boolean isClassic = !isXP;
        final boolean isVista = LookUtils.IS_OS_WINDOWS_VISTA;

        initFontDefaults(table);

        if (isClassic) {
            initComponentDefaultsClassic(table);
        }
        if (isXP && LookUtils.IS_JAVA_1_4) {
            initComponentDefaultsXP14(table);
        }

        Object marginBorder = new BasicBorders.MarginBorder();
        Object checkBoxMargin = new InsetsUIResource(2, 0, 2, 0);

        Object etchedBorder = new UIDefaults.ProxyLazyValue(
                "javax.swing.plaf.BorderUIResource",
                "getEtchedBorderUIResource");
        Object buttonBorder = new SimpleProxyLazyValue(
                "com.jgoodies.looks.windows.WindowsLookAndFeel",
                "getButtonBorder");
        
        Object menuBorder = isXP
            ? WindowsBorders.getXPMenuBorder()
            : WindowsBorders.getMenuBorder();

        Object menuBarEmptyBorder     = marginBorder;
        Object menuBarSeparatorBorder = WindowsBorders.getSeparatorBorder();
        Object menuBarEtchedBorder    = WindowsBorders.getEtchedBorder();
        Object menuBarHeaderBorder    = WindowsBorders.getMenuBarHeaderBorder();

        Object toolBarEmptyBorder     = marginBorder;
        Object toolBarSeparatorBorder = WindowsBorders.getSeparatorBorder();
        Object toolBarEtchedBorder    = WindowsBorders.getEtchedBorder();
        Object toolBarHeaderBorder    = WindowsBorders.getToolBarHeaderBorder();

        int buttonPad = Options.getUseNarrowButtons() ? 4 : 14;
        Object buttonMargin = isVista 
            ? (isClassic
                    ? new InsetsUIResource(0, buttonPad, 1, buttonPad)
                    : new InsetsUIResource(1, buttonPad, 1, buttonPad))
            : (isClassic
                    ? new InsetsUIResource(1, buttonPad, 1, buttonPad)
                    : new InsetsUIResource(2, buttonPad, 3, buttonPad));

        Object toolBarSeparatorSize = LookUtils.IS_JAVA_1_4_2_OR_LATER
            ? null
            : new DimensionUIResource(6, Options.getDefaultIconSize().height);

        Insets textInsets = isVista 
            ? (isClassic
                    ? new InsetsUIResource(1, 2, 1, 2)
                    : new InsetsUIResource(1, 2, 1, 2))
            : (isClassic
                    ? new InsetsUIResource(1, 2, 2, 2)
                    : new InsetsUIResource(2, 2, 3, 2));
        
        int comboBorderSize  = isClassic ? 2 : 1;
        int comboPopupBorderSize = 1;
        int comboRendererGap = textInsets.left + comboBorderSize - comboPopupBorderSize;
        Object comboRendererBorder = new EmptyBorder(1, comboRendererGap, 1, comboRendererGap);
        Object comboTableEditorInsets = new Insets(0, 0, 0, 0);
        
        Object menuItemMargin = LookUtils.IS_LOW_RESOLUTION
                ? new InsetsUIResource(3, 0, 3, 0)
                : new InsetsUIResource(2, 0, 2, 0);
        Object menuMargin = isXP
                ? (LookUtils.IS_LOW_RESOLUTION
                        ? new InsetsUIResource(2, 3, 2, 4)
                        : new InsetsUIResource(2, 5, 2, 6))
                : (LookUtils.IS_LOW_RESOLUTION
                        ? new InsetsUIResource(2, 3, 2, 3)
                        : new InsetsUIResource(2, 4, 2, 4));

        int pad = isXP ? 3 : 0;
        Object popupMenuSeparatorMargin = LookUtils.IS_LOW_RESOLUTION
                ? new InsetsUIResource(2, pad, 3, pad)
                : new InsetsUIResource(3, pad, 4, pad);

        Icon menuItemCheckIcon = new MinimumSizedIcon();

        // 	Should be active.
        int treeFontSize = table.getFont("Tree.font").getSize();
        Integer rowHeight = new Integer(treeFontSize + 6);

        Class superclass = getClass().getSuperclass();
        Color controlColor = table.getColor("control");
        Color inactiveTextBackground = table.getColor("TextField.inactiveBackground");

        Object menuBarBackground = isXP 
                ? table.get("control") 
				: table.get("menu");
        Object menuSelectionBackground = isXP
                ? table.get("MenuItem.selectionBackground")
                : table.get("Menu.background");
        Object menuSelectionForeground = isXP
                ? table.get("MenuItem.selectionForeground")
                : table.get("Menu.foreground");
        
        Object[] defaults = {
            "Button.border",              buttonBorder, 
			"Button.margin",              buttonMargin, // Sun's 14px margin is too wide

            // 1.4.2 uses a 2 pixel non-standard border that leads to bad
            // alignment in the typical case that the border is not painted
            "CheckBox.border",            marginBorder, 
            "CheckBox.margin",            checkBoxMargin,
            
            "ComboBox.disabledBackground", inactiveTextBackground,
            "ComboBox.editorBorder",      marginBorder,
            "ComboBox.editorColumns",     new Integer(5),
            "ComboBox.editorInsets",      textInsets,          // Added by JGoodies
            "ComboBox.tableEditorInsets", comboTableEditorInsets,            
            "ComboBox.rendererBorder",    comboRendererBorder, // Added by JGoodies
            
            "EditorPane.margin",		  textInsets,
            
            // Begin 1.3 und 1.4.0
            "Menu.border",                menuBorder, // Fixed in 1.4.1
            "Menu.borderPainted",         Boolean.TRUE, 
			"Menu.background",            menuBarBackground, 
			"Menu.selectionForeground",   menuSelectionForeground, 
			"Menu.selectionBackground",   menuSelectionBackground, 
            // End 1.3 und 1.4.0

            "Menu.margin",                menuMargin, // 1.4.1 Bug

            "MenuBar.background",         menuBarBackground, 
			"MenuBar.border",             menuBarSeparatorBorder, // 1.4.1 Separator wrong
            "MenuBar.emptyBorder",        menuBarEmptyBorder, // Added by JGoodies 
            "MenuBar.separatorBorder",    menuBarSeparatorBorder, // Added by JGoodies 
            "MenuBar.etchedBorder",       menuBarEtchedBorder, // Added by JGoodies
            "MenuBar.headerBorder",       menuBarHeaderBorder, // Added by JGoodies

            "MenuItem.borderPainted",     Boolean.TRUE, 
			"MenuItem.checkIcon",         menuItemCheckIcon, // Aligns menu items
            "MenuItem.margin",            menuItemMargin, // 1.4.1 Bug
            "CheckBoxMenuItem.margin",    menuItemMargin, // 1.4.1 Bug
            "RadioButtonMenuItem.margin", menuItemMargin, // 1.4.1 Bug

            "FormattedTextField.disabledBackground", inactiveTextBackground, // for readonly
            "FormattedTextField.inactiveBackground", inactiveTextBackground, // for readonly
            "FormattedTextField.margin",  textInsets, // Poor in 1.6
            "PasswordField.margin",       textInsets, // Poor in 1.6
            
            "PopupMenu.border",           WindowsBorders.getPopupMenuBorder(),
            "PopupMenu.noMarginBorder",   WindowsBorders.getNoMarginPopupMenuBorder(),
            "PopupMenuSeparator.margin",  popupMenuSeparatorMargin, 

            "ScrollPane.etchedBorder",    etchedBorder, // Added by JGoodies
            
            "Spinner.defaultEditorInsets", textInsets, // Added by JGoodies
            
            // 1.4.1 uses a 2 pixel non-standard border, that leads to bad
            // alignment in the typical case that the border is not painted
            "RadioButton.border",         marginBorder, 
            "RadioButton.margin",         checkBoxMargin, 
            
            "Table.gridColor",            controlColor, // 1.4.1 Bug; active
            "TextArea.margin",            textInsets, // 1.4.1 Bug
            "TextArea.disabledBackground", inactiveTextBackground,
            "TextArea.inactiveBackground", inactiveTextBackground,
            "TextField.margin",           textInsets, // 1.4.1 Bug
            "ToggleButton.margin",        buttonMargin, // Sun's 14px margin is too wide

			"ToolBar.emptyBorder",        toolBarEmptyBorder, // Added by JGoodies
            "ToolBar.separatorBorder",    toolBarSeparatorBorder, // Added by JGoodies
            "ToolBar.etchedBorder",       toolBarEtchedBorder, // Added by JGoodies
            "ToolBar.headerBorder",       toolBarHeaderBorder, // Added by JGoodies
            "ToolBar.separatorSize",      toolBarSeparatorSize, 
			"ToolBar.margin",             new InsetsUIResource(0, 10, 0, 0), 

            "Tree.selectionBorderColor",  controlColor, // 1.4.1 Bug; active
            "Tree.rowHeight",             rowHeight, // 1.4.1 Bug
        };
        if (LookUtils.IS_JAVA_1_4) {
            defaults = append(defaults, new Object[] {
            "InternalFrame.icon",         makeIcon(superclass, "icons/JavaCup.gif"),
            "OptionPane.errorIcon",       isXP ? makeIcon(getClass(), "icons/xp/Error.png")
                                               : makeIcon(superclass, "icons/Error.gif"),
            "OptionPane.informationIcon", isXP ? makeIcon(getClass(), "icons/xp/Inform.png")
                                               : makeIcon(superclass, "icons/Inform.gif"), 
            "OptionPane.warningIcon",     isXP ? makeIcon(getClass(), "icons/xp/Warn.png")
                                               : makeIcon(superclass, "icons/Warn.gif"), 
            "OptionPane.questionIcon",    isXP ? makeIcon(getClass(), "icons/xp/Inform.png")
                                               : makeIcon(superclass, "icons/Question.gif")
            });
        }
        if (LookUtils.IS_JAVA_1_4 || LookUtils.IS_JAVA_5) {
            defaults = append(defaults, new Object[] {
            "Tree.openIcon",              isXP ? makeIcon(getClass(), "icons/xp/TreeOpen.png")
                                               : makeIcon(getClass(), "icons/TreeOpen.gif"),
            "Tree.closedIcon",            isXP ? makeIcon(getClass(), "icons/xp/TreeClosed.png")
                                               : makeIcon(getClass(), "icons/TreeClosed.gif")
            });
        }
        table.putDefaults(defaults);
    }

    /**
     * Initializes component defaults required in classic mode only.
     */
    private void initComponentDefaultsClassic(UIDefaults table) {
        Object checkBoxIcon = new SimpleProxyLazyValue(
                "com.jgoodies.looks.windows.WindowsLookAndFeel",
                "getCheckBoxIcon");

        Object radioButtonIcon = new SimpleProxyLazyValue(
                "com.jgoodies.looks.windows.WindowsLookAndFeel",
                "getRadioButtonIcon");

        Border winInsetBorder = new BasicBorders.FieldBorder(table
                .getColor("controlShadow"), table
                .getColor("controlDkShadow"),
                table.getColor("controlHighlight"), table
                        .getColor("controlLtHighlight"));

        Object[] defaults = {
            "CheckBox.checkColor",    table.get("controlText"), // kind-of black
            "CheckBox.icon",          checkBoxIcon, 
			"RadioButton.checkColor", table.get("controlText"), // kind-of black
            "RadioButton.icon",       radioButtonIcon, 
            "Table.scrollPaneBorder", winInsetBorder, // 1.4.1 Bug

        };
        table.putDefaults(defaults);
    }

    /**
     * Initializes component defaults required in XP mode on Java 1.4.2 only.
     */
    private void initComponentDefaultsXP14(UIDefaults table) {
        Object[] defaults = {
            "TitledBorder.titleColor", table.getColor("activeCaption")
        };
        table.putDefaults(defaults);
    }

    /**
     * Looks up the correct control font and sets it for all controls.
     */
    private void initFontDefaults(UIDefaults table) {
        FontChoicePolicy fontChoicePolicy = getFontChoicePolicy();
        FontSet fontSet = fontChoicePolicy.getFontSet("Windows", table);
        initFontDefaults(table, fontSet);
    }

    
    /**
	 * Sets Fonts in the given FontSet as defaults for all known 
     * component types in the given UIDefaults table.
     * 
     * @param table       the UIDefaults table used to set fonts
     * @param fontSet     describes the set of Fonts to be installed
	 */
	private static void initFontDefaults(UIDefaults table, FontSet fontSet) {
        Font controlFont = fontSet.getControlFont();
        Font menuFont    = fontSet.getMenuFont();
        Font messageFont = fontSet.getMessageFont();
        Font toolTipFont = fontSet.getSmallFont();
        Font titleFont   = fontSet.getTitleFont();
        Font windowFont  = fontSet.getWindowTitleFont();
        
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
                "TextArea.font",                        controlFont,  
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
				"TitledBorder.font",					titleFont,
				"ToolTip.font",							toolTipFont,

                "CheckBoxMenuItem.font",                menuFont,
                "CheckBoxMenuItem.acceleratorFont",     menuFont,  // 1.3 only ?
                "Menu.font",                            menuFont,
                "Menu.acceleratorFont",                 menuFont,
                "MenuBar.font",                         menuFont,
                "MenuItem.font",                        menuFont,
                "MenuItem.acceleratorFont",             menuFont,
                "PopupMenu.font",                       menuFont,
                "RadioButtonMenuItem.font",             menuFont,
                "RadioButtonMenuItem.acceleratorFont",  menuFont,   // 1.3 only ?
            };
        table.putDefaults(defaults);
	}

    
    // Getters for Proxy Access (Referred classes can stay package visible) ***

    public static Border getButtonBorder() {
        return WindowsBorders.getButtonBorder();
    }

    public static Icon getCheckBoxIcon() {
        return WindowsIconFactory.getCheckBoxIcon();
    }

    public static Icon getRadioButtonIcon() {
        return WindowsIconFactory.getRadioButtonIcon();
    }
    
    // Helper Code ************************************************************
    
    /**
     * Appends the key and value to the given source array and returns
     * a copy that has the two new elements at its end.
     * 
     * @return an array with the key and value appended
     */
    private static Object[] append(Object[] source, String key, Object value) {
        int length = source.length;
        Object[] destination = new Object[length + 2];
        System.arraycopy(source, 0, destination, 0, length);
        destination[length] = key;
        destination[length + 1] = value;
        return destination;
    }


    /**
     * Appends the key and value to the given source array and returns
     * a copy that has the two new elements at its end.
     * 
     * @return an array with the key and value appended
     */
    private static Object[] append(Object[] source, Object[] keysAndValues) {
        int length = source.length;
        Object[] destination = new Object[length + keysAndValues.length];
        System.arraycopy(source, 0, destination, 0, length);
        for (int i = 0; i < keysAndValues.length; i++) {
            destination[length + i] = keysAndValues[i];
        }
        return destination;
    }

    // Helper Class ***********************************************************	

    /**
     * This class provides an implementation of <code>LazyValue</code> that
     * can be used to delay loading of the Class for the instance to be created.
     * It also avoids creation of an anonymous inner class for the
     * <code>LazyValue</code>
     * subclass.  Both of these improve performance at the time that a
     * a Look and Feel is loaded, at the cost of a slight performance
     * reduction the first time <code>createValue</code> is called
     * (since Reflection APIs are used).
     */
    private static class SimpleProxyLazyValue implements UIDefaults.LazyValue {

        private final String className;
        private final String methodName;

        /**
         * Creates a <code>LazyValue</code> which will construct an instance
         * when asked.
         * 
         * @param c    a <code>String</code> specifying the classname of the class
         *             	containing a static method to be called for instance creation
         * @param m    a <code>String</code> specifying the static 
         *		method to be called on class c
         */
        public SimpleProxyLazyValue(String c, String m) {
            className = c;
            methodName = m;
        }

        /**
         * Creates the value retrieved from the <code>UIDefaults</code> table.
         * The object is created each time it is accessed.
         *
         * @param table  a <code>UIDefaults</code> table
         * @return the created <code>Object</code>
         */
        public Object createValue(UIDefaults table) {
            Object instance = null;
            try {
                Class c;
                // We use a separate ClassLoader
                ClassLoader classLoader = table != null
                        ? (ClassLoader) table.get("ClassLoader")
                        : Thread.currentThread().getContextClassLoader();
                if (classLoader == null) {
                    classLoader = getClass().getClassLoader();
                }
                c = Class.forName(className, true, classLoader);
                Method m = c.getMethod(methodName, null);
                instance = m.invoke(c, null);
            } catch (Throwable t) {
                LookUtils.log("Problem creating " + className + " with method "
                        + methodName + t);
            }
            return instance;
        }
    }

}