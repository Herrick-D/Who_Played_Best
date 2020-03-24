package com.example.interview.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.interview.models.Song;
import com.example.interview.services.SongService;

@Controller
@RequestMapping(path="/song")
public class SongController {
	@Autowired
	private SongService songService;

	@GetMapping(path="/all")
	public @ResponseBody List<Song> getAllSongs() {
		return songService.getAllSongs();
	}
	
	@GetMapping(path="/searchByPerformer")
	public @ResponseBody List<Song> getSongsByPerformerSearch(@RequestParam("performer") String performer) {
		return songService.getSongsByPerformerSearch(performer);
	}
	
	@GetMapping(path="/searchByGenre")
	public @ResponseBody List<Song> getSongsByGenreSearch(@RequestParam("genre") String genre) {
		return songService.getSongsByGenreSearch(genre);
	}
	
	@GetMapping(path="/searchByYear") 
	public @ResponseBody List<Song> getSongsByYear(@RequestParam("year") Integer year) {
		return songService.getSongsByYear(year);
	}
	
	@PutMapping(path="/addSong")
	public @ResponseBody List<String> insertSong(@RequestBody Song song) {
		return songService.insertSong(song);
	}
	
	@PutMapping(path="/addRating")
	public @ResponseBody String insertRating(@RequestParam("songId") Integer songId, 
																					 @RequestParam("rating") Double rating){
		return songService.insertRating(songId, rating);
	}
	
	@DeleteMapping(path="/delete")
	public @ResponseBody String deleteSong(@RequestParam("songId") Integer songId) {
		return songService.deleteSong(songId);
	}
	
	@PostMapping(path="/update")
	public @ResponseBody List<String> updateSong(@RequestBody Song song) {
		return songService.updateSong(song);
	}
}
