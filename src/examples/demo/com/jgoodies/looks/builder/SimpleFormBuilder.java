/*
 * Copyright (c) 2003 JGoodies Karsten Lentzsch. All Rights Reserved.
 *
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 * 
 *  o Redistributions of source code must retain the above copyright notice, 
 *    this list of conditions and the following disclaimer. 
 *     
 *  o Redistributions in binary form must reproduce the above copyright notice, 
 *    this list of conditions and the following disclaimer in the documentation 
 *    and/or other materials provided with the distribution. 
 *     
 *  o Neither the name of JGoodies Karsten Lentzsch nor the names of 
 *    its contributors may be used to endorse or promote products derived 
 *    from this software without specific prior written permission. 
 *     
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, 
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR 
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR 
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, 
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, 
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; 
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, 
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE 
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, 
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. 
 */

package com.jgoodies.looks.builder;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

/**
 * Provides a means to build simple form-oriented panels quickly
 * using the {@link FormLayout}. This builder combines frequently used
 * panel building steps: add a new row, add a label, proceed to the next 
 * data column then add a component.
 * <p>
 * This builder has been extraced from the <code>DefaultFormBuilder</code>.
 *
 * @author	Karsten Lentzsch
 * @see	com.jgoodies.forms.builder.AbstractFormBuilder
 * @see	com.jgoodies.forms.factories.FormFactory
 * @see	com.jgoodies.forms.layout.FormLayout
 */
public final class SimpleFormBuilder extends PanelBuilder {

    /**
     * Holds the row specification that is reused to describe
     * the constant gaps between component lines.
     */
    private RowSpec lineGapSpec = FormFactory.LINE_GAP_ROWSPEC;

    /**
     * Holds the offset of the leading column - often 0 or 1.
     */
    private int leadingColumnOffset = 0;
    
    /**
     * Determines wether new data rows are being grouped or not. 
     */
    private boolean rowGroupingEnabled = true;
    

    // Instance Creation ****************************************************

    /**
     * Constructs an instance of <code>DefaultFormBuilder</code> for the given
     * layout.
     * 
     * @param layout	the <code>FormLayout</code> to be used
     */    
    public SimpleFormBuilder(FormLayout layout) {
        super(new JPanel(), layout);
    }
    
    
    // Settings Gap Sizes ***************************************************

    /**
     * Adds a component to the panel using the default constraints.
     * Proceeds to the next data column.
     * 
     * @param component	the component to add
     */
    private void append(Component component) {
        ensureCursorColumnInGrid();
        ensureHasGapRow(lineGapSpec);
        ensureHasComponentLine();
        
        setColumnSpan(1);
        add(component);
        setColumnSpan(1);
        nextColumn(1 + 1);
    }

    /**
     * Adds a text label to the panel and proceeds to the next column.
     * 
     * @param textWithMnemonic  the label's text - may mark a mnemonic
     * @return the added label
     */
    private JLabel append(String textWithMnemonic) {
        JLabel label = getComponentFactory().createLabel(textWithMnemonic);
        append(label);
        return label;
    }

    /**
     * Adds a text label and component to the panel. 
     * Then proceeds to the next data column.
     * 
     * @param textWithMnemonic  the label's text - may mark a mnemonic
     * @param component         the component to add
     * @return the added label
     */    
    public JLabel append(String textWithMnemonic, Component component) {
        JLabel label = append(textWithMnemonic);
        label.setLabelFor(component);
        append(component);
        return label;
    }

    /**
     * Returns the leading column. Unlike the superclass we take a 
     * column offset into account.
     * 
     * @return the leading column
     */
    protected int getLeadingColumn() {
        int column = super.getLeadingColumn();
        return column + leadingColumnOffset * getColumnIncrementSign();
    }
    
    
    // Adding Rows **********************************************************
    
    /**
     * Ensures that the cursor is in the grid. In case it's beyond the 
     * form's right hand side, the cursor is moved to the leading column
     * of the next line.
     */
    private void ensureCursorColumnInGrid() {
        if (getColumn() > getColumnCount()) {
            nextLine();
        }
    }
    
    /**
     * Ensures that we have a gap row before the next component row.
     * Checks if the current row is the given <code>RowSpec</code>
     * and appends this row spec if necessary.
     * 
     * @param gapRowSpec  the row specification to check for
     */
    private void ensureHasGapRow(RowSpec gapRowSpec) {
        if ((getRow() == 1) || (getRow() <= getRowCount()))
            return;
        
        if (getRow() <= getRowCount()) {
            RowSpec rowSpec = getCursorRowSpec();
            if ((rowSpec == gapRowSpec))
                return;
        }
        appendRow(gapRowSpec);
        nextLine();
    }
    
    /**
     * Ensures that the form has a component row. Adds a component row
     * if the cursor is beyond the form's bottom.
     */
    private void ensureHasComponentLine() {
        if (getRow() <= getRowCount()) return;
        appendRow(FormFactory.PREF_ROWSPEC);  
        if (rowGroupingEnabled) {
            getLayout().addGroupedRow(getRow());
        }      
    }
    
    /**
     * Looks up and answers the row specification of the current row.
     *  
     * @return the row specification of the current row
     */
    private RowSpec getCursorRowSpec() {
        return getLayout().getRowSpec(getRow());
    }


}
