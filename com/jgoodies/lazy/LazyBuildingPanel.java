package com.jgoodies.lazy;

/*
 * Copyright (c) 2003 JGoodies Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of JGoodies Karsten Lentzsch.
 * Use is subject to license terms.
 *
 */

import javax.swing.JPanel;
import java.awt.LayoutManager;
import com.jgoodies.util.logging.Logger;

/**
 * An abstract superclass of panels that are build lazily.
 * Subclasses must implement the #build method and collaborators must
 * call #ensureBuilt(), to ensure that this panel has been built.
 *
 * @author Karsten Lentzsch
 */
public abstract class LazyBuildingPanel extends JPanel {

    private boolean hasBeenBuilt = false;
    private boolean isBuilding   = false;

    /**
     * Constructs a panel that provides lazy building.
     */
    public LazyBuildingPanel() {}

    /**
     * Constructs a panel that provides lazy building.
     */
    public LazyBuildingPanel(LayoutManager layoutManager) {
        super(layoutManager);
    }

    /**
     * Ensures that the panel has been built and calls #built if necessary:<ol>
     * <li>Checks whether the panel has been built; if so, does nothing.</li>
     * <li>Checks whether we are just building; if so, log a warning.</li>
     * <li>Note that we are building.</li>
     * <li>Otherwise, calls the #build method.</li>
     * <li>Note that we have built the panel.</li>
     * <li>Note that we are not building any longer.</li>
     */
    public synchronized final void ensureBuilt() {
        if (hasBeenBuilt)
            return;
        if (isBuilding) {
            getLogger().warning(
                "Don't call #ensureBuilt while the panel is building.");
            return;
        }
        isBuilding = true;
        getLogger().info("Building panel");
        build();
        hasBeenBuilt = true;
        isBuilding = false;
    }

    /**
     * Subclass must implement this method to build the panel.
     */
    abstract protected void build();

    /*
     * Answers whether the panel has been built or not.
     */
    public boolean hasBeenBuilt() {
        return hasBeenBuilt;
    }

    // Answers a logger for this class.
    private Logger getLogger() {
        return Logger.getLogger(this.getClass().getName());
    }

}