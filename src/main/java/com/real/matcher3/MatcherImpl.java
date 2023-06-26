package com.real.matcher3;

import com.real.matcher.Matcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class MatcherImpl implements Matcher {
	private static final Logger LOGGER = LoggerFactory.getLogger(com.real.matcher.MatcherImpl.class);
	private static InternalDB internalDB;

	public MatcherImpl(CsvStream movieDb, CsvStream actorAndDirectorDb) {
	    LOGGER.info("importing database");
	    
	    internalDB = new InternalDB(movieDb, actorAndDirectorDb);
	        
	    LOGGER.info("database imported");
	  }

	  @Override
	  public List<IdMapping> match(DatabaseType databaseType, CsvStream externalDb) {
		  
//		List<IdMapping> mappings = new ArrayList<Matcher.IdMapping>();

		List<IdMapping> mappings = MatcherImpl.getMatcher(databaseType).match(externalDb);
		
		return mappings;
//	    return Collections.emptyList();
	  }
	  
	  
	  public static Feeder getMatcher(DatabaseType databaseType) {
		 switch (databaseType) {		 
		 	case XBOX:
		 		return new XboxFeeder(internalDB);
			case AMAZON_INSTANT:
				return new AmazonFeeder(internalDB);
			default:
				return new XboxFeeder(internalDB);
		 }
		 
	  }
	  

}
