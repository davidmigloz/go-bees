package com.davidmiguel.gobees.utils;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static org.junit.Assert.*;

/**
 * Test for HourAxisValueFormatter.
 */
public class HourAxisValueFormatterTest {
    @Test
    public void getFormattedValue() throws Exception {
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy kk:mm:ss", Locale.getDefault());
        Date t0 = df.parse("20/11/2016 20:00:00");
        Date t1 = df.parse("20/11/2016 21:00:00");

        HourAxisValueFormatter havf = new HourAxisValueFormatter(t0.getTime() / 1000);
        String result = havf.getFormattedValue((t1.getTime() - t0.getTime()) / 1000, null);
        assertTrue(result.equals("21:00"));
    }
}