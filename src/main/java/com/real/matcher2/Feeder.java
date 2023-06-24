package com.real.matcher2;

import java.util.List;

import com.real.matcher.Matcher.CsvStream;
import com.real.matcher.Matcher.IdMapping;

public interface Feeder {
	List<IdMapping> match(CsvStream externalDb);
}
