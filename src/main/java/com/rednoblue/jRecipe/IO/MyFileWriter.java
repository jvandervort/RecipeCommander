package com.rednoblue.jrecipe.io;

import java.io.File;

import javax.swing.JFileChooser;

import com.google.inject.Inject;
import com.rednoblue.jrecipe.AppFrame;
import com.rednoblue.jrecipe.io.output.IRecipeWriter;
import com.rednoblue.jrecipe.io.output.WriterMealMaster;
import com.rednoblue.jrecipe.io.output.WriterPdf;
import com.rednoblue.jrecipe.io.output.WriterXmlFile;
import com.rednoblue.jrecipe.model.Book;
import com.rednoblue.jrecipe.model.Recipe;

/**
 * Writes the book out to a file. Handles the creation of the various
 * FileWriters.
 */
public class MyFileWriter {

	private IRecipeWriter fileWriter;

	private WriterXmlFile writerXmlFile;
	private WriterPdf writerPdf;
	private WriterMealMaster writerMealMaster;

	@Inject
	public MyFileWriter(WriterXmlFile writerXmlFile, WriterPdf writerPdf, WriterMealMaster writerMealMaster) {
		this.writerXmlFile = writerXmlFile;
		this.writerPdf = writerPdf;
		this.writerMealMaster = writerMealMaster;
	}
	
	/**
	 * @param overwrite existing file, or error.
	 * @return success or fail
	 */
	public boolean saveFile(Book book, Recipe rec, String argFileName, boolean overwrite) {
		String fileName = argFileName;
		String fileEx = FileUtil.getExtension(fileName);
				
		if (fileWriter == null) {
			if (fileEx.equalsIgnoreCase("xml")) {
				fileWriter = writerXmlFile;
			} else if (fileEx.equalsIgnoreCase("mmf")) {
				fileWriter = writerMealMaster;
			} else if (fileEx.equalsIgnoreCase("pdf")) {
				fileWriter = writerPdf;
			} else {
				// unsupported file format, maybe detect later
				System.err.println("unsupported file extension(" + fileEx + ")");
				return false;
			}
		}

		try {

			// If we don't have the proper file extension, add it!
			if (!FileUtil.getExtension(fileName).equalsIgnoreCase(fileWriter.getFileExtension())) {
				fileName = fileName + "." + fileWriter.getFileExtension();
			}

			// see if bak file exists, if so, delete it.
			String bakFileName = fileName.replace("." + FileUtil.getExtension(fileName), "") + ".bak";
			File bakFile = new File(bakFileName);
			bakFile.delete();

			// if the format is output only, don't create bak file
			if (fileWriter.isReadable() == true) {
				// rename the old file to => .bak
				File oldFile = new File(fileName);
				if (oldFile.renameTo(new File(bakFileName))) {
					System.err.println("Rename Successful");
				}
			}
			fileWriter.setBook(book);
			if (rec != null) {
				fileWriter.setRec(rec);
			}
			fileWriter.setFileName(fileName);
			fileWriter.write();
		} catch (Exception e) {
			System.err.println(this.getClass().getName() + ".saveFile: " + e.getMessage());
			e.printStackTrace();
		}

		return true;
	}

	public String browseFileSystem(AppFrame app, String selectedFormat) {
		final JFileChooser fc = new JFileChooser(app.getLastFileName());
		
		MyFileFilter xmlFilter = writerXmlFile.getChoosableFileFilter();
		MyFileFilter pdfFilter = writerPdf.getChoosableFileFilter();
		MyFileFilter mealMasterFilter = writerMealMaster.getChoosableFileFilter();
		
		fc.addChoosableFileFilter(xmlFilter);
		fc.addChoosableFileFilter(pdfFilter);
		fc.addChoosableFileFilter(mealMasterFilter);
		
		if (selectedFormat.equals("jRecipe")) {
			fc.setFileFilter(xmlFilter);	
		} else if (selectedFormat.equals("PDF")) {
			fc.setFileFilter(pdfFilter);
		} else if (selectedFormat.equals("")) {
			fc.setFileFilter(mealMasterFilter);
		} 

		// In response to a button click:
		int returnVal = fc.showOpenDialog(app);

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File f = fc.getSelectedFile();
			return f.getAbsolutePath();
		} else {
			return null;
		}

	}

	/**
	 *
	 * @return fileWriter instance
	 */
	public IRecipeWriter getLastWriter() {
		return fileWriter;
	}
}
