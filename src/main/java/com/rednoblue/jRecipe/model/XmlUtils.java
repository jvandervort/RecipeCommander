package com.rednoblue.jrecipe.model;

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

import com.google.inject.Inject;

public class XmlUtils {
	private final Logger logger;

	@Inject
	public XmlUtils(Logger logger) {
		this.logger = logger;
	}
	
	/**
	 * Transform entire book into an xml string
	 * @param book
	 * @param type
	 * @return
	 */
	public String transformToXmlString(Book book, EDisplayType type) {
		Document doc = transformToXmlDocument(book, type);
		try {
			// try {
			DOMImplementationRegistry registry = DOMImplementationRegistry.newInstance();
			DOMImplementation domImpl = registry.getDOMImplementation("LS 3.0");
			DOMImplementationLS implLS = (DOMImplementationLS) domImpl;
			LSSerializer dom3Writer = implLS.createLSSerializer();
			dom3Writer.getDomConfig().setParameter("format-pretty-print", Boolean.TRUE);
			LSOutput output = implLS.createLSOutput();
			output.setEncoding("UTF-8");
			StringWriter out = new StringWriter();
			output.setCharacterStream(out);
			dom3Writer.write(doc, output);
			return out.toString();
		} catch (ClassNotFoundException ex) {
			logger.severe(ex.toString());
		} catch (InstantiationException ex) {
			logger.severe(ex.toString());
		} catch (IllegalAccessException ex) {
			logger.severe(ex.toString());
		} catch (ClassCastException ex) {
			logger.severe(ex.toString());
		}
		return null;
	}
	
	/**
	 * Transform a book with a single recipe into an xml string 
	 * @param book
	 * @param rec
	 * @param type
	 * @return
	 */
	public String transformToXmlString(Book book, Recipe rec, EDisplayType type) {
		Document doc = transformToXmlDocument(book, rec, type);
		try {
			// try {
			DOMImplementationRegistry registry = DOMImplementationRegistry.newInstance();
			DOMImplementation domImpl = registry.getDOMImplementation("LS 3.0");
			DOMImplementationLS implLS = (DOMImplementationLS) domImpl;
			LSSerializer dom3Writer = implLS.createLSSerializer();
			dom3Writer.getDomConfig().setParameter("format-pretty-print", Boolean.TRUE);
			LSOutput output = implLS.createLSOutput();
			output.setEncoding("UTF-8");
			StringWriter out = new StringWriter();
			output.setCharacterStream(out);
			dom3Writer.write(doc, output);
			return out.toString();
		} catch (ClassNotFoundException ex) {
			logger.severe(ex.toString());
		} catch (InstantiationException ex) {
			logger.severe(ex.toString());
		} catch (IllegalAccessException ex) {
			logger.severe(ex.toString());
		} catch (ClassCastException ex) {
			logger.severe(ex.toString());
		}
		return null;
	}
	
	/**
	 * Transform a whole book into a w3c xml Document
	 * @param book
	 * @param type
	 * @return
	 */
	public Document transformToXmlDocument(Book book, EDisplayType type) {
		Document doc;
		try {
			doc = getXmlDoc(book);
			Element bookElement = doc.getDocumentElement();
			Iterator<Recipe> it = book.getRecipes().iterator();
			while (it.hasNext()) {
				Recipe r = it.next();
				bookElement.appendChild(createRecipeElement(doc, r, type));
			}
			return doc;
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Transform a book with a single recipe into a w3c xml Document
	 * @param book
	 * @param rec
	 * @param type
	 * @return
	 */
	public Document transformToXmlDocument(Book book, Recipe rec, EDisplayType type) {
		Document doc;
		try {
			doc = getXmlDoc(book);
			Element bookElement = doc.getDocumentElement();
			bookElement.appendChild(createRecipeElement(doc, rec, type));
			return doc;
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		return null;
	}
		
	private Document getXmlDoc(Book book) throws ParserConfigurationException {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true);
		dbf.setValidating(false);
		DocumentBuilder db = dbf.newDocumentBuilder();
		DOMImplementation dim = db.getDOMImplementation();
		Document d = dim.createDocument("", "jRecipeBook", null);
		Element root = d.getDocumentElement();
		root.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
		root.setAttribute("xsi:noNamespaceSchemaLocation", "recipe_book.xsd");
		root.setAttribute("Name", book.getBookName());
		root.setAttribute("ModDate", "");
		return d;
	}

	private Element createRecipeElement(Document doc, Recipe r, EDisplayType type ) {
		Element e = doc.createElement("Recipe");
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
			Iterator<Ingredient> it = r.getIngredientsList().iterator();
			while (it.hasNext()) {
				Ingredient i = it.next();
				il.appendChild(createIngredientElement(doc, i, type));
			}
		}
		e.appendChild(il);

		Element pro = doc.createElement("ProcessList");
		if (type == EDisplayType.JASPER) {
			CDATASection cd = doc.createCDATASection(RecipeUtils.htmlEncode(r.getProcess()));
			pro.appendChild(cd);
		} else if (type == EDisplayType.NORMAL) {
			pro.setTextContent(r.getProcess());
		}
		e.appendChild(pro);

		Element com = doc.createElement("Comments");
		if (type == EDisplayType.JASPER) {
			CDATASection cd = doc.createCDATASection(RecipeUtils.htmlEncode(r.getComments()));
			com.appendChild(cd);

		} else if (type == EDisplayType.NORMAL) {
			com.setTextContent(r.getComments());
		}

		e.appendChild(com);

		return (e);
	}

	private Element createIngredientElement(Document doc, Ingredient i, EDisplayType type) {
		Element e = doc.createElement("Ingredient");

		if (type == EDisplayType.JASPER) {
			e.setAttribute("Amount", i.getAmountString());
		} else if (type == EDisplayType.NORMAL) {
			e.setAttribute("Amount", i.getAmount().toString());
		}

		e.setAttribute("Units", i.getUnits());
		e.setAttribute("Name", i.getName());
		return (e);
	}
}
