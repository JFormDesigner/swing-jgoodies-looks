/*
 * Copyright (c) 2005 JGoodies Karsten Lentzsch. All Rights Reserved.
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

package com.jgoodies.looks.common;

import java.awt.Component;

import javax.swing.LookAndFeel;
import javax.swing.Popup;
import javax.swing.PopupFactory;

import com.jgoodies.looks.Options;


/**
 * The JGoodies Looks implementation of <code>PopupFactory</code>. 
 * Adds a drop shadow border to all popups except ComboBox popups. 
 * It is installed by the JGoodies Plastic L&amp;F, as well as by
 * the JGoodies Windows L&amp;F during the Look&amp;Feel initialization,
 * see {@link com.jgoodies.looks.plastic.PlasticLookAndFeel#initialize} and 
 * {@link com.jgoodies.looks.windows.WindowsLookAndFeel#initialize}.<p>
 * 
 * This factory shall not be used on platforms that provide native drop shadows,
 * such as the Mac OS X. Therefore the invocation of the {@link #install()}
 * method will have no effect on such platforms.<p>
 * 
 * <strong>Note:</strong> To be used in a sandbox environment, this PopupFactory
 * requires two AWT permissions: <code>createRobot</code> and
 * <code>readDisplayPixels</code>. The reason for it is, that in the case of
 * the heavy weight popups this PopupFactory uses a Robot to snapshot 
 * the screen background to simulate the drop shadow effect.
 * 
 * @author Andrej Golovnin
 * @author Karsten Lentzsch
 * @version $Revision: 1.1 $
 * 
 * @see java.awt.AWTPermission
 * @see java.awt.Robot
 * @see javax.swing.Popup
 * @see LookAndFeel#initialize
 * @see LookAndFeel#uninitialize
 */
public final class ShadowPopupFactory extends PopupFactory {

    /**
     * In the case of heavy weight popups, snapshots of the screen background
     * will be stored as client properties of the popup contents' parent.
     * These snapshots will be used by the popup border to simulate the drop
     * shadow effect. The two following constants define the names of
     * these client properties. 
     * 
     * @see com.jgoodies.looks.common.ShadowPopupBorder
     */
    static final String PROP_HORIZONTAL_BACKGROUND = "jgoodies.hShadowBg";
    static final String PROP_VERTICAL_BACKGROUND   = "jgoodies.vShadowBg";

    /**
     * The PopupFactory used before this PopupFactory has been installed
     * in <code>#install</code>. Used to restored the original state
     * in <code>#uninstall</code>. 
     */
    private final PopupFactory storedFactory;

    
    // Instance Creation ******************************************************

    private ShadowPopupFactory(PopupFactory storedFactory) {
        this.storedFactory = storedFactory;
    }
    

    // API ********************************************************************

    /**
     * Installs the ShadowPopupFactory as the shared popup factory. 
     * Also stores the previously set factory in the replacement,
     * so that it can be restored in <code>#uninstall</code>.
     * 
     * @see #uninstall
     */
    public static void install() {
        PopupFactory factory = PopupFactory.getSharedInstance();
        if (factory instanceof ShadowPopupFactory) 
            return;
        
        PopupFactory.setSharedInstance(new ShadowPopupFactory(factory));
    }

    /**
     * Uninstalls the ShadowPopupFactory and restores the original 
     * popup factory as the new shared popup factory.
     * 
     * @see #install
     */
    public static void uninstall() {
        PopupFactory factory = PopupFactory.getSharedInstance();
        if (!(factory instanceof ShadowPopupFactory)) 
            return;
        
        PopupFactory stored = ((ShadowPopupFactory) factory).storedFactory;
        PopupFactory.setSharedInstance(stored);
    }
    

    /** {@inheritDoc} */
    public Popup getPopup(Component owner, Component contents, int x, int y)
            throws IllegalArgumentException {
        Popup popup = storedFactory.getPopup(owner, contents, x, y);
        return Options.isPopupDropShadowActive()
            ? ShadowPopup.getInstance(owner, contents, x, y, popup)
            : popup;
    }

}