package com.rednoblue.jrecipe;

import java.io.IOException;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.rednoblue.jrecipe.logging.MyLogger;

public class RecipeRaptor {

	public static void main(String[] args) {
		try {
			MyLogger.setup();
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("Logging Setup Failure");
		}

		Injector injector = Guice.createInjector();
		AppFrame app = injector.getInstance(AppFrame.class);
		app.setTitle(Global.appName);
		app.loadGlobals();
		app.setIcon();
		app.repaint();
		app.setVisible(true);
	}

}
