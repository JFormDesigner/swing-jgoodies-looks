package com.jgoodies.swing.panels;

/*
 * Copyright (c) 2002 Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of Karsten Lentzsch.  
 * Use is subject to license terms.
 *
 */

import java.awt.*;

import javax.swing.*;
import javax.swing.border.AbstractBorder;

import com.jgoodies.util.Utilities;

/** 
 * A <code>JPanel</code> subclass that provides a header with
 * icon, title and tool bar, and that uses drop shadow border.
 * This class is intended to replace the <code>JInternalFrame</code>,
 * for use outside of a <code>JDesktopPane</code>.
 * 
 * @author Karsten Lentzsch
 * @see javax.swing.JInternalFrame
 */

public class SimpleInternalFrame extends JPanel {

    private JLabel          titleLabel;
    private GradientPanel   gradientPanel;
    private JPanel          headerPanel;
    private boolean        isSelected;
    
    
    // Instance Creation ****************************************************

    /**
     * Constructs a <code>SimpleInternalFrame</code> for the specified 
     * icon, title, tool bar, and content panel.
     */
    public SimpleInternalFrame(
        Icon frameIcon,
        String title,
        JToolBar bar,
        JComponent content) {
        super(new BorderLayout());
        this.isSelected = false;
        this.titleLabel =
            new JLabel(title, frameIcon, SwingConstants.LEADING);
        JPanel top = buildHeader(titleLabel, bar);

        add(top, BorderLayout.NORTH);
        if (content != null) {
            setContent(content);
        }
        setBorder(new ShadowBorder());
        setSelected(true);
        updateHeader();
    }

    /**
     * Constructs a <code>SimpleInternalFrame</code> for the specified 
     * title, tool bar, and content panel.
     */
    public SimpleInternalFrame(String title, JToolBar bar, JComponent c) {
        this(null, title, bar, c);
    }

    /**
     * Constructs a <code>SimpleInternalFrame</code> for the specified 
     * icon, and title.
     */
    public SimpleInternalFrame(Icon icon, String title) {
        this(icon, title, null, null);
    }

    /**
     * Constructs a <code>SimpleInternalFrame</code> for the specified title.
     */
    public SimpleInternalFrame(String title) {
        this(null, title, null, null);
    }
    
    
    // Public API ***********************************************************

    /**
     * Answers the frame's icon.
     */
    public Icon getFrameIcon() {
        return titleLabel.getIcon();
    }

    /**
     * Sets a new frame icon.
     */
    public void setFrameIcon(Icon icon) {
        titleLabel.setIcon(icon);
    }

    /**
     * Answers the frame's title text.
     * 
     * @return String   the current title text
     */
    public String getTitle() {
        return titleLabel.getText();
    }
    
    /**
     * Sets a new title text
     * 
     * @param text  the new title
     */
    public void setTitle(String text) {
        titleLabel.setText(text);
    }

    /**
     * Sets a new tool bar in the header.
     * 
     * @param newToolBar the tool bar to set in the header
     */
    public void setToolBar(JToolBar newToolBar) {
        JToolBar oldToolBar = null;
        if (headerPanel.getComponentCount() > 1) {
            oldToolBar = (JToolBar) headerPanel.getComponent(1);
        }
        if (oldToolBar == newToolBar) {
            return;
        }
        if (oldToolBar != null) {
            headerPanel.remove(oldToolBar);
        }
        if (newToolBar != null) {
            newToolBar.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
            headerPanel.add(newToolBar, BorderLayout.EAST);
        }
        updateHeader();
    }

    /**
     * Answers the content - null, if none has been set.
     * 
     * @return Component
     */
    public Component getContent() {
        return hasContent() ? getComponent(1) : null;
    }
    
    /**
     * Sets a new panel content; replaces any existing content, if existing.
     * 
     * @param content   the panel's new content
     */
    public void setContent(Component content) {
        if (hasContent()) {
            remove(getContent());
        }
        add(content, BorderLayout.CENTER);
    }

    /**
     * Answers if the panel is currently selected (or in other words active)
     * or not. In the selected state, the header background will be
     * rendered differently.
     * 
     * @return boolean  a boolean, where true means the frame is selected 
     *                  (currently active) and false means it is not  
     */
    public boolean isSelected() {
        return isSelected;
    }
    
    /**
     * This panel draws its title bar differently if it is selected,
     * which may be used to indicate to the user that this panel
     * has the focus, or should get more attention than other
     * simple internal frames.
     *
     * @param selected  a boolean, where true means the frame is selected 
     *                  (currently active) and false means it is not
     */
    public void setSelected(boolean selected) {
        isSelected = selected;
        updateHeader();
    }

    // Building *************************************************************

    /**
     * Creates and answers the header panel, that consists of:
     * an icon, a title label, a tool bar, and a gradient background.
     */
    private JPanel buildHeader(JLabel label, JToolBar bar) {
        gradientPanel =
            new GradientPanel(new BorderLayout(), getHeaderBackground());
        label.setOpaque(false);

        gradientPanel.add(label, BorderLayout.WEST);
        gradientPanel.setBorder(BorderFactory.createEmptyBorder(3, 4, 3, 1));

        headerPanel = new JPanel(new BorderLayout());
        headerPanel.add(gradientPanel, BorderLayout.CENTER);
        setToolBar(bar);
        headerPanel.setBorder(new RaisedHeaderBorder());
        headerPanel.setOpaque(false);
        return headerPanel;
    }

    /**
     * Updates the header.
     */
    private void updateHeader() {
        gradientPanel.setBackground(getHeaderBackground());
        gradientPanel.setOpaque(isSelected());
        titleLabel.setForeground(getTextForeground(isSelected()));
        headerPanel.repaint();
    };

    /**
     * Updates the UI. In addition to the superclass behavior, we need
     * to update the header component.
     */
    public void updateUI() {
        super.updateUI();
        if (titleLabel != null) {
            updateHeader();
        }
    }


    // Helper Code **********************************************************

    /**
     * Checks and answers if the panel has a content component set.
     */
    private boolean hasContent() {
        return getComponentCount() > 1;
    }
    
    /**
     * Determines and answers the header's text foreground color.
     * Tries to lookup a special color from the L&amp;F.
     * In case it is absent, it uses the standard internal frame forground.
     */
    protected Color getTextForeground(boolean selected) {
        Color c =
            UIManager.getColor(
                selected
                    ? "SimpleInternalFrame.activeTitleForeground"
                    : "SimpleInternalFrame.inactiveTitleForeground");
        if (c != null) {
            return c;
        }
        return UIManager.getColor(
            selected 
                ? "InternalFrame.activeTitleForeground" 
                : "Label.foreground");

    }

    /**
     * Determines and answers the header's background color.
     * Tries to lookup a special color from the L&amp;F.
     * In case it is absent, it uses the standard internal frame background.
     */
    protected Color getHeaderBackground() {
        Color c =
            UIManager.getColor("SimpleInternalFrame.activeTitleBackground");
        if (c != null)
            return c;
        if (Utilities.IS_WINDOWS_XP && !Utilities.IS_BEFORE_14)
            c = UIManager.getColor("InternalFrame.activeTitleGradient");
        return c != null
            ? c
            : UIManager.getColor("InternalFrame.activeTitleBackground");
    }


    // Helper Classes *******************************************************

    // A custom border for the raised header pseudo 3D effect.
    private static class RaisedHeaderBorder extends AbstractBorder {

        private static final Insets INSETS = new Insets(1, 1, 1, 0);

        public Insets getBorderInsets(Component c) { return INSETS; }

        public void paintBorder(Component c, Graphics g,
            int x, int y, int w, int h) {
                
            g.translate(x, y);
            g.setColor(UIManager.getColor("controlLtHighlight"));
            g.fillRect(0, 0,   w, 1);
            g.fillRect(0, 1,   1, h-1);
            g.setColor(UIManager.getColor("controlShadow"));
            g.fillRect(0, h-1, w, 1);
            g.translate(-x, -y);
        }
    }

    // A custom border that has a shadow on the right and lower sides.
    private static class ShadowBorder extends AbstractBorder {

        private static final Insets INSETS = new Insets(1, 1, 3, 3);

        public Insets getBorderInsets(Component c) { return INSETS; }

        public void paintBorder(Component c, Graphics g,
            int x, int y, int w, int h) {
                
            Color shadow        = UIManager.getColor("controlShadow");
            Color lightShadow   = new Color(shadow.getRed(), 
                                            shadow.getGreen(), 
                                            shadow.getBlue(), 
                                            170);
            Color lighterShadow = new Color(shadow.getRed(),
                                            shadow.getGreen(),
                                            shadow.getBlue(),
                                            70);
            g.translate(x, y);
            
            g.setColor(shadow);
            g.fillRect(0, 0, w - 3, 1);
            g.fillRect(0, 0, 1, h - 3);
            g.fillRect(w - 3, 1, 1, h - 3);
            g.fillRect(1, h - 3, w - 3, 1);
            // Shadow line 1
            g.setColor(lightShadow);
            g.fillRect(w - 3, 0, 1, 1);
            g.fillRect(0, h - 3, 1, 1);
            g.fillRect(w - 2, 1, 1, h - 3);
            g.fillRect(1, h - 2, w - 3, 1);
            // Shadow line2
            g.setColor(lighterShadow);
            g.fillRect(w - 2, 0, 1, 1);
            g.fillRect(0, h - 2, 1, 1);
            g.fillRect(w-2, h-2, 1, 1);
            g.fillRect(w - 1, 1, 1, h - 2);
            g.fillRect(1, h - 1, w - 2, 1);
            g.translate(-x, -y);
        }
    }

    // A panel with a horizontal gradient background.
    private static class GradientPanel extends JPanel {
        
        private GradientPanel(LayoutManager lm, Color background) {
            super(lm);
            setBackground(background);
        }

        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (!isOpaque()) {
                return;
            }
            Color control = UIManager.getColor("control");
            int width  = getWidth();
            int height = getHeight();

            Graphics2D g2 = (Graphics2D) g;
            Paint storedPaint = g2.getPaint();
            g2.setPaint(
                new GradientPaint(0, 0, getBackground(), width, 0, control));
            g2.fillRect(0, 0, width, height);
            g2.setPaint(storedPaint);
        }
    }

}