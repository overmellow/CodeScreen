package com.real.matcher;

import java.util.ArrayList;
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
		LOGGER.info("database imported");
	}

	@Override
	public List<IdMapping> match(DatabaseType databaseType, CsvStream externalDb) {
		// 	TODO implement me
		return Collections.emptyList();
	}
	  
	
}
