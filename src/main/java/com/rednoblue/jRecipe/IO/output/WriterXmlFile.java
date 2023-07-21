package com.rednoblue.jrecipe.io.output;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Logger;

import com.google.inject.Inject;
import com.rednoblue.jrecipe.io.MyFileFilter;
import com.rednoblue.jrecipe.model.Book;
import com.rednoblue.jrecipe.model.EDisplayType;
import com.rednoblue.jrecipe.model.Recipe;
import com.rednoblue.jrecipe.model.XmlUtils;

/**
 * Provides native xml output.
 */
public class WriterXmlFile implements IRecipeWriter {
	private final static Logger LOGGER = Logger.getLogger(WriterXmlFile.class.getName());

	static private final String formatName = "jRecipe";
	static private final String fileExtension = "xml";
	static private final String fileDescription = formatName + " Files";
	static private final boolean readable = true;

	private XmlUtils xmlUtils;

	@Inject
	public WriterXmlFile(XmlUtils xmlUtils) {
		this.xmlUtils = xmlUtils;
	}

	public boolean isReadable() {
		return readable;
	}

	public boolean write(Book book, Recipe recipe, String fileName) {
		try (FileWriter writer = new FileWriter(new File(fileName));
				BufferedWriter buffer = new BufferedWriter(writer);) {
			if (recipe == null) {
				buffer.write(xmlUtils.transformToXmlString(book, EDisplayType.NORMAL));
			} else {
				buffer.write(xmlUtils.transformToXmlString(book, recipe, EDisplayType.NORMAL));
			}
			buffer.close();
			return true;
		} catch (FileNotFoundException ex) {
			LOGGER.severe(ex.getMessage());
		} catch (ClassCastException ex) {
			LOGGER.severe(ex.getMessage());
		} catch (IOException e) {
			LOGGER.severe(e.getMessage());
			e.printStackTrace();
		}
		return false;
	}

	public MyFileFilter getChoosableFileFilter() {
		return new MyFileFilter(fileExtension, fileDescription);
	}
	
	public String getFileExtension() {
		return fileExtension;
	}
	
}
