package com.rednoblue.jrecipe.io.output;

import com.rednoblue.jrecipe.io.MyFileFilter;
import com.rednoblue.jrecipe.model.Book;
import com.rednoblue.jrecipe.model.Recipe;

public interface IRecipeWriter {
	abstract public boolean write(Book book, Recipe recipe, String fileName);
	abstract public MyFileFilter getChoosableFileFilter();
	abstract public boolean isReadable();
	abstract public String getFileExtension();
}
