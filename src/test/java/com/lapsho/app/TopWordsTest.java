package com.lapsho.app;

import static org.junit.Assert.*;
import java.util.*;
import org.junit.Test;
import static org.hamcrest.core.AnyOf.*;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit test for simple App.
 */
public class TopWordsTest
{
    @Test
    public void top3_EmptyInput_ShouldReturnEmptyList()
    {
        String input = "";
        List<String> expected = new ArrayList<>();

        assertEquals("top3_Empty_ShouldReturnEmptyList", expected, TopWords.top3(input));
    }

    @Test
    public void top3_NullInput_ShouldReturnEmptyList()
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
        String input = " 'abc, 'abc, 'abc, abc', 'abc', 'abc', 'abc', 'abc',ab'c, ab'c";
        List<String> expected = Arrays.asList("'abc'", "'abc", "ab'c");

        assertEquals("top3_ApostrophesAtStartMiddleEnd_PartOfWord", expected, TopWords.top3(input));
    }

    @Test
    public void top3_FewApostrophesAtStartMiddleEnd_PartOfWord() {
        String input = "ab''c'c,  ab''c'c, ab''c'c,  ab''c'c, 'abc'', 'abc'', 'abc'', abc'', abc'', 'abc";
        List<String> expected = Arrays.asList("ab''c'c", "'abc''", "abc''");

        assertEquals("top3_ApostrophesAtStartMiddleEnd_PartOfWord", expected, TopWords.top3(input));
    }

    @Test
    public void top3_FewApostrophesAtBeginning_IgnoreExceptOne() {
        String input = "'''ab''c'c,  '''ab''c'c, '''ab''c'c,  '''ab''c'c, ''abc'', ''abc'', ''abc'', abc'', abc'', ''abc";
        List<String> expected = Arrays.asList("'ab''c'c", "'abc''", "abc''");

        assertEquals("top3_ApostrophesAtStartMiddleEnd_PartOfWord", expected, TopWords.top3(input));
    }

    @Test
    public void top3_SingleApostrophe_NotPartOfWord() {
        String input = "' ";
        List<String> expected = Arrays.asList();

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
        String input = "lance#-rack? @lance! rock _lance &rack* $lance rac#k lance\" -rack+ k";
        List<String> expected = Arrays.asList("lance", "rack", "k");

        assertEquals("top3_SpecialChars_NotPartOfWord", expected, TopWords.top3(input));
    }

    @Test
    public void top3_NumbersInWord_ShouldBeCut() {
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

    @Test
    public void top3_WordsWithSameMaxPriority_ShouldBeInResult() {
        String input = "one one two two three three word";
        List<String> expectedSequence1 = Arrays.asList("one", "two", "three");
        List<String> expectedSequence2 = Arrays.asList("two", "one", "three");
        List<String> expectedSequence3 = Arrays.asList("two", "three", "one");
        List<String> expectedSequence4 = Arrays.asList("one", "three", "two");
        List<String> expectedSequence5 = Arrays.asList("three", "one", "two");
        List<String> expectedSequence6 = Arrays.asList("three", "two", "one");

        assertThat(TopWords.top3(input), anyOf(
                equalTo(expectedSequence1),
                equalTo(expectedSequence2),
                equalTo(expectedSequence3),
                equalTo(expectedSequence4),
                equalTo(expectedSequence5),
                equalTo(expectedSequence6)
                ));
    }

    @Test
    public void top3_TwoWordsSecondInPriorityListButEqualByPriority_ShouldBeInResult() {
        String input = "one one one one two two two tree tree tree word word";
        List<String> expectedSequence1 = Arrays.asList("one", "two", "tree");
        List<String> expectedSequence2 = Arrays.asList("one", "tree", "two");
        List<String> result = TopWords.top3(input);

        assertThat(result, anyOf(equalTo(expectedSequence1), equalTo(expectedSequence2)));
    }

    @Test
    public void top3_TwoWordsFirstInPriorityListAndEqualByPriority_ShouldBeInResult() {
        String input = "one one one two two two two tree tree tree tree word word";
        List<String> expectedSequence1 = Arrays.asList( "two", "tree", "one");
        List<String> expectedSequence2 = Arrays.asList("tree", "two", "one");
        List<String> result = TopWords.top3(input);

        assertThat(result, anyOf(equalTo(expectedSequence1), equalTo(expectedSequence2)));
    }

    @Test
    public void top3_TwoWordsThirdInPriorityList_ResultShouldIncludeOneOfThem() {
        String input = "cYAsP wSEm_SBYLExN.cYAsP/Iczkn VdYltE Iczkn:EIM cdv cdv?EIM;Iczkn ctWe_VdYltE:KKLXxuvu EIM ctWe cYAsP,cYAsP.cYAsP-wSEm KKLXxuvu ZmQnsE_Iczkn VdYltE;SBYLExN ZmQnsE-WRMxHI xogizMz!LXHeXC EIM!WRMxHI xogizMz wSEm LXHeXC.xogizMz.VdYltE cdv Iczkn;ZmQnsE Iczkn.xogizMz VdYltE wSEm/cYAsP KKLXxuvu wSEm_xogizMz:cYAsP_LXHeXC:cYAsP SBYLExN.cdv TYANt-LXHeXC ZmQnsE_cdv_TYANt,VdYltE,cdv!Iczkn.TYANt;EIM-ctWe cYAsP cYAsP;WRMxHI cdv wSEm-LXHeXC.VdYltE WRMxHI,xogizMz,sepqTIOS KKLXxuvu Iczkn/SBYLExN EIM LXHeXC!cYAsP wSEm.xogizMz,cYAsP Iczkn-TYANt sepqTIOS cYAsP?VdYltE/cYAsP EIM cYAsP!TYANt;TYANt!Iczkn/ctWe ctWe!wSEm;VdYltE-cdv,EIM LXHeXC;cYAsP EIM wSEm EIM LXHeXC?EIM,SBYLExN Iczkn TYANt!EIM_WRMxHI:VdYltE wSEm/EIM xogizMz:TYANt SBYLExN!Iczkn/wSEm cdv.Iczkn/ctWe,cdv-Iczkn,WRMxHI,cdv WRMxHI_Iczkn VdYltE LXHeXC SBYLExN!EIM!wSEm cYAsP wSEm Iczkn/Iczkn xogizMz wSEm cdv xogizMz xogizMz;SBYLExN ZmQnsE cdv-wSEm wSEm;ZmQnsE_TYANt cYAsP WRMxHI WRMxHI xogizMz sepqTIOS?cYAsP Iczkn ZmQnsE LXHeXC TYANt TYANt_cdv?cdv cYAsP,xogizMz xogizMz EIM?wSEm cdv:cdv_xogizMz Iczkn/LXHeXC EIM LXHeXC/wSEm xogizMz Iczkn/EIM_wSEm-xogizMz!EIM.SBYLExN SBYLExN!xogizMz cYAsP ZmQnsE ZmQnsE.ctWe cYAsP cdv?wSEm,Iczkn Iczkn,xogizMz Iczkn VdYltE VdYltE;wSEm cYAsP cYAsP;cYAsP_SBYLExN.LXHeXC cdv.wSEm LXHeXC VdYltE EIM-SBYLExN WRMxHI-xogizMz wSEm KKLXxuvu sepqTIOS LXHeXC WRMxHI VdYltE:EIM EIM/SBYLExN cdv LXHeXC TYANt:WRMxHI_VdYltE-TYANt xogizMz LXHeXC xogizMz:cYAsP LXHeXC;LXHeXC wSEm_xogizMz/";
        List<String> expectedSequence1 = Arrays.asList("cyasp", "wsem", "iczkn");
        List<String> expectedSequence2 = Arrays.asList("cyasp", "wsem", "xogizmz");
        List<String> result = TopWords.top3(input);

        assertThat(result, anyOf(equalTo(expectedSequence1), equalTo(expectedSequence2)));
    }

    @Test
    public void top3_TwoWordsSecondInPriorityList_ResultShouldIncludeBothOfThem() {
        String input = "u'AZmXjYV MYg?bMEtGoSI QHJjWoU/bSM bSM QHJjWoU/YtfrbefP:ANHAPrG u'AZmXjYV?ANHAPrG/QHJjWoU:MYg dgPMMOe;MYg MYg MYg_bMEtGoSI dgPMMOe/YtfrbefP:MYg ANHAPrG YtfrbefP?bMEtGoSI bMEtGoSI QHJjWoU dgPMMOe bMEtGoSI.QHJjWoU;ANHAPrG u'AZmXjYV dgPMMOe,bMEtGoSI/YtfrbefP?MYg?dgPMMOe bMEtGoSI QHJjWoU dgPMMOe_QHJjWoU MYg,dgPMMOe.QHJjWoU;u'AZmXjYV-MYg bSM!dgPMMOe YtfrbefP YtfrbefP!MYg kNsn bSM:ANHAPrG dgPMMOe bMEtGoSI,YtfrbefP u'AZmXjYV:ANHAPrG bMEtGoSI;MYg MYg:kNsn YtfrbefP bSM/MYg kNsn-MYg YtfrbefP dgPMMOe_QHJjWoU!RVCps_QHJjWoU kNsn QHJjWoU!bMEtGoSI:QHJjWoU QHJjWoU QHJjWoU,kNsn QHJjWoU:bSM.YtfrbefP bMEtGoSI.dgPMMOe/bMEtGoSI!u'AZmXjYV!kNsn u'AZmXjYV,YtfrbefP u'AZmXjYV MYg u'AZmXjYV,bSM MYg_bMEtGoSI QHJjWoU YtfrbefP u'AZmXjYV bMEtGoSI dgPMMOe;dgPMMOe_bSM-dgPMMOe bMEtGoSI QHJjWoU YtfrbefP MYg;dgPMMOe.MYg/QHJjWoU ANHAPrG QHJjWoU QHJjWoU;bMEtGoSI kNsn u'AZmXjYV QHJjWoU dgPMMOe;dgPMMOe YtfrbefP/ANHAPrG ANHAPrG u'AZmXjYV dgPMMOe dgPMMOe/bMEtGoSI;YtfrbefP;kNsn,YtfrbefP MYg.bSM!ANHAPrG u'AZmXjYV!MYg.YtfrbefP u'AZmXjYV YtfrbefP!YtfrbefP bMEtGoSI QHJjWoU dgPMMOe YtfrbefP:bMEtGoSI_dgPMMOe bSM QHJjWoU!YtfrbefP-MYg bSM MYg:bMEtGoSI/ANHAPrG ANHAPrG?u'AZmXjYV bSM MYg;ANHAPrG YtfrbefP MYg!YtfrbefP kNsn bMEtGoSI-kNsn YtfrbefP u'AZmXjYV QHJjWoU,bMEtGoSI u'AZmXjYV:dgPMMOe-RVCps,dgPMMOe QHJjWoU u'AZmXjYV_MYg MYg u'AZmXjYV u'AZmXjYV bMEtGoSI;kNsn bMEtGoSI.dgPMMOe_YtfrbefP u'AZmXjYV kNsn dgPMMOe?u'AZmXjYV:MYg.ANHAPrG u'AZmXjYV,bSM bSM?ANHAPrG-bSM!dgPMMOe u'AZmXjYV MYg u'AZmXjYV?u'AZmXjYV dgPMMOe!kNsn_bMEtGoSI,u'AZmXjYV bMEtGoSI kNsn bSM QHJjWoU_bSM QHJjWoU bMEtGoSI?YtfrbefP?YtfrbefP;MYg?u'AZmXjYV.QHJjWoU dgPMMOe_";
        List<String> expectedSequence1 = Arrays.asList("myg", "u'azmxjyv", "qhjjwou");
        List<String> expectedSequence2 = Arrays.asList("myg", "qhjjwou", "u'azmxjyv");
        List<String> expectedSequence3 = Arrays.asList("myg", "dgpmmoe", "qhjjwou");
        List<String> expectedSequence4 = Arrays.asList("myg", "qhjjwou", "dgpmmoe");
        List<String> expectedSequence5 = Arrays.asList("myg", "qhjjwou", "u'azmxjyv");
        List<String> expectedSequence6 = Arrays.asList("myg", "u'azmxjyv", "qhjjwou");
        List<String> result = TopWords.top3(input);

        assertThat(result, anyOf(
                equalTo(expectedSequence1),
                equalTo(expectedSequence2),
                equalTo(expectedSequence3),
                equalTo(expectedSequence4),
                equalTo(expectedSequence5),
                equalTo(expectedSequence6)
        ));
    }
}
