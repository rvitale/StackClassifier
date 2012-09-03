package com.example.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class CsvReader implements Iterable<Map<String, Object>> {

	private File file;
	private BufferedReader reader;
	private String nextLine;

	public CsvReader(File file) throws IOException {
		this.file = file;
		
		this.nextLine = reader.readLine();
		
	}
	
	private void closeStream() {
		try {
			this.reader.close();
			this.reader = null;
		} catch (IOException e) {
			// Error while closing the file!
			// Nothing we can do! :(
		}
	}
	
	private void openStream() throws FileNotFoundException {
		this.reader = new BufferedReader(
				new FileReader(file));
	}
	
	private boolean isStreamClosed() {
		return this.reader == null;
	}
	
	public Iterator<Map<String, Object>> iterator() {
		return new Iterator<Map<String,Object>>() {
			
			public boolean hasNext() {
				if(isStreamClosed()) {
					return false;
				}
				
				if(nextLine == null) {
					closeStream();
					return false;
				}
				return true;
			}

			public Map<String, Object> next() {
				// Do we open the stream if we find it closed?
				try {
					String ret = nextLine;
					nextLine = reader.readLine();
					return new HashMap<String, Object>();
				} catch (IOException e) {
					closeStream();
					return null;
				}
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
