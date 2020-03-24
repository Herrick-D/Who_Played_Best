package com.example.interview.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.interview.daos.PerformerDAO;
import com.example.interview.models.Performer;
import com.example.interview.repositories.PerformerRepository;

@Service
public class PerformerService {
	@Autowired
	private PerformerRepository performerRepo;
	
	public List<Performer> getAllPerformers() {
		return performerRepo.getAllPerformers();
	}

	public List<Performer> getPerformersByPerformer(String performer) {
		return performerRepo.getPerformersByPerformer(performer);
	}

	public List<Performer> getPerformersBySong(String song) {
		return performerRepo.getPerformersBySong(song);
	}

	public List<Performer> getPerformersBySongId(Integer songId) {
		return performerRepo.getPerformersBySongId(songId);
	}

	public List<Performer> getPerformersByGenre(String genre) {
		return performerRepo.getPerformersByGenre(genre);
	}

	public List<Performer> getPerformersByGenreId(Integer genreId) {
		return performerRepo.getPerformersByGenreId(genreId);
	}

	public String deletePerformer(Integer performerId) {
		return performerRepo.deletePerformer(performerId);
	}

	public String updatePerformer(PerformerDAO performer) {
		return performerRepo.updatePerformer(performer);
	}

}
