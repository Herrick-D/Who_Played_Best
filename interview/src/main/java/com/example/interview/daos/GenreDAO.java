package com.example.interview.daos;

public class GenreDAO {
	Integer genreId;
	String genre;
	
	public GenreDAO() {}
	
	public GenreDAO(Integer genreId, String genre) {
		super();
		this.genreId = genreId;
		this.genre = genre;
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
}
