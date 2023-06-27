package com.real.matcher;

import com.real.matcher.Matcher.CsvStream;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.*;
import org.apache.lucene.search.*;
import org.apache.lucene.store.*;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class InternalDBWithLucene {
	private Map<Integer, Movie> moviesMap = new HashMap<>();
	private Map<Movie, Integer> moviesReverseMap = new HashMap<>();
	
	private static final String INDEX_DIRECTORY = "./lucene";

	public InternalDBWithLucene(CsvStream movieDb, CsvStream actorAndDirectorDb) {
		loadMovies(movieDb); // Load movies from the movie database
		loadActorsAndDirectors(actorAndDirectorDb); // Load actors and directors from the associated database and them to movie objects
		removeDuplicateMovies(); // Remove any duplicate movies from the loaded data
		reverseMap(); // Create a reverse mapping of movies and their IDs
		
		try {
			createIndex();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void loadMovies(CsvStream movieDb) {
		Map<String, Integer> headerIndices = extractHeadersIndices(movieDb);

	    moviesMap = movieDb.getDataRows()
            .distinct()
            .map(entry -> entry.split(","))
            .collect(Collectors.toMap(
                arr -> Integer.parseInt(arr[headerIndices.get("id")]),
                arr -> new Movie(
                        Integer.parseInt(arr[headerIndices.get("id")]),
                        arr[headerIndices.get("title")].replaceAll("\"", ""),
                        arr[headerIndices.get("year")]
                ),
                (v1, v2) -> v1, // Handle duplicate keys
                HashMap::new
            ));		
	}
	
	private void loadActorsAndDirectors(CsvStream actorAndDirectorDb) {
		Map<String, Integer> headerIndices = extractHeadersIndices(actorAndDirectorDb);
		
	    actorAndDirectorDb.getDataRows()
        .distinct()
        .map(s -> s.split(","))
        .forEach(arr -> {
            int movieId = Integer.parseInt(arr[headerIndices.get("movie_id")]); // Movie ID
            Movie movie = moviesMap.get(movieId); // Get the corresponding movie
            if (movie != null) {
                String name = arr[headerIndices.get("name")]; // Actor or director name
                String role = arr[headerIndices.get("role")]; // Role of the crew member
                movie.getCrew().add(new Crew(name, role)); // Add the crew member to the movie's crew list
            }
        });
	    
	}

	private void removeDuplicateMovies() {
		Map<Integer, Movie> uniqueMovies = new HashMap<>();
		Map<String, Integer> duplicatesTracker = new HashMap<>();
	
	    for (Movie movie : moviesMap.values()) {
	        String title = movie.getTitle(); // Get the movie title

	        if (duplicatesTracker.containsKey(title)) {
	            Integer existingMovieId = duplicatesTracker.get(title); // Get the ID of the existing movie with the same title
	            Movie existingMovie = uniqueMovies.get(existingMovieId); // Get the existing movie object
	            existingMovie.merge(movie); // Merge the current movie with the existing one
	        } else {
	            duplicatesTracker.put(title, movie.getId()); // Track the title and ID of the movie as a duplicate
	            uniqueMovies.put(movie.getId(), movie); // Add the movie to the unique movies map
	        }
	    }
		
		moviesMap.clear();
		moviesMap.putAll(uniqueMovies); // Update moviesMap with the unique movies
	}

	private void reverseMap() {
		moviesMap.forEach((key, value) -> moviesReverseMap.put(value, key));
	}
	
	
	public Map<Integer, Movie> getDB() {
		return moviesMap; // Return the movies database map
	}
	
	public Map<Movie, Integer> getDBInverse() {
		return moviesReverseMap; // Return the reverse mapping of movies and their IDs
	}

	private Map<String, Integer> extractHeadersIndices(CsvStream externalDb) {
		Map<String, Integer> headerIndices = new HashMap<>();
		String[] headers = externalDb.getHeaderRow().split(","); // Split the header row into individual headers
		for (int i = 0; i < headers.length; i++) {
			headerIndices.put(headers[i], i); // Store the header name and its corresponding index
		}
		return headerIndices; // Return the map of header names and their indices
	}
	
	private void createIndex() throws IOException {
		// Create the index directory
        Directory indexDirectory = FSDirectory.open(Paths.get(INDEX_DIRECTORY));

        // Create an analyzer
        Analyzer analyzer = new StandardAnalyzer();

        // Create the index writer configuration
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        
        // Specify that the index should be created or overwritten
        config.setOpenMode(IndexWriterConfig.OpenMode.CREATE);

        // Create the index writer
        IndexWriter indexWriter = new IndexWriter(indexDirectory, config);

        // Index the movies
        List<Movie> movies = getMovies(); // Get the list of movies
        for (Movie movie : movies) {
            Document doc = new Document();
            doc.add(new StringField("id", String.valueOf(movie.getId()), Field.Store.YES));
            doc.add(new TextField("title", movie.getTitle(), Field.Store.YES));
            doc.add(new StringField("year", movie.getYear(), Field.Store.YES));

            // Index the crew
            for (Crew crewMember : movie.getCrew()) {
                doc.add(new StringField("crew_name", crewMember.getName(), Field.Store.YES));
                doc.add(new StringField("crew_role", crewMember.getRole(), Field.Store.YES));
            }

            indexWriter.addDocument(doc);
        }

        // Commit and close the index writer
        indexWriter.commit();
        indexWriter.close();
	}
	
	// Example method to retrieve a list of movies
    private List<Movie> getMovies() {
    	return new ArrayList<>(moviesMap.values());
    }
	
}
