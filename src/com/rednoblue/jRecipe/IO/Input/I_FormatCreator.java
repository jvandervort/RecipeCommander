package com.rednoblue.jRecipe.IO.Input;

import com.rednoblue.jRecipe.*;
import java.util.Hashtable;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author John Vandervort
 * @version 1.0
 */
public abstract class I_FormatCreator {
    public static Hashtable<String,I_Factory> iFactories = new Hashtable<String,I_Factory>();
        
    protected static I_Interface createFormat(String name) throws InputFormatNotCreatedException {
        Logger.getLogger(I_FormatCreator.class.getName()).log(Level.INFO, name);
        I_Factory i = (I_Factory) iFactories.get(name);
        if(i == null) {
            // format not found
            try {
                Class.forName("com.rednoblue.jRecipe.IO.Input."+name);
                // By now the static block with no function would
                // have been executed if the shape was found.
                // the shape is expected to have put its factory
                // in the hashtable.
                
                i = (I_Factory) iFactories.get(name);
                if(i == null) {
                    // if the input factory is not there even now
                    throw (new InputFormatNotCreatedException());
                }
            } catch(ClassNotFoundException e) {
                // We'll throw an exception to indicate that
                // the shape could not be created
                throw(new InputFormatNotCreatedException());
            }
        }
        return(i.create());
    }
}