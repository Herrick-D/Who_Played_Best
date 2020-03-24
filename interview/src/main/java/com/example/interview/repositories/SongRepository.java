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
import com.example.interview.daos.PerformerDAO;
import com.example.interview.daos.SongDAO;
import com.example.interview.models.Song;

@Repository
public class SongRepository {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public List<Song> getAllSongs(){
		List<SongDAO> songs = getAllSongsFromDB();
		return getSongData(songs);
	}
	
	private List<SongDAO> getAllSongsFromDB() {
		String sql = "SELECT * FROM  song ";
		return jdbcTemplate.query(sql, (rs, rowNum) -> new SongDAO(rs.getInt("song_id"),
																															 rs.getString("song_title"),
																															 rs.getInt("year")));
	}
	
	private List<PerformerDAO> getPerformersBySongId(Integer songId) {
		String sql = "SELECT p.* "
							 + "FROM song s "
							 + "LEFT JOIN song_performer sp ON s.song_id = sp.song_id "
							 + "LEFT JOIN performer p ON sp.performer_id = p.performer_id "
							 + "WHERE s.song_id = ? ";
		return jdbcTemplate.query(sql, new Object[] {songId}, (rs, rowNum) -> new PerformerDAO(rs.getInt("performer_id"),
																																													 rs.getString("performer")));
	}
	
	private List<GenreDAO> getGenresBySongId(Integer songId) {
		String sql = "SELECT g.* "
							 + "FROM song s "
							 + "LEFT JOIN song_genre sg ON s.song_id = sg.song_id "
							 + "LEFT JOIN genre g ON sg.genre_id = g.genre_id "
							 + "WHERE s.song_id = ? ";
		return jdbcTemplate.query(sql, new Object[] {songId}, (rs, rowNum) -> new GenreDAO(rs.getInt("genre_id"),
																																											 rs.getString("genre")));
	}

	public List<Song> getSongsByPerformerSearch(String performer) {
		List<SongDAO> songs = getSongsByPerfomer(performer);
		return getSongData(songs);
	}

	private List<SongDAO> getSongsByPerfomer(String performer) {
		String sql = "SELECT s.* "
							 + "FROM song s "
							 + "LEFT JOIN song_performer sp ON s.song_id = sp.song_id "
							 + "LEFT JOIN performer p ON sp.performer_id = p.performer_id "
							 + "WHERE p.performer LIKE ?";
		return jdbcTemplate.query(sql, new Object[] {"%"+performer+"%"}, (rs, rowNum) -> new SongDAO(rs.getInt("song_id"),
																																											   rs.getString("song_title"),
																																											   rs.getInt("year")));
	}
	
	private List<Song> getSongData(List<SongDAO> songs) {
		List<Song> result = new ArrayList<Song>();
		
		for(SongDAO i : songs) {
			Song song = new Song();
			List<PerformerDAO> performers = getPerformersBySongId(i.getSongId());
			
			List<GenreDAO> genres = getGenresBySongId(i.getSongId());
			Double rating = getSongRatingBySongId(i.getSongId());
			
			song.setSongId(i.getSongId());
			song.setSongTitle(i.getSongTitle());
			song.setPerformers(performers);
			song.setGenres(genres);
			song.setRating(rating);
			song.setYear(i.getYear());
			
			result.add(song);
		}
		return result;
	}

	private Double getSongRatingBySongId(Integer songId) {
		Boolean exists = checkSongRatingExists(songId);
		Double result = 0.0;
		if(exists) {
			String sql = "SELECT AVG(IFNULL(rating, 0.0)) as rating FROM song_rating WHERE song_id = ? GROUP BY song_id";
			result = jdbcTemplate.queryForObject(sql, new Object[] {songId}, Double.class);
		}
		return result;
	}

	private Boolean checkSongRatingExists(Integer songId) {
		String sql = "SELECT EXISTS (SELECT song_id FROM song_rating WHERE song_id = ? )";
		Boolean result = false;
		List<Boolean> temp = jdbcTemplate.query(sql, new Object[] {songId}, (rs, rowNum) -> new Boolean(rs.getBoolean(1)));
		if(!temp.isEmpty()) {
			result = temp.get(0);
		}
		return result;
	}

	public List<Song> getSongsByGenreSearch(String genre) {
		List<SongDAO> songs = getSongsByGenre(genre);
		return getSongData(songs);
	}

	private List<SongDAO> getSongsByGenre(String genre) {
		String sql = "SELECT s.* "
				       + "FROM song s "
				       + "LEFT JOIN song_genre sg ON s.song_id = sg.song_id "
				       + "LEFT JOIN genre g ON sg.genre_id = g.genre_id "
				       + "WHERE g.genre LIKE ? ";
		return jdbcTemplate.query(sql, new Object[] {"%"+genre.toUpperCase()+"%"}, (rs, rowNum) -> new SongDAO(rs.getInt("song_id"),
				 																											                               rs.getString("song_title"),
				 																											                               rs.getInt("year")));
	}

	public List<Song> getSongsByYear(Integer year) {
		List<SongDAO> songs = getSongsDaoByYear(year);
		return getSongData(songs);
	}

	private List<SongDAO> getSongsDaoByYear(Integer year) {
		String sql = "SELECT * FROM song WHERE year = ? ";
		return jdbcTemplate.query(sql, new Object[] {year}, (rs, rowNum) -> new SongDAO(rs.getInt("song_id"),
																																										rs.getString("song_title"),
																																										rs.getInt("year")));
	}

	@Transactional
	public List<String> insertSong(Song song) {
		List<String> updates = new ArrayList<String>();
		SongDAO songData = setSongDaoData(song);
		Integer songId = insertSongToDB(songData);
		if(songId != null) {
			updates.add("Song inserted successfully.");
			song.setSongId(songId);
			
			updates.addAll(insertPerformersForSong(song));
			updates.addAll(insertGenresForSong(song));
			if(songData.getRating() != null) {
				insertRating(song.getSongId(), songData.getRating());
			}
		}
		else {
			updates.add("Song was not entered.");
		}
		return updates;
	}

	@Transactional
	private List<String> insertGenresForSong(Song song) {
		List<String> updates = new ArrayList<String>();
		List<GenreDAO> genreList = song.getGenres();
		
		for(GenreDAO i : genreList) {
			Integer genreId = getGenreId(i.getGenre());
			if(genreId == null) {
				i.setGenreId(insertGenre(i.getGenre()));
			}
			if(i.getGenreId() != null) {
				updates.add(insertSongGenre(song.getSongId(), i));
			}
			else {
				updates.add("Genre could not be inserted.");
			}
		}
		return updates;
	}

	@Transactional
	private String insertSongGenre(Integer songId, GenreDAO i) {
		String update = "";
		String sql = "INSERT INTO song_genre (song_id, genre_id) "
							 + "VALUES (?, ?) ";
		int result = jdbcTemplate.update(sql, new Object[] {songId, i.getGenreId()});
		if(result > 0) {
			update = "Song tied to genre "+i.getGenre().toUpperCase()+" successfully.";
		}
		else {
			update = "Song not tied to genre "+i.getGenre().toUpperCase()+".";
		}
		return update;
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

	private Integer getGenreId(String genre) {
		String sql = "SELECT genre_id FROM genre WHERE genre = ? ";
		Integer result = null;
		List<Integer> temp = jdbcTemplate.query(sql, new Object[] {genre.toUpperCase()}, (rs, rowNum) -> new Integer(rs.getInt("genre_id")));
		if(!temp.isEmpty()) {
			result = temp.get(0);
		}
		return result;
	}

	@Transactional
	private List<String> insertPerformersForSong(Song song) {
		List<String> updates = new ArrayList<String>();
		List<PerformerDAO> performerList = song.getPerformers();
		
		for(PerformerDAO i : performerList) {
			Integer performerId = getPerformerId(i.getPerformer());
			if(performerId == null) {
				i.setPerformerId(insertPerformer(i.getPerformer()));
			}
			if(i.getPerformerId() != null) {
				updates.add(insertSongPerformer(song.getSongId(), i));
			}
			else{
				updates.add("Performer could not be inserted.");
			}
		}
		return updates;
	}

	@Transactional
	private String insertSongPerformer(Integer songId, PerformerDAO performer) {
		String update = "";
		String sql = "INSERT INTO song_performer (song_id, performer_id) "
							 + "VALUES (?, ?) ";
		int result = jdbcTemplate.update(sql, new Object[] {songId, performer.getPerformerId()});
		if(result > 0) {
			update = "Performer "+performer.getPerformer()+" tied to song successfully.";
		}
		else {
			update = "Performer "+performer.getPerformer()+" not tied to song.";
		}
		return update;
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

	private Integer getPerformerId(String performer) {
		String sql = "SELECT performer_id FROM performer WHERE performer = ? ";
		Integer result = null;
		List<Integer> temp = jdbcTemplate.query(sql, new Object[] {performer}, (rs, rowNum) -> new Integer(rs.getInt("performer_id")));
		if(!temp.isEmpty()) {
			result = temp.get(0);
		}
		return result;
	}

	@Transactional
	private Integer insertSongToDB(SongDAO songData) {
		KeyHolder key = new GeneratedKeyHolder();
		
		String sql = "INSERT INTO song (song_title, year) "
							 + "VALUES (?, ?) ";
		
		jdbcTemplate.update(connection -> {
      PreparedStatement ps = connection
        .prepareStatement(sql, new String[] {"song_id"});
        ps.setString(1, songData.getSongTitle());
        ps.setInt(2, songData.getYear());
        return ps;
      }, key);

    return key.getKey().intValue();
	}

	private SongDAO setSongDaoData(Song song) {
		SongDAO data = new SongDAO();
		if(song.getSongId() != null) {
			data.setSongId(song.getSongId());
		}
		data.setSongTitle(song.getSongTitle());
		data.setYear(song.getYear());
		if(song.getRating() != null) {
			data.setRating(song.getRating());
		}
		return data;
	}

	@Transactional
	public String insertRating(Integer songId, Double rating) {
		String update = "";
		String sql = "INSERT INTO song_rating (song_id, rating) "
							 + "VALUES (?, ?) ";
		int result = jdbcTemplate.update(sql, new Object[] {songId, rating});
		
		if(result > 0) { 
			update = "New rating inserted successfully."; 
	  }
		else {
			update = "New rating was not inserted.";
		}
		
		return update;
	}

	@Transactional
	public String deleteSong(Integer songId) {
		String update = "";
		String sql = "DELETE FROM song WHERE song_id = ? ";
		
		int result = jdbcTemplate.update(sql, new Object[] {songId});
		
		if(result > 0) {
			update = "Song deleted successfully.";
		}
		else {
			update = "Song was not deleted.";
		}
		
		return update;
	}

	@Transactional
	public List<String> updateSong(Song song) {
		List<String> updates = new ArrayList<String>();
		SongDAO songDAO = setSongDaoData(song);
		
		updates.add(updateSongData(songDAO));
		updates.addAll(updatePerformers(song.getPerformers()));
		updates.addAll(updateGenres(song.getGenres()));
		return updates;
	}

	private List<String> updateGenres(List<GenreDAO> genres) {
		List<String> updates = new ArrayList<String>();
		for(GenreDAO i : genres) {
			Boolean exists = checkGenreExists(i.getGenreId());
			if(exists) {
				updates.add(updateGenre(i));
			}
			else {
				int insert = insertGenre(i.getGenre());
				if(insert > 0) {
					updates.add("Genre "+i.getGenre()+" added.");
				}
			}
		}
		return updates;
	}

	private Boolean checkGenreExists(Integer genreId) {
		String sql = "SELECT EXISTS (SELECT * FROM genre WHERE genre_id = ? )";
		return jdbcTemplate.queryForObject(sql, new Object[] {genreId}, Boolean.class);
	}

	private String updateGenre(GenreDAO genreDAO) {
		String update = "";
		String sql = "UPDATE genre SET genre = ? WHERE genre_id = ? ";
		int result = jdbcTemplate.update(sql, new Object[] {genreDAO.getGenre(), genreDAO.getGenreId()});
		if(result > 0) {
			update = "Genre updated successfully.";
		}
		else {
			update = "Genre was not updated.";
		}
		return update;
	}

	private List<String> updatePerformers(List<PerformerDAO> performers) {
		List<String> updates = new ArrayList<String>();
		for(PerformerDAO i : performers) {
			Boolean exists = checkPerformerExists(i.getPerformerId());
			if(exists) {
				updates.add(updatePerformer(i));
			}
			else {
				int insert = insertPerformer(i.getPerformer());
				if(insert > 0) {
					updates.add("Performer "+i.getPerformer()+" added.");
				}
			}
			
		}
		return updates;
	}

	private Boolean checkPerformerExists(Integer performerId) {
		String sql = "SELECT EXISTS (SELECT * FROM performer WHERE performer_id = ?)";
		return jdbcTemplate.queryForObject(sql, new Object[] {performerId}, Boolean.class);
	}

	private String updatePerformer(PerformerDAO performerDAO) {
		String update = "";
		String sql = "UPDATE performer SET performer = ? WHERE performer_id = ? ";
		int result = jdbcTemplate.update(sql, new Object[] {performerDAO.getPerformer(), performerDAO.getPerformerId()});
		if(result > 0) {
			update = "Performer updated successfully.";
		}
		else {
			update = "Performer was not updated.";
		}
		return update;
	}

	private String updateSongData(SongDAO songDAO) {
		String updates = "";
		Boolean exists = checkSongExists(songDAO.getSongId());
		if(exists) {
			String sql = "UPDATE song SET song_title = ?, year = ? WHERE song_id = ?";
			int result = jdbcTemplate.update(sql, new Object[] {songDAO.getSongTitle(), songDAO.getYear(), songDAO.getSongId()});
			if(result > 0) {
				updates = "Song updated successfully.";
			}
			else {
				updates = "Song was not updated.";
			}
		}
		
		return updates;
	}

	private Boolean checkSongExists(Integer songId) {
		String sql = "SELECT EXISTS (SELECT * FROM song WHERE song_id = ?) ";
		return jdbcTemplate.queryForObject(sql, new Object[] {songId}, Boolean.class);
	}
}
