package com.rednoblue.jRecipe;

import com.rednoblue.jRecipe.model.Book;
import com.rednoblue.jRecipe.model.Recipe;
import com.rednoblue.jRecipe.model.Ingredient;


import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.*;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.search.*;
import org.apache.lucene.queryParser.*;

//import org.apache.lucene.search.Hits;

import java.io.File;
import java.util.*;

import java.util.logging.Logger;

/**
 * Provides indexing services to the GUI.  Handles indexing and searching.  The indexing 
 * is done in separate thread so it shouldn't affect the GUI.
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
   * @param book of recipes
   */
  public void indexRecipes(Book book) {
    indexThread = new IndexerThread(book);
    indexThread.start();
  }

  public HashMap searchRecipes(Collection fields, String querystr) {
    HashMap<String, String> returnList = new HashMap<String, String>();
    try {
      Searcher searcher = new IndexSearcher(FSDirectory.open(new File(indexLocation)), true);
      Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_CURRENT);

      if (querystr == null || querystr.length() == 0) {
        searcher.close();
        return null;
      }
      QueryParser qp = new QueryParser("recipeName", analyzer);

      if (fields.size() > 0) {
        StringBuffer buf = new StringBuffer();
        Iterator it = fields.iterator();
        while (it.hasNext()) {
          String s = (String) it.next();
          buf.append(s + ":" + querystr + " OR ");
        }
        querystr = buf.toString().substring(0, buf.length() - 4);
      }

      LOGGER.info(querystr);

      Query query = qp.parse(querystr);
      TopFieldDocs hits = searcher.search(query, null, 100000, Sort.RELEVANCE);
      
      LOGGER.info(hits.totalHits + " total matching documents");
      
      for (int i = 0; i < hits.totalHits; i += 1) {

        ScoreDoc[] sd = hits.scoreDocs;
        // get the actual document with stored fields
        Document doc = searcher.doc(sd[i].doc);
        String name = doc.get("recipeName");
        String uuid = doc.get("UUID");
        if (name != null) {
          returnList.put(uuid, "1");
          LOGGER.info(i + ". " + name);
        }
      }
      searcher.close();

    } catch (Exception e) {
      LOGGER.severe(e.toString());
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
        IndexWriter writer = new IndexWriter(FSDirectory.open(new File(indexLocation)), new StandardAnalyzer(Version.LUCENE_CURRENT), true, IndexWriter.MaxFieldLength.UNLIMITED);

        Iterator it = book.getRecipes().iterator();
        while (it.hasNext()) {
          Recipe r = (Recipe) it.next();
          // index Recipe
          Document ldoc = new Document();
          ldoc.add(new Field("UUID", r.getUUID().toString(), Field.Store.YES, Field.Index.NOT_ANALYZED));
          ldoc.add(new Field("recipeName", r.getRecipeName(), Field.Store.YES, Field.Index.ANALYZED));
          ldoc.add(new Field("source", r.getSource(), Field.Store.YES, Field.Index.ANALYZED));
          ldoc.add(new Field("chapter", r.getChapter(), Field.Store.YES, Field.Index.NOT_ANALYZED));
          ldoc.add(new Field("cat", r.getCat(), Field.Store.YES, Field.Index.NOT_ANALYZED));
          ldoc.add(new Field("subcat", r.getSubCat(), Field.Store.YES, Field.Index.NOT_ANALYZED));
          ldoc.add(new Field("origin", r.getOrigin(), Field.Store.YES, Field.Index.ANALYZED));
          ldoc.add(new Field("process", r.getProcess(), Field.Store.YES, Field.Index.ANALYZED));
          ldoc.add(new Field("comments", r.getComments(), Field.Store.YES, Field.Index.ANALYZED));
          Collection ingList = r.getIngredientsList();
          Iterator ingIt = ingList.iterator();
          while (ingIt.hasNext()) {
            Ingredient i = (Ingredient) ingIt.next();
            ldoc.add(new Field("ingredient", i.getName(), Field.Store.YES, Field.Index.ANALYZED));
          }
          writer.addDocument(ldoc);
        }
        writer.optimize();
        writer.close();
        LOGGER.info("Indexing complete");

      } catch (java.io.IOException ex) {
        LOGGER.severe(ex.toString());
      }
    }
  }
}
