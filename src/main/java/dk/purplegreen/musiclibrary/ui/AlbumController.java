package dk.purplegreen.musiclibrary.ui;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dk.purplegreen.musiclibrary.AlbumNotFoundException;
import dk.purplegreen.musiclibrary.ArtistNotFoundException;
import dk.purplegreen.musiclibrary.MusicLibraryService;
import dk.purplegreen.musiclibrary.model.Album;
import dk.purplegreen.musiclibrary.model.Artist;
import dk.purplegreen.musiclibrary.model.Song;

@Named(value = "albumController")
@SessionScoped
public class AlbumController implements Serializable {

	private static final long serialVersionUID = 7960192974336036316L;

	private final static Logger log = LogManager.getLogger(AlbumController.class);

	@Inject
	private MusicLibraryService musicLibraryService;

	// Search
	private String albumArtist;
	private String albumTitle;
	private Integer albumYear;

	// Album edit
	private Integer albumId;
	private List<Album> albums = new ArrayList<>();
	private Album album;

	// Artist edit
	private Integer artistId;
	private String artistName;

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

	public Integer getAlbumArtistId() throws AlbumNotFoundException {
		return getAlbum().getArtist() != null ? getAlbum().getArtist().getId() : null;
	}

	public void setAlbumArtistId(Integer artistId) throws AlbumNotFoundException {
		getAlbum().getArtist().setId(artistId);
	}

	public String search() {
		try {
			albums = musicLibraryService.findAlbums(getAlbumArtist(), getAlbumTitle(), getAlbumYear());
			return "index";
		} catch (Exception e) {
			return handleException(e);
		}
	}

	public String clearSearch() {
		try {
			albums = new ArrayList<>();
			setAlbumArtist(null);
			setAlbumTitle(null);
			setAlbumYear(null);
			return "index";
		} catch (Exception e) {
			return handleException(e);
		}
	}

	public String create() {
		album = new Album();
		return "edit";
	}

	public Album getAlbum() throws AlbumNotFoundException {
		try {
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
		} catch (AlbumNotFoundException e) {
			log.error("Exception caught in getAlbum", e);
			throw e;
		}
		return album;
	}

	public String save() {
		if (log.isDebugEnabled()) {
			log.debug("Saving album: " + album.getArtist() + " " + album.getTitle());
		}

		try {
			if (album.getId() == -1) {
				album.setId(null);
				album = musicLibraryService.createAlbum(album);
				// Need to set id to new value or -1 (empty) will be
				// shown/passed as
				// query parameter to album.
				setAlbumId(album.getId());
			} else {
				album = musicLibraryService.updateAlbum(album);

				// If the updated album is in the search list, replace it with
				// updated album
				for (int i = 0; i < albums.size(); i++) {
					if (albums.get(i).getId().equals(album.getId())) {
						albums.set(i, album);
						break;
					}
				}
			}
		} catch (AlbumNotFoundException | ArtistNotFoundException e) {
			return handleException(e);
		}

		return "album";
	}

	public String delete() {

		if (log.isDebugEnabled()) {
			log.debug("Deleting album: " + album.getArtist() + " " + album.getTitle());
		}

		if (album.getId() != -1) {
			try {
				musicLibraryService.deleteAlbum(album.getId());

				// Album object in search list not same as retrieved object
				// -possibly not a good idea to override equals for JPA entity?
				albums.removeIf(new Predicate<Album>() {
					@Override
					public boolean test(Album a) {
						if (a.getId().equals(album.getId())) {
							if (log.isDebugEnabled()) {
								log.debug("Deleting album from search list: " + album.getId());
							}
							return true;
						} else {
							return false;
						}
					}
				});
			} catch (Exception e) {
				return handleException(e);
			}
		}

		album = null;

		return "index";
	}

	public String cancel() {
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

		if (album.getSongs().size() > 0) {
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

	public String getArtistName() throws ArtistNotFoundException {

		if (artistName == null) {
			if (getArtistId() != null && -1 != getArtistId()) {
				artistName = musicLibraryService.getArtist(getArtistId()).getName();
			}
		}
		return artistName;
	}

	public void setArtistName(String artistName) {
		this.artistName = artistName;
	}

	public Integer getArtistId() {
		return artistId;
	}

	public void setArtistId(Integer artistId) {
		this.artistId = artistId;
	}

	public String saveArtist() throws ArtistNotFoundException {

		if (log.isDebugEnabled()) {
			log.debug("Saving artist: " + getArtistId() + " " + getArtistName());

		}
		Artist artist = new Artist();

		artist.setName(getArtistName());
		setArtistName(null);

		if (getArtistId() == -1) {
			musicLibraryService.createArtist(artist);
			return "edit";
		} else {
			artist.setId(getArtistId());
			musicLibraryService.updateArtist(artist);
			return "artistlist";
		}
	}

	public String deleteArtist() throws ArtistNotFoundException {
		setArtistName(null);
		if (getArtistId() == -1) {
			return "edit";
		} else {
			musicLibraryService.deleteArtist(getArtistId());
			return "artistlist";
		}
	}

	public String cancelEditArtist() {
		setArtistName(null);
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

	private String errorMessage;

	public String getErrorMessage() {
		return errorMessage;
	}

	private String handleException(Exception e) {
		errorMessage = e.getMessage();
		log.error("Exception caught in AlbumController", e);
		return "error";
	}
}
