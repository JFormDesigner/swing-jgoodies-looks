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

package com.jgoodies.looks;

import javax.swing.plaf.InsetsUIResource;

/**
 * Describes the insets and margins used by a Look&amp;Feel or theme.
 * 
 * @author  Karsten Lentzsch
 * @version $Revision: 1.1 $
 * 
 * @since 2.1
 */
public final class MicroLayout {
    
    private final InsetsUIResource buttonMargin;
    private final InsetsUIResource commandButtonMargin;
    private final int comboBorderSize;
    private final int comboPopupBorderSize;
    private final InsetsUIResource menuItemMargin;
    private final InsetsUIResource menuMargin;
    private final InsetsUIResource popupMenuSeparatorMargin;
    private final InsetsUIResource textInsets;
    
    
    // Instance Creation ******************************************************
    
    public MicroLayout(
            InsetsUIResource textInsets,
            InsetsUIResource buttonMargin, 
            InsetsUIResource commandButtonMargin, 
            int comboBorderSize,
            int comboPopupBorderSize,
            InsetsUIResource menuItemMargin,
            InsetsUIResource menuMargin,
            InsetsUIResource popupMenuSeparatorMargin) {
        this.textInsets = textInsets;
        this.buttonMargin = buttonMargin;
        this.commandButtonMargin = commandButtonMargin;
        this.comboBorderSize = comboBorderSize;
        this.comboPopupBorderSize = comboPopupBorderSize;
        this.menuItemMargin = menuItemMargin;
        this.menuMargin = menuMargin;
        this.popupMenuSeparatorMargin = popupMenuSeparatorMargin;
    }
    
    // Getters ****************************************************************

    public InsetsUIResource getTextInsets() {
        return textInsets;
    }
    
    public InsetsUIResource getButtonMargin() {
        return buttonMargin;
    }

    public InsetsUIResource getCommandButtonMargin() {
        return commandButtonMargin;
    }

    public int getComboBorderSize() {
        return comboBorderSize;
    }
    
    public int getComboPopupBorderSize() {
        return comboPopupBorderSize;
    }

    public InsetsUIResource getMenuItemMargin() {
        return menuItemMargin;
    }
    
    public InsetsUIResource getMenuMargin() {
        return menuMargin;
    }
    
    public InsetsUIResource getPopupMenuSeparatorMargin() {
        return popupMenuSeparatorMargin;
    }
    
}
