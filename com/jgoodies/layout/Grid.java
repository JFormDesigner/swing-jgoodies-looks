package com.jgoodies.layout;

/*
 * Copyright (c) 2003 Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of Karsten Lentzsch.  
 * Use is subject to license terms.
 *
 */
import java.awt.Dimension;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import com.jgoodies.util.Utilities;

/**
 * Defines layout constants. Superceded by the JGoodies Forms framework.
 *
 * @author Karsten Lentzsch
 */
public final class Grid {
	
	// Override default constructor, prevents instantiation.
	private Grid() {}
	
/* 
 * 96 dpi
 *
 * Widget           | Win2k | Word2k|Eclipse|   Me  | 1.3.1 | 1.4.0 | 1.4.1  
 * -----------------+-------+-------+-------+-------+-------+-------+--------
 * Menu Font size   |   11 T|   11 T|   11 T|11/12 T|   12 A|   11 T|   11 A| 1)
 * -----------------+-------+-------+-------+-------+-------+-------+--------
 * Control font size|   11 T|   11 T|   11 T|11/12 T|   12 A|   11 T|   11 A| 1)
 * -----------------+-------+-------+-------+-------+-------+-------+--------
 * Dialog ctlr font |   11 A|   11 T|  N/A  |  N/A  |  N/A  |  N/A  |  N/A  | 2)
 * -----------------+-------+-------+-------+-------+-------+-------+--------
 * Button size      | 75x23 | 74x22 | 76x23 | 75x23 |       |       |       |
 * -----------------+-------+-------+-------+-------+-------+-------+--------
 * Textfield height |    20 |    20 |    20 |    20 |       |       |       |
 * -----------------+-------+-------+-------+-------+-------+-------+--------
 * ComboBox height  |    21 | 21/22 |    21 |       |       |       |       |
 * -----------------+-------+-------+-------+-------+-------+-------+--------
 * Tree row height  |    17 |       |    16 |    17 |       |       |       |
 * -----------------+-------+-------+-------+-------+-------+-------+--------
 * Table row height |    17 |       |    17 |    17 |       |       |       |
 * -----------------+-------+-------+-------+-------+-------+-------+--------
 * Menubar height   |    22 |       |    16 |       |       |       |       |
 * -----------------+-------+-------+-------+-------+-------+-------+--------
 * Toolbar height   |    22 |       |    20 |       |       |       |       |
 * -----------------+-------+-------+-------+-------+-------+-------+--------
 * Menu bar button  |    19 |    19 |    15 |       |       |       |       |
 * -----------------+-------+-------+-------+-------+-------+-------+--------
 * 
 * 
 * 
 * 120 dpi
 *                                                 
 * Widget           | Win2k | Word2k|Eclipse|   Me  | 1.3.1 | 1.4.0 | 1.4.1  
 * -----------------+-------+-------+-------+-------+-------+-------+--------
 * Menu Font size   |   14 T|   14 T|   14 T|12/14 T|   12 A|   14 T|   14 T| 1)
 * -----------------+-------+-------+-------+-------+-------+-------+--------
 * Control font size|   14 T|   14 T|   14 T|12/14 T|   12 A|   14 T|   14 A| 1)
 * -----------------+-------+-------+-------+-------+-------+-------+--------
 * Dialog ctlr font |   14 A|   12 T|  N/A  |  N/A  |  N/A  |  N/A  |  N/A  | 2)
 * -----------------+-------+-------+-------+-------+-------+-------+--------
 * Button size      |100x28 | 87x27 | 92x30 | 95x27 |   x27 |   x27 |   x27 |
 * -----------------+-------+-------+-------+-------+-------+-------+--------
 * Textfield height |    24 |    24 |    24 |    24 |    21 |    22 |    24 |
 * -----------------+-------+-------+-------+-------+-------+-------+--------
 * ComboBox height  |    24 |    27 |    25 |       |       |       |       |
 * -----------------+-------+-------+-------+-------+-------+-------+--------
 * Tree row height  |    20 |       |    20 |    20 |       |       |       |
 * -----------------+-------+-------+-------+-------+-------+-------+--------
 * Table row height |    18 |       |    18 |    18 |       |       |       |
 * -----------------+-------+-------+-------+-------+-------+-------+--------
 * Menubar height   |    23 |    25 |    20 |       |       |       |       |
 * -----------------+-------+-------+-------+-------+-------+-------+--------
 * Toolbar height   |    23 |    26 |    22 |       |       |       |       |
 * -----------------+-------+-------+-------+-------+-------+-------+--------
 * Menu bar button  |    23 |    23 |    19 |       |       |       |       |
 * -----------------+-------+-------+-------+-------+-------+-------+--------
 * 
 * 1) Font sizes when using system font settings as enabled by
 *    "swing.useSystemFontSettings" SystemProperty and
 *    "Application.useSystemFontSettings" UIDefault.
 *    A is typically an Arial font; T is often a Tahoma font.
 * 
 * 2) Some applications use a different control font in dialogs forms, only.
 * 
 * 
 * 
 * Size                 | Win2000 96dpi | Win2000 120dpi
 * ---------------------+---------------+---------------
 * Notifier button size |    95x23      |    100x28
 * ---------------------+---------------+---------------
 * Notifier border size | 10, 6, 7, 6   | 12, 12, 12, 12  (top, left, bottom, right)
 * 
 * 
 * Eclipse Checkbox V gap   : 8 pixel
 * Eclipse Radiobutton V gap: 9 pixel
 * 
 * 
 */
 	// Default sizes ********************************************************************
 	
 	public static final Dimension LORES_DIALOG_BUTTON_SIZE	= new Dimension(75, 0);
 	public static final Dimension HIRES_DIALOG_BUTTON_SIZE	= new Dimension(90, 0);
 	
 	public static final Dimension LORES_NOTIFIER_BUTTON_SIZE	= new Dimension(85, 0);
 	public static final Dimension HIRES_NOTIFIER_BUTTON_SIZE	= new Dimension(90, 0);

 
 	public static Dimension defaultDialogButtonSize() {
 		return Utilities.IS_LOW_RES 
 			? LORES_DIALOG_BUTTON_SIZE 
 			: HIRES_DIALOG_BUTTON_SIZE;
 	}
	
 	public static Dimension defaultNotifierButtonSize() {
 		return Utilities.IS_LOW_RES 
 			? LORES_NOTIFIER_BUTTON_SIZE 
 			: HIRES_NOTIFIER_BUTTON_SIZE;
 	}
 	
 	
 	public static Dimension defaultPhantomSize() {
 		JButton button = new JButton("Phantom");
 		return new Dimension(
 			defaultDialogButtonSize().width,
 			button.getPreferredSize().height + 2);
 	}
 	
 	
	// Horizontal and vertical paddings -----------------------------------------
	
	public static final int HPAD1			=  6;  // 3dluX
	public static final int VPAD1			=  6;
	
	public static final int HPAD2			= 10;  // 5dluX
	public static final int VPAD2			= 10;
	
	public static final int HPAD3			= 12;  // 6dluX
	public static final int VPAD3			= 12;
	
	public static final int HPAD4			= 20;  // 10dluX
	public static final int VPAD4			= 20;
	
	public static final int HPAD5			= 24;  // 12dluX
	public static final int VPAD5			= 24;
	
	public static final int HPAD6			= 36;  // 18dluX
	public static final int VPAD6			= 36;
	
	public static final int GROUP_HINDENT	= HPAD2;
	public static final int GROUP_VINDENT	= VPAD1;
	public static final int GROUP_VSEPARATION = VPAD1 + VPAD2;
	public static final int LINE_VSEPARATION	= 2;
	public static final int ROW_SEPARATION	= 4;

	// Borders ------------------------------------------------------------------

	public static final Border EMPTY_BORDER		= new EmptyBorder(0,0,0,0);
	public static final Border VPAD1_BORDER		= new EmptyBorder(VPAD1,0,0,0);
	public static final Border HPAD1_BORDER		= new EmptyBorder(0,HPAD1,0,0);
	public static final Border CARD_DIALOG_BORDER = new EmptyBorder(VPAD2, HPAD1, 
																	  VPAD1 + 1, HPAD1);
	public static final Border DIALOG_BORDER		= new EmptyBorder(VPAD2 + 2, HPAD2,
																	  VPAD2, HPAD2);
	public static final Border NOTIFIER_BORDER	= new EmptyBorder(VPAD3, HPAD3, 
																	  VPAD3, HPAD3);

	// Insets ------------------------------------------------------------------

	public static final Insets EMPTY_INSETS		= new Insets(0,0,0,0);
	public static final Insets VPAD1_INSETS		= new Insets(VPAD1,0,0,0);
	public static final Insets VLINE_INSETS		= new Insets(2,0,0,0);
}
