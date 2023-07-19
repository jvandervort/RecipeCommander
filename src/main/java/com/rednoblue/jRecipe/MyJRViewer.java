
package com.rednoblue.jrecipe;

import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.swing.JRViewer;

public class MyJRViewer extends JRViewer {

	public MyJRViewer(JasperPrint jasperPrint) {
		super(jasperPrint);
	}
}
