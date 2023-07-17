package com.rednoblue.jRecipe.IO.Output;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
// logging
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

// XML Imports
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSOutput;
import org.w3c.dom.ls.LSSerializer;

import com.rednoblue.jRecipe.IO.MyFileFilter;
import com.rednoblue.jRecipe.model.Book;
import com.rednoblue.jRecipe.model.BookUtils;
import com.rednoblue.jRecipe.model.Recipe;
import com.rednoblue.jRecipe.model.XmlUtils;

/**
 * Provides native xml output.
 */
public class Writer_XmlFile implements O_Interface {
	private final static Logger LOGGER = Logger.getLogger(Writer_XmlFile.class.getName());

	static private final String formatName = "jRecipe";
	static private final String fileExtension = "xml";
	static private final String fileDescription = formatName + " Files";
	static private final boolean readable = true;
	String type;
	Book book = null;
	Recipe rec = null;
	String fileName = null;

	public Writer_XmlFile() {
	}

	public Writer_XmlFile(String t) {
		this();
		type = t;
	}

	public Writer_XmlFile(Book argBook, String argFileName) {
		book = argBook;
		fileName = argFileName;
		write();
	}

	public boolean isReadable() {
		return readable;
	}

	public String getFormatName() {
		return formatName;
	}

	public String getFileExtension() {
		return fileExtension;
	}

	public void setBook(Book argBook) {
		book = argBook;
	}

	public void setRec(Recipe argRec) {
		rec = argRec;
	}

	public void setFileName(String argFileName) {
		fileName = argFileName;
	}

	public void write() {
		Document doc;
		BookUtils bu = new BookUtils(book);
		if (rec == null) {
			doc = XmlUtils.getBookXml(book, "datamodel");
		} else {
			doc = XmlUtils.getBookXml(book, rec, "datamodel");
		}

		//LOGGER.info(XmlUtils.getXmlString(doc));

		try {
			FileWriter writer = new FileWriter(new File(fileName));
			BufferedWriter buffer = new BufferedWriter(writer);
			buffer.write(XmlUtils.getXmlString(doc));
			buffer.close();
		} catch (FileNotFoundException ex) {
			LOGGER.severe(ex.getMessage());
		} catch (ClassCastException ex) {
			LOGGER.severe(ex.getMessage());
		} catch (IOException e) {
			LOGGER.severe(e.getMessage());
			e.printStackTrace();
		}
	}

	public MyFileFilter getChoosableFileFilter() {
		return new MyFileFilter(fileExtension, fileDescription);
	}

	static class XmlFileWriterFactory extends O_Factory {

		public O_Interface create() {
			return (new Writer_XmlFile());
		}
	}

	static {
		O_FormatCreator.oFactories.put("Writer_XmlFile", new XmlFileWriterFactory());
	}
}
