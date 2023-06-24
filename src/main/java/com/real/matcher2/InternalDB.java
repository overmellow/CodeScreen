package com.real.matcher2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.real.matcher.Matcher.CsvStream;

public class InternalDB {
	private BiMap<Integer, Movie> moviesBiMap = HashBiMap.create();
	
	public InternalDB(CsvStream movieDb, CsvStream actorAndDirectorDb) {
		loadMovies(movieDb);
		loadActorsAndDirectors(actorAndDirectorDb);
		removeDuplicateMovies();
		
		System.out.println();
//		moviesBiMap.forEach((name, obj) -> System.out.println(name + ": " + obj));
		System.out.println();
	}
	
	private void loadMovies(CsvStream movieDb) {
	    moviesBiMap = movieDb.getDataRows()
	    		.distinct()
                .map(entry -> entry.split(","))
                .collect(
            		Collectors.toMap(
    					arr -> Integer.parseInt(arr[0]), 
    					arr -> new Movie(Integer.parseInt(arr[0]), arr[1].replaceAll("\"", ""), arr[2]), 
        				(v1, v2) -> v1, 		// Handle duplicate keys by keeping the existing value
//    					(v1, v2) -> {
//    						v1.merge(v2); 
//    						return v1;
//						},
        				HashBiMap::create)
            		);
		
//	    moviesBiMap.forEach((name, obj) -> System.out.println(name + ": " + obj));
	}
	
	private void loadActorsAndDirectors(CsvStream actorAndDirectorDb) {
	    actorAndDirectorDb.getDataRows()
	    	.distinct()
		    .map(s -> s.split(","))
	    	.forEach(arr -> {
				Movie m;
				if((m = moviesBiMap.get(Integer.parseInt(arr[0]))) != null) {
					if(arr[2].equals("cast")) { 
						m.getCasts().add(arr[1]);
					}
					else if(arr[2].equals("director")){
						m.getDirectors().add(arr[1]);
					}
				}
	    	});
//	    moviesBiMap.forEach((name, obj) -> System.out.println(name + ": " + obj));
	}
	

	private void removeDuplicateMovies() {
		BiMap<Integer, Movie> uniqueValuesBiMap = HashBiMap.create();
		Map<String, Integer> duplicatesTracker = new HashMap<>();
		for (BiMap.Entry<Integer, Movie> entry : moviesBiMap.entrySet()) {
	        if (duplicatesTracker.containsKey(entry.getValue().getTitle())) {
	            Integer existingMovieId = duplicatesTracker.get(entry.getValue().getTitle());
	            moviesBiMap.put(existingMovieId, moviesBiMap.get(existingMovieId).merge(entry.getValue()));
	            uniqueValuesBiMap.put(existingMovieId, moviesBiMap.get(existingMovieId).merge(entry.getValue()));

	        } else {
	        	duplicatesTracker.put(entry.getValue().getTitle(), entry.getKey());
                uniqueValuesBiMap.put(entry.getKey(), entry.getValue());
	        }
		}
		moviesBiMap.clear();
		moviesBiMap.putAll(uniqueValuesBiMap);
	}
	
	
	public BiMap<Integer, Movie> getDB() {
		return moviesBiMap;
	}
	
	public BiMap<Movie, Integer> getDBInverse() {
		return moviesBiMap.inverse();
	}
	
}
