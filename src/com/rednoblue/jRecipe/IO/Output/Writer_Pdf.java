package com.rednoblue.jRecipe.IO.Output;

import com.rednoblue.jRecipe.model.BookUtils;
import com.rednoblue.jRecipe.*;
import com.rednoblue.jRecipe.IO.*;
import com.rednoblue.jRecipe.model.*;
import java.net.URL;

// jasper imports
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.*;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.engine.data.JRXmlDataSource;



/**
 *
 * @author jcv9868
 */
public class Writer_Pdf implements O_Interface {
    // extension info
    static private final String formatName = "PDF";
    static private final String fileExtension = "pdf";
    static private final String fileDescription = formatName + " Files";
    static private final boolean readable = false;
    
    String fileName = null;
    Book book = null;
    Recipe rec = null;
    
    /** Creates a new instance of pdfWriter */
    public Writer_Pdf() {
    }
    public boolean isReadable() {
        return readable;
    }
    
    public void write() {
        
        try {
            URL url = getClass().getResource("/xml/Report1.jrxml");
            JasperDesign jasperDesign = JRXmlLoader.load(url.openStream());
            
            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
            
            JRDataSource ds=null;
            BookUtils bu = new BookUtils(book);
            if ( rec == null ) {
                ds = new JRXmlDataSource(XmlUtils.getBookXml(book, "jasper"), "/jRecipeBook/Recipe");
            } else {
                //src = new DOMSource(book.getXmlDocumentForOutput(rec));
                ds = new JRXmlDataSource(XmlUtils.getBookXml(book, rec, "jasper"), "/jRecipeBook/Recipe");
            }
            
            //JasperFillManager.fillReportToFile(jasperReport, "out.txt", null, ds);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, ds);
            JasperExportManager.exportReportToPdfFile(jasperPrint, fileName);
            
            
        } catch ( JRException e) {
            System.err.println("threw JRException:\n"+e);
        } catch ( java.io.IOException e) {
            System.err.println("threw IOException:\n"+e);
        }

    }
    
    public void setFileName(String argFileName) {
        java.util.regex.Pattern p = java.util.regex.Pattern.compile("\\.pdf$");
        java.util.regex.Matcher m = p.matcher(argFileName);
        
        boolean b = m.find();
        if ( b==true ) {
            fileName = argFileName;
        } else {
            fileName = argFileName + ".pdf";
        }
    }
    
    public void setBook(Book argBook) {
        book = argBook;
    }
    public void setRec(Recipe argRec) {
        rec = argRec;
    }
    
    public String getFormatName() {
        return formatName;
    }
    public String getFileExtension() {
        return fileExtension;
    }
    
    public MyFileFilter getChoosableFileFilter() {
        return new MyFileFilter(fileExtension, fileDescription);
    }
    
    static class PdfWriterFactory extends O_Factory {
        public O_Interface create() {
            return(new Writer_Pdf());
        }
    }
    
    static {
        O_FormatCreator.oFactories.put("Writer_Pdf", new PdfWriterFactory());
    }
    
}
