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

package com.jgoodies.looks.demo;

import java.awt.Component;
import java.awt.GridLayout;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.plaf.Options;

/** 
 * Demonstrates the jGoodies Looks <i>narrowMargin</i> option. 
 * Therefore it contains button rows that use different combinations
 * of layout managers and narrow hints. 
 * 
 * 
 * @author Karsten Lentzsch
 */
final class NarrowTab {

    /**
     * Builds the panel.
     */
    JComponent build() {
        FormLayout fl = new FormLayout(
                "7dlu, right:max(50dlu;pref), 4dlu, left:pref, 0:grow",
                "pref, 2dlu, pref, 4dlu, pref, 4dlu, pref, 11dlu, "
              + "pref, 2dlu, pref, 4dlu, pref, 4dlu, pref, 11dlu, "
              + "pref, 2dlu, pref, 4dlu, pref, 4dlu, pref, 0:grow");
        PanelBuilder builder = new PanelBuilder(fl);
        builder.setDefaultDialogBorder();

        CellConstraints cc = new CellConstraints();
        
        // BoxLayout
        builder.addSeparator("Unmodified Button Widths (BoxLayout)", cc.xywh(1, 1, 5, 1));

        builder.addLabel("No Narrow Hint:",         cc.xy(2, 3));
        builder.add(buildButtonBoxNoNarrow(),       cc.xy(4, 3));

        builder.addLabel("One Narrow Hint:",        cc.xy(2, 5));
        builder.add(buildButtonBoxOneNarrow(),      cc.xy(4, 5));

        builder.addLabel("All Narrow Hints:",       cc.xy(2, 7));
        builder.add(buildButtonBoxAllNarrow(),      cc.xy(4, 7));

        // DesignGridLayout
        builder.addSeparator("Adjusted Button Widths (FormLayout)", cc.xywh(1, 9, 5, 1));

        builder.addLabel("No Narrow Hint:",         cc.xy(2, 11));
        builder.add(buildButtonFormNoNarrow(),      cc.xy(4, 11));

        builder.addLabel("One Narrow Hint:",        cc.xy(2, 13));
        builder.add(buildButtonFormOneNarrow(),     cc.xy(4, 13));

        builder.addLabel("All Narrow Hints:",       cc.xy(2, 15));
        builder.add(buildButtonFormAllNarrow(),     cc.xy(4, 15));

        // GridLayout
        builder.addSeparator("Equalized Button Widths (GridLayout)", cc.xywh(1, 17, 5, 1));

        builder.addLabel("No Narrow Hint:",         cc.xy(2, 19));
        builder.add(buildButtonGridNoNarrow(),      cc.xy(4, 19));

        builder.addLabel("One Narrow Hint:",        cc.xy(2, 21));
        builder.add(buildButtonGridOneNarrow(),     cc.xy(4, 21));

        builder.addLabel("All Narrow Hints:",       cc.xy(2, 23));
        builder.add(buildButtonGridAllNarrow(),     cc.xy(4, 23));

        return builder.getPanel();
    }

    // Button FlowLayout Panels *********************************************

    private Component buildButtonBoxNoNarrow() {
        return buildButtonBox(createButtons());
    }

    private Component buildButtonBoxOneNarrow() {
        return buildButtonBox(createButtonsWithOneNarrowHint());
    }

    private Component buildButtonBoxAllNarrow() {
        return buildButtonBox(createNarrowHintedButtons());
    }

    private Component buildButtonBox(JButton[] buttons) {
        Box box = Box.createHorizontalBox();
        for (int i = 0; i < buttons.length; i++) {
            box.add(buttons[i]);
            box.add(Box.createHorizontalStrut(6));
        }
        return box;
    }

    // Button DesignGrids ***************************************************

    private Component buildButtonFormNoNarrow() {
        return buildButtonForm(createButtons());
    }

    private Component buildButtonFormOneNarrow() {
        return buildButtonForm(createButtonsWithOneNarrowHint());
    }

    private Component buildButtonFormAllNarrow() {
        return buildButtonForm(createNarrowHintedButtons());
    }

    private Component buildButtonForm(JButton[] buttons) {
        FormLayout fl = new FormLayout(
                "pref, 4dlu, pref, 4dlu, pref, 4dlu, pref, 4dlu, pref", 
                "pref");
        fl.setColumnGroups(new int[][]{{1, 3, 5, 7}});
        JPanel panel = new JPanel(fl);
        for (int i = 0; i < buttons.length; i++) {
            panel.add(buttons[i], new CellConstraints(i * 2 + 1, 1));
        }
        return panel;
    }

    // Button Grids *********************************************************

    private Component buildButtonGridNoNarrow() {
        return buildButtonGrid(createButtons());
    }

    private Component buildButtonGridOneNarrow() {
        return buildButtonGrid(createButtonsWithOneNarrowHint());
    }

    private Component buildButtonGridAllNarrow() {
        return buildButtonGrid(createNarrowHintedButtons());
    }

    private Component buildButtonGrid(JButton[] buttons) {
        JPanel grid = new JPanel(new GridLayout(1, 4, 6, 0));
        for (int i = 0; i < buttons.length; i++) {
            grid.add(buttons[i]);
        }
        return grid;
    }

    // Helper Code **********************************************************

    /**
     * Creates and returns a <code>JButton</code> with a narrow hint set.
     */
    private JButton createNarrowButton(String text) {
        JButton button = new JButton(text);
        button.putClientProperty(Options.IS_NARROW_KEY, Boolean.TRUE);
        return button;
    }

    /**
     * Creates and answers an array of buttons that have no narrow hints set.
     */
    private JButton[] createButtons() {
        return new JButton[] {
            new JButton("Add..."),
            new JButton("Remove"),
            new JButton("Up"),
            new JButton("Down"),
            new JButton("A Long Label")};
    }

    /**
     * Creates and answers an array of buttons, where only the last should be narrow.
     */
    private JButton[] createButtonsWithOneNarrowHint() {
        return new JButton[] {
            new JButton("Add..."),
            new JButton("Remove"),
            new JButton("Up"),
            new JButton("Down"),
            createNarrowButton("A Long Label")};
    }

    /**
     * Creates and answers an array of buttons that have no narrow hints set.
     */
    private JButton[] createNarrowHintedButtons() {
        return new JButton[] {
            createNarrowButton("Add..."),
            createNarrowButton("Remove"),
            createNarrowButton("Up"),
            createNarrowButton("Down"),
            createNarrowButton("A Long Label")};
    }


}