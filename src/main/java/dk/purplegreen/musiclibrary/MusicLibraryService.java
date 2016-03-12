package dk.purplegreen.musiclibrary;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import dk.purplegreen.musiclibrary.model.Album;
import dk.purplegreen.musiclibrary.model.Song;
import dk.purplegreen.musiclibrary.persistence.AlbumDAO;

@Stateless
public class MusicLibraryService {
	@Inject
	private AlbumDAO dao;

	public Album getAlbum(Integer id) throws AlbumNotFoundException {

		Album result = dao.find(id);
		if (result == null)
			throw new AlbumNotFoundException("Album with id: " + id + " does not exist.");
		return result;
	}

	public List<Album> getAlbums() {
		return dao.getAllAlbums();
	}

	public List<Album> findAlbums(String artist, String title, Integer year) {
		return dao.find(artist, title, year);
	}

	public Album createAlbum(Album album) {
		album.setId(null);
		attachSongs(album);
		return dao.save(album);
	}

	public Album updateAlbum(Album album) throws AlbumNotFoundException {
		getAlbum(album.getId());
		attachSongs(album);
		return dao.save(album);
	}

	public void deleteAlbum(Album album) throws AlbumNotFoundException {
		dao.delete(getAlbum(album.getId()));
	}

	private void attachSongs(Album album) {
		for (Song song : album.getSongs()) {
			song.setAlbum(album);
		}
	}
}
