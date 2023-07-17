package com.rednoblue.jRecipe.IO.Output;

//import java.util.*;
import java.util.Hashtable;

public abstract class O_FormatCreator {
	public static Hashtable<String, O_Factory> oFactories = new Hashtable<String, O_Factory>();

	protected static O_Interface createFormat(String name) throws O_FormatNotCreatedException {
		O_Factory o = (O_Factory) oFactories.get(name);
		if (o == null) {
			// format not found, try to load it
			try {
				System.err.println("Loading " + name);
				Class.forName("com.rednoblue.jRecipe.IO.Output." + name);
				// By now the static block with no function would
				// have been executed if the output format was found.
				// the output format is expected to have put its factory
				// in the hashtable.
				o = (O_Factory) oFactories.get(name);
				// couldn't load it
				if (o == null) {
					throw (new O_FormatNotCreatedException("Class.forName for " + name + ",but was not successful."));
				}
			} catch (ClassNotFoundException e) {
				throw (new O_FormatNotCreatedException());
			}
		}
		return (o.create());
	}
}