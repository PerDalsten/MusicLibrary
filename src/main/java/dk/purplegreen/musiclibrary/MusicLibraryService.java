package dk.purplegreen.musiclibrary;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import dk.purplegreen.musiclibrary.model.Album;
import dk.purplegreen.musiclibrary.model.Artist;
import dk.purplegreen.musiclibrary.model.Song;
import dk.purplegreen.musiclibrary.persistence.AlbumDAO;
import dk.purplegreen.musiclibrary.persistence.ArtistDAO;

@Stateless
public class MusicLibraryService {
	@Inject
	private AlbumDAO albumDAO;
	@Inject
	private ArtistDAO artistDAO;
	@Inject
	private ArtistValidator artistValidator;
	@Inject
	private AlbumValidator albumValidator;

	public Album getAlbum(Integer id) throws AlbumNotFoundException {
		Album result = albumDAO.find(id);
		if (result == null)
			throw new AlbumNotFoundException("Album with id: " + id + " does not exist.");
		return result;
	}

	public List<Album> getAlbums() {
		return albumDAO.getAllAlbums();
	}

	public List<Album> findAlbums(String artist, String title, Integer year) {
		return albumDAO.find(artist, title, year);
	}

	public Album createAlbum(Album album) throws ArtistNotFoundException, InvalidAlbumException {
		album.setId(null);
		album.setArtist(getArtist(album.getArtist().getId()));
		attachSongs(album);
		albumValidator.validate(album);
		return albumDAO.save(album);
	}

	public Album updateAlbum(Album album)
			throws AlbumNotFoundException, ArtistNotFoundException, InvalidAlbumException {
		getAlbum(album.getId());
		album.setArtist(getArtist(album.getArtist().getId()));
		attachSongs(album);
		albumValidator.validate(album);
		return albumDAO.save(album);
	}

	public void deleteAlbum(Integer id) throws AlbumNotFoundException {
		albumDAO.delete(getAlbum(id));
	}

	private void attachSongs(Album album) {
		for (Song song : album.getSongs()) {
			song.setAlbum(album);
		}
	}

	public Artist getArtist(Integer id) throws ArtistNotFoundException {
		Artist result = artistDAO.find(id);
		if (result == null)
			throw new ArtistNotFoundException("Artist with id: " + id + " does not exist.");
		return result;
	}

	public List<Artist> getArtists() {
		return artistDAO.getAllArtists();
	}

	public Artist createArtist(Artist artist) throws InvalidArtistException {
		artist.setId(null);
		artistValidator.validate(artist);
		return artistDAO.save(artist);
	}

	public Artist updateArtist(Artist artist) throws ArtistNotFoundException, InvalidArtistException {
		getArtist(artist.getId());
		artistValidator.validate(artist);
		return artistDAO.save(artist);
	}

	public void deleteArtist(Integer id) throws ArtistNotFoundException {
		artistDAO.delete(getArtist(id));
	}

	public List<Album> getAlbums(Artist artist) {
		return albumDAO.findByArtist(artist);
	}
}
