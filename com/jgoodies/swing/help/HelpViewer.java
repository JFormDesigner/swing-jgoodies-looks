package com.jgoodies.swing.help;

/*
 * Copyright (c) 2002 Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of Karsten Lentzsch.  
 * Use is subject to license terms.
 *
 */

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.net.URL;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.DefaultMutableTreeNode;

import com.jgoodies.plaf.BorderStyle;
import com.jgoodies.plaf.plastic.PlasticLookAndFeel;
import com.jgoodies.swing.AbstractFrame;
import com.jgoodies.swing.BorderlessSplitPane;
import com.jgoodies.swing.ExtToolBar;
import com.jgoodies.swing.application.ActionManager;
import com.jgoodies.swing.application.Workbench;
import com.jgoodies.swing.model.ValueHolder;
import com.jgoodies.swing.model.ValueModel;
import com.jgoodies.swing.util.PopupAdapter;
import com.jgoodies.util.ScreenUtils;
import com.jgoodies.util.prefs.Preferences;

/** 
 * This class implements a help viewer frame.
 * 
 * @author Karsten Lentzsch
 */

final class HelpViewer extends AbstractFrame {
	
	private final static String DIVIDER_LOCATION_KEY		 = "help.frame.dividerLocation";
	private final static int	  INITIAL_PRESENTATION_WIDTH = 420;

	private final HelpSet		helpSet;
	private final ValueModel	selectionModel = new ValueHolder();

	private History			history;
	private NavigationPanel	navigationPanel;
	private PresentationPanel	presentationPanel;
	private JSplitPane			splitPane;
	

	HelpViewer(HelpSet helpSet) {
		super(helpSet.getTitle());
		this.helpSet = helpSet;
		history = new History(helpSet.getHome());
		HelpActions.registerActions(history);
	}
	
	// Help API *************************************************************************
	
	/**
	 * Answers the <code>URL</code> that is selected in the help viewer.
	 */
	URL getSelectedURL() {
		return (URL) (getSelectionModel().getValue());
	}
	
	
	/**
	 * Shows or hides the help viewer.
	 */
	void setDisplayed(boolean display) {
		if (display)
			show((URL) history.getHome());
		else
			setVisible(false);
	}
	
	
	/**
	 * Makes the help viewer visible and opens the specified <code>URL</code>.
	 */
	void show(URL url) {
		if (!isVisible())
			open();
		else {
			setState(Frame.NORMAL);
			setVisible(true);
		}
		setSelection(url);
	}
	
	
	// Abstract Building ****************************************************************
	
	/**
	 * Builds the help viewer.
	 */
	public void build() {
		super.build();
		registerPopupMenu();
		setTitle(getTitle());
	}
	
	
	/**
	 * Unlike the default frames, we are located on screen north-east by default.
	 */
	protected void locateOnScreen() {
		ScreenUtils.locateOnScreenNorthEast(this);
	}
	
	
	/**
	 * Builds and answers the help viewer's content pane.
	 */
	protected JComponent buildContentPane() {
		JPanel content = new JPanel(new BorderLayout(0, 3));
		content.add(buildToolBar(), 					BorderLayout.NORTH);
		content.add(buildNavigationPresentationPanel(), BorderLayout.CENTER);
		return content;
	}
	
	
	// Concrete Building ****************************************************************
	
	// Currently not used
	/*
	private JMenuBar buildMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		Set the header style here!!
		ExtMenu menu;

		menu = new ExtMenu("File", 'F');
		menu.add(ActionManager.get(HelpActions.HELP_PAGE_SETUP_ID));
		menu.add(ActionManager.get(HelpActions.HELP_PRINT_ID));
		menu.addSeparator();
		menu.add(ActionManager.get(HelpActions.HELP_CLOSE_ID));
		menuBar.add(menu);

		menu = new ExtMenu("Navigate", 'N');
		menu.add(ActionManager.get(HelpActions.HELP_GO_BACK_ID));
		menu.add(ActionManager.get(HelpActions.HELP_GO_NEXT_ID)); 
		menu.add(ActionManager.get(HelpActions.HELP_GO_HOME_ID));
		menuBar.add(menu);

		return menuBar;
	}
	*/
	
	
	/**
	 * Builds and answers the tool bar.
	 */
	private JToolBar buildToolBar() {
		ExtToolBar toolBar = new ExtToolBar(getTitle(), ExtToolBar.TOOLBAR_ONLY_STYLE);
        toolBar.putClientProperty(
            PlasticLookAndFeel.BORDER_STYLE_KEY,
            BorderStyle.SEPARATOR);
		toolBar.addGap();
		toolBar.add(history.createGoBackButton());
		toolBar.add(history.createGoNextButton());
		toolBar.addGap();
		toolBar.add(ActionManager.get(HelpActions.HELP_GO_HOME_ID));
		toolBar.addGap();
		toolBar.add(ActionManager.get(HelpActions.HELP_PRINT_ID));
		return toolBar;
	}
	
	
	/**
	 * Builds and answers the navigation and presentation split panel.
	 */
	private JComponent buildNavigationPresentationPanel() {
		JPanel panel = new JPanel(new BorderLayout());

		// Create the navigation panel, a URL tree panel.
		navigationPanel   = new NavigationPanel(helpSet.getRoot(), helpSet.getBaseURL());
		presentationPanel = new PresentationPanel();
		JComponent left   = new JScrollPane(navigationPanel);
		JComponent right  = new JScrollPane(presentationPanel);
		left.putClientProperty("jgoodies.isEtched", Boolean.TRUE);
		left.updateUI();
		right.putClientProperty("jgoodies.isEtched", Boolean.TRUE);
		right.updateUI();

		// Set preferred size with chart.
		int width = INITIAL_PRESENTATION_WIDTH;
		int height = ScreenUtils.has1024x768Screen() ? 600 : (ScreenUtils.has800x600Screen() ? 550 : 410);
		right.setPreferredSize(new Dimension(width, height));

		// Add a splitpane with tree in the left and graph in the right.
		splitPane = new BorderlessSplitPane(JSplitPane.HORIZONTAL_SPLIT, left, right);
		splitPane.setOneTouchExpandable(false);

		panel.add(splitPane, BorderLayout.CENTER);
		panel.setBorder(BorderFactory.createEmptyBorder(2, 0, 0, 0));
		return panel;
	}
	
	
	/**
	 * Registers a popup menu.
	 */
	private void registerPopupMenu() {
		PopupAdapter popupAdapter = new PopupAdapter() {
			protected JPopupMenu createPopupMenu() {
				JPopupMenu popupMenu = new JPopupMenu(getTitle() + " Menu");
				popupMenu.add(ActionManager.get(HelpActions.HELP_GO_BACK_ID));
				popupMenu.add(ActionManager.get(HelpActions.HELP_GO_NEXT_ID));
				popupMenu.add(ActionManager.get(HelpActions.HELP_GO_HOME_ID));
				popupMenu.addSeparator();
				popupMenu.add(ActionManager.get(HelpActions.HELP_PRINT_ID));

				return popupMenu;
			}
		};
		navigationPanel.addMouseListener(popupAdapter);
		presentationPanel.addMouseListener(popupAdapter);
	}
	
	
	// Overriding superclass behavior **************************************************
	
	protected Dimension getFrameMinimumSize()  { return new Dimension(200, 200);	}
	protected String 	 getFrameID()			{ return "help";					}
	
	protected void doCloseWindow()			{ setVisible(false); 				}
	protected void resizeHook(JComponent c)	{}
	
	
	// Misc *****************************************************************************
	
	/**
	 * Registers listeners for the navigation selection, and history.
	 */
	protected void registerListeners() {
		super.registerListeners();

		// History listens to selection changes.
		getSelectionModel().addChangeListener(history);

		// Listen to history change events.
		history.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent event) {
				setSelection((URL) history.getSelection());
			}
		});

		// -------------------------------------------------------------------

		// Navigation panel listens to selection changes.
		getSelectionModel().addChangeListener(navigationPanel);

		// Listen to navigation tree selection events.
		navigationPanel.addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent e) {
				JTree tree = (JTree) e.getSource();
				Object newSelection = tree.getLastSelectedPathComponent();
				if (null == newSelection)
					return;
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) newSelection;
				HelpItem item = (HelpItem) node.getUserObject();
				URL newURL = item.getURL();
				if (null == newURL)
					return;
				setSelection(newURL);
			}
		});

		// -------------------------------------------------------------------

		// Presentation panel listens to selection changes.
		selectionModel.addChangeListener(presentationPanel);

		// Listen to presentation hyperlink events.
		presentationPanel.addHyperlinkListener(new HyperlinkListener() {
			public void hyperlinkUpdate(HyperlinkEvent e) {
				if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED)
					setSelection(e.getURL());
			}
		});
	}
	
	
	// Storing and Restoring window layout *********************************************
	
	/**
	 *  Set the frame's split pane divider location from the Preferences.
	 */
	private void restoreDividerLocation(Preferences prefs) {
		// Read the raw layout data.
		int dividerLocation = prefs.getInt(DIVIDER_LOCATION_KEY, -1);

		// Do nothing if the data couldn't be read.
		if (dividerLocation == -1)
			return;

		// Do nothing if the divider location exceeds the frame's width.
		if (dividerLocation > getSize().width)
			return;

		// Set the divider location.
		splitPane.setDividerLocation(dividerLocation);
	}
	
	
	/**
	 * Restores the frame's state, here: bounds and divider location.
	 */
	protected void restoreState() {
		super.restoreState(); 
		restoreDividerLocation(Workbench.userPreferences());
	}
	
	
	/**
	 * Stores the frame's state, here: bounds and divider location.
	 */
	public void storeState() {
		if (splitPane == null)
			return;
			
		super.storeState();
		Workbench.userPreferences().putInt(DIVIDER_LOCATION_KEY, splitPane.getDividerLocation());
	}
	

	// Misc *****************************************************************************
	
	private ValueModel getSelectionModel() { return selectionModel; }
	
	private void setSelection(URL url) { getSelectionModel().setValue(url); }
	
	
	
    /*
     * Unlike its superclass this helper class displays a simplified tooltip. 
     */
	private static class History extends com.jgoodies.swing.util.History {

    	private static final char ZIP_SEPARATOR = '/';
    	
    	public History(URL home) {
    		super(10);
    		setHome(home);
    	}
    	
    	protected String createToolTip(Object anObject) {
    		URL url = (URL) anObject;
    		String filename = url.getFile();
    		int lastSeparatorIndex = filename.lastIndexOf(ZIP_SEPARATOR);
    		return filename.substring(lastSeparatorIndex + 1);
    	}
    }
    	
	
}