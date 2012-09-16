package com.stackoverflow.utils;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;

import org.junit.Test;

import com.stackoverflow.utils.CsvReader;

public class CsvReaderTest {

	@Test
	public void iterableTest() {
		CsvReader reader = null;
		try {
			File testFile = new File(this.getClass()
				.getResource("/com/stackoverflow/utils/sample.csv")
				.toURI());
			reader = new CsvReader(testFile);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		int iterations = 0;
		for (Map<String, String> line : reader) {
			assertEquals(CsvReader.numberOfColumns, line.keySet().size());
			++iterations;
		}
		assertEquals(2, iterations);
	}

}
