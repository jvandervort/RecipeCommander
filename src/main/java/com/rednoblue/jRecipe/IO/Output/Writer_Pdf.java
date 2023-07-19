package com.rednoblue.jrecipe.io.output;

import java.net.URL;

import com.rednoblue.jrecipe.io.MyFileFilter;
import com.rednoblue.jrecipe.model.Book;
import com.rednoblue.jrecipe.model.BookUtils;
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
public class Writer_Pdf implements O_Interface {
	// extension info
	static private final String formatName = "PDF";
	static private final String fileExtension = "pdf";
	static private final String fileDescription = formatName + " Files";
	static private final boolean readable = false;

	String fileName = null;
	Book book = null;
	Recipe rec = null;

	/** Creates a new instance of pdfWriter */
	public Writer_Pdf() {
	}

	public boolean isReadable() {
		return readable;
	}

	public void write() {

		try {
			URL url = getClass().getResource("/xml/Report1.jrxml");
			JasperDesign jasperDesign = JRXmlLoader.load(url.openStream());

			JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);

			JRDataSource ds = null;
			BookUtils bu = new BookUtils(book);
			if (rec == null) {
				ds = new JRXmlDataSource(XmlUtils.getBookXml(book, "jasper"), "/jRecipeBook/Recipe");
			} else {
				// src = new DOMSource(book.getXmlDocumentForOutput(rec));
				ds = new JRXmlDataSource(XmlUtils.getBookXml(book, rec, "jasper"), "/jRecipeBook/Recipe");
			}

			// JasperFillManager.fillReportToFile(jasperReport, "out.txt", null, ds);
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, ds);
			JasperExportManager.exportReportToPdfFile(jasperPrint, fileName);

		} catch (JRException e) {
			System.err.println("threw JRException:\n" + e);
		} catch (java.io.IOException e) {
			System.err.println("threw IOException:\n" + e);
		}

	}

	public void setFileName(String argFileName) {
		java.util.regex.Pattern p = java.util.regex.Pattern.compile("\\.pdf$");
		java.util.regex.Matcher m = p.matcher(argFileName);

		boolean b = m.find();
		if (b == true) {
			fileName = argFileName;
		} else {
			fileName = argFileName + ".pdf";
		}
	}

	public void setBook(Book argBook) {
		book = argBook;
	}

	public void setRec(Recipe argRec) {
		rec = argRec;
	}

	public String getFormatName() {
		return formatName;
	}

	public String getFileExtension() {
		return fileExtension;
	}

	public MyFileFilter getChoosableFileFilter() {
		return new MyFileFilter(fileExtension, fileDescription);
	}

	static class PdfWriterFactory extends O_Factory {
		public O_Interface create() {
			return (new Writer_Pdf());
		}
	}

	static {
		O_FormatCreator.oFactories.put("Writer_Pdf", new PdfWriterFactory());
	}

}
