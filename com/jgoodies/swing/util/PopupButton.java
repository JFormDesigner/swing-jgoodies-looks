package com.jgoodies.swing.util;

/*
 * Copyright (c) 2003 JGoodies Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of JGoodies Karsten Lentzsch.  
 * Use is subject to license terms.
 *
 */
 
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;


/**
 * A combination of an action button and a popup button.<p>
 * 
 * It is intended to be used in cases where you have a list of 
 * frequently used items, which you want to choose from a menu.
 * To quickly choose the most recently used element, you click
 * the action button. This is popular in recent Web browsers.
 *
 * @author Karsten Lentzsch
 */

public final class PopupButton extends JPanel {

	private final JButton mainButton;
	private final JPopupMenu popupMenu;

	private AbstractButton arrowButton;
	private boolean mouseIsOver = false;
	
    
    // Instance Creation ****************************************************

	/**
	 * Constructs a popup button using the given main button and popup menu.
	 */
	public PopupButton(JButton mainButton, JPopupMenu popupMenu) {
		this.mainButton = mainButton;
		this.popupMenu = popupMenu;
		build();
	}
    
    
    // Add components to a tool bar *****************************************
    public void addTo(JToolBar toolBar) {
        toolBar.add(mainButton);
        toolBar.add(arrowButton);
    }
    
	
	
	/**
	 * Builds the popup button component.
	 */ 
	protected void build() {
		arrowButton = createArrowButton();

		mainButton.getModel().addChangeListener(createMainButtonChangeListener());
		mainButton.addMouseListener(createMainButtonMouseListener());
		popupMenu.addPopupMenuListener(createPopupMenuListener());

		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.VERTICAL;
		gbc.anchor = GridBagConstraints.WEST;

		gbc.gridwidth = 1;
		gbc.gridheight = GridBagConstraints.REMAINDER;
		gbc.weightx = 0.0;
		add(mainButton, gbc);

		gbc.gridwidth = GridBagConstraints.REMAINDER;
		add(arrowButton, gbc);

		setOpaque(false);
		//updateSize();
	}
	

	/**
	 * Creates and answers the arrow button.
	 */	
	private AbstractButton createArrowButton() {
        Icon mainButtonIcon = mainButton.getIcon();
        int iconHeight = mainButtonIcon != null
            ? mainButtonIcon.getIconHeight()
            : 16;
        Icon arrowIcon = new ArrowIcon();
        Icon compoundIcon = new CompoundIcon(
            new NullIcon(new Dimension(arrowIcon.getIconWidth(), iconHeight)),
            arrowIcon, 
            CompoundIcon.CENTER);
		AbstractButton button = new ToolBarButton(compoundIcon);
		button.setModel(new DelegatingButtonModel(mainButton.getModel()));
		button.addActionListener(createArrowButtonActionListener());
		button.addMouseListener(createArrowButtonMouseListener());
        Insets insets = button.getMargin();
		button.setMargin(new Insets(insets.top, 0, insets.bottom, 0));
		return button;
	}
	
	
	/**
	 * Creates and answers the arrow button's action listener, 
	 * that in turn shows the menu and sets the button to armed.
	 */
	private ActionListener createArrowButtonActionListener() {
		return new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				popupMenu.show(mainButton, 0, mainButton.getHeight());
				arrowButton.getModel().setArmed(true);
			}
		};
	}
	
	
	/**
	 * Creates and answers the arrow button's mouse listener,
	 * that switches the rollover property on and off.
	 */
	private MouseListener createArrowButtonMouseListener() {
		return new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				mouseIsOver = true;
				mainButton.getModel().setRollover(true);
			}
			public void mouseExited(MouseEvent e) {
				mouseIsOver = false;
				mainButton.getModel().setRollover(popupMenu.isVisible());
			}
		};
	}
	
	
	private ChangeListener createMainButtonChangeListener() {
		return new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				arrowButton.repaint();
			}
		};
	}
	
	
	private MouseListener createMainButtonMouseListener() {
		return new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				mouseIsOver = true;
				arrowButton.getModel().setRollover(true);
			}
			public void mouseExited(MouseEvent e) {
				mouseIsOver = false;
				arrowButton.getModel().setRollover(popupMenu.isVisible());
			}
		};
	}
	
	
	private PopupMenuListener createPopupMenuListener() {
		return new PopupMenuListener() {
			public void popupMenuCanceled(PopupMenuEvent e) {}
			public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
				arrowButton.getModel().setRollover(mouseIsOver);
				arrowButton.getModel().setPressed(false);
			}
			public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
				arrowButton.getModel().setRollover(true);
				arrowButton.getModel().setPressed(true);
			}
		};
	}
	
	
	public float getAlignmentX() {
		return mainButton.getAlignmentX();
	}
	
	
	public Dimension getMaximumSize() {
		return getPreferredSize();
	}
	
	
	public void updateUI() {
		super.updateUI();
		if (null == popupMenu)
			return;

		MySwingUtilities.updateComponentTreeUI(popupMenu);
	}


	// Helper Classes *******************************************************
		
	// An icon implementation for the arrow button
    private static class ArrowIcon implements Icon {

        private static final int HEIGHT = 4;
        private static final int WIDTH  = 2 * HEIGHT + 1;

        public int getIconWidth() {
            return WIDTH;
        }
        public int getIconHeight() {
            return HEIGHT;
        }

        public void paintIcon(Component c, Graphics g, int x, int y) {
            AbstractButton b = (AbstractButton) c;
            ButtonModel m = b.getModel();
            int w = getIconWidth() - 2;
            int h = HEIGHT;

            g.translate(x, y);

            g.setColor(
                UIManager.getColor(
                    m.isEnabled() ? "controlText" : "textInactiveText"));
            for (int i = 0; i < h; i++)
                g.drawLine(i + 1, i, w - i, i);

            g.translate(-x, -y);
        }
    }
	
	// A button model that delegates everything to a delegate.
	private static class DelegatingButtonModel extends DefaultButtonModel {
		
		private final ButtonModel delegate;

		private DelegatingButtonModel(ButtonModel delegate) { this.delegate = delegate; }

		public boolean isEnabled()	{ return delegate.isEnabled(); 					 }
		public boolean isRollover()	{ return delegate.isRollover(); 					 }
		public boolean isArmed()		{ return super.isArmed()   || delegate.isArmed();   }
		public boolean isPressed()	{ return super.isPressed() || delegate.isPressed(); }

		public void setRollover(boolean b) { 
            delegate.setRollover(b); 
        }

		public void setEnabled(boolean b) {
			if (delegate != null)
				delegate.setEnabled(b);
		}
        
	}
	
}