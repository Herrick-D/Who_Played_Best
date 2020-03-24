package com.example.interview.models;

import java.util.List;

public class Genre {
	private Integer genreId;
	private String genre;
	private List<Performer> performers;
	
	public Genre() {}

	public Genre(Integer genreId, String genre, List<Performer> performers) {
		super();
		this.genreId = genreId;
		this.genre = genre;
		this.performers = performers;
	}

	public Integer getGenreId() {
		return genreId;
	}

	public void setGenreId(Integer genreId) {
		this.genreId = genreId;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public List<Performer> getPerformers() {
		return performers;
	}

	public void setPerformers(List<Performer> performers) {
		this.performers = performers;
	}
}
