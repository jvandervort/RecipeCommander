package com.rednoblue.jrecipe;

import java.net.URL;
import java.util.logging.Logger;

import com.google.inject.Inject;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

/**
 * Compiles built-in jasper reports in a separate thread.
 */
public class JasperCompiler {
	private JasperCompilerThread compiler_thread;
	private JasperReport main_report;
	private JasperReport ingred_report;
	private final Logger logger;
	
	@Inject
	public JasperCompiler(Logger logger) {
		this.logger = logger;
		compiler_thread = new JasperCompilerThread();
		compiler_thread.start();
	}

	public JasperReport get_main_report() {
		return main_report;
	}

	public JasperReport get_ingred_report() {
		return ingred_report;
	}

	public boolean is_running() {
		if (compiler_thread != null && compiler_thread.isAlive()) {
			return true;
		} else {
			return false;
		}
	}

	public class JasperCompilerThread extends Thread {

		public JasperCompilerThread() {
			super(JasperCompilerThread.class.getName());
		}

		@Override
		public void run() {
			
			try {
				logger.info("jasper compiler starting");
				
				URL url = getClass().getResource("/Report1.jrxml");

				logger.fine("loading " + url.getPath());
				JasperDesign jasperDesign = JRXmlLoader.load(url.openStream());

				logger.fine("compiling " + url.getPath());
				main_report = JasperCompileManager.compileReport(jasperDesign);

				url = getClass().getResource("/Report1_ingredients.jrxml");

				logger.fine("loading " + url.getPath());
				JasperDesign ingredientDesign = JRXmlLoader.load(url.openStream());

				logger.fine("compiling " + url.getPath());
				ingred_report = JasperCompileManager.compileReport(ingredientDesign);

				logger.info("jasper compiler done");

			} catch (JRException e) {
				logger.severe(e.toString());
				e.printStackTrace();
			} catch (java.io.IOException e) {
				logger.severe(e.toString());
				e.printStackTrace();
			}
		}
	}
}
