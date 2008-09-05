package com.rednoblue.jRecipe.IO;

import com.rednoblue.jRecipe.IO.Output.*;
import com.rednoblue.jRecipe.model.Book;
import com.rednoblue.jRecipe.model.Recipe;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.filechooser.*;
import java.util.Enumeration;
import com.rednoblue.jRecipe.AppFrame;
import com.rednoblue.jRecipe.IO.Output.O_Interface;

/**
 * Writes the book out to a file.  Handles the creation of the various FileWriters.
 */
public class MyFileWriter extends O_FormatCreator {
    private String fileFormat="";
    private String fileName = "";
    private String fileEx = "";
    
    private O_Interface fileWriter;
    
    private Book book=null;
    private Recipe rec=null;
    
    public MyFileWriter() {
        createOutputFormats();
    }
    /**
     *
     * @param argBook
     * @param argFileName
     */
    public MyFileWriter(Book argBook, String argFileName) {
        createOutputFormats();
        book = argBook;
        fileName = argFileName;
        fileEx = FileUtil.getExtension(fileName);
        saveFile(true);
    }
    public MyFileWriter(Book argBook, Recipe argRec, String argFileName) {
        createOutputFormats();
        book = argBook;
        rec = argRec;
        fileName = argFileName;
        fileEx = FileUtil.getExtension(fileName);
        saveFile(true);
    }
    
    
    /**
     * createOutputFormats loads formats,
     * and populates the oFactories list of formats
     */
    private void createOutputFormats() {
        
        O_Interface writer;
        
        // These need to be actual classnames
        String[] oFormats;
        oFormats = new String[3];
        oFormats[0] = "Writer_MealMaster";
        oFormats[1] = "Writer_XmlFile";
        oFormats[2] = "Writer_Pdf";
        for (int i = 0; i < oFormats.length; i++) {
            try {
                writer = createFormat(oFormats[i]);
            } catch(O_FormatNotCreatedException e) {
                System.out.println("O_FormatNotCreatedException(" + oFormats[i] + "): " + e.getMessage());
            } catch(ClassCastException e) {
                System.out.println("ClassCastException(" + oFormats[i] + "): Tried to load a non-format as a format!!");
            }
        }
    }
    
    /**
     * Saves the book to a filename.
     * @param argBook
     * @param argRec
     * @param argFileName
     * @return success
     */
    public boolean saveAsFile(Book argBook, Recipe argRec, String argFileName) {
        book = argBook;
        rec = argRec;
        fileName = argFileName;
        if (saveFile(false)) {
            System.err.println("Book Saved!");
            return true;
        }
        return false;
    }
    
    /**
     * @param overwrite existing file, or error.
     * @return success or fail
     */
    public boolean saveFile(boolean overwrite) {
        
        // If we have no format, try to detect
        if ( fileFormat.equals("")) {
            if (fileEx.equalsIgnoreCase("xml") ) {
                fileFormat = "Writer_XmlFile";
            } else if (fileEx.equalsIgnoreCase("mmf") ) {
                fileFormat = "Writer_MealMaster";
            } else if (fileEx.equalsIgnoreCase("pdf") ) {
                fileFormat = "Writer_Pdf";
            } else {
                // unsupported file format, maybe detect later
                System.err.println("unsupported file extension(" + fileEx + ")");
                return false;
            }
        }
        
        
        try {
            O_Interface writer = createFormat(fileFormat);
            
            // If we don't have the proper file extension, add it!
            if ( ! FileUtil.getExtension(fileName).equalsIgnoreCase(writer.getFileExtension()) ) {
                fileName = fileName + "." + writer.getFileExtension();
            }
            
            // see if bak file exists, if so, delete it.
            String bakFileName = fileName.replace("." + FileUtil.getExtension(fileName), "") + ".bak";
            File bakFile = new File(bakFileName);
            bakFile.delete();
            
            // if the format is output only, don't create bak file
            if (writer.isReadable() == true) {
                // rename the old file to => .bak
                File oldFile = new File(fileName);
                if (oldFile.renameTo(new File(bakFileName))) {
                    System.err.println("Rename Successful");
                } else {
                    // NOTE: saveAs will always fail here
                    System.err.println("Rename Failed (OK for SaveAs) " + fileName + " to " + bakFileName);
                }
            }
            
            writer.setBook(book);
            if ( rec != null) {
                writer.setRec(rec);
            }
            writer.setFileName(fileName);
            writer.write();
            fileWriter=writer;
            fileFormat = writer.getFormatName();
            
        } catch(Exception e) {
            System.out.println(this.getClass().getName() + ".saveFile: " + e.getMessage());
        }
        
        
        return true;
    }
    
    /**
     *
     * @param app application handle
     * @param selectedFilter filter by file type
     * @return success or failure
     */
    public boolean browseFileSystem(AppFrame app, String selectedFilter) {
        //Create a file chooser
        final JFileChooser fc = new JFileChooser(app.getLastFileName());
        
        // Set Filters
        Enumeration keys = oFactories.keys();
        MyFileFilter jRecipeFilter = null;
        while( keys.hasMoreElements() ) {
            String key = (String)keys.nextElement( );
            try {
                O_Interface o = createFormat(key);
                MyFileFilter f = o.getChoosableFileFilter();
                fc.addChoosableFileFilter(f);
                if ( o.getFormatName().equals(selectedFilter)) {
                    jRecipeFilter = f;
                }
                
            } catch(O_FormatNotCreatedException e) {
                System.out.println(e.getMessage());
            } catch(ClassCastException e) {
                System.out.println(e.getMessage()+ " browseFileSystem:ClassCastException!");
            }
        }
        fc.setFileFilter(jRecipeFilter);
        
        // set last selected file
        fc.setSelectedFile(new File(app.getLastFileName()));
        
        //In response to a button click:
        int returnVal = fc.showSaveDialog(app);
        
        if (returnVal == javax.swing.JFileChooser.APPROVE_OPTION) {
            MyFileFilter ff = (MyFileFilter) fc.getFileFilter();
            
            fileName = fc.getSelectedFile().getAbsolutePath();
            fileEx = FileUtil.getExtension(fileName);
            
            if (ff.getDescription().equalsIgnoreCase("jRecipe Files (.xml)") ) {
                fileFormat = "Writer_XmlFile";
            } else if (ff.getDescription().equalsIgnoreCase("Meal-Master Files (.mmf)") ) {
                fileFormat = "Writer_MealMaster";
            } else if (ff.getDescription().equalsIgnoreCase("PDF Files (.pdf)") ) {
                fileFormat = "Writer_Pdf";
            } else {
                // unsupported file format, maybe detect later
                System.err.println("Couldn't Use Filter");
                fileFormat = "";
            }
            
            return true;
        } else {
            return false;
        }
        
    }
    
    /**
     *
     * @return fileFormat string
     */
    public String getLastWriterFormat() {
        return fileFormat;
    }
    
    /**
     *
     * @return fileWriter instance
     */
    public O_Interface getLastWriter() {
        return fileWriter;
    }
    
    
    /**
     * Set the book property
     * @param argBook 
     */
    public void setBook(Book argBook) {
        book = argBook;
    }
    /**
     *
     * @param argFileName
     */
    public void setFileName(String argFileName) {
        fileName = argFileName;
    }
    
    /**
     *
     * @return fileName
     */
    public String getFileName() {
        return fileName;
    }
    
}
