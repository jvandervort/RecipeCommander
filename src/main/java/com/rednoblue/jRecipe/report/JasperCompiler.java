package com.rednoblue.jrecipe.report;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import com.google.inject.Inject;
import com.rednoblue.jrecipe.model.Book;
import com.rednoblue.jrecipe.model.EDisplayType;
import com.rednoblue.jrecipe.model.Recipe;
import com.rednoblue.jrecipe.xml.XmlTransformer;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRXmlDataSource;
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
	private final XmlTransformer xmlTransformer;
	
	@Inject
	public JasperCompiler(Logger logger, XmlTransformer xmlTransformer) {
		this.logger = logger;
		this.xmlTransformer = xmlTransformer;
		compiler_thread = new JasperCompilerThread();
		compiler_thread.start();
	}

	public JasperReport getMainReport() {
		return main_report;
	}

	public JasperReport getIngredientReport() {
		return ingred_report;
	}

	public JasperPrint getJasperPrint(Book book, Recipe r) {
		JasperPrint jasperPrint = new JasperPrint();
		try {
			JRDataSource ds = null;
			if (r == null) {
				ds = new JRXmlDataSource(xmlTransformer.transformToXmlDocument(book, EDisplayType.JASPER), "//Recipe");
			} else {
				ds = new JRXmlDataSource(xmlTransformer.transformToXmlDocument(book, r, EDisplayType.JASPER), "//Recipe");
			}
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("IngredientSubreport", getIngredientReport());
			jasperPrint = JasperFillManager.fillReport(getMainReport(), parameters, ds);
		} catch (JRException e) {
			logger.severe(e.toString());
			e.printStackTrace();
		}
		return jasperPrint;
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
