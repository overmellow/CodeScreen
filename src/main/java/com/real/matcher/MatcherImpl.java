package com.real.matcher;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MatcherImpl implements Matcher {

  private static final Logger LOGGER = LoggerFactory.getLogger(MatcherImpl.class);

  public MatcherImpl(CsvStream movieDb, CsvStream actorAndDirectorDb) {
    LOGGER.info("importing database");
    // TODO implement me
    System.out.println(movieDb.getHeaderRow());
    
    Map<String, Movie> movies = movieDb.getDataRows()
			.map(s -> s.split(","))
			.collect(HashMap::new, (map, arr) -> map.put(arr[0], new Movie(arr[0], arr[1].replaceAll("\"", ""), arr[2])), HashMap::putAll);
    
    System.out.println(actorAndDirectorDb.getHeaderRow());
    actorAndDirectorDb.getDataRows().map(s -> s.split(","))
    	.forEach(arr -> {
    			Movie m;
    			if((m = movies.get(arr[0])) != null) {
    				if(arr[2].equals("cast")) { 
    					m.getCasts().add(arr[1]);
    				}
    				else {
    					m.getDirectors().add(arr[1]);
    				}
    			}
    		});
    	
    movies.forEach((name, obj) -> System.out.println(name + ": " + obj));

//    List<Movie> movies = movieDb.getDataRows()
//    					.map(s -> s.split(","))
//    					.map(arr -> new Movie(Integer.parseInt(arr[0]), arr[1], arr[2]))
//						.collect(Collectors.toList());
//    movies.entrySet().forEach(System.out::println);
    LOGGER.info("database imported");
  }

  @Override
  public List<IdMapping> match(DatabaseType databaseType, CsvStream externalDb) {
    // TODO implement me
    return Collections.emptyList();
  }
}
