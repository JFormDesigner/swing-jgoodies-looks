package com.jgoodies.swing.panels;

/*
 * Copyright (c) 2002 Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of Karsten Lentzsch.  
 * Use is subject to license terms.
 *
 */
 
import java.awt.*;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.UIManager;

/**
 * A panel with a gradient background; provides three background types:
 * <ul>
 * <li> a simple white area for environments that don't have true colors,
 * <li> a horizontal left to right gradient, 
 * <li> a diagonal gradient.
 * </ul>
 *
 * @author Karsten Lentzsch
 */

public class GradientBackgroundPanel extends JPanel {
	
	
	private boolean horizontal;
	
    
	/**
	 * Constructs a <code>GradientBackgroundPanel</code> with a horizontal
	 * gradient background.
	 */
	public GradientBackgroundPanel() {
		this(true);
	}
	

	/**
	 * Constructs a <code>GradientBackgroundPanel</code> with the
	 * specified gradient orientation.
	 */
	public GradientBackgroundPanel(boolean horizontal) {
		super(new BorderLayout());
		this.horizontal = horizontal;
	}
	

	/**
	 * Constructs a <code>GradientBackgroundPanel</code> with the
	 * specified foreground component and gradient orientation.
	 */
	public GradientBackgroundPanel(JComponent foreground, boolean horizontal) {
		this(horizontal);
		foreground.setOpaque(false);
		add(foreground);
	}
	

	/**
	 * Overridden to paint a special background.
	 */
	protected void paintComponent(Graphics g) {
		paintBackground(g, this, getWidth(), getHeight(), isHorizontal());
	}
	

	/**
	 * Answers if the background gradient should be painted horizontally
	 * or diagonally.
	 */	
	protected boolean isHorizontal() {
		return horizontal;
	}
	
	
	/**
	 * Paints a background, either plain white, horizontal gradient,
	 * or diagonal gradient background.
	 */
	public static void paintBackground(Graphics g, 
		Component c, int width, int height, boolean horizontal) {
		Graphics2D g2 = (Graphics2D) g;
		int x, y, w, h;
		Rectangle clip = g2.getClipBounds();
		if (clip != null) {
			x = clip.x; y = clip.y; w = clip.width; h = clip.height;
		} else {
			x = 0;      y = 0;      w = width;      h = height;
		}
		if (!isTrueColor(c)) {
			paintWhiteBackground(g2, x, y, w, h);
		} else if (horizontal) {
			paintHorizontalGradient(g2, x, y, w, h, width, height);
		} else {
		    paintDiagonalGradient(g2, x, y, w, h, width, height);
		}
	}
	
	
	/**
	 * Paints a plain white background.
	 */
	private static void paintWhiteBackground(Graphics2D g2, 
								int x, int y, int w, int h) {
		g2.setColor(Color.white);
		g2.fillRect(x, y, w, h);
	}
	
	
	/**
	 * Paints a horizontal gradient background from white to the control color.
	 */
	private static void paintHorizontalGradient(Graphics2D g2, 
								int x, int y, int w, int h, int width, int height) {
		Color gradientColor = getHorizontalGradientColor(UIManager.getColor("control"));
		g2.setPaint(new GradientPaint(100, height, Color.white, width, height, gradientColor));
		g2.fillRect(x, y, w, h);
	}
	
	
	/**
	 * Paints a diagonal gradient background, using a darkened control color.
	 */
	private static void paintDiagonalGradient(Graphics2D g2, 
								int x, int y, int w, int h, int width, int height) {
		Color gradientColor = getDiagonalGradientColor(UIManager.getColor("control"));
		g2.setColor(Color.white);
		g2.fillRect(x, y, w, h);
		g2.setPaint(new GradientPaint(width-50, height-300, new Color(255, 255, 255, 0), width, height, gradientColor));
		g2.fillRect(x, y, w, h);
		Composite composite = g2.getComposite();
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
		g2.setPaint(new GradientPaint(width-300, height-200, new Color(255, 255, 255, 0), width, height, gradientColor));
		g2.fillRect(x, y, w, h);
		g2.setComposite(composite);
	}
	
	
 	/**
	 * Computes and answers a <code>Color</code> that has a minimum brightness. 
	 */
	private static Color getHorizontalGradientColor(Color color) {
		float minimumBrightness = 0.7f;
		float[] hsbValues = new float[3];
		Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), hsbValues);
		float brightness = hsbValues[2]; 
		if (brightness > minimumBrightness)
			return color;
		float hue        = hsbValues[0];
		float saturation = hsbValues[1];
		return Color.getHSBColor(hue, saturation, Math.max(minimumBrightness, brightness));
	}

 	/**
	 * Computes and answers a <code>Color</code> that has a minimum
	 * and maximum brightness and is slightly darker than the 
	 * specified <code>Color</code>. 
	 */
	private static Color getDiagonalGradientColor(Color color) {
		float[] hsbValues = new float[3];
		Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), hsbValues);
		float hue = hsbValues[0];
		float saturation = hsbValues[1];
		float brightness = hsbValues[2]; 
		if (brightness > 0.8f)
			brightness *= 0.85f;
		brightness = Math.max(0.6f, Math.min(0.80f, brightness));
		return Color.getHSBColor(hue, saturation, brightness);
	}


	/**
	 * Checks and answers whether we have a true color system.
	 */
	private static boolean isTrueColor(Component c) { 
		return c.getToolkit().getColorModel().getPixelSize() >= 24;
	}
	
}