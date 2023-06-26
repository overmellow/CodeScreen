package com.real.matcher3;

import com.real.matcher.Matcher.CsvStream;
import com.real.matcher.Matcher.IdMapping;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XboxFeeder implements Feeder {
	private InternalDB internalDB;
	Map<String, Integer> headerIndices = new HashMap<>();
	
	public XboxFeeder(InternalDB internalDB) {
		this.internalDB = internalDB;
	}

	@Override
	public List<IdMapping> match(CsvStream externalDb) {
		List<IdMapping> mappings = new ArrayList<IdMapping>();
		extractHeadersIndices(externalDb);
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy hh:mm:ss a");
		externalDb.getDataRows()
			.map(s -> s.split(",(?=([^\\\"]*\\\"[^\\\"]*\\\")*[^\\\"]*$)"))
			.filter(distinctByKey(arr -> arr[headerIndices.get("MediaId")]))
			.forEach(arr -> {
				LocalDateTime dateTime = LocalDateTime.parse(arr[headerIndices.get("OriginalReleaseDate")], formatter);
				Movie m = new Movie(arr[headerIndices.get("Title")].replaceAll("\"", ""), String.valueOf(dateTime.getYear()));
				
				if(internalDB.getDBInverse().get(m) != null) {
					mappings.add(new IdMapping(internalDB.getDBInverse().get(m), arr[headerIndices.get("MediaId")]));
				}
			});
		return mappings;
	}

	public static <T> java.util.function.Predicate<T> distinctByKey(java.util.function.Function<? super T, Object> keyExtractor) {
		java.util.Map<Object, Boolean> seen = new java.util.concurrent.ConcurrentHashMap<>();
		return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
	}

	private void extractHeadersIndices(CsvStream externalDb) {
		String[] headers = externalDb.getHeaderRow().split(",");
		for (int i = 0; i < headers.length; i++) {
			headerIndices.put(headers[i], i);
		}
	}
}
