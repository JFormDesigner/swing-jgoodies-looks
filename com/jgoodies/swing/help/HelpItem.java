package com.jgoodies.swing.help;

/*
 * Copyright (c) 2002 Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of Karsten Lentzsch.  
 * Use is subject to license terms.
 *
 */
import java.net.URL;

import javax.swing.Icon;

import com.jgoodies.swing.application.ResourceManager;

/**
 * Describes and models an item in the JGoodies tiny help.
 *
 * @author Karsten Lentzsch
 */

public final class HelpItem {
	
	private static final int    CHAPTER_ITEM			= 0;
	private static final int    TOPIC_ITEM			= 1;

	private static final String  CLOSED_BOOK_ICON_ID	= "com.jgoodies.help.closedBook.icon";
	private static final String  OPEN_BOOK_ICON_ID 	= "com.jgoodies.help.openBook.icon";
	private static final String  TOPIC_ICON_ID		= "com.jgoodies.help.topic.icon";

	private static final Icon    CLOSED_BOOK_ICON		= ResourceManager.getIcon(CLOSED_BOOK_ICON_ID);
	private static final Icon    OPEN_BOOK_ICON		= ResourceManager.getIcon(OPEN_BOOK_ICON_ID);
	private static final Icon    TOPIC_ICON			= ResourceManager.getIcon(TOPIC_ICON_ID);
	
	private final String name;
	private final int type;
	private final URL url;
	

	/**
	 * Constructs a help item for the given name, type, and URL.
	 */
	private HelpItem(String name, int type, URL url) {
		this.name = name;
		this.type = type;
		this.url  = url;
	}
	
	
	/**
	 * Creates and answers a chapter using the given name.
	 */
	static HelpItem createChapter(String name) { 
		return new HelpItem(name, CHAPTER_ITEM, null);
	}
	
	
	/**
	 * Creates and answers a topic for the given name and URL.
	 */
	static HelpItem createTopic(String name, URL url) { 
		return new HelpItem(name, TOPIC_ITEM, url);
	}
	
	
	/**
	 * Answers the item's URL.
	 */
	URL getURL() { return url; }
	
	
	/**
	 * Answers whether the item's URL matches the given URL.
	 */
	boolean matches(URL aUrl) { return aUrl.equals(getURL()); }
	
	
	/**
	 * Answers whether the item is a chapter.
	 */
	boolean isChapter() { return type == CHAPTER_ITEM; }
	
	
	/**
	 * Answers the icon for this item using the specified selection state.
	 */
	Icon	 getIcon(boolean sel)	{	
		if (isChapter()) {
			return sel ? OPEN_BOOK_ICON : CLOSED_BOOK_ICON;
		} else
			return TOPIC_ICON;
	}

	public String toString() 		{ return name; }
}