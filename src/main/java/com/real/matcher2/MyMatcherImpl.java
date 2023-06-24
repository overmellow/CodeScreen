package com.real.matcher2;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.real.matcher.MatcherImpl;

public class MyMatcherImpl extends MatcherImpl {
	private static final Logger LOGGER = LoggerFactory.getLogger(MatcherImpl.class);
	private static InternalDB internalDB;

	public MyMatcherImpl(CsvStream movieDb, CsvStream actorAndDirectorDb) {
		super(movieDb, actorAndDirectorDb);
	    LOGGER.info("importing database");
	    
	    internalDB = new InternalDB(movieDb, actorAndDirectorDb);
	        
	    LOGGER.info("database imported");
	  }

	  @Override
	  public List<IdMapping> match(DatabaseType databaseType, CsvStream externalDb) {
		  
//		List<IdMapping> mappings = new ArrayList<Matcher.IdMapping>();
		
//		externalDb.getDataRows().forEach(System.out::println);
		  
		List<IdMapping> mappings = MyMatcherImpl.getMatcher(databaseType).match(externalDb);
		
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
