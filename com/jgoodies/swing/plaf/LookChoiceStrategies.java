package com.jgoodies.swing.plaf;

/*
 * Copyright (c) 2003 JGoodies Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of JGoodies Karsten Lentzsch.
 * Use is subject to license terms.
 *
 */

import com.jgoodies.plaf.LookUtils;
import com.jgoodies.plaf.Options;

/**
 * Provides three convenience implementations for the 
 * <code>LookChoiceStrategy</code> with the following behavior:
 * 
 * <table>
 * <tr>
 *      <td>&nbsp</td>
 *      <td colspan="4"><b>Strategy</b></td>
 * </tr>
 * <tr>
 *      <td><b>OS</b></td>
 *      <td>FIXED_3D</td>
 *      <td>FIXED_XP</td>
 *      <td>SYSTEM</td>
 *      <td>DEFAULT</td>
 * </tr>
 * <tr>
 *      <td>Windows 95/98/NT/ME/2000</td>
 *      <td>Plastic3D</td>
 *      <td>PlasticXP</td>
 *      <td>ExtWindows</td>
 *      <td>ExtWindows</td>
 * </tr>
 * <tr>
 *      <td>Windows XP</td>
 *      <td>Plastic3D</td>
 *      <td>PlasticXP</td>
 *      <td>ExtWindows</td>
 *      <td>Plastic3D</td>
 * </tr>
 * <tr>
 *      <td>Mac OS X</td>
 *      <td>Plastic3D</td>
 *      <td>PlasticXP</td>
 *      <td>Aqua</td>
 *      <td>Aqua</td>
 * </tr>
 * <tr>
 *      <td>Other</td>
 *      <td>Plastic3D</td>
 *      <td>PlasticXP</td>
 *      <td>Plastic3D</td>
 *      <td>Plastic3D</td>
 * </tr>
 * </table>
 *
 * @see     LookChoiceStrategy
 * 
 * @author  Karsten Lentzsch
 */
 
public final class LookChoiceStrategies {
	

    // Convenience Strategies for Choosing a Look ***************************

    public static final LookChoiceStrategy DEFAULT  = new DefaultStrategy();
    public static final LookChoiceStrategy SYSTEM   = new SystemStrategy();
    public static final LookChoiceStrategy FIXED_3D = new Fixed3DStrategy();
    public static final LookChoiceStrategy FIXED_XP = new FixedXPStrategy();
    public static final LookChoiceStrategy FIXED    = FIXED_3D;

    
	// Override default constructor; prevents instantation.
	private LookChoiceStrategies() {}
	
	
    // LookChoiceStrategy Convenience Implementations ***********************       

    // Chooses the cross-platform Plastic3D look.
    private static class Fixed3DStrategy implements LookChoiceStrategy {
        
        public String getDefaultLookClassName() {
            return Options.PLASTIC3D_NAME;
        }
    }


    // Chooses the cross-platform PlasticXP look.
    private static class FixedXPStrategy implements LookChoiceStrategy {

        public String getDefaultLookClassName() {
            return Options.PLASTICXP_NAME;
        }
    }


    // Chooses the system look.
    private static class SystemStrategy implements LookChoiceStrategy {
        
        public String getDefaultLookClassName() {
            return Options.getSystemLookAndFeelClassName();
        }
    }


    // Chooses PlasticXP on WindowXP and the system look otherwise.
    private static class DefaultStrategy implements LookChoiceStrategy {
        
        public String getDefaultLookClassName() {
            return LookUtils.isWindowsXP()
                        ? Options.PLASTICXP_NAME
                        : Options.getSystemLookAndFeelClassName();
        }
    }


}