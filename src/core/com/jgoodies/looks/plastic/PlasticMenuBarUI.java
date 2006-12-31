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

import java.awt.Graphics;
import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComponent;
import javax.swing.LookAndFeel;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicMenuBarUI;

import com.jgoodies.looks.BorderStyle;
import com.jgoodies.looks.HeaderStyle;
import com.jgoodies.looks.Options;


/**
 * The JGoodies Plastic look and feel implemenation of <code>MenuBarUI</code>.
 * Can handle optional <code>Border</code> types as specified by the 
 * <code>BorderStyle</code> or <code>HeaderStyle</code> client properties.
 *
 * @author  Karsten Lentzsch
 * @version $Revision: 1.3 $
 */
public final class PlasticMenuBarUI extends BasicMenuBarUI {
	
	
	private PropertyChangeListener listener;
	
	
	public static ComponentUI createUI(JComponent b) {
		return new PlasticMenuBarUI();
	}
	
	
	// Handling Special Borders *********************************************
	
	protected void installDefaults() {
		super.installDefaults();
		installSpecialBorder();
	}
	
	
	protected void installListeners() {
		super.installListeners();
		listener = createBorderStyleListener();
		menuBar.addPropertyChangeListener(listener);
	}
	
	
	protected void uninstallListeners() {
		menuBar.removePropertyChangeListener(listener);
		super.uninstallListeners();
	}
	
	
	private PropertyChangeListener createBorderStyleListener() {
		return new PropertyChangeListener() {
			
			public void propertyChange(PropertyChangeEvent e) {
				String prop = e.getPropertyName();
				if (prop.equals(Options.HEADER_STYLE_KEY) || 
				    prop.equals(PlasticLookAndFeel.BORDER_STYLE_KEY)) {
				   PlasticMenuBarUI.this.installSpecialBorder();
				}
			}
			
		};
	}
	
	
	/**
	 * Installs a special border, if either a look-dependent <code>BorderStyle</code> 
	 * or a look-independent <code>HeaderStyle</code> has been specified.
	 * A look specific <code>BorderStyle</code> shadows a <code>HeaderStyle</code>.<p>
	 * 
	 * We recommend to specify a <code>HeaderStyle</code>.
	 */	
	public void installSpecialBorder() {
		String suffix;
		BorderStyle borderStyle = BorderStyle.from(menuBar, 
												PlasticLookAndFeel.BORDER_STYLE_KEY);
		if (borderStyle == BorderStyle.EMPTY)
			suffix = "emptyBorder";
		else if (borderStyle == BorderStyle.ETCHED)
			suffix = "etchedBorder";
		else if (borderStyle == BorderStyle.SEPARATOR)
			suffix = "separatorBorder";
		else {
			HeaderStyle headerStyle = HeaderStyle.from(menuBar);
			if (headerStyle == HeaderStyle.BOTH)
				suffix = "headerBorder";
			else if (headerStyle == HeaderStyle.SINGLE && is3D())
				suffix = "etchedBorder";
			else
				return;
		}

		LookAndFeel.installBorder(menuBar, "MenuBar." + suffix);
	}
	

	// 3D Effect ************************************************************************
		
	public void update(Graphics g, JComponent c) {
		if (c.isOpaque()) {
			g.setColor(c.getBackground());
			g.fillRect(0, 0, c.getWidth(), c.getHeight());
            if (is3D()) {
                Rectangle bounds =
                    new Rectangle(0, 0, c.getWidth(), c.getHeight());
                PlasticUtils.addLight3DEffekt(g, bounds, true);
            }
        }

		paint(g, c);
	}
	
	
	/**
	 * Checks and answers if we should add a pseudo 3D effect.
	 */
	private boolean is3D() {
		if (PlasticUtils.force3D(menuBar))
			return true;
		if (PlasticUtils.forceFlat(menuBar))
			return false;
		return	PlasticUtils.is3D("MenuBar.") && 
				(HeaderStyle.from(menuBar) != null) &&
				(BorderStyle.from(menuBar, PlasticLookAndFeel.BORDER_STYLE_KEY) 
					!= BorderStyle.EMPTY);
	}
	
}