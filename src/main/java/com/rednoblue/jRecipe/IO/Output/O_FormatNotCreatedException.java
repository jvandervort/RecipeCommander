package com.rednoblue.jRecipe.IO.Output;

/**
 *
 * @author jcv9868
 */
public class O_FormatNotCreatedException extends Exception {
    O_FormatNotCreatedException() {
        super("O_FormatNotCreatedException!");
    }
    O_FormatNotCreatedException(String msg) {
        super("O_FormatNotCreatedException: " + msg);
    }
    
}
