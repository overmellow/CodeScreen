package com.real.matcher;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.real.matcher.Matcher.CsvStream;
import com.real.matcher.Matcher.DatabaseType;
import com.real.matcher.Matcher.IdMapping;

public class MyMatcherImpl extends MatcherImpl {
	private static final Logger LOGGER = LoggerFactory.getLogger(MatcherImpl.class);
	private static BiMap<Integer, Movie> moviesBiMap = HashBiMap.create();
	private static Map<Integer, Movie> movies = new HashMap<Integer, Movie>();

	public MyMatcherImpl(CsvStream movieDb, CsvStream actorAndDirectorDb) {
		super(movieDb, actorAndDirectorDb);
	    LOGGER.info("importing database");
	    // TODO implement me
	    System.out.println(movieDb.getHeaderRow());
	    
	    movies = movieDb.getDataRows()
				.map(s -> s.split(","))
				.collect(HashMap::new, (map, arr) -> map.put(Integer.parseInt(arr[0]), new Movie(Integer.parseInt(arr[0]), arr[1].replaceAll("\"", ""), arr[2])), HashMap::putAll);
	    for (Map.Entry<Integer, Movie> entry : movies.entrySet()) {
			moviesBiMap.put(entry.getKey(), entry.getValue());
		}
	    System.out.println("///");
		moviesBiMap.entrySet().forEach(m -> System.out.println(m.hashCode() + " : " + m));
		System.out.println(moviesBiMap.inverse().get(new Movie("Sherlock Holmes", "2009")));
	    System.out.println(actorAndDirectorDb.getHeaderRow());
	    actorAndDirectorDb.getDataRows().map(s -> s.split(","))
	    	.forEach(arr -> {
	    			Movie m;
	    			if((m = movies.get(Integer.parseInt(arr[0]))) != null) {
	    				if(arr[2].equals("cast")) { 
	    					m.getCasts().add(arr[1]);
	    				}
	    				else {
	    					m.getDirectors().add(arr[1]);
	    				}
	    			}
	    		});
	    	
//	    movies.forEach((name, obj) -> System.out.println(name + ": " + obj));

//	    List<Movie> movies = movieDb.getDataRows()
//	    					.map(s -> s.split(","))
//	    					.map(arr -> new Movie(Integer.parseInt(arr[0]), arr[1], arr[2]))
//							.collect(Collectors.toList());
//	    movies.entrySet().forEach(System.out::println);
	    LOGGER.info("database imported");
	  }

	  @Override
	  public List<IdMapping> match(DatabaseType databaseType, CsvStream externalDb) {
	    // TODO implement me

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy H:mm");
		List<IdMapping> mappings = new ArrayList<Matcher.IdMapping>();
		System.out.println(externalDb.getHeaderRow());
//		System.out.println();
//		externalDb.getDataRows().forEach(System.out::println);
		externalDb.getDataRows().map(s -> s.split(","))
			.forEach(arr -> {
				LocalDateTime dateTime = LocalDateTime.parse(arr[4], formatter);
				System.out.println(dateTime.getYear());
//				System.out.println(movies.get(arr[3]);
				Movie m = new Movie(arr[3], String.valueOf(dateTime.getYear()));
				System.out.println(m.hashCode());
				System.out.println("key: " + moviesBiMap.inverse().get(m));
				if(moviesBiMap.inverse().get(m) != null) {
					mappings.add(new IdMapping(moviesBiMap.inverse().get(m), arr[2]));	
				}
				
				
			});
		return mappings;
//	    return Collections.emptyList();
	  }

}
