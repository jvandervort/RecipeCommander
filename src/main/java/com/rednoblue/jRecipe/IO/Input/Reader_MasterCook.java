package com.rednoblue.jrecipe.io.input;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.regex.Pattern;

import com.rednoblue.jrecipe.io.MyFileFilter;
import com.rednoblue.jrecipe.model.Book;
import com.rednoblue.jrecipe.model.Ingredient;
import com.rednoblue.jrecipe.model.Recipe;

class Reader_MasterCook implements I_Interface {
	// extension info
	static private final String formatName = "MasterCook";
	static private final String fileExtension = "txt";
	static private final String fileDescription = formatName + " Files";

	// ingredient objects
	private ArrayList<Ingredient> iList;
	// recipes in book
	private ArrayList<Recipe> rList;
	// recipe, gets pushed onto Collection
	private Recipe rec;
	// Ingredient, gets pushed onto Collection
	private Ingredient ingred;
	// book
	private Book book;

	public Reader_MasterCook() {

	}

	public Reader_MasterCook(String argFileName) {
		try {
			java.io.Reader reader = new java.io.FileReader(argFileName);
			// java.io.Reader r = new FileReader(new File(argFileName));
			parseSource(reader);
		} catch (java.io.IOException e) {
			System.err.println("ERROR: File Not Found");
			// handle FileNotFoundExcept, etc. here
		}
	}

	public void parseSource(java.io.Reader reader) {
		final int recStart = 1;
		final int recTitle = 2;
		final int recCat = 3;
		// final int recYield=4;
		final int recIngredients = 4;
		final int recDirections = 5;
		final int recEnd = 0;

		try {
			BufferedReader bf = new BufferedReader(reader);

			book = new Book();
			rList = new ArrayList<Recipe>();
			Recipe rec = null;

			book.setBookName("MasterCook book");

			// while read line
			String line;

			Pattern pRecStart = Pattern.compile("\\* +Exported from  MasterCook +\\*");
			Pattern pTitle = Pattern.compile("[A-Za-z]");
			Pattern pCategories = Pattern.compile("^Categories    :(.*)$");
			Pattern pStartIngred = Pattern.compile("^--------  ------------  --------------------------------");
			Pattern pIngredient = Pattern.compile("^(.{8})  (.{12})  (.+)$");
			Pattern pEndIngred = Pattern.compile("^$");
			Pattern pRecEnd = Pattern.compile("^                   - - - - - - - - - - - - - - - - - - $");
			java.util.regex.Matcher m;
			boolean b;

			int stage = recEnd;
			while ((line = bf.readLine()) != null) {
				// System.err.println(line);
				// Read line, check for end-of-file

				m = pRecStart.matcher(line);
				b = m.find();
				if (b == true) {
					// new rec
					System.err.println("pRecStart Marker");
					stage = recStart;
					// if ( rec !=null ) {
					// rList.add(rec);
					// }
					rec = new Recipe();
					continue;
				}

				if (stage == recStart) {
					m = pTitle.matcher(line);
					b = m.find();
					if (b == true) {
						// Title:
						stage = recCat;
						rec.setRecipeName(line.trim());
						System.err.println("Title: " + rec.getRecipeName());
						continue;
					}
				}

				if (stage == recCat) {
					m = pCategories.matcher(line);
					b = m.find();
					if (b == true) {
						// Categories:
						String cat = m.group(1);

						Pattern pGetFirstCat = Pattern.compile("([A-Za-z/ ]+), ");
						m = pGetFirstCat.matcher(cat);
						b = m.find();
						if (b == true) {
							// System.err.println(m.group(1));
							cat = m.group(1);
						}
						if (cat.length() > 0) {
							System.err.println("Cat: " + cat);
							rec.setChapter(cat);
						}
						// System.err.println("Categories: " + m.group(1).trim());
						continue;
					}
				}

				if (stage <= recIngredients) {
					// start of ingredient section
					m = pStartIngred.matcher(line);
					b = m.find();
					if (b == true) {
						System.err.println("pStartIngred Marker");
						stage = recIngredients;
						continue;
					}
					if (stage == recIngredients) {
						m = pEndIngred.matcher(line);
						b = m.find();
						if (b == true) {
							System.err.println("pEndIngred Marker");
							stage = recDirections;
							continue;
						}
						// Ingredient:
						m = pIngredient.matcher(line);
						b = m.find();
						if (b == true) {
							String name = m.group(3).trim();
							String amount = m.group(1).trim();
							String units = m.group(2).trim();

							System.err.println("Ingredient1 ");
							System.err.println("   Name=" + name);
							System.err.println("   Amount=" + amount);
							System.err.println("   Units=" + units);

							Ingredient ingred = new Ingredient();
							ingred.setName(name);
							ingred.setAmount(Ingredient.parseAmount(amount));
							ingred.setUnits(units);
							rec.addIngredient(ingred);

							continue;
						}
					}

				}
				if (stage >= recIngredients) {
					String tline;
					String rline;

					m = pRecEnd.matcher(line);
					b = m.find();
					if (b == true) {
						// end rec
						System.err.println("pRecEnd Marker");
						stage = recStart;
						if (rec != null) {
							rList.add(rec);
						}
						continue;
					}
					System.err.println("Add Directions");
					stage = recDirections;
					rline = rec.getProcess();
					tline = line.trim();
					tline = tline.replaceAll("\\.", ".<br>");
					rec.setProcess(rline + " " + tline);
				}

			}

			bf.close();
			book.setRecipes(rList);

		} catch (java.io.IOException e) {
			System.err.println("ERROR: File Not Found");
			// handle FileNotFoundExcept, etc. here
		}

	}

	public String getFormatName() {
		return formatName;
	}

	public Book getBook() {
		return book;
	}

	public MyFileFilter getChoosableFileFilter() {
		return new MyFileFilter(fileExtension, fileDescription);
	}

	public boolean isFileMine(String tenlines) {
		Pattern p = Pattern.compile("MasterCook");
		if (p.matcher(tenlines).find() == true) {
			return true;
		} else {
			return false;
		}
	}

	static class MasterCookReaderFactory extends I_Factory {
		public I_Interface create() {
			return (new Reader_MasterCook());
		}
	}

	static {
		I_FormatCreator.iFactories.put("Reader_MasterCook", new MasterCookReaderFactory());
	}

}
