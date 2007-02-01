/*
 * Copyright (c) 2001-2007 JGoodies Karsten Lentzsch. All Rights Reserved.
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

import javax.swing.UIDefaults;

/**
 * Intializes class and component defaults for the JGoodies Plastic3D
 * look&amp;feel.
 *
 * @author Karsten Lentzsch
 * @version $Revision: 1.5 $
 */
public class Plastic3DLookAndFeel extends PlasticLookAndFeel {

    /**
     * Constructs the JGoodies Plastic3D look&amp;feel.
     */
    public Plastic3DLookAndFeel() {
    }

    public String getID() {
        return "JGoodies Plastic 3D";
    }
    
    public String getName() {
        return "JGoodies Plastic 3D";
    }
    
    public String getDescription() {
        return "The JGoodies Plastic 3D Look and Feel"
            + " - \u00a9 2001-2007 JGoodies Karsten Lentzsch";
    }

    protected boolean is3DEnabled() {
        return true;
    }

    /**
     * Initializes the Plastic3D component defaults.
     * 
     * @param table   the UIDefaults table to work with
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