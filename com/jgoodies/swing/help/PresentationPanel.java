package com.jgoodies.swing.help;

/*
 * Copyright (c) 2002 Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of Karsten Lentzsch.  
 * Use is subject to license terms.
 *
 */

import java.awt.Cursor;
import java.io.IOException;
import java.net.URL;

import javax.swing.JEditorPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import com.jgoodies.swing.model.ValueModel;
import com.jgoodies.util.logging.Logger;

/**
 * Contains the editor pane for the help viewer.<p>
 * 
 * TODO: use an editor pane instead of subclassing it.
 *
 * @author Karsten Lentzsch
 */

final class PresentationPanel extends JEditorPane implements ChangeListener, HyperlinkListener {


	PresentationPanel() {
		setEditable(false);
		setContentType("text/html");

		/*// Don't load asynchronously.
		Document document = getDocument();
		if (document instanceof AbstractDocument) {
			((AbstractDocument) document).setAsynchronousLoadPriority(-1);
			System.out.println("Asynchrones Laden abgeschaltet.");
		}
		*/ //

		addHyperlinkListener(this);
	}
	
	
	public void hyperlinkUpdate(HyperlinkEvent e) {
		HyperlinkEvent.EventType eventType = e.getEventType();
		if (eventType == HyperlinkEvent.EventType.ENTERED)
			setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		else if (eventType == HyperlinkEvent.EventType.EXITED)
			setCursor(Cursor.getDefaultCursor());
	}
	
	
	public void stateChanged(ChangeEvent event) {
		ValueModel model = (ValueModel) event.getSource();
		show((URL) model.getValue());
	}


	private void show(URL helpURL) {
		try {
			setPage(helpURL);
		} catch (IOException e) {
			if (helpURL != null)
				Logger.getLogger("help.PresentationPanel").warning(
					"Can't show help URL: " + helpURL);
		}
	}

}