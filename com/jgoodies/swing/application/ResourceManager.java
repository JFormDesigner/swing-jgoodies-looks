package com.jgoodies.swing.application;

/*
 * Copyright (c) 2003 JGoodies Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of JGoodies Karsten Lentzsch.
 * Use is subject to license terms.
 *
 */
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;

import com.jgoodies.util.logging.Level;
import com.jgoodies.util.logging.Logger;

/**
 * A singleton that provides access to internationalized application
 * resources: strings, icons, file paths, URLs, and text file contents. It
 * therefore uses a <code>ResourceBundle</code> to get localized paths to the
 * resources which it should fetch and answer.<p>
 * 
 * Note: When loading image resources, you must often specify 
 * a <code>ClassLoader</code>, e.g. when using JavaWebStart. 
 * The default mechanism assumes that all resources are loaded using the same
 * class loader this <code>ResourceManager</code> class has been loaded with.
 * You can either set a different default <code>ClassLoader</code> or call
 * #getIcons and provide an individual <code>ClassLoader</code>.
 * 
 * @author	Karsten Lentzsch
 * @see	ResourceBundle
 */

public final class ResourceManager {

    private static final ResourceManager INSTANCE = new ResourceManager();
    private static final Logger LOGGER = Logger.getLogger("ResourceManager");

    private ResourceBundle bundle;

    private ClassLoader defaultClassLoader =
        ResourceManager.class.getClassLoader();

    // Suppresses default constructor, ensuring non-instantiability.
    private ResourceManager() {}

    /**
     * Sets the path to the <code>ResourceBundle</code> that will be used to
     * fetch string resources and path resources.
     */
    public static void setBundlePath(String path) {
        INSTANCE.bundle = ResourceBundle.getBundle(path);
    }
    
    /**
     * Returns the <code>ResourceBundle</code> that is used to look up
     * localized resources.
     * 
     * @return ResourceBundle
     */
    public static ResourceBundle getBundle() {
        return INSTANCE.bundle;
    }

    /**
     * Sets a <code>ClassLoader</code> that will be used as default
     * when reading image resources.
     */
    public static void setDefaultClassLoader(ClassLoader classLoader) {
        INSTANCE.defaultClassLoader = classLoader;
    }

    /**
     * Retrieves and answers a <code>String</code> for the given key from the
     * bundle.
     */
    public static String getString(String key) {
        try {
            return INSTANCE.bundle.getString(key);
        } catch (MissingResourceException e) {
            LOGGER.warning("Missing resource for key:" + key);
            return null;
        }
    }

    /**
     * Retrieves and answers an <code>ImageIcon</code> for the given key from
     * the bundle.
     */
    public static ImageIcon getIcon(String key) {
        String path = getString(key);
        if (path == null) {
            return null;
        } else if (path.length() == 0) {
            LOGGER.warning(key + " has an empty path.");
            return null;
        } else
            return readImageIcon(path);
    }

    /**
     * Answers a <code>URL</code> for the given path using 
     * the default <code>ClassLoader</code>.
     */
    public static URL getURL(String path) {
        return getURL(path, INSTANCE.defaultClassLoader);
    }

    /**
     * Answers a <code>URL</code> for the given path and 
     * the given <code>ClassLoader</code>.
     */
    public static URL getURL(String path, ClassLoader classLoader) {
        URL url = classLoader.getResource(path);
        if (null == url)
            LOGGER.warning(path + " not found.");
        return url;
    }

    /**
     * Gets and answers an <code>InputStream</code> for the given path 
     * using the default <code>ClassLoader</code>.
     */
    public static InputStream getInputStream(String path) {
        URL url = getURL(path);
        if (url != null) {
            try {
                return url.openStream();
            } catch (IOException e) {
                LOGGER.log(
                    Level.WARNING,
                    "Can't open URL " + url.toString(),
                    e);
            }
        }
        return null;
    }

    /**
     * Reads and answers an <code>ImageIcon</code> for the given path 
     * using the default <code>ClassLoader</code>.
     */
    public static ImageIcon readImageIcon(String path) {
        URL url = getURL(path);
        return null == url ? null : new ImageIcon(url);
    }

    /**
     * Reads and answers the <code>String</code> contents of a text file 
     * located at the the given path using the default <code>ClassLoader</code>.
     */
    public static String readTextFromFile(String path) {
        InputStream in = getInputStream(path);
        if (null == in)
            return null;
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        StringBuffer result = new StringBuffer();
        try {
            String line = reader.readLine();
            while (line != null) {
                result.append(line);
                line = reader.readLine();
                if (line != null)
                    result.append('\n');
            }
            return result.toString();
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Can't read string from " + path, e);
            return null;
        } finally {
            try {
                reader.close();
            } catch (IOException e) {}
        }
    }

    /*
     * [Pending 1.4]: Could replace #readImageIcon, but care must
     * then be taken of image caching.
     * 
    public static Image readImage(String path) {
    	URL url = getURL(path);
    	if (url == null) return null;
    	try { 
    	    return ImageIO.read(url); 
    	} catch (IOException e) {
    	    return null;
    	}
    }
    */

    /**
     * Loads <code>Properties</code> from the specified path.
     * 
     * @deprecated Property files should be replaced by resource bundles. 
     */
    public static void loadProperties(
        Properties properties,
        String pathname) {
        try {
            properties.load(
                new BufferedInputStream(
                    ResourceManager.getInputStream(pathname)));
        } catch (Exception e) {
            LOGGER.log(
                Level.WARNING,
                "Can't load properties from file " + pathname,
                e);
        }
    }

}