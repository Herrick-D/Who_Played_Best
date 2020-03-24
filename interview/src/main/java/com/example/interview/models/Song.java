package com.example.interview.models;

import java.util.List;

import com.example.interview.daos.GenreDAO;
import com.example.interview.daos.PerformerDAO;

public class Song {
	private Integer songId;
	private String songTitle;
	private List<PerformerDAO> performers;
	private List<GenreDAO> genres;
	private Integer year;
	private Double rating;
	
	public Song() {}
	
	public Song(Integer songId, String songTitle, List<PerformerDAO> performer, List<GenreDAO> genre, Integer year,
			Double rating) {
		super();
		this.songId = songId;
		this.songTitle = songTitle;
		this.performers = performer;
		this.genres = genre;
		this.year = year;
		this.rating = rating;
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

	public List<PerformerDAO> getPerformers() {
		return performers;
	}

	public void setPerformers(List<PerformerDAO> performers) {
		this.performers = performers;
	}

	public List<GenreDAO> getGenres() {
		return genres;
	}

	public void setGenres(List<GenreDAO> genres) {
		this.genres = genres;
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
