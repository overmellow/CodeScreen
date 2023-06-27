package com.real.matcher;

import com.real.matcher.Matcher.CsvStream;
import com.real.matcher.Matcher.IdMapping;

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

public class XboxFeederImpl implements Feeder {
    private InternalDB internalDB;

    public XboxFeederImpl(InternalDB internalDB) {
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
            // Process each data row in the CSV stream
            dataRows
                .map(s -> s.split(",(?=([^\\\"]*\\\"[^\\\"]*\\\")*[^\\\"]*$)")) // regex delimiter to split on commas but ignore commas inside double quotes to not split movie title inside double quotes with comma separator like "Zanya Flaming Grill, Part 2 of 2"   
                .filter(distinctByKey(arr -> arr[headerIndices.get("MediaId")]))
                .forEach(arr -> {
                    // Parse the release date and create a Movie object
                    LocalDateTime dateTime = LocalDateTime.parse(arr[headerIndices.get("OriginalReleaseDate")], formatter);
                    Movie movie = createMovie(arr[headerIndices.get("Title")], dateTime);

                    // Check if the movie exists in the internal database and create a mapping
                    if (internalDB.getDBInverse().containsKey(movie)) {
                        mappings.add(new IdMapping(internalDB.getDBInverse().get(movie), arr[headerIndices.get("MediaId")]));
                    }
                });
        }

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
}


