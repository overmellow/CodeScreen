package com.real.matcher;

public class XboxLuceneFeederFactory extends FeederFactory {
	
    public Feeder createFeeder(InternalDB internalDB) {
        return new XboxLuceneFeederImpl(internalDB);
    }
}
