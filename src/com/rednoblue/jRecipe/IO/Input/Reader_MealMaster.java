package com.rednoblue.jRecipe.IO.Input;

import com.rednoblue.jRecipe.IO.*;
import com.rednoblue.jRecipe.model.Book;
import com.rednoblue.jRecipe.model.Ingredient;
import com.rednoblue.jRecipe.model.Recipe;
import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.regex.Pattern;
import org.xml.sax.*;

class Reader_MealMaster implements I_Interface {
    // extension info
    static private final String formatName = "Meal-Master";
    static private final String fileExtension = "mmf";
    static private final String fileDescription = formatName + " Files";
    
    // Collection of ingredient objects
    private ArrayList<Ingredient> iList;
    // Collection of recipes in book
    private ArrayList<Recipe> rList;
    // recipe, gets pushed onto Collection
    private Recipe rec;
    // Ingredient, gets pushed onto Collection
    private Ingredient ingred;
    // book
    private Book book;
    
    public Reader_MealMaster() {
        
    }
    
    public Reader_MealMaster(String argFileName) {
        try {
            java.io.Reader reader = new java.io.FileReader(argFileName);
            //java.io.Reader r = new FileReader(new File(argFileName));
            parseSource(reader);
        }
        catch (java.io.IOException e) {
            System.err.println("ERROR: File Not Found");
            // handle FileNotFoundExcept, etc. here
        }
    }
    
    
    public void parseSource(java.io.Reader reader) {
        
        final int recStart=1;
        final int recTitle=2;
        final int recCat=3;
        final int recYield=4;
        final int recIngredients = 5;
        final int recDirections = 6;
        final int recEnd = 0;
        
        try {
            BufferedReader bf = new BufferedReader(reader);
            
            book = new Book();
            rList = new ArrayList<Recipe>();
            Recipe rec = null;
            
            book.setBookName("mmf book");
            
            // while read line
            String line;
            
            Pattern pRecStart = Pattern.compile("^-----.*Meal-Master.*");
            Pattern pRecEnd = Pattern.compile("^-----$");
            Pattern pTitle = Pattern.compile("^.*Title: (.*) *$");
            Pattern pCategories = Pattern.compile("^.*Categories: (.*) *$");
            Pattern pYield = Pattern.compile("^.*Yield: (.*) *$");
            Pattern pIngredient = Pattern.compile("^.{39}   ");
            //Pattern pIngredient1 = Pattern.compile(" *(.*) +([a-z]{1,2}) +([A-Z].*)   ");
            Pattern pIngredient1 = Pattern.compile("^(.{7}) ([a-z]{1,2}) (.{29})");
            Pattern pIngredient2 = Pattern.compile("^.{41}(.{7}) ([a-z]{1,2}) (.*)");
            //Pattern pIngredient2 = Pattern.compile(".{40} *(.*) +([a-z]{1,2}) +([A-Z].*)");
            java.util.regex.Matcher m;
            boolean b;
            
            int stage = recEnd;
            while ((line = bf.readLine()) != null) {
                //System.err.println("          " + line);
                // Read line, check for end-of-file
                if ( stage < recStart ) {
                    m = pRecStart.matcher(line);
                    b = m.find();
                    if ( b==true ) {
                        // new rec
                        //System.err.println("pRecStart Marker");
                        stage = recStart;
                        rec = new Recipe();
                        continue;
                    }
                }
                
                if ( stage<recTitle ) {
                    m = pTitle.matcher(line);
                    b = m.find();
                    if ( b == true ) {
                        // Title:
                        stage=recTitle;
                        rec.setRecipeName(m.group(1).trim());
                        //System.err.println("Title: " + rec.getRecipeName());
                        continue;
                    }
                }
                
                if ( stage<recCat ) {
                    m = pCategories.matcher(line);
                    b = m.find();
                    if ( b == true ) {
                        // Categories:
                        stage=recCat;
                        String cat = m.group(1);
                        
                        Pattern pGetFirstCat = Pattern.compile("([A-Za-z/ ]+), ");
                        m = pGetFirstCat.matcher(cat);
                        b = m.find();
                        if ( b == true ) {
                            //System.err.println(m.group(1));
                            cat=m.group(1);
                        }
                        
                        //System.err.println(cat);
                        rec.setChapter(cat);
                        
                        //System.err.println("Categories: " + m.group(1).trim());
                        continue;
                    }
                }
                
                
                if ( stage<recYield ) {
                    m = pYield.matcher(line);
                    b = m.find();
                    if ( b == true ) {
                        // Categories:
                        stage=recYield;
                        //System.err.println("Yield: " + m.group(1).trim());
                        continue;
                    }
                }
                
                if ( stage<=recIngredients ) {
                    m = pIngredient.matcher(line);
                    b = m.find();
                    if ( b == true ) {
                        // Ingredient1:
                        m = pIngredient1.matcher(line);
                        b = m.find();
                        if ( b == true ) {
                            String name=m.group(3).trim();
                            String amount=m.group(1).trim();
                            String units=m.group(2).trim();
                            
                                   /*
                                   System.err.println("Ingredient1 ");
                                   System.err.println("   Name=" + name);
                                   System.err.println("   Amount=" + amount);
                                   System.err.println("   Units=" + units);
                                    */
                            
                            stage=recIngredients;
                            
                            
                            Ingredient ingred = new Ingredient();
                            ingred.setName(name);
                            ingred.setAmount(Ingredient.parseAmount(amount));
                            ingred.setUnits(units);
                            rec.addIngredient(ingred);
                        }
                        
                        // Ingredient2:
                        
                        m = pIngredient2.matcher(line);
                        b = m.find();
                        if ( b == true ) {
                            String name=m.group(3).trim();
                            String amount=m.group(1).trim();
                            String units=m.group(2).trim();
                            
                                   /*
                                   System.err.println("Ingredient2 ");
                                   System.err.println("   Name=" + name);
                                   System.err.println("   Amount=" + amount);
                                   System.err.println("   Units=" + units);
                                    */
                            
                            Ingredient ingred = new Ingredient();
                            ingred.setName(name);
                            ingred.setAmount(Ingredient.parseAmount(amount));
                            ingred.setUnits(units);
                            rec.addIngredient(ingred);
                        }
                        
                        continue;
                    }
                }
                
                if ( stage>=recIngredients ) {
                    String tline;
                    String rline;
                    
                    m = pRecEnd.matcher(line);
                    b = m.find();
                    if ( b == true ) {
                        // end rec
                        //System.err.println("pRecEnd Marker");
                        stage=recEnd;
                        if ( rec != null ) {
                            rList.add(rec);
                        }
                        continue;
                    }
                    stage=recDirections;
                    rline = rec.getProcess();
                    tline = line.trim();
                    tline = tline.replaceAll("\\.",".<br>");
                    rec.setProcess(rline + " " + tline);
                }
            }
            
            bf.close();
            book.setRecipes(rList);
            
            // already close a stream when you are done with it
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
        Pattern p = Pattern.compile("Meal-Master");
        if (p.matcher(tenlines).find() == true ) {
            return true;
        } else {
            return false;
        }
    }
    
    
   static class MealMasterReaderFactory extends I_Factory {
          public I_Interface create() {
               return(new Reader_MealMaster());
          }
     }
     
     static {
          I_FormatCreator.iFactories.put("Reader_MealMaster", new MealMasterReaderFactory());
     }
     
    
}



