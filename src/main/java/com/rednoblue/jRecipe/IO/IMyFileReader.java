package com.rednoblue.jrecipe.io;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.rednoblue.jrecipe.AppFrame;

public interface IMyFileReader {
	public BufferedReader openBufferedReader() throws FileNotFoundException, IOException;
	public void loadFile() throws FileNotFoundException, IOException;
	public boolean browseFileSystem(AppFrame app);
	public void setFileName(String argFileName);
	public String getFileName();
}
