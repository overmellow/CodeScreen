package com.real.matcher;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MatcherImpl implements Matcher {

	private static final Logger LOGGER = LoggerFactory.getLogger(com.real.matcher.MatcherImpl.class);
	private static InternalDB internalDB;
//	private static InternalDBWithLucene internalDBWithLucene;
	private final FeederFactory feederFactory;

	public MatcherImpl(CsvStream movieDb, CsvStream actorAndDirectorDb) {
	    LOGGER.info("importing database");
	    
	    internalDB = new InternalDB(movieDb, actorAndDirectorDb);
	    
//	    internalDBWithLucene = new InternalDBWithLucene(movieDb, actorAndDirectorDb);
	        
	    LOGGER.info("database imported");
	    
	    feederFactory = new XboxFeederFactory();
	    
	    
//	    feederFactory = new XboxLuceneFeederFactory();
	    
	  }

	@Override
	public List<IdMapping> match(DatabaseType databaseType, CsvStream externalDb) {
	  
		Feeder feeder = feederFactory.createFeeder(internalDB);
	  
	  	List<IdMapping> mappings = feeder.match(externalDb);
	  
	  	return mappings;
	}
	  
	
}
