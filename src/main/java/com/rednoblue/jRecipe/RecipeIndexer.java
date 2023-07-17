package com.rednoblue.jRecipe;

import java.io.File;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Logger;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.StoredFields;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.queryparser.flexible.standard.StandardQueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.TopFieldDocs;
import org.apache.lucene.store.FSDirectory;

import com.rednoblue.jRecipe.model.Book;
import com.rednoblue.jRecipe.model.Ingredient;
import com.rednoblue.jRecipe.model.Recipe;

/**
 * Provides indexing services to the GUI. Handles indexing and searching. The
 * indexing is done in separate thread so it shouldn't affect the GUI.
 * 
 * @author John Vandervort
 * @version 1.0
 */
public class RecipeIndexer {
	private final static Logger LOGGER = Logger.getLogger(RecipeIndexer.class.getName());
	private String indexLocation = "";
	public IndexerThread indexThread;

	public RecipeIndexer() {

	}

	/**
	 * Begin indexing the book of recipes
	 * 
	 * @param book of recipes
	 */
	public void indexRecipes(Book book) {
		indexThread = new IndexerThread(book);
		indexThread.start();
	}

	public HashMap<String,String> searchRecipes(Collection<String> fields, String querystr) {
		HashMap<String, String> returnList = new HashMap<String, String>();
		try {
			FSDirectory dir = FSDirectory.open(Paths.get(indexLocation));
			IndexReader reader = DirectoryReader.open(dir);
			IndexSearcher searcher = new IndexSearcher(reader);
			Analyzer analyzer = new StandardAnalyzer();

			if (querystr == null || querystr.length() == 0) {
				analyzer.close();
				return null;
			}
			StandardQueryParser qp = new StandardQueryParser();
			if (fields.size() > 0) {
				StringBuffer buf = new StringBuffer();
				Iterator<String> it = fields.iterator();
				while (it.hasNext()) {
					String s = (String) it.next();
					buf.append(s + ":" + querystr + " OR ");
				}
				querystr = buf.toString().substring(0, buf.length() - 4);
			}
			LOGGER.info(querystr);

			Query query = qp.parse(querystr, "defaultField");
			TopFieldDocs hits = searcher.search(query, 100000, Sort.RELEVANCE);

			LOGGER.info(hits.totalHits + " total matching documents");

			final ScoreDoc[] scoreDocs = hits.scoreDocs;
			StoredFields storedFields = searcher.storedFields();
			for (ScoreDoc scoreDoc : scoreDocs) {
				Document doc = storedFields.document(scoreDoc.doc);
				String name = doc.get("Name");
				String uuid = doc.get("UUID");
				if (name != null) {
					LOGGER.info("Matched " + name);
					returnList.put(uuid, "1");
				}
			}
			analyzer.close();

		} catch (Exception e) {
			LOGGER.severe(e.toString());
			e.printStackTrace();
		}
		return returnList;
	}

	public class IndexerThread extends Thread {

		Book book;

		public IndexerThread(Book book) {
			super(IndexerThread.class.getName());
			this.book = book;
		}

		@Override
		public void run() {
			LOGGER.info("Indexing NOW!");
			try {
				File f = File.createTempFile("Temp1", "out", null);
				File tempDir = new File(f.getParent());
				f.delete();
				f = null;
				indexLocation = new String(tempDir.getAbsolutePath() + "/" + Global.appName + "_index");

				File[] files = new File(indexLocation).listFiles();
				for (File file : files) {
					file.delete();
				}

				FSDirectory dir = FSDirectory.open(Paths.get(indexLocation));
				IndexWriterConfig config = new IndexWriterConfig(new StandardAnalyzer());
				IndexWriter writer = new IndexWriter(dir, config);

				Iterator<Recipe> it = book.getRecipes().iterator();
				while (it.hasNext()) {
					Recipe r = (Recipe) it.next();
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
				// writer.optimize();
				writer.close();
				LOGGER.info("Indexing complete");

			} catch (java.io.IOException ex) {
				LOGGER.severe(ex.toString());
				ex.printStackTrace();
			}
		}
	}
}
