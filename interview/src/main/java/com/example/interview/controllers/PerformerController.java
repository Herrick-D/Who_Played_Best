package com.example.interview.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.interview.daos.PerformerDAO;
import com.example.interview.models.Performer;
import com.example.interview.services.PerformerService;

@Controller
@RequestMapping(path="/performer")
public class PerformerController {
	@Autowired
	private PerformerService performerService;
	
	@GetMapping(path="/all")
	public @ResponseBody List<Performer> getAllPerformers() {
		return performerService.getAllPerformers();
	}
	
	@GetMapping(path="/searchByPerformer")
	public @ResponseBody List<Performer> getPerformersByPerformer(@RequestParam("performer") String performer) {
		return performerService.getPerformersByPerformer(performer);
	}
	
	@GetMapping(path="/searchBySong")
	public @ResponseBody List<Performer> getPerformersBySong(@RequestParam("song") String song) {
		return performerService.getPerformersBySong(song);
	}
	
	@GetMapping(path="/searchBySongId")
	public @ResponseBody List<Performer> getPerformersBySongId(@RequestParam("songId") Integer songId) {
		return performerService.getPerformersBySongId(songId);
	}
	
	@GetMapping(path="/searchByGenre")
	public @ResponseBody List<Performer> getPerformersByGenre(@RequestParam("genre") String genre) {
		return performerService.getPerformersByGenre(genre);
	}
	
	@GetMapping(path="/searchByGenreId")
	public @ResponseBody List<Performer> getPerformersByGenreId(@RequestParam("genreId") Integer genreId) {
		return performerService.getPerformersByGenreId(genreId);
	}
	
	@DeleteMapping(path="/delete")
	public @ResponseBody String deletePerformer(@RequestParam("performerId") Integer performerId) {
		return performerService.deletePerformer(performerId);
	}
	
	@PostMapping(path="/update")
	public @ResponseBody String updatePerformer(@RequestParam("performerId") PerformerDAO performer) {
		return performerService.updatePerformer(performer);
	}
	
}
