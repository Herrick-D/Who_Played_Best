package com.example.interview.daos;

public class SongDAO {
	Integer songId;
	String songTitle;
	Integer year;
	Double rating;
	
	public SongDAO() {}
	
	public SongDAO(Integer songId, String songTitle, Integer year, Double rating) {
		super();
		this.songId = songId;
		this.songTitle = songTitle;
		this.year = year;
		this.rating = rating;
	}
	
	public SongDAO(Integer songId, String songTitle, Integer year) {
		super();
		this.songId = songId;
		this.songTitle = songTitle;
		this.year = year;
	}

	public Integer getSongId() {
		return songId;
	}
	public void setSongId(Integer songId) {
		this.songId = songId;
	}
	public String getSongTitle() {
		return songTitle;
	}
	public void setSongTitle(String songTitle) {
		this.songTitle = songTitle;
	}
	public Integer getYear() {
		return year;
	}
	public void setYear(Integer year) {
		this.year = year;
	}
	public Double getRating() {
		return rating;
	}
	public void setRating(Double rating) {
		this.rating = rating;
	}
}
