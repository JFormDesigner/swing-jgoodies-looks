package com.jgoodies.swing.application;

/*
 * Copyright (c) 2002 Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of Karsten Lentzsch.  
 * Use is subject to license terms.
 *
 */

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;

import com.jgoodies.util.LazyEvaluator;
import com.jgoodies.util.logging.Logger;
import com.jgoodies.lazy.Preparable;

/**
 * A repository for <code>Action</code> instances and manager that
 * reads and initializes action values from a <code>ResourceBundle</code>.<p>
 * 
 * Once an action has been registered via #register, it can be retrieved
 * with #get, or #getAll. Per default, registered Actions will read their
 * values at the first request (lazy). You can force an eager initialization
 * by setting the <code>InitializationType</code> <code>EAGER</code>.
 * For example:
 * <pre>
 * ActionManager.register(new MyOpenAction(), ActionManager.EAGER);
 * </pre>
 * <p>
 * 
 * Also provides an <code>EagerReader</code> that implements the
 * <code>Preparable</code> interface to eagerly load all lazy action values.
 *
 * @see	Action
 * @see	ResourceBundle
 * @see	Preparable
 * 
 * @author Karsten Lentzsch
 */

public final class ActionManager {

    public static final String SMALL_GRAY_ICON          = "smallGrayIcon";
    public static final String DISPLAYED_MNEMONIC_INDEX = "mnemonicIndex";

    static final ActionManager INSTANCE = new ActionManager();
    static final Logger        LOGGER   = Logger.getLogger("ActionManager");


    /*
     * A type-safe enumeration for initialization types: eager and lazy
     */
    public static final InitializationType EAGER =
        new InitializationType("EAGER");
    public static final InitializationType LAZY =
        new InitializationType("LAZY");


    public static class InitializationType {
        private final String name;

        InitializationType(String name) { this.name = name; }
        public  String toString()       { return name; }

    }


    private final Map actions;
    ResourceBundle bundle;

    // Suppresses default constructor, ensuring non-instantiability.
    private ActionManager() {
        this.actions = new HashMap(50);
    }

    /**
     * Sets the path to the <code>ResourceBundle</code> that 
     * is used to read <code>Action</code> values.
     */
    public static void setBundlePath(String path) {
        INSTANCE.bundle = ResourceBundle.getBundle(path);
    }

    /**
     * Registers an <code>Action</code> for the given <code>id</code>
     * using the default lazy initialization.
     */
    public static Action register(String id, Action action) {
        return register(id, action, LAZY);
    }

    /**
     * Registers an <code>Action</code> for the given <code>id</code>
     * using a given <code>InitializationType</code> (EAGER or LAZY).
     */
    public static Action register(
        String id,
        Action action,
        InitializationType type) {
        if (action == null)
            throw new NullPointerException("Registered actions must not be null.");

        LazyActionReader reader = new LazyActionReader(id, action);
        Object oldValue = INSTANCE.actions.put(id, reader);
        if (oldValue != null)
            LOGGER.warning("Duplicate action id: " + id);

        if (type == EAGER)
            reader.action(); // force immediate read

        return action;
    }

    /**
     * Retrieves and answers an <code>Action</code> for the given id.
     */
    public static Action get(String id) {
        LazyActionReader reader =
            (LazyActionReader) (INSTANCE.actions.get(id));
        if (null == reader) {
            LOGGER.severe("No action found for id: " + id);
            return null;
        }
        return (Action) reader.action();
    }

    /**
     * Retrieves and answers a list of <code>Actions</code> for the given ids.
     */
    public static List getAll(String[] allIDs) {
        List result = new LinkedList();
        for (int i = 0; i < allIDs.length; i++) {
            Action action = get(allIDs[i]);
            if (action != null)
                result.add(action);
        }
        return result;
    }

    /**
     * Retrieves and answers the small icon for the given <code>id</code>.
     */
    public static Icon getIcon(String id) {
        Action action = get(id);
        if (action == null)
            return null;
        return (Icon) action.getValue(Action.SMALL_ICON);
    }

    /**
     * Ensures that all registered <code>Actions</code> are loaded.
     */
    static void ensureAllActionsLoaded() {
        for (Iterator i = INSTANCE.actions.values().iterator();
            i.hasNext();
            ) {
            LazyActionReader reader = (LazyActionReader) i.next();
            reader.prepare();
        }
    }

    /**
     * The ActionReader reads action values for label, descriptions, etc.
     * from a resource bundle for a given id, and puts these values into an action.
     */
    private static class ActionReader {

        private static final String LABEL             = "label";
        private static final char  MNEMONIC_MARKER   = '&';
        private static final String DOT_STRING        = "...";
        private static final String SHORT_DESCRIPTION = "tooltip";
        private static final String LONG_DESCRIPTION  = "helptext";
        private static final String ICON              = "icon";
        private static final String GRAY_ICON         = ICON + ".gray";
        private static final String ACCELERATOR       = "accelerator";

        private final String    id;
        private final String    name;
        private final Integer   mnemonic;
        private final Integer   aMnemonicIndex;
        private final String    shortDescription;
        private final String    longDescription;
        private final ImageIcon icon;
        private final ImageIcon grayIcon;
        private final KeyStroke accellerator;

        /**
         * Reads properties for <code>id</code> in <code>bundle</code>.
         */
        static void readValues(ResourceBundle bundle, String id) {
            new ActionReader(bundle, id);
        }

        /**
         * Reads properties for <code>id</code> in <code>bundle</code> and
         * sets the approriate values in the given <code>action</code>.
         */
        static void readAndPutValues(
            Action action,
            ResourceBundle bundle,
            String id) {
            ActionReader reader = new ActionReader(bundle, id);
            reader.putValues(action);
        }

        private ActionReader(ResourceBundle bundle, String id) {
            this.id = id;
            String nameWithMnemonic = getString(bundle, id + "." + LABEL, id);
            int index = mnemonicIndex(nameWithMnemonic);
            name = stripName(nameWithMnemonic, index);
            mnemonic = stripMnemonic(nameWithMnemonic, index);
            aMnemonicIndex = new Integer(index);

            shortDescription =
                getString(
                    bundle,
                    id + '.' + SHORT_DESCRIPTION,
                    defaultShortDescription(name));
            longDescription =
                getString(bundle, id + '.' + LONG_DESCRIPTION, name);

            String iconPath = getString(bundle, id + '.' + ICON, null);
            icon =
                null == iconPath
                    ? null
                    : ResourceManager.readImageIcon(iconPath);

            String grayIconPath =
                getString(bundle, id + '.' + GRAY_ICON, null);
            grayIcon =
                null == grayIconPath
                    ? null
                    : ResourceManager.readImageIcon(grayIconPath);

            String shortcut = getString(bundle, id + '.' + ACCELERATOR, null);
            accellerator = getKeyStroke(shortcut);
        }

        /**
         * Put the ActionReader's properties as values in the Action.
         */
        private void putValues(Action action) {
            action.putValue(Action.NAME, name);
            action.putValue(Action.SHORT_DESCRIPTION, shortDescription);
            action.putValue(Action.LONG_DESCRIPTION, longDescription);
            action.putValue(Action.SMALL_ICON, icon);
            action.putValue(ActionManager.SMALL_GRAY_ICON, grayIcon);
            action.putValue(Action.ACCELERATOR_KEY, accellerator);
            action.putValue(Action.MNEMONIC_KEY, mnemonic);
            action.putValue(
                ActionManager.DISPLAYED_MNEMONIC_INDEX,
                aMnemonicIndex);
        }

        private int mnemonicIndex(String nameWithMnemonic) {
            return nameWithMnemonic.indexOf(MNEMONIC_MARKER);
        }

        private String stripName(
            String nameWithMnemonic,
            int mnemonicIndex) {
            return mnemonicIndex == -1
                ? nameWithMnemonic
                : nameWithMnemonic.substring(0, mnemonicIndex)
                    + nameWithMnemonic.substring(mnemonicIndex + 1);
        }

        private Integer stripMnemonic(
            String nameWithMnemonic,
            int mnemonicIndex) {
            return mnemonicIndex == -1
                ? null
                : new Integer(nameWithMnemonic.charAt(mnemonicIndex + 1));
        }

        private String defaultShortDescription(String nameWithDots) {
            return nameWithDots.endsWith(DOT_STRING)
                ? (nameWithDots.substring(0, nameWithDots.length() - DOT_STRING.length()))
                : nameWithDots;
        }

        private KeyStroke getKeyStroke(String accelleratorString) {
            if (accelleratorString == null) {
                return null;
            } else {
                KeyStroke keyStroke =
                    KeyStroke.getKeyStroke(accelleratorString);
                if (keyStroke == null)
                    Logger.getLogger("ActionReader").warning(
                        "Action "
                            + id
                            + " has an invalid accellerator "
                            + accelleratorString);
                return keyStroke;
            }
        }

        private String getString(
            ResourceBundle bundle,
            String key,
            String defaultString) {
            try {
                return bundle.getString(key);
            } catch (MissingResourceException e) {
                return defaultString;
            }
        }

    }

    /**
     * An implementation of the <code>Preparable</code> interface that ensures
     * that all lazy <code>Actions</code> are read.
     */
    public static class EagerReader implements Preparable {

        /**
         * Ensures that registered <code>Actions</code> are read.
         */
        public void prepare() {
            LOGGER.info("Loading all lazy actions...");
            ActionManager.ensureAllActionsLoaded();
        }
    }

    // This class deferres reading from the resource bundle and reading icons.
    private static class LazyActionReader extends LazyEvaluator {
        private final String id;
        private final Action action;

        private LazyActionReader(String id, Action action) {
            this.id = id;
            this.action = action;
        }

        void prepare() {
            ActionReader.readValues(INSTANCE.bundle, id);
        }

        protected Object evaluate() {
            ActionReader.readAndPutValues(action, INSTANCE.bundle, id);
            return null;
        }

        Action action() {
            value();
            return action;
        }
    }

}