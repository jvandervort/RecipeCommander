package com.rednoblue.jrecipe.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import com.rednoblue.jrecipe.model.Book;
import com.rednoblue.jrecipe.model.Recipe;

public class RecipeTreeLoader {

	public RecipeTreeLoader() {
	}

	/**
	 * Loads the jtree on the main screen. Includes support for filtering and
	 * sorting.
	 * 
	 * @param tree   from the mainframe
	 * @param viewBy various facets of sorting/viewing
	 * @param filter lucene filtered grouping
	 * @param book   recipe book
	 */
	public void loadTree(JTree tree, String viewBy, HashMap<String, String> filter, Book book) {
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
		Collections.sort(rList, new RecipeTreeComparator(viewBy));

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

}
