package com.jgoodies.swing.printing;

/*
 * Copyright (c) 2002 Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of Karsten Lentzsch.  
 * Use is subject to license terms.
 *
 */
 
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.print.Book;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URL;

import javax.swing.JEditorPane;
import javax.swing.JOptionPane;
import javax.swing.RepaintManager;
import javax.swing.table.TableModel;

import com.jgoodies.swing.ExtTable;
import com.jgoodies.swing.util.UIFactory;
import com.jgoodies.util.logging.Level;
import com.jgoodies.util.logging.Logger;

/**
 * Instances of PrintableDocument are printed by the PrintManager.
 * 
 * @see	PrintManager
 *
 * @author Karsten Lentzsch
 */

public final class PrintableDocument implements Printable {
	
	private static final int CONFIRM_PRINT_LIMIT =  10;
	private static final int MAX_PRINTABLE_PAGES = 500;
	private static final int HEADER_HEIGHT		=  25;
	private static final int FOOTER_HEIGHT		=  25;

	private static Logger logger = Logger.getLogger("PrintableDocument");

	private Component component;
	private boolean centerOutput;
	private String header;
	private String footer;
	private String jobName;
	private Book book;
	
	
	/**
	 * Constructs a printable document for the given component, centered or not.
	 */
	public PrintableDocument(Component component, boolean centerOutput) {
		this("", component, centerOutput);
	}
	
	
	/**
	 * Constructs a printable document for the given component, using the
	 * specified string header and centered property.
	 */
	public PrintableDocument(String header, Component component, boolean centerOutput) {
		this.component = component;
		this.centerOutput = centerOutput;
		setHeader(header);
	}
	
	
	/** 
	 *  Check and answer whether we should print pageCount pages.
	 *  If this is an incredible number, don't print.
	 *  If this is a big bunch of paper, ask the user.
	 */
	private static boolean checkPageLimit(int pageCount) {
		if (pageCount <= CONFIRM_PRINT_LIMIT)
			return true;
		if (pageCount > MAX_PRINTABLE_PAGES)
			return false;
		int answer =
			JOptionPane.showConfirmDialog(
				null,
				"Do you really want to print " + pageCount + " pages?",
				null,
				JOptionPane.YES_NO_OPTION);
		return JOptionPane.YES_OPTION == answer;
	}
	

	/**
	 * Creates and answers a <code>Book</code> for the specified page format.
	 */	
	Book createBook(PageFormat pageFormat) {
		double imageableWidth = pageFormat.getImageableWidth();
		double imageableHeight = pageFormat.getImageableHeight();
		double componentHeight = imageableHeight - HEADER_HEIGHT - FOOTER_HEIGHT;

		// Layout the printable document if necessary and get it's size.
		doLayout((int) imageableWidth);
		Dimension componentSize = getSize();
		int numOfPages = (int) (componentSize.height / componentHeight) + 1;

		// Debug logging
		logger.info("Imageable X:      " + pageFormat.getImageableX());
		logger.info("Imageable Y:      " + pageFormat.getImageableY());
		logger.info("Imageable width:  " + imageableWidth);
		logger.info("Imageable height: " + imageableHeight + "; Header= " + HEADER_HEIGHT + "; component= " + componentHeight + "; footer=" + FOOTER_HEIGHT);
		logger.info("Component width:  " + componentSize.width);
		logger.info("Component height: " + componentSize.height);
		logger.info("Number of pages:  " + numOfPages);

		if (!checkPageLimit(numOfPages))
			return null;

		book = new Book();
		book.append(this, pageFormat, numOfPages);
		return book;
	}
	
	
	/**
	 * Creates and answers a <code>PrintableDocument</code> from the
	 * text that is located at the specified <code>URL</code>.
	 */
	public static PrintableDocument createFrom(URL url) {
		JEditorPane editorPane = UIFactory.createHTMLPane(false, false);
		try {
			editorPane.setPage(url);
		} catch (IOException e) {
			logger.log(Level.WARNING, "Can't print URL " + url, e);
		}
		String file = url.getFile();
		int index = file.lastIndexOf('/');
		String filename = -1 == index ? file : file.substring(index + 1);
		PrintableDocument doc = new PrintableDocument(filename, editorPane, false);
		doc.setJobName(filename);
		return doc;
	}
	
	
	/**
	 * Creates and answers a <code>PrintableDocument</code> from 
	 * the given table model and alignments array.
	 */
	public static PrintableDocument createFrom(TableModel model, int[] alignments) {
		StringWriter writer = new StringWriter();

		HTMLTableWriter htmlTableWriter = new HTMLTableWriter();
		htmlTableWriter.setColumnAlignments(alignments);
		htmlTableWriter.writeTable(writer, model);

		JEditorPane htmlPane = new JEditorPane();
		htmlPane.setEditable(false);
		htmlPane.setContentType("text/html");
		htmlPane.setText(writer.toString());

		return new PrintableDocument(null, htmlPane, false);
	}
	
	
	/**
	 * Creates and answers a <code>PrintableDocument</code> from 
	 * the given extended table - that implicitly includes alignment data.
	 */
	public static PrintableDocument createFrom(ExtTable table) {
		return createFrom(table.getModel(), table.getColumnAlignments());
	}
	
	
	/** 
	 *  Layout the printable document if necessary.
	 */
	private void doLayout(int imageableWidth) {
		// Get the document's component and it's size.
		Dimension componentSize = getSize();

		// If the component has no size set, set the page's size.
		if (0 == componentSize.width || 0 == componentSize.height) {
			Component aComponent = getComponent();
			//component.setMinimumSize(new Dimension(usedSize.width, 0));
			//component.setMaximumSize(new Dimension(usedSize.width, Integer.MAX_VALUE));
			aComponent.setSize(imageableWidth, Integer.MAX_VALUE);
			if (aComponent instanceof JEditorPane) {
				Component view = (JEditorPane) aComponent;
				Dimension preferredSize = view.getPreferredSize();
				aComponent.setSize(imageableWidth, preferredSize.height);
			}
			componentSize = aComponent.getSize();
		}
	}
	
	
	public boolean getCenterOutput()	{ return centerOutput; }
	public Component getComponent()	{ return component;	}
	public String getJobName()			{ return jobName;		}
	private Dimension getSize()		{ return getComponent().getSize(); }
	
	
	/**
	 * Prints this printable document via the <code>PrintManager</code>.
	 */
	public void print() {
		PrintManager.print(this, false);
	}
	
	
	public int print(Graphics g, PageFormat pf, int pageIndex) throws PrinterException {
		int imageableX = (int) pf.getImageableX();
		int imageableY = (int) pf.getImageableY();
		int imageableWidth = (int) pf.getImageableWidth();
		int imageableHeight = (int) pf.getImageableHeight();
		int componentHeight = (int) imageableHeight - HEADER_HEIGHT - FOOTER_HEIGHT;

		int pageCount = book.getNumberOfPages();
		Component aComponent = getComponent();

		// Render header and footer with an 8pt 'dialog' font.
		Font font = new Font("dialog", Font.PLAIN, 8);
		g.setFont(font);
		FontMetrics fm = g.getFontMetrics();

		// Header
		if (header != null)
			g.drawString(header, imageableX, imageableY + fm.getAscent());

		// Pages
		String pagesString = "Page " + (pageIndex + 1) + " of " + pageCount;
		int pagesStringWidth = fm.stringWidth(pagesString) + 2;
		g.drawString(
			pagesString,
			imageableX + imageableWidth - pagesStringWidth,
			imageableY + fm.getAscent());

		// Footer
		if (footer != null)
			g.drawString(footer, imageableX, imageableY + imageableHeight);

		// Set a clipping region so the component doesn't go outside the page.
		g.setClip(imageableX, imageableY + HEADER_HEIGHT, imageableWidth, componentHeight);

		// Translate to the printed region.
		if (pageCount == 1 && getCenterOutput()) {
			// Center the output on the page.
			g.translate(
				imageableX + (imageableWidth - aComponent.getSize().width) / 2,
				imageableY + HEADER_HEIGHT + (componentHeight - aComponent.getSize().height) / 2);
		} else
			g.translate(imageableX, imageableY + HEADER_HEIGHT - pageIndex * componentHeight);

		// Store the double buffering state, switch it off for printing, then restore it.
		RepaintManager manager = RepaintManager.currentManager(aComponent);
		boolean isDoubleBuffered = manager.isDoubleBufferingEnabled();
		try {
			manager.setDoubleBufferingEnabled(false);
			aComponent.print(g);
		} finally {
			manager.setDoubleBufferingEnabled(isDoubleBuffered);
		}

		return Printable.PAGE_EXISTS;
	}
	
	
	/**
	 * Prints this printable document via the <code>PrintManager</code>.
	 * Opens a print dialog before, to allow the user to select a printer.
	 */
	public void printWithDialog() {
		PrintManager.print(this, true);
	}
	
	
	/**
	 * Sets a string header that is repeated on each page.
	 */
	public void setHeader(String header)	{	this.header = header; }
	
	
	/**
	 * Sets a string footer that is repeated on each page.
	 */
	public void setFooter(String footer)	{	this.footer = footer; }
	
		
	/**
	 * Sets a job name, that may be used by the OS or printer engine.
	 */
	public void setJobName(String jobName) {	this.jobName = jobName; }
}