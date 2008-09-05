package com.rednoblue.jRecipe.model;

import java.util.regex.*;
import java.util.logging.Logger;

/**
 * Data model: contained by recipes
 */
public class Ingredient {
    private final static Logger LOGGER = Logger.getLogger(Ingredient.class.getName());
    private String name = "";
    private Float amount = new Float(0.0);
    private String units = "";
    
    /**
     *  Creates a new instance of Ingredient
     *
     *@since
     */
    public Ingredient() {
    }
    public Ingredient(String arg) {
        this();
        this.parseIngredientString(arg);
    }
    
    public String getName() {
        return name;
    }
    public void setName(String arg) {
        name = arg;
    }
    
    public Float getAmount() {
        return amount;
    }
    public String getAmountString() {
        Pattern p = Pattern.compile("([0-9]+).([0-9]+)");
        Matcher m = p.matcher(amount.toString());
        
        boolean b = m.find();
        if ( b == true) {
            String part1 = m.group(1);
            String part2 = m.group(2);
            
            while ( part2.length() < 3 ) {
                part2 += "0";
            }
            
            String fraction;
            String part2short = part2.substring(0,3);
            
            if ( part2short.equals("500") ) {
                fraction="1/2";
            } else if ( part2short.equals("250") ) {
                fraction="1/4";
            } else if ( part2short.equals("750") ) {
                fraction="3/4";
            } else if ( part2short.equals("333") ) {
                fraction="1/3";
            } else if ( part2short.equals("666") ) {
                fraction="2/3";
            } else if ( part2short.equals("200") ) {
                fraction="1/5";
            } else if ( part2short.equals("400") ) {
                fraction="2/5";
            } else if ( part2short.equals("600") ) {
                fraction="3/5";
            } else if ( part2short.equals("800") ) {
                fraction="4/5";
            } else if ( part2short.equals("125") ) {
                fraction="1/8";
            } else if ( part2short.equals("375") ) {
                fraction="3/8";
            } else if ( part2short.equals("625") ) {
                fraction="5/8";
            } else if ( part2short.equals("875") ) {
                fraction="7/8";
            } else {
                fraction="";
            }
            
            if ( fraction.length() == 0 ) {
                return part1;
            } else {
                if ( part1.equals("0") ) {
                    return fraction;
                } else {
                    return part1 + "-" + fraction;
                }
            }
        } else {
            return amount.toString();
        }
    }
    public void setAmount(Float arg) {
        amount = arg;
    }
    public void setAmount(String arg) {
        setAmount(parseAmount(arg));
    }
    
    public String getUnits() {
        return units;
    }
    public void setUnits(String arg) {
        units = arg;
    }
    
  @Override
    public boolean equals(Object obj) {
        
        Ingredient ingred = (Ingredient)obj;
        
        if ( this.getAmount().equals(ingred.getAmount()) == false ) { return false; }
        if ( this.getUnits().equals(ingred.getUnits()) == false ) { return false; }
        if ( this.getName().equals(ingred.getName()) == false ) { return false; }
        
        return true;
    }
    
    public Ingredient duplicate() {
        Ingredient newingred = new Ingredient();
        
        newingred.setName(this.getName());
        newingred.setAmount(this.getAmount());
        newingred.setUnits(this.getUnits());
        
        return newingred;
    }
    
    static public Float parseAmount(String arg) {
        Matcher m;
        boolean b;
        Float retval=null;
        
        Pattern pIntFrac = Pattern.compile("([0-9.]*)[ -]*(([0-9]+)/([0-9]+))");
        m = pIntFrac.matcher(arg);
        b = m.find();
        if ( b == true) {
            float ip = 0;
            if ( m.group(1).length() > 0 ) {
                ip = Float.parseFloat(m.group(1));
            }
            
            if ( m.group(3) != null && m.group(4) != null ) {
                float np = Float.parseFloat(m.group(3));
                float dp = Float.parseFloat(m.group(4));
                float tot = np / dp + ip;
                retval= new Float(tot);
            } else {
                retval= new Float(ip);
            }
        } else {
            // check for fraction only
            Pattern pFracOnly = Pattern.compile("([0-9]+)/([0-9]+)*");
            m = pFracOnly.matcher(arg);
            b = m.find();
            if ( b == true) {
                if ( m.group(1) != null && m.group(2) != null ) {
                    float np = Float.parseFloat(m.group(1));
                    float dp = Float.parseFloat(m.group(2));
                    float tot = np / dp;
                    retval= new Float(tot);
                }
            } else {
                // didn't match anything, try to set it.
                try {
                    retval= new Float(Float.parseFloat(arg));
                } catch ( NumberFormatException e ) {
                    LOGGER.warning("Not a valid amount(" + arg + ")");
                    retval=new Float(0.0);
                }
            }
        }
        return retval;
    }
    
    private boolean parseIngredientString(String arg) {
        Pattern p;
        Matcher m;
        boolean b;
        arg = arg.trim();
        
        // check for an ingredient only statement
        p = Pattern.compile("^([A-Za-z]+)");
        m = p.matcher(arg);
        if ( m.find() == true) {
            name=arg;
            LOGGER.fine("0 Amount " + this.toString());
            return true;
        }
        
        // check for an ingredient with amount, units, and name
        p = Pattern.compile("^([\\S-/]+) +(\\S+) +(.*)$");
        m = p.matcher(arg);
        b = m.find();
        if ( b == true && m.groupCount() == 3 ) {
            amount = parseAmount(m.group(1));
            units = m.group(2);
            name = m.group(3);
            LOGGER.fine("Non-0 Amount " + this.toString());
            return true;
        }
        return false;
    }

  @Override
    public String toString() {
        return Ingredient.class.getName() + " name=" + this.name + "\tAmountString=" + this.getAmountString() + "\tUnits=" + this.units + "\n";
    }
    
}