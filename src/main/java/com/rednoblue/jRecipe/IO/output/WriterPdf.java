package com.rednoblue.jrecipe.io.output;

import java.net.URL;

import com.google.inject.Inject;
import com.rednoblue.jrecipe.io.MyFileFilter;
import com.rednoblue.jrecipe.model.Book;
import com.rednoblue.jrecipe.model.EDisplayType;
import com.rednoblue.jrecipe.model.Recipe;
import com.rednoblue.jrecipe.model.XmlUtils;

// jasper imports
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRXmlDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

/**
 *
 * @author jcv9868
 */
public class WriterPdf implements IRecipeWriter {
	static private final String formatName = "PDF";
	static private final String fileExtension = "pdf";
	static private final String fileDescription = formatName + " Files";
	static private final boolean readable = false;

	private XmlUtils xmlUtils;

	@Inject
	public WriterPdf(XmlUtils xmlUtils) {
		this.xmlUtils = xmlUtils;
	}

	public boolean isReadable() {
		return readable;
	}

	public boolean write(Book book, Recipe recipe, String fileName) {

		try {
			URL url = getClass().getResource("/Report1.jrxml");
			JasperDesign jasperDesign = JRXmlLoader.load(url.openStream());

			JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);

			JRDataSource ds = null;
			if (recipe == null) {
				// whole book
				ds = new JRXmlDataSource(xmlUtils.transformToXmlDocument(book, EDisplayType.JASPER), "//Recipe");
			} else {
				// single recipe
				ds = new JRXmlDataSource(xmlUtils.transformToXmlDocument(book, recipe, EDisplayType.JASPER), "//Recipe");
			}

			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, ds);
			JasperExportManager.exportReportToPdfFile(jasperPrint, fileName);
			return true;

		} catch (JRException e) {
			System.err.println("threw JRException:\n" + e);
		} catch (java.io.IOException e) {
			System.err.println("threw IOException:\n" + e);
		}
		return false;
	}

	public String getFileExtension() {
		return fileExtension;
	}
	
	public MyFileFilter getChoosableFileFilter() {
		return new MyFileFilter(fileExtension, fileDescription);
	}
}
