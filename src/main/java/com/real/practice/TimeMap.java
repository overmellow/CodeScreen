package com.real.practice;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

class TimeMap {

    private class Tuple {
        String key;
        int timestamp;

        public Tuple(String key, int timestamp) {
            this.key = key;
            this.timestamp = timestamp;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (!(o instanceof Tuple))
                return false;
            Tuple tuple = (Tuple) o;
            return timestamp == tuple.timestamp && key.equals(tuple.key);
        }

        @Override
        public int hashCode() {

            return Objects.hash(key, timestamp);
        }
    }

    Map<Tuple, String> map;

    /** Initialize your data structure here. */
    public TimeMap() {
        map = new HashMap<>();
    }

    public void set(String key, String value, int timestamp) {
        map.put(new Tuple(key, timestamp), value);
    }

    public String get(String key, int timestamp) {
        
        for (int i = timestamp; i >= 1; i--) {
            String value = map.getOrDefault(new Tuple(key, i), "");
            if (!value.isEmpty())
                return value;
        }
        return "";
    }
    
    public static void main(String[] args) {
		TimeMap tm = new TimeMap();
		
		tm.set("hey", "hello", 1234);
		tm.set("bye", "good", 12342);
		tm.set("how", "hep", 12341);
		
		System.out.println(tm.map);
		tm.map.entrySet().forEach(s -> System.out.println(s.getKey().key));
	}
}