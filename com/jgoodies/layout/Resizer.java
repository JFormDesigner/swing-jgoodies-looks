package com.jgoodies.layout;

/*
 * Copyright (c) 2003 JGoodies Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of JGoodies Karsten Lentzsch.
 * Use is subject to license terms.
 *
 */
import java.awt.Dimension;

import javax.swing.JComponent;

/**
 * Assists in getting aesthetic aspect ratios in panel and dialog layout.
 *
 * @author Karsten Lentzsch
 */

public final class Resizer {

    private static final float  SQRT_RATIO    = 1.0f / (float) Math.sqrt(2.0d);
    private static final float  DEFAULT_RATIO = 4.0f / 3.0f;
    private static final int    TOP_GUESS     = 6;
    private static final int    MARGIN_GUESS  = 25;

    public static final Resizer DEFAULT       = new Resizer(DEFAULT_RATIO);
    public static final Resizer SQRT          = new Resizer(SQRT_RATIO);
    public static final Resizer ONE2ONE       = new Resizer(1);
    public static final Resizer FIVE2FOUR     = new Resizer(5.0f / 4.0f);

    private final float ratio;


    /**
     * Construct a <code>Resizer</code> for the specified aspect ratio.
     */
    public Resizer(float ratio) {
        this.ratio = ratio;
    }


    /**
     * Answers an aesthetic dimension for the given dimension, using my height-width-ratio.
     */
    public Dimension from(Dimension original) {
        int width = original.width + TOP_GUESS;
        int height = original.height + MARGIN_GUESS;
        int expandedWidth = (int) Math.round(height * ratio);
        int expandedHeight = (int) Math.round(width / ratio);
        if (width < expandedWidth) {
            return new Dimension(
                expandedWidth - TOP_GUESS,
                height - MARGIN_GUESS);
        } else {
            return new Dimension(
                width - TOP_GUESS,
                expandedHeight - MARGIN_GUESS);
        }
    }


    /**
     * Answers an aesthetic dimension for the given height, using my height-width-ratio.
     */
    public Dimension fromHeight(int height) {
        return new Dimension((int) Math.round(height * ratio), height);
    }


    /**
     * Answers an aesthetic dimension for the given with, using my height-width-ratio.
     */
    public Dimension fromWidth(int width) {
        return new Dimension(width, (int) Math.round(width / ratio));
    }


    /**
     * Resizes the given component to give it an aesthetic height-width-ratio.
     */
    public void resize(JComponent component) {
        component.setPreferredSize(from(component.getPreferredSize()));
    }
}