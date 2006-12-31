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

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.IndexColorModel;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.Icon;

/**
 * The bumps used in the JGoodies Plastic Look&amp;Feel.
 *
 * @author Sun
 * @version $Revision: 1.3 $
 */
final class PlasticBumps implements Icon {

	protected int xBumps;
	protected int yBumps;
	protected Color topColor;
	protected Color shadowColor;
	protected Color backColor;

	protected static Vector buffers = new Vector();
	protected BumpBuffer buffer;

	public PlasticBumps(Dimension bumpArea) {
		this(bumpArea.width, bumpArea.height);
	}

	public PlasticBumps(int width, int height) {
		this(width, height,
			PlasticLookAndFeel.getPrimaryControlHighlight(),
			PlasticLookAndFeel.getPrimaryControlDarkShadow(),
			PlasticLookAndFeel.getPrimaryControlShadow());
	}

	public PlasticBumps(int width, int height,
		Color newTopColor, Color newShadowColor, Color newBackColor) {
		setBumpArea(width, height);
		setBumpColors(newTopColor, newShadowColor, newBackColor);
	}

	private BumpBuffer getBuffer(GraphicsConfiguration gc, 
		Color aTopColor, Color aShadowColor, Color aBackColor) {
		if (buffer != null
			&& buffer.hasSameConfiguration(gc, aTopColor, aShadowColor, aBackColor)) {
			return buffer;
		}
		BumpBuffer result = null;
		Enumeration elements = buffers.elements();
		while (elements.hasMoreElements()) {
			BumpBuffer aBuffer = (BumpBuffer) elements.nextElement();
			if (aBuffer.hasSameConfiguration(gc, aTopColor, aShadowColor, aBackColor)) {
				result = aBuffer;
				break;
			}
		}
		if (result == null) {
			result = new BumpBuffer(gc, topColor, shadowColor, backColor);
			buffers.addElement(result);
		}
		return result;
	}

	public void setBumpArea(Dimension bumpArea) {
		setBumpArea(bumpArea.width, bumpArea.height);
	}

	public void setBumpArea(int width, int height) {
		xBumps = width / 2;
		yBumps = height / 2;
	}

	public void setBumpColors(Color newTopColor, Color newShadowColor, Color newBackColor) {
		topColor = newTopColor;
		shadowColor = newShadowColor;
		backColor = newBackColor;
	}

	public void paintIcon(Component c, Graphics g, int x, int y) {
		GraphicsConfiguration gc = (g instanceof Graphics2D)
				? (GraphicsConfiguration) ((Graphics2D) g).getDeviceConfiguration()
				: null;

		buffer = getBuffer(gc, topColor, shadowColor, backColor);

		int bufferWidth = buffer.getImageSize().width;
		int bufferHeight = buffer.getImageSize().height;
		int iconWidth = getIconWidth();
		int iconHeight = getIconHeight();
		int x2 = x + iconWidth;
		int y2 = y + iconHeight;
		int savex = x;

		while (y < y2) {
			int h = Math.min(y2 - y, bufferHeight);
			for (x = savex; x < x2; x += bufferWidth) {
				int w = Math.min(x2 - x, bufferWidth);
				g.drawImage(buffer.getImage(), x, y, x + w, y + h, 0, 0, w, h, null);
			}
			y += bufferHeight;
		}
	}

	public int getIconWidth()  { return xBumps * 2; }
	public int getIconHeight() { return yBumps * 2; }
}

final class BumpBuffer {

	static final int IMAGE_SIZE = 64;
	static Dimension imageSize = new Dimension(IMAGE_SIZE, IMAGE_SIZE);

	transient Image image;
	Color topColor;
	Color shadowColor;
	Color backColor;
	private GraphicsConfiguration gc;

	public BumpBuffer(
		GraphicsConfiguration gc,
		Color aTopColor,
		Color aShadowColor,
		Color aBackColor) {
		this.gc = gc;
		topColor = aTopColor;
		shadowColor = aShadowColor;
		backColor = aBackColor;
		createImage();
		fillBumpBuffer();
	}

	public boolean hasSameConfiguration(
		GraphicsConfiguration aGC,
		Color aTopColor,
		Color aShadowColor,
		Color aBackColor) {
		if (this.gc != null) {
			if (!this.gc.equals(aGC)) {
				return false;
			}
		} else if (aGC != null) {
			return false;
		}
		return topColor.equals(aTopColor)
			&& shadowColor.equals(aShadowColor)
			&& backColor.equals(aBackColor);
	}

	/**
	 * Returns the Image containing the bumps appropriate for the passed in
	 * <code>GraphicsConfiguration</code>.
	 */
	public Image getImage() { return image; }

	public Dimension getImageSize() { return imageSize; }

	/**
	 * Paints the bumps into the current image.
	 */
	private void fillBumpBuffer() {
		Graphics g = image.getGraphics();

		g.setColor(backColor);
		g.fillRect(0, 0, IMAGE_SIZE, IMAGE_SIZE);

		g.setColor(topColor);
		for (int x = 0; x < IMAGE_SIZE; x += 4) {
			for (int y = 0; y < IMAGE_SIZE; y += 4) {
				g.drawLine(x, y, x, y);
				g.drawLine(x + 2, y + 2, x + 2, y + 2);
			}
		}

		g.setColor(shadowColor);
		for (int x = 0; x < IMAGE_SIZE; x += 4) {
			for (int y = 0; y < IMAGE_SIZE; y += 4) {
				g.drawLine(x + 1, y + 1, x + 1, y + 1);
				g.drawLine(x + 3, y + 3, x + 3, y + 3);
			}
		}
		g.dispose();
	}

	/**
	 * Creates the image appropriate for the passed in
	 * <code>GraphicsConfiguration</code>, which may be null.
	 */
	private void createImage() {
		if (gc != null) {
			image = gc.createCompatibleImage(IMAGE_SIZE, IMAGE_SIZE);
		} else {
			int[] cmap = { backColor.getRGB(), topColor.getRGB(), shadowColor.getRGB()};
			IndexColorModel icm =
				new IndexColorModel(8, 3, cmap, 0, false, -1, DataBuffer.TYPE_BYTE);
			image = new BufferedImage(IMAGE_SIZE, IMAGE_SIZE, BufferedImage.TYPE_BYTE_INDEXED, icm);
		}
	}
}
