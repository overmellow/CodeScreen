package com.real.matcher;

import com.real.matcher.Matcher.CsvStream;
import com.real.matcher.Matcher.IdMapping;

import java.util.List;

public interface Feeder {
	List<IdMapping> match(CsvStream externalDb);
}
