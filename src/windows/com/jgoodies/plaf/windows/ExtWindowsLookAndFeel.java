/*
 * Copyright (c) 2003 JGoodies Karsten Lentzsch. All Rights Reserved.
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

package com.jgoodies.plaf.windows;

import java.awt.Color;
import java.awt.Font;
import java.lang.reflect.Method;

import javax.swing.Icon;
import javax.swing.UIDefaults;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.BorderUIResource;
import javax.swing.plaf.DimensionUIResource;
import javax.swing.plaf.FontUIResource;
import javax.swing.plaf.InsetsUIResource;
import javax.swing.plaf.basic.BasicBorders;

import com.sun.java.swing.plaf.windows.WindowsLookAndFeel;

import com.jgoodies.plaf.FontSizeHints;
import com.jgoodies.plaf.FontUtils;
import com.jgoodies.plaf.LookUtils;
import com.jgoodies.plaf.Options;
import com.jgoodies.plaf.common.MinimumSizedIcon;

/**
 * The main class of the JGoodies Windows Look&amp;Feel.
 * This look provides several corrections and extensions to Sun's Windows L&F.
 * In addition it tries to provide a unified look for the J2SE 1.4.0x, 1.4.1x
 * and 1.4.2 environments.
 * 
 * @author Karsten Lentzsch
 * @version $Revision: 1.10 $
 */
public final class ExtWindowsLookAndFeel extends WindowsLookAndFeel {

    /** 
     * Client property key to set a border style - shadows the header style. 
     * */
    public static final String BORDER_STYLE_KEY = "jgoodies.windows.borderStyle";

    // The look dependent fontSizeHints
    private static FontSizeHints fontSizeHints = null;

    public String getID() {
        return "JGoodies Windows";
    }

    public String getName() {
        return "JGoodies Windows";
    }

    public String getDescription() {
        return "The JGoodies Windows Look and Feel"
                + " - \u00a9 2003 JGoodies Karsten Lentzsch";
    }

    // Special Properties ***************************************************

    /**
     * Answers the current <code>FontSizeHints</code>; look specific 
     * settings shadow the global users defaults as stored under 
     * key <code>FontSizeHints.KEY</code>.
     * 
     * @see Options#setGlobalFontSizeHints(FontSizeHints)
     * @see FontSizeHints
     */
    public static FontSizeHints getFontSizeHints() {
        return fontSizeHints != null ? fontSizeHints : Options
                .getGlobalFontSizeHints();
    }

    /**
     * Sets <code>FontSizeHints</code> that shadow the global font size hints.
     * 
     * @see Options#setGlobalFontSizeHints(FontSizeHints)
     * @see FontSizeHints
     */
    public static void setFontSizeHints(FontSizeHints newHints) {
        fontSizeHints = newHints;
    }

    // Overriding Superclass Behavior ***************************************

    /**
     * Initializes the class defaults, that is, overrides some UI delegates
     * with JGoodies Windows implementations.
     */
    protected void initClassDefaults(UIDefaults table) {
        super.initClassDefaults(table);
        String WINDOWS_PREFIX = "com.jgoodies.plaf.windows.ExtWindows";
        String COMMON_PREFIX  = "com.jgoodies.plaf.common.ExtBasic";

        String menuUIPrefix = LookUtils.IS_LAF_WINDOWS_XP_ENABLED
                ? WINDOWS_PREFIX
                : COMMON_PREFIX;

        // Overwrite some of the uiDefaults.
        Object[] uiDefaults = {
        // Can use narrow margins
            "ButtonUI",              WINDOWS_PREFIX + "ButtonUI", 
			"ToggleButtonUI",        WINDOWS_PREFIX + "ToggleButtonUI", 

            // Modified size 
            "ComboBoxUI",            WINDOWS_PREFIX + "ComboBoxUI", 

            // Uses ClearLook.
            "LabelUI",               WINDOWS_PREFIX + "LabelUI", 
			"PanelUI",               WINDOWS_PREFIX + "PanelUI", 
			"ScrollPaneUI",          WINDOWS_PREFIX + "ScrollPaneUI", 

            // Optional style and optional special borders
            "MenuBarUI",             WINDOWS_PREFIX + "MenuBarUI", 

            // Aligned menu items
            "MenuUI",                menuUIPrefix + "MenuUI", 
			"MenuItemUI",            COMMON_PREFIX + "MenuItemUI", 
			"CheckBoxMenuItemUI",    COMMON_PREFIX + "CheckBoxMenuItemUI", 
			"RadioButtonMenuItemUI", COMMON_PREFIX + "RadioButtonMenuItemUI", 

            // Has padding above and below the separator lines				
            "PopupMenuSeparatorUI",  COMMON_PREFIX + "PopupMenuSeparatorUI", 

            // Honors the screen resolution and uses a minimum button width             
            "OptionPaneUI",          WINDOWS_PREFIX + "OptionPaneUI", 

            // 1.4.1 has ugly one touch triangles
            "SplitPaneUI",           WINDOWS_PREFIX + "SplitPaneUI", 

            // Work in progress: Can have a flat presentation
            "TabbedPaneUI",          WINDOWS_PREFIX + "TabbedPaneUI", 

            // Corrected position of the tree button icon
            "TreeUI",                WINDOWS_PREFIX + "TreeUI",

            // Modified Border
            "SpinnerUI",          WINDOWS_PREFIX + "SpinnerUI", 
        };
        table.putDefaults(uiDefaults);

        Object[] otherDefaults;
        if (LookUtils.IS_LAF_WINDOWS_XP_ENABLED) {
            otherDefaults = new Object[] {
                // Renders a circle, not the star ("*") character                       
                "PasswordFieldUI",    WINDOWS_PREFIX + "XPPasswordFieldUI", 

                // Optional style and optional special borders; 
                // rollover borders for compound buttons
                "ToolBarUI",          WINDOWS_PREFIX + "XPToolBarUI",
                
            };
        } else {
            otherDefaults = new Object[]{
            // Optional style and optional special borders; 
                // rollover borders corrected
                "ToolBarUI",          WINDOWS_PREFIX + "ToolBarUI", 

                // Black arrows
                "ScrollBarUI",        WINDOWS_PREFIX + "ScrollBarUI", 

                // Uses unmodified size specified by "ToolBar.separatorSize"
                "ToolBarSeparatorUI", WINDOWS_PREFIX + "ToolBarSeparatorUI",
                };
        }
        table.putDefaults(otherDefaults);
    }

    /**
     * Initializes the component defaults.
     */
    protected void initComponentDefaults(UIDefaults table) {
        super.initComponentDefaults(table);
        
        final boolean isXP = LookUtils.IS_LAF_WINDOWS_XP_ENABLED;

        // Override font settings if and only if we are allowed to.
        if (FontUtils.useSystemFontSettings()) {
            initFontDefaults(table);
        }

        if (!isXP) {
            initComponentDefaultsNoXP(table);
        }

        initClearLookDefaults(table);

        Object marginBorder = new BasicBorders.MarginBorder();
        Object checkBoxMargin = new InsetsUIResource(2, 0, 2, 0);

        Object etchedBorder = new UIDefaults.ProxyLazyValue(
                "javax.swing.plaf.BorderUIResource",
                "getEtchedBorderUIResource");
        Object buttonBorder = new SimpleProxyLazyValue(
                "com.jgoodies.plaf.windows.ExtWindowsLookAndFeel",
                "getButtonBorder");
        Object menuBorder = ExtWindowsBorders.getMenuBorder();

        Object menuBarEmptyBorder     = marginBorder;
        Object menuBarSeparatorBorder = ExtWindowsBorders.getSeparatorBorder();
        Object menuBarEtchedBorder    = ExtWindowsBorders.getEtchedBorder();
        Object menuBarHeaderBorder    = ExtWindowsBorders.getMenuBarHeaderBorder();

        Object toolBarEmptyBorder     = marginBorder;
        Object toolBarSeparatorBorder = ExtWindowsBorders.getSeparatorBorder();
        Object toolBarEtchedBorder    = ExtWindowsBorders.getEtchedBorder();
        Object toolBarHeaderBorder    = ExtWindowsBorders.getToolBarHeaderBorder();

        Object defaultButtonMargin    = LookUtils.createButtonMargin(false);
        Object narrowButtonMargin     = LookUtils.createButtonMargin(true);

        Object toolBarSeparatorSize = LookUtils.IS_JAVA_1_4_2_OR_LATER
            ? null
            : new DimensionUIResource(6, Options.getDefaultIconSize().height);

        Object textInsets  = new InsetsUIResource(2, 2, 2, 2);
        
        Object comboRendererMargin = LookUtils.IS_JAVA_1_4
        	? textInsets
        	: new InsetsUIResource(0, 0, 0, 0);

        Object menuItemMargin = LookUtils.isLowRes
                ? new InsetsUIResource(3, 0, 3, 0)
                : new InsetsUIResource(2, 0, 2, 0);
        Object menuMargin = LookUtils.isLowRes
                ? new InsetsUIResource(2, 3, 2, 3)
                : new InsetsUIResource(2, 4, 2, 4);

        int pad = isXP ? 3 : 0;
        Object popupMenuSeparatorMargin = LookUtils.isLowRes
                ? new InsetsUIResource(2, pad, 3, pad)
                : new InsetsUIResource(3, pad, 4, pad);

        Icon menuItemCheckIcon = new MinimumSizedIcon();

        // 	Should be active.
        int treeFontSize = table.getFont("Tree.font").getSize();
        Integer rowHeight = new Integer(treeFontSize + 6);

        Class superclass = getClass().getSuperclass();
        Color controlColor = table.getColor("control");

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
			"Button.margin",              defaultButtonMargin, // 1.4.1 Bug
            "Button.narrowMargin",        narrowButtonMargin, // Added by JGoodies

            // 1.4.2 uses a 2 pixel non-standard border, that leads to bad
            // alignment in the typical case that the border is not painted
            "CheckBox.border",            marginBorder, 
            "CheckBox.margin",            checkBoxMargin,
            
            "ComboBox.editorBorder",      marginBorder,
            "ComboBox.editorColumns",     new Integer(5),
            "ComboBox.rendererMargin",    comboRendererMargin, // Added by JGoodies
            
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

            "OptionPane.errorIcon",       isXP ? makeIcon(getClass(), "icons/xp/Error.png")
                                               : makeIcon(superclass, "icons/Error.gif"),
            "OptionPane.informationIcon", isXP ? makeIcon(getClass(), "icons/xp/Inform.png")
                                               : makeIcon(superclass, "icons/Inform.gif"), 
			"OptionPane.warningIcon",     isXP ? makeIcon(getClass(), "icons/xp/Warn.png")
                                               : makeIcon(superclass, "icons/Warn.gif"), 
			"OptionPane.questionIcon",    isXP ? makeIcon(getClass(), "icons/xp/Inform.png")
                                               : makeIcon(superclass, "icons/Question.gif"),
            "FormattedTextField.margin",  textInsets, // 1.4.1 Bug
            "PasswordField.margin",       textInsets, // 1.4.1 Bug
            "PopupMenuSeparator.margin",  popupMenuSeparatorMargin, 

            "ScrollPane.etchedBorder",    etchedBorder, // Added by JGoodies
            
            // 1.4.1 uses a 2 pixel non-standard border, that leads to bad
            // alignment in the typical case that the border is not painted
            "RadioButton.border",         marginBorder, 
            "RadioButton.margin",         checkBoxMargin, 
            
            "Table.gridColor",            controlColor, // 1.4.1 Bug; active
            "TextArea.margin",            textInsets, // 1.4.1 Bug
            "TextField.margin",           textInsets, // 1.4.1 Bug
            "ToggleButton.margin",        defaultButtonMargin, // 1.4.1 Bug
            "ToggleButton.narrowMargin",  narrowButtonMargin, // Added by JGoodies

            "ToolBar.border",             toolBarEmptyBorder, 
			"ToolBar.emptyBorder",        toolBarEmptyBorder, // Added by JGoodies
            "ToolBar.separatorBorder",    toolBarSeparatorBorder, // Added by JGoodies
            "ToolBar.etchedBorder",       toolBarEtchedBorder, // Added by JGoodies
            "ToolBar.headerBorder",       toolBarHeaderBorder, // Added by JGoodies
            "ToolBar.separatorSize",      toolBarSeparatorSize, 
			"ToolBar.margin",             new InsetsUIResource(0, 10, 0, 0), 

            "Tree.selectionBorderColor",  controlColor, // 1.4.1 Bug; active
            "Tree.rowHeight",             rowHeight, // 1.4.1 Bug
            "Tree.openIcon",              isXP ? makeIcon(getClass(), "icons/xp/TreeOpen.png")
                                               : makeIcon(getClass(), "icons/TreeOpen.gif"),
            "Tree.closedIcon",            isXP ? makeIcon(getClass(), "icons/xp/TreeClosed.png")
                                               : makeIcon(getClass(), "icons/TreeClosed.gif"),
        };
        table.putDefaults(defaults);
    }

    /**
     * Initializes component defaults required in 1.3 runtime environments only.
     */
    private void initComponentDefaultsNoXP(UIDefaults table) {
        Object checkBoxIcon = new SimpleProxyLazyValue(
                "com.jgoodies.plaf.windows.ExtWindowsLookAndFeel",
                "getCheckBoxIcon");

        Object radioButtonIcon = new SimpleProxyLazyValue(
                "com.jgoodies.plaf.windows.ExtWindowsLookAndFeel",
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
     * Initializes the font defaults.
     * Uses the superclass' menu font (often Tahoma) in a smaller size
     * as control font and overrides the TextArea font with control font.
     */
    private void initFontDefaults(UIDefaults table) {
        Font messageFont;
        Font toolTipFont;
        Font windowFont;

        // Guess or get the menu font and control font.
        Font menuFont = FontUtils.getMenuFont(table, getFontSizeHints());
        Font controlFont = FontUtils.getControlFont(table, getFontSizeHints());

        // Derive a bold version of the control font.
        Font controlBoldFont = new FontUIResource(controlFont
                .deriveFont(Font.BOLD));

        messageFont = table.getFont("OptionPane.font");
        toolTipFont = table.getFont("ToolTip.font");
        windowFont  = table.getFont("InternalFrame.titleFont");

        FontUtils.initFontDefaults(table, controlFont, controlBoldFont,
                controlFont, menuFont, messageFont, toolTipFont, windowFont);
    }

    private void initClearLookDefaults(UIDefaults table) {
        Object emptyBorder = new BorderUIResource(new EmptyBorder(0, 0, 0, 0));
        Object statusCellBorder = new BorderUIResource.CompoundBorderUIResource(
                new EmptyBorder(2, 0, 1, 3),
                new ExtWindowsBorders.ThinLoweredBorder());
        Object thinLoweredBorder = new ExtWindowsBorders.ThinLoweredBorder();
        Object thinRaisedBorder  = new ExtWindowsBorders.ThinRaisedBorder();

        Object[] defaults = {
            "ClearLook.ScrollPaneReplacementBorder",emptyBorder, 
            "ClearLook.SplitPaneReplacementBorder", emptyBorder,
            "ClearLook.ThinLoweredBorder",          thinLoweredBorder,
            "ClearLook.ThinRaisedBorder",           thinRaisedBorder,
            "ClearLook.NetBeansScrollPaneBorder",   emptyBorder,
            "ClearLook.NetBeansSpecialPanelBorder", emptyBorder,
            "ClearLook.NetBeansStatusCellBorder",   statusCellBorder,
            "ClearLook.NetBeansTabbedPaneBorder",   emptyBorder,};
        table.putDefaults(defaults);
    }

    // Getters for Proxy Access (Referred classes can stay package visible) ************

    public static Border getButtonBorder() {
        return ExtWindowsBorders.getButtonBorder();
    }

    public static Icon getCheckBoxIcon() {
        return ExtWindowsIconFactory.getCheckBoxIcon();
    }

    public static Icon getRadioButtonIcon() {
        return ExtWindowsIconFactory.getRadioButtonIcon();
    }

    // Helper Class ********************************************************************	

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