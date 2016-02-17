package dk.purplegreen.musiclibrary;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import dk.purplegreen.musiclibrary.model.Album;
import dk.purplegreen.musiclibrary.persistence.AlbumDAO;

@Stateless
public class MusicLibraryService {
	@Inject
	private AlbumDAO dao;

	public Album getAlbum(Integer id) {
		return dao.find(id);
	}

	public List<Album> getAlbums() {
		return dao.getAllAlbums();
	}

	public List<Album> findAlbums(String artist, String title, Integer year) {
		return dao.find(artist, title, year);
	}	
}
