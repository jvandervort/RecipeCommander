package com.rednoblue.jrecipe.io.output;

import com.rednoblue.jrecipe.io.MyFileFilter;
import com.rednoblue.jrecipe.model.Book;
import com.rednoblue.jrecipe.model.Recipe;

public interface O_Interface {
	abstract public boolean isReadable();

	abstract public String getFormatName();

	abstract public String getFileExtension();

	abstract public void setBook(Book b);

	abstract public void setRec(Recipe r);

	abstract public void setFileName(String s);

	abstract public MyFileFilter getChoosableFileFilter();

	abstract public void write();
}
