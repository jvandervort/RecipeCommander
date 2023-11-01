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

import com.google.inject.Inject;

public class UserPrefs {
	public final String appName = "RecipeCommander";
	private final String prefString = "/com/rednoblue/" + appName;
	private final String prefLocX = "loc_x";
	private final String prefLocY = "loc_y";
	private final String prefSizeW = "size_w";
	private final String prefSizeH = "size_h";
	private final String prefLastFile = "last_file";

	public String fileName = "";
	public String lastFileName = "";
	public int LocX = 300;
	public int LocY = 300;
	public int SizeW = 300;
	public int SizeH = 300;

	@Inject
	private UserPrefs() {
	}

	public void loadAll() {
		Preferences prefs = Preferences.userRoot().node(prefString);

		LocX = prefs.getInt(prefLocX, LocX);
		LocY = prefs.getInt(prefLocY, LocY);
		SizeW = prefs.getInt(prefSizeW, SizeW);
		SizeH = prefs.getInt(prefSizeH, SizeH);
		lastFileName = prefs.get(prefLastFile, "");
	}

	public void saveAll() {
		Preferences prefs = Preferences.userRoot().node(prefString);

		prefs.putInt(prefLocX, LocX);
		prefs.putInt(prefLocY, LocY);
		prefs.putInt(prefSizeW, SizeW);
		prefs.putInt(prefSizeH, SizeH);
		prefs.put(prefLastFile, lastFileName);
	}
}
