package com.endlesscreator.tidimenslib;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void test() {
        String la = "1.56.";

        System.out.println("la = " + la.charAt(la.length()-1));
        System.out.println("la = " + Character.isDigit(la.charAt(la.length()-1)));


    }


}