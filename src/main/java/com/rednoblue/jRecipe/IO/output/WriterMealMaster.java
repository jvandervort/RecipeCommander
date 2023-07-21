package com.rednoblue.jrecipe.io.output;

import com.google.inject.Inject;
import com.rednoblue.jrecipe.io.MyFileFilter;
import com.rednoblue.jrecipe.model.Book;
import com.rednoblue.jrecipe.model.Recipe;

public class WriterMealMaster implements IRecipeWriter {
	static private final String formatName = "Meal-Master";
	static private final String fileExtension = "mmf";
	static private final String fileDescription = formatName + " Files";
	static private final boolean readable = true;
	String type;

	@Inject
	public WriterMealMaster() {
	}

	public boolean isReadable() {
		return readable;
	}

	public boolean write(Book book, Recipe recipe, String fileName) {
		return false;
	}

	public String getFileExtension() {
		return fileExtension;
	}
	
	public MyFileFilter getChoosableFileFilter() {
		return new MyFileFilter(fileExtension, fileDescription);
	}
}