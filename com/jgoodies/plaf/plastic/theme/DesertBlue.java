package com.jgoodies.plaf.plastic.theme;

/*
 * Copyright (c) 2002 Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of Karsten Lentzsch.  
 * Use is subject to license terms.
 *
 */

import javax.swing.UIDefaults;
import javax.swing.plaf.ColorUIResource;

import com.jgoodies.plaf.plastic.PlasticLookAndFeel;
import com.jgoodies.plaf.plastic.PlasticScrollBarUI;


/**
 * A theme with low saturated blue primary colors and a light brown background.
 *
 * @author Karsten Lentzsch
 */
public class DesertBlue extends DesertBluer {
	
	public String getName() { return "Desert Blue"; }


	private static final ColorUIResource secondary2	= new ColorUIResource(148, 144, 140);
	private static final ColorUIResource secondary3	= new ColorUIResource(211, 210, 204); 

	

	protected ColorUIResource getPrimary1()		{ return Colors.GRAY_DARK; }
	protected ColorUIResource getPrimary2()		{ return Colors.BLUE_LOW_MEDIUM; }
	protected ColorUIResource getPrimary3()		{ return Colors.BLUE_LOW_LIGHTEST; }

	protected ColorUIResource getSecondary1()		{ return Colors.GRAY_MEDIUM;}
	protected ColorUIResource getSecondary2()		{ return secondary2; }
	protected ColorUIResource getSecondary3()		{ return secondary3; }
	
	public    ColorUIResource getTitleTextColor()	{ return Colors.BLUE_MEDIUM_DARKEST;}
	
    public ColorUIResource getFocusColor() {
        return PlasticLookAndFeel.useHighContrastFocusColors
            ? Colors.YELLOW_FOCUS
            : Colors.BLUE_MEDIUM_DARK;
    }
	// Background
	public    ColorUIResource getPrimaryControlShadow()		{ return getPrimary3(); }
	public    ColorUIResource getMenuItemSelectedBackground()	{ return getPrimary1(); }
	
	public void addCustomEntriesToTable(UIDefaults table) {
		super.addCustomEntriesToTable(table);
		Object[] uiDefaults = { 
			"ScrollBar.is3DEnabled", 				Boolean.FALSE,
            "ScrollBar.thumbHighlight", 			getPrimaryControlHighlight(),
			PlasticScrollBarUI.MAX_BUMPS_WIDTH_KEY, null,	
		};
		table.putDefaults(uiDefaults);
	}

	
}