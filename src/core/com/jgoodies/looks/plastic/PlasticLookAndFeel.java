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

package com.jgoodies.looks.plastic;

import java.awt.Color;
import java.awt.Insets;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.Icon;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.plaf.BorderUIResource;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.FontUIResource;
import javax.swing.plaf.InsetsUIResource;
import javax.swing.plaf.basic.BasicBorders;
import javax.swing.plaf.metal.*;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.metal.MetalTheme;

import sun.awt.AppContext;
import sun.security.action.GetPropertyAction;

import com.jgoodies.looks.FontChoicePolicies;
import com.jgoodies.looks.FontChoicePolicy;
import com.jgoodies.looks.LookUtils;
import com.jgoodies.looks.Options;
import com.jgoodies.looks.common.MinimumSizedIcon;
import com.jgoodies.looks.common.ShadowPopupFactory;
import com.jgoodies.looks.plastic.theme.SkyBluer;

/**
 * Initializes class and component defaults for the 
 * JGoodies Plastic look&amp;feel.<p>
 * 
 * TODO: A version that requires Java 5 could implement
 * the theme access using the MetalLookAndFeel #getCurrentTheme
 * that is public since 1.5.
 *
 * @author Karsten Lentzsch
 * @version $Revision: 1.5 $
 */
public class PlasticLookAndFeel extends MetalLookAndFeel {
	
    // System and Client Property Keys ****************************************
        
	/** 
     * Client property key to set a border style - shadows the header style.
     */ 
	public static final String BORDER_STYLE_KEY = "Plastic.borderStyle";
	
	/** 
     * Client property key to disable the pseudo 3D effect. 
     */
	public static final String IS_3D_KEY = "Plastic.is3D";

    /**
     * A System property key to set the default theme.
     */ 
    public static final String DEFAULT_THEME_KEY =
        "Plastic.defaultTheme";
        
    /**
     * A System property key that indicates that the high contrast
     * focus colors shall be choosen - if applicable. 
     * If not set, some focus colors look good but have low contrast.
     * Basically, the low contrast scheme uses the Plastic colors
     * before 1.0.7, and the high contrast scheme is 1.0.7 - 1.0.9.
     */	
    public static final String HIGH_CONTRAST_FOCUS_ENABLED_KEY =
        "Plastic.highContrastFocus";
        
    /** 
     * A System property key for the rendering style of the Plastic
     * TabbedPane. Valid values are: <tt>default</tt> for the
     * Plastic 1.0 tabs, and <tt>metal</tt> for the Metal L&amp;F tabs.
     */
    protected static final String TAB_STYLE_KEY =
        "Plastic.tabStyle";

    /** 
     * A System property value that indicates that Plastic shall render
     * tabs in the Plastic 1.0 style. This is the default. 
     */
    public static final String TAB_STYLE_DEFAULT_VALUE =
        "default";

    /** 
     * A System property value that indicates that Plastic shall
     * render tabs in the Metal L&amp;F style.
     */
    public static final String TAB_STYLE_METAL_VALUE =
        "metal";

        
    /**
     * A UIManager key used to store and retrieve 
     * the FontChoicePolicy for this Look&amp;Feel.
     * 
     * @see #getFontChoicePolicy()
     * @see #setFontChoicePolicy(FontChoicePolicy)
     */
    private static final String FONT_CHOICE_POLICY_KEY = "Plastic.fontChoicePolicy";

    
    /**
     * A UIManager key used to store and retrieve the PlasticTheme.
     * 
     * @see #getPlasticTheme()
     * @see #setPlasticTheme(PlasticTheme)
     */
    private static final String THEME_KEY = "Plastic.theme";
    
    
    // State *****************************************************************
    
    /**
     * Used to check if the current application context is the same that 
     * we've used before. If so, we can use the static variables below as-is,
     * otherwise, we update this cached app context and need to re-read the
     * static variables from the application context.<p>
     * 
     * The current implementation uses the context for the Plastic theme only.
     * 
     * @see #getPlasticTheme()
     * @see #setPlasticTheme(PlasticTheme)
     */
    private static AppContext cachedAppContext;

    
    /** 
     * Holds whether Plastic uses Metal or Plastic tabbed panes.
     */
    private static boolean useMetalTabs =
        LookUtils.getSystemProperty(TAB_STYLE_KEY, "").
            equalsIgnoreCase(TAB_STYLE_METAL_VALUE);
                    
    /**
     * Holds whether we are using the high contrast focus colors.
     */
    public static boolean useHighContrastFocusColors =
        LookUtils.getSystemProperty(HIGH_CONTRAST_FOCUS_ENABLED_KEY) != null;
                
	/** 
     * The List of installed Plastic themes.
     */
	private static List	installedThemes;

	/** The current Plastic color and font theme. */	
	private static PlasticTheme plasticTheme;
	
	
	/** The look-global state for the 3D enablement. */
	private static boolean is3DEnabled = false;
	
    
    // Instance Creation ******************************************************
	
    /**
     * Constructs the PlasticLookAndFeel, creates the default theme
     * and sets it as current Plastic theme.
     */
    public PlasticLookAndFeel() {
    }

    
    // L&f Description ********************************************************
    
    public String getID() {
        return "JGoodies Plastic";
    }
    
    public String getName() {
        return "JGoodies Plastic";
    }
    
    public String getDescription() {
        return "The JGoodies Plastic Look and Feel"
            + " - \u00a9 2001-2006 JGoodies Karsten Lentzsch";
    }
    
    
    // Optional Settings ******************************************************
    
    /**
     * Looks up and retrieves the FontChoicePolicy used
     * by the JGoodies Windows Look&amp;Feel.
     * If none is set a default policy is used.
     * 
     * @return the FontChoicePolicy set for this Look&amp;feel,
     *     or a default policy if none has been set.
     * 
     * @see #setFontChoicePolicy
     */
    public static FontChoicePolicy getFontChoicePolicy() {
        FontChoicePolicy policy = (FontChoicePolicy) UIManager.get(FONT_CHOICE_POLICY_KEY);
        return policy != null
            ? policy
            : FontChoicePolicies.getCustomizablePlatformSpecificPolicy();
    }
    
    
    /**
     * Sets the FontChoicePolicy to be used with the JGoodies Windows L&amp;F.
     * If the specified policy is <code>null</code>, the policy is used
     * to the default.
     * 
     * @param fontChoicePolicy   the FontChoicePolicy to be used with 
     *     the JGoodies Windows L&amp;F, or <code>null</code> to reset
     *     to the default
     *     
     * @see #getFontChoicePolicy()
     */
    public static void setFontChoicePolicy(FontChoicePolicy fontChoicePolicy) {
        UIManager.put(FONT_CHOICE_POLICY_KEY, fontChoicePolicy);
    }
    

    protected boolean is3DEnabled() {
        return is3DEnabled;
    }

    public static void set3DEnabled(boolean b) {
        is3DEnabled = b;
    }
    
    public static String getTabStyle() {
        return useMetalTabs ? TAB_STYLE_METAL_VALUE : TAB_STYLE_DEFAULT_VALUE;
    }

    public static void setTabStyle(String tabStyle) {
        useMetalTabs = tabStyle.equalsIgnoreCase(TAB_STYLE_METAL_VALUE);
    }

    public static boolean getHighContrastFocusColorsEnabled() {
        return useHighContrastFocusColors;
    }

    public static void setHighContrastFocusColorsEnabled(boolean b) {
        useHighContrastFocusColors = b;
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
	 * with JGoodies Plastic implementations.
	 * 
     * @param table   the UIDefaults table to work with
	 * @see javax.swing.plaf.basic.BasicLookAndFeel#getDefaults()
	 */
	protected void initClassDefaults(UIDefaults table) {
		super.initClassDefaults(table);

		final String plasticPrefix = "com.jgoodies.looks.plastic.Plastic";
        final String commonPrefix  = "com.jgoodies.looks.common.ExtBasic";
        
		// Overwrite some of the uiDefaults.
		Object[] uiDefaults = {
				// 3D effect; optional narrow margins
				"ButtonUI",					plasticPrefix + "ButtonUI",
				"ToggleButtonUI",			plasticPrefix + "ToggleButtonUI",

				// 3D effect
				"ComboBoxUI", 	 			plasticPrefix + "ComboBoxUI",
				"ScrollBarUI", 				plasticPrefix + "ScrollBarUI",
				"SpinnerUI",				plasticPrefix + "SpinnerUI",
				
				// Special borders defined by border style or header style, see LookUtils
				"MenuBarUI",				plasticPrefix + "MenuBarUI",
				"ToolBarUI",				plasticPrefix + "ToolBarUI",
				
				// Aligns menu icons
                "MenuUI",                   plasticPrefix + "MenuUI",
				"MenuItemUI",				commonPrefix + "MenuItemUI",
				"CheckBoxMenuItemUI",		commonPrefix + "CheckBoxMenuItemUI",
				"RadioButtonMenuItemUI",	commonPrefix + "RadioButtonMenuItemUI",

                // Provides an option for a no margin border              
                "PopupMenuUI",              plasticPrefix + "PopupMenuUI",
               
				// Has padding above and below the separator lines				
		        "PopupMenuSeparatorUI",		commonPrefix + "PopupMenuSeparatorUI",
		       
                // Honors the screen resolution and uses a minimum button width             
                "OptionPaneUI",             plasticPrefix + "OptionPaneUI",
               
                // Can installs an optional etched border
				"ScrollPaneUI",				plasticPrefix + "ScrollPaneUI",
                   
                // Uses a modified split divider
				"SplitPaneUI", 				plasticPrefix + "SplitPaneUI",
				
				// Modified icons and lines
				"TreeUI", 					plasticPrefix + "TreeUI",
				
				// Just to use Plastic colors
				"InternalFrameUI",			plasticPrefix + "InternalFrameUI",
                
                // Share the UI delegate instances
                "SeparatorUI",              plasticPrefix + "SeparatorUI",
                "ToolBarSeparatorUI",       plasticPrefix + "ToolBarSeparatorUI"

			};
		table.putDefaults(uiDefaults);
        if (!useMetalTabs) {
            // Modified tabs and ability use a version with reduced borders.
            table.put("TabbedPaneUI", plasticPrefix + "TabbedPaneUI");
        }
	}


	protected void initComponentDefaults(UIDefaults table) {
		super.initComponentDefaults(table);
		
        final boolean isVista = LookUtils.IS_OS_WINDOWS_VISTA;

        Object marginBorder				= new BasicBorders.MarginBorder();
		
        Object buttonBorder				= PlasticBorders.getButtonBorder();
		Object menuItemBorder			= PlasticBorders.getMenuItemBorder();
        Object textFieldBorder			= PlasticBorders.getTextFieldBorder();
        Object toggleButtonBorder		= PlasticBorders.getToggleButtonBorder();

		Object scrollPaneBorder			= PlasticBorders.getScrollPaneBorder();
		Object tableHeaderBorder		= new BorderUIResource(
										   (Border) table.get("TableHeader.cellBorder"));

		Object menuBarEmptyBorder		= marginBorder;
		Object menuBarSeparatorBorder	= PlasticBorders.getSeparatorBorder();  
		Object menuBarEtchedBorder		= PlasticBorders.getEtchedBorder();
		Object menuBarHeaderBorder		= PlasticBorders.getMenuBarHeaderBorder(); 
		
		Object toolBarEmptyBorder		= marginBorder;
		Object toolBarSeparatorBorder	= PlasticBorders.getSeparatorBorder();
		Object toolBarEtchedBorder		= PlasticBorders.getEtchedBorder();
		Object toolBarHeaderBorder		= PlasticBorders.getToolBarHeaderBorder();
		
		Object internalFrameBorder		= getInternalFrameBorder();
		Object paletteBorder			= getPaletteBorder();
		
		Color controlColor 				= table.getColor("control");
		
		Object checkBoxIcon				= PlasticIconFactory.getCheckBoxIcon();
		Object checkBoxMargin			= new InsetsUIResource(2, 0, 2, 1); // 1.4.1 uses 2,2,2,2
		
        Object buttonMargin = createButtonMargin();
		
		Object textInsets = isVista
            ? new InsetsUIResource(1, 1, 1, 1)
            : new InsetsUIResource(1, 1, 2, 1);
            
        Object wrappedTextInsets = isVista
            ? new InsetsUIResource(1, 1, 2, 1)
            : new InsetsUIResource(2, 1, 2, 1);
                                                
		Object menuItemMargin			= new InsetsUIResource(3, 0, 3, 0);
		Object menuMargin				= new InsetsUIResource(2, 4, 2, 4);

		Icon   menuItemCheckIcon		= new MinimumSizedIcon(); 
		Icon   checkBoxMenuItemIcon		= PlasticIconFactory.getCheckBoxMenuItemIcon();
		Icon   radioButtonMenuItemIcon	= PlasticIconFactory.getRadioButtonMenuItemIcon();
		
		Color  menuItemForeground		= table.getColor("MenuItem.foreground");

		// 	Should be active.
		int     treeFontSize			= table.getFont("Tree.font").getSize(); 
		Integer rowHeight				= new Integer(treeFontSize + 6);
        Object  treeExpandedIcon		= PlasticIconFactory.getExpandedTreeIcon();
        Object  treeCollapsedIcon		= PlasticIconFactory.getCollapsedTreeIcon();
        ColorUIResource gray 			= new ColorUIResource(Color.GRAY);
		
		Boolean is3D					= Boolean.valueOf(is3DEnabled());
		
		Object[] defaults = { 
		"Button.border",								buttonBorder,
		"Button.margin",								buttonMargin,

		"CheckBox.margin", 								checkBoxMargin,

		// Use a modified check
		"CheckBox.icon", 								checkBoxIcon,
			
		"CheckBoxMenuItem.border",						menuItemBorder,
		"CheckBoxMenuItem.margin",						menuItemMargin,			// 1.4.1 Bug
		"CheckBoxMenuItem.checkIcon",					checkBoxMenuItemIcon,
        "CheckBoxMenuItem.background", 					getMenuItemBackground(),// Added by JGoodies
		"CheckBoxMenuItem.selectionForeground",			getMenuItemSelectedForeground(),
		"CheckBoxMenuItem.selectionBackground",			getMenuItemSelectedBackground(),
		"CheckBoxMenuItem.acceleratorForeground",		menuItemForeground,
		"CheckBoxMenuItem.acceleratorSelectionForeground",getMenuItemSelectedForeground(),
		"CheckBoxMenuItem.acceleratorSelectionBackground",getMenuItemSelectedBackground(),

		// ComboBox uses menu item selection colors
		"ComboBox.selectionForeground",					getMenuSelectedForeground(),
		"ComboBox.selectionBackground",					getMenuSelectedBackground(),
        "ComboBox.arrowButtonBorder",                   PlasticBorders.getComboBoxArrowButtonBorder(),
        "ComboBox.editorBorder",                        PlasticBorders.getComboBoxEditorBorder(),
        "ComboBox.editorColumns",                       new Integer(5),
        
        "EditorPane.margin",                            wrappedTextInsets,

        "InternalFrame.border", 						internalFrameBorder,
        "InternalFrame.paletteBorder", 					paletteBorder,

		"List.font",									getControlTextFont(),
		"Menu.border",									PlasticBorders.getMenuBorder(), 
		"Menu.margin",									menuMargin,
		"Menu.arrowIcon",								PlasticIconFactory.getMenuArrowIcon(),

		"MenuBar.emptyBorder",							menuBarEmptyBorder,		// Added by JGoodies 
		"MenuBar.separatorBorder",						menuBarSeparatorBorder,	// Added by JGoodies
		"MenuBar.etchedBorder",							menuBarEtchedBorder,	// Added by JGoodies
		"MenuBar.headerBorder",							menuBarHeaderBorder,	// Added by JGoodies

		"MenuItem.border",								menuItemBorder,
		"MenuItem.checkIcon",	 						menuItemCheckIcon,		// Aligns menu items
		"MenuItem.margin",								menuItemMargin,			// 1.4.1 Bug
        "MenuItem.background", 							getMenuItemBackground(),// Added by JGoodies
		"MenuItem.selectionForeground",					getMenuItemSelectedForeground(),// Added by JGoodies
		"MenuItem.selectionBackground",					getMenuItemSelectedBackground(),// Added by JGoodies
		"MenuItem.acceleratorForeground",				menuItemForeground,
		"MenuItem.acceleratorSelectionForeground",		getMenuItemSelectedForeground(),
		"MenuItem.acceleratorSelectionBackground",		getMenuItemSelectedBackground(),

		"OptionPane.errorIcon",							makeIcon(getClass(), "icons/Error.png"),
        "OptionPane.informationIcon",                   makeIcon(getClass(), "icons/Inform.png"),
        "OptionPane.warningIcon",                       makeIcon(getClass(), "icons/Warn.png"),
        "OptionPane.questionIcon",                      makeIcon(getClass(), "icons/Question.png"),
		
		//"DesktopIcon.icon", 							makeIcon(superclass, "icons/DesktopIcon.gif"),
		"FileView.computerIcon",						makeIcon(getClass(), "icons/Computer.gif"),
		"FileView.directoryIcon",						makeIcon(getClass(), "icons/TreeClosed.gif"),
		"FileView.fileIcon", 							makeIcon(getClass(), "icons/File.gif"),
		"FileView.floppyDriveIcon", 					makeIcon(getClass(), "icons/FloppyDrive.gif"),
		"FileView.hardDriveIcon", 						makeIcon(getClass(), "icons/HardDrive.gif"),
		"FileChooser.homeFolderIcon", 					makeIcon(getClass(), "icons/HomeFolder.gif"),
        "FileChooser.newFolderIcon", 					makeIcon(getClass(), "icons/NewFolder.gif"),
        "FileChooser.upFolderIcon",						makeIcon(getClass(), "icons/UpFolder.gif"),
		"Tree.closedIcon", 								makeIcon(getClass(), "icons/TreeClosed.gif"),
	  	"Tree.openIcon", 								makeIcon(getClass(), "icons/TreeOpen.gif"),
	  	"Tree.leafIcon", 								makeIcon(getClass(), "icons/TreeLeaf.gif"),
			
        "FormattedTextField.border",                    textFieldBorder,            
        "FormattedTextField.margin",                    textInsets,             

		"PasswordField.border",							textFieldBorder,			
        "PasswordField.margin",                         textInsets,             

		"PopupMenu.border",								PlasticBorders.getPopupMenuBorder(),
        "PopupMenu.noMarginBorder",                     PlasticBorders.getNoMarginPopupMenuBorder(),
		"PopupMenuSeparator.margin",					new InsetsUIResource(3, 4, 3, 4),	

		"RadioButton.margin",							checkBoxMargin,					
		"RadioButtonMenuItem.border",					menuItemBorder,
		"RadioButtonMenuItem.checkIcon",				radioButtonMenuItemIcon,
		"RadioButtonMenuItem.margin",					menuItemMargin,			// 1.4.1 Bug
        "RadioButtonMenuItem.background", 				getMenuItemBackground(),// Added by JGoodies
		"RadioButtonMenuItem.selectionForeground",		getMenuItemSelectedForeground(),
		"RadioButtonMenuItem.selectionBackground",		getMenuItemSelectedBackground(),
		"RadioButtonMenuItem.acceleratorForeground",	menuItemForeground,
		"RadioButtonMenuItem.acceleratorSelectionForeground",	getMenuItemSelectedForeground(),
		"RadioButtonMenuItem.acceleratorSelectionBackground",	getMenuItemSelectedBackground(),
		"Separator.foreground",							getControlDarkShadow(),
		"ScrollPane.border",							scrollPaneBorder,
		"ScrollPane.etchedBorder",   					scrollPaneBorder,
//			"ScrollPane.background",					table.get("window"),

		"SimpleInternalFrame.activeTitleForeground",	getSimpleInternalFrameForeground(),
		"SimpleInternalFrame.activeTitleBackground",	getSimpleInternalFrameBackground(),
		
	    "Spinner.border", 								PlasticBorders.getFlush3DBorder(),
	    "Spinner.defaultEditorInsets",				    textInsets,
	    
		"SplitPane.dividerSize",						new Integer(7),
		"TabbedPane.focus",								getFocusColor(),
		"TabbedPane.tabInsets",							new InsetsUIResource(1, 9, 1, 8),
		"Table.foreground",								table.get("textText"),
		"Table.gridColor",								controlColor, //new ColorUIResource(new Color(216, 216, 216)),
        "Table.scrollPaneBorder", 						scrollPaneBorder,
		"TableHeader.cellBorder",						tableHeaderBorder,
		"TextArea.margin",								wrappedTextInsets,	
		"TextField.border",								textFieldBorder,			
		"TextField.margin", 							textInsets,				
		"TitledBorder.font",							getTitleTextFont(),
		"TitledBorder.titleColor",						getTitleTextColor(),
		"ToggleButton.border",							toggleButtonBorder,
		"ToggleButton.margin",							buttonMargin,

		"ToolBar.emptyBorder", 							toolBarEmptyBorder,		// Added by JGoodies
		"ToolBar.separatorBorder", 						toolBarSeparatorBorder,	// Added by JGoodies
		"ToolBar.etchedBorder", 						toolBarEtchedBorder,	// Added by JGoodies
		"ToolBar.headerBorder", 						toolBarHeaderBorder,	// Added by JGoodies

		"ToolTip.hideAccelerator",						Boolean.TRUE,
		
        "Tree.expandedIcon", 							treeExpandedIcon,
        "Tree.collapsedIcon", 							treeCollapsedIcon,
        "Tree.line",									gray,
        "Tree.hash",									gray,
		"Tree.rowHeight",								rowHeight,
		
		"Button.is3DEnabled",							is3D,
		"ComboBox.is3DEnabled",							is3D,
		"MenuBar.is3DEnabled",							is3D,
		"ToolBar.is3DEnabled",							is3D,
		"ScrollBar.is3DEnabled",						is3D,
		"ToggleButton.is3DEnabled",						is3D,

        // 1.4.1 uses a 2 pixel non-standard border, that leads to bad
        // alignment in the typical case that the border is not painted
        "CheckBox.border",                      marginBorder,
        "RadioButton.border",                   marginBorder,
        
        // Fix of the issue #21
        "ProgressBar.selectionForeground",      getSystemTextColor(),
        "ProgressBar.selectionBackground",      getSystemTextColor()
		};
		table.putDefaults(defaults);
        
        // Set paths to sounds for auditory feedback
        String soundPathPrefix = "/javax/swing/plaf/metal/";
        Object[] auditoryCues = (Object[]) table.get("AuditoryCues.allAuditoryCues");
        if (auditoryCues != null) {
            Object[] audioDefaults = new String[auditoryCues.length * 2];
            for (int i = 0; i < auditoryCues.length; i++) {
                Object auditoryCue = auditoryCues[i];
                audioDefaults[2*i]     = auditoryCue;
                audioDefaults[2*i + 1] = soundPathPrefix + table.getString(auditoryCue);
            }
            table.putDefaults(audioDefaults);
        }
	}


    /**
     * Creates and returns the margin used by <code>JButton</code>
     * and <code>JToggleButton</code>. Honors the screen resolution
     * and the global <code>Options.getUseNarrowButtons()</code> property.<p>
     *
     * Sun's L&F implementations use wide button margins.
     * 
     * @return an Insets object that describes the button margin
     * @see Options#getUseNarrowButtons()
     */
    protected Insets createButtonMargin() {
        int pad = Options.getUseNarrowButtons() ? 4 : 14;
        return LookUtils.IS_OS_WINDOWS_VISTA
            ? new InsetsUIResource(0, pad, 1, pad)
            : (LookUtils.IS_LOW_RESOLUTION
                ? new InsetsUIResource(1, pad, 1, pad)
                : new InsetsUIResource(2, pad, 3, pad));
    }


	/**
	 * Unlike my superclass I register a unified shadow color.
	 * This color is used by my ThinBevelBorder class.
     * 
     * @param table   the UIDefaults table to work with
	 */
	protected void initSystemColorDefaults(UIDefaults table) {
		super.initSystemColorDefaults(table);
		table.put("unifiedControlShadow", table.getColor("controlDkShadow"));
		table.put("primaryControlHighlight", getPrimaryControlHighlight());
	}


	// Color Theme Behavior *************************************************************
	
	private static final String THEME_CLASSNAME_PREFIX = "com.jgoodies.looks.plastic.theme.";
	
	/**
	 * Creates and returns the default color theme. Honors the current platform
     * and platform flavor - if available.
     * 
     * @return the default color theme for the current environemt
	 */
	public static PlasticTheme createMyDefaultTheme() {
		String defaultName = LookUtils.IS_LAF_WINDOWS_XP_ENABLED
								? "ExperienceBlue"
								: (LookUtils.IS_OS_WINDOWS_MODERN ? "DesertBluer" : "SkyBlue");
		// Don't use the default now, so we can detect that the users tried to set one.
		String   userName  = LookUtils.getSystemProperty(DEFAULT_THEME_KEY, "");
		boolean overridden = userName.length() > 0;
		String   themeName = overridden ? userName : defaultName;
		PlasticTheme theme = createTheme(themeName);
		PlasticTheme result = theme != null ? theme : new SkyBluer(); 
		
		// In case the user tried to set a theme, log a message.
		if (overridden) {
			String className = theme.getClass().getName().substring(
													THEME_CLASSNAME_PREFIX.length());
			if (className.equals(userName)) {
				LookUtils.log("I have successfully installed the '" + theme.getName() + "' theme.");
			} else {
				LookUtils.log("I could not install the Plastic theme '" + userName + "'.");
				LookUtils.log("I have installed the '" + theme.getName() + "' theme, instead.");
			}
		}
		return result;
		
	}
	
	
	/**
	 * Lazily initializes and returns the <code>List</code> of installed 
     * color themes.
     * 
     * @return a list of installed color/font themes
	 */
	public static List getInstalledThemes() {
		if (null == installedThemes)
			installDefaultThemes();

		Collections.sort(installedThemes, new Comparator() {
			public int compare(Object o1, Object o2) {
				MetalTheme theme1 = (MetalTheme) o1;
				MetalTheme theme2 = (MetalTheme) o2;
				return theme1.getName().compareTo(theme2.getName());
			}
		});

		return installedThemes;
	}
	
	
	/**
	 * Install the default color themes.
	 */
	protected static void installDefaultThemes() {
		installedThemes = new ArrayList();
		String[] themeNames = {
		    "BrownSugar",
		    "DarkStar",
			"DesertBlue",	
		    "DesertBluer",
		    "DesertGreen", 	
		    "DesertRed",
		    "DesertYellow",
			"ExperienceBlue",
			"ExperienceGreen",
			"Silver",
		    "SkyBlue",
		    "SkyBluer",		
		    "SkyGreen",
		    "SkyKrupp",
		    "SkyPink",
		    "SkyRed",
		    "SkyYellow"};
		for (int i = themeNames.length - 1; i >= 0; i--) 
			installTheme(createTheme(themeNames[i]));
	}
	
	
	/**
	 * Creates and returns a color theme from the specified theme name.
     * 
     * @param themeName   the unqualified name of the theme to create
     * @return the associated color theme or <code>null</code> in case of
     *     a problem
	 */
	protected static PlasticTheme createTheme(String themeName) {
	    String className = THEME_CLASSNAME_PREFIX + themeName;
	    try {
		    Class cl = Class.forName(className);
            return (PlasticTheme) (cl.newInstance());
        } catch (ClassNotFoundException e) {
            // Ignore the exception here and log below.
        } catch (IllegalAccessException e) {
            // Ignore the exception here and log below.
	    } catch (InstantiationException e) {
            // Ignore the exception here and log below.
	    }
	    LookUtils.log("Can't create theme " + className);
	    return null;
	}


	/**
	 * Installs a color theme.
     * 
     * @param theme    the theme to install
	 */
	public static void installTheme(PlasticTheme theme) {
		if (null == installedThemes)
			installDefaultThemes();
		installedThemes.add(theme);
	}
    
    
    /**
     * Looks up and returns the PlasticTheme stored in the UIManager.
     * 
     * @return the current PlasticTheme
     */
    public static PlasticTheme getPlasticTheme() {
        AppContext context = AppContext.getAppContext();

        if (cachedAppContext != context) {
            plasticTheme = (PlasticTheme)context.get(THEME_KEY);
                if (plasticTheme == null) {
                    // This will happen in two cases:
                    // . When PlasticLookAndFeel is first being initialized.
                    // . When a new AppContext has been created that hasn't
                    //   triggered UIManager to load a LAF. Rather than invoke
                    //   a method on the UIManager, which would trigger the loading
                    //   of a potentially different LAF, we directly set the
                    //   Theme here.
                    setPlasticTheme(createMyDefaultTheme());
                }
            cachedAppContext = context;
        }
        return plasticTheme;
    }


    /**
     * Sets the theme for colors and fonts used by the Plastic L&amp;F.<p>
     * 
     * After setting the theme, you need to re-install the Look&amp;Feel,
     * as well as update the UI's of any previously created components
     * - just as if you'd change the Look&amp;Feel.
     * 
     * @param theme    the PlasticTheme to be set
     * 
     * @throws NullPointerException   if the theme is null.
     * 
     * @see #getPlasticTheme()
     */
    public static void setPlasticTheme(PlasticTheme theme) {
        if (theme == null)
            throw new NullPointerException("The theme must not be null.");
        
        UIManager.put(THEME_KEY, theme);
        plasticTheme = theme;
        cachedAppContext = AppContext.getAppContext();
        cachedAppContext.put(THEME_KEY, theme);
        
        // Also set the theme in the superclass.
        setCurrentTheme(theme);
    }
    
	/**
     * Looks up and returns the PlasticTheme stored in the UIManager.
     * 
     * @return the current PlasticTheme
     * 
     * @deprecated Replaced by {@link #getPlasticTheme()}.
	 */
	public static PlasticTheme getMyCurrentTheme() {
		return getPlasticTheme();
	}
	
	
	/**
	 * Sets a new <code>PlasticTheme</code> for colors and fonts.
     * 
     * @param theme    the PlasticTheme to be set
     * 
     * @deprecated Replaced by {@link #setPlasticTheme(PlasticTheme)}.
	 */
	public static void setMyCurrentTheme(PlasticTheme theme) {
		setPlasticTheme(theme);
	}
	
	
	// Accessed by ProxyLazyValues ******************************************
	
	public static BorderUIResource getInternalFrameBorder() {
		return new BorderUIResource(PlasticBorders.getInternalFrameBorder()); 
	}
	
	public static BorderUIResource getPaletteBorder() {
		return new BorderUIResource(PlasticBorders.getPaletteBorder()); 
	}
	
	

	// Accessing Theme Colors and Fonts *************************************
	 
	 
	public static ColorUIResource getPrimaryControlDarkShadow() {
		return getPlasticTheme().getPrimaryControlDarkShadow();
	}
	
	public static ColorUIResource getPrimaryControlHighlight() {
		return getPlasticTheme().getPrimaryControlHighlight();
	}
	
	public static ColorUIResource getPrimaryControlInfo() {
		return getPlasticTheme().getPrimaryControlInfo();
	}
	
	public static ColorUIResource getPrimaryControlShadow() {
		return getPlasticTheme().getPrimaryControlShadow();
	}
	
	public static ColorUIResource getPrimaryControl() {
		return getPlasticTheme().getPrimaryControl();
	}
	
	public static ColorUIResource getControlHighlight() {
		return getPlasticTheme().getControlHighlight();
	}
	
	public static ColorUIResource getControlDarkShadow() {
		return getPlasticTheme().getControlDarkShadow();
	}
	
	public static ColorUIResource getControl() {
		return getPlasticTheme().getControl();
	}
	
	public static ColorUIResource getFocusColor() {
		return getPlasticTheme().getFocusColor();
	}
	
	public static ColorUIResource getMenuItemBackground() {
		return getPlasticTheme().getMenuItemBackground();
	}
	
	public static ColorUIResource getMenuItemSelectedBackground() {
		return getPlasticTheme().getMenuItemSelectedBackground();
	}
	
	public static ColorUIResource getMenuItemSelectedForeground() {
		return getPlasticTheme().getMenuItemSelectedForeground();
	}
	
	public static ColorUIResource getWindowTitleBackground() {
		return getPlasticTheme().getWindowTitleBackground();
	}
	
	public static ColorUIResource getWindowTitleForeground() {
		return getPlasticTheme().getWindowTitleForeground();
	}
	
	public static ColorUIResource getWindowTitleInactiveBackground() {
		return getPlasticTheme().getWindowTitleInactiveBackground();
	}
	
	public static ColorUIResource getWindowTitleInactiveForeground() {
		return getPlasticTheme().getWindowTitleInactiveForeground();
	}
	
	public static ColorUIResource getSimpleInternalFrameForeground() {
		return getPlasticTheme().getSimpleInternalFrameForeground();
	}
	
	public static ColorUIResource getSimpleInternalFrameBackground() {
		return getPlasticTheme().getSimpleInternalFrameBackground();
	}
	
	public static ColorUIResource getTitleTextColor() {
		return getPlasticTheme().getTitleTextColor();
	}

	public static FontUIResource getTitleTextFont() {
		return getPlasticTheme().getTitleTextFont();
	}

}