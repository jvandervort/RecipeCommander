package com.rednoblue.jrecipe.ui;

import java.util.ArrayList;
import java.util.Comparator;

import com.rednoblue.jrecipe.model.Recipe;

public /**
		 * Used for sorting the jtree in various ways
		 */
class RecipeTreeComparator implements Comparator<Recipe> {
	private final ArrayList<String> chapterTree = new ArrayList<String>();
	private final ArrayList<String> originTree = new ArrayList<String>();
	private final ArrayList<String> sourceTree = new ArrayList<String>();
	private final ArrayList<String> recipeNameTree = new ArrayList<String>();
	private final String by;

	public RecipeTreeComparator(String by) {
		this.by = by;
		
		this.chapterTree.add(new String("chapter"));
		this.chapterTree.add(new String("cat"));
		this.chapterTree.add(new String("subcat"));
		this.chapterTree.add(new String("recipeName"));

		this.originTree.add(new String("origin"));
		this.originTree.add(new String("chapter"));
		this.originTree.add(new String("cat"));
		this.originTree.add(new String("subcat"));
		this.originTree.add(new String("recipeName"));

		this.sourceTree.add(new String("source"));
		this.sourceTree.add(new String("chapter"));
		this.sourceTree.add(new String("cat"));
		this.sourceTree.add(new String("subcat"));
		this.sourceTree.add(new String("recipeName"));

		this.recipeNameTree.add(new String("recipeName"));
	}

	public int compare(Recipe o1, Recipe o2) {

		Recipe r1 = (Recipe) o1;
		Recipe r2 = (Recipe) o2;

		int compareVal = 0;
		if (by.equals("chapter")) {
			compareVal = r1.getTreeAncestors(chapterTree).compareTo(r2.getTreeAncestors(chapterTree));
		} else if (by.equals("origin")) {
			compareVal = r1.getTreeAncestors(originTree).compareTo(r2.getTreeAncestors(originTree));
		} else if (by.equals("source")) {
			compareVal = r1.getTreeAncestors(sourceTree).compareTo(r2.getTreeAncestors(sourceTree));
		} else if (by.equals("recipeName")) {
			compareVal = r1.getTreeAncestors(recipeNameTree)
					.compareTo(r2.getTreeAncestors(recipeNameTree));
		}

		if (compareVal != 0) {
			return compareVal;
		}
		return 0; // they are the same
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof RecipeTreeComparator)) {
			return false;
		} else {
			return true;
		}
	}
}
