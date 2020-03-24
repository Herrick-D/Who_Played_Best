package com.example.interview.repositories;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.interview.daos.GenreDAO;
import com.example.interview.models.Genre;

@Repository
public class GenreRepository {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired 
	private PerformerRepository performerRepo;

	public List<Genre> getAllGenres() {
		List<GenreDAO> genres = getAllGenreDAOs();
		return setGenreData(genres);
	}

	private List<Genre> setGenreData(List<GenreDAO> genres) {
		List<Genre> result = new ArrayList<Genre>();
		for(GenreDAO i : genres) {
			Genre temp = new Genre();
			temp.setGenreId(i.getGenreId());
			temp.setGenre(i.getGenre());
			
			temp.setPerformers(performerRepo.getPerformersByGenreId(i.getGenreId()));
			result.add(temp);
		}
		return result;
	}

	private List<GenreDAO> getAllGenreDAOs() {
		String sql = "SELECT * FROM genre ";
		return jdbcTemplate.query(sql, (rs, rowNum) -> new GenreDAO(rs.getInt("genre_id"),
																																rs.getString("genre")));
	}

	public List<Genre> getGenresBySong(String song) {
		List<GenreDAO> genres = getGenreDAOsBySong(song);
		return setGenreData(genres);
	}

	private List<GenreDAO> getGenreDAOsBySong(String song) {
		String sql = "SELECT g.genre_id, g.genre "
							 + "FROM genre g "
							 + "LEFT JOIN song_genre sg ON g.genre_id = sg.genre_id "
							 + "LEFT JOIN song s ON sg.song_id = s.song_id "
							 + "WHERE song_title LIKE ? ";
		return jdbcTemplate.query(sql, new Object[] {"%"+song+"%"}, (rs, rowNum) -> new GenreDAO(rs.getInt("genre_id"),
																																														 rs.getString("genre")));
	}

	public List<Genre> getGenresBySongId(Integer songId) {
		List<GenreDAO> genres = getGenreDAOsbySongId(songId);
		return setGenreData(genres);
	}

	private List<GenreDAO> getGenreDAOsbySongId(Integer songId) {
		String sql = "SELECT g.genre_id, g.genre "
							 + "FROM genre g "
							 + "LEFT JOIN song_genre sg ON g.genre_id = sg.genre_id "
							 + "LEFT JOIN song s ON sg.song_id = s.song_id "
							 + "WHERE song_id = ? ";
		return jdbcTemplate.query(sql, new Object[] {songId}, (rs, rowNum) -> new GenreDAO(rs.getInt("genre_id"),
																																											 rs.getString("genre")));
	}

	public List<Genre> getGenresByPerformer(String performer) {
		List<GenreDAO> genres = getGenreDAOsByPerformer(performer);
		return setGenreData(genres);
	}

	private List<GenreDAO> getGenreDAOsByPerformer(String performer) {
		String sql = "SELECT g.genre_id, g.genre "
							 + "FROM genre g "
							 + "LEFT JOIN song_genre sg ON g.genre_id = sg.genre_id "
							 + "LEFT JOIN song_performer sp ON sg.song_id = sp.song_id "
							 + "LEFT JOIN performer p ON sp.performer_id = p.performer_id "
							 + "WHERE p.performer LIKE ? ";
		return jdbcTemplate.query(sql, new Object[] {"%"+performer+"%"}, (rs, rowNum) -> new GenreDAO(rs.getInt("genre_id"),
																																																	rs.getString("genre")));
	}

	public List<Genre> getGenresByPerformerId(Integer performerId) {
		List<GenreDAO> genres = getGenreDAOsByPerformerId(performerId);
		return setGenreData(genres);
	}

	private List<GenreDAO> getGenreDAOsByPerformerId(Integer performerId) {
		String sql = "SELECT g.genre_id, g.genre "
				 			 + "FROM genre g "
				 			 + "LEFT JOIN song_genre sg ON g.genre_id = sg.genre_id "
				 			 + "LEFT JOIN song_performer sp ON sg.song_id = sp.song_id "
				 			 + "LEFT JOIN performer p ON sp.performer_id = p.performer_id "
				 			 + "WHERE p.performer LIKE ? ";
		return jdbcTemplate.query(sql, new Object[] {performerId}, (rs, rowNum) -> new GenreDAO(rs.getInt("genre_id"),
																																														rs.getString("genre")));
	}

	@Transactional
	public String deleteGenre(Integer genreId) {
		String update = "";
		String sql = "DELETE genre FROM genre WHERE genre_id = ? ";
		int result = jdbcTemplate.update(sql, new Object[] {genreId});
		if(result > 0) {
			update = "Genre deleted.";
		}
		else {
			update = "Genre could not be deleted.";
		}
		return update;
	}

	public String updateGenre(GenreDAO genre) {
		String update = "";
		
		if(genre.getGenreId() != null) {
			update = updateGenreDAO(genre);
		}
		else {
			int insert = insertGenre(genre.getGenre());
			if(insert > 0) {
				update = "Genre added.";
			}
		}
		return update;
	}
	
	@Transactional
	private String updateGenreDAO(GenreDAO genre) {
		String update = "";
		Boolean exists = checkGenreExists(genre.getGenreId());
		if(exists) {
			String sql = "UPDATE genre SET genre = ? WHERE genre_id = ? ";
			int result = jdbcTemplate.update(sql, new Object[] {genre.getGenre(), genre.getGenreId()});
			if(result > 0) {
				update = "Genre updated successfully.";
			}
			else {
				update = "Genre was not updated.";
			}
		}
		else {
			update = "Genre does not exist.";
		}
			
		return update;
	}

	private Boolean checkGenreExists(Integer genreId) {
		String sql = "SELECT EXISTS (SELECT * FROM genre WHERE genre_id = ? ) ";
		return jdbcTemplate.queryForObject(sql, new Object[] {genreId}, Boolean.class);
	}

	@Transactional
	private Integer insertGenre(String genre) {
		KeyHolder key = new GeneratedKeyHolder();
		
		String sql = "INSERT INTO genre (genre) "
							 + "VALUES (?) ";
		
		jdbcTemplate.update(connection -> {
			PreparedStatement ps = connection.prepareStatement(sql, new String[] {"genre_id"});
			ps.setString(1, genre.toUpperCase());
			return ps;
		}, key);
		
		return key.getKey().intValue();
	}
}
