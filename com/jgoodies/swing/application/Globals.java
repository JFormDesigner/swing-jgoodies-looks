package com.jgoodies.swing.application;

/*
 * Copyright (c) 2002 Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of Karsten Lentzsch.  
 * Use is subject to license terms.
 *
 */


/**
 * A container for globally accessable application values that
 * describe a product or project and the required resource paths. 
 *
 * @author Karsten Lentzsch
 */

public final class Globals {

	private static final String ACTIONS_BUNDLE_NAME    = "Action"; 
	private static final String RESOURCES_BUNDLE_NAME	 = "Resource";

	private final String productName;
	private final String preferencesNodeName;
	private final String version;
	private final String fullVersion;
	private final String copyright;
	private final String vendor;
	private final String description;
	private final String toolPath;
	private final String helpSetPath;
	private final String tipIndexPath;
	private final String vendorURL;
	private final String vendorMail;
	

	/**
	 * Constructs a <code>Globals</code> without tip index path.
	 * 
	 * @deprecated Replace by the constructor that sets the tip index path.
	 */
	public Globals(String productName, String preferencesNodeName, String version,
		String fullVersion, String description, String copyright, String vendor, 
		String toolPath, String helpSetPath, String vendorURL, String vendorMail) {
		this(productName, preferencesNodeName, version, fullVersion, description,
			copyright, vendor, toolPath, helpSetPath, "", 
			vendorURL, vendorMail);
	}
	
	
	/**
	 * Constructs a <code>Globals</code> with tip index path.
	 */
	public Globals(String productName, String preferencesNodeName, String version,
		String fullVersion, String description, String copyright, String vendor, 
		String toolPath, String helpSetPath, String tipIndexPath, 
		String vendorURL, String vendorMail) {
		this.productName			= productName;
		this.preferencesNodeName	= preferencesNodeName;
		this.version				= version;
		this.fullVersion			= fullVersion;
		this.copyright				= copyright;
		this.vendor				= vendor;
		this.description			= description;
		this.toolPath				= toolPath;
		this.helpSetPath			= helpSetPath;
		this.tipIndexPath			= tipIndexPath;
		this.vendorURL				= vendorURL;
		this.vendorMail			= vendorMail;
	}
	

	public String getProductName()			{ return productName;							}	
	public String getProductText() 		{ return getProductName() + ' ' + getVersion();}
	public String getDescription() 		{ return description;	}
	public String getWindowTitle()			{ return getProductName();						}

	public String getVersion()				{ return version; 								}
	public String getFullVersion() 		{ return fullVersion; 							}
	public String getVersionText()			{ return getProductName() + ' ' + 
													  getVersion() + "  " + 
													  getShortCopyright();					}

	public String getCopyright() 			{ return copyright; 							}
	public String getShortCopyright()		{ return getCopyright() + ' ' + getVendor(); 	}
	public String getFullCopyright() 		{ return getShortCopyright() + ". All rights reserved."; }

	public String getVendor()				{ return vendor; 								}
	public String getVendorMail()			{ return vendorMail; 							}
	public String getVendorURL()			{ return vendorURL; 							}
	
	public String getPreferencesNode() 	{ return preferencesNodeName; 					}
	public String getToolPath()			{ return toolPath; 							}
	public String getActionsBundlePath() 	{ return getToolPath() + ACTIONS_BUNDLE_NAME;	}
	public String getResourcesBundlePath()	{ return getToolPath() + RESOURCES_BUNDLE_NAME;}

	public String getHelpSetPath() 		{ return helpSetPath; 							}
	public String getTipIndexPath()		{ return tipIndexPath;					 		}

	
}