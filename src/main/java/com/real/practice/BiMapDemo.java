package com.real.practice;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

public class BiMapDemo {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		BiMap<String, String> capitalCountryBiMap = HashBiMap.create();
		capitalCountryBiMap.put("iran", "tehran");
		capitalCountryBiMap.put("iraq", "baghdad");
		capitalCountryBiMap.put("us", "washington");
		
		System.out.println(capitalCountryBiMap);
		System.out.println(capitalCountryBiMap.get("iran"));
		System.out.println(capitalCountryBiMap.get("iraq"));
		System.out.println(capitalCountryBiMap.inverse().get("washington"));
		System.out.println(capitalCountryBiMap.inverse().get("tehran"));
		
		BiMap<String, YClass> biMap = HashBiMap.create();
		biMap.put("1", new YClass("jake", 121));
		biMap.put("2", new YClass("mori", 122));
		biMap.put("3", new YClass("iman", 123));
		biMap.put("4", new YClass("iman", 1233));
		System.out.println(biMap);
		System.out.println(biMap.get("1"));
		System.out.println(biMap.inverse().get(new YClass("mori", 122)));
		
		
		System.out.println();
		BiMap<Integer, YClass> biMap2 = HashBiMap.create();
		YClass a = new YClass("jake", 121);
		YClass b = new YClass("mori", 122);
		YClass c = new YClass("iman", 123);
		YClass d = new YClass("iman", 123);
		biMap2.put(a.hashCode(), a);
		biMap2.put(b.hashCode(), b);
		biMap2.put(c.hashCode(), c);
		biMap2.put(d.hashCode(), d);
		System.out.println(biMap2);
		System.out.println(biMap2.get(a.hashCode()));
//		System.out.println(biMap2.inverse().get(new YClass("mori", 122)));
		
		System.out.println();
		BiMap<YClass, YClass> biMap3 = HashBiMap.create();
		YClass a3 = new YClass("jake", 121);
		YClass b3 = new YClass("mori", 122);
		YClass c3 = new YClass("iman", 123);
		YClass d3 = new YClass("iman", 123);
		biMap3.put(a3, a3);
		biMap3.put(b3, b3);
		biMap3.put(c3, c3);
		biMap3.put(d3, d3);
		System.out.println(biMap3);
		System.out.println(biMap3.get(a));
//		System.out.println(biMap2.inverse().get(new YClass("mori", 122)));

	}

}

class YClass {
    private String name;
    private int id;

    public YClass(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

	@Override
	public String toString() {
		return "MyClass [name=" + name + ", id=" + id + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		YClass other = (YClass) obj;
		if (id != other.id)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
}