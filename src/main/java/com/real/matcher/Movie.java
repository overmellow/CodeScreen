package com.real.matcher;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import me.xdrop.fuzzywuzzy.FuzzySearch;

public class Movie {
	private int id;
	private String title;
	private String year;
	private Set<Crew> crew;
	
	public Movie() {}
	
	public Movie(String title, String year) {
		this.title = title;
		this.year = year;
	}
			
	public Movie(int id, String title, String year) {
		this.id = id;
		this.title = title;
		this.year = year;
		this.crew = new HashSet<>();
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}

	public Set<Crew> getCrew() {
		return crew;
	}

	public void setCrew(Set<Crew> crew) {
		this.crew = crew;
	}

	@Override
	public String toString() {
		return "Movie [id=" + id + ", title=" + title + ", year=" + year + ", crew=" + crew + "]";
	}	
	
	
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        
        Movie other = (Movie) obj;
        return title.equals(other.title) && year.equals(other.year);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, year);
    }
    
    public Movie merge(Movie other) {
		this.crew.addAll(other.crew);
    	return this;
    }
    
    public static double similarity(Movie a, Movie b) {
        double titleSimilarity = FuzzySearch.ratio(a.getTitle(), b.getTitle());
        
        System.out.println("titleSimilarity: " + titleSimilarity);

        double yearSimilarity = a.getYear().equals(b.getYear()) ? 100.0 : 0.0;
        System.out.println("yearSimilarity: " + yearSimilarity);
        
        double crewSimilarity = 0.0;
        int maxCrewSize = Math.max(a.getCrew().size(), b.getCrew().size());
        System.out.println("maxCrewSize: " + maxCrewSize);
        
        if (maxCrewSize > 0) {
            for (Crew crewA : a.getCrew()) {
                for (Crew crewB : b.getCrew()) {
                    double crewNameSimilarity = FuzzySearch.ratio(crewA.getName(), crewB.getName());
                    double crewRoleSimilarity = FuzzySearch.ratio(crewA.getRole(), crewB.getRole());
                    System.out.println("crewNameSimilarity: " + crewNameSimilarity);
                    System.out.println("crewRoleSimilarity: " + crewRoleSimilarity);

                    double averageCrewSimilarity = (crewNameSimilarity + crewRoleSimilarity) / 2.0;
                    System.out.println("averageCrewSimilarity: " + averageCrewSimilarity);
                    
                    if (averageCrewSimilarity > crewSimilarity) {
                        crewSimilarity = averageCrewSimilarity;
                        System.out.println("crewSimilarity: " + crewSimilarity);
                    }
                }
            }
        } else {
            crewSimilarity = 1.0;
        }
        System.out.println((titleSimilarity + yearSimilarity + crewSimilarity));
        // Assuming title, year, and crew have equal weights
        return (titleSimilarity + yearSimilarity + crewSimilarity) / 3.0;
    }
    
    
}
