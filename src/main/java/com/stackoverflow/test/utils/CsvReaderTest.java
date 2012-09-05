package com.stackoverflow.test.utils;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.junit.Test;

import com.stackoverflow.utils.CsvReader;

public class CsvReaderTest {


	@Test
	public void iterableTest() {
		CsvReader reader = null;
		try {
			reader = new CsvReader(new File("/home/riccardo/sample.csv"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		int iterations = 0;
		for (Map<String, String> line : reader) {
			assertEquals(CsvReader.NUMBER_OF_COLUMNS, line.keySet().size());
			++iterations;
		}
		assertEquals(2, iterations);
	}

}
