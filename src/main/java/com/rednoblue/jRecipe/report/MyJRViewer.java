
package com.rednoblue.jrecipe.report;

import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.swing.JRViewer;

public class MyJRViewer extends JRViewer {

	private static final long serialVersionUID = 1L;

	public MyJRViewer(JasperPrint jasperPrint) {
		super(jasperPrint);
	}
}
