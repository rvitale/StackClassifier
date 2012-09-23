package com.stackoverflow.utils;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class CsvLineTest {
	
	CsvLine line;
	
	@Before
	public void setUp() {
		line = new CsvLine();
		line.put("double", "2.3");
		line.put("int", "2");
		line.put("string", "test");
	}
	
	@Test
	public void csvLineTest() {
		assertEquals(new Double(2.3), line.get("double", Double.class));
		assertEquals(new Integer(2), line.get("int", Integer.class));
		assertEquals("test", line.get("string"));
	}

}
