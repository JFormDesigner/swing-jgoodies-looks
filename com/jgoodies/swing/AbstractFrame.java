package com.jgoodies.swing;

/*
 * Copyright (c) 2002 Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of Karsten Lentzsch.  
 * Use is subject to license terms.
 *
 */
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;

import com.jgoodies.layout.Resizer;
import com.jgoodies.swing.application.ResourceIDs;
import com.jgoodies.swing.application.ResourceManager;
import com.jgoodies.swing.application.Workbench;
import com.jgoodies.swing.util.MySwingUtilities;
import com.jgoodies.util.ScreenUtils;
import com.jgoodies.util.Utilities;
import com.jgoodies.util.prefs.Preferences;

/**
 * Provides convenience code that is frequently used in application frames:
 * <ul>
 * <li>standardized build process,
 * <li>minimum size,
 * <li>aesthetic height-width ratio,
 * <li>good screen location,
 * <li>frame icons fetched from resource manager,
 * <li>storing and restoring state: bounds.
 * </ul>
 * 
 * @see	com.jgoodies.swing.application.AbstractMainFrame
 *
 * @author Karsten Lentzsch
 */
public abstract class AbstractFrame extends JFrame {

	private Rectangle storedBounds;
	

	/**
	 * Constructs an <code>AbstractFrame</code> using the given title.
	 */
	protected AbstractFrame(String title) {
		super(title);
	}
	
	
	// Building *************************************************************
	
	/**
	 * Builds the frame's content pane, packs it, locates it on the screen,
	 * sets the icon, restores the saved state, and registers listeners.
	 * It then is ready to be opened.<p>
	 * 
	 * Subclasses should rarely override this method.
	 */
	public void build() {
		JComponent content = buildContentPane();
		resizeHook(content);
		setContentPane(content);
		// pack(); TODO: Check in 1.5
		MySwingUtilities.packWithWorkaround(this);  
		locateOnScreen();

		setIcon();
		restoreState();
		registerListeners();
	}
	

	// Abstract Behavior ****************************************************
	
	/**
	 * Subclasses must override this method to build and answer the content pane.
	 */
	abstract protected JComponent buildContentPane();
	
	/**
	 * Subclasses must override this method to define the frame's minimum size.
	 */
	abstract protected Dimension getFrameMinimumSize();
	
	/**
	 * Subclasses must override this method to define the close window behavior.
	 */
	abstract protected void doCloseWindow();

	/**
	 * Subclasses must override this method to answer the frame's id.
	 */
	abstract protected String getFrameID();
	

	// Default Behavior *****************************************************
	
	/**
	 * Makes the frame visible.
	 */
	public void open() {
		setVisible(true);
	}
	
	/**
	 * Resizes the specified component. This is called during the #build
	 * process and enables subclasses to achieve a better aspect ratio,
	 * by applying a resizer, e.g. the <code>Resizer</code>.
	 */	
	protected void resizeHook(JComponent component) {
		Resizer.DEFAULT.resize(component);
	}
	
	/**
	 * Locates the frame on the screen. The default implementation centers
	 * centers it on the screen.
	 */
	protected void locateOnScreen() {
		ScreenUtils.locateOnScreenCenter(this);
	}
	
	
	/**
	 * Registers required listeners.
	 */	
	protected void registerListeners() {
	    addWindowClosingListener();
	    addMinimumSizeListener();
	}
	

	/**
	 * Restores the frame's state from the user preferences.
	 */
	protected void restoreState() {
		restoreBounds(Workbench.userPreferences());
		storedBounds = getBounds();
	}
	

	/**
	 * Stores the frame's state in the user preferences.
	 */
	public void storeState() {
		storeBounds(Workbench.userPreferences());
	}
	
	
    /**
     * Sets the frame's icon, that is fetched via the
     * <code>ResourceManager</code>.
     */
    protected void setIcon() {
        ImageIcon defaultLogo =
            ResourceManager.getIcon(ResourceIDs.APPLICATION_ICON);
        if (defaultLogo == null) {
            return;
        } else if (
            Utilities.IS_LOW_RES
                && (Utilities.IS_WINDOWS2000 || Utilities.IS_WINDOWS_XP)) {
            ImageIcon smallLogo =
                ResourceManager.getIcon(ResourceIDs.APPLICATION_12x12_ICON);
            setIconImage(
                smallLogo != null
                    ? smallLogo.getImage()
                    : defaultLogo.getImage());
        } else {
            setIconImage(defaultLogo.getImage());
        }
    }

    // Basic Listeners ******************************************************

    private void addMinimumSizeListener() {
        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                ScreenUtils.ensureMinimumSize(
                    AbstractFrame.this,
                    AbstractFrame.this.getFrameMinimumSize());
                if (!isVisible()
                    || ScreenUtils.isNearlyFullScreen(AbstractFrame.this))
                    return;
                storedBounds = getBounds();
            }
        });
    }
	

	private void addWindowClosingListener() {
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				doCloseWindow();
			}
		});
	}
	
	
	// Storing and Restoring Bounds *****************************************
	
    private static final String FRAME_ORIGIN_X_KEY      = "frame.originX";
    private static final String FRAME_ORIGIN_Y_KEY      = "frame.originY";
    private static final String FRAME_WIDTH_KEY         = "frame.width";
    private static final String FRAME_HEIGHT_KEY        = "frame.height";
    private static final int    MINIMUM_VISIBLE_POINTS  = 40;
    
	/**
	 * Stores the frame's bounds in the given preferences.
	 */
	private void storeBounds(Preferences prefs) {
		String prefix = getFrameID() + '.';
		Rectangle bounds = ScreenUtils.isNearlyFullScreen(this) 
			? storedBounds : getBounds();

		prefs.putInt(prefix + FRAME_ORIGIN_X_KEY,	bounds.x);
		prefs.putInt(prefix + FRAME_ORIGIN_Y_KEY,	bounds.y);
		prefs.putInt(prefix + FRAME_WIDTH_KEY,		bounds.width);
		prefs.putInt(prefix + FRAME_HEIGHT_KEY,		bounds.height);
	}
	
	
	/**
	 * Restores the frame's bounds from the given preferences.
	 */
	private void restoreBounds(Preferences prefs) {
		String prefix = getFrameID() + '.';

		// Read the raw layout data.
		int originX     = prefs.getInt(prefix + FRAME_ORIGIN_X_KEY, -1);
		int originY     = prefs.getInt(prefix + FRAME_ORIGIN_Y_KEY, -1);
		int frameWidth  = prefs.getInt(prefix + FRAME_WIDTH_KEY,    -1);
		int frameHeight = prefs.getInt(prefix + FRAME_HEIGHT_KEY,   -1);

        // Do nothing if any of the data couldn't be read.
        if ((originX == -1)
            || (originY == -1)
            || (frameWidth == -1)
            || (frameHeight == -1)) {
            return;
        }
			
		restoreBounds(new Rectangle(originX, originY, frameWidth, frameHeight));
	}
	
	
	/**
	 * Restores the frame's bounds from a given <code>Rectangle</code>.
	 */
	protected void restoreBounds(Rectangle r) {
		Dimension screenSize = getToolkit().getScreenSize();

		// Do nothing if the frame does not fit on the screen.
		if ((r.width > screenSize.width) && (r.height > screenSize.height))
			return;

		// Do nothing if the frame is not visible with at least 
        // MINIMUM_VISIBLE_POINTS.
		// TODO: Check wether this test is correct; seems to be not (KL).
        Rectangle screenRectangle =
            new Rectangle(0, 0, screenSize.width, screenSize.height);
        screenRectangle.grow(
            -MINIMUM_VISIBLE_POINTS,
            -MINIMUM_VISIBLE_POINTS);
        if (!screenRectangle.intersects(r)) {
            return;
        }
        
		// Set the frame's location and size.
		//DebugManager.show("Setting " + originX + " "  + 
        // originY + " " + frameWidth + " " + frameHeight);
		setBounds(r);
	}

}