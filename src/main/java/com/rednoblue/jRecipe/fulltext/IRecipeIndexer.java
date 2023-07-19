package com.rednoblue.jrecipe.fulltext;

import java.util.Collection;
import java.util.HashMap;

public interface IRecipeIndexer {

	/**
	 * Begin indexing the book of recipes
	 * 
	 * @param book of recipes
	 */
	void indexRecipes();

	HashMap<String, String> searchRecipes(Collection<String> fields, String querystr);

}