/*
 * Copyright (c) 2003 JGoodies Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of JGoodies Karsten Lentzsch.  
 * Use is subject to license terms.
 *
 */

package com.jgoodies.looks.demo;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.jgoodies.looks.launcher.Launcher;
import com.jgoodies.plaf.Options;
import com.jgoodies.plaf.plastic.PlasticLookAndFeel;
import com.jgoodies.plaf.windows.ExtWindowsLookAndFeel;
import com.jgoodies.swing.application.ResourceManager;

/**
 * Builds the <code>JMenuBar</code> and pull-down menus in the Looks Demo.
 *
 * @author Karsten Lentzsch
 */

final class MenuBuilder {
    
    private static final String HTML_TEXT = 
        "<html><b>Bold</b>, <i>Italics</i>, <tt>Typewriter</tt></html>";
    
	/**
	 * Builds, configures, and answers the menubar. Requests HeaderStyle, 
	 * look-specific BorderStyles, and Plastic 3D hint from Launcher.
	 */
	JMenuBar buildMenuBar(Launcher launcher) {
		JMenuBar bar = new JMenuBar();
		bar.putClientProperty(	Options.HEADER_STYLE_KEY, 
								launcher.menuBarHeaderStyle());
		bar.putClientProperty(	PlasticLookAndFeel.BORDER_STYLE_KEY, 
								launcher.menuBarPlasticBorderStyle());
		bar.putClientProperty(	ExtWindowsLookAndFeel.BORDER_STYLE_KEY, 
								launcher.menuBarWindowsBorderStyle());
		bar.putClientProperty(	PlasticLookAndFeel.IS_3D_KEY,
								launcher.menuBar3DHint());

		bar.add(buildFileMenu());
		bar.add(buildRadioMenu());
		bar.add(buildCheckMenu());
        bar.add(buildHtmlMenu());
        bar.add(buildAlignmentTestMenu());
		bar.add(buildHelpMenu());
		return bar;
	}
	
	
	/**
	 * Builds and answers the file menu.
	 */
	private JMenu buildFileMenu() {
		JMenuItem item;
		
		JMenu menu = new JMenu("File");
		menu.setMnemonic('F');
		
		// Build a submenu that has the noIcons hint set.
		JMenu submenu = new JMenu("New");
        submenu.setMnemonic('N');
		submenu.putClientProperty(Options.NO_ICONS_KEY, Boolean.TRUE);
		submenu.add(new JMenuItem("Project...", 'P'));
		submenu.add(new JMenuItem("Folder...", 'F'));
		submenu.add(new JMenuItem("Document...", 'D'));
		submenu.addSeparator();
		submenu.add(new JMenuItem("No icon hint set"));
		
		menu.add(submenu);
		menu.addSeparator();
		item = new JMenuItem("Close", 'C');
		item.setAccelerator(KeyStroke.getKeyStroke("ctrl F4"));
		menu.add(item);
		item = new JMenuItem("Close All", 'o');
		item.setAccelerator(KeyStroke.getKeyStroke("ctrl shift F4"));
		menu.add(item);
		menu.addSeparator();
		item = new JMenuItem("Save description.txt",		 readImageIcon("save_edit.gif"));
        item.setMnemonic('d');
		item.setAccelerator(KeyStroke.getKeyStroke("ctrl S"));
		item.setEnabled(false);
		menu.add(item);
		item = new JMenuItem("Save description.txt As...", readImageIcon("saveas_edit.gif"));
        item.setMnemonic('e');
        menu.add(item);
		item = new JMenuItem("Save All");
        item.setMnemonic('A');
		item.setAccelerator(KeyStroke.getKeyStroke("ctrl shift S"));
		item.setEnabled(false);
		menu.add(item);
		menu.addSeparator();
		item = new JMenuItem("Print", readImageIcon("print.gif"));
        item.setMnemonic('P');
		item.setAccelerator(KeyStroke.getKeyStroke("ctrl P"));
		menu.add(item);
		menu.addSeparator();
		menu.add(new JMenuItem("1 WinXPMenuItemUI.java",  '1'));
		menu.add(new JMenuItem("2 WinXPUtils.java",       '2'));
		menu.add(new JMenuItem("3 WinXPBorders.java",     '3'));
		menu.add(new JMenuItem("4 WinXPLookAndFeel.java", '4'));
		menu.addSeparator();
		menu.add(new JMenuItem("Exit", 'E'));
		return menu;
	}
    

	/**
	 * Builds and answers a menu with different JRadioButtonMenuItems.
	 */
	private JMenu buildRadioMenu() {
		JRadioButtonMenuItem item;
		
		JMenu menu = new JMenu("Radio");
		menu.setMnemonic('R');
		
		// Default icon
		ButtonGroup group1 = new ButtonGroup();
		item = createRadioItem(true, false);
		group1.add(item);
		menu.add(item);
		item = createRadioItem(true, true);
		group1.add(item);
		menu.add(item);
		
		menu.addSeparator();

		item = createRadioItem(false, false);
		menu.add(item);
		item = createRadioItem(false, true);
		menu.add(item);
		
		menu.addSeparator();
		
		// Custom icon
		ButtonGroup group2 = new ButtonGroup();
		item = createRadioItem(true, false);
		item.setIcon(readImageIcon("pie_mode.png"));
		group2.add(item);
		menu.add(item);
		item = createRadioItem(true, true);
		item.setIcon(readImageIcon("bar_mode.png"));
		group2.add(item);
		menu.add(item);

		menu.addSeparator();

		item = createRadioItem(false, false);
		item.setIcon(readImageIcon("alphab_sort.png"));
		//item.setDisabledIcon(Utils.getIcon("alphab_sort_gray.png"));
		menu.add(item);
		item = createRadioItem(false, true);
		item.setIcon(readImageIcon("size_sort.png"));
		item.setDisabledIcon(readImageIcon("size_sort_gray.png"));
		menu.add(item);
		
		return menu;	
	}
	
	
	/**
	 * Builds and answers a menu with different JCheckBoxMenuItems.
	 */
	private JMenu buildCheckMenu() {
		JCheckBoxMenuItem item;
		
		JMenu menu = new JMenu("Check");
		menu.setMnemonic('C');
		
		// Default icon
		menu.add(createCheckItem(true, false));
		menu.add(createCheckItem(true, true));
		menu.addSeparator();
		menu.add(createCheckItem(false, false));
		menu.add(createCheckItem(false, true));
		
		menu.addSeparator();
		
		// Custom icon
		item = createCheckItem(true, false);
		item.setIcon(readImageIcon("check.gif"));
		item.setSelectedIcon(readImageIcon("check_selected.gif"));
		menu.add(item);
		item = createCheckItem(true, true);
		item.setIcon(readImageIcon("check.gif"));
		item.setSelectedIcon(readImageIcon("check_selected.gif"));
		menu.add(item);

		menu.addSeparator(); 
		
		item = createCheckItem(false, false);
		item.setIcon(readImageIcon("check.gif"));
		item.setSelectedIcon(readImageIcon("check_selected.gif"));
		menu.add(item);
		item = createCheckItem(false, true);
		item.setIcon(readImageIcon("check.gif"));
		item.setSelectedIcon(readImageIcon("check_selected.gif"));
		item.setDisabledSelectedIcon(readImageIcon("check_disabled_selected.gif"));
		menu.add(item);
		
		return menu;
	}
	
	
    /**
     * Builds and answers a menu with items that use a HTML text.
     */
    private JMenu buildHtmlMenu() {
        JMenu menu = new JMenu("Styled");
        menu.setMnemonic('S');

        menu.add(createSubmenu(HTML_TEXT));
        menu.add(new JMenuItem(HTML_TEXT));
        menu.addSeparator();
        menu.add(new JRadioButtonMenuItem(HTML_TEXT, false));
        menu.add(new JRadioButtonMenuItem(HTML_TEXT, true));
        menu.addSeparator();
        menu.add(new JCheckBoxMenuItem(HTML_TEXT, true));
        menu.add(new JCheckBoxMenuItem(HTML_TEXT, false));
        return menu;
    }
        
    /**
     * Builds and answers a menu with items that use a HTML text.
     */
    private JMenu buildAlignmentTestMenu() {
        JMenu menu = new JMenu("Alignment");
        menu.setMnemonic('A');
        
        menu.add(new JMenuItem("Menu item"));
        menu.add(new JMenuItem("Menu item with icon", readImageIcon("refresh.gif")));
        menu.addSeparator();
        JMenu submenu = createSubmenu("Submenu");
        menu.add(submenu);

        submenu = createSubmenu("Submenu with icon");
        submenu.setIcon(readImageIcon("refresh.gif"));
        menu.add(submenu);
        
        return menu;
    }
        
	/**
	 * Builds and answers the help menu.
	 */
	private JMenu buildHelpMenu() {
		JMenu menu = new JMenu("Help");
		menu.setMnemonic('H');
		
		JMenuItem item = new JMenuItem("Help Contents", readImageIcon("help.gif"));
        item.setMnemonic('H');
		item.addActionListener(DemoFrame.createHelpActionListener());
		menu.add(item);
		menu.addSeparator();
		item = new JMenuItem("About");
		item.setMnemonic('a');
		item.addActionListener(DemoFrame.createAboutActionListener());
		menu.add(item);
		
		return menu;
	}
	
		
	/**
	 * Creates and answers a <code>JRadioButtonMenuItem</code>
	 * with the given enablement and selection state.
	 */
	private JRadioButtonMenuItem createRadioItem(boolean enabled, boolean selected) {
		JRadioButtonMenuItem item = new JRadioButtonMenuItem(
			getToggleLabel(enabled, selected),
			selected);
		item.setEnabled(enabled);
		item.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				JRadioButtonMenuItem source = (JRadioButtonMenuItem) e.getSource();
				source.setText(getToggleLabel(source.isEnabled(), source.isSelected()));
			}
		});
		return item;
	}
	

	/**
	 * Creates and answers a <code>JCheckBoxMenuItem</code>
	 * with the given enablement and selection state.
	 */
	private JCheckBoxMenuItem createCheckItem(boolean enabled, boolean selected) {
		JCheckBoxMenuItem item = new JCheckBoxMenuItem(
			getToggleLabel(enabled, selected),
			selected);
		item.setEnabled(enabled);
		item.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				JCheckBoxMenuItem source = (JCheckBoxMenuItem) e.getSource();
				source.setText(getToggleLabel(source.isEnabled(), source.isSelected()));
			}
		});
		return item;
	}
	
	
	/**
	 *  Answers an appropriate label for the given enablement and selection state.
	 */
	private String getToggleLabel(boolean enabled, boolean selected) {
		String prefix = enabled  ? "Enabled" : "Disabled";
		String suffix = selected ? "Selected" : "Deselected";
		return prefix + " and " + suffix;
	}
	
    // Helper Code **********************************************************************
    
    /**
     * Looks up and answers an icon for the specified filename suffix.<p>
     */
    private ImageIcon readImageIcon(String filename) {
        return ResourceManager.readImageIcon("images/" + filename);
    }
    
    /**
     * Creates and answers a submenu labeled with the given text.
     */
    private JMenu createSubmenu(String text) {
        JMenu submenu = new JMenu(text);
        submenu.add(new JMenuItem("Item 1"));
        submenu.add(new JMenuItem("Item 2"));
        submenu.add(new JMenuItem("Item 3"));
        return submenu;
    }
    

}