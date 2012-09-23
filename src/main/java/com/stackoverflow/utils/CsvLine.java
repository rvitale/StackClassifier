package com.stackoverflow.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;


public class CsvLine {

	Map<String, String> csv;

	public CsvLine() {
		csv = new HashMap<String, String>();
	}
	
	public void put(String key, String value) {
		csv.put(key, value);
	}
	
	public String get(String key) {
		return (String)csv.get(key);
	}
	
	public <T extends Number> T get(String key, Class<T> type) throws ClassCastException { 
		try {
			return type.getConstructor(String.class).newInstance(csv.get(key));
		} catch (Exception e) {
			throw new ClassCastException(e.getMessage());
		}
	}
	
	public Set<String> columns() {
		return csv.keySet();
	}

	public boolean containsColumn(String columnName) {
		return csv.containsKey(columnName);
	}
}
