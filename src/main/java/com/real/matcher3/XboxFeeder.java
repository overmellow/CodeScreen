package com.real.matcher3;

import com.real.matcher.Matcher.CsvStream;
import com.real.matcher.Matcher.IdMapping;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class XboxFeeder implements Feeder {
	private InternalDB internalDB;
	private
	
	public XboxFeeder(InternalDB internalDB) {
		this.internalDB = internalDB;
	}

	@Override
	public List<IdMapping> match(CsvStream externalDb) {
		List<IdMapping> mappings = new ArrayList<IdMapping>();
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy hh:mm:ss a");
		externalDb.getDataRows()
			.map(s -> s.split(",(?=([^\\\"]*\\\"[^\\\"]*\\\")*[^\\\"]*$)"))
			.filter(distinctByKey(arr -> arr[2]))

			.forEach(arr -> {
				LocalDateTime dateTime = LocalDateTime.parse(arr[4], formatter);
				Movie m = new Movie(arr[3].replaceAll("\"", ""), String.valueOf(dateTime.getYear()));
				
				if(internalDB.getDBInverse().get(m) != null) {
					mappings.add(new IdMapping(internalDB.getDBInverse().get(m), arr[2]));
				}
			});
		return mappings;
	}

	public static <T> java.util.function.Predicate<T> distinctByKey(java.util.function.Function<? super T, Object> keyExtractor) {
		java.util.Map<Object, Boolean> seen = new java.util.concurrent.ConcurrentHashMap<>();
		return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
	}

}
