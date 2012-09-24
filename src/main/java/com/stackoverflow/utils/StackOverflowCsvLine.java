package com.stackoverflow.utils;

import java.util.HashSet;
import java.util.Set;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class StackOverflowCsvLine extends CsvLine {
	
	private DateTime parseDate;
	private Set<String> aggregates;
	
	public StackOverflowCsvLine() {
		super();
		parseDate = new DateTime();
		aggregates = new HashSet<String>();
		aggregates.add("BodyLength");
		aggregates.add("NumTags");
		aggregates.add("TitleLength");
		aggregates.add("UserAge");
	}

	private int checkAggregates(String key) {
		if(key.equals("BodyLength")) {
			return get("BodyMarkdown").length();
		} else if(key.equals("NumTags")) {
			int numTags = 0;
			for(int i=1; i<=5; i++) {
				String tag = get(String.format("Tag%d", i));
				if(!tag.trim().isEmpty()) {
					++numTags;
				}
			}
			return numTags;
		} else if(key.equals("TitleLength")) {
			return get("Title").length();
		} else if(key.equals("UserAge")) {
			DateTimeFormatter format = DateTimeFormat.forPattern("MM/dd/yyyy kk:mm:ss");
			DateTime creationDate = format.parseDateTime(get("OwnerCreationDate"));
			Interval interval = new Interval(creationDate, parseDate);
			Period period = interval.toPeriod();
			return period.getHours();
		}
		return -1;  // No such aggregate field. 
	}
	
	@Override
	public String get(String key) {
		if (aggregates.contains(key)) {
			return String.format("%d", checkAggregates(key));
		}
		return super.get(key);
	}

	@Override
	public <T extends Number> T get(String key, Class<T> type)
			throws ClassCastException {
		if (aggregates.contains(key)) {
			try {
				return type.getConstructor(String.class).newInstance(checkAggregates(key));
			} catch (Exception e) {
				throw new ClassCastException(e.getMessage());
			}
		}
		return super.get(key, type);
	}

	
	
}
