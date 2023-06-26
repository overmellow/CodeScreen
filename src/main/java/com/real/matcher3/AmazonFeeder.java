package com.real.matcher3;

import com.real.matcher.Matcher.CsvStream;
import com.real.matcher.Matcher.IdMapping;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class AmazonFeeder implements Feeder {
	private InternalDB internalDB;
	
	public AmazonFeeder(InternalDB internalDB) {
		this.internalDB = internalDB;
	}

	@Override
	public List<IdMapping> match(CsvStream externalDb) {
		List<IdMapping> mappings = new ArrayList<IdMapping>();
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy H:mm");
		externalDb.getDataRows().map(s -> s.split(","))
		.forEach(arr -> {
			LocalDateTime dateTime = LocalDateTime.parse(arr[4], formatter);
//			System.out.println(movies.get(arr[3]);
//			Movie m = new Movie(arr[3], String.valueOf(dateTime.getYear()));
//			System.out.println(m.hashCode());
//			System.out.println("key: " + moviesBiMap.inverse().get(m));
//			if(internalDB.getDBInverse().get(m) != null) {
//				mappings.add(new IdMapping(internalDB.getDBInverse().get(m), arr[2]));
//			}
			
			
		});
		return mappings;
	}
	
	

}
