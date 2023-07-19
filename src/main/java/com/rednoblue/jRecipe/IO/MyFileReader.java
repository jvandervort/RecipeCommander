package com.rednoblue.jrecipe.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipInputStream;

import javax.swing.JFileChooser;

import com.rednoblue.jrecipe.AppFrame;
import com.rednoblue.jrecipe.Global;
import com.rednoblue.jrecipe.io.input.I_Interface;
import com.rednoblue.jrecipe.io.input.Reader_MasterCook;
import com.rednoblue.jrecipe.io.input.Reader_MealMaster;
import com.rednoblue.jrecipe.io.input.Reader_XmlFile;
import com.rednoblue.jrecipe.model.Book;

/**
 * Handles the loading of a recipe book
 */
public class MyFileReader {
	private Reader_XmlFile readerXmlFile;
	private Reader_MasterCook readerMasterCook;
	private Reader_MealMaster readerMealMaster;
	
	private String fileName = "";
	private Book book;

	public MyFileReader() {
		readerXmlFile = new Reader_XmlFile();
		readerMasterCook = new Reader_MasterCook();
		readerMealMaster = new Reader_MealMaster();
	}

	public MyFileReader(String argFileName) throws FileNotFoundException, IOException {
		this();
		fileName = argFileName;
		loadFile();
	}

	private I_Interface getCorrectReader(String tenlines) {
		if (readerXmlFile.isFileMine(tenlines)) {
			return readerXmlFile;
		}
		if (readerMasterCook.isFileMine(tenlines)) {
			return readerMasterCook;
		}
		if (readerMealMaster.isFileMine(tenlines)) {
			return readerMealMaster;
		}
		return null;
	}

	@SuppressWarnings("resource")
	public BufferedReader getBufferedReader() throws FileNotFoundException, IOException {
		BufferedReader br = null;
		if (fileName.startsWith("http://") == true || fileName.startsWith("file:/") == true
				|| fileName.equals("/test/TestXmlBook.xml")) {
			// normal files and internal .jar file
			java.io.Reader r = null;
			URL u = null;
			u = new URL(fileName.replaceAll(" ", "%20"));
			Global.lastFileName = new String(u.toString().replaceAll("%20", " "));
			r = new InputStreamReader(u.openStream());
			br = new BufferedReader(r);

		} else if (FileUtil.getExtension(fileName).equalsIgnoreCase("zip")) {
			// zip file
			ZipInputStream in = null;
			String inFilename = fileName;
			in = new ZipInputStream(new FileInputStream(inFilename));
			in.getNextEntry();
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
		br = getBufferedReader();
		StringBuffer b = new StringBuffer("");
		for (int i = 0; i < 10; i++) {
			b.append(br.readLine() + " ");
		}
		br.close();
		I_Interface reader = getCorrectReader(b.toString());
		if (reader != null) {
			java.io.Reader r = (java.io.Reader) getBufferedReader();
			reader.parseSource(r);
			book = reader.getBook();
		}

		if (reader == null) {
			// unsupported file format, maybe detect later
			Logger.getLogger(MyFileReader.class.getName()).log(Level.SEVERE, null, "unsupported file format");
		}
	}

	public boolean browseFileSystem(AppFrame app) {
		final JFileChooser fc = new JFileChooser(app.getLastFileName());
		
		MyFileFilter jRecipeFilter = readerXmlFile.getChoosableFileFilter();
		fc.addChoosableFileFilter(jRecipeFilter);
		fc.addChoosableFileFilter(readerMasterCook.getChoosableFileFilter());
		fc.addChoosableFileFilter(readerMealMaster.getChoosableFileFilter());
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
}
