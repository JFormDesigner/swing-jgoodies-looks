package com.jgoodies.plaf.plastic.theme;

/*
 * Copyright (c) 2002 Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of Karsten Lentzsch.  
 * Use is subject to license terms.
 *
 */

import javax.swing.plaf.ColorUIResource;

/**
 * An inverted theme with light foreground colors and a dark brown background.
 *
 * @author Karsten Lentzsch
 */
public class BrownSugar extends InvertedColorTheme {
	
	private final ColorUIResource softWhite  = new ColorUIResource(165, 157, 143);  

	private final ColorUIResource primary1   = new ColorUIResource( 83,  83,  61); //90,  90,  66);// Dunkel: Rollbalkenrahmen-Dunkel
	private final ColorUIResource primary2   = new ColorUIResource(115, 107,  82); //132, 123,  90);// Mittel: Rollbalkenhintergrund
	private final ColorUIResource primary3   = new ColorUIResource(156, 156, 123); //148, 140, 107); //181, 173, 148); // Hell:   Ordnerfläche, Selektion, Rollbalken-Hoch, Menühintergrund

	private final ColorUIResource secondary1 = new ColorUIResource( 35,  33,  29); // Abwärts  (dunkler)73,  59,  23);
	private final ColorUIResource secondary2 = new ColorUIResource(105,  99,  87); // Aufwärts (heller)136, 112,  46);
	private final ColorUIResource secondary3 = new ColorUIResource( 92,  87,  76); // Fläche   134, 104,  22);  
	
	
	public String getName() { return "Brown Sugar"; }
	
	
	protected ColorUIResource getPrimary1() { return primary1; }
	protected ColorUIResource getPrimary2() { return primary2; }
	protected ColorUIResource getPrimary3() { return primary3; }
	protected ColorUIResource getSecondary1() { return secondary1; }
	protected ColorUIResource getSecondary2() { return secondary2; }
	protected ColorUIResource getSecondary3() { return secondary3; }
	protected ColorUIResource getSoftWhite() { return softWhite; }
	
}
