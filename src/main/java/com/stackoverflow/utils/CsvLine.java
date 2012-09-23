package com.stackoverflow.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;


public class CsvLine {

	Map<String, Object> csv;

	public CsvLine() {
		csv = new HashMap<String, Object>();
	}
	
	public void put(String key, Object value) {
		csv.put(key, value);
	}
	
	public String get(String key) {
		return (String)csv.get(key);
	}
	
	
	@SuppressWarnings("unchecked")
	public <T> T get(String key, Class<T> type) {
		return (T) csv.get(key);
	}
	
	public Set<String> columns() {
		return csv.keySet();
	}

	public boolean containsColumn(String columnName) {
		return csv.containsKey(columnName);
	}
}
