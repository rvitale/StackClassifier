package com.stackoverflow.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class CsvReader implements Iterable<Map<String, String>> {

	public static final int NUMBER_OF_COLUMNS = 15;
	
	private File file;
	private BufferedReader reader;
	private String[] columns;

	public CsvReader(File file) throws IOException {
		int i = 0;
		this.file = file;
		openStream();
	}
	
	private void closeStream() {
		try {
			reader.close();
			reader = null;
		} catch (IOException e) {
			// Error while closing the file!
			// Nothing we can do! :(
		}
	}
	
	private void openStream() throws IOException {
		reader = new BufferedReader(
				new FileReader(file));
		String header;
		if ((header = reader.readLine()) != null && !header.isEmpty()) {
			System.out.println(header);
			columns = header.split(",");
			System.out.println(Arrays.toString(columns));
		} else {
			// TODO: throw exception (file empty)
		}
		
		if (columns.length != NUMBER_OF_COLUMNS) {
			// TODO: throw exception (bad format)
		}
	}
	
	private boolean isStreamClosed() {
		return reader == null;
	}
	
	public Iterator<Map<String, String>> iterator() {
		return new Iterator<Map<String, String>>() {
			
			private String nextLine;

			public boolean hasNext() {
				if(isStreamClosed()) {
					return false;
				}
				
				try {
					// Pre-loading line: if it is null there are no more lines to read.
					// Otherwise the line will be preloaded for the next() operation.
					if((nextLine = reader.readLine()) == null) {
						closeStream();
						return false;
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				return true;
			}

			public Map<String, String> next() {
				// Do we open the stream if we find it closed?
				int colIndex = 0, lastComma = 0, numberOfQuotes = 0;
				String carry = "";  // Used for multiline fields.
				Map<String, String> record = new HashMap<String, String>();
				
				// Stopping when we fill all the columns or the nextLine is null.
				while (colIndex < NUMBER_OF_COLUMNS && nextLine != null) {
					for (int i = 0; i < nextLine.length(); ++i) {
						if (nextLine.charAt(i) == '"') {
							++numberOfQuotes;
						}
						// We should ignore this step if the current numberOfQuotes
						// is odd. That is, if there is any open double quote opened
						// we should just keep going.
						if ((numberOfQuotes & 1) == 0 &&
							(nextLine.charAt(i) == ',' || i == nextLine.length()-1)) {
							i = (colIndex + 1 < NUMBER_OF_COLUMNS) ? i : i + 1;
							record.put(columns[colIndex++],
								carry + nextLine.substring(lastComma, i));
							// Prepare variables for next field.
							lastComma = i + 1;
							if (!carry.equals("")) {
								carry = "";
							}
							numberOfQuotes = 0;
						}
					}
					// We haven't filled all the columns but the line 
					if (colIndex < NUMBER_OF_COLUMNS) {
						// If we haven't consumed all the line, put the remaining chunk in a carry.
						if (lastComma != nextLine.length()) {
							carry += nextLine.substring(lastComma);
						}
						try {
							// Fetch next line.
							nextLine = reader.readLine();
						} catch (IOException e) {
							System.out.println("error");
							closeStream();
							return null;
						}
						lastComma = 0;
					}
				}
				
				// Sanity check: verify that we have filled all the columns.
				if (record.keySet().size() != NUMBER_OF_COLUMNS) {
					// TODO: throw exception (unexpected end of file or malformed record).
				}
				return record;
			}

			public void remove() {
				try {
					throw new UnsupportedOperationException();
				} finally {
					closeStream();
				}
			}
			
		};
	}
}
