package com.jgoodies.plaf.motif;

/*
 * Copyright (c) 2002 Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of Karsten Lentzsch.  
 * Use is subject to license terms.
 *
 */

import javax.swing.UIDefaults;

import com.sun.java.swing.plaf.motif.MotifLookAndFeel;

/**
 * Installs class and component defaults for the JGoodies Motif look&feel.<p>
 * 
 * This look&feel is based on Sun's Motif look&feel implementation;
 * it modifies font defaults for <code>TextArea</code> and <code>TextPane</code>,
 * and adds several <code>Icons</code>.
 *
 * @author Karsten Lentzsch
 */
public final class ExtMotifLookAndFeel extends MotifLookAndFeel {
	
	public String getDescription() { return "JGoodies Motif Look and Feel";	}
	public String getID()			{ return "JGoodies Motif";					}
	public String getName()		{ return "JGoodies Motif";					}
	
	
	/**
	 * Initializes component defaults; override two fonts and adds some icons.
	 */
	protected void initComponentDefaults(UIDefaults table) {
		super.initComponentDefaults(table);
		Class superclass = getClass().getSuperclass();
	

		Object[] defaults = {
	 	    "TextArea.font", 				table.get("TextField.font"),
		    "TextPane.font", 				table.get("TextField.font"),
		    
			"DesktopIcon.icon", 			makeIcon(superclass, "icons/DesktopIcon.gif"),
			"FileView.directoryIcon",		makeIcon(superclass, "icons/TreeClosed.gif"),
			"FileView.fileIcon", 			makeIcon(superclass, "icons/File.gif"),
			"FileView.computerIcon",		makeIcon(superclass, "icons/Computer.gif"),
			"FileView.floppyDriveIcon", 	makeIcon(getClass(), "icons/FloppyDrive.gif"),
	
			"OptionPane.errorIcon", 		makeIcon(superclass, "icons/Error.gif"),
		    "OptionPane.informationIcon",	makeIcon(superclass, "icons/Inform.gif"),
		    "OptionPane.warningIcon", 		makeIcon(superclass, "icons/Warn.gif"),
		    "OptionPane.questionIcon", 		makeIcon(superclass, "icons/Question.gif"),
	
	  		"Tree.openIcon", 				makeIcon(superclass, "icons/TreeOpen.gif"),
			"Tree.closedIcon", 				makeIcon(superclass, "icons/TreeClosed.gif")
		};
	
		table.putDefaults(defaults);
	}
}
