package com.rednoblue.jrecipe.fulltext;

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
import org.apache.lucene.queryparser.flexible.standard.StandardQueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.TopFieldDocs;
import org.apache.lucene.store.FSDirectory;

import com.google.inject.Inject;
import com.rednoblue.jrecipe.model.Book;
import com.rednoblue.jrecipe.prefs.Prefs;

public class RecipeIndexer {
	private final static Logger LOGGER = Logger.getLogger(RecipeIndexer.class.getName());
	public String indexLocation = "";
	public IndexerThread indexThread;
	private Book book;
	private Prefs prefs;
	
	@Inject
	public RecipeIndexer(Book book, Prefs prefs) {
		this.book = book;
		this.prefs = prefs;
	}

	/**
	 * Begin indexing the book of recipes
	 * 
	 * @param book of recipes
	 */
	public void indexRecipes() {
		indexThread = new IndexerThread(book, this, prefs);
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

	public boolean stillIndexing() {
		if (indexThread != null && indexThread.isAlive()) {
			return true;
		}
		return false;
	}
	
}
