package com.real.matcher;

import java.util.HashSet;
import java.util.Set;

public class Practice {

	public static void main(String[] args) {
		Set<MyClass> set = new HashSet();
		MyClass a1 = new MyClass("jame", 11);
		MyClass a2 = new MyClass("james", 12);
		MyClass a3 = new MyClass("jake", 13);
		MyClass a4 = new MyClass("jame", 11);
		
		set.add(a1);
		set.add(a2);
		set.add(a3);
		set.add(a4);
		set.add(a1);
		
		set.forEach(System.out::println);
		System.out.println(set);

	}

}

class MyClass implements Comparable<MyClass>{
    private String name;
    private int age;

    public MyClass(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

	@Override
	public int compareTo(MyClass o) {
		if(this.name.equals(o.name) && this.age == o.age) {
			return 0;	
		}
		return 1;
	}
	
//	 @Override
//    public boolean equals(Object obj) {
//        if (this == obj)
//            return true;
//        if (obj == null)
//            return false;
//        if (getClass() != obj.getClass())
//            return false;
//        MyClass other = (MyClass) obj;
//        if (age != other.age)
//            return false;
//        if (name == null) {
//            if (other.name != null)
//                return false;
//        } else if (!name.equals(other.name))
//            return false;
//        return true;
//    }	

//	@Override
//	public String toString() {
//		return "MyClass [name=" + name + ", age=" + age + "]";
//	}
    
    
}
