package com.jgoodies.plaf.plastic.theme;

/*
 * Copyright (c) 2003 Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of Karsten Lentzsch.  
 * Use is subject to license terms.
 *
 */

import javax.swing.plaf.ColorUIResource;

import com.jgoodies.plaf.plastic.PlasticLookAndFeel;

/**
 * A theme with medium saturated blue primary colors and a light brown background.
 *
 * @author Karsten Lentzsch
 */
public class DesertBluer extends SkyBluerTahoma {
	
	private final ColorUIResource primary1	= new ColorUIResource( 10,  36, 106);
	private final ColorUIResource primary2	= new ColorUIResource( 85, 115, 170);
	private final ColorUIResource primary3	= new ColorUIResource(172, 210, 248);

	private final ColorUIResource secondary2	= new ColorUIResource(148, 144, 140);
	private final ColorUIResource secondary3	= new ColorUIResource(212, 208, 200);
	
	
	public String getName() { return "Desert Bluer"; }
	

	protected ColorUIResource getPrimary1()					{ return primary1; }
	protected ColorUIResource getPrimary2()					{ return primary2; }
	protected ColorUIResource getPrimary3()					{ return primary3; }
	protected ColorUIResource getSecondary1()					{ return Colors.GRAY_MEDIUM;}
	protected ColorUIResource getSecondary2()					{ return secondary2; }
	protected ColorUIResource getSecondary3()					{ return secondary3; }

	public    ColorUIResource getTextHighlightColor()			{ return getPrimary1(); }
	public    ColorUIResource getHighlightedTextColor()		{ return getWhite(); }
	public    ColorUIResource getMenuItemSelectedBackground()	{ return getPrimary1(); }

    public ColorUIResource getFocusColor() {
        return PlasticLookAndFeel.useHighContrastFocusColors
            ? Colors.ORANGE_FOCUS
            : super.getFocusColor();
    }

}