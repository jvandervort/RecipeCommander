package com.rednoblue.jRecipe.IO;

import java.io.File;

/**
 * Utility for dealing with filenames.
 */
public class FileUtil {

	public static String getExtension(String namearg) {
		String ext = null;
		File f = new File(namearg);
		String s = f.getName();
		int i = s.lastIndexOf('.');

		if (i > 0 && i < s.length() - 1) {
			ext = s.substring(i + 1).toLowerCase();
		}
		return ext;
	}

}
