package com.real.practice;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class Hash_Map_Demo {
	public static void main(String[] args)
    {
  
        // Creating an empty HashMap
  
        Stream<MyObject> objectStream = Stream.of(
                new MyObject(1, "Object 1"),
                new MyObject(2, "Object 2"),
                new MyObject(1, "Object 1 Updated"),
                new MyObject(3, "Object 3"),
                new MyObject(4, "Object 3")
        );

        Map<Integer, MyObject> objectMap = objectStream.collect(Collectors.toMap(
                MyObject::getId,                      // Key mapper
                obj -> obj,                           // Value mapper
                (existing, updated) -> {
                    existing.setName(updated.getName());  // Merge logic for objects with the same key
                    return existing;
                },
                HashMap::new                          // Map implementation
        ));
        
        HashMap<Integer, MyObject> uniqueValuesBiMap = new HashMap<Integer, MyObject>();

        for (Map.Entry<Integer, MyObject> entry : objectMap.entrySet()) {
        	Integer key = entry.getKey();
            MyObject value = entry.getValue();
            if (!uniqueValuesBiMap.containsValue(value)) {
            	System.out.println("hey");
                uniqueValuesBiMap.put(key, value);
            }
        }       

        objectMap.clear();
        objectMap.putAll(uniqueValuesBiMap);

//        System.out.println(myBiMap);
        
        objectMap.forEach((key, value) -> System.out.println(key + ": " + value.getName()));
    }
}



class MyObject {
    private int id;
    private String name;

    public MyObject(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

	public void setId(int id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public int hashCode() {
//		final int prime = 31;
//		int result = 1;
//		result = prime * result + id;
//		result = prime * result + ((name == null) ? 0 : name.hashCode());
//		return result;
		return Objects.hash(id, name);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MyObject other = (MyObject) obj;
//		if (id != other.id)
//			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
	
    
    
}