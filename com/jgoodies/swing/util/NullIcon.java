package com.jgoodies.swing.util;

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

/**
 * An implementation of the <code>Icon</code> interface that 
 * has a size but does not render any content.
 *
 * @author Karsten Lentzsch
 */
public final class NullIcon implements Icon {
	
	private final Dimension size;
	
	/**
	 * Constructs a <code>NullIcon</code> with the given size.
	 */
	public NullIcon(Dimension size) { this.size = size; 	}
	
	
	public int getIconHeight()	{ return size.height;	}
	public int getIconWidth() 	{ return size.width;	}
	
	public void paintIcon(Component c, Graphics g, int x, int y) {}
}