package com.jgoodies.plaf.common;

/*
 * Copyright (c) 2002 Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of Karsten Lentzsch.  
 * Use is subject to license terms.
 *
 */
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.Icon;

import com.jgoodies.plaf.Options;

/**
 * An <code>Icon</code> with a minimum size that is read from the 
 * <code>UIManager</code> <code>defaultIconSize</code> key.
 *
 * @author Karsten Lentzsch
 */

public class MinimumSizedIcon implements Icon {
	
	private final Icon icon;
	private final int  width;
	private final int  height;
	private final int  xOffset;
	private final int  yOffset;
	
	
	public MinimumSizedIcon() { 
		this(null); 
	}
	
	public MinimumSizedIcon(Icon icon) {
		Dimension minimumSize = Options.getDefaultIconSize();
		this.icon      = icon;
		int iconWidth  = icon == null ? 0 : icon.getIconWidth();
		int iconHeight = icon == null ? 0 : icon.getIconHeight();
		width   = Math.max(iconWidth,  Math.max(20, minimumSize.width));
		height  = Math.max(iconHeight, Math.max(20, minimumSize.height));
		xOffset = Math.max(0, (width  - iconWidth)  / 2);
		yOffset = Math.max(0, (height - iconHeight) / 2);
	}
	
	
	public int getIconHeight() {  return height;	}
	public int getIconWidth()	{  return width;	}
	
	
	public void paintIcon(Component c, Graphics g, int x, int y) {
		if (icon != null)
			icon.paintIcon(c, g, x + xOffset, y + yOffset);
	}
	
	
}