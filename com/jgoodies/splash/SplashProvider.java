package com.jgoodies.splash;

/*
 * Copyright (c) 2003 JGoodies Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of JGoodies Karsten Lentzsch.
 * Use is subject to license terms.
 *
 */

/**
 * An interface that defines splash providers as used by the
 * {@link Splash} class. A typical implementation will open a window, that
 * displays the progress messages and progress percent and will close and
 * dispose the window on #closeSplash().
 * 
 * @author Karsten Lentzsch
 * @see	Splash
 */

public interface SplashProvider {

    /**
     * Indicates that the splash should be opened.
     */
    void openSplash();

    /**
     * Indicates that the splash should be closed.
     */
    void closeSplash();

    /**
     * Sets the splash text message.
     */
    void setNote(String message);

    /**
     * Sets the splash progress.
     */
    void setProgress(int percent);

}