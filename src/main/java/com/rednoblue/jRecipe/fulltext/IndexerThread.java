package com.rednoblue.jrecipe.fulltext;

import java.io.File;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Iterator;
import java.util.logging.Logger;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.FSDirectory;

import com.google.inject.Inject;
import com.rednoblue.jrecipe.model.Book;
import com.rednoblue.jrecipe.model.Ingredient;
import com.rednoblue.jrecipe.model.Recipe;
import com.rednoblue.jrecipe.prefs.Prefs;

public class IndexerThread extends Thread {
	private final static Logger LOGGER = Logger.getLogger(IndexerThread.class.getName());
	private Book book;
	private RecipeIndexer recipeIndexer;
	private Prefs prefs;

	public IndexerThread(Book book, RecipeIndexer recipeIndexer, Prefs prefs) {
		super(IndexerThread.class.getName());
		this.book = book;
		this.recipeIndexer = recipeIndexer;
		this.prefs = prefs;
	}

	@Override
	public void run() {
		LOGGER.info("Indexing NOW!");
		try {
			File f = File.createTempFile("Temp1", "out", null);
			File tempDir = new File(f.getParent());
			f.delete();
			f = null;
			recipeIndexer.indexLocation = new String(tempDir.getAbsolutePath() + "/" + prefs.getAppname() + "_index");
			LOGGER.info("Index location " + recipeIndexer.indexLocation);
			
			File[] files = new File(recipeIndexer.indexLocation).listFiles();
			for (File file : files) {
				file.delete();
			}

			FSDirectory dir = FSDirectory.open(Paths.get(recipeIndexer.indexLocation));
			IndexWriterConfig config = new IndexWriterConfig(new StandardAnalyzer());
			IndexWriter writer = new IndexWriter(dir, config);

			Iterator<Recipe> it = book.getRecipes().iterator();
			while (it.hasNext()) {
				Recipe r = (Recipe) it.next();
				
				LOGGER.info("Indexing " + r.recipe_name);
				
				// index Recipe
				Document ldoc = new Document();
				ldoc.add(new StoredField("UUID", r.getUUID().toString()));
				ldoc.add(new TextField("Name", r.getRecipeName(), Field.Store.YES));
				ldoc.add(new TextField("Source", r.getSource(), Field.Store.YES));
				ldoc.add(new TextField("Chapter", r.getChapter(), Field.Store.YES));
				ldoc.add(new TextField("Cat", r.getCat(), Field.Store.YES));
				ldoc.add(new TextField("SubCat", r.getSubCat(), Field.Store.YES));
				ldoc.add(new TextField("Origin", r.getOrigin(), Field.Store.YES));
				ldoc.add(new TextField("Process", r.getProcess(), Field.Store.YES));
				ldoc.add(new TextField("Comments", r.getComments(), Field.Store.YES));
				Collection<Ingredient> ingList = r.getIngredientsList();
				Iterator<Ingredient> ingIt = ingList.iterator();
				while (ingIt.hasNext()) {
					Ingredient i = (Ingredient) ingIt.next();
					ldoc.add(new TextField("Ingredient", i.getName(), Field.Store.YES));
				}
				writer.addDocument(ldoc);
			}
			
			writer.close();
			LOGGER.info("Indexing complete");

		} catch (java.io.IOException ex) {
			LOGGER.severe(ex.toString());
			ex.printStackTrace();
		}
	}
}
