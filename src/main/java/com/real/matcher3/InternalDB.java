package com.real.matcher3;

import com.google.common.collect.BiMap;
import com.real.matcher.Matcher.CsvStream;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class InternalDB {
	private Map<Integer, Movie> moviesMap = new HashMap<>();
	private Map<Movie, Integer> moviesReverseMap = new HashMap<>();
//	private Map<Integer, List<Integer>> duplicates = new HashMap<>();

	public InternalDB(CsvStream movieDb, CsvStream actorAndDirectorDb) {
		loadMovies(movieDb);
		loadActorsAndDirectors(actorAndDirectorDb);
		removeDuplicateMovies();
		reverseMap();
		
		System.out.println();
		moviesMap.forEach((name, obj) -> System.out.println(name + ": " + obj));
		System.out.println();
	}
	
	private void loadMovies(CsvStream movieDb) {
		Map<String, Integer> headerIndices = extractHeadersIndices(movieDb);

	    moviesMap = movieDb.getDataRows()
	    		.distinct()
                .map(entry -> entry.split(","))
                .collect(
            		Collectors.toMap(
    					arr -> Integer.parseInt(arr[headerIndices.get("id")]),
    					arr -> new Movie(Integer.parseInt(arr[headerIndices.get("id")]), arr[headerIndices.get("title")].replaceAll("\"", ""), arr[headerIndices.get("year")]),
        				(v1, v2) -> v1, 		// Handle duplicate keys by keeping the existing value
//    					(v1, v2) -> {
////    						v1.merge(v2);
//    						return v1;
//						},
        				HashMap::new)
            		);
		
//	    moviesBiMap.forEach((name, obj) -> System.out.println(name + ": " + obj));
	}
	
	private void loadActorsAndDirectors(CsvStream actorAndDirectorDb) {
		Map<String, Integer> headerIndices = extractHeadersIndices(actorAndDirectorDb);
	    actorAndDirectorDb.getDataRows()
	    	.distinct()
		    .map(s -> s.split(","))
	    	.forEach(arr -> {
				Movie m;
				if((m = moviesMap.get(Integer.parseInt(arr[headerIndices.get("movie_id")]))) != null) {
					String name = arr[headerIndices.get("name")];
					String role = arr[headerIndices.get("role")];
					m.getCrew().add(new Crew(name, role));
				}
	    	});
//	    moviesBiMap.forEach((name, obj) -> System.out.println(name + ": " + obj));
	}

	private void removeDuplicateMovies() {
		Map<Integer, Movie> uniqueValuesBiMap = new HashMap<>();
		Map<String, Integer> duplicatesTracker = new HashMap<>();
		for (Map.Entry<Integer, Movie> entry : moviesMap.entrySet()) {
	        if (duplicatesTracker.containsKey(entry.getValue().getTitle())) {
	            Integer existingMovieId = duplicatesTracker.get(entry.getValue().getTitle());
	            moviesMap.put(existingMovieId, moviesMap.get(existingMovieId).merge(entry.getValue()));
	            uniqueValuesBiMap.put(existingMovieId, moviesMap.get(existingMovieId).merge(entry.getValue()));

	        } else {
	        	duplicatesTracker.put(entry.getValue().getTitle(), entry.getKey());
                uniqueValuesBiMap.put(entry.getKey(), entry.getValue());
	        }
		}
		moviesMap.clear();
		moviesMap.putAll(uniqueValuesBiMap);
	}

	private void reverseMap() {
		moviesMap.entrySet().forEach(e -> {
			moviesReverseMap.put(e.getValue(), e.getKey());
		});
//		moviesReverseMap.forEach((name, obj) -> System.out.println(name + ": " + obj));
	}
	
	
	public Map<Integer, Movie> getDB() {
		return moviesMap;
	}
	
	public Map<Movie, Integer> getDBInverse() {
		return moviesReverseMap;
	}

	private Map<String, Integer> extractHeadersIndices(CsvStream externalDb) {
		Map<String, Integer> headerIndices = new HashMap<>();
		String[] headers = externalDb.getHeaderRow().split(",");
		for (int i = 0; i < headers.length; i++) {
			headerIndices.put(headers[i], i);
		}
		return headerIndices;
	}
	
}
