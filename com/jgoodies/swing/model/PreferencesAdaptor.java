package com.jgoodies.swing.model;

/*
 * Copyright (c) 2003 JGoodies Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of JGoodies Karsten Lentzsch.  
 * Use is subject to license terms.
 *
 */

import com.jgoodies.util.prefs.Preferences;

/**
 * An implementation of {@link ValueModel} that reads and writes values
 * from/to a key of a given <code>Preferences</code> node.
 * Changes are observed and fire state changes.
 *
 * @author Karsten Lentzsch
 */
public final class PreferencesAdaptor extends AbstractValueModel {

    private static final String ERROR_MSG =
        "Value must be a Boolean, Double, Float, Integer, Long, or String.";

    private final Preferences prefs;
    private final String      key;
    private final Class       type;
    private final Object      defaultValue;

    /**
     * Constructs a <code>PreferencesAdaptor</code> on the given
     * <code>Preferences</code> using the specified key and default value.
     * 
     * @param prefs   the <code>Preferences</code> to store and retrieve
     * @param key     the key used to get and set values 
     * @param defaultValue    the default value
     */
    public PreferencesAdaptor(
        Preferences prefs,
        String key,
        Object defaultValue) {
        this.prefs = prefs;
        this.key = key;
        this.type = defaultValue.getClass();
        this.defaultValue = defaultValue;
    }

    /**
     * Retrieves the value from the preferences, converts and answers it.
     * 
     * @return Object   the retrieved and converted value
     */
    public Object getValue() {
        if (type == String.class)
            return prefs.get(key, null);
        else if (type == Boolean.class)
            return new Boolean(getBoolean());
        // TODO 1.4 Boolean.valueOf(getBoolean());
        else if (type == Double.class)
            return new Double(getDouble());
        else if (type == Float.class)
            return new Float(getFloat());
        else if (type == Integer.class)
            return new Integer(getInt());
        else if (type == Long.class)
            return new Long(getLong());
        else if (type == String.class)
            return getString();
        else
            throw new IllegalArgumentException(ERROR_MSG);
    }

    public boolean getBoolean() {
        return prefs.getBoolean(key, ((Boolean) defaultValue).booleanValue());
    }

    public double getDouble() {
        return prefs.getDouble(key, ((Double) defaultValue).doubleValue());
    }

    public float getFloat() {
        return prefs.getFloat(key, ((Float) defaultValue).floatValue());
    }

    public int getInt() {
        return prefs.getInt(key, ((Integer) defaultValue).intValue());
    }

    public long getLong() {
        return prefs.getLong(key, ((Long) defaultValue).longValue());
    }

    public String getString() {
        return prefs.get(key, (String) defaultValue);
    }

    /**
     * Converts the given value to a string and puts it into the preferences.
     * 
     * @param newValue   the object to be stored
     */
    public void setValue(Object newValue) {
        Object value = getValue();
        if (((value == null) && (newValue == null))
            || ((value != null) && (value.equals(newValue))))
            return;
        if (newValue instanceof Boolean)
            setBoolean(((Boolean) newValue).booleanValue());
        else if (newValue instanceof Float)
            setFloat(((Float) newValue).floatValue());
        else if (newValue instanceof Integer)
            setInt(((Integer) newValue).intValue());
        else if (newValue instanceof Long)
            setLong(((Long) newValue).longValue());
        else if (newValue instanceof String)
            setString((String) newValue);
        else
            throw new IllegalArgumentException(ERROR_MSG);
    }

    public void setBoolean(boolean newValue) {
        prefs.putBoolean(key, newValue);
        fireStateChanged();
    }

    public void setDouble(double newValue) {
        prefs.putDouble(key, newValue);
        fireStateChanged();
    }

    public void setFloat(float newValue) {
        prefs.putFloat(key, newValue);
        fireStateChanged();
    }

    public void setInt(int newValue) {
        prefs.putInt(key, newValue);
        fireStateChanged();
    }

    public void setLong(long newValue) {
        prefs.putLong(key, newValue);
        fireStateChanged();
    }

    public void setString(String newValue) {
        prefs.put(key, newValue);
        fireStateChanged();
    }
}