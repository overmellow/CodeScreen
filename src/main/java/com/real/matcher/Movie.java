package com.real.matcher;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Movie {
	private int id;
	private String title;
	private String year;
	private Set<String> directors;
	private Set<String> casts;
	
	public Movie() {}
	
	public Movie(String title, String year) {
		this.title = title;
		this.year = year;
	}
			
	public Movie(int id, String title, String year) {
		this.id = id;
		this.title = title;
		this.year = year;
		this.casts = new HashSet<String>();
		this.directors = new HashSet<String>();
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

	public Set<String> getDirectors() {
		return directors;
	}

	public void setDirectors(Set<String> directors) {
		this.directors = directors;
	}

	public Set<String> getCasts() {
		return casts;
	}

	public void setCasts(Set<String> casts) {
		this.casts = casts;
	}

	@Override
	public String toString() {
		return "Movie [id=" + id + ", title=" + title + ", year=" + year + ", directors=" + directors + ", casts="
				+ casts + "]";
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
	
}
