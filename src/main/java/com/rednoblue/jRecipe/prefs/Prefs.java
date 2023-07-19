/*
 * Global.java
 *
 * Created on August 22, 2006, 12:40 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rednoblue.jrecipe.prefs;

import java.util.prefs.Preferences;

import com.google.inject.Inject;

/**
 *
 * @author John
 */
public class Prefs {
	private final String appName = "RecipeRaptor";
	private final String prefString = "/com/rednoblue/" + appName;
	private final String prefLocX = "loc_x";
	private final String prefLocY = "loc_y";
	private final String prefSizeW = "size_w";
	private final String prefSizeH = "size_h";
	private final String prefLastFile = "last_file";

	private String fileName = "";
	private String lastFileName = "";
	private int locX = 300;
	private int locY = 300;
	private int sizeW = 300;
	private int sizeH = 300;

	public Prefs() {
	}
	
	
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getLastFileName() {
		return lastFileName;
	}

	public void setLastFileName(String lastFileName) {
		this.lastFileName = lastFileName;
	}

	public int getLocX() {
		return locX;
	}

	public void setLocX(int locX) {
		this.locX = locX;
	}

	public int getLocY() {
		return locY;
	}

	public void setLocY(int locY) {
		this.locY = locY;
	}

	public int getSizeW() {
		return sizeW;
	}

	public void setSizeW(int sizeW) {
		this.sizeW = sizeW;
	}

	public int getSizeH() {
		return sizeH;
	}

	public void setSizeH(int sizeH) {
		this.sizeH = sizeH;
	}

	public String getAppname() {
		return appName;
	}


	public void loadAll() {
		Preferences prefs = Preferences.userRoot().node(prefString);

		locX = prefs.getInt(prefLocX, locX);
		locY = prefs.getInt(prefLocY, locY);
		sizeW = prefs.getInt(prefSizeW, sizeW);
		sizeH = prefs.getInt(prefSizeH, sizeH);
		lastFileName = prefs.get(prefLastFile, "");
	}

	public void saveAll() {
		Preferences prefs = Preferences.userRoot().node(prefString);

		prefs.putInt(prefLocX, locX);
		prefs.putInt(prefLocY, locY);
		prefs.putInt(prefSizeW, sizeW);
		prefs.putInt(prefSizeH, sizeH);
		prefs.put(prefLastFile, lastFileName);
	}
}
