/*
 * Copyright (c) 2003 JGoodies Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of JGoodies Karsten Lentzsch.  
 * Use is subject to license terms. 
 *
 */

package com.jgoodies.clearlook;

import javax.swing.JComponent;
import javax.swing.border.Border;


/**
 * Describes a ClearLook(tm) policy as used by the {@link ClearLookManager}.
 *
 * @author Karsten Lentzsch
 * @see	ClearLookManager
 * @see	DefaultClearLookPolicy
 */
public interface ClearLookPolicy {

	/**
	 * Answers the policy's name.
	 */
	String getName();
	
	
	/**
	 * Detects if the component's border should be replaced.
	 * In this case, the original border is returned, <code>null</code>otherwise.
	 */
	Border replaceBorder(JComponent component);

}