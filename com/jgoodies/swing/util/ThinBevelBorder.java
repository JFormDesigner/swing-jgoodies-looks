package com.jgoodies.swing.util;

/*
 * Copyright (c) 2002 Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of Karsten Lentzsch.  
 * Use is subject to license terms.
 *
 */
 
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.UIManager;
import javax.swing.border.AbstractBorder;

/**
 * This is just a 1-point line border that uses a pseudo-3D effect,
 * either raised or lowered.
 *
 * @author Karsten Lentzsch
 */

public final class ThinBevelBorder extends AbstractBorder {
	
	public static final int RAISED  = 0;
	public static final int LOWERED = 1;
	
	private static final Insets INSETS = new Insets(1, 1, 1, 1);

	private final int mode;
	
	
	/**
	 * Constructs a thin bevel border, either raised or lowered.
	 */
	public ThinBevelBorder(int mode) { this.mode = mode; }
	
	
	public Insets getBorderInsets(Component c) { return INSETS; }
	
	
	private Color getShadowColor() {
		Color unifiedShadow = UIManager.getColor("unifiedControlShadow");
		return unifiedShadow != null ? unifiedShadow : UIManager.getColor("controlShadow");
	}
	
	
	public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
		int w = width;
		int h = height;

		g.translate(x, y);

		Color down = getShadowColor();
		Color up = UIManager.getColor("controlLtHighlight");

		g.setColor(mode == LOWERED ? down : up);
		g.drawLine(0, 1, 0, h - 2); // left
		g.drawLine(0, 0, w - 1, 0); // top

		g.setColor(mode == LOWERED ? up : down);
		g.drawLine(w - 1, 0, w - 1, h - 1);
		g.drawLine(0, h - 1, w - 1, h - 1);

		g.translate(-x, -y);
	}
	
}