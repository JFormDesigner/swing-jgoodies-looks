package com.jgoodies.util;

/*
 * Copyright (c) 2002 JGoodies Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of JGoodies Karsten Lentzsch.
 * Use is subject to license terms.
 *
 */
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;

/**
 * This class consists only of static convenince behavior for screen handling.
 *
 * @author Karsten Lentzsch
 */

public final class ScreenUtils {

    // Override default constructor - prevents instantiation.
    private ScreenUtils() {}

    /**
     * Resizes the given component if necessary to have at least the given
     * minimum size.
     */
    public static void ensureMinimumSize(Component component, Dimension minimumSize) {
        if (null == minimumSize)
            return;

        Dimension size = component.getSize();
        int width = size.width;
        int height = size.height;
        if ((width < minimumSize.width) || (height < minimumSize.height)) {
            Dimension newSize =
                new Dimension(
                    Math.max(width, minimumSize.width),
                    Math.max(height, minimumSize.height));
            component.setSize(newSize);
        }
    }

    private static Dimension screenSize() {
        return Toolkit.getDefaultToolkit().getScreenSize();
    }

    /**
     * Answers if the screen dimension is equal or larger than 1024x768 pixel.
     */
    public static boolean has1024x768Screen() {
        Dimension screenSize = screenSize();
        return (screenSize.width >= 1024) && (screenSize.height >= 768);
    }

    /**
     * Answers if the screen dimension is equal or larger than 800x600 pixel.
     */
    public static boolean has800x600Screen() {
        Dimension screenSize = screenSize();
        return (screenSize.width >= 800) && (screenSize.height >= 600);
    }

    /**
     * Checks and answers whether the component is nearly as large as the screen is.
     */
    public static boolean isNearlyFullScreen(Component component) {
        final int HORIZONTAL_TOLERANCE = 20;
        final int VERTICAL_TOLERANCE = 40;
        Dimension screenSize = screenSize();

        return (component.getWidth()  > (screenSize.width - HORIZONTAL_TOLERANCE))
             && (component.getHeight() > (screenSize.height - VERTICAL_TOLERANCE));
    }

    /**
     * Locates the given component on the screen's center.
     */
    public static void locateOnScreenCenter(Component component) {
        Dimension paneSize = component.getSize();
        Dimension screenSize = component.getToolkit().getScreenSize();
        component.setLocation(
            (screenSize.width  - paneSize.width)  / 2,
            (screenSize.height - paneSize.height) / 2);
    }

    /**
     * Locates the given component on the screen's north-east.
     */
    public static void locateOnScreenNorthEast(Component component) {
        Dimension paneSize   = component.getSize();
        Dimension screenSize = component.getToolkit().getScreenSize();
        component.setLocation((screenSize.width - paneSize.width), 0);
    }

    /**
     * Locates the given component on the screen's west.
     */
    public static void locateOnScreenWest(Component component) {
        Dimension paneSize   = component.getSize();
        Dimension screenSize = component.getToolkit().getScreenSize();
        component.setLocation(0, (screenSize.height - paneSize.height) / 2);
    }

    /**
     * Locates the given component on the screen's east.
     */
    public static void locateOnScreenEast(Component component) {
        Dimension paneSize   = component.getSize();
        Dimension screenSize = component.getToolkit().getScreenSize();
        component.setLocation(
            (screenSize.width  - paneSize.width),
            (screenSize.height - paneSize.height) / 2);
    }

}