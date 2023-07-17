package com.rednoblue.jRecipe.model;

import java.io.StringWriter;
import java.util.Iterator;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

// XML Imports
import org.w3c.dom.CDATASection;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSOutput;
import org.w3c.dom.ls.LSSerializer;

/**
 *
 * @author John
 */
public class XmlUtils {

	private final static Logger LOGGER = Logger.getLogger(XmlUtils.class.getName());
	private static Book book;
	private static Document doc;
	private static String display_type;

	/*
	 * 
	 * 
	 * XML SECTION
	 * 
	 * 
	 */
	private static Document getXmlDoc() throws ParserConfigurationException {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true);
		dbf.setValidating(false);
		DocumentBuilder db = dbf.newDocumentBuilder();
		DOMImplementation dim = db.getDOMImplementation();
		Document d = dim.createDocument("http://www.w3.org/2001/XMLSchema-instance", "jRecipeBook", null);
		d.getDocumentElement().setAttribute("xsi:noNamespaceSchemaLocation", "recipe_book.xsd");
		return d;
	}

	private static void setDisplayType(String type) {
		if (!(type.equals("datamodel") || type.equals("jasper"))) {
			LOGGER.severe("Invalid Display Type: " + type);
		}
		XmlUtils.display_type = type;
	}

	public static Document getBookXml(Book book, String display_type) {
		XmlUtils.book = book;
		try {
			XmlUtils.doc = getXmlDoc();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		XmlUtils.setDisplayType(display_type);
		Element bookElement = XmlUtils.doc.getDocumentElement();
		Iterator it = book.getRecipes().iterator();
		while (it.hasNext()) {
			Recipe r = (Recipe) it.next();
			bookElement.appendChild(createRecipeElement(r));
		}

		// LOGGER.fine(getXmlString(doc));

		return doc;
	}

	public static Document getBookXml(Book book, Recipe singleRec, String display_type) {
		XmlUtils.book = book;
		try {
			XmlUtils.doc = getXmlDoc();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		XmlUtils.setDisplayType(display_type);
		Element bookElement = XmlUtils.doc.getDocumentElement();
		bookElement.appendChild(createRecipeElement(singleRec));

		// LOGGER.fine(getXmlString(doc));

		return doc;
	}

	private static Element createRecipeElement(Recipe r) {
		Element e = XmlUtils.doc.createElement("Recipe");
		e.setAttribute("Name", r.getRecipeName());
		e.setAttribute("Source", r.getSource());
		e.setAttribute("Chapter", r.getChapter());
		e.setAttribute("Cat", r.getCat());
		e.setAttribute("SubCat", r.getSubCat());
		e.setAttribute("Origin", r.getOrigin());
		e.setAttribute("UUID", r.getUUID().toString());
		e.setAttribute("ModDate", r.getModDateString());

		Element il = doc.createElement("IngredientList");
		if (r.getIngredientsList().size() > 0) {
			Iterator it = r.getIngredientsList().iterator();
			while (it.hasNext()) {
				Ingredient i = (Ingredient) it.next();
				il.appendChild(createIngredientElement(i));
			}
		}
		e.appendChild(il);

		Element pro = doc.createElement("ProcessList");
		if (XmlUtils.display_type.equals("jasper")) {
			CDATASection cd = doc.createCDATASection(RecipeUtils.htmlEncode(r.getProcess()));
			pro.appendChild(cd);
		} else if (XmlUtils.display_type.equals("datamodel")) {
			pro.setTextContent(r.getProcess());
		}
		e.appendChild(pro);

		Element com = doc.createElement("Comments");
		if (XmlUtils.display_type.equals("jasper")) {
			CDATASection cd = doc.createCDATASection(RecipeUtils.htmlEncode(r.getComments()));
			com.appendChild(cd);

		} else if (XmlUtils.display_type.equals("datamodel")) {
			com.setTextContent(r.getComments());
		}

		e.appendChild(com);

		return (e);
	}

	private static Element createIngredientElement(Ingredient i) {
		Element e = doc.createElement("Ingredient");

		if (XmlUtils.display_type.equals("jasper")) {
			e.setAttribute("Amount", i.getAmountString());
		} else if (XmlUtils.display_type.equals("datamodel")) {
			e.setAttribute("Amount", i.getAmount().toString());
		}

		e.setAttribute("Units", i.getUnits());
		e.setAttribute("Name", i.getName());
		return (e);
	}

	/**
	 * Get an XML representation of the model
	 * 
	 * @param doc xml document
	 * @return string representation of xml
	 */
	public static String getXmlString(Document doc) {
		try {
			// try {
			DOMImplementationRegistry registry = DOMImplementationRegistry.newInstance();
			DOMImplementation domImpl = registry.getDOMImplementation("LS 3.0");
			DOMImplementationLS implLS = (DOMImplementationLS) domImpl;
			LSSerializer dom3Writer = implLS.createLSSerializer();
			LSOutput output = implLS.createLSOutput();
			output.setEncoding("UTF-8");
			StringWriter out = new StringWriter();
			output.setCharacterStream(out);
			dom3Writer.write(doc, output);
			return out.toString();
		} catch (ClassNotFoundException ex) {
			LOGGER.severe(ex.toString());
		} catch (InstantiationException ex) {
			LOGGER.severe(ex.toString());
		} catch (IllegalAccessException ex) {
			LOGGER.severe(ex.toString());
		} catch (ClassCastException ex) {
			LOGGER.severe(ex.toString());
		}
		return null;
	}
}
