package com.jgoodies.swing.util;

/*
 * Copyright (c) 2003 JGoodies Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of JGoodies Karsten Lentzsch.
 * Use is subject to license terms.
 *
 */
import java.awt.Component;
import java.awt.Graphics;

import javax.swing.Icon;

/**
 * An <code>Icon</code> implementation that paints two component icons 
 * as compound or overlayed icon using a specified alignment.
 *
 * @author Karsten Lentzsch
 */

public final class CompoundIcon implements Icon {

    /**
     * Put the foreground icon at the top of its display area,
     * centered horizontally. 
     */
    public static final int CENTER = 0;

    /**
     * Put the foreground icon at the top of its display area,
     * centered horizontally. 
     */
    public static final int NORTH = 1;

    /**
     * Put the foreground icon at the top-right corner of its display area. 
     */
    public static final int NORTHEAST = 2;

    /**
     * Put the foreground icon on the right side of its display area, 
     * centered vertically.
     */
    public static final int EAST = 3;

    /**
     * Put the foreground icon at the bottom-right corner of its display area. 
     */
    public static final int SOUTHEAST = 4;

    /**
     * Put the foreground icon at the bottom of its display area, centered 
     * horizontally. 
     */
    public static final int SOUTH = 5;

    /**
     * Put the foreground icon at the bottom-left corner of its display area. 
     */
    public static final int SOUTHWEST = 6;

    /**
     * Put the foreground icon on the left side of its display area, 
     * centered vertically.
     */
    public static final int WEST = 7;

    /**
     * Put the foreground icon at the top-left corner of its display area. 
     */
    public static final int NORTHWEST = 8;

    private final Icon backgroundIcon;
    private final Icon foregroundIcon;
    private final int  height;
    private final int  width;
    private int xOffset;
    private int yOffset;

    /**
     * Constructs a compound icon for the given forground and background icons,
     * using a default anchor.
     */
    public CompoundIcon(Icon backgroundIcon, Icon foregroundIcon) {
        this(backgroundIcon, foregroundIcon, SOUTHEAST);
    }

    /**
     * Constructs a compound icon for the given forground and background icons,
     * using the specified anchor.
     */
    public CompoundIcon(Icon backgroundIcon, Icon foregroundIcon, int anchor) {
        this.backgroundIcon = backgroundIcon;
        this.foregroundIcon = foregroundIcon;
        height = Math.max(
                backgroundIcon.getIconHeight(),
                foregroundIcon.getIconHeight());
        width  = Math.max(
                backgroundIcon.getIconWidth(),
                foregroundIcon.getIconWidth());
        setAnchor(anchor);
    }

    public int getIconHeight() {
        return height;
    }
    
    public int getIconWidth() {
        return width;
    }

    public void paintIcon(Component c, Graphics g, int x, int y) {
        backgroundIcon.paintIcon(c, g, x, y);
        foregroundIcon.paintIcon(c, g, x + xOffset, y + yOffset);
    }

    private void setAnchor(int anchor) {
        int xDiff = backgroundIcon.getIconWidth() - foregroundIcon.getIconWidth();
        int yDiff = backgroundIcon.getIconHeight() - foregroundIcon.getIconHeight();

        xOffset = ((anchor == NORTHWEST)
                || (anchor == WEST)
                || (anchor == SOUTHWEST))
                ? 0
                : (((anchor == NORTH)
                    || (anchor == CENTER)
                    || (anchor == SOUTH))
                    ? xDiff / 2
                    : xDiff);

        yOffset = ((anchor == NORTHWEST)
                || (anchor == NORTH)
                || (anchor == NORTHEAST))
                ? 0
                : (((anchor == WEST) || (anchor == CENTER) || (anchor == EAST))
                    ? yDiff / 2
                    : yDiff);
    }

}