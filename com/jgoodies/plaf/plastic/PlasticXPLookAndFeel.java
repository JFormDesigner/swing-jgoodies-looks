package com.jgoodies.plaf.plastic;

/*
 * Copyright (c) 2003 JGoodies Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of JGoodies Karsten Lentzsch.  
 * Use is subject to license terms.
 *
 */

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
    public PlasticXPLookAndFeel() {}

    public String getID() {
        return "JGoodies Plastic XP";
    }
    
    public String getName() {
        return "JGoodies Plastic XP";
    }
    
    public String getDescription() {
        return "The JGoodies Plastic XP Look and Feel"
            + " - \u00a9 2003 JGoodies Karsten Lentzsch";
    }
    
    /**
     * Initializes the PlasticXP class defaults.
     * Overrides the check box and radio button UIs.
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
	 */	
	protected void initComponentDefaults(UIDefaults table) {
		super.initComponentDefaults(table);

        Object buttonBorder         = PlasticXPBorders.getButtonBorder();
        Object checkBoxIcon         = PlasticXPIconFactory.getCheckBoxIcon();
        Object comboBoxButtonBorder = PlasticXPBorders.getComboBoxArrowButtonBorder();
        Object comboBoxEditorBorder = PlasticXPBorders.getComboBoxEditorBorder();
        Object radioButtonIcon      = PlasticXPIconFactory.getRadioButtonIcon();
        Object textFieldBorder      = PlasticXPBorders.getTextFieldBorder();
        Object toggleButtonBorder   = PlasticXPBorders.getToggleButtonBorder();

        Object defaultButtonMargin  = createButtonMargin(false);
        Object narrowButtonMargin   = createButtonMargin(true);

        String radioCheckIconName   = LookUtils.isLowRes
                                            ? "icons/RadioLight5x5.png"
                                            : "icons/RadioLight7x7.png";
        
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

            "FormattedTextField.border",      textFieldBorder,
            "PasswordField.border",           textFieldBorder,
            "TextField.border",               textFieldBorder,

            "RadioButton.icon",               radioButtonIcon,
            "RadioButton.check",              getToggleButtonCheckColor(),
            "RadioButton.interiorBackground", getControlHighlight(),
            "RadioButton.checkIcon",          makeIcon(getClass(), radioCheckIconName),
            
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
     * @see Options#getUseNarrowButtons()
     */
    private static Insets createButtonMargin(boolean narrow) {
        int pad = narrow || Options.getUseNarrowButtons() ? 4 : 14;
        return LookUtils.isLowRes
            ? (LookUtils.IS_BEFORE_14
                ? new InsetsUIResource(0, pad, 1, pad)
                : new InsetsUIResource(1, pad, 1, pad))
            : (LookUtils.IS_BEFORE_14
                ? new InsetsUIResource(1, pad, 1, pad)
                : new InsetsUIResource(2, pad, 2, pad));
    }


    private ColorUIResource getToggleButtonCheckColor() {
        return getMyCurrentTheme().getToggleButtonCheckColor();
    }


}