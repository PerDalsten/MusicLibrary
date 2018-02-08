package dk.purplegreen.musiclibrary;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
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

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public Album getAlbum(Integer id) throws MusicLibraryException {
		Album result = albumDAO.find(id);
		if (result == null)
			throw new AlbumNotFoundException("Album with id: " + id + " does not exist.");
		return result;
	}

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<Album> getAlbums() {
		return albumDAO.getAllAlbums();
	}

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<Album> findAlbums(String artist, String title, Integer year) {
		return albumDAO.find(artist, title, year);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Album createAlbum(Album album) throws MusicLibraryException {

		albumValidator.validate(album);

		album.setId(null);
		album.setArtist(getArtist(album.getArtist().getId()));
		attachSongs(album);

		return albumDAO.save(album);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Album updateAlbum(Album album) throws MusicLibraryException {

		albumValidator.validate(album);

		getAlbum(album.getId());
		album.setArtist(getArtist(album.getArtist().getId()));
		attachSongs(album);

		return albumDAO.save(album);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void deleteAlbum(Album album) throws MusicLibraryException {

		album = getAlbum(album.getId());

		albumDAO.delete(album);
	}

	private void attachSongs(Album album) {
		for (Song song : album.getSongs()) {
			song.setAlbum(album);
		}
	}

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public Artist getArtist(Integer id) throws MusicLibraryException {
		Artist result = artistDAO.find(id);
		if (result == null)
			throw new ArtistNotFoundException("Artist with id: " + id + " does not exist.");
		return result;
	}

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<Artist> getArtists() {
		return artistDAO.getAllArtists();
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Artist createArtist(Artist artist) throws MusicLibraryException {

		artistValidator.validate(artist);

		artist.setId(null);
		return artistDAO.save(artist);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Artist updateArtist(Artist artist) throws MusicLibraryException {

		artistValidator.validate(artist);

		getArtist(artist.getId());
		return artistDAO.save(artist);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void deleteArtist(Artist artist) throws MusicLibraryException {

		artist = getArtist(artist.getId());

		if (albumDAO.getArtistAlbumCount(artist) > 0) {
			throw new InvalidArtistException("Artist has albums and cannot be deleted");
		}

		artistDAO.delete(artist);
	}

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<Album> getAlbums(Artist artist) throws MusicLibraryException {

		artist = getArtist(artist.getId());

		return albumDAO.findByArtist(artist);
	}
}
