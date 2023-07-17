package com.rednoblue.jRecipe.IO.Input;

import com.rednoblue.jRecipe.IO.MyFileFilter;
import com.rednoblue.jRecipe.model.Book;

public interface I_Interface {
	abstract public boolean isFileMine(String tenlines);

	abstract public String getFormatName();

	abstract public Book getBook();

	abstract public void parseSource(java.io.Reader reader);

	abstract public MyFileFilter getChoosableFileFilter();
}
