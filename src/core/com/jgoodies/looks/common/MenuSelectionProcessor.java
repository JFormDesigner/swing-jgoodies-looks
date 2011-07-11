/*
 * Copyright (c) 2001-2011 JGoodies Karsten Lentzsch. All Rights Reserved.
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

import java.awt.KeyEventPostProcessor;
import java.awt.Window;
import java.awt.event.KeyEvent;

import javax.swing.*;
import javax.swing.plaf.basic.ComboPopup;

import com.sun.java.swing.plaf.windows.WindowsRootPaneUI;

/**
 * Handles the Alt key to select the first menu in the menu bar - if any.
 * Useful to let non-Windows L&amp;fs like Plastic feel more like Windows.
 *
 * @see WindowsRootPaneUI
 */
public final class MenuSelectionProcessor implements KeyEventPostProcessor {

    private boolean altKeyPressed = false;
    private boolean menuCanceledOnPress = false;

    public boolean postProcessKeyEvent(KeyEvent ev) {
        if (ev.isConsumed()) {
            return false;
        }
        if (ev.getKeyCode() == KeyEvent.VK_ALT) {
            if (ev.getID() == KeyEvent.KEY_PRESSED) {
                if (!altKeyPressed) {
                    altPressed(ev);
                }
                altKeyPressed = true;
                return true;
            } else if (ev.getID() == KeyEvent.KEY_RELEASED) {
                if (altKeyPressed) {
                    altReleased(ev);
                }
                altKeyPressed = false;
            }
        } else {
            altKeyPressed = false;
        }
        return false;
    }


    private void altPressed(KeyEvent ev) {
        MenuSelectionManager msm =
            MenuSelectionManager.defaultManager();
        MenuElement[] path = msm.getSelectedPath();
        if (path.length > 0 && ! (path[0] instanceof ComboPopup)) {
            msm.clearSelectedPath();
            menuCanceledOnPress = true;
            ev.consume();
        } else if (path.length > 0) { // we are in a combo box
            menuCanceledOnPress = false;
            ev.consume();
        } else {
            menuCanceledOnPress = false;
            JMenuBar mbar = getMenuBar(ev);
            JMenu menu = mbar != null ? mbar.getMenu(0) : null;
            if (menu != null) {
                ev.consume();
            }
        }
    }


    private void altReleased(KeyEvent ev) {
        if (menuCanceledOnPress) {
            return;
        }
        MenuSelectionManager msm = MenuSelectionManager.defaultManager();
        if (msm.getSelectedPath().length == 0) {
            // If no menu is active, we try activating the menu bar.
            JMenuBar mbar = getMenuBar(ev);
            JMenu menu = mbar != null ? mbar.getMenu(0) : null;
            if (menu != null) {
                MenuElement[] path = new MenuElement[2];
                path[0] = mbar;
                path[1] = menu;
                msm.setSelectedPath(path);
            }
        }
    }


    private static JMenuBar getMenuBar(KeyEvent ev) {
        JRootPane root = SwingUtilities.getRootPane(ev.getComponent());
        Window winAncestor = root == null ? null : SwingUtilities.getWindowAncestor(root);
        JMenuBar mbar = root != null ? root.getJMenuBar() : null;
        if(mbar == null && winAncestor instanceof JFrame) {
            mbar = ((JFrame)winAncestor).getJMenuBar();
        }
        return mbar;
    }

}
