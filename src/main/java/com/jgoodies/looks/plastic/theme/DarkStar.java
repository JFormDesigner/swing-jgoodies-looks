/*
 * Copyright (c) 2001-2015 JGoodies Software GmbH. All Rights Reserved.
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
 *  o Neither the name of JGoodies Software GmbH nor the names of
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

package com.jgoodies.looks.plastic.theme;

import javax.swing.plaf.ColorUIResource;


/**
 * An inverted theme with light foreground colors and a black
 * window background.
 *
 * @author Karsten Lentzsch
 * @version $Revision: 1.8 $
 */
public class DarkStar extends InvertedColorTheme {

	private final ColorUIResource softWhite  = new ColorUIResource(154, 154, 154);

	private final ColorUIResource primary1   = new ColorUIResource( 83,  83,  61); 
	private final ColorUIResource primary2   = new ColorUIResource(115, 107,  82); 
	private final ColorUIResource primary3   = new ColorUIResource(156, 156, 123); 

	private final ColorUIResource secondary1 = new ColorUIResource( 32,  32,  32); 
	private final ColorUIResource secondary2 = new ColorUIResource( 96,  96,  96); 
	private final ColorUIResource secondary3 = new ColorUIResource( 84,  84,  84); 


	@Override
    public String getName() { return "Dark Star"; }


	@Override
    protected ColorUIResource getPrimary1()    { return primary1; }
	@Override
    protected ColorUIResource getPrimary2()    { return primary2; }
	@Override
    protected ColorUIResource getPrimary3()    { return primary3; }
	@Override
    protected ColorUIResource getSecondary1()  { return secondary1; }
	@Override
    protected ColorUIResource getSecondary2()  { return secondary2; }
	@Override
    protected ColorUIResource getSecondary3()  { return secondary3; }
	@Override
    protected ColorUIResource getSoftWhite()   { return softWhite; }
}
