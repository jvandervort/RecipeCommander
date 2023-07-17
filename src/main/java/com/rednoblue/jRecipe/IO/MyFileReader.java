package com.rednoblue.jRecipe.IO;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.swing.JFileChooser;

import com.rednoblue.jRecipe.AppFrame;
import com.rednoblue.jRecipe.Global;
import com.rednoblue.jRecipe.IO.Input.I_FormatCreator;
import com.rednoblue.jRecipe.IO.Input.I_Interface;
import com.rednoblue.jRecipe.IO.Input.InputFormatNotCreatedException;
import com.rednoblue.jRecipe.model.Book;

/**
 * Handles the loading of a recipe book
 */
public class MyFileReader extends I_FormatCreator {

	private String fileName = "";
	private Book book;
	private String[] iFormats = { "Reader_MealMaster", "Reader_MasterCook", "Reader_XmlFile" };

	public MyFileReader() {
		createInputFormats();
	}

	public MyFileReader(String argFileName) throws FileNotFoundException, IOException {
		createInputFormats();
		fileName = argFileName;
		loadFile();
	}

	private void createInputFormats() {
		for (int i = 0; i < iFormats.length; i++) {
			try {
				I_Interface reader = createFormat(iFormats[i]);
			} catch (InputFormatNotCreatedException e) {
				Logger.getLogger(MyFileReader.class.getName()).log(Level.SEVERE, null, iFormats[i] + " " + e);
			} catch (ClassCastException e) {
				Logger.getLogger(MyFileReader.class.getName()).log(Level.SEVERE, null, iFormats[i] + " " + e);
			}
		}
	}

	private I_Interface getCorrectReader(String tenlines) {
		for (int i = 0; i < iFormats.length; i++) {
			try {
				I_Interface reader = createFormat(iFormats[i]);
				if (reader.isFileMine(tenlines)) {
					return reader;
				}
			} catch (InputFormatNotCreatedException e) {
				Logger.getLogger(MyFileReader.class.getName()).log(Level.SEVERE, null, iFormats[i] + " " + e);
			} catch (ClassCastException e) {
				Logger.getLogger(MyFileReader.class.getName()).log(Level.SEVERE, null, iFormats[i] + " " + e);
			}
		}
		return null;
	}

	public BufferedReader openBufferedReader() throws FileNotFoundException, IOException {
		BufferedReader br = null;
		if (fileName.startsWith("http://") == true || fileName.startsWith("file:/") == true
				|| fileName.equals("/test/TestXmlBook.xml")) {
			// normal files and internal .jar file
			java.io.Reader r = null;
			URL u = null;
			if (fileName.equals("/test/TestXmlBook.xml")) {
				u = getClass().getResource("/test/TestXmlBook.xml");
				Global.lastFileName = fileName;
			} else {
				u = new URL(fileName.replaceAll(" ", "%20"));
				Global.lastFileName = new String(u.toString().replaceAll("%20", " "));
			}
			r = new InputStreamReader(u.openStream());
			br = new BufferedReader(r);

		} else if (FileUtil.getExtension(fileName).equalsIgnoreCase("zip")) {
			// zip file
			ZipInputStream in = null;
			String inFilename = fileName;
			in = new ZipInputStream(new FileInputStream(inFilename));
			ZipEntry entry = in.getNextEntry();
			br = new BufferedReader((java.io.Reader) new InputStreamReader(in));
			Global.lastFileName = fileName;
		} else {
			// local file
			FileInputStream fis = new FileInputStream(fileName);
			InputStreamReader isr = new InputStreamReader(fis, "UTF8");
			br = new BufferedReader(isr);
			Global.lastFileName = fileName;
		}
		return br;
	}

	public void loadFile() throws FileNotFoundException, IOException {
		Logger.getLogger(MyFileReader.class.getName()).log(Level.INFO, fileName);
		BufferedReader br = null;
		br = openBufferedReader();
		StringBuffer b = new StringBuffer("");
		for (int i = 0; i < 10; i++) {
			b.append(br.readLine() + " ");
		}
		br.close();
		I_Interface reader = getCorrectReader(b.toString());
		if (reader != null) {
			java.io.Reader r = (java.io.Reader) openBufferedReader();
			reader.parseSource(r);
			book = reader.getBook();
		}

		if (reader == null) {
			// unsupported file format, maybe detect later
			Logger.getLogger(MyFileReader.class.getName()).log(Level.SEVERE, null, "unsupported file format");
		}
	}

	public boolean browseFileSystem(AppFrame app) {
		// Create a file chooser
		final JFileChooser fc = new JFileChooser(app.getLastFileName());
		// Set Filters
		Enumeration<String> keys = iFactories.keys();
		MyFileFilter jRecipeFilter = null;
		while (keys.hasMoreElements()) {
			String key = (String) keys.nextElement();
			try {
				I_Interface i = createFormat(key);
				MyFileFilter f = i.getChoosableFileFilter();
				fc.addChoosableFileFilter(f);
				if (i.getFormatName().equals("jRecipe")) {
					jRecipeFilter = f;
				}

			} catch (InputFormatNotCreatedException e) {
				Logger.getLogger(MyFileReader.class.getName()).log(Level.SEVERE, null, e);

			} catch (ClassCastException e) {
				Logger.getLogger(MyFileReader.class.getName()).log(Level.SEVERE, null, e);
			}
		}

		fc.setFileFilter(jRecipeFilter);

		// In response to a button click:
		int returnVal = fc.showOpenDialog(app);

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File f = fc.getSelectedFile();
			fileName = f.getAbsolutePath();
			return true;
		} else {
			return false;
		}

	}

	public void setFileName(String argFileName) {
		fileName = argFileName;
	}

	public Book getBook() {
		return book;
	}

	public String getFileName() {
		return fileName;
	}

	public static I_Interface getSpecificReader(String name) {

		try {
			I_Interface reader = createFormat(name);
			return reader;
		} catch (InputFormatNotCreatedException e) {
			Logger.getLogger(MyFileReader.class.getName()).log(Level.SEVERE, null, e);
			return null;
		} catch (ClassCastException e) {
			Logger.getLogger(MyFileReader.class.getName()).log(Level.SEVERE, null, e);
			return null;
		}
	}
}
