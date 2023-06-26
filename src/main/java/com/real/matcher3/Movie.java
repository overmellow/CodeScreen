package com.real.matcher3;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

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
    
    
}
