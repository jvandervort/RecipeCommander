
package com.rednoblue.jRecipe;

import net.sf.jasperreports.view.JRViewer;
import net.sf.jasperreports.engine.JasperPrint;


public class MyJRViewer extends JRViewer {

  public MyJRViewer() {
    super((JasperPrint) null);
  }
  
  public void setJasperPrint(JasperPrint jrPrint) {
    loadReport(jrPrint);
    refreshPage();
  }

}
