package com.jgoodies.swing.printing;

/*
 * Copyright (c) 2002 Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of Karsten Lentzsch.  
 * Use is subject to license terms.
 *
 */
 
import java.awt.print.Book;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.util.StringTokenizer;

import com.jgoodies.swing.application.Workbench;
import com.jgoodies.util.logging.Level;
import com.jgoodies.util.logging.Logger;
import com.jgoodies.util.prefs.Preferences;

/**
 * The PrintManager prints instances of PrintableDocument.
 * It creates the necessary print job and handles problems.
 * The printing is delegated back to the PrintableDocument.
 * Also, it provides behavior for opening a page setup dialog. 
 *
 * @author Karsten Lentzsch
 */

public final class PrintManager {
	
	private static final String PAGE_FORMAT_KEY = "pageFormat";

	
	private static PageFormat	pageFormat;
	private static Logger		logger		= Logger.getLogger("PrintManager");
	
	
	/**
	 * Answers the current PageFormat.
	 */
	public static PageFormat getPageFormat() {
		if (null == pageFormat) {
			Preferences prefs = Workbench.userPreferences();
			pageFormat = decodePageFormatFrom(prefs.get(PAGE_FORMAT_KEY, null));
			if (pageFormat == null)
				pageFormat = defaultPageFormat();
		}
		return pageFormat;
	}
	
	
	private static PageFormat defaultPageFormat() {
		return PrinterJob.getPrinterJob().defaultPage();
	}
	
	
	/**
	 * Sets the current PageFormat.
	 */
	public static void setPageFormat(PageFormat pf) {
		pageFormat = pf;
		Workbench.userPreferences().put(PAGE_FORMAT_KEY, encodePageFormat(pf));
	}


	/**
	 * Opens the default page setup dialog using the current page format.
	 */
	public static void openPageSetupDialog() {
		pageFormat = PrinterJob.getPrinterJob().pageDialog(getPageFormat());
	}
	
	
	/** 
	 *  Print out the document. Delegate the creation of a book back to document.
	 */
	static void print(PrintableDocument document, boolean openPrintDialog) {
		PrinterJob printerJob = PrinterJob.getPrinterJob();

		pageFormat = printerJob.validatePage(getPageFormat());

		Book book = document.createBook(getPageFormat());
		// In case of an invalid number of pages, a null-book is returned.
		if (null == book)
			return;

		printerJob.setPageable(book);
		printerJob.setJobName(document.getJobName());

		// If the user cancelled the print dialog, do nothing.
		if (openPrintDialog && !(printerJob.printDialog()))
			return;

		try {
			printerJob.print();
		} catch (PrinterException e) {
			logger.log(Level.WARNING, "A printing problem occured.", e);
		} catch (Exception e) {
			logger.severe("A severe printing problem occured: " + e.getLocalizedMessage());
		}
	}
	

	/**
	 * Encodes a PageFormat as a String; 
	 * used for storing in Preferences.
	 */
	private static String encodePageFormat(PageFormat pf) {
		Paper paper = pf.getPaper();
		StringBuffer buffer = new StringBuffer();

		buffer.append("[" + pf.getOrientation());
		buffer.append("|" + paper.getWidth());
		buffer.append("|" + paper.getHeight());
		buffer.append("|" + paper.getImageableX());
		buffer.append("|" + paper.getImageableY());
		buffer.append("|" + paper.getImageableWidth());
		buffer.append("|" + paper.getImageableHeight());
		buffer.append(']');

		return buffer.toString();
	}
	
	
	/**
	 * Decodes a PageFormat from a String representation; 
	 * used for reading from Preferences.
	 */
	private static PageFormat decodePageFormatFrom(String spec) {
		if (null == spec)
			return null;

		StringTokenizer tokenizer = new StringTokenizer(spec, "[|]");
		PageFormat pf = defaultPageFormat();
		Paper paper = new Paper();

		try {
			int	orientation		= Integer.parseInt  (tokenizer.nextToken());
			double	width			= Double.parseDouble(tokenizer.nextToken());
			double	height			= Double.parseDouble(tokenizer.nextToken());
			double	imageableX		= Double.parseDouble(tokenizer.nextToken());
			double	imageableY		= Double.parseDouble(tokenizer.nextToken());
			double	imageableWidth	= Double.parseDouble(tokenizer.nextToken());
			double	imageableHeight	= Double.parseDouble(tokenizer.nextToken());

			pf.setOrientation(orientation);
			paper.setSize(width, height);
			paper.setImageableArea(imageableX, imageableY, imageableWidth, imageableHeight);
			pf.setPaper(paper);
			return pf;
		} catch (Exception e) {
			logger.log(Level.WARNING, "Can't decode page format.", e);
			return null;
		}
	}
	
}