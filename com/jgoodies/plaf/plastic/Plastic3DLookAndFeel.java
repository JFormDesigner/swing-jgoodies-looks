package com.jgoodies.plaf.plastic;

/*
 * Copyright (c) 2003 JGoodies Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of JGoodies Karsten Lentzsch.
 * Use is subject to license terms.
 *
 */

import javax.swing.UIDefaults;

/**
 * Intializes class and component defaults for the JGoodies Plastic3D
 * look&amp;feel.
 *
 * @author Karsten Lentzsch
 */
public class Plastic3DLookAndFeel extends PlasticLookAndFeel {

    /**
     * Constructs the JGoodies Plastic3D look&feel.
     */
    public Plastic3DLookAndFeel() {
        if (null == getMyCurrentTheme())
            setMyCurrentTheme(createMyDefaultTheme());
    }

    public String getID() {
        return "JGoodies Plastic 3D";
    }
    
    public String getName() {
        return "JGoodies Plastic 3D";
    }
    
    public String getDescription() {
        return "The JGoodies Plastic 3D Look and Feel"
            + " - \u00a9 2003 JGoodies Karsten Lentzsch";
    }

    protected boolean is3DEnabled() {
        return true;
    }

    /**
     * Initializes the Plastic3D component defaults.
     */
    protected void initComponentDefaults(UIDefaults table) {
        super.initComponentDefaults(table);

        Object menuBarBorder = PlasticBorders.getThinRaisedBorder();
        Object toolBarBorder = PlasticBorders.getThinRaisedBorder();

        Object[] defaults =  {
            "MenuBar.border",               menuBarBorder,
            "ToolBar.border",               toolBarBorder,
        };
        table.putDefaults(defaults);
    }

    protected static void installDefaultThemes() {}
}