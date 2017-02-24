package dk.purplegreen.musiclibrary.ui;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dk.purplegreen.musiclibrary.AlbumNotFoundException;
import dk.purplegreen.musiclibrary.ArtistNotFoundException;
import dk.purplegreen.musiclibrary.InvalidAlbumException;
import dk.purplegreen.musiclibrary.InvalidArtistException;
import dk.purplegreen.musiclibrary.MusicLibraryService;
import dk.purplegreen.musiclibrary.model.Album;
import dk.purplegreen.musiclibrary.model.Artist;
import dk.purplegreen.musiclibrary.model.Song;

@Named(value = "albumController")
@SessionScoped
public class AlbumController implements Serializable {

	private static final long serialVersionUID = 7960192974336036316L;

	private static final Logger log = LogManager.getLogger(AlbumController.class);

	@Inject
	private MusicLibraryService musicLibraryService;

	// Search
	private String albumArtist;
	private String albumTitle;
	private Integer albumYear;
	private List<Album> albums = new ArrayList<>();

	// Album edit
	private Integer albumId;
	private Album album;

	// Artist edit
	private Integer artistId;
	private Artist artist;

	public String getAlbumArtist() {
		return albumArtist;
	}

	public void setAlbumArtist(String artist) {
		this.albumArtist = artist;
	}

	public String getAlbumTitle() {
		return albumTitle;
	}

	public void setAlbumTitle(String title) {
		this.albumTitle = title;
	}

	public Integer getAlbumYear() {
		return albumYear;
	}

	public void setAlbumYear(Integer year) {
		this.albumYear = year;
	}

	public Integer getAlbumId() {
		return albumId;
	}

	public void setAlbumId(Integer albumId) {
		if (log.isDebugEnabled()) {
			log.debug("Calling setAlbumId: " + albumId);
		}
		this.albumId = albumId;
	}

	public List<Album> getAlbums() {
		return albums;
	}

	public String search() {
		albums = musicLibraryService.findAlbums(getAlbumArtist(), getAlbumTitle(), getAlbumYear());
		return "index";
	}

	public String clearSearch() {

		albums = new ArrayList<>();
		setAlbumArtist(null);
		setAlbumTitle(null);
		setAlbumYear(null);
		return "index";
	}

	public String create() {
		album = new Album();
		return "edit";
	}

	public Album getAlbum() throws AlbumNotFoundException {

		if (album == null || !album.getId().equals(getAlbumId())) {
			if (-1 == getAlbumId()) {

				if (log.isDebugEnabled()) {
					log.debug("Creating new album");
				}

				album = new Album();
				album.setId(-1);
				album.setArtist(new Artist());
			} else {
				if (log.isDebugEnabled()) {
					log.debug("Reading album with id: " + getAlbumId());
				}

				album = musicLibraryService.getAlbum(getAlbumId());
			}
		} else {
			if (log.isDebugEnabled()) {
				log.debug("Already has copy of album with id: " + getAlbumId());
			}
		}
		return album;
	}

	public String saveAlbum() throws AlbumNotFoundException, ArtistNotFoundException, InvalidAlbumException {
		if (log.isDebugEnabled()) {
			log.debug("Saving album: " + album.getArtist() + " " + album.getTitle());
		}

		try {
			if (album.getId() == -1) {
				album.setId(null);
				album = musicLibraryService.createAlbum(album);
				// Need to set id to new value or -1 (empty) will be
				// shown/passed as query parameter to album.
				setAlbumId(album.getId());
			} else {
				album = musicLibraryService.updateAlbum(album);

				// If the updated album is in the search list, replace it with
				// updated album
				for (ListIterator<Album> iter = albums.listIterator(); iter.hasNext();) {
					if (iter.next().equals(album)) {
						iter.set(album);
						if (log.isDebugEnabled()) {
							log.debug("Replacing album in search list");
						}
						break;
					}
				}
			}
		} catch (AlbumNotFoundException | ArtistNotFoundException | InvalidAlbumException e) {
			album = null;
			throw e;
		}

		return "album";
	}

	public String deleteAlbum() throws AlbumNotFoundException {

		if (log.isDebugEnabled()) {
			log.debug("Deleting album: " + album.getArtist() + " " + album.getTitle());
		}

		musicLibraryService.deleteAlbum(album);
		albums.remove(album);

		album = null;

		return "index";
	}

	public String cancelEditAlbum() {
		if (log.isDebugEnabled()) {
			log.debug("Cancel album edit: " + album.getArtist().getName() + " " + album.getTitle());
		}

		String result;
		if (album.getId() == -1) {
			result = "index";
		} else {
			result = "album";
		}

		album = null;

		return result;
	}

	public String addSong() {
		if (log.isDebugEnabled()) {
			log.debug("Adding song");
		}

		if (!album.getSongs().isEmpty()) {
			Song song = album.getSongs().get(album.getSongs().size() - 1);
			album.addSong(new Song("", song.getTrack() + 1, song.getDisc()));
		} else {
			album.addSong(new Song("", album.getSongs().size() + 1));
		}

		return "edit";
	}

	public String deleteSong(Song song) {
		if (log.isDebugEnabled()) {
			log.debug("Deleting song: " + song.getId() + " " + song.getTitle());
		}

		album.getSongs().remove(song);

		return "edit";
	}

	public List<Artist> getArtists() {
		if (log.isDebugEnabled()) {
			log.debug("Get artists");
		}

		return musicLibraryService.getArtists();
	}

	public Integer getArtistId() {
		return artistId;
	}

	public void setArtistId(Integer artistId) {
		this.artistId = artistId;
	}

	public Artist getArtist() throws ArtistNotFoundException {

		if (artist == null || !artist.getId().equals(getArtistId())) {
			if (-1 == getArtistId()) {

				if (log.isDebugEnabled()) {
					log.debug("Creating new artist");
				}

				artist = new Artist();
				artist.setId(-1);

			} else {
				if (log.isDebugEnabled()) {
					log.debug("Reading artist with id: " + getArtistId());
				}

				artist = musicLibraryService.getArtist(getArtistId());
			}
		} else {
			if (log.isDebugEnabled()) {
				log.debug("Already has copy of artist with id: " + getArtistId());
			}
		}

		return artist;

	}

	public String saveArtist() throws InvalidArtistException, ArtistNotFoundException {

		if (log.isDebugEnabled()) {
			log.debug("Saving artist: " + artist.getId() + " " + artist.getName());
		}

		if (artist.getId() == -1) {
			musicLibraryService.createArtist(artist);
			return "edit";
		} else {
			musicLibraryService.updateArtist(artist);
			return "artistlist";
		}

	}

	public String deleteArtist() throws ArtistNotFoundException, InvalidArtistException {

		musicLibraryService.deleteArtist(new Artist(getArtistId()));
		
		return "artistlist";
	}

	public String cancelEditArtist() {

		if (getArtistId() == -1) {
			return "edit";
		} else {
			return "artistlist";
		}
	}

	public String artistsOK() {

		if (albumId == null) {
			return "index";
		} else {
			return "edit";
		}
	}
}
