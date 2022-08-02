package com.lapsho.app;

import static org.junit.Assert.*;
import java.util.*;
import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class TopWordsTest
{
    @Test
    public void top3_Empty_ShouldReturnEmptyList()
    {
        String input = "";
        List<String> expected = new ArrayList<>();

        assertEquals("top3_Empty_ShouldReturnEmptyList", expected, TopWords.top3(input));
    }

    @Test
    public void top3_Null_ShouldReturnEmptyList()
    {
        String input = null;
        List<String> expected = new ArrayList<>();

        assertEquals("top3_Null_ShouldReturnEmptyList", expected, TopWords.top3(input));
    }

    @Test
    public void top3_RegularText() {
        String input = "In a village of La Mancha, the name of which I have no desire to call to" +
                "mind, there lived not long since one of those gentlemen that keep a lance" +
                "in the lance-rack, an old buckler, a lean hack, and a greyhound for" +
                "coursing. An olla of rather more beef than mutton, a salad on most" +
                "nights, scraps on Saturdays, lentils on Fridays, and a pigeon or so extra" +
                "on Sundays, made away with three-quarters of his income. ";
        List<String> expected = Arrays.asList("a", "of", "on");

        assertEquals("top3_RegularText", expected, TopWords.top3(input));
    }

    @Test
    public void top3_UpperCaseWord_ShouldBeConvertedToLowercase() {
        String input = "E E E E DDD ddd DdD: ddd ddd aa aA Aa, bb cc cC E e e";
        List<String> expected = Arrays.asList("e", "ddd", "aa");

        assertEquals("top3_UpperCaseWord_ShouldBeConvertedToLowercase", expected, TopWords.top3(input));
    }

    @Test
    public void top3_ApostrophesAtStartMiddleEnd_PartOfWord() {
        String input = " 'abc, 'abc, abc', 'abc', 'abc', 'abc',ab'c, ab'c";
        List<String> expected = Arrays.asList("'abc'", "'abc", "ab'c");

        assertEquals("top3_ApostrophesAtStartMiddleEnd_PartOfWord", expected, TopWords.top3(input));
    }

    @Test
    public void top3_Dash_NotPartOfWord() {
        String input = "lance-rack lance-rock lance rack lance rack lance rack";
        List<String> expected = Arrays.asList("lance", "rack", "rock");

        assertEquals("top3_Dash_NotPartOfWord", expected, TopWords.top3(input));
    }

    @Test
    public void top3_SpecialChars_NotPartOfWord() {
        String input = "lance#-rack? @lance! rock _lance &rack* $lance rac#k lance\" -rack+";
        List<String> expected = Arrays.asList("lance", "rack", "rock");

        assertEquals("top3_SpecialChars_NotPartOfWord", expected, TopWords.top3(input));
    }

    @Test
    public void top3_Numbers_NotPartOfWord() {
        String input = "lance12 lance lance rack3 rack rock 123 2";
        List<String> expected = Arrays.asList("lance", "rack", "rock");

        assertEquals("top3_Numbers_NotPartOfWord", expected, TopWords.top3(input));
    }

    @Test
    public void top3_OneWordString_ShouldReturnOneWordArray() {
        String input = "one";
        List<String> expected = Collections.singletonList("one");

        assertEquals("top3_OneWordString_ShouldReturnOneWordArray", expected, TopWords.top3(input));
    }

    @Test
    public void top3_TwoWordString_ShouldReturnTwoWordArray() {
        String input = "two word";
        List<String> expected = Arrays.asList("two", "word");

        assertEquals("top3_TwoWordString_ShouldReturnTwoWordArray", expected, TopWords.top3(input));
    }
}
