/*
 * Global.java
 *
 * Created on August 22, 2006, 12:40 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rednoblue.jrecipe;

import java.util.prefs.Preferences;

/**
 *
 * @author John
 */
public class Global {
	// properties for prefs
	public static final String appName = "jrecipe";
	private static final String prefString = "/com/rednoblue/" + appName;
	private static final String prefLocX = "loc_x";
	private static final String prefLocY = "loc_y";
	private static final String prefSizeW = "size_w";
	private static final String prefSizeH = "size_h";
	private static final String prefLastFile = "last_file";

	public static String fileName = "";
	public static String lastFileName = "";
	public static int LocX = 300;
	public static int LocY = 300;
	public static int SizeW = 300;
	public static int SizeH = 300;

	/** Creates a new instance of Global */
	private Global() {
	}

	public static void loadAll() {
		Preferences prefs = Preferences.userRoot().node(prefString);

		LocX = prefs.getInt(prefLocX, LocX);
		LocY = prefs.getInt(prefLocY, LocY);
		SizeW = prefs.getInt(prefSizeW, SizeW);
		SizeH = prefs.getInt(prefSizeH, SizeH);
		lastFileName = prefs.get(prefLastFile, "");
	}

	public static void saveAll() {
		Preferences prefs = Preferences.userRoot().node(prefString);

		prefs.putInt(prefLocX, LocX);
		prefs.putInt(prefLocY, LocY);
		prefs.putInt(prefSizeW, SizeW);
		prefs.putInt(prefSizeH, SizeH);
		prefs.put(prefLastFile, lastFileName);
	}
}
