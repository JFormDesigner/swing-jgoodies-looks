/*
 * Copyright (c) 2003 JGoodies Karsten Lentzsch. All Rights Reserved.
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

package com.jgoodies.plaf.windows;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.plaf.BorderUIResource;
import javax.swing.plaf.ComponentUI;

import com.jgoodies.clearlook.ClearLookManager;
import com.jgoodies.clearlook.ClearLookMode;
import com.jgoodies.plaf.Options;
import com.sun.java.swing.plaf.windows.WindowsScrollPaneUI;

/**
 * The JGoodies Windows Look&amp;Feel implementation of
 * <code>ScrollPaneUI</code>.
 * <p>
 * Can replace obsolete <code>Border</code>s and use optional etched borders.
 * 
 * @author Karsten Lentzsch
 */
public final class ExtWindowsScrollPaneUI extends WindowsScrollPaneUI {

    private static final Border DEBUG_ETCHED_BORDER =
        new BorderUIResource(new LineBorder(Color.yellow));

    // Stores the original border, in case we replace it.
    private Border storedBorder;

    // Have we already checked the parent container?
    private boolean hasCheckedBorderReplacement = false;

    // Have we already checked the type of the top-level container?
    private boolean hasCheckedTopLevelContainer = false;

    // Do we use an etchedBorder
    private boolean hasEtchedBorder = false;

    private PropertyChangeListener listener;

    public static ComponentUI createUI(JComponent b) {
        return new ExtWindowsScrollPaneUI();
    }

    protected void installDefaults(JScrollPane scrollPane) {
        super.installDefaults(scrollPane);
        installEtchedBorder(scrollPane);
    }

    protected void installEtchedBorder(JScrollPane scrollPane) {
        Object value = scrollPane.getClientProperty(Options.IS_ETCHED_KEY);
        if (Boolean.TRUE.equals(value)) {
            LookAndFeel.installBorder(scrollPane, "ScrollPane.etchedBorder");
            hasEtchedBorder = true;
        }
    }

    // Managing the Etched Property *******************************************

    protected void installListeners(JScrollPane scrollPane) {
        super.installListeners(scrollPane);
        listener = createBorderStyleListener();
        scrollPane.addPropertyChangeListener(listener);
    }

    protected void uninstallListeners(JComponent c) {
        ((JScrollPane) c).removePropertyChangeListener(listener);
        super.uninstallListeners(c);
    }

    private PropertyChangeListener createBorderStyleListener() {
        return new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent e) {
                String prop = e.getPropertyName();
                if (prop.equals(Options.IS_ETCHED_KEY)) {
                    JScrollPane scrollPane = (JScrollPane) e.getSource();
                    ExtWindowsScrollPaneUI.this.installEtchedBorder(scrollPane);
                }
            }

        };
    }

    /**
     * Replaces the scrollpane's <code>Border</code> if appropriate,
     * then paints.
     */
    public void paint(Graphics g, JComponent c) {
        if (hasEtchedBorder) {
            super.paint(g, c);
            return;
        }

        if (!hasCheckedBorderReplacement) {
            storedBorder = ClearLookManager.replaceBorder(c);
            hasCheckedBorderReplacement = true;
        }
        if (!hasCheckedTopLevelContainer && storedBorder == null) {
            ClearLookMode mode = ClearLookManager.getMode();
            JScrollPane scrollPane = (JScrollPane) c;
            Component viewportView = scrollPane.getViewport().getView();
            if (mode.isEnabled()
                && (!(viewportView instanceof JTable) && isInFrame(c))) {
                if (mode.isDebug())
                    c.setBorder(DEBUG_ETCHED_BORDER);
                else
                    LookAndFeel.installBorder(
                        scrollpane,
                        "ScrollPane.etchedBorder");
                if (mode.isVerbose()) {
                    ClearLookManager.log(
                        "Set scrollpane border nested in JFrame to etched.");
                }
            }
            hasCheckedTopLevelContainer = true;
        }
        super.paint(g, c);
    }

    /**
     * Restores the original <code>Border</code>, in case we replaced it.
     */
    protected void uninstallDefaults(JScrollPane scrollPane) {
        if (storedBorder != null) {
            scrollPane.setBorder(storedBorder);
        }
        super.uninstallDefaults(scrollPane);
    }

    /**
     * Checks and ansers if the top-level container of 
     * the specified component is a <code>JFrame</code>.
     */
    private boolean isInFrame(JComponent c) {
        for (Container p = c.getParent(); p != null; p = p.getParent()) {
            if (p instanceof JFrame || p instanceof JInternalFrame) {
                return true;
            } else if (p instanceof JDialog) {
                return false;
            }
        }
        return false;
    }

}