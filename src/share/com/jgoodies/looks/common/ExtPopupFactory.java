/*
 * Copyright (c) 2005 JGoodies Karsten Lentzsch. All Rights Reserved.
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

package com.jgoodies.looks.common;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Panel;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Window;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JApplet;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.JRootPane;
import javax.swing.JWindow;
import javax.swing.LookAndFeel;
import javax.swing.Popup;
import javax.swing.PopupFactory;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.Border;

import com.jgoodies.looks.LookUtils;


/**
 * The JGoodies implementation of <code>PopupFactory</code>. Adds to all
 * Popups (except ComboBox popups) a border with a nice looking drop shadow.
 * To activate ExtPopupFactory use the {@link #install()} method.<p>
 * 
 * This factory shall not be used on platforms that provide native drop shadows,
 * such as the Mac OS X. Therefore the invocation of the {@link #install()}
 * method will have no effect on such platforms.<p>
 * 
 * <strong>Note:</strong> To be used in a sandbox environment, ExtPopupFactory
 * requires two AWT permissions: <code>createRobot</code> and
 * <code>readDisplayPixels</code>. The reason for it is, that in the case of
 * the heavy weight popups the ExtPopupFactory will use a Robot object to make
 * snapshots of the screen background to simulate the drop shadow effect.
 * 
 * @author Andrej Golovnin
 * @version $Revision: 1.1 $
 * 
 * @see java.awt.AWTPermission
 * @see java.awt.Robot
 * @see javax.swing.Popup
 */
public final class ExtPopupFactory extends PopupFactory {

    /**
     * In the case of heavy weight popups, snapshots of the screen background
     * will be stored as client properties of the popup contents' parent.
     * These snapshots will be used by the popup border to simulate the drop
     * shadow effect. The two following constants define the names of
     * these client properties. 
     * 
     * @see com.jgoodies.looks.common.ShadowPopupBorder
     */
    static final String PROP_HORIZONTAL_BACKGROUND = "jgoodies.hShadowBg";
    static final String PROP_VERTICAL_BACKGROUND = "jgoodies.vShadowBg";

    /**
     * The Popup factory which was replaced by ExtPopupFactory.
     */
    private static PopupFactory originalFactory;

    /**
     * Indicates whether the original factory should be used to create popups.
     */
    private static boolean useOriginalFactory;

    /**
     * Listens to changes of the L&F and recalculates useOriginalFactory.
     */
    private static PropertyChangeListener lafChangeHandler;

    /**
     * Users should use #install() and #uninstall() methods.
     */
    private ExtPopupFactory() {
        // Suppresses default constructor.
    }

    // API *******************************************************************

    /**
     * Installs the ExtPopupFactory as the default popup factory. This method
     * has no effect on platforms, that provide native drop shadows.<p>
     * Currently only the Mac OS X is detected as platform with native drop
     * shadows.
     */
    public static void install() {
        if (LookUtils.IS_OS_MAC) {
            return;
        }
        PopupFactory factory = PopupFactory.getSharedInstance();
        if (!(factory instanceof ExtPopupFactory)) {
            originalFactory = factory;
            PopupFactory.setSharedInstance(new ExtPopupFactory());
            lafChangeHandler = new LookAndFeelChangeHandler();
            UIManager.addPropertyChangeListener(lafChangeHandler);
            useOriginalFactory = computeUseOriginalFactory();
        }
    }

    /**
     * Uninstalls the ExtPopupFactory and restores the original popup factory
     * as the default factory.
     */
    public static void uninstall() {
        PopupFactory factory = PopupFactory.getSharedInstance();
        if (factory instanceof ExtPopupFactory) {
            PopupFactory.setSharedInstance(originalFactory);
            UIManager.removePropertyChangeListener(lafChangeHandler);
            lafChangeHandler = null;
            originalFactory = null;
        }
    }

    /** {@inheritDoc} */
    public Popup getPopup(Component owner, Component contents, int x, int y)
            throws IllegalArgumentException {
        if (useOriginalFactory) {
            return originalFactory.getPopup(owner, contents, x, y);
        }
        Popup popup = super.getPopup(owner, contents, x, y);
        return PopupWrapper.getInstance(owner, contents, x, y, popup);
    }

    // Helper code ************************************************************

    /**
     * Computes and answers whether the original factory should be used or not.
     * 
     * @return true if the original factory should be used.
     */
    private static boolean computeUseOriginalFactory() {
        if (!PopupWrapper.canMakeSnapshots) {
            return true;
        }
        LookAndFeel laf = UIManager.getLookAndFeel();
        String name = laf.getName();
        if (!name.startsWith("JGoodies")) {
            return true;
        }
        return (name.indexOf("Windows") != -1)
            && !LookUtils.IS_LAF_WINDOWS_XP_ENABLED;
    }

    /**
     * The class that does all the magic. It adds the drop shadow border to
     * Popup. Makes snapshots of the screen background on show if needed.
     * And cleans up on hide all changes, which we made.
     */
    private static class PopupWrapper extends Popup {

        /**
         * Max number of items to store in the cache.
         */
        private static final int MAX_CACHE_SIZE = 5;

        /**
         * The cache to use for PopupWrapper popups.
         */
        private static List cache;

        /**
         * The singleton instance used to draw all borders.
         */
        private static final Border SHADOW_BORDER = ShadowPopupBorder.getInstance();

        /**
         * The size of the drop shadow.
         */
        private static final int SHADOW_SIZE = 5;

        /**
         * Indicates whether we can make snapshots from screen or not.
         */
        private static boolean canMakeSnapshots = true;

        /**
         * The component mouse coordinates are relative to, may be null.
         */
        private Component owner;

        /**
         * The contents of the popup.
         */
        private Component contents;

        /**
         * The desired x and y location of the popup.
         */
        private int x, y;

        /**
         * The real popup. The #show() and #hide() methods will delegate
         * all calls to these popup.
         */
        private Popup popup;

        /**
         * The border of the contents' parent replaced by SHADOW_BORDER.
         */
        private Border oldBorder;

        /**
         * The old value of the opaque property of the contents' parent.
         */
        private boolean oldOpaque;

        /**
         * The heavy weight container of the popup contents, may be null.
         */
        private Container heavyWeightContainer;

        /**
         * Returns a previously used <code>PopupWrapper</code>, or a new one
         * if none of the popups have been recycled.
         */
        static Popup getInstance(Component owner, Component contents, int x,
                int y, Popup delegate) {
            PopupWrapper result;
            synchronized (PopupWrapper.class) {
                if (cache == null) {
                    cache = new ArrayList(MAX_CACHE_SIZE);
                }
                if (cache.size() > 0) {
                    result = (PopupWrapper) cache.remove(0);
                } else {
                    result = new PopupWrapper();
                }
            }
            result.reset(owner, contents, x, y, delegate);
            return result;
        }

        /**
         * Recycles the PopupWrapper <code>popup</code>.
         */
        private static void recycle(PopupWrapper popup) {
            synchronized (PopupWrapper.class) {
                if (cache.size() < MAX_CACHE_SIZE) {
                    cache.add(popup);
                }
            }
        }

        /** {@inheritDoc} */
        public void hide() {
            JComponent parent = (JComponent) contents.getParent();
            popup.hide();
            if (parent.getBorder() == SHADOW_BORDER) {
                parent.setBorder(oldBorder);
                parent.setOpaque(oldOpaque);
                oldBorder = null;
                if (heavyWeightContainer != null) {
                    parent.putClientProperty(PROP_HORIZONTAL_BACKGROUND, null);
                    parent.putClientProperty(PROP_VERTICAL_BACKGROUND, null);
                    heavyWeightContainer = null;
                }
            }
            owner = null;
            contents = null;
            popup = null;
            recycle(this);
        }

        /** {@inheritDoc} */
        public void show() {
            if (heavyWeightContainer != null) {
                makeSnapshot();
            }
            popup.show();
        }

        /**
         * Reinitializes this popup wrapper using given parameters.
         * 
         * @param owner component mouse coordinates are relative to, may be null
         * @param contents the contents of the popup
         * @param x the desired x location of the popup
         * @param y the desired y location of the popup
         * @param popup the popup to wrap
         */
        private void reset(Component owner, Component contents, int x, int y,
                Popup popup) {
            this.owner = owner;
            this.contents = contents;
            this.popup = popup;
            this.x = x;
            this.y = y;
            if (owner instanceof JComboBox) {
                return;
            }
            Container mediumWeightContainer = null;
            for(Container p = contents.getParent(); p != null; p = p.getParent()) {
                if (p instanceof JWindow) {
                    // Workaround for the gray rect problem.
                    p.setBackground(contents.getBackground());
                    heavyWeightContainer = p;
                    break;
                } else if (p instanceof Panel) {
                    // It is medium weight. Setting background
                    // to a transparent color makes it transparent and
                    // we don't need to capture the screen background.
                    Color bg = p.getBackground();
                    int rgba = contents.getBackground().getRGB() & 0x00ffffff;
                    if ((bg == null) || (bg.getRGB() != rgba)) {
                        p.setBackground(new Color(rgba, true));
                    }
                    mediumWeightContainer = p;
                    break;
                }
            }
            JComponent parent = (JComponent) contents.getParent();
            oldOpaque = parent.isOpaque();
            oldBorder = parent.getBorder();
            parent.setOpaque(false);
            parent.setBorder(SHADOW_BORDER);
            // Pack it because we have changed the border.
            if (mediumWeightContainer != null) {
                mediumWeightContainer.setSize(
                        mediumWeightContainer.getPreferredSize());
            } else {
                parent.setSize(parent.getPreferredSize());
            }
        }

        /**
         * The 'scratch pad' objects used to calculate dirty regions of
         * the screen snapshots.
         * 
         * @see #makeSnapshot()
         */
        private static final Point point = new Point();
        private static final Rectangle rect = new Rectangle();

        /**
         * Snapshots the background. The snapshots are stored as client
         * properties of the contents' parent. The next time the border is drawn,
         * this background will be used.<p>
         * 
         * Uses a robot on the default screen device to capture the screen
         * region under the drop shadow. Does <em>not</em> use the window's
         * device, because that may be an outdated device (due to popup reuse)
         * and the robot's origin seems to be adjusted with the default screen
         * device.
         * 
         * @see #show()
         * @see com.jgoodies.looks.common.ShadowPopupBorder
         */
        private void makeSnapshot() {
            try {
                Robot robot = new Robot(); // uses the default screen device

                Dimension size = heavyWeightContainer.getPreferredSize();
                int width = size.width;
                int height = size.height;

                rect.setBounds(x, y + height - SHADOW_SIZE, width, SHADOW_SIZE);
                BufferedImage hShadowBg = robot.createScreenCapture(rect);

                rect.setBounds(x + width - SHADOW_SIZE, y, SHADOW_SIZE,
                        height - SHADOW_SIZE);
                BufferedImage vShadowBg = robot.createScreenCapture(rect);

                JComponent parent = (JComponent) contents.getParent();
                parent.putClientProperty(PROP_HORIZONTAL_BACKGROUND, hShadowBg);
                parent.putClientProperty(PROP_VERTICAL_BACKGROUND, vShadowBg);

                JComponent layeredPane = getLayeredPane();
                if (layeredPane == null) {
                    // This could happen if owner is null.
                    return;
                }

                int layeredPaneWidth = layeredPane.getWidth();
                int layeredPaneHeight = layeredPane.getHeight();

                point.x = x;
                point.y = y;
                SwingUtilities.convertPointFromScreen(point, layeredPane);

                // If needed paint dirty region of the horizontal snapshot.
                rect.x = point.x;
                rect.y = point.y + height - SHADOW_SIZE;
                rect.width = width;
                rect.height = SHADOW_SIZE;

                if ((rect.x + rect.width) > layeredPaneWidth) {
                    rect.width = layeredPaneWidth - rect.x;
                }
                if ((rect.y + rect.height) > layeredPaneHeight) {
                    rect.height = layeredPaneHeight - rect.y;
                }
                if (!rect.isEmpty()) {
                    Graphics g = hShadowBg.createGraphics();
                    g.translate(-rect.x, -rect.y);
                    g.setClip(rect);
                    boolean doubleBuffered = layeredPane.isDoubleBuffered();
                    layeredPane.setDoubleBuffered(false);
                    layeredPane.paint(g);
                    layeredPane.setDoubleBuffered(doubleBuffered);
                    g.dispose();
                }

                // If needed paint dirty region of the vertical snapshot.
                rect.x = point.x + width - SHADOW_SIZE;
                rect.y = point.y;
                rect.width = SHADOW_SIZE;
                rect.height = height - SHADOW_SIZE;

                if ((rect.x + rect.width) > layeredPaneWidth) {
                    rect.width = layeredPaneWidth - rect.x;
                }
                if ((rect.y + rect.height) > layeredPaneHeight) {
                    rect.height = layeredPaneHeight - rect.y;
                }
                if (!rect.isEmpty()) {
                    Graphics g = vShadowBg.createGraphics();
                    g.translate(-rect.x, -rect.y);
                    g.setClip(rect);
                    boolean doubleBuffered = layeredPane.isDoubleBuffered();
                    layeredPane.setDoubleBuffered(false);
                    layeredPane.paint(g);
                    layeredPane.setDoubleBuffered(doubleBuffered);
                    g.dispose();
                }
            } catch (AWTException e) {
                canMakeSnapshots = false;
                useOriginalFactory = true;
            } catch (SecurityException e) {
                canMakeSnapshots = false;
                useOriginalFactory = true;
            }
        }

        /**
         * @return the top level layered pane which contains the owner.
         */
        private JComponent getLayeredPane() {
            // The code below is copied from PopupFactory#LightWeightPopup#show()
            Container parent = null;
            if (owner != null) {
                parent = owner instanceof Container
                        ? (Container) owner 
                        : owner.getParent();
            }
            // Try to find a JLayeredPane and Window to add 
            for (Container p = parent; p != null; p = p.getParent()) {
                if (p instanceof JRootPane) {
                    if (p.getParent() instanceof JInternalFrame) {
                        continue;
                    }
                    parent = ((JRootPane)p).getLayeredPane();
                    // Continue, so that if there is a higher JRootPane, we'll
                    // pick it up.
                } else if(p instanceof Window) {
                    if (parent == null) {
                        parent = p;
                    }
                    break;
                } else if (p instanceof JApplet) {
                    // Painting code stops at Applets, we don't want
                    // to add to a Component above an Applet otherwise
                    // you'll never see it painted.
                    break;
                }
            }
            return (JComponent) parent;
        }

    }

    /**
     * Listens to changes of the L&F and recalculates useOriginalFactory.
     */
    private static class LookAndFeelChangeHandler implements PropertyChangeListener {

        /**
         * Recalculates useOriginalFactory if the look&amp;feel changes.
         * 
         * @param evt  describes the property change
         */
        public void propertyChange(PropertyChangeEvent evt) {
            useOriginalFactory = computeUseOriginalFactory();
        }
    }
}
