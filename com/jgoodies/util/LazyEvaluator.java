package com.jgoodies.util;

/*
 * Copyright (c) 2002 JGoodies Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of JGoodies Karsten Lentzsch.
 * Use is subject to license terms.
 *
 */

/**
 * This abstract class provides a generic mechanism for lazy evaluation.
 * A value is requested via #value, and will then be computed by
 * #evaluate, if and only if it has not been evaluated before.<p>
 * 
 * Concrete subclasses must implement #evaluate to answer a value.
 *
 * @author Karsten Lentzsch
 */

public abstract class LazyEvaluator {

    private Object   value;              // The computed value.
    private boolean evaluated = false;

    /**
     * Computes and answers the value.
     */
    abstract protected Object evaluate();

    /**
     * Answers the value; computes it if necessary in a synchronized block.
     */
    public final Object value() {
        if (!evaluated) {
            synchronized (this) {
                value = evaluate();
                evaluated = true;
            }
        }
        return value;
    }

}