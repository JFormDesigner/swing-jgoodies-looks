/*
 * Copyright (c) 2003 JGoodies Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of JGoodies Karsten Lentzsch.  
 * Use is subject to license terms.
 *
 */
 
package com.jgoodies.swing.util;

import java.awt.*;
import java.awt.event.MouseEvent;

import javax.swing.*;
import javax.swing.plaf.UIResource;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.text.Document;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;

import com.jgoodies.forms.factories.DefaultComponentFactory;
import com.jgoodies.layout.Grid;
import com.jgoodies.swing.BorderlessSplitPane;
import com.jgoodies.swing.ExtLabel;

/**
 * A factory class that consists only of static methods 
 * to create frequently used Swing components.
 *
 * @author Karsten Lentzsch
 */

public final class UIFactory {
	
	
	// Labels *****************************************************************
	
	/**
	 * Creates and answers a <code>JLabel</code> with a bold font 
	 * for the specified text.
	 */
	public static JLabel createBoldLabel(String text) {
		return createBoldLabel(text, 0);
	}
	
	
	/**
	 * Creates and answers a <code>JLabel</code> with a bold font for the 
	 * specified text and size increment, e.g. +3 is 3 pixel larger than the default.
	 */
	public static JLabel createBoldLabel(String text, int sizeIncrement) {
		return createBoldLabel(text, sizeIncrement, UIManager.getColor("controlText")); 
	}
	
	
	/**
	 * Creates and answers a <code>JLabel</code> with a bold font for the 
	 * specified text, size increment, and foreground color.
	 */
	public static JLabel createBoldLabel(String text, int sizeIncrement, Color foreground) {
		return createBoldLabel(text, sizeIncrement, foreground, false);
	}
	
	
	/**
	 * Creates and answers a <code>JLabel</code> with a bold font for the 
	 * specified text, size increment, foreground color, and anti-alias setting.
	 */
	public static JLabel createBoldLabel(String text, int sizeIncrement,
								          Color foreground, boolean antiAliased) {
		JLabel label = new ExtLabel(text, Font.BOLD, sizeIncrement, antiAliased);
		label.setForeground(foreground);
		return label;
	}
	
	
	/**
	 * Creates and answers a <code>JLabel</code> that uses a plain font.
	 * <p>
	 * Should be used carefully, since it override l&f settings.
	 */
	public static JLabel createPlainLabel(String text) {
		return createPlainLabel(text, UIManager.getColor("controlText"));
	}
	
	
	/**
	 * Creates and answers a <code>JLabel</code> that uses a plain font
	 * and the given foreground color.
	 * <p>
	 * Should be used carefully, since it override l&f settings.
	 */
	public static JLabel createPlainLabel(String text, Color foreground) {
		return createPlainLabel(text, foreground, false);
	}
	
	
	/**
	 * Creates and answers a <code>JLabel</code> that uses a plain font
	 * and the given foreground color.
	 * <p>
	 * Should be used carefully, since it override l&f settings.
	 */
	public static JLabel createPlainLabel(String text, Color foreground, boolean antiAliased) {
		JLabel label = new ExtLabel(text, Font.PLAIN, 0, antiAliased);
		if (foreground != null)
			label.setForeground(foreground);
		label.setVerticalAlignment(JLabel.BOTTOM);
		return label;
	}
	
	
	/**
	 * Creates and answers a label that uses the foreground color
	 * and font of a <code>TitledBorder</code>.
	 */
	public static JLabel createTitle(String text) {
        return DefaultComponentFactory.getInstance().createTitle(text);
	}
	
	
	// Other Text *************************************************************

	/**
	 * Creates and answers a <code>JTextField</code>, that is read-only.
	 */	
	public static JTextField createReadOnlyTextField() {
		JTextField field = new JTextField() {
			public boolean isFocusTraversable() { return false; }
			protected void processEvent(AWTEvent e) {}
		};
		field.setEditable(false);
		field.setOpaque(false);
		field.setForeground(Color.black);
		return field;
	}
	

	/**
	 * Creates and answers a <code>JTextArea</code> that can be used
	 * as a label that spans over multiple lines. 
	 * Requires hand-wrapped text.
	 */
	public static JTextArea createMultilineLabel(String text) {
		JTextArea area = new JTextArea(text) {
			public boolean isFocusTraversable() { return false; }
			protected void processEvent(AWTEvent e) {}
			public void updateUI() {
				super.updateUI();
				setOpaque(false);
				setMargin(Grid.EMPTY_INSETS);
				if (getForeground() instanceof UIResource)
					setForeground(UIManager.getColor("controlText"));
			}
		};
		area.setBorder(Grid.EMPTY_BORDER);
		return area;
	}
	

	/**
	 * Creates and answers a <code>JTextArea</code> that can be used
	 * as a label that spans over multiple lines. 
	 * Will wrap lines at words.
	 */
	public static JTextArea createWrappedMultilineLabel(String text) {
		JTextArea area = createMultilineLabel(text);
		area.setLineWrap(true);
		area.setWrapStyleWord(true);
		return area;
	}
	
	
	/**
	 * Creates and answers an editor pane that shall be used for
	 * displaying HTML pages. Uses the specified properties to set
	 * whether we load synchronously or asynchronously, and process events,
	 * or shield the pane from processing events.
	 */
    public static JEditorPane createHTMLPane(
        boolean asynchronousLoad,
        boolean processEvents) {
        JEditorPane htmlPane;
        if (processEvents) {
            htmlPane = new JEditorPane() {
                protected void processMouseMotionEvent(MouseEvent e) {
                }
            };
        } else {
            htmlPane = new JEditorPane() {
                public boolean isFocusTraversable() {
                    return false;
                }
                protected void processEvent(AWTEvent e) {
                }
            };
        }
        HTMLEditorKit htmlEditorKit =
            asynchronousLoad ? new HTMLEditorKit() : new HTMLEditorKit() {
            public Document createDefaultDocument() {
                HTMLDocument doc = (HTMLDocument) super.createDefaultDocument();
                doc.setAsynchronousLoadPriority(-1);
                return doc;
            }
        };
        htmlPane.setEditorKit(htmlEditorKit);
        htmlPane.setEditable(false);
        return htmlPane;
    }
	
	
	// Table ******************************************************************
	
	/**
	 * Creates and answers a component that wraps a <code>JTable</code>
	 * with a <code>JScrollPane</code>, sets the viewport's background
	 * and appropriate corners.
	 */
	public static JComponent createTablePanel(JTable table) {
		Color background = UIManager.getColor("window");

		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.getViewport().setOpaque(false);
		scrollPane.getViewport().setBackground(background);
		scrollPane.setOpaque(false);
		
		scrollPane.setCorner(JScrollPane.UPPER_RIGHT_CORNER, new JPanel());
		scrollPane.setCorner(JScrollPane.LOWER_RIGHT_CORNER, new JPanel());

		JPanel panel = new JPanel(new GridLayout(0, 1)) {
			public void updateUI() {
				super.updateUI();
				setBackground(UIManager.getColor("window"));
			}
		};
		panel.setPreferredSize(new Dimension(220, 100));
		panel.add(scrollPane);

		return panel;
	}
	
	
	/**
	 * Creates and answers a left aligned table header renderer.
	 */	
	public static TableCellRenderer createLeftAlignedDefaultHeaderRenderer() {
		DefaultTableCellRenderer label = new DefaultTableCellRenderer() {
			public Component getTableCellRendererComponent(
				JTable table,
				Object value,
				boolean isSelected, boolean hasFocus,
				int row, int column) {
				if (table != null) {
					JTableHeader header = table.getTableHeader();
					if (header != null) {
						this.setForeground(header.getForeground());
						this.setBackground(header.getBackground());
						this.setFont(header.getFont());
					}
				}
				setText((value == null) ? "" : value.toString());
				this.setBorder(UIManager.getBorder("TableHeader.cellBorder"));
				return this;
			}
		};
		label.setHorizontalAlignment(JLabel.LEFT);
		return label;
	}

	// Misc *******************************************************************
		
	/**
	 * Creates and answers a <code>JSplitPane</code> that has an empty border.
	 */
    public static JSplitPane createStrippedSplitPane(
        int orientation,
        Component comp1,
        Component comp2,
        double resizeWeight) {
        JSplitPane split = new BorderlessSplitPane(orientation, comp1, comp2);
        split.setResizeWeight(resizeWeight);
        split.setBorder(BorderFactory.createEmptyBorder());
        split.setOneTouchExpandable(false);
        return split;
    }
	
	
	/**
	 * Creates and answers a <code>JScrollPane</code> that has an empty border.
	 */
	public static JScrollPane createStrippedScrollPane(Component component) {
		JScrollPane scrollPane = new JScrollPane(component);
		scrollPane.setBorder(BorderFactory.createEmptyBorder());
		return scrollPane;
	}


    /**
     * Creates and answers a label plus separator, useful to separate
     * vertically stacked content in a panel; is often a better choice
     * than a <code>TitledBorder</code>.
     * 
     * @param title   a String for the separator's title
     * @return a separator with a leading label
     */
    public static JComponent createSeparator(String title) {
        return DefaultComponentFactory.getInstance().createSeparator(title);
    }

	/**
	 * Creates and answers a label plus separator, useful to separate
	 * vertically stacked content in a panel; is often a better choice
	 * than a <code>TitledBorder</code>.
     * 
     * @param title   a String for the separator's title
     * @return a separator with a leading label
     * @deprecated use #createSeparator or 
     * com.jgoodies.forms.AbstractPanelBuilder.addSeparator instead
	 */
	public static JPanel createHeader(String title) {
        return (JPanel) createSeparator(title);
	}
	
	
	/**
	 * Answers a light background <code>Color</code>.
	 * Computes and answers a <code>Color</code> that has a minimum
	 * and maximum brightness and is slightly darker than the 
	 * specified <code>Color</code>. 
	 */
	public static  Color getLightBackground() {
		Color color = UIManager.getColor("control");
		float[] hsbValues = new float[3];
		Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), hsbValues);
		float hue = hsbValues[0];
		//float saturation = hsbValues[1];
		float brightness = hsbValues[2]; 
		if (brightness > 0.95f)
			return Color.white;
		else 
			return Color.getHSBColor(hue, hue < 0.01f ? 0 : 0.03f, 0.97f);
	}

}