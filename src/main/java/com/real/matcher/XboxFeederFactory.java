package com.real.matcher;

public class XboxFeederFactory extends FeederFactory {
	
    public Feeder createFeeder(InternalDB internalDB) {
        return new XboxFeederImpl(internalDB);
    }
}
