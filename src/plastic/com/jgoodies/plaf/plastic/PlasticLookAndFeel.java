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

package com.jgoodies.plaf.plastic;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.Icon;
import javax.swing.UIDefaults;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.BorderUIResource;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.FontUIResource;
import javax.swing.plaf.InsetsUIResource;
import javax.swing.plaf.basic.BasicBorders;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.metal.MetalTheme;

import com.jgoodies.plaf.FontSizeHints;
import com.jgoodies.plaf.LookUtils;
import com.jgoodies.plaf.Options;
import com.jgoodies.plaf.common.MinimumSizedIcon;
import com.jgoodies.plaf.plastic.theme.SkyBluerTahoma;

/**
 * Initializes class and component defaults for the 
 * JGoodies Plastic look&amp;feel.
 *
 * @author Karsten Lentzsch
 * @version $Revision: 1.11 $
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

        
    // State *****************************************************************
        
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
     * The <code>List</code> of installed color themes.
     */
	private static List		 installedThemes;

	/** The current color theme. */	
	private static PlasticTheme myCurrentTheme;
	
	
	/** The look-global state for the 3D enabledment. */
	private static boolean	 is3DEnabled = false;
	
	
	/** The look dependent <code>FontSizeHints</code> */
	private static FontSizeHints fontSizeHints;

	
    /**
     * Constructs the <code>PlasticLookAndFeel</code>.
     */
    public PlasticLookAndFeel() {
        if (null == myCurrentTheme)
            setMyCurrentTheme(createMyDefaultTheme());
    }

    public String getID() {
        return "JGoodies Plastic";
    }
    
    public String getName() {
        return "JGoodies Plastic";
    }
    
    public String getDescription() {
        return "The JGoodies Plastic Look and Feel"
            + " - \u00a9 2001-2004 JGoodies Karsten Lentzsch";
    }
    
	// Special Properties ***************************************************
	
    /**
     * Returns the current <code>FontSizeHints</code>, 
     * where look specific settings shadow the global users defaults 
     * as stored under key <code>FontSizeHints.KEY</code>.
     * 
     * @return the current FontSizeHints
     * @see Options#setGlobalFontSizeHints(FontSizeHints)
     * @see FontSizeHints
     */
    public static FontSizeHints getFontSizeHints() {
        return fontSizeHints != null
            ? fontSizeHints
            : Options.getGlobalFontSizeHints();
    }

    /**
     * Sets <code>FontSizeHints</code> that shadow the global font size hints.
     * 
     * @param newHints   the font size hints to be set
     * @see Options#setGlobalFontSizeHints(FontSizeHints)
     * @see FontSizeHints
     */
    public static void setFontSizeHints(FontSizeHints newHints) {
        fontSizeHints = newHints;
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
	 * Initializes the class defaults, that is, overrides some UI delegates
	 * with JGoodies Plastic implementations.
	 * 
     * @param table   the UIDefaults table to work with
	 * @see javax.swing.plaf.basic.BasicLookAndFeel#getDefaults()
	 */
	protected void initClassDefaults(UIDefaults table) {
		super.initClassDefaults(table);

		String PLASTIC_PREFIX = "com.jgoodies.plaf.plastic.Plastic";
        String COMMON_PREFIX  = "com.jgoodies.plaf.common.ExtBasic";
        
		// Overwrite some of the uiDefaults.
		Object[] uiDefaults = {
				// 3D effect; optional narrow margins
				"ButtonUI",					PLASTIC_PREFIX + "ButtonUI",
				"ToggleButtonUI",			PLASTIC_PREFIX + "ToggleButtonUI",

				// 3D effect
				"ComboBoxUI", 	 			PLASTIC_PREFIX + "ComboBoxUI",
				"ScrollBarUI", 				PLASTIC_PREFIX + "ScrollBarUI",
				"SpinnerUI",				PLASTIC_PREFIX + "SpinnerUI",
				
				// Special borders defined by border style or header style, see LookUtils
				"MenuBarUI",				PLASTIC_PREFIX + "MenuBarUI",
				"ToolBarUI",				PLASTIC_PREFIX + "ToolBarUI",
				
				// Aligns menu icons
                "MenuUI",                   PLASTIC_PREFIX + "MenuUI",
				"MenuItemUI",				COMMON_PREFIX + "MenuItemUI",
				"CheckBoxMenuItemUI",		COMMON_PREFIX + "CheckBoxMenuItemUI",
				"RadioButtonMenuItemUI",	COMMON_PREFIX + "RadioButtonMenuItemUI",

                // Provides a drop shadow
                "PopupMenuUI",             PLASTIC_PREFIX + "PopupMenuUI",
				// Has padding above and below the separator lines				
		        "PopupMenuSeparatorUI",		COMMON_PREFIX + "PopupMenuSeparatorUI",
		       
                // Honors the screen resolution and uses a minimum button width             
                "OptionPaneUI",             PLASTIC_PREFIX + "OptionPaneUI",
               
		        // Uses ClearLook.
				"LabelUI", 					PLASTIC_PREFIX + "LabelUI",
				"PanelUI", 					PLASTIC_PREFIX + "PanelUI",
				"ScrollPaneUI",				PLASTIC_PREFIX + "ScrollPaneUI",
				"SplitPaneUI", 				PLASTIC_PREFIX + "SplitPaneUI",
				
				// Modified icons and lines
				"TreeUI", 					PLASTIC_PREFIX + "TreeUI",
				
				// Just to use Plastic colors
				"InternalFrameUI",			PLASTIC_PREFIX + "InternalFrameUI",
			};
		table.putDefaults(uiDefaults);
        if (!useMetalTabs) {
            // Modified tabs and ability use a version with reduced borders.
            table.put("TabbedPaneUI", PLASTIC_PREFIX + "TabbedPaneUI");
        }
	}


	protected void initComponentDefaults(UIDefaults table) {
		super.initComponentDefaults(table);
		
		Object marginBorder				= new BasicBorders.MarginBorder();
		
        Object buttonBorder				= PlasticBorders.getButtonBorder();
		Object menuItemBorder			= PlasticBorders.getMenuItemBorder();
        Object textFieldBorder			= PlasticBorders.getTextFieldBorder();
        Object toggleButtonBorder		= PlasticBorders.getToggleButtonBorder();

		Object popupMenuBorder			= PlasticBorders.getPopupMenuBorder();
        Object dropShadowPopupMenuBorder= PlasticBorders.getDropShadowPopupMenuBorder();
    	
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
		
		Object emptyBorder				= new BorderUIResource(
											new EmptyBorder(0, 0, 0, 0));
		Border thinLoweredBorder		= PlasticBorders.getThinLoweredBorder();
		Border thinRaisedBorder			= PlasticBorders.getThinRaisedBorder();
		Object statusCellBorder			= new BorderUIResource.CompoundBorderUIResource(
											new EmptyBorder(2, 0, 1, 3),
											thinLoweredBorder);
		
		Color controlColor 				= table.getColor("control");
		
		Object checkBoxIcon				= PlasticIconFactory.getCheckBoxIcon();
		Object checkBoxMargin			= new InsetsUIResource(2, 0, 2, 1); // 1.4.1 uses 2,2,2,2
		
		Object defaultButtonMargin		= LookUtils.createButtonMargin(false);
		Object narrowButtonMargin		= LookUtils.createButtonMargin(true);
		
		// Windows uses 2,2,2,2, but we try to adjust baselines of text and label.
		Object textInsets 			    = new InsetsUIResource(1, 2, 1, 2);
        Object wrappedTextInsets		= new InsetsUIResource(2, 3, 1, 2);
                                                
		Object menuItemMargin			= LookUtils.isLowRes
											? new InsetsUIResource(3, 0, 3, 0)
											: new InsetsUIResource(2, 0, 2, 0);
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
        ColorUIResource gray 			= new ColorUIResource(Color.gray);
		
		Boolean is3D					= new Boolean(is3DEnabled());
		
		Object[] defaults = { 
		"Button.border",								buttonBorder,
		"Button.margin",								defaultButtonMargin,
		"Button.narrowMargin",							narrowButtonMargin,

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

		"PopupMenu.border",								popupMenuBorder,
        "PopupMenu.dropShadowBorder",                   dropShadowPopupMenuBorder,
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
		"ToggleButton.margin",							defaultButtonMargin,
		"ToggleButton.narrowMargin",					narrowButtonMargin,

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
        
                
		// ClearLook Borders ************************************************
		"ClearLook.ScrollPaneReplacementBorder", 		emptyBorder,
		"ClearLook.SplitPaneReplacementBorder",			emptyBorder,
		"ClearLook.ThinLoweredBorder",					thinLoweredBorder,
		"ClearLook.ThinRaisedBorder",					thinRaisedBorder,
		"ClearLook.NetBeansScrollPaneBorder",			emptyBorder,
		"ClearLook.NetBeansSpecialPanelBorder",			emptyBorder,
		"ClearLook.NetBeansStatusCellBorder",			statusCellBorder,
		};
		table.putDefaults(defaults);
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
	
	private static final String THEME_CLASSNAME_PREFIX = "com.jgoodies.plaf.plastic.theme.";
	
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
		PlasticTheme result = theme != null ? theme : new SkyBluerTahoma(); 
		
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
		    "SkyBluerTahoma", 
		    "SkyGreen",
		    "SkyKrupp",
		    "SkyPink",
		    "SkyRed",
		    "SkyYellow"};
		for (int i=themeNames.length - 1; i >= 0; i--) 
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
	 * Gets the current <code>PlasticTheme</code>.
     * 
     * @return the current PlasticTheme
	 */
	public static PlasticTheme getMyCurrentTheme() {
		return myCurrentTheme;
	}
	
	
	/**
	 * Sets a new <code>PlasticTheme</code> for colors and fonts.
     * 
     * @param theme    the PlasticTheme to be set
	 */
	public static void setMyCurrentTheme(PlasticTheme theme) {
		myCurrentTheme = theme;
		setCurrentTheme(theme);
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
		return getMyCurrentTheme().getPrimaryControlDarkShadow();
	}
	
	public static ColorUIResource getPrimaryControlHighlight() {
		return getMyCurrentTheme().getPrimaryControlHighlight();
	}
	
	public static ColorUIResource getPrimaryControlInfo() {
		return getMyCurrentTheme().getPrimaryControlInfo();
	}
	
	public static ColorUIResource getPrimaryControlShadow() {
		return getMyCurrentTheme().getPrimaryControlShadow();
	}
	
	public static ColorUIResource getPrimaryControl() {
		return getMyCurrentTheme().getPrimaryControl();
	}
	
	public static ColorUIResource getControlHighlight() {
		return getMyCurrentTheme().getControlHighlight();
	}
	
	public static ColorUIResource getControlDarkShadow() {
		return getMyCurrentTheme().getControlDarkShadow();
	}
	
	public static ColorUIResource getControl() {
		return getMyCurrentTheme().getControl();
	}
	
	public static ColorUIResource getFocusColor() {
		return getMyCurrentTheme().getFocusColor();
	}
	
	public static ColorUIResource getMenuItemBackground() {
		return getMyCurrentTheme().getMenuItemBackground();
	}
	
	public static ColorUIResource getMenuItemSelectedBackground() {
		return getMyCurrentTheme().getMenuItemSelectedBackground();
	}
	
	public static ColorUIResource getMenuItemSelectedForeground() {
		return getMyCurrentTheme().getMenuItemSelectedForeground();
	}
	
	public static ColorUIResource getWindowTitleBackground() {
		return getMyCurrentTheme().getWindowTitleBackground();
	}
	
	public static ColorUIResource getWindowTitleForeground() {
		return getMyCurrentTheme().getWindowTitleForeground();
	}
	
	public static ColorUIResource getWindowTitleInactiveBackground() {
		return getMyCurrentTheme().getWindowTitleInactiveBackground();
	}
	
	public static ColorUIResource getWindowTitleInactiveForeground() {
		return getMyCurrentTheme().getWindowTitleInactiveForeground();
	}
	
	public static ColorUIResource getSimpleInternalFrameForeground() {
		return getMyCurrentTheme().getSimpleInternalFrameForeground();
	}
	
	public static ColorUIResource getSimpleInternalFrameBackground() {
		return getMyCurrentTheme().getSimpleInternalFrameBackground();
	}
	
	public static ColorUIResource getTitleTextColor() {
		return getMyCurrentTheme().getTitleTextColor();
	}

	public static FontUIResource getTitleTextFont() {
		return getMyCurrentTheme().getTitleTextFont();
	}

}