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

import com.example.interview.daos.PerformerDAO;
import com.example.interview.models.Performer;

@Repository
public class PerformerRepository {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private SongRepository songRepo;
	
	public List<Performer> getAllPerformers() {
		List<PerformerDAO> performers = getAllPerformerDAOs();
		return setPerformerData(performers);
	}

	private List<PerformerDAO> getAllPerformerDAOs() {
		String sql = "SELECT performer_id, performer "
							 + "FROM performer ";
		return jdbcTemplate.query(sql, (rs, rowNum) -> new PerformerDAO(rs.getInt("performer_id"),
																																		rs.getString("performer")));
	}

	private List<Performer> setPerformerData(List<PerformerDAO> performerList) {
		List<Performer> result = new ArrayList<Performer>();
		for(PerformerDAO i : performerList) {
			Performer temp = new Performer();
			temp.setPerformerId(i.getPerformerId());
			temp.setPerformer(i.getPerformer());
			
			temp.setSongs(songRepo.getSongsByPerformerSearch(i.getPerformer()));
			result.add(temp);
		}
		return result;
	}
	
	
	public List<Performer> getPerformersByPerformer(String performer) {
		List<PerformerDAO> performers = getPerformersByPerformerSearch(performer);
		return setPerformerData(performers);
	}

	private List<PerformerDAO> getPerformersByPerformerSearch(String performer) {
		String sql = "SELECT performer_id, performer "
	 			 			 + "FROM performer "
	 			 			 + "WHERE performer LIKE ? ";
		return jdbcTemplate.query(sql, new Object[] {"%"+performer+"%"}, (rs, rowNum) -> new PerformerDAO(rs.getInt("performer_id"),
																																																			rs.getString("performer")));
	}

	public List<Performer> getPerformersBySong(String song) {
		List<PerformerDAO> performers = getPerformersBySongSearch(song);
		return setPerformerData(performers);
	}

	private List<PerformerDAO> getPerformersBySongSearch(String song) {
		String sql = "SELECT performer_id, performer "
	 			 			 + "FROM performer p "
	 			 			 + "LEFT JOIN song_performer sp ON p.performer_id = sp.performer_id "
	 			 			 + "LEFT JOIN song s ON sp.song_id = s.song_id "
	 			 			 + "WHERE s.song LIKE ? ";
		return jdbcTemplate.query(sql, new Object[] {"%"+song+"%"}, (rs, rowNum) -> new PerformerDAO(rs.getInt("performer_id"),
																																																 rs.getString("performer")));
	}

	public List<Performer> getPerformersBySongId(Integer songId) {
		List<PerformerDAO> performers = getPerformersDAOsBySongId(songId);
		return setPerformerData(performers);
	}

	private List<PerformerDAO> getPerformersDAOsBySongId(Integer songId) {
		String sql = "SELECT performer_id, performer "
	 			 			 + "FROM performer p "
	 			 			 + "LEFT JOIN song_performer sp ON p.performer_id = sp.performer_id "
	 			 			 + "LEFT JOIN song s ON sp.song_id = s.song_id "
	 			 			 + "WHERE s.song_id = ? ";
		return jdbcTemplate.query(sql, new Object[] {songId}, (rs, rowNum) -> new PerformerDAO(rs.getInt("performer_id"),
																																													 rs.getString("performer")));
	}

	public List<Performer> getPerformersByGenre(String genre) {
		List<PerformerDAO> performers = getPerformersDAOsByGenre(genre);
		return setPerformerData(performers);
	}

	private List<PerformerDAO> getPerformersDAOsByGenre(String genre) {
		String sql = "SELECT performer_id, performer "
	 			 			 + "FROM performer p "
	 			 			 + "LEFT JOIN song_performer sp ON p.performer_id = sp.performer_id "
	 			 			 + "LEFT JOIN song_genre sg ON sp.song_id = sg.song_id "
	 			 			 + "LEFT JOIN genre g ON sg.genre_id = g.genre_id "
	 			 			 + "WHERE g.genre LIKE ? ";
		return jdbcTemplate.query(sql, new Object[] {"%"+genre+"%"}, (rs, rowNum) -> new PerformerDAO(rs.getInt("performer_id"),
																																										 							rs.getString("performer")));
	}

	public List<Performer> getPerformersByGenreId(Integer genreId) {
		List<PerformerDAO> performers = getPerformersDAOsByGenreId(genreId);
		return setPerformerData(performers);
	}

	private List<PerformerDAO> getPerformersDAOsByGenreId(Integer genreId) {
		String sql = "SELECT performer_id, performer "
	 			 			 + "FROM performer p "
	 			 			 + "LEFT JOIN song_performer sp ON p.performer_id = sp.performer_id "
	 			 			 + "LEFT JOIN song_genre sg ON sp.song_id = sg.song_id "
	 			 			 + "LEFT JOIN genre g ON sg.genre_id = g.genre_id "
	 			 			 + "WHERE g.genre_id = ? ";
		return jdbcTemplate.query(sql, new Object[] {genreId}, (rs, rowNum) -> new PerformerDAO(rs.getInt("performer_id"),
																																							 							rs.getString("performer")));
	}

	@Transactional
	public String deletePerformer(Integer performerId) {
		String update = "";
		String sql = "DELETE FROM performer WHERE performer_id = ? ";
		int result = jdbcTemplate.update(sql, new Object[] {performerId});
		if(result > 0) {
			update = "Performer successfully deleted.";
		}
		else {
			update = "Performer could not be deleted.";
		}
		return update;
	}

	public String updatePerformer(PerformerDAO performer) {
		String updates = "";
		if(performer.getPerformerId() != null) {
			updates = updatePerformerDAO(performer);
		}
		else {
			int insert = insertPerformer(performer.getPerformer());
			if(insert > 0) {
				updates = "Performer added.";
			}
		}
		return updates;
	}
	
	@Transactional
	private Integer insertPerformer(String performer) {
		KeyHolder key = new GeneratedKeyHolder();
		
		String sql = "INSERT INTO performer (performer) "
							 + "VALUES (?) ";
		
		jdbcTemplate.update(connection -> {
			PreparedStatement ps = connection.prepareStatement(sql, new String[] {"performer_id"});
			ps.setString(1, performer);
			return ps;
		}, key);
		
		return key.getKey().intValue();
	}

	@Transactional
	private String updatePerformerDAO(PerformerDAO performer) {
		String update = "";
		Boolean exists = checkPerformerExists(performer.getPerformerId());
		if(exists) {
			String sql = "UPDATE performer SET performer = ? WHERE performer_id = ? ";
			int result = jdbcTemplate.update(sql, new Object[] {performer.getPerformer(), performer.getPerformerId()});
			if(result > 0) {
				update = "Performer update was successful";
			}
			else {
				update = "Performer update was not successful";
			}
		}
		else {
			update = "Performer does not exist.";
		}
		return update;
	}

	private Boolean checkPerformerExists(Integer performerId) {
		String sql = "SELECT EXISTS (SELECT * FROM performer WHERE performer_id = ?) ";
		return jdbcTemplate.queryForObject(sql, new Object[] {performerId}, Boolean.class);
	}
	
	
}
