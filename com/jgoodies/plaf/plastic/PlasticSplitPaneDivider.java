package com.jgoodies.plaf.plastic;

/*
 * Copyright (c) 2002 Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of Karsten Lentzsch.  
 * Use is subject to license terms.
 *
 */

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;


/**
 * Paints a single drag symbol instead of many bumps.
 */
public final class PlasticSplitPaneDivider extends BasicSplitPaneDivider {
	
	
	public PlasticSplitPaneDivider(BasicSplitPaneUI ui) {
		super(ui);
	}
	
	
	public void paint(Graphics g) {
		Dimension size = getSize();
		Color bgColor = getBackground();

		if (bgColor != null) {
			g.setColor(bgColor);
			g.fillRect(0, 0, size.width, size.height);
		}
		
		/*
		Object value = splitPane.getClientProperty("add3D");
		if (value != null && value.equals(Boolean.TRUE)) {
			Rectangle r = new Rectangle(0, 0, size.width, size.height);
			FreebopUtils.addLight3DEffekt(g, r, true, false);
		}
		*/
		
		//paintDragRectangle(g);
		super.paint(g);
	}
	
	/*
	private void paintDragRectangle(Graphics g) {
		Dimension size = getSize();
		int xCenter = size.width / 2;
		int yCenter = size.height / 2;
		int x = xCenter - 2;
		int y = yCenter - 2;
		int w = 4;
		int h = 4;

		Color down = UIManager.getColor("controlDkShadow");
		Color up   = UIManager.getColor("controlHighlight");

		g.translate(x, y);

		g.setColor(down);
		g.drawLine(0, 1, 0, h - 1); // left
		g.drawLine(0, 0, w - 1, 0); // top

		g.setColor(up);
		g.drawLine(w - 1, 1, w - 1, h - 1);
		g.drawLine(1, h - 1, w - 1, h - 1);

		g.translate(-x, -y);

		super.paint(g);
	}
	
	
	private void paintDragLines(Graphics g) {
		Dimension size = getSize();
		Color bgColor = getBackground();

		if (bgColor != null) {
			g.setColor(bgColor);
			g.fillRect(0, 0, size.width, size.height);
		}

		int xCenter = size.width / 2;
		int yCenter = size.height / 2;
		int y0 = yCenter - 10;
		int y1 = yCenter + 10;

		Color dark = UIManager.getColor("controlDkShadow");
		int bars = 3;

		g.setColor(dark);
		for (int i = 0; i < bars; i++) {
			int x = 2 * i + xCenter - bars;
			g.drawLine(x, y0, x, y1);
		}

		super.paint(g);
	}
	*/
}