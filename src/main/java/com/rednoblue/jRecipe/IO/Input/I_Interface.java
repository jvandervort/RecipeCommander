package com.rednoblue.jrecipe.io.input;

import com.rednoblue.jrecipe.io.MyFileFilter;
import com.rednoblue.jrecipe.model.Book;

public interface I_Interface {
	abstract public boolean isFileMine(String tenlines);

	abstract public String getFormatName();

	abstract public Book getBook();

	abstract public void parseSource(java.io.Reader reader);

	abstract public MyFileFilter getChoosableFileFilter();
}
