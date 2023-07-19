/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rednoblue.jrecipe.logging;

import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

//import java.util.logging.SimpleFormatter;
public class MyLogger {

	// static private FileHandler fileTxt;
	// static private SimpleFormatter formatterTxt;
	static private FileHandler fileLog;
	static private Formatter formatter;

	static public void setup() throws IOException {
		// Create Logger
		Logger logger = Logger.getLogger("");

		// reset the default console handler
		Handler[] handlers = logger.getHandlers(); // returns 1
		for (int i = 0; i < handlers.length; i++) {
			if (handlers[i] instanceof ConsoleHandler) {
				((ConsoleHandler) handlers[i]).setFormatter(new MyFormatter());
			}
		}

		// set global logging level
		logger.setLevel(Level.INFO);

		fileLog = new FileHandler("jRecipe.log");
		formatter = new MyFormatter();
		fileLog.setFormatter(formatter);
		logger.addHandler(fileLog);
	}
}
