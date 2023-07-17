package com.rednoblue.jRecipe.IO.Output;

import com.rednoblue.jRecipe.IO.MyFileFilter;
import com.rednoblue.jRecipe.model.Book;
import com.rednoblue.jRecipe.model.Recipe;

public class Writer_MealMaster implements O_Interface {
	static private final String formatName = "Meal-Master";
	static private final String fileExtension = "mmf";
	static private final String fileDescription = formatName + " Files";
	static private final boolean readable = true;
	String type;

	Book book = null;
	Recipe rec = null;
	String fileName = null;

	// MealMaster Format Writer

	public Writer_MealMaster() {
	}

	public Writer_MealMaster(String t) {
		type = t;
	}

	public Writer_MealMaster(Book argBook, String argFileName) {
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

	// xml Format Writer (Default)
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
		// not implemented yet
	}

	public MyFileFilter getChoosableFileFilter() {
		return new MyFileFilter(fileExtension, fileDescription);
	}

	static class MealMasterWriterFactory extends O_Factory {
		public O_Interface create() {
			return (new Writer_MealMaster());
		}
	}

	static {
		O_FormatCreator.oFactories.put("Writer_MealMaster", new MealMasterWriterFactory());
	}

}