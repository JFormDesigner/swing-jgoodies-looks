package com.jgoodies.swing.util;

/*
 * Copyright (c) 2002 Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of Karsten Lentzsch.  
 * Use is subject to license terms.
 *
 */
import java.awt.*;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JScrollPane;
import javax.swing.RootPaneContainer;

import com.jgoodies.util.Utilities;
import com.jgoodies.util.logging.Logger;

/**
 * This class consists only of static methods that provides
 * useful code for general Swing tasks.
 *
 * @author Karsten Lentzsch
 */
public final class MySwingUtilities {
    
    private static Logger logger = Logger.getLogger("MySwingUtilities");
    
	
	/**
	 * Updates all UIs of all frames.
	 * Gives instances of AbstractFrame the chance to update special component UIs.
	 */
	public static void updateAllUIs() {
		logger.info("Updating all UIs.");

		// Tell all Frames to update their component UIs.
		Frame[] frames = Frame.getFrames();
		for (int i = 0; i < frames.length; i++) {
			updateComponentTreeUI(frames[i]);
		}
	}
	

   /**
     * A simple minded look and feel change: ask each node in the tree
     * to updateUI() -- that is, to initialize its UI property with the
     * current look and feel.
     * Unlike SwingUtilities, we update a <code>JToolBar's</code> childs before 
     * updating the toolbar.
     * Note, that the JScrollPane requires to be updated before its JTable
     * child (re-)configuresScrollPane.
     */
    public static void updateComponentTreeUI(Component c) {
        updateComponentTreeUI0(c);
        c.invalidate();
        c.validate();
        c.repaint();
    }

    private static void updateComponentTreeUI0(Component c) {
        if (c instanceof JScrollPane) {
            ((JComponent) c).updateUI();
        }	
    	
        Component[] children = null;
        if (c instanceof JMenu) {
            children = ((JMenu)c).getMenuComponents();
        }
        else if (c instanceof Container) {
            children = ((Container)c).getComponents();
        }
        if (children != null) {
            for(int i = 0; i < children.length; i++) {
                updateComponentTreeUI0(children[i]);
            }
        }

        if (c instanceof JComponent && !(c instanceof JScrollPane)) {
            ((JComponent) c).updateUI();
        }	
    	
    }
    
    
    /**
     * Sets the frame decoration property in 1.4 environments.
     */
	public static void setFrameDecoration() {
	    //!"true".equals(System.getProperty("useLAFFrameDecoration"))
		if (Utilities.IS_BEFORE_14) {
			logger.info("Default Look&Feel decoration mode rejected.");
			return;
		}
		
		// [Pending:] Invoke #setDefault... directly in 1.4.
		try {
			java.lang.reflect.Method method =
				JFrame.class.getMethod("setDefaultLookAndFeelDecorated", new Class[] { Boolean.TYPE });
			method.invoke(JFrame.class, new Object[] { Boolean.TRUE });
			logger.info("Default Look&Feel Decoration mode set.");
		} catch (NoSuchMethodException e) {
		} catch (java.lang.reflect.InvocationTargetException e) {
		} catch (IllegalAccessException e) {
		}
	}
	
	
    /** 
     * Tries to pack the given windows.<p>
     * 
     * [Pending]: Workaround for Bug #4379472, java.awt.Window.pack()
     * sets zero window size under KDE and other window managers.<p>
	 * 
	 * Returns whether it seems that the windows has been successfully packed.
     */
	public static boolean packWithWorkaround(Window w) {
	    return packWithWorkaround(w, null);
	}


	/**
	 * Tries to pack the given window. In case of trouble, uses
	 * the specified default size.<p>
	 * 
	 * Returns whether it seems that the windows has been successfully packed.
	 */
	public static boolean packWithWorkaround(Window w, Dimension defaultSize) {
    	w.pack();
    	
    	if (Utilities.IS_WINDOWS) return true;
    
    	int marginGuess =  6;
    	int topGuess    = 26;
    	Component content = (w instanceof RootPaneContainer)
    	    ? ((RootPaneContainer) w).getContentPane()
    	    : w;
    	Dimension minimumSize	= content.getMinimumSize();
    	Dimension preferredSize = content.getPreferredSize();
    	Dimension screenSize	= w.getToolkit().getScreenSize();
    	int width  = Math.min(preferredSize.width,  screenSize.width  - marginGuess);
    	int height = Math.min(preferredSize.height, screenSize.height - topGuess);
    	Dimension guessedSize = new Dimension(width + marginGuess, height + topGuess);
    	Dimension size = (defaultSize != null) ? defaultSize : guessedSize;
    
    	String title = null;
    	if (w instanceof Frame)
    	    title = ((Frame) w).getTitle();
    	else if (w instanceof Dialog) 
    		title = ((Dialog) w).getTitle();
    	logger.info("#packWithWorkaround(...)");
    	logger.info("   window title    = " + title);
    	logger.info("   minimum size    = " + minimumSize);
    	logger.info("   preferred size  = " + preferredSize);
    	logger.info("   packed size     = " + w.getSize());
    	logger.info("   guessed size    = " + guessedSize);
    	logger.info("   default size    = " + defaultSize);
    	logger.info("   size            = " + size);
    
    	w.setSize(size);
    	w.validate();
    	return false;
    }

}