package com.example.interview.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.interview.daos.GenreDAO;
import com.example.interview.models.Genre;
import com.example.interview.repositories.GenreRepository;

@Service
public class GenreService {
	@Autowired
	private GenreRepository genreRepo;

	public List<Genre> getAllGenres() {
		return genreRepo.getAllGenres();
	}

	public List<Genre> getGenresBySong(String song) {
		return genreRepo.getGenresBySong(song);
	}

	public List<Genre> getGenresBySongId(Integer songId) {
		return genreRepo.getGenresBySongId(songId);
	}

	public List<Genre> getGenresByPerformer(String performer) {
		return genreRepo.getGenresByPerformer(performer);
	}

	public List<Genre> getGenresByPerformerId(Integer performerId) {
		return genreRepo.getGenresByPerformerId(performerId);
	}

	public String deleteGenre(Integer genreId) {
		return genreRepo.deleteGenre(genreId);
	}

	public String updateGenre(GenreDAO genre) {
		return genreRepo.updateGenre(genre);
	}
}
