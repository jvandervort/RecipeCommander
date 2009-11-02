package com.rednoblue.jRecipe;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.*;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import java.util.logging.*;
import java.net.URL;

/**
 * Compiles built-in jasper reports in a separate thread.
 * @author John Vandervort
 * @version 1.0
 */
public class JasperCompiler {

  private final static Logger LOGGER = Logger.getLogger(JasperCompiler.class.getName());
  private JasperCompilerThread compiler_thread;
  private JasperReport main_report;
  private JasperReport ingred_report;

  public JasperCompiler() {
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
    if ( compiler_thread != null && compiler_thread.isAlive()) {
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
      LOGGER.info("jasper compiler thread running");
      try {
        URL url = getClass().getResource("/xml/Report1.jrxml");

        LOGGER.fine("loading " + url.getPath());
        JasperDesign jasperDesign = JRXmlLoader.load(url.openStream());

        LOGGER.fine("compiling " + url.getPath());
        main_report = JasperCompileManager.compileReport(jasperDesign);

        url = getClass().getResource("/xml/Report1_ingredients.jrxml");

        LOGGER.fine("loading " + url.getPath());
        JasperDesign ingredientDesign = JRXmlLoader.load(url.openStream());

        LOGGER.fine("compiling " + url.getPath());
        ingred_report = JasperCompileManager.compileReport(ingredientDesign);

        LOGGER.fine("jasper compiler thread ");

      } catch (JRException e) {
        LOGGER.severe(e.toString());
      } catch (java.io.IOException e) {
        LOGGER.severe(e.toString());
      }
    }
  }
}
