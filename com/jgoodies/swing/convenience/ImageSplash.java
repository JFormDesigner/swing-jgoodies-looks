package com.jgoodies.swing.convenience;

/*
 * Copyright (c) 2003 JGoodies Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of JGoodies Karsten Lentzsch.
 * Use is subject to license terms.
 *
 */

import java.awt.*;

import com.jgoodies.splash.SplashProvider;
import com.jgoodies.util.ScreenUtils;

/**
 * This implementation of the {@link SplashProvider} interface uses an
 * AWT <code>Window</code> to display a splash image and an optional progress
 * bar. It has been optimized for quick startup.
 * <p>
 * 
 * @author	Karsten Lentzsch
 * @see	com.jgoodies.splash.Splash
 * @see	com.jgoodies.splash.SplashProvider
 */

public final class ImageSplash extends Window implements SplashProvider {

    private static final int DEFAULT_BAR_WIDTH  = 100;
    private static final int DEFAULT_BAR_HEIGHT =  10;
    private static final int VPAD               =  10;

    private final Image     image;
    private final boolean   showProgress;
    private final String    text;
    
    private Color           textColor;
    private Rectangle       progressBarBounds;
    private int             percent;


    // Instance Creation ****************************************************

    /**
     * Constructs an AWT based splash for the given <code>image</code> using a
     * default <code>Frame</code> and has no progress indication.
     */
    public ImageSplash(Image image) {
        this(image, false);
    }

    /**
     * Constructs an AWT based splash for the given <code>image</code>
     * using a default <code>Frame</code> that has an optional progress bar.
     */
    public ImageSplash(Image image, boolean showProgress) {
        this(new Frame(), image, "Loading...", showProgress);
    }

    /**
     * Constructs an AWT based splash for the given <code>Frame</code>,
     * <code>Image</code>, <code>text</code>, that has an optional progress bar.
     */
    public ImageSplash(
        Frame owner,
        Image image,
        String text,
        boolean showProgress) {
        super(owner);
        this.image = image;
        this.text = text;
        this.percent = 0;
        this.showProgress = showProgress;
        setSize(image.getWidth(null), image.getHeight(null));
        setProgressBarBounds(VPAD);
        setForeground(Color.darkGray);
        setBackground(Color.lightGray);
        textColor = Color.black;
        ScreenUtils.locateOnScreenCenter(this);
    }

    // Setting Properties ***************************************************

    /**
     * Returns the text color.
     * 
     * @return the text color
     */
    public Color getTextColor() {
        return textColor;
    }
    
    /**
     * Sets a new text color.
     * 
     * @param newTextColor   the new text color
     */
    public void setTextColor(Color newTextColor) {
        textColor = newTextColor;
    }
    
    
    /**
     * Sets the bounds for the progress bar using the given <code>Rectangle</code>.
     */
    public void setProgressBarBounds(Rectangle r) {
        progressBarBounds = new Rectangle(r);
    }

    /**
     * Sets the bounds for the progress bar using a pad from the dialog's bottom.
     */
    public void setProgressBarBounds(int bottomPad) {
        setProgressBarBounds(defaultProgressBarBounds(bottomPad));
    }

    /**
     * Answers the progress bar's default bounds using a pad from the dialog's bottom.
     */
    private Rectangle defaultProgressBarBounds(int bottomPad) {
        int x = (getWidth() - DEFAULT_BAR_WIDTH) / 2;
        int y = getHeight() - DEFAULT_BAR_HEIGHT - bottomPad;
        return new Rectangle(x, y, DEFAULT_BAR_WIDTH, DEFAULT_BAR_HEIGHT);
    }

    // Painting *************************************************************

    /**
     * Renders the image and optionally a progress bar with a text.
     */
    public void paint(Graphics g) {
        // Check whether we are about to refresh the progress bar.
        boolean clipIsProgressRect =
            progressBarBounds.equals(g.getClipBounds());

        if (image != null && (!showProgress || !clipIsProgressRect)) {
            g.drawImage(image, 0, 0, this);
        }
        if (showProgress) {
            int x = progressBarBounds.x;
            int y = progressBarBounds.y;
            int w = progressBarBounds.width;
            int h = progressBarBounds.height;
            int progressWidth = (w - 2) * percent / 100;
            int progressHeight = h - 2;

            g.translate(x, y);
            // Paint border
            g.setColor(Color.gray);
            g.drawLine(0, 0, w - 2, 0);
            g.drawLine(0, 0, 0, h - 1);
            g.setColor(Color.white);
            g.drawLine(0, h - 1, w - 1, h - 1);
            g.drawLine(w - 1, 0, w - 1, h - 1);
            // Paint background
            g.setColor(getBackground());
            g.fillRect(1, 1, w - 2, progressHeight);
            // Paint progress bar
            g.setColor(getForeground());
            g.fillRect(1, 1, progressWidth, progressHeight);
            g.translate(-x, -y);

            if (!clipIsProgressRect) {
                FontMetrics fm = getFontMetrics(g.getFont());
                int textWidth = fm.stringWidth(text);
                int textX = (getWidth() - textWidth) / 2;
                g.setColor(textColor);
                g.drawString(text, textX, progressBarBounds.y - VPAD / 2);
            }
        }
    }

    /**
     * Opens the splash window.
     */
    public void openSplash() {
        setVisible(true);
    }

    /**
     * Closes and disposes the splash window.
     */
    public void closeSplash() {
        dispose();
    }

    /**
     * Sets a new progress value.
     */
    public void setProgress(int percent) {
        if (!showProgress)
            return;
        this.percent = percent;
        repaint(
            progressBarBounds.x,
            progressBarBounds.y,
            progressBarBounds.width,
            progressBarBounds.height);
    }

    /**
     * Does nothing; progress messages are ignored.
     */
    public void setNote(String message) {}

}