package persistence;

import business.Playlist;
import org.junit.jupiter.api.Test;
import business.Song;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PlaylistDaoImplTest {
    private MySQLDao connectionSource = new MySQLDao("database_test.properties");


    /*
    at current time of making tests, connection to dummy db isnt working. Unsure if tests are correct or wrong
     */

    /**
     * Tests the successful creation of a playlist.
     */
    @Test
    void createPlaylist() throws SQLException {
        Connection conn = connectionSource.getConnection();
        conn.setAutoCommit(false);
        PlaylistDaoImpl playlistDao = new PlaylistDaoImpl(conn);

        Playlist newPlaylist = Playlist.builder()
                .userId(1) //assumes user id is 1
                .name("My Playlist")
                .isPublic(true)
                .songs(null)
                .build();

        boolean result = playlistDao.createPlaylist(newPlaylist);
        int generatedId = newPlaylist.getPlaylistId();

        assertTrue(result, "Playlist should be created successfully.");
        assertTrue(generatedId > 0, "Generated Playlist ID should be greater than 0.");

        Playlist insertedPlaylist = playlistDao.getPlaylistById(generatedId);
        assertNotNull(insertedPlaylist, "Inserted Playlist should not be null.");
        assertEquals(newPlaylist.getUserId(), insertedPlaylist.getUserId(), "User IDs should match.");
        assertEquals(newPlaylist.getName(), insertedPlaylist.getName(), "Playlist names should match.");
        assertEquals(newPlaylist.isPublic(), insertedPlaylist.isPublic(), "Visibility should match.");
        assertNull(insertedPlaylist.getSongs(), "Songs list should be null initially.");

        conn.commit();
    }

    /**
     * Tests updating an existing playlist.
     */
    @Test
    void updatePlaylist() throws SQLException {
        Connection conn = connectionSource.getConnection();
        conn.setAutoCommit(false);
        PlaylistDaoImpl playlistDao = new PlaylistDaoImpl(conn);

        // create new playlist to update
        Playlist originalPlaylist = Playlist.builder()
                .userId(2) // assumes id 2 exists
                .name("Original Playlist")
                .isPublic(false)
                .songs(null)
                .build();

        boolean createResult = playlistDao.createPlaylist(originalPlaylist);
        assertTrue(createResult, "Playlist should be created successfully.");
        int playlistId = originalPlaylist.getPlaylistId();
        assertTrue(playlistId > 0, "Generated Playlist ID should be greater than 0.");

        Playlist updatedPlaylist = Playlist.builder()
                .playlistId(playlistId)
                .userId(2)
                .name("Updated Playlist")
                .isPublic(true) // if updated visibility
                .songs(null) // no change in songs
                .build();

        boolean updateResult = playlistDao.updatePlaylist(updatedPlaylist);

        assertTrue(updateResult, "Playlist should be updated successfully.");
        Playlist fetchedPlaylist = playlistDao.getPlaylistById(playlistId);
        assertNotNull(fetchedPlaylist, "Updated playlist should not be null.");
        assertEquals(updatedPlaylist.getName(), fetchedPlaylist.getName(), "Playlist name should be updated.");
        assertEquals(updatedPlaylist.isPublic(), fetchedPlaylist.isPublic(), "Visibility should be updated.");
        assertNull(fetchedPlaylist.getSongs(), "Songs list should remain unchanged.");

        conn.commit();
        conn.close();
    }
//
    /**
     * Tests adding songs to a playlist.
     */
    @Test
    void addSongToPlaylist() throws SQLException {
        Connection conn = connectionSource.getConnection();
        conn.setAutoCommit(false);
        PlaylistDaoImpl playlistDao = new PlaylistDaoImpl(conn);

        Playlist newPlaylist = Playlist.builder()
                .userId(3) // assumes id 3 exists
                .name("Rock Classics")
                .isPublic(true)
                .songs(null)
                .build();

        boolean createResult = playlistDao.createPlaylist(newPlaylist);
        assertTrue(createResult, "Playlist should be created successfully.");
        int playlistId = newPlaylist.getPlaylistId();
        assertTrue(playlistId > 0, "Generated Playlist ID should be greater than 0.");

        // this assumes the following song ids actually exists in db
        int songId1 = 1;
        int songId2 = 2;

        boolean addResult1 = playlistDao.addSongToPlaylist(playlistId, songId1);
        boolean addResult2 = playlistDao.addSongToPlaylist(playlistId, songId2);

        assertTrue(addResult1, "Song 1 should be added successfully.");
        assertTrue(addResult2, "Song 2 should be added successfully.");

        // check if songs are in playlst
        List<Song> songsInPlaylist = playlistDao.getSongsInPlaylist(playlistId);
        assertNotNull(songsInPlaylist, "Songs list should not be null.");
        assertEquals(2, songsInPlaylist.size(), "Playlist should contain two songs.");

        conn.commit();
        conn.close();
    }  }
//
//    @Test
//    void removeSongFromPlaylist() {
//    }
//
//    @Test
//    void getPlaylistById() {
//    }
//
//    @Test
//    void getUserPlaylists() {
//    }
//
//    @Test
//    void getPublicPlaylists() {
//    }
//
//    @Test
//    void getSongsInPlaylist() {
//    }
    }