package com.example.interview.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.interview.daos.GenreDAO;
import com.example.interview.models.Genre;
import com.example.interview.services.GenreService;

@Controller
@RequestMapping(path="/genre")
public class GenreController {
	@Autowired
	private GenreService genreService;
	
	@GetMapping(path="/all")
	public @ResponseBody List<Genre> getAllGenres() {
		return genreService.getAllGenres();
	}
	
	@GetMapping(path="/searchBySong")
	public @ResponseBody List<Genre> getGenresBySong(@RequestParam("song") String song) {
		return genreService.getGenresBySong(song);
	}
	
	@GetMapping(path="/searchBySongId")
	public @ResponseBody List<Genre> getGenresBySongId(@RequestParam("songId") Integer songId) {
		return genreService.getGenresBySongId(songId);
	}
	
	@GetMapping(path="/searchByPerformer")
	public @ResponseBody List<Genre> getGenresByPerformer(@RequestParam("performer") String performer) {
		return genreService.getGenresByPerformer(performer);
	}
	
	@GetMapping(path="/searchByPerformerId")
	public @ResponseBody List<Genre> getGenresByPerformerId(@RequestParam("performerId") Integer performerId) {
		return genreService.getGenresByPerformerId(performerId);
	}
	
	@DeleteMapping(path="/delete")
	public @ResponseBody String deleteGenre(@RequestParam("genreId") Integer genreId) {
		return genreService.deleteGenre(genreId);
	}
	
	@PostMapping(path="/update")
	public @ResponseBody String updateGenre(@RequestBody GenreDAO genre) {
		return genreService.updateGenre(genre);
	}
}
