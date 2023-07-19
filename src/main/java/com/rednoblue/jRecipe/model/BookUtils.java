package com.rednoblue.jrecipe.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Logger;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

/**
 * Helper class for the Book, Recipe, and Ingredient classes
 * 
 * @author John Vandervort
 * @version 1.0
 */
public class BookUtils {
	private final static Logger LOGGER = Logger.getLogger(BookUtils.class.getName());
	private Book book;

	/** make nice and pretty for printing */
	private boolean _for_display = false;

	public BookUtils(Book book) {
		this.book = book;
	}

	public BookUtils(Book book, boolean for_display) {
		this(book);
		this._for_display = for_display;
	}

	/**
	 * Loads the jtree on the main screen. Includes support for filtering and
	 * sorting.
	 * 
	 * @param tree   from the mainframe
	 * @param viewBy various facets of sorting/viewing
	 * @param filter lucene filtered grouping
	 */
	public void loadJtree(javax.swing.JTree tree, String viewBy, HashMap<String,String> filter) {
		// setup variables
		DefaultTreeModel treeModel;
		DefaultMutableTreeNode rootNode;
		DefaultMutableTreeNode nMatch;
		DefaultMutableTreeNode nNoMatch;
		String nodeText;
		Recipe lastrec = new Recipe();
		DefaultMutableTreeNode chapterNode = null;
		DefaultMutableTreeNode catNode = null;
		DefaultMutableTreeNode subCatNode = null;
		DefaultMutableTreeNode originNode = null;
		DefaultMutableTreeNode sourceNode = null;

		ArrayList<Recipe> rList = book.getRecipes();
		Collections.sort(rList, new TreeComparator(viewBy));

		if (filter != null) {
			// sort should be based on group by
			if (viewBy.equals("chapter")) {
				rootNode = new DefaultMutableTreeNode(book);
				Iterator<Recipe> it = rList.iterator();
				while (it.hasNext()) {
					Recipe rec = (Recipe) it.next();
					DefaultMutableTreeNode node = new DefaultMutableTreeNode(rec);

					// if group by chapter, but also should support group by region, and maybe
					// others
					if (chapterNode == null || lastrec.getChapter().compareTo(rec.getChapter()) != 0) {
						// add source node
						if (rec.getChapter().compareTo("") == 0) {
							nodeText = "No Chapter";
						} else {
							nodeText = rec.getChapter();
						}
						chapterNode = new DefaultMutableTreeNode(nodeText);
						rootNode.add(chapterNode);
						catNode = null;
						subCatNode = null;
					}

					if (rec.getCat().compareTo("") != 0 && chapterNode != null
							&& lastrec.getCat().compareTo(rec.getCat()) != 0) {
						catNode = new DefaultMutableTreeNode(rec.getCat());
						chapterNode.add(catNode);
						subCatNode = null;
					}

					if (rec.getSubCat().compareTo("") != 0 && catNode != null
							&& lastrec.getSubCat().compareTo(rec.getSubCat()) != 0) {
						subCatNode = new DefaultMutableTreeNode(rec.getSubCat());
						catNode.add(subCatNode);
					}

					// add node to appropriate level
					if (subCatNode != null) {
						subCatNode.add(node);
					} else if (catNode != null) {
						catNode.add(node);
					} else if (chapterNode != null) {
						chapterNode.add(node);
					} else {
						rootNode.add(node);
					}
					lastrec = rec;
				}
				treeModel = new DefaultTreeModel(rootNode);
				tree.setModel(treeModel);

			} else if (viewBy.equals("origin")) {
				rootNode = new DefaultMutableTreeNode(book);
				Iterator<Recipe> it = rList.iterator();
				while (it.hasNext()) {

					Recipe rec = (Recipe) it.next();
					DefaultMutableTreeNode node = new DefaultMutableTreeNode(rec);

					// if group by chapter, but also should support group by region, and maybe
					// others
					if (originNode == null || lastrec.getOrigin().compareTo(rec.getOrigin()) != 0) {
						// add source node
						if (rec.getOrigin().compareTo("") == 0) {
							nodeText = "No Origin";
						} else {
							nodeText = rec.getOrigin();
						}
						originNode = new DefaultMutableTreeNode(nodeText);
						rootNode.add(originNode);
						chapterNode = null;
						catNode = null;
						subCatNode = null;
					}

					if (rec.getChapter().compareTo("") != 0 && originNode != null
							&& lastrec.getChapter().compareTo(rec.getChapter()) != 0) {
						// they are different, add chapter node
						chapterNode = new DefaultMutableTreeNode(rec.getChapter());
						originNode.add(chapterNode);
						catNode = null;
						subCatNode = null;
					}

					if (rec.getCat().compareTo("") != 0 && chapterNode != null
							&& lastrec.getCat().compareTo(rec.getCat()) != 0) {
						catNode = new DefaultMutableTreeNode(rec.getCat());
						chapterNode.add(catNode);
						subCatNode = null;
					}

					if (rec.getSubCat().compareTo("") != 0 && catNode != null
							&& lastrec.getSubCat().compareTo(rec.getSubCat()) != 0) {
						subCatNode = new DefaultMutableTreeNode(rec.getSubCat());
						catNode.add(subCatNode);
					}

					// add node to appropriate level
					if (subCatNode != null) {
						subCatNode.add(node);
					} else if (catNode != null) {
						catNode.add(node);
					} else if (chapterNode != null) {
						chapterNode.add(node);
					} else if (originNode != null) {
						originNode.add(node);
					} else {
						rootNode.add(node);
					}
					lastrec = rec;
				}
				treeModel = new DefaultTreeModel(rootNode);
				tree.setModel(treeModel);

			} else if (viewBy.equals("source")) {
				rootNode = new DefaultMutableTreeNode(book);
				Iterator<Recipe> it = rList.iterator();
				while (it.hasNext()) {

					Recipe rec = (Recipe) it.next();
					DefaultMutableTreeNode node = new DefaultMutableTreeNode(rec);

					// if group by chapter, but also should support group by region, and maybe
					// others
					if (sourceNode == null || lastrec.getSource().compareTo(rec.getSource()) != 0) {
						// add source node
						if (rec.getSource().compareTo("") == 0) {
							nodeText = "No Source";
						} else {
							nodeText = rec.getSource();
						}
						sourceNode = new DefaultMutableTreeNode(nodeText);
						rootNode.add(sourceNode);
						chapterNode = null;
						catNode = null;
						subCatNode = null;
					}

					if (rec.getChapter().compareTo("") != 0 && sourceNode != null
							&& lastrec.getChapter().compareTo(rec.getChapter()) != 0) {
						// they are different, add chapter node
						chapterNode = new DefaultMutableTreeNode(rec.getChapter());
						sourceNode.add(chapterNode);
						catNode = null;
						subCatNode = null;
					}

					if (rec.getCat().compareTo("") != 0 && chapterNode != null
							&& lastrec.getCat().compareTo(rec.getCat()) != 0) {
						catNode = new DefaultMutableTreeNode(rec.getCat());
						chapterNode.add(catNode);
						subCatNode = null;
					}

					if (rec.getSubCat().compareTo("") != 0 && catNode != null
							&& lastrec.getSubCat().compareTo(rec.getSubCat()) != 0) {
						subCatNode = new DefaultMutableTreeNode(rec.getSubCat());
						catNode.add(subCatNode);
					}

					// add node to appropriate level
					if (subCatNode != null) {
						subCatNode.add(node);
					} else if (catNode != null) {
						catNode.add(node);
					} else if (chapterNode != null) {
						chapterNode.add(node);
					} else if (sourceNode != null) {
						sourceNode.add(node);
					} else {
						rootNode.add(node);
					}
					lastrec = rec;
				}
				treeModel = new DefaultTreeModel(rootNode);
				tree.setModel(treeModel);

			} else if (viewBy.equals("recipe")) {
				rootNode = new DefaultMutableTreeNode(book);
				nMatch = new DefaultMutableTreeNode("Matching");
				nNoMatch = new DefaultMutableTreeNode("Non-Matching");
				Iterator<Recipe> it = rList.iterator();
				while (it.hasNext()) {
					Recipe rec = (Recipe) it.next();
					DefaultMutableTreeNode node = new DefaultMutableTreeNode(rec);
					if (filter.containsKey(rec.getUUID().toString())) {
						nMatch.add(node);
					} else {
						nNoMatch.add(node);
					}
				}
				if (nMatch.getChildCount() > 0) {
					rootNode.add(nMatch);
				}
				if (nNoMatch.getChildCount() > 0) {
					rootNode.add(nNoMatch);
				}
				treeModel = new DefaultTreeModel(rootNode);
				tree.setModel(treeModel);

				if (nMatch.getChildCount() > 0) {
					// select Matched root
					TreePath sel = new TreePath(treeModel.getPathToRoot(nMatch));
					tree.setSelectionPath(sel);
					tree.scrollPathToVisible(sel);
					tree.expandPath(sel);
				}

			} else {
				System.err.println("ViewBy=" + viewBy + " Not Handled.");
			}
		} else {
			// sort should be based on group by
			if (viewBy.equals("chapter")) {
				rootNode = new DefaultMutableTreeNode(book);
				Iterator<Recipe> it = rList.iterator();
				while (it.hasNext()) {

					Recipe rec = (Recipe) it.next();
					DefaultMutableTreeNode node = new DefaultMutableTreeNode(rec);

					// if group by chapter, but also should support group by region, and maybe
					// others
					if (chapterNode == null || lastrec.getChapter().compareTo(rec.getChapter()) != 0) {
						// add source node
						if (rec.getChapter().compareTo("") == 0) {
							nodeText = "No Chapter";
						} else {
							nodeText = rec.getChapter();
						}
						chapterNode = new DefaultMutableTreeNode(nodeText);
						rootNode.add(chapterNode);
						catNode = null;
						subCatNode = null;
					}

					if (rec.getCat().compareTo("") != 0 && chapterNode != null
							&& lastrec.getCat().compareTo(rec.getCat()) != 0) {
						catNode = new DefaultMutableTreeNode(rec.getCat());
						chapterNode.add(catNode);
						subCatNode = null;
					}

					if (rec.getSubCat().compareTo("") != 0 && catNode != null
							&& lastrec.getSubCat().compareTo(rec.getSubCat()) != 0) {
						subCatNode = new DefaultMutableTreeNode(rec.getSubCat());
						catNode.add(subCatNode);
					}

					// add node to appropriate level
					if (subCatNode != null) {
						subCatNode.add(node);
					} else if (catNode != null) {
						catNode.add(node);
					} else if (chapterNode != null) {
						chapterNode.add(node);
					} else {
						rootNode.add(node);
					}
					lastrec = rec;
				}
				treeModel = new DefaultTreeModel(rootNode);
				tree.setModel(treeModel);

			} else if (viewBy.equals("origin")) {
				rootNode = new DefaultMutableTreeNode(book);
				Iterator<Recipe> it = rList.iterator();
				while (it.hasNext()) {

					Recipe rec = (Recipe) it.next();
					DefaultMutableTreeNode node = new DefaultMutableTreeNode(rec);

					// if group by chapter, but also should support group by region, and maybe
					// others
					if (originNode == null || lastrec.getOrigin().compareTo(rec.getOrigin()) != 0) {
						// add source node
						if (rec.getOrigin().compareTo("") == 0) {
							nodeText = "No Origin";
						} else {
							nodeText = rec.getOrigin();
						}
						originNode = new DefaultMutableTreeNode(nodeText);
						rootNode.add(originNode);
						chapterNode = null;
						catNode = null;
						subCatNode = null;
					}

					if (rec.getChapter().compareTo("") != 0 && originNode != null
							&& lastrec.getChapter().compareTo(rec.getChapter()) != 0) {
						// they are different, add chapter node
						chapterNode = new DefaultMutableTreeNode(rec.getChapter());
						originNode.add(chapterNode);
						catNode = null;
						subCatNode = null;
					}

					if (rec.getCat().compareTo("") != 0 && chapterNode != null
							&& lastrec.getCat().compareTo(rec.getCat()) != 0) {
						catNode = new DefaultMutableTreeNode(rec.getCat());
						chapterNode.add(catNode);
						subCatNode = null;
					}

					if (rec.getSubCat().compareTo("") != 0 && catNode != null
							&& lastrec.getSubCat().compareTo(rec.getSubCat()) != 0) {
						subCatNode = new DefaultMutableTreeNode(rec.getSubCat());
						catNode.add(subCatNode);
					}

					// add node to appropriate level
					if (subCatNode != null) {
						subCatNode.add(node);
					} else if (catNode != null) {
						catNode.add(node);
					} else if (chapterNode != null) {
						chapterNode.add(node);
					} else if (originNode != null) {
						originNode.add(node);
					} else {
						rootNode.add(node);
					}
					lastrec = rec;
				}
				treeModel = new DefaultTreeModel(rootNode);
				tree.setModel(treeModel);

			} else if (viewBy.equals("source")) {
				rootNode = new DefaultMutableTreeNode(book);
				Iterator<Recipe> it = rList.iterator();
				while (it.hasNext()) {

					Recipe rec = (Recipe) it.next();
					DefaultMutableTreeNode node = new DefaultMutableTreeNode(rec);

					// if group by chapter, but also should support group by region, and maybe
					// others
					if (sourceNode == null || lastrec.getSource().compareTo(rec.getSource()) != 0) {
						// add source node
						if (rec.getSource().compareTo("") == 0) {
							nodeText = "No Source";
						} else {
							nodeText = rec.getSource();
						}
						sourceNode = new DefaultMutableTreeNode(nodeText);
						rootNode.add(sourceNode);
						chapterNode = null;
						catNode = null;
						subCatNode = null;
					}

					if (rec.getChapter().compareTo("") != 0 && sourceNode != null
							&& lastrec.getChapter().compareTo(rec.getChapter()) != 0) {
						// they are different, add chapter node
						chapterNode = new DefaultMutableTreeNode(rec.getChapter());
						sourceNode.add(chapterNode);
						catNode = null;
						subCatNode = null;
					}

					if (rec.getCat().compareTo("") != 0 && chapterNode != null
							&& lastrec.getCat().compareTo(rec.getCat()) != 0) {
						catNode = new DefaultMutableTreeNode(rec.getCat());
						chapterNode.add(catNode);
						subCatNode = null;
					}

					if (rec.getSubCat().compareTo("") != 0 && catNode != null
							&& lastrec.getSubCat().compareTo(rec.getSubCat()) != 0) {
						subCatNode = new DefaultMutableTreeNode(rec.getSubCat());
						catNode.add(subCatNode);
					}

					// add node to appropriate level
					if (subCatNode != null) {
						subCatNode.add(node);
					} else if (catNode != null) {
						catNode.add(node);
					} else if (chapterNode != null) {
						chapterNode.add(node);
					} else if (sourceNode != null) {
						sourceNode.add(node);
					} else {
						rootNode.add(node);
					}
					lastrec = rec;
				}
				treeModel = new DefaultTreeModel(rootNode);
				tree.setModel(treeModel);

			} else if (viewBy.equals("recipe")) {
				rootNode = new DefaultMutableTreeNode(book);
				Iterator<Recipe> it = rList.iterator();
				while (it.hasNext()) {
					Recipe rec = (Recipe) it.next();
					DefaultMutableTreeNode node = new DefaultMutableTreeNode(rec);
					rootNode.add(node);
				}
				treeModel = new DefaultTreeModel(rootNode);
				tree.setModel(treeModel);

			} else {
				System.err.println("ViewBy=" + viewBy + " Not Handled.");
			}
		}

	}

	/*
	 * 
	 * 
	 * HELPER CLASSES
	 * 
	 * 
	 */
	/**
	 * Used for sorting the jtree in various ways
	 */
	class TreeComparator implements Comparator<Recipe> {

		private String by;

		public TreeComparator(String by) {
			this.by = by;
		}

		public int compare(Recipe o1, Recipe o2) {

			Recipe r1 = (Recipe) o1;
			Recipe r2 = (Recipe) o2;

			int compareVal = 0;
			if (by.equals("chapter")) {
				compareVal = r1.getTreeAncestors(TreeViewBy.chapter).compareTo(r2.getTreeAncestors(TreeViewBy.chapter));
			} else if (by.equals("origin")) {
				compareVal = r1.getTreeAncestors(TreeViewBy.origin).compareTo(r2.getTreeAncestors(TreeViewBy.origin));
			} else if (by.equals("source")) {
				compareVal = r1.getTreeAncestors(TreeViewBy.source).compareTo(r2.getTreeAncestors(TreeViewBy.source));
			} else if (by.equals("recipeName")) {
				compareVal = r1.getTreeAncestors(TreeViewBy.recipeName)
						.compareTo(r2.getTreeAncestors(TreeViewBy.recipeName));
			}

			if (compareVal != 0) {
				return compareVal;
			}
			return 0; // they are the same
		}

		@Override
		public boolean equals(Object o) {
			if (!(o instanceof TreeComparator)) {
				return false;
			} else {
				return true;
			}
		}
	}
}

class TreeViewBy {

	static ArrayList<String> chapter = new ArrayList<String>();
	static ArrayList<String> origin = new ArrayList<String>();
	static ArrayList<String> source = new ArrayList<String>();
	static ArrayList<String> recipeName = new ArrayList<String>();

	static {
		chapter.add(new String("chapter"));
		chapter.add(new String("cat"));
		chapter.add(new String("subcat"));
		chapter.add(new String("recipeName"));

		origin.add(new String("origin"));
		origin.add(new String("chapter"));
		origin.add(new String("cat"));
		origin.add(new String("subcat"));
		origin.add(new String("recipeName"));

		source.add(new String("source"));
		source.add(new String("chapter"));
		source.add(new String("cat"));
		source.add(new String("subcat"));
		source.add(new String("recipeName"));

		recipeName.add(new String("recipeName"));

	}
}
