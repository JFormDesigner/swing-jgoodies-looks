package com.jgoodies.swing.printing;

/*
 * Copyright (c) 2003 JGoodies Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of JGoodies Karsten Lentzsch.  
 * Use is subject to license terms.
 *
 */

import java.io.IOException;
import java.io.Writer;

import javax.swing.JLabel;
import javax.swing.table.TableModel;

/**
 * Renders a <Swing>TableModel</code> as HTML. You can specify
 * the column alignments.
 *
 * @author Karsten Lentzsch
 */

public class HTMLTableWriter {

    private static final String FONT_DESCRIPTION = "<FONT size=-2>";

    private final String lineSeparator;

    private Writer      out;
    private TableModel  model;
    private int[]       columnAlignments = null;
    private boolean     trouble = false;

    /**
     * Constructs a <code>HTMLTableWriter</code>.
     */
    public HTMLTableWriter() {
        lineSeparator = System.getProperty("line.separator");
    }

    /**
     * Answers the aligment of the column with the given column index.
     */
    public int getColumnAlignment(int col) {
        return null == columnAlignments ? JLabel.LEFT : columnAlignments[col];
    }

    /**
     * Sets the aligment of the column with the given column index.
     */
    public void setColumnAlignments(int[] alignments) {
        this.columnAlignments = alignments;
    }

    /**
     * Flushes the stream and checks its error state. Errors are cumulative;
     * once the stream encounters an error, this routine will return true on
     * all successive calls.
     *
     * @return True if the print stream has encountered an error, either on the
     * underlying output stream or during a format conversion.
     */
    public boolean checkError() {
        if (out != null)
            flush();
        return trouble;
    }

    private void flush() {
        try {
            out.flush();
        } catch (IOException e) {
            trouble = true;
        }
    }

    private void newLine() {
        try {
            out.write(lineSeparator);
        } catch (IOException e) {
            trouble = true;
        }
    }

    protected void print(String str) {
        try {
            out.write(str);
        } catch (IOException e) {
            trouble = true;
        }
    }

    protected void println(String str) {
        print(str);
        newLine();
    }

    protected void writeFontEndTag() {
        print("</FONT>");
    }

    protected void writeFontStartTag() {
        print(FONT_DESCRIPTION);
    }

    /**
     * Writes the page to the specified <code>Writer</code> using
     * the given table model.
     */
    public void writePage(Writer newOut, TableModel newModel) {
        this.out = newOut;
        this.model = newModel;

        // Print the page start tag.
        println("<HTML>");

        // Print header.
        println("<HEAD><TITLE>title</TITLE></HEAD>");

        // Print body start tag.
        println("<BODY>");

        writeTable(newOut, newModel);

        // Print body end tag.
        println("</BODY>");

        // Print page end tag.
        println("</HTML>");

        flush();
    }

    /**
     * Writes the table to the specified <code>Writer</code> using
     * the given table model.
     */
    public void writeTable(Writer newOut, TableModel newModel) {
        this.out = newOut;
        this.model = newModel;

        // Print the title in bold.
        /*if (title != null) {
        	print("<H3>");
        	print(title);
        	println("</H3>");
        }
        */
        // Print the table start tag.
        println("<TABLE border=0 cellpadding=0 cellspacing=0>");

        int rowCount = newModel.getRowCount();

        writeTableHeader();

        for (int row = 0; row < rowCount; row++)
            writeTableRow(row);

        // Print table end tag.
        println("</TABLE>");

        flush();
    }

    /**
     * Writes a table cell.
     */
    public void writeTableCellHTML(Object value, int column) {
        print("<td valign=top align=");
        print(getColumnAlignment(column) == JLabel.LEFT ? "left" : "right");
        print(">");
        if (column > 3)
            print("&nbsp;");
        print("<font=\"arial,helvetica\" size=-2>");
        print(value.toString());
        writeFontEndTag();

        // Print table data end tag.
        println("</td>");
    }

    private void writeTableHeader() {
        // Print the row start tag.
        print("<TR>");

        int columnCount = model.getColumnCount();
        for (int column = 0; column < columnCount; column++)
            writeTableHeaderCellHTML(model.getColumnName(column));

        // Print row end tag.
        println("</TR>");
    }

    /**
     * Writes a table header cell.
     */
    public void writeTableHeaderCellHTML(Object value) {
        print("<th>");
        print("&nbsp;");
        writeFontStartTag();
        print(value.toString());
        writeFontEndTag();
        print("&nbsp;");

        // Print table data end tag.
        println("</th>");
    }

    private void writeTableRow(int row) {
        // Print the row start tag.
        print("<TR>");

        int columnCount = model.getColumnCount();
        for (int column = 0; column < columnCount; column++)
            writeTableCellHTML(model.getValueAt(row, column), column);

        // Print row end tag.
        println("</TR>");
    }

}