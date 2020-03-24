package com.example.interview.models;

import java.util.List;

public class Performer {
	private Integer performerId;
	private String performer;
	private List<Song> songs;
	
	public Performer() {}

	public Performer(Integer performerId, String performer, List<Song> songs) {
		super();
		this.performerId = performerId;
		this.performer = performer;
		this.songs = songs;
	}

	public Integer getPerformerId() {
		return performerId;
	}

	public void setPerformerId(Integer performerId) {
		this.performerId = performerId;
	}

	public String getPerformer() {
		return performer;
	}

	public void setPerformer(String performer) {
		this.performer = performer;
	}

	public List<Song> getSongs() {
		return songs;
	}

	public void setSongs(List<Song> songs) {
		this.songs = songs;
	}

}
