package com.davidmiguel.gobees.utils;

/**
 * String utils.
 */
public class StringUtils {

    /**
     * Capitalize firt letter of the string.
     *
     * @param string string.
     * @return capitalized string.
     */
    public static String capitalize(String string) {
        return string.substring(0, 1).toUpperCase() + string.substring(1);
    }
}
