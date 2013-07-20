package test;

import static org.junit.Assert.*;
import org.junit.*;

import processor.Utility;

public class UtilityTest {
	@BeforeClass
	public static void testSetup() {
	}

	@AfterClass
	public static void testCleanup() {
	}

	@Test
	public void testIsSimilar() {
		assertTrue(Utility.isSimilar("I am 10 years old", "I am 10 *"));		
		assertTrue(Utility.isSimilar("I am 10 years old", "* 10 *"));
		assertTrue(Utility.isSimilar("I am 10 years old", "* 10 years old"));
		assertTrue(Utility.isSimilar("Mày đi đâu đó", "Mày đi đâu *"));		
		assertTrue(Utility.isSimilar("Mày đi đâu *", "Mày đi *"));
		
		assertFalse(Utility.isSimilar("Mày có đi đâu đó không", "Mày có * đó không vậy"));
		
		assertFalse(Utility.isSimilar("* đi đâu đó", "Mày đi đâu *"));
		assertFalse(Utility.isSimilar("I am 10 years old", "I am 10"));
	}
	
	@Test
	public void testRemoveUnwanterWord() {
		assertEquals(Utility.replaceUnwantedWord("Tôi/P muốn/V đi/V chơi./chơi.", "*"), "* muốn đi chơi.");
	}
	
	@Test
	public void testJoinString() {
		String[] strings = {"tôi", "muốn", "đi", "chơi"};
		assertEquals(Utility.joinString(strings, ' '), "tôi muốn đi chơi");
	}
}
