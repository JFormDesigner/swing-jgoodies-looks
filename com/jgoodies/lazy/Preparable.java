package com.jgoodies.lazy;

/*
 * Copyright (c) 2003 JGoodies Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of JGoodies Karsten Lentzsch.
 * Use is subject to license terms.
 *
 */

/**
 * An interface that defines how to prepare an object or class.
 * It can be used to increase an application's responsiveness by eagerly
 * loading classes, prebuilding layouts and invoking lazy objects.
 * 
 * @author Karsten Lentzsch
 * @see	BackgroundLoader
 */

public interface Preparable {

    /**
     * Prepares an object or class. For example loads classes, prebuilds
     * a layout or invokes a lazy object.
     */
    void prepare();

}