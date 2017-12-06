package com.bparent.improPhoto.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class AsciiConstants {

    private static final Boolean XXXX = true;
    private static final Boolean ____ = false;
    /*
        System.out.println("###### ############ ############ ############ ############ ############ ############ ############ ############ ############ ############");
        System.out.println("###### ###      ### #####   #### ###     #### ####     ### ######    ## ##        ## ###      ### ##        ## ###      ### ###      ###");
        System.out.println("###### ##  ####  ## ####    #### ##  ###  ### ###  ###  ## #####  #  ## ##  ######## ##  ######## #######  ### ##  ####  ## ##  ####  ##");
        System.out.println("###### ##  ####  ## ###  #  #### ######  #### ########  ## ####  ##  ## ##  ######## ##  ######## ######  #### ##  ####  ## ##  ####  ##");
        System.out.println("###### ##  ####  ## ######  #### #####  ##### ######   ### ###  ###  ## ##       ### ##       ### #####  ##### ###      ### ###       ##");
        System.out.println("###### ##  ####  ## ######  #### ####  ###### ########  ## ##        ## ########  ## ##  ####  ## ####  ###### ##  ####  ## ########  ##");
        System.out.println("###### ##  ####  ## ######  #### ###  ####### ###  ###  ## ########  ## ########  ## ##  ####  ## ###  ####### ##  ####  ## ########  ##");
        System.out.println("##  ## ###      ### ####      ## ##       ### ####     ### ########  ## ##       ### ###      ### ##  ######## ###      ### ###      ###");
        System.out.println("###### ############ ############ ############ ############ ############ ############ ############ ############ ############ ############");
     */
    public static Map<String, Boolean[][]> asciiNumberMap;
    static {
        Map<String, Boolean[][]> aMap = new HashMap<>();

        aMap.put(".", new Boolean[][] {
                { XXXX, XXXX, XXXX, XXXX },
                { XXXX, XXXX, XXXX, XXXX },
                { XXXX, XXXX, XXXX, XXXX },
                { XXXX, XXXX, XXXX, XXXX },
                { XXXX, XXXX, XXXX, XXXX },
                { XXXX, XXXX, XXXX, XXXX },
                { XXXX, XXXX, XXXX, XXXX },
                { XXXX, ____, ____, XXXX },
                { XXXX, XXXX, XXXX, XXXX }
        });
        aMap.put(":", new Boolean[][] {
                { XXXX, XXXX, XXXX, XXXX },
                { XXXX, XXXX, XXXX, XXXX },
                { XXXX, XXXX, XXXX, XXXX },
                { XXXX, XXXX, XXXX, XXXX },
                { XXXX, ____, ____, XXXX },
                { XXXX, XXXX, XXXX, XXXX },
                { XXXX, XXXX, XXXX, XXXX },
                { XXXX, ____, ____, XXXX },
                { XXXX, XXXX, XXXX, XXXX }
        });
        aMap.put("0", new Boolean[][] {
                { XXXX, XXXX, XXXX, XXXX, XXXX, XXXX, XXXX, XXXX, XXXX, XXXX },
                { XXXX, XXXX, ____, ____, ____, ____, ____, ____, XXXX, XXXX },
                { XXXX, ____, ____, XXXX, XXXX, XXXX, XXXX, ____, ____, XXXX },
                { XXXX, ____, ____, XXXX, XXXX, XXXX, XXXX, ____, ____, XXXX },
                { XXXX, ____, ____, XXXX, XXXX, XXXX, XXXX, ____, ____, XXXX },
                { XXXX, ____, ____, XXXX, XXXX, XXXX, XXXX, ____, ____, XXXX },
                { XXXX, ____, ____, XXXX, XXXX, XXXX, XXXX, ____, ____, XXXX },
                { XXXX, XXXX, ____, ____, ____, ____, ____, ____, XXXX, XXXX },
                { XXXX, XXXX, XXXX, XXXX, XXXX, XXXX, XXXX, XXXX, XXXX, XXXX }
        });
        aMap.put("1", new Boolean[][] {
                { XXXX, XXXX, XXXX, XXXX, XXXX, XXXX, XXXX, XXXX, XXXX, XXXX },
                { XXXX, XXXX, XXXX, XXXX, ____, ____, ____, XXXX, XXXX, XXXX },
                { XXXX, XXXX, XXXX, ____, ____, ____, ____, XXXX, XXXX, XXXX },
                { XXXX, XXXX, ____, ____, XXXX, ____, ____, XXXX, XXXX, XXXX },
                { XXXX, XXXX, XXXX, XXXX, XXXX, ____, ____, XXXX, XXXX, XXXX },
                { XXXX, XXXX, XXXX, XXXX, XXXX, ____, ____, XXXX, XXXX, XXXX },
                { XXXX, XXXX, XXXX, XXXX, XXXX, ____, ____, XXXX, XXXX, XXXX },
                { XXXX, XXXX, XXXX, ____, ____, ____, ____, ____, ____, XXXX },
                { XXXX, XXXX, XXXX, XXXX, XXXX, XXXX, XXXX, XXXX, XXXX, XXXX }
        });
        aMap.put("2", new Boolean[][] {
                { XXXX, XXXX, XXXX, XXXX, XXXX, XXXX, XXXX, XXXX, XXXX, XXXX },
                { XXXX, XXXX, ____, ____, ____, ____, ____, XXXX, XXXX, XXXX },
                { XXXX, ____, ____, XXXX, XXXX, XXXX, ____, ____, XXXX, XXXX },
                { XXXX, XXXX, XXXX, XXXX, XXXX, ____, ____, XXXX, XXXX, XXXX },
                { XXXX, XXXX, XXXX, XXXX, ____, ____, XXXX, XXXX, XXXX, XXXX },
                { XXXX, XXXX, XXXX, ____, ____, XXXX, XXXX, XXXX, XXXX, XXXX },
                { XXXX, XXXX, ____, ____, XXXX, XXXX, XXXX, XXXX, XXXX, XXXX },
                { XXXX, ____, ____, ____, ____, ____, ____, ____, XXXX, XXXX },
                { XXXX, XXXX, XXXX, XXXX, XXXX, XXXX, XXXX, XXXX, XXXX, XXXX }
        });
        aMap.put("3", new Boolean[][] {
                { XXXX, XXXX, XXXX, XXXX, XXXX, XXXX, XXXX, XXXX, XXXX, XXXX },
                { XXXX, XXXX, XXXX, ____, ____, ____, ____, ____, XXXX, XXXX },
                { XXXX, XXXX, ____, ____, XXXX, XXXX, XXXX, ____, ____, XXXX },
                { XXXX, XXXX, XXXX, XXXX, XXXX, XXXX, XXXX, ____, ____, XXXX },
                { XXXX, XXXX, XXXX, XXXX, XXXX, ____, ____, ____, XXXX, XXXX },
                { XXXX, XXXX, XXXX, XXXX, XXXX, XXXX, XXXX, ____, ____, XXXX },
                { XXXX, XXXX, ____, ____, XXXX, XXXX, XXXX, ____, ____, XXXX },
                { XXXX, XXXX, XXXX, ____, ____, ____, ____, ____, XXXX, XXXX },
                { XXXX, XXXX, XXXX, XXXX, XXXX, XXXX, XXXX, XXXX, XXXX, XXXX }
        });
        aMap.put("4", new Boolean[][] {
                { XXXX, XXXX, XXXX, XXXX, XXXX, XXXX, XXXX, XXXX, XXXX, XXXX },
                { XXXX, XXXX, XXXX, XXXX, XXXX, ____, ____, ____, ____, XXXX },
                { XXXX, XXXX, XXXX, XXXX, ____, ____, XXXX, ____, ____, XXXX },
                { XXXX, XXXX, XXXX, ____, ____, XXXX, XXXX, ____, ____, XXXX },
                { XXXX, XXXX, ____, ____, XXXX, XXXX, XXXX, ____, ____, XXXX },
                { XXXX, XXXX, ____, ____, ____, ____, ____, ____, ____, XXXX },
                { XXXX, XXXX, XXXX, XXXX, XXXX, XXXX, XXXX, ____, ____, XXXX },
                { XXXX, XXXX, XXXX, XXXX, XXXX, XXXX, XXXX, ____, ____, XXXX },
                { XXXX, XXXX, XXXX, XXXX, XXXX, XXXX, XXXX, XXXX, XXXX, XXXX }
        });
        aMap.put("5", new Boolean[][] {
                { XXXX, XXXX, XXXX, XXXX, XXXX, XXXX, XXXX, XXXX, XXXX, XXXX },
                { XXXX, ____, ____, ____, ____, ____, ____, ____, ____, XXXX },
                { XXXX, ____, ____, XXXX, XXXX, XXXX, XXXX, XXXX, XXXX, XXXX },
                { XXXX, ____, ____, XXXX, XXXX, XXXX, XXXX, XXXX, XXXX, XXXX },
                { XXXX, ____, ____, ____, ____, ____, ____, ____, XXXX, XXXX },
                { XXXX, XXXX, XXXX, XXXX, XXXX, XXXX, XXXX, ____, ____, XXXX },
                { XXXX, XXXX, XXXX, XXXX, XXXX, XXXX, XXXX, ____, ____, XXXX },
                { XXXX, ____, ____, ____, ____, ____, ____, ____, XXXX, XXXX },
                { XXXX, XXXX, XXXX, XXXX, XXXX, XXXX, XXXX, XXXX, XXXX, XXXX }
        });
        aMap.put("6", new Boolean[][] {
                { XXXX, XXXX, XXXX, XXXX, XXXX, XXXX, XXXX, XXXX, XXXX, XXXX },
                { XXXX, XXXX, ____, ____, ____, ____, ____, ____, XXXX, XXXX },
                { XXXX, ____, ____, XXXX, XXXX, XXXX, XXXX, XXXX, XXXX, XXXX },
                { XXXX, ____, ____, XXXX, XXXX, XXXX, XXXX, XXXX, XXXX, XXXX },
                { XXXX, ____, ____, ____, ____, ____, ____, ____, XXXX, XXXX },
                { XXXX, ____, ____, XXXX, XXXX, XXXX, XXXX, ____, ____, XXXX },
                { XXXX, ____, ____, XXXX, XXXX, XXXX, XXXX, ____, ____, XXXX },
                { XXXX, XXXX, ____, ____, ____, ____, ____, ____, XXXX, XXXX },
                { XXXX, XXXX, XXXX, XXXX, XXXX, XXXX, XXXX, XXXX, XXXX, XXXX }
        });
        aMap.put("7", new Boolean[][] {
                { XXXX, XXXX, XXXX, XXXX, XXXX, XXXX, XXXX, XXXX, XXXX, XXXX },
                { XXXX, ____, ____, ____, ____, ____, ____, ____, ____, XXXX },
                { XXXX, XXXX, XXXX, XXXX, XXXX, XXXX, ____, ____, XXXX, XXXX },
                { XXXX, XXXX, XXXX, XXXX, XXXX, ____, ____, XXXX, XXXX, XXXX },
                { XXXX, XXXX, XXXX, XXXX, ____, ____, XXXX, XXXX, XXXX, XXXX },
                { XXXX, XXXX, XXXX, ____, ____, XXXX, XXXX, XXXX, XXXX, XXXX },
                { XXXX, XXXX, ____, ____, XXXX, XXXX, XXXX, XXXX, XXXX, XXXX },
                { XXXX, ____, ____, XXXX, XXXX, XXXX, XXXX, XXXX, XXXX, XXXX },
                { XXXX, XXXX, XXXX, XXXX, XXXX, XXXX, XXXX, XXXX, XXXX, XXXX }
        });
        aMap.put("8", new Boolean[][] {
                { XXXX, XXXX, XXXX, XXXX, XXXX, XXXX, XXXX, XXXX, XXXX, XXXX },
                { XXXX, XXXX, ____, ____, ____, ____, ____, ____, XXXX, XXXX },
                { XXXX, ____, ____, XXXX, XXXX, XXXX, XXXX, ____, ____, XXXX },
                { XXXX, ____, ____, XXXX, XXXX, XXXX, XXXX, ____, ____, XXXX },
                { XXXX, XXXX, ____, ____, ____, ____, ____, ____, XXXX, XXXX },
                { XXXX, ____, ____, XXXX, XXXX, XXXX, XXXX, ____, ____, XXXX },
                { XXXX, ____, ____, XXXX, XXXX, XXXX, XXXX, ____, ____, XXXX },
                { XXXX, XXXX, ____, ____, ____, ____, ____, ____, XXXX, XXXX },
                { XXXX, XXXX, XXXX, XXXX, XXXX, XXXX, XXXX, XXXX, XXXX, XXXX }
        });
        aMap.put("9", new Boolean[][] {
                { XXXX, XXXX, XXXX, XXXX, XXXX, XXXX, XXXX, XXXX, XXXX, XXXX },
                { XXXX, XXXX, ____, ____, ____, ____, ____, ____, XXXX, XXXX },
                { XXXX, ____, ____, XXXX, XXXX, XXXX, XXXX, ____, ____, XXXX },
                { XXXX, ____, ____, XXXX, XXXX, XXXX, XXXX, ____, ____, XXXX },
                { XXXX, XXXX, ____, ____, ____, ____, ____, ____, ____, XXXX },
                { XXXX, XXXX, XXXX, XXXX, XXXX, XXXX, XXXX, ____, ____, XXXX },
                { XXXX, XXXX, XXXX, XXXX, XXXX, XXXX, XXXX, ____, ____, XXXX },
                { XXXX, XXXX, ____, ____, ____, ____, ____, ____, XXXX, XXXX },
                { XXXX, XXXX, XXXX, XXXX, XXXX, XXXX, XXXX, XXXX, XXXX, XXXX }
        });

        asciiNumberMap = Collections.unmodifiableMap(aMap);
    }

}
