package com.real.matcher;

import com.real.matcher.Matcher.CsvStream;
import com.real.matcher.Matcher.IdMapping;

import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class XboxLuceneFeederImpl implements Feeder {
    private InternalDB internalDB;
    private static final String INDEX_DIRECTORY = "./lucene";

    public XboxLuceneFeederImpl(InternalDB internalDB) {
        this.internalDB = internalDB;
    }

    @Override
    public List<IdMapping> match(CsvStream externalDb) {
        List<IdMapping> mappings = new ArrayList<>();

        // Extract the header indices from the CSV stream
        Map<String, Integer> headerIndices = extractHeadersIndices(externalDb);

        // Define the date-time formatter for parsing the release date
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy hh:mm:ss a");
       
        try (Stream<String> dataRows = externalDb.getDataRows()) {
        	//	Process each data row in the CSV stream
        	dataRows
        	  .distinct()
	          .map(s -> s.split(",(?=([^\\\"]*\\\"[^\\\"]*\\\")*[^\\\"]*$)")) // regex delimiter to split on commas but ignore commas inside double quotes to not split movie title inside double quotes with comma separator like "Zanya Flaming Grill, Part 2 of 2"   
	          .filter(distinctByKey(arr -> arr[headerIndices.get("MediaId")]))
//	          .limit(1000)
	          .forEach(arr -> {
	              // Parse the release date and create a Movie object
	              LocalDateTime dateTime = LocalDateTime.parse(arr[headerIndices.get("OriginalReleaseDate")], formatter);

	              try {
	            	  Movie movie = searchMovies(arr[headerIndices.get("Title")], String.valueOf(dateTime.getYear()), new ArrayList<Crew>());
	            	  
	            	  if(movie != null) {
		            	  mappings.add(new IdMapping(movie.getId(), arr[headerIndices.get("MediaId")]));
		              }
	              } 
	              catch (IOException | ParseException e) {
						e.printStackTrace();
	              }	              
	
	          });
	  	}
//        System.out.println();
//        mappings.forEach(s -> System.out.println(s));
        return mappings;
    }

    /**
     * Extracts the header indices from the CSV stream.
     * 
     * @param externalDb the CSV stream
     * @return a map of header names to indices
     */
    private Map<String, Integer> extractHeadersIndices(CsvStream externalDb) {
        String[] headers = externalDb.getHeaderRow().split(",");
        Map<String, Integer> headerIndices = new HashMap<>();

        // Map each header name to its corresponding index
        for (int i = 0; i < headers.length; i++) {
            headerIndices.put(headers[i], i);
        }

        return headerIndices;
    }

    /**
     * Creates a Movie object from the title and release date.
     * 
     * @param title    the movie title
     * @param dateTime the release date
     * @return the created Movie object
     */
    private Movie createMovie(String title, LocalDateTime dateTime) {
        String cleanTitle = title.replaceAll("\"", "");
        String year = String.valueOf(dateTime.getYear());
        return new Movie(cleanTitle, year);
    }

    /**
     * Returns a predicate that filters distinct elements based on a key extractor function.
     * 
     * @param keyExtractor the function to extract the key from an element
     * @param <T>          the type of the elements
     * @return the distinct-by-key predicate
     */
    public static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
        Set<Object> seen = new HashSet<>();
        return t -> seen.add(keyExtractor.apply(t));
    }
    
    private Movie searchMovies(String title, String year, List<Crew> crew) throws IOException, ParseException {
//    private Movie searchMovies(String queryStr) throws IOException, ParseException {
        // Create the index directory
        Directory indexDirectory = FSDirectory.open(Paths.get(INDEX_DIRECTORY));

        // Create an index reader
        IndexReader indexReader = DirectoryReader.open(indexDirectory);

        // Create an index searcher
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);

        // Create a query parser
//        QueryParser queryParser = new QueryParser("title", new StandardAnalyzer());

        // Parse the query
//        Query query = queryParser.parse(queryStr);
//        Query query = new BooleanQuery.Builder()
//        	    .add(new TermQuery(new Term("title", title)), BooleanClause.Occur.SHOULD)
//        	    .add(new TermQuery(new Term("series_title", "The Series Title")), BooleanClause.Occur.SHOULD)
//        	    .add(new TermQuery(new Term("year", year)), BooleanClause.Occur.MUST)
////        	    .add(new TermQuery(new Term("crew_name", )), BooleanClause.Occur.MUST)
////        	    .add(new TermQuery(new Term("crew_role", "Role")), BooleanClause.Occur.MUST)
//        	    .build();
        
        Analyzer analyzer = new WhitespaceAnalyzer();
        
        BooleanQuery.Builder booleanQueryBuilder = new BooleanQuery.Builder();
        
        // Create a TermQuery for the "title" field
//        Query titleQuery = new QueryParser("title", analyzer).parse(QueryParser.escape(title));
//        booleanQueryBuilder.add(titleQuery, BooleanClause.Occur.MUST);
        
        String[] queryTerms = title.split("\\s+"); // Split query text into terms

        for (String term : queryTerms) {
//        	System.out.println(term);
            Query termQuery = new TermQuery(new Term("title", QueryParser.escape(term))); // Replace "content" with your field name
            booleanQueryBuilder.add(termQuery, BooleanClause.Occur.SHOULD);
        }

        booleanQueryBuilder.setMinimumNumberShouldMatch((int) Math.ceil(queryTerms.length * 0.9)); // Set minimum should match
        
        // Create a TermQuery for the "year" field
        Query yearQuery = new QueryParser("year", analyzer).parse(year);
        booleanQueryBuilder.add(yearQuery, BooleanClause.Occur.MUST);
        
        // Build the final query
        Query query = booleanQueryBuilder.build();

        // Execute the search
        TopDocs topDocs = indexSearcher.search(query, 1);

        // Process the search results
        Movie mostSimilarMovie = null;
        // Check if there are any search results
        if (topDocs.totalHits.value > 0) {
            // Get the most similar movie
            ScoreDoc scoreDoc = topDocs.scoreDocs[0];
            Document doc = indexSearcher.doc(scoreDoc.doc);
            mostSimilarMovie = createMovieFromDocument(doc);
        }

        // Close the index reader
        indexReader.close();

        return mostSimilarMovie;
    }
    
    private static Movie createMovieFromDocument(Document doc) {
        int id = Integer.parseInt(doc.get("id"));
        String title = doc.get("title");
        String year = doc.get("year");

        Set<Crew> crew = new HashSet<>();

        // Retrieve crew members from the document
        String[] crewNames = doc.getValues("crew_name");
        String[] crewRoles = doc.getValues("crew_role");
        for (int i = 0; i < crewNames.length; i++) {
            String name = crewNames[i];
            String role = crewRoles[i];
            Crew crewMember = new Crew(name, role);
            crew.add(crewMember);
        }

        Movie movie = new Movie(id, title, year);
        movie.setCrew(crew);

        return movie;
    }
}


