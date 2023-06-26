package com.real.matcher3;

import com.real.matcher.Matcher.CsvStream;
import com.real.matcher.Matcher.DatabaseType;
import com.real.matcher.Matcher.IdMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Demo {
	public static void main(String[] args) {
		Demo d = new Demo();
		try {
			d.matchTest();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static final Logger LOGGER = LoggerFactory.getLogger(Demo.class);

//	  @Test
	  public void matchTest() throws Exception {
	    List<IdMapping> idMappings;
	    // load and process the data files
	    try (var closer = new Closer()) {
	      var moviesCsv = loadCsvFile(closer, "movies.csv");
	      var actorsAndDirectorsCsv = loadCsvFile(closer, "actors_and_directors.csv");
	      var matcher = new MatcherImpl(moviesCsv, actorsAndDirectorsCsv);
	      var xboxCsv = loadCsvFile(closer, "xbox_feed.csv");
	      idMappings = matcher.match(DatabaseType.XBOX, xboxCsv);
	    }
	    LOGGER.info("Total items matched: {}", idMappings.size());
	    // test the results
		System.out.println();
	    var seenExternal = new HashSet<UUID>();
	    for (var mapping : idMappings) {
	      var internalId = mapping.getInternalId();
	      var externalId = mapping.getExternalId();
//	      System.out.println(mapping);
	    }
	  }

	  private static CsvStream loadCsvFile(Closer closer, String fileName) throws IOException {
	    LOGGER.info("reading {}", fileName);
	    var stream = MatcherImpl.class.getClassLoader().getResourceAsStream(fileName);
	    var reader = closer.register(fileName, new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8)));
	    var header = reader.readLine().trim();
	    LOGGER.info("headers: {}", header);
	    var lines = reader
	        .lines()
	        .map(String::trim)
	        .filter(x -> !x.isBlank());
	    return new CsvStream(header, lines);
	  }

	  private static class Closer implements Closeable {
	    private final List<Map.Entry<String, Closeable>> closeables = new ArrayList<>();

	    @Override
	    public void close() throws IOException {
	      closeables.forEach(e -> {
	        var name = e.getKey();
	        LOGGER.info("closing {}", name);
	        try {
	          e.getValue().close();
	          LOGGER.info("close {}", name);
	        } catch (IOException ex) {
	          LOGGER.error("can't close {}", name, ex);
	        }
	      });
	      closeables.clear();
	    }

	    public <T extends Closeable> T register(String name, T closeable) {
	      closeables.add(Map.entry(name, closeable));
	      return closeable;
	    }
	  }
}