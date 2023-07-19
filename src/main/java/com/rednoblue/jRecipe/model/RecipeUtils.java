package com.rednoblue.jrecipe.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.logging.Logger;

public class RecipeUtils {

	private final static Logger LOGGER = Logger.getLogger(RecipeUtils.class.getName());

	/** clean up process and comment strings when saving */
	public static String trimMultiLine(String arg) {
		StringBuffer ret = new StringBuffer("");
		try {
			BufferedReader r = null;
			r = new BufferedReader(new StringReader(arg.replaceAll("\n{3,}", "\n\n")));
			String line;
			while ((line = r.readLine()) != null) {
				ret.append(line.trim() + "\n");
			}
		} catch (IOException e) {
			LOGGER.severe(e.toString());
			e.printStackTrace();
		}
		return ret.toString();
	}

	/** encode with html for jasper rendering */
	public static String htmlEncode(String arg) {

		// replace triple newlines with two.
		arg = arg.replaceAll("\n{3,}", "\n\n");
		// add breaks for double newlines
		arg = arg.replaceAll("\n\n", "<br/>\n");

		StringBuffer ret = new StringBuffer("");
		try {
			BufferedReader r = null;
			r = new BufferedReader(new StringReader(arg));
			String line;

			//Pattern li = Pattern.compile("^\\*.*");
			// Pattern br = Pattern.compile("<br> *$");

			while ((line = r.readLine()) != null) {
				//Matcher m = li.matcher(line);
				// if ( m.matches() ) {
				// ret.append("<li>" + line.trim() + "</li>\n");
				// } else {
				ret.append(line.trim() + "\n");
				// }
			}
		} catch (IOException e) {
			LOGGER.severe(e.toString());
		}

		return ret.toString();
	}

}
