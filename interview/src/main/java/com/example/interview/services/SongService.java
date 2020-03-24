package com.example.interview.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.interview.models.Song;
import com.example.interview.repositories.SongRepository;

@Service
public class SongService {
	@Autowired
	private SongRepository songRepo;

	public List<Song> getAllSongs() {
		return songRepo.getAllSongs();
	}

	public List<Song> getSongsByPerformerSearch(String performer) {
		return songRepo.getSongsByPerformerSearch(performer);
	}
	
	public List<Song> getSongsByGenreSearch(String genre) {
		return songRepo.getSongsByGenreSearch(genre);
	}

	public List<Song> getSongsByYear(Integer year) {
		return songRepo.getSongsByYear(year);
	}

	public List<String> insertSong(Song song) {
		return songRepo.insertSong(song);
	}

	public String insertRating(Integer songId, Double rating) {
		return songRepo.insertRating(songId, rating);
	}

	public String deleteSong(Integer songId) {
		return songRepo.deleteSong(songId);
	}

	public List<String> updateSong(Song song) {
		return songRepo.updateSong(song);
	}

	
	
	
}
