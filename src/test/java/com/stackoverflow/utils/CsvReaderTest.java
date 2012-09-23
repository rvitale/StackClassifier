package com.stackoverflow.utils;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import org.junit.Test;

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
		for (CsvLine line : reader) {
			assertEquals(CsvReader.numberOfColumns, line.columns().size());
			++iterations;
		}
		assertEquals(2, iterations);
	}

}
