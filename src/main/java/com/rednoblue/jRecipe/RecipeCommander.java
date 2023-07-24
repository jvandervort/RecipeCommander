package com.rednoblue.jrecipe;

import java.io.IOException;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.rednoblue.jrecipe.logging.MyLogger;
import com.rednoblue.jrecipe.ui.AppFrame;

public class RecipeCommander {

	public static void main(String[] args) {
		try {
			MyLogger.setup();
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("Logging Setup Failure");
		}

		Injector injector = Guice.createInjector();
		AppFrame app = injector.getInstance(AppFrame.class);
		app.setAppFrameTitle();
		app.loadGlobals();
		app.setIcon();
		app.repaint();
		app.setVisible(true);
	}

}
