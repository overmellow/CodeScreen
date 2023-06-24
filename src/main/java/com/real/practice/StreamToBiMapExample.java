package com.real.practice;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.real.matcher2.Movie;

import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class Person {
    private String name;

    public Person(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        
        Person other = (Person) obj;
        return name.equals(other.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
    
    
    public void merge(Person other) {
        // Merge logic based on your requirements
        // Here, we simply concatenate the names
        this.name += ", " + other.getName();
    }
}

public class StreamToBiMapExample {
    public static void main(String[] args) {
        Stream<String> stream = Stream.of("1:John", "2:Jane", "3:Alice", "4:Jane");

        BiMap<Integer, Person> biMap = stream
                .map(entry -> entry.split(":"))  // Split each entry into key-value pairs
                .collect(Collectors.toMap(
                        pair -> Integer.parseInt(pair[0]),
                        pair -> new Person(pair[1]),
//                        (v1, v2) -> v1, // Handle duplicate keys by keeping the existing value
                        (v1, v2) -> {
                            v1.merge(v2);  // Merge the objects instead of replacing
                            return v1;
                        },
                        HashBiMap::create
                ));

        // Print the BiMap
        for (BiMap.Entry<Integer, Person> entry : biMap.entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue().getName());
        }
    }
}

