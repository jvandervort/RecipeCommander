package com.rednoblue.jRecipe.model;

import com.rednoblue.jRecipe.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Collections;

/**
 * Data model: highest level item, contains recipes.
 */
public class Book {

  /** name of the book */
  private String book_name;
  /** modification date of book */
  private Date mod_date;
  /** unique id of book.  Should be globally unique */
  private long book_id;
  /** list of recipes contained in the book */
  private ArrayList<Recipe> recipe_list;
  /** has the book been changed? */
  private boolean is_modified = false;

  // CONSTRUCTOR: caller is the parent frame that hosts the file menu
  public Book() {
    recipe_list = new ArrayList<Recipe>();
  }

  /*
   * 
   * 
   * Accessors
   * 
   * 
   */
  public Date getModDate() {
    return mod_date;
  }

  public boolean setModDate(Date modDate) {
    this.mod_date = modDate;
    return true;
  }

  public boolean setModified(boolean modified) {
    this.is_modified = modified;
    return true;
  }

  public boolean getModified() {
    return is_modified;
  }

  /**
   * Get the name of the book
   *
   * @return book_name
   */
  public String getBookName() {
    return book_name;
  }

  /**
   * Set the name of the book
   * @param arg name
   * @return success
   */
  public boolean setBookName(String arg) {
    book_name = arg;

    return true;
  }

  /**
   * Get the book_id
   *
   * @return book_id
   */
  public long getBookID() {

    return book_id;
  }

  /**
   * Set the book id
   * @param arg book_id
   * @return success
   */
  public boolean setBookID(long arg) {
    book_id = arg;

    return true;
  }

  /**
   * Get the recipe list
   *
   * @return recipes
   */
  public ArrayList<Recipe> getRecipes() {
    return recipe_list;
  }

  /**
   * Set the recipe list
   * @param argRlist recipe list
   * @return success
   */
  public boolean setRecipes(ArrayList<Recipe> argRlist) {
    recipe_list = argRlist;
    return true;
  }

  /** 
   * Add a single recipe to the book
   * @param rec recipe
   * @return success
   */
  public boolean addRecipe(Recipe rec) {
    //rec.setBook(this);
    recipe_list.add(rec);
    return true;
  }

  /**
   * Delete a sinlge recipe from the book
   * @param rec recipe
   * @return success
   */
  public boolean deleteRecipe(Recipe rec) {
    recipe_list.remove(rec);
    return true;
  }

  /**
   * Get the recipe count
   * @return count
   */
  public int recipeCount() {
    return recipe_list.size();
  }

  /** Find a recipe by name (first occurance) */
  public Recipe findRecipeByName(String recipeName) {
    Iterator it = recipe_list.iterator();
    while (it.hasNext()) {
      Recipe rec = (Recipe) it.next();
      if (rec.getRecipeName().equals(recipeName)) {
        return rec;
      }
    }
    return null;
  }

  /**
   * Get a list of chapters from the book
   * @return list of chapters
   */
  public ArrayList<String> getChapters() {
    ArrayList<String> l = new ArrayList<String>();
    Iterator it = recipe_list.iterator();
    while (it.hasNext()) {
      Recipe rec = (Recipe) it.next();
      if (l.indexOf(rec.getChapter()) == -1) {
        l.add(rec.getChapter());
      }
    }
    Collections.sort(l);
    return l;
  }

  /**
   * Get a list of categories from the book
   * @return list of categories
   */
  public ArrayList<String> getCategories() {
    ArrayList<String> l = new ArrayList<String>();
    Iterator it = recipe_list.iterator();
    while (it.hasNext()) {
      Recipe rec = (Recipe) it.next();
      if (l.indexOf(rec.getCat()) == -1) {
        l.add(rec.getCat());
      }
    }
    Collections.sort(l);
    return l;
  }

  public ArrayList<String> getSubCategories() {
    ArrayList<String> l = new ArrayList<String>();
    Iterator it = recipe_list.iterator();
    while (it.hasNext()) {
      Recipe rec = (Recipe) it.next();
      if (l.indexOf(rec.getSubCat()) == -1) {
        l.add(rec.getSubCat());
      }
    }
    Collections.sort(l);
    return l;
  }

  @Override
  public String toString() {
    return book_name;
  }
}    

