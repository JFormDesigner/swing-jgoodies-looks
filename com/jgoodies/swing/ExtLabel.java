package com.jgoodies.swing;

/*
 * Copyright (c) 2003 Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of Karsten Lentzsch.  
 * Use is subject to license terms.
 *
 */
 
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JLabel;
import javax.swing.plaf.FontUIResource;


/**
 * Adds support for font changes and anti-aliasing to <code>JLabel</code>.
 * 
 * @author Karsten Lentzsch
 */

public final class ExtLabel extends JLabel {
	
	private final int			fontExtraSize;
	private final int			fontStyle;
	private final boolean	antialiased;
	
	
	/**
	 * Constructs an <code>ExtLabel</code> for the given text,
	 * font style, font extra size, and anti-aliasing state.
	 */
	public ExtLabel(String text, boolean antialiased) {
		this(text, Font.PLAIN, 0, antialiased);
	}
	
	
	/**
	 * Constructs an <code>ExtLabel</code> for the given text,
	 * font style, font extra size, and anti-aliasing state.
	 */
	public ExtLabel(String text, int fontStyle, int fontExtraSize, boolean antialiased) {
		super(text);
		this.fontStyle		= fontStyle;
		this.fontExtraSize	= fontExtraSize;
		this.antialiased	= antialiased;
		updateUI();
	}
	
	
	/**
	 * Paints the label: switches on anti-aliasing, if necessary.
	 */
	public void paint(Graphics g) {
		Object oldHint = null;
		Graphics2D g2 = (Graphics2D) g;
		
		if (antialiased) {
			oldHint = g2.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
								RenderingHints.VALUE_ANTIALIAS_ON);
		}
		super.paint(g2);
		if (oldHint != null) 
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, oldHint);
	}
	
	
	/**
	 * Restores the fonts after the UI has changed.
	 */
	public void updateUI() {
		super.updateUI();
		Font font = getFont();
		if (0 == fontExtraSize) {
			if (font.getStyle() != fontStyle)
				setFont(new FontUIResource(font.deriveFont(fontStyle)));
		} else
			setFont(new FontUIResource(new Font(font.getName(), fontStyle, font.getSize() + fontExtraSize)));
	}
}