package com.jgoodies.plaf.plastic;

/*
 * Copyright (c) 2002 Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of Karsten Lentzsch.  
 * Use is subject to license terms.
 *
 */

import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.plaf.basic.BasicComboBoxEditor;


/**
 * The default editor for editable combo boxes in the JGoodies Plastic Look
 * and Feel.<p>
 * 
 * It differs from <code>MetalComboBoxEdit</code> in that the border
 * is quite the same as for text fields: a compound border, with an inner
 * <code>MarginBorder</code>.
  *
 * @author Karsten Lentzsch
 */
class PlasticComboBoxEditor extends BasicComboBoxEditor {


    public PlasticComboBoxEditor() {
        editor = new JTextField("", 9);
        editor.setBorder(UIManager.getBorder("ComboBox.editorBorder"));
    }
    
    /**
     * A subclass of BasicComboBoxEditor that implements UIResource.
     * BasicComboBoxEditor doesn't implement UIResource
     * directly so that applications can safely override the
     * cellRenderer property with BasicListCellRenderer subclasses.
     */
    public static final class UIResource extends PlasticComboBoxEditor
    	implements javax.swing.plaf.UIResource {
    }
}

