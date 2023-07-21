package com.rednoblue.jrecipe.io.input;

import com.rednoblue.jrecipe.io.MyFileFilter;
import com.rednoblue.jrecipe.model.Book;

public interface IRecipeReader {
	abstract public boolean isFileMine(String tenlines);
	abstract public Book parseSource(java.io.Reader reader);
	abstract public MyFileFilter getChoosableFileFilter();
}
