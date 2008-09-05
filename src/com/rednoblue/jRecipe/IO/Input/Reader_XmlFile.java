package com.rednoblue.jRecipe.IO.Input;

import com.rednoblue.jRecipe.IO.*;
import com.rednoblue.jRecipe.model.Book;
import com.rednoblue.jRecipe.model.Ingredient;
import com.rednoblue.jRecipe.model.Recipe;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.regex.Pattern;

// xml imports
import javax.xml.parsers.*;
import org.xml.sax.*;
import org.xml.sax.helpers.*;
import javax.xml.transform.*;
import javax.xml.validation.*;

import java.util.logging.Level;
import java.util.logging.Logger;

class Reader_XmlFile extends DefaultHandler implements I_Interface {

  //
  // Constants
  //
  // feature ids
  /** Namespaces feature id (http://xml.org/sax/features/namespaces). */
  protected static final String NAMESPACES_FEATURE_ID = "http://xml.org/sax/features/namespaces";
  /** Namespace prefixes feature id (http://xml.org/sax/features/namespace-prefixes). */
  protected static final String NAMESPACE_PREFIXES_FEATURE_ID = "http://xml.org/sax/features/namespace-prefixes";
  /** Validation feature id (http://xml.org/sax/features/validation). */
  protected static final String VALIDATION_FEATURE_ID = "http://xml.org/sax/features/validation";
  /** Schema validation feature id (http://apache.org/xml/features/validation/schema). */
  protected static final String SCHEMA_VALIDATION_FEATURE_ID = "http://apache.org/xml/features/validation/schema";
  /** Schema full checking feature id (http://apache.org/xml/features/validation/schema-full-checking). */
  protected static final String SCHEMA_FULL_CHECKING_FEATURE_ID = "http://apache.org/xml/features/validation/schema-full-checking";
  /** Validate schema annotations feature id (http://apache.org/xml/features/validate-annotations) */
  protected static final String VALIDATE_ANNOTATIONS_ID = "http://apache.org/xml/features/validate-annotations";
  /** Dynamic validation feature id (http://apache.org/xml/features/validation/dynamic). */
  protected static final String DYNAMIC_VALIDATION_FEATURE_ID = "http://apache.org/xml/features/validation/dynamic";
  /** XInclude feature id (http://apache.org/xml/features/xinclude). */
  protected static final String XINCLUDE_FEATURE_ID = "http://apache.org/xml/features/xinclude";
  /** XInclude fixup base URIs feature id (http://apache.org/xml/features/xinclude/fixup-base-uris). */
  protected static final String XINCLUDE_FIXUP_BASE_URIS_FEATURE_ID = "http://apache.org/xml/features/xinclude/fixup-base-uris";
  /** XInclude fixup language feature id (http://apache.org/xml/features/xinclude/fixup-language). */
  protected static final String XINCLUDE_FIXUP_LANGUAGE_FEATURE_ID = "http://apache.org/xml/features/xinclude/fixup-language";
  // default settings
  protected static final String DEFAULT_PARSER_NAME = "org.apache.xerces.parsers.SAXParser";
  protected static final int DEFAULT_REPETITION = 1;
  protected static final boolean DEFAULT_NAMESPACES = true;
  protected static final boolean DEFAULT_NAMESPACE_PREFIXES = false;
  protected static final boolean DEFAULT_VALIDATION = false;
  protected static final boolean DEFAULT_SCHEMA_VALIDATION = false;
  protected static final boolean DEFAULT_SCHEMA_FULL_CHECKING = false;
  protected static final boolean DEFAULT_VALIDATE_ANNOTATIONS = false;
  protected static final boolean DEFAULT_DYNAMIC_VALIDATION = false;
  protected static final boolean DEFAULT_XINCLUDE = false;
  protected static final boolean DEFAULT_XINCLUDE_FIXUP_BASE_URIS = true;
  protected static final boolean DEFAULT_XINCLUDE_FIXUP_LANGUAGE = true;
  protected static final boolean DEFAULT_MEMORY_USAGE = false;
  protected static final boolean DEFAULT_TAGGINESS = false;
  protected static final String SCHEMA_LOCATION_PROP = "http://apache.org/xml/properties/schema/external-noNamespaceSchemaLocation";
  // Data
  private Collection iList;
  // Collection of ingredient objects
  //private Collection rList;
  // Collection of recipes in book
  private Recipe rec;
  // recipe, gets pushed onto Collection
  private Ingredient ingred;
  // book
  private Book book;
  private String chars;
  // extension info
  static private final String formatName = "jRecipe";
  static private final String[] fileExtension = {"xml", "jrec"};
  static private final String fileDescription = formatName + " Files";

  public Reader_XmlFile() {
  }
/*
  public Reader_XmlFile(java.io.Reader reader) {
    try {
      parseXml(new InputSource(reader));
    } catch (SAXException e) {
      Logger.getLogger(Reader_XmlFile.class.getName()).log(Level.SEVERE, null, e);
    }
  }
*/
  public void parseSource(java.io.Reader reader) {
    try {
      InputSource s = new InputSource(reader);
      s.setEncoding("UTF-8");
      parseXml(s);
    } catch (SAXException e) {
      Logger.getLogger(Reader_XmlFile.class.getName()).log(Level.SEVERE, null, e);
    }
  }

  public void parseXml(InputSource inputStream) throws SAXException {
    XMLReader parser = null;

    book = new Book();

    // create parser
    try {
      parser = XMLReaderFactory.createXMLReader(DEFAULT_PARSER_NAME);
    } catch (Exception e) {
      Logger.getLogger(Reader_XmlFile.class.getName()).log(Level.SEVERE, null, e);
    }

    // set parser features
    try {
      parser.setFeature(NAMESPACES_FEATURE_ID, DEFAULT_NAMESPACES);
    } catch (SAXException e) {
      Logger.getLogger(Reader_XmlFile.class.getName()).log(Level.SEVERE, null, e);
    }
    try {
      parser.setFeature(NAMESPACE_PREFIXES_FEATURE_ID, DEFAULT_NAMESPACE_PREFIXES);
    } catch (SAXException e) {
      Logger.getLogger(Reader_XmlFile.class.getName()).log(Level.SEVERE, null, e);
    }
    try {
      parser.setFeature(VALIDATION_FEATURE_ID, DEFAULT_VALIDATION);
    } catch (SAXException e) {
      Logger.getLogger(Reader_XmlFile.class.getName()).log(Level.SEVERE, null, e);
    }
    try {
      parser.setFeature(SCHEMA_VALIDATION_FEATURE_ID, DEFAULT_SCHEMA_VALIDATION);
    } catch (SAXNotRecognizedException e) {
      Logger.getLogger(Reader_XmlFile.class.getName()).log(Level.SEVERE, null, e);

    } catch (SAXNotSupportedException e) {
      Logger.getLogger(Reader_XmlFile.class.getName()).log(Level.SEVERE, null, e);
    }
    try {
      parser.setFeature(SCHEMA_FULL_CHECKING_FEATURE_ID, DEFAULT_SCHEMA_FULL_CHECKING);
    } catch (SAXNotRecognizedException e) {
      Logger.getLogger(Reader_XmlFile.class.getName()).log(Level.SEVERE, null, e);

    } catch (SAXNotSupportedException e) {
      Logger.getLogger(Reader_XmlFile.class.getName()).log(Level.SEVERE, null, e);
    }
    try {
      parser.setFeature(VALIDATE_ANNOTATIONS_ID, DEFAULT_VALIDATE_ANNOTATIONS);
    } catch (SAXNotRecognizedException e) {
      Logger.getLogger(Reader_XmlFile.class.getName()).log(Level.SEVERE, null, e);

    } catch (SAXNotSupportedException e) {
      Logger.getLogger(Reader_XmlFile.class.getName()).log(Level.SEVERE, null, e);
    }
    try {
      parser.setFeature(DYNAMIC_VALIDATION_FEATURE_ID, DEFAULT_DYNAMIC_VALIDATION);
    } catch (SAXNotRecognizedException e) {
      Logger.getLogger(Reader_XmlFile.class.getName()).log(Level.SEVERE, null, e);

    } catch (SAXNotSupportedException e) {
      Logger.getLogger(Reader_XmlFile.class.getName()).log(Level.SEVERE, null, e);
    }
    try {
      parser.setFeature(XINCLUDE_FEATURE_ID, DEFAULT_XINCLUDE);
    } catch (SAXNotRecognizedException e) {
      Logger.getLogger(Reader_XmlFile.class.getName()).log(Level.SEVERE, null, e);

    } catch (SAXNotSupportedException e) {
      Logger.getLogger(Reader_XmlFile.class.getName()).log(Level.SEVERE, null, e);
    }
    try {
      parser.setFeature(XINCLUDE_FIXUP_BASE_URIS_FEATURE_ID, DEFAULT_XINCLUDE_FIXUP_BASE_URIS);
    } catch (SAXNotRecognizedException e) {
      Logger.getLogger(Reader_XmlFile.class.getName()).log(Level.SEVERE, null, e);

    } catch (SAXNotSupportedException e) {
      Logger.getLogger(Reader_XmlFile.class.getName()).log(Level.SEVERE, null, e);
    }
    try {
      parser.setFeature(XINCLUDE_FIXUP_LANGUAGE_FEATURE_ID, DEFAULT_XINCLUDE_FIXUP_LANGUAGE);
    } catch (SAXNotRecognizedException e) {
      Logger.getLogger(Reader_XmlFile.class.getName()).log(Level.SEVERE, null, e);

    } catch (SAXNotSupportedException e) {
      Logger.getLogger(Reader_XmlFile.class.getName()).log(Level.SEVERE, null, e);
    }


    try {
      parser = XMLReaderFactory.createXMLReader(DEFAULT_PARSER_NAME);
    } catch (Exception e) {
      parser = null;
      Logger.getLogger(Reader_XmlFile.class.getName()).log(Level.SEVERE, null, e);
    }

    // Set the ContentHandler of the XMLReader
    parser.setContentHandler(this);

    // Set an ErrorHandler before parsing
    parser.setErrorHandler(this);

    /* never worked
    try {
    URL url = getClass().getResource("/xml/recipe_book.xs2d");
    parser.setProperty(SCHEMA_LOCATION_PROP, url.getFile());
    } catch (SAXNotSupportedException e) {
    System.err.println("warning: Parser does not support feature ("+SCHEMA_LOCATION_PROP+")");
    }
     */

    // Tell the XMLReader to parse the XML document
    try {
      inputStream.setEncoding("UTF-8");
      parser.parse(inputStream);
    } catch (java.io.IOException ioe) {
      Logger.getLogger(Reader_XmlFile.class.getName()).log(Level.SEVERE, null, ioe);
    }

    Logger.getLogger(Reader_XmlFile.class.getName()).log(Level.INFO, "File Parsed!");
  }

  @Override
  public void startElement(String uri, String localpart, String rawname, org.xml.sax.Attributes attributes) throws SAXException {
    if (localpart.equals("Recipe")) {
      //<Recipe Name="Glue" Submitter="JV" Category="Misc" UID="1" ModDate="5/14/2002">
      rec = new Recipe();
      if (attributes != null) {
        int length = attributes.getLength();


        for (int i = 0; i < length; i++) {
          String name = attributes.getLocalName(i);
          String value = attributes.getValue(i);

          if (name.equalsIgnoreCase("Name")) {
            rec.setRecipeName(value);
          }
          if (name.equalsIgnoreCase("Source")) {
            rec.setSource(value);
          }
          if (name.equalsIgnoreCase("Chapter")) {
            rec.setChapter(value);
          }
          if (name.equalsIgnoreCase("Cat")) {
            rec.setCat(value);
          }
          if (name.equalsIgnoreCase("SubCat")) {
            rec.setSubCat(value);
          }
          if (name.equalsIgnoreCase("Origin")) {
            rec.setOrigin(value);
          }
          if (name.equalsIgnoreCase("UUID")) {
            if (attributes.getValue(i).length() > 0) {
              rec.setUID(value);
            }
          }
          if (name.equalsIgnoreCase("ModDate")) {
            SimpleDateFormat sdf;
            try {
              sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
              java.util.Date d = sdf.parse(attributes.getValue(i));
              rec.setModDate(d);
            } catch (java.text.ParseException e1) {
              Logger.getLogger(Reader_XmlFile.class.getName()).log(Level.SEVERE, null, e1);
            }
          }
        }
      }
    } else if (localpart.equals("Ingredient")) {
      //<Ingredient Name="Sugar, granulated" Amount="1.5" Units="Cup"/>
      ingred = new Ingredient();
      if (attributes != null) {
        int length = attributes.getLength();
        for (int i = 0; i < length; i++) {
          if (attributes.getLocalName(i).equalsIgnoreCase("Name")) {
            ingred.setName(attributes.getValue(i));
          }
          if (attributes.getLocalName(i).equalsIgnoreCase("Amount")) {
            Float fltVal = new Float(attributes.getValue(i));
            ingred.setAmount(fltVal);
          }
          if (attributes.getLocalName(i).equalsIgnoreCase("Units")) {
            ingred.setUnits(attributes.getValue(i));
          }
        }
      }

    } else if (localpart.equals("IngredientRef")) {
      if (attributes != null) {
        int length = attributes.getLength();
        for (int i = 0; i < length; i++) {
          if (attributes.getLocalName(i).equalsIgnoreCase("Name")) {
            chars = chars + attributes.getValue(i);
          }
        }
      }


    } else if (localpart.equals("ProcessList")) {
      //<ProcessList PrepTime="00:10" TotalTime="00:40">
      chars = "";
    } else if (localpart.equals("Comments")) {
      //<Comments>Well this is a really good recipe ...</Comments>
      chars = "";
    // Html tags for comments and process
    //} else if ( (localpart.equalsIgnoreCase("LI")) || (localpart.equalsIgnoreCase("UL")) || (localpart.equalsIgnoreCase("OL")) || (localpart.equalsIgnoreCase("H1")) || (localpart.equalsIgnoreCase("H2")) || (localpart.equalsIgnoreCase("H3")) || (localpart.equalsIgnoreCase("B")) ) {
    //<Comments>Well this is a really good recipe ...</Comments>
    //chars = chars + "<" + localpart  + ">";

    } else if (localpart.equals("jRecipeBook")) {
      //<Ingredient Name="Sugar, granulated" Amount="1.5" Units="Cup"/>
      if (attributes != null) {
        int length = attributes.getLength();
        for (int i = 0; i < length; i++) {
          if (attributes.getLocalName(i).equalsIgnoreCase("Name")) {
            book.setBookName(attributes.getValue(i));
          }
        }
      }
    }
  }

  @Override
  public void characters(char[] ch, int offset, int length) throws SAXException {
    if (length > 0) {
      chars = chars + new String(ch, offset, length);
    }
  }

  @Override
  public void endElement(String uri, String localpart, String rawname) throws SAXException {
    if (localpart.equals("Recipe")) {
      //<Recipe Name="Glue" Submitter="JV" Category="Misc" UID="1" ModDate="5/14/2002">
      book.addRecipe(rec);
    } else if (localpart.equals("Ingredient")) {
      rec.addIngredient(ingred);
    } else if (localpart.equals("ProcessList")) {
      rec.setProcess(chars);
    } else if (localpart.equals("Comments")) {
      rec.setComments(chars);
    }
  }

  /*
  The Next Four methods (warning, error, and fatalerror, getLocationString)
  make up the errorhandler
   */
  @Override
  public void warning(SAXParseException ex) {
    Logger.getLogger(Reader_XmlFile.class.getName()).log(Level.SEVERE, null, ex);
  }

  @Override
  public void error(SAXParseException ex) {
    Logger.getLogger(Reader_XmlFile.class.getName()).log(Level.SEVERE, null, ex);
  }

  @Override
  public void fatalError(SAXParseException ex) throws SAXException {
    Logger.getLogger(Reader_XmlFile.class.getName()).log(Level.SEVERE, null, ex);
    throw ex;
  }

  private String getLocationString(SAXParseException ex) {
    StringBuffer str = new StringBuffer();

    String systemId = ex.getSystemId();
    if (systemId != null) {
      int index = systemId.lastIndexOf('/');
      if (index != -1) {
        systemId = systemId.substring(index + 1);
      }
      str.append(systemId);
    }
    str.append('(');
    str.append(ex.getLineNumber());
    str.append(',');
    str.append(ex.getColumnNumber());
    str.append(')');

    return str.toString();
  }

  public String getFormatName() {
    return formatName;
  }

  public Book getBook() {
    //book.setRecipes(rList);
    //book.setIngredients(iList);
    return book;
  }

  public MyFileFilter getChoosableFileFilter() {
    return new MyFileFilter(fileExtension, fileDescription);
  }

  public boolean isFileMine(String tenlines) {
    Pattern p = Pattern.compile("<jRecipeBook");
    if (p.matcher(tenlines).find() == true) {
      return true;
    } else {
      return false;
    }
  }

  static class XmlReaderFactory extends I_Factory {

    public I_Interface create() {
      return (new Reader_XmlFile());
    }
    }

  static {
    I_FormatCreator.iFactories.put("Reader_XmlFile", new XmlReaderFactory());
  }
}
