package com.rednoblue.jRecipe.model;

import com.rednoblue.jRecipe.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.io.StringReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.UUID;
import java.lang.reflect.Field;

import java.util.logging.Logger;

/**
 * Data model: contained by book, contains ingredients
 */
public class Recipe {

  private final static Logger LOGGER = Logger.getLogger(Recipe.class.getName());
  //private Book book = null;
  public String recipe_name;
  public String source;
  public String chapter;
  public String cat;
  public String subcat;
  public String origin;
  public Date mod_date;
  public String process;
  public String comments;
  public UUID uuid = null;
  public ArrayList<Ingredient> iList;

  public Recipe() {
    recipe_name = new String("New Recipe");
    source = new String("");
    chapter = new String("");
    cat = new String("");
    subcat = new String("");
    origin = new String("");
    mod_date = new Date();
    iList = new ArrayList<Ingredient>();
    process = new String("");
    comments = new String("");
  }

  /*
  public void setBook(Book arg) {
  this.book = arg;
  }
  public Book getBook() {
  return book;
  }
   */
  public String getRecipeName() {
    return recipe_name;
  }

  public boolean setRecipeName(String arg) {
    recipe_name = arg;
    return true;
  }

  public boolean setComments(String arg) {
    String s = RecipeUtils.trimMultiLine(arg.trim());
    comments = s;
    return true;
  }

  public String getComments() {
    return comments;
  }

  public boolean setProcess(String arg) {
    String s = RecipeUtils.trimMultiLine(arg.trim());
    process = s;
    return true;
  }

  public String getProcess() {
    return process;
  }

  public String getSource() {
    if (source == null) {
      return "";
    } else {
      return source;
    }
  }

  public boolean setSource(String arg) {
    source = arg;

    return true;
  }

  public Date getModDate() {
    return mod_date;
  }

  public String getModDateString() {
    SimpleDateFormat sdf;
    sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    return sdf.format(mod_date);
  }

  public boolean setModDate(Date modDate) {
    this.mod_date = modDate;
    return true;
  }

  public String getChapter() {
    if (chapter == null) {
      return "";
    } else {
      return chapter;
    }
  }

  public boolean setChapter(String arg) {
    chapter = arg;

    return true;
  }

  public String getCat() {
    if (cat == null) {
      return "";
    } else {
      return cat;
    }
  }

  public boolean setCat(String arg) {
    cat = arg;

    return true;
  }

  public String getSubCat() {
    if (subcat == null) {
      return "";
    } else {
      return subcat;
    }
  }

  public boolean setSubCat(String arg) {
    subcat = arg;

    return true;
  }

  public UUID getUUID() {
    if (uuid == null) {
      uuid = UUID.randomUUID();
    }
    return uuid;
  }

  public boolean setUID(UUID arg) {
    uuid = arg;
    return true;
  }

  public boolean setUID(String arg) {
    uuid = UUID.fromString(arg);
    return true;
  }

  public String getOrigin() {
    if (origin == null) {
      return "";
    } else {
      return origin;
    }
  }

  public boolean setOrigin(String arg) {
    origin = arg;

    return true;
  }

  public boolean setIngredients(ArrayList<Ingredient> argIlist) {
    iList = argIlist;
    return true;
  }

  public ArrayList<Ingredient> getIngredientsList() {
    return iList;
  }

  public String getIngredientList(String type, boolean htmlHeader) {
    StringBuffer ret = new StringBuffer();

    String newline;
    if (type.equals("html")) {
      newline = "<br>";
    } else {
      newline = "\n";
    }

    if (iList.size() > 0) {
      Iterator it = iList.iterator();
      if (it.hasNext() && htmlHeader == true) {
        ret.append("<h2>Ingredients</h2>");
      }
      while (it.hasNext()) {
        Ingredient i = (Ingredient) it.next();
        if (i.getAmount().equals(new Float(0.0))) {
          ret.append(i.getName() + newline);
        } else {
          ret.append(i.getAmountString() + " " + i.getUnits() + " " + i.getName() + newline);
        }
      }
    }
    return ret.toString();
  }

  public boolean setIngredientsString(String ingreds) {
    try {
      iList = new ArrayList<Ingredient>();
      BufferedReader r = new BufferedReader(new StringReader(ingreds));
      String line;
      while ((line = r.readLine()) != null) {
        iList.add(new Ingredient(line));
      }
    } catch (IOException e) {
      LOGGER.severe(e.toString());
      System.exit(1);
    }
    return true;
  }

  public boolean addIngredient(Ingredient ingred) {
    iList.add(ingred);

    return true;
  }

  public String getRecipeElement(String element, String format, boolean header) {
    StringBuffer ret = new StringBuffer("");
    try {
      BufferedReader r = null;
      if (element.equals("Process")) {
        if (process.length() == 0) {
          return "";
        }
        r = new BufferedReader(new StringReader(process));
      } else if (element.equals("Comments")) {
        if (comments.length() == 0) {
          return "";
        }
        r = new BufferedReader(new StringReader(comments));
      }
      String line;
      if (header == true) {
        if (format.equals("html")) {
          ret.append("<h2>" + element + "</h2>");
        } else {
          ret.append(element + ":\n");
        }
      }
      while ((line = r.readLine()) != null) {
        if (format.equals("html")) {
          ret.append(line + "<br>");
        } else {
          ret.append(line + "\n");
        }
      }
    } catch (IOException e) {
      LOGGER.severe(e.toString());
      System.exit(1);
    }
    return ret.toString();
  }

  @Override
  public boolean equals(Object obj) {

    Recipe rec = (Recipe) obj;

    if (rec == null) {
      LOGGER.warning("rec was null");
    }

    if (this.getRecipeName().equals(rec.getRecipeName()) == false) {
      return false;
    }
    if (this.getSource().equals(rec.getSource()) == false) {
      return false;
    }
    if (this.getChapter().equals(rec.getChapter()) == false) {
      return false;
    }
    if (this.getCat().equals(rec.getCat()) == false) {
      return false;
    }
    if (this.getSubCat().equals(rec.getSubCat()) == false) {
      return false;
    }
    if (this.getOrigin().equals(rec.getOrigin()) == false) {
      return false;
    }
    if (this.getProcess().equals(rec.getProcess()) == false) {
      return false;
    }
    if (this.getComments().equals(rec.getComments()) == false) {
      return false;
    }

    // bail if ingredient lists are different sizes
    if (this.iList.size() != rec.iList.size()) {
      return false;
    }

    Iterator it1 = this.iList.iterator();
    Iterator it2 = rec.iList.iterator();
    while (it1.hasNext()) {
      Ingredient i1 = (Ingredient) it1.next();
      Ingredient i2 = (Ingredient) it2.next();

      if (i1.equals(i2) == false) {
        return false;
      }
    }

    return true;
  }

  /** perform a deep copy of recipe object */
  public Recipe duplicate() {
    Recipe newrec = new Recipe();

    newrec.setRecipeName(this.getRecipeName());
    newrec.setModDate(this.getModDate());
    newrec.setSource(this.getSource());
    newrec.setChapter(this.getChapter());
    newrec.setCat(this.getCat());
    newrec.setSubCat(this.getSubCat());
    newrec.setOrigin(this.getOrigin());
    newrec.setProcess(this.getProcess());
    newrec.setComments(this.getComments());

    if (this.iList.size() > 0) {
      Iterator it = this.iList.iterator();
      while (it.hasNext()) {
        Ingredient ingred = (Ingredient) it.next();
        newrec.addIngredient(ingred.duplicate());
      }
    }

    return newrec;
  }

  public String getTreeAncestors(ArrayList<String> byArray) {
    StringBuffer ret = new StringBuffer("");
    String val = "";
    for (String fieldname : byArray) {
      try {
        Field f = this.getClass().getField(fieldname);
        val = (String) f.get(this);
        ret.append(val + ":");
      } catch (Throwable e) {
        LOGGER.warning(e.toString());
      }

    }
    return ret.toString();
  }

  /** used by jtree node names in AppFrame */
  public String toString() {
    String ret = recipe_name;
    if (source.length() > 0) {
      ret = ret + " (" + source + ")";
    }
    return ret;
  }

  /** used by copytoplaintext in AppFrame */
  public String toStringComplete() {
    StringBuffer s = new StringBuffer("");
    s.append(recipe_name + " ");
    if (source.length() > 0) {
      s.append("(" + source + ")\n");
    }
    s.append("\n");
    s.append("\nIngredients\n");
    s.append("  " + getIngredientList("text", false).replaceAll("\n", "\n  "));
    if (process.length() > 0) {
      s.append("\nProcess\n  " + getProcess().replaceAll("\n", "\n  "));
    }
    if (comments.length() > 0) {
      s.append("\nComments\n  " + getComments().replaceAll("\n", "\n  "));
    }
    return s.toString();
  }
}

