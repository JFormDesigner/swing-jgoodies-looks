/*
 * Copyright (c) 2002-2004 JGoodies Karsten Lentzsch. All Rights Reserved.
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

package com.jgoodies.plaf.plastic;

import java.awt.Insets;

import javax.swing.UIDefaults;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.InsetsUIResource;

import com.jgoodies.plaf.LookUtils;
import com.jgoodies.plaf.Options;


/**
 * Intializes class and component defaults for the 
 * JGoodies PlasticXP look&amp;feel.
 *
 * @author Karsten Lentzsch
 */
public class PlasticXPLookAndFeel extends Plastic3DLookAndFeel {
	
    /**
     * Constructs the JGoodies PlasticXP look&amp;feel.
     */
    public PlasticXPLookAndFeel() {
        // Provide an empty constructor for subclassing.
    }

    public String getID() {
        return "JGoodies Plastic XP";
    }
    
    public String getName() {
        return "JGoodies Plastic XP";
    }
    
    public String getDescription() {
        return "The JGoodies Plastic XP Look and Feel"
            + " - \u00a9 2002-2004 JGoodies Karsten Lentzsch";
    }
    
    /**
     * Initializes the PlasticXP class defaults.
     * Overrides the check box and radio button UIs.
     * 
     * @param table   the UIDefaults table to work with
     */
    protected void initClassDefaults(UIDefaults table) {
        super.initClassDefaults(table);

        String UI_CLASSNAME_PREFIX   = "com.jgoodies.plaf.plastic.PlasticXP";
        Object[] uiDefaults = {
            "CheckBoxUI",    UI_CLASSNAME_PREFIX + "CheckBoxUI",
            "RadioButtonUI", UI_CLASSNAME_PREFIX + "RadioButtonUI",
        };
        table.putDefaults(uiDefaults);
    }
    
    
	/**
	 * Initializes the PlasticXP component defaults.
     * 
     * @param table   the UIDefaults table to work with
	 */	
	protected void initComponentDefaults(UIDefaults table) {
		super.initComponentDefaults(table);

        Object buttonBorder         = PlasticXPBorders.getButtonBorder();
        Object checkBoxIcon         = PlasticXPIconFactory.getCheckBoxIcon();
        Object comboBoxButtonBorder = PlasticXPBorders.getComboBoxArrowButtonBorder();
        Object comboBoxEditorBorder = PlasticXPBorders.getComboBoxEditorBorder();
        Object radioButtonIcon      = PlasticXPIconFactory.getRadioButtonIcon();
        Object scrollPaneBorder     = PlasticXPBorders.getScrollPaneBorder();
        Object textFieldBorder      = PlasticXPBorders.getTextFieldBorder();
        Object toggleButtonBorder   = PlasticXPBorders.getToggleButtonBorder();

        Object defaultButtonMargin  = createButtonMargin(false);
        Object narrowButtonMargin   = createButtonMargin(true);

        String radioCheckIconName   = LookUtils.IS_LOW_RESOLUTION
                                            ? "icons/RadioLight5x5.png"
                                            : "icons/RadioLight7x7.png";
                                            
        // Windows uses 2,2,2,2, but we try to adjust baselines of text and label.
        Object textInsets 		    = new InsetsUIResource(2, 3, 2, 2);
                                            
		Object[] defaults = {
            "Button.border",                  buttonBorder,
            "Button.margin",                  defaultButtonMargin,
            "Button.narrowMargin",            narrowButtonMargin,
            "Button.borderPaintsFocus",       Boolean.TRUE,
            
            "CheckBox.icon",                  checkBoxIcon,
            "CheckBox.check",                 getToggleButtonCheckColor(),
            
            "ComboBox.arrowButtonBorder",     comboBoxButtonBorder,
            "ComboBox.editorBorder",          comboBoxEditorBorder,
            "ComboBox.borderPaintsFocus",     Boolean.TRUE,

            "EditorPane.margin",              textInsets,

            "FormattedTextField.border",      textFieldBorder,
            "FormattedTextField.margin",      textInsets,             
            "PasswordField.border",           textFieldBorder,
            "PasswordField.margin",           textInsets,             
            "Spinner.border", 				  scrollPaneBorder,
            "Spinner.defaultEditorInsets",	  textInsets,
            
            "ScrollPane.border",              scrollPaneBorder,
            "Table.scrollPaneBorder", 		  scrollPaneBorder,
            
            "RadioButton.icon",               radioButtonIcon,
            "RadioButton.check",              getToggleButtonCheckColor(),
            "RadioButton.interiorBackground", getControlHighlight(),
            "RadioButton.checkIcon",          makeIcon(getClass(), radioCheckIconName),
            
            "TextArea.margin",				  textInsets,	
            "TextField.border",               textFieldBorder,
            "TextField.margin", 			  textInsets,
            
            "ToggleButton.border",            toggleButtonBorder,
            "ToggleButton.margin",            defaultButtonMargin,
            "ToggleButton.narrowMargin",      narrowButtonMargin,
            "ToggleButton.borderPaintsFocus", Boolean.TRUE,
		};
		table.putDefaults(defaults);
	}
    
    protected static void installDefaultThemes() {}   
    
    /**
     * Creates and answers the margin used by <code>JButton</code>
     * and <code>JToggleButton</code>. Honors the screen resolution
     * and the global <code>isNarrowButtonsEnabled</code> property.<p>
     *
     * Sun's L&F implementations use wide button margins.
     * 
     * @param narrow   true indicates a narrow margin, false a wide margin
     * @return an Insets object that describes the button margin
     * @see Options#getUseNarrowButtons()
     */
    private static Insets createButtonMargin(boolean narrow) {
        int pad = narrow || Options.getUseNarrowButtons() ? 4 : 14;
        return LookUtils.IS_LOW_RESOLUTION
            ? new InsetsUIResource(1, pad, 1, pad)
            : new InsetsUIResource(2, pad, 2, pad);
    }


    private ColorUIResource getToggleButtonCheckColor() {
        return getMyCurrentTheme().getToggleButtonCheckColor();
    }


}