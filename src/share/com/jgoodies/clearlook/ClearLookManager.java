/*
 * Copyright (c) 2003 JGoodies Karsten Lentzsch. All Rights Reserved.
 *
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 * 
 *  o Redistributions of source code must retain the above copyright notice, 
 *    this list of conditions and the following disclaimer. 
 *     
 *  o Redistributions in binary form must reproduce the above copyright notice, 
 *    this list of conditions and the following disclaimer in the documentation 
 *    and/or other materials provided with the distribution. 
 *     
 *  o Neither the name of JGoodies Karsten Lentzsch nor the names of 
 *    its contributors may be used to endorse or promote products derived 
 *    from this software without specific prior written permission. 
 *     
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, 
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR 
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR 
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, 
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, 
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; 
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, 
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE 
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, 
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. 
 */

package com.jgoodies.clearlook;

import javax.swing.JComponent;
import javax.swing.UIManager;
import javax.swing.border.Border;

import com.jgoodies.plaf.LookUtils;
import com.jgoodies.plaf.Options;

/**
 * Manages the JGoodies ClearLook(tm) technology; provides access
 * to the {@link ClearLookMode}, {@link ClearLookPolicy},
 * and delegates the component analysis to the current policy.
 * <p>
 * ClearLook can automatically improve an application's visual appearance. 
 * Therefore it detects typical GUI problems, for example, nested borders.
 * It then removes or replaces obsolete decorators and visual clutter.
 * <p>
 * The concrete detection and replacement is performed by an implementation
 * of <code>ClearLookPolicy</code>.
 * <p>
 * Users can choose the ClearLook mode and policy by setting System properties.
 * To set the mode, specify its name under key <tt>ClearLook.mode</tt>;
 * to set the policy, specify its class name under <tt>ClearLook.policy</tt>.
 * 
 * @author Karsten Lentzsch
 * @see	ClearLookMode
 * @see	ClearLookPolicy
 * @see	DefaultClearLookPolicy
 * @see     Options
 */
public final class ClearLookManager {

    private static final ClearLookMode DEFAULT_MODE = ClearLookMode.OFF;

    private static final ClearLookMode NETBEANS_DEFAULT_MODE = ClearLookMode.ON;

    private static final String DEFAULT_POLICY_NAME =
        "com.jgoodies.clearlook.DefaultClearLookPolicy";

    private static final String NETBEANS_POLICY_NAME =
        "com.jgoodies.clearlook.NetBeansClearLookPolicy";

    /** 
     * Holds the current ClearLook mode, 
     * an instance of <code>ClearLookMode</code>. 
     */
    private static ClearLookMode mode;

    /** 
     * Holds the current ClearLook policy, 
     * an instance of <code>ClearLookPolicy</code>.  
     */
    private static ClearLookPolicy policy;

    static {
        installDefaultMode();
        installDefaultPolicy();
    }

    private ClearLookManager() {
        // Override default constructor; prevents instantiation.
    }

    // Delegating the Border Replacement ************************************

    /**
     * Detects if ClearLook should replace the component's <code>Border</code>.
     * In this case, it replaces the border and returns the original border.
     * Otherwise, it returns <code>null</code>.
     */
    public static Border replaceBorder(JComponent component) {
        return mode.isEnabled()
            && (policy != null) ? policy.replaceBorder(component) : null;
    }


    // Accessing the ClearLook Mode *****************************************

    /**
     * Returns the current ClearLook mode.
     * 
     * @return the current <code>ClearLookMode</code> instance.
     */
    public static ClearLookMode getMode() {
        return mode;
    }

    /**
     * Sets a new <code>ClearLookMode</code>.
     * 
     * @param newMode   the <code>ClearLookMode</code> to be set
     */
    public static void setMode(ClearLookMode newMode) {
        setMode(newMode, false);
    }

    /**
     * Sets a new <code>ClearLook Mode</code>. 
     * Forces a log if the user has set a System property to choose the mode.
     */
    private static void setMode(ClearLookMode newMode, boolean userChoosen) {
        mode = newMode;
        if (userChoosen) {
            LookUtils.log(
                "You have choosen to use the ClearLook(tm) mode '"
                    + mode.getName()
                    + "'.");
        } else if (mode.isEnabled()) {
            LookUtils.log(
                "The ClearLook(tm) mode has been set to '"
                    + mode.getName()
                    + "'.");
        }
    }

    /**
     * Detects and answers the default ClearLook mode.
     * Default is <code>OFF</code>, unless we are in NetBeans, 
     * where it defaults to <code>ON</code>.
     * <p>
     * The default can be overridden by setting the mode in the 
     * <code>UIDefaults</code> table at key <code>ClearLook.mode</code>,
     * which in turn can be overridden by the system properties
     * using the same key.
     * <p>
     * In case the user has set a system property or we detect a problem,
     * we log a message about the choosen style.
     */
    public static void installDefaultMode() {
        String userMode = LookUtils.getSystemProperty(Options.CLEAR_LOOK_MODE_KEY, "");
        boolean overridden = userMode.length() > 0;
        Object value =
            overridden ? userMode : UIManager.get(Options.CLEAR_LOOK_MODE_KEY);

        ClearLookMode result;
        if (value == null) {
            result = getDefaultMode();
        } else if (value instanceof ClearLookMode) {
            result = (ClearLookMode) value;
        } else if (value instanceof String) {
            ClearLookMode aMode = ClearLookMode.valueOf((String) value);
            result = aMode != null ? aMode : ClearLookMode.OFF;
        } else
            result = ClearLookMode.OFF;

        // In case the user tried to set a theme and we detected a problem, log a message.
        if (overridden && !result.getName().equalsIgnoreCase(userMode)) {
            LookUtils.log(
                "I could not find the ClearLook(tm) mode '" + userMode + "'.");
        }
        setMode(result, overridden);
    }

    /**
     * Returns the default mode used for the default mode installation.
     */
    private static ClearLookMode getDefaultMode() {
        return LookUtils.IS_NETBEANS ? NETBEANS_DEFAULT_MODE : DEFAULT_MODE;
    }
    

    // Accessing the ClearLook policy ***************************************

    /**
     * Returns the current ClearLook policy.
     * 
     * @return the current <code>ClearLookPolicy</code>
     */
    public static ClearLookPolicy getPolicy() {
        return policy;
    }

    /**
     * Sets a new ClearLook policy.
     * 
     * @param newPolicy    the <code>ClearLookPolicy</code> to be set
     */
    public static void setPolicy(ClearLookPolicy newPolicy) {
        policy = newPolicy;
        if (mode.isVerbose() && policy != null) {
            LookUtils.log(
                "You have choosen to use the ClearLook(tm) policy '"
                    + policy.getName()
                    + "'.");
        }
    }

    /**
     * Sets a new ClearLook policy using an instance of the specified class.
     * <p>
     * In case we detect a problem, we log a message.
     * 
     * @param policyClassName  the class name of the ClearLook policy to be set
     */
    public static void setPolicy(String policyClassName) {
        try {
            Class clazz = Class.forName(policyClassName);
            setPolicy((ClearLookPolicy) clazz.newInstance());
        } catch (ClassNotFoundException e) {
            LookUtils.log(
                "I could not find the ClearLook(tm) policy '"
                    + policyClassName
                    + "'.");
        } catch (Exception e) {
            LookUtils.log(
                "I could not instantiate the ClearLook(tm) policy '"
                    + policyClassName
                    + "'.");
        }
    }

    /**
     * Detects and installs the default <code>ClearLookPolicy</code>.
     * Default is <code>DefaultClearLookPolicy</code>, unless we are 
     * in NetBeans, where it defaults to <code>NetBeansClearLookPolicy</code>.
     * <p>
     * The default can be overridden by specifying the policy in the 
     * System properties at key <code>ClearLook.policy</code>.
     */
    private static void installDefaultPolicy() {
        String userPolicy =
            LookUtils.getSystemProperty(Options.CLEAR_LOOK_POLICY_KEY, "");
        String className =
            userPolicy.length() > 0 ? userPolicy : getDefaultPolicyName();
        setPolicy(className);
    }

    /**
     * Returns the default policy name used for the default policy installation.
     */
    private static String getDefaultPolicyName() {
        return (
            LookUtils.IS_NETBEANS ? NETBEANS_POLICY_NAME : DEFAULT_POLICY_NAME);
    }
    

    // Helper Code **********************************************************

    /**
     * Logs a message to the System output if we are in verbose mode.
     * 
     * @param message   the string to log
     */
    public static void log(String message) {
        if (getMode().isVerbose())
            System.out.println("CL:" + message);
    }

}