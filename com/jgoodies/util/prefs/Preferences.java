package com.jgoodies.util.prefs;

/*
 * Copyright (c) 2002 JGoodies Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of JGoodies Karsten Lentzsch.
 * Use is subject to license terms.
 *
 */

import java.io.*;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.jgoodies.util.Utilities;
import com.jgoodies.util.logging.Level;
import com.jgoodies.util.logging.Logger;

/**
 * This class provides access to user preferences, which are
 * backed by a properties file on all platforms. This code
 * is a partial implementation of the 1.4 preferences API.
 * 
 * [Pending 1.4] This class is obsolete in 1.4 environments.
 *
 * @author Karsten Lentzsch
 */

public final class Preferences {

    private static final String FILENAME = "preferences.txt";
    private static final String FILE_HEADER = "Preferences File";
    private static final Logger logger = Logger.getLogger("Preferences");

    private static final Preferences userRoot = new Preferences(null, "");

    private final String        name;
    private final String        absolutePath;
    private final Preferences   root; // Relative to this node
    private final Map           childCache;
    private final Properties    props;

    /**
     * This is a fake method that provides better forward compatibility
     * with the 1.4 preferences API. It returns the one and only root node.
     * The 1.4 return the root preference node for the calling user.
     */
    public static Preferences userRoot() {
        return userRoot;
    }

    /**
     * Creates a preference node with the specified parent and the specified
     * name relative to its parent.
     *
     * @param parent the parent of this preference node, or null if this
     *               is the root.
     * @param name the name of this preference node, relative to its parent,
     *             or <tt>""</tt> if this is the root.
     * @throws IllegalArgumentException if <tt>name</tt> contains a slash
     *          (<tt>'/'</tt>),  or <tt>parent</tt> is <tt>null</tt> and
     *          name isn't <tt>""</tt>.  
     */
    private Preferences(Preferences parent, String name) {
        if (parent == null) {
            if (!name.equals(""))
                throw new IllegalArgumentException(
                    "Root name '" + name + "' must be \"\"");
            this.absolutePath = "/";
            root = this;
        } else {
            if (name.indexOf('/') != -1)
                throw new IllegalArgumentException(
                    "Name '" + name + "' contains '/'");
            if (name.equals(""))
                throw new IllegalArgumentException("Illegal name: empty string");

            root = parent.root;
            absolutePath =
                (parent == root
                    ? "/" + name
                    : parent.absolutePath + "/" + name);
        }
        this.name = name;
        this.childCache = new HashMap();
        this.props = new SortedProperties();
    }

    /** 
     * Returns a child preference node in the same tree as this node.
     * The preferences are read from the backing store before returned.
     * 
     * Unlike the 1.4 implementation it won't create any of its ancestors 
     * if they do not already exist.
     * The 1.4 additionally accepts a relative or absolute path name.  
     */
    public Preferences node(String pathName) {
        if (this != root)
            throw new IllegalArgumentException("Can't build a preference tree with depth larger than 1.");
        Preferences prefs = (Preferences) childCache.get(pathName);
        if (prefs == null) {
            prefs = new Preferences(this, pathName);
            prefs.load();
            childCache.put(pathName, prefs);
        }
        return prefs;
    }

    // Accessing preferences **************************************************

    public String get(String key, String defaultValue) {
        String val = props.getProperty(key);
        return val != null ? val : defaultValue;
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        String value = get(key, null);
        return null == value ? defaultValue : value.equals("true");
    }

    public double getDouble(String key, double defaultValue) {
        String value = get(key, null);
        if (null == value)
            return defaultValue;

        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            logger.warning("Key '" + key + "' has no double value.");
            return defaultValue;
        }
    }

    public float getFloat(String key, float defaultValue) {
        String value = get(key, null);
        if (null == value)
            return defaultValue;

        try {
            return Float.parseFloat(value);
        } catch (NumberFormatException e) {
            logger.warning("Key '" + key + "' has no float value.");
            return defaultValue;
        }
    }

    public int getInt(String key, int defaultValue) {
        String value = get(key, null);
        if (null == value)
            return defaultValue;

        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            logger.warning("Key '" + key + "' has no integer value.");
            return defaultValue;
        }
    }

    public long getLong(String key, long defaultValue) {
        String value = get(key, null);
        if (null == value)
            return defaultValue;

        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            logger.warning("Key '" + key + "' has no long value.");
            return defaultValue;
        }
    }

    public void put(String key, String value) {
        props.put(key, value);
    }

    public void putBoolean(String key, boolean value) {
        put(key, String.valueOf(value));
    }

    public void putDouble(String key, double value) {
        put(key, String.valueOf(value));
    }

    public void putFloat(String key, float value) {
        put(key, String.valueOf(value));
    }

    public void putInt(String key, int value) {
        put(key, String.valueOf(value));
    }

    public void putLong(String key, long value) {
        put(key, String.valueOf(value));
    }

    public Enumeration keys() {
        return props.keys();
    }

    public void remove(String key) {
        props.remove(key);
    }

    public String toString() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            props.store(out, FILE_HEADER);
        } catch (IOException e) {}
        return "Preferences on file "
            + getFile().getAbsolutePath()
            + ": "
            + out.toString();
    }

    // Backing store **************************************************

    private File getFile() {
        String userHomePath = System.getProperty("user.home");
        String nodePath = '.' + name;
        String path = userHomePath + File.separatorChar + nodePath;
        return new File(path, FILENAME);
    }

    private void load() {
        File file = getFile();
        if (!file.exists())
            return;

        FileInputStream in = null;
        try {
            in = new FileInputStream(file);
            props.load(in);
        } catch (FileNotFoundException e1) {
            logger.warning("Preferences file '" + file + "' not found.");
        } catch (IOException e2) {
            logger.log(
                Level.WARNING,
                "Can't load preferences from file: " + file,
                e2);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    logger.log(
                        Level.WARNING,
                        "Can't close preferences file: " + file,
                        e);
                }
            }
        }
    }

    public void flush() {
        File file = getFile();
        if (!file.exists())
            file.getParentFile().mkdirs();

        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
            props.store(out, FILE_HEADER);
        } catch (IOException e) {
            logger.log(
                Level.WARNING,
                "Can't save preferences to file : " + file,
                e);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    logger.log(
                        Level.WARNING,
                        "Can't close preferences file: " + file,
                        e);
                }
            }
        }
    }

    /*
     * Unlike its superclass keys are answered (and written)
     * in alphabetical order.
     */
    private static class SortedProperties extends Properties {

        public Enumeration keys() {
            return Utilities.sort(super.keys());
        }

        public Enumeration propertyNames() {
            return Utilities.sort(super.propertyNames());
        }

    }

}