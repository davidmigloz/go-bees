package com.davidmiguel.gobees.utils;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit tests for StringUtils.
 */
public class StringUtilsTest {

    @Test
    public void capitalize() throws Exception {
        String string = "abcd abcd.";
        String capitalized = StringUtils.capitalize(string);
        assertEquals("Abcd abcd.", capitalized);
    }
}