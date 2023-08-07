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

import com.google.inject.Inject;
import com.rednoblue.jrecipe.UserPrefs;
import com.rednoblue.jrecipe.io.input.IRecipeReader;
import com.rednoblue.jrecipe.io.input.ReaderMasterCook;
import com.rednoblue.jrecipe.io.input.ReaderMealMaster;
import com.rednoblue.jrecipe.io.input.ReaderXmlFile;
import com.rednoblue.jrecipe.model.Book;
import com.rednoblue.jrecipe.ui.AppFrame;

/**
 * Handles the loading of a recipe book
 */
public class MyFileReader {
	private ReaderXmlFile readerXmlFile;
	private ReaderMasterCook readerMasterCook;
	private ReaderMealMaster readerMealMaster;
	private final UserPrefs userPrefs;

	private Book book;

	@Inject
	public MyFileReader(ReaderXmlFile readerXmlFile, ReaderMasterCook readerMasterCook,
			ReaderMealMaster readerMealMaster, UserPrefs userPrefs) {
		this.userPrefs = userPrefs;
		this.readerXmlFile = readerXmlFile;
		this.readerMasterCook = readerMasterCook;
		this.readerMealMaster = readerMealMaster;
	}

	public void loadFile(String fileName) throws FileNotFoundException, IOException {
		String tenLines = getTenLines(fileName);
		IRecipeReader reader = getCorrectReader(tenLines);
		if (reader == null) {
			Logger.getLogger(MyFileReader.class.getName()).log(Level.SEVERE, null, "unsupported file format");
			return;
		}
		java.io.Reader r = (java.io.Reader) getBufferedReader(fileName);
		book = reader.parseSource(r);
	}

	public String browseFileSystem(AppFrame app) {
		
		String openPath;
		if (app.getLastFileName() != null) {
			openPath = app.getLastFileName();
		}
		else {
			openPath = System.getProperty("user.home");
		}
		
		final JFileChooser fc = new JFileChooser(openPath);

		MyFileFilter jRecipeFilter = readerXmlFile.getChoosableFileFilter();
		fc.addChoosableFileFilter(jRecipeFilter);
		fc.addChoosableFileFilter(readerMasterCook.getChoosableFileFilter());
		fc.addChoosableFileFilter(readerMealMaster.getChoosableFileFilter());
		fc.setFileFilter(jRecipeFilter);

		// In response to a button click:
		int returnVal = fc.showOpenDialog(app);

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File f = fc.getSelectedFile();
			return f.getAbsolutePath();
		} else {
			return null;
		}
	}

	public Book getBook() {
		return book;
	}

	private String getTenLines(String fileName) {
		try (BufferedReader br = getBufferedReader(fileName)) {
			StringBuffer b = new StringBuffer("");
			for (int i = 0; i < 10; i++) {
				b.append(br.readLine() + " ");
			}
			return b.toString();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private IRecipeReader getCorrectReader(String tenlines) {
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
	private BufferedReader getBufferedReader(String fileName) throws FileNotFoundException, IOException {
		BufferedReader br = null;
		if (fileName.startsWith("http://") == true || fileName.startsWith("file:/") == true
				|| fileName.equals("/test/TestXmlBook.xml")) {
			// normal files and internal .jar file
			java.io.Reader r = null;
			URL u = null;
			u = new URL(fileName.replaceAll(" ", "%20"));
			userPrefs.lastFileName = new String(u.toString().replaceAll("%20", " "));
			r = new InputStreamReader(u.openStream());
			br = new BufferedReader(r);

		} else if (FileUtil.getExtension(fileName).equalsIgnoreCase("zip")) {
			// zip file
			ZipInputStream in = null;
			String inFilename = fileName;
			in = new ZipInputStream(new FileInputStream(inFilename));
			in.getNextEntry();
			br = new BufferedReader((java.io.Reader) new InputStreamReader(in));
			userPrefs.lastFileName = fileName;
		} else {
			// local file
			FileInputStream fis = new FileInputStream(fileName);
			InputStreamReader isr = new InputStreamReader(fis, "UTF8");
			br = new BufferedReader(isr);
			userPrefs.lastFileName = fileName;
		}
		return br;
	}
}
