/*
 * Copyright (c) 2003 JGoodies Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of JGoodies Karsten Lentzsch.
 * Use is subject to license terms.
 *
 */

package com.jgoodies.lazy;

import java.util.Enumeration;

import javax.swing.UIDefaults;
import javax.swing.UIManager;

import com.jgoodies.util.logging.Logger;

/**
 * This class consists only of static methods that construct and answer
 * <code>Preparable</code>s.
 *
 * @author Karsten Lentzsch
 */
public final class Preparables {

    // Overrides the default constructor; prevents instantiation.
    private Preparables() {}

    /**
     * Creates and answers a <code>Preparable</code> object 
     * that initializes all values in the <code>UIDefaults</code> table.
     */
    public static Preparable createUIDefaultsInitializer() {
        return new EagerUIDefaultsInitializer();
    };
    
    /**
     * Creates and answers a <code>Preparable</code> object 
     * that prepares menu bars, menus and menu items.
     */
    public static Preparable createMenuInitializer() {
        return new MenuPreparer();
    };
    

    // Helper Classes *******************************************************

    // Helper class that activates all UIDefaults values.
    private static class EagerUIDefaultsInitializer implements Preparable {

        /**
         * Activates all <code>UIDefaults</code> values.
         * 
         * @see Preparable#prepare()
         */
        public void prepare() {
            Logger logger = Logger.getLogger("EagerUIDefaultInitializer");
            logger.info("Activating all lazy UI values...");
            long start = System.currentTimeMillis();
            int count = activateLazyValues();
            long stop = System.currentTimeMillis();
            logger.info(
                "Activated "
                    + count
                    + " UI values in "
                    + (stop - start)
                    + "ms.");
        }

        private int activateLazyValues() {
            UIDefaults defaults = UIManager.getDefaults();
            UIDefaults lafDefaults = UIManager.getLookAndFeelDefaults();
            int count = 0;
            for (Enumeration enum = defaults.keys();
                enum.hasMoreElements();
                count++) {
                defaults.get(enum.nextElement());
            }
            for (Enumeration enum = lafDefaults.keys();
                enum.hasMoreElements();
                count++) {
                defaults.get(enum.nextElement());
            }
            return count;
        }

    }
    
    
    // Helper class that prepares menu bars, menus and menu items.
    private static class MenuPreparer implements Preparable {

        public void prepare() {
            new BackgroundClassLoader(
                new String[] {
                    "javax.swing.JMenuBar",
                    "javax.swing.JMenu",
                    "javax.swing.JMenuItem",
                    "javax.swing.plaf.basic.BasicMenuBarUI",
                    "javax.swing.plaf.basic.BasicMenuUI",
                    "javax.swing.plaf.basic.BasicMenuItemUI" })
                .prepare();
        }

    }


}