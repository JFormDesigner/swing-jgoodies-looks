/*
 * Copyright (c) 2001-2005 JGoodies Karsten Lentzsch. All Rights Reserved.
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

package com.jgoodies.looks.plastic.theme;

import java.awt.Font;

import com.jgoodies.looks.LookUtils;

/**
 * A theme with medium saturated blue primary colors and a light brown 
 * window background.<p>
 * 
 * Unlike its superclass, DesertBluer, this class uses a font appropriate 
 * for displaying Chinese, Korean, Japanese and other non-western characters.
 *
 * @author Karsten Lentzsch
 * @version $Revision: 1.1 $
 * 
 * @see Font
 */
public final class DesertBluerDefaultFont extends DesertBluer {
	
	
	public String getName() { return "Desert Bluer - Default Font"; }
	

    /**
     * Looks up and returns the logical default font "dialog".
     * This is appropriate for Chinese, Korean, Japanese and other
     * non-western characters. 
     * Overrides the superclass' "Tahoma" choice.
     * 
     * @see SkyBluerTahoma#getFont0(int)
     */
    protected Font getFont0(int size) {
        int defaultSize = LookUtils.IS_LOW_RESOLUTION ? 13 : 16;
        return new Font("dialog", Font.PLAIN, defaultSize);
    }

}