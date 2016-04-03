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
import dk.purplegreen.musiclibrary.MusicLibraryService;
import dk.purplegreen.musiclibrary.model.Album;
import dk.purplegreen.musiclibrary.model.Song;

@Named(value = "albumController")
@SessionScoped
public class AlbumController implements Serializable {

	private static final long serialVersionUID = 7960192974336036316L;

	private final static Logger log = LogManager.getLogger(AlbumController.class);

	@Inject
	private MusicLibraryService musicLibraryService;

	private String artist;
	private String title;
	private Integer year;
	private Integer id;
	private List<Album> albums = new ArrayList<>();
	private Album album;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		if (log.isDebugEnabled()) {
			log.debug("Calling setId: " + id);
		}
		this.id = id;
	}

	public List<Album> getAlbums() {
		return albums;
	}

	public String getArtist() {
		return artist;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public String search() {
		try {
			albums = musicLibraryService.findAlbums(getArtist(), getTitle(), getYear());
			return "index";
		} catch (Exception e) {
			return handleException(e);
		}
	}

	public String clearSearch() {
		try {
			albums = new ArrayList<>();
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
			if (album == null || !album.getId().equals(getId())) {
				if (-1 == getId()) {

					if (log.isDebugEnabled()) {
						log.debug("Creating new album");
					}

					album = new Album();
					album.setId(-1);
				} else {
					if (log.isDebugEnabled()) {
						log.debug("Reading album with id: " + getId());
					}

					album = musicLibraryService.getAlbum(getId());
				}
			} else {
				if (log.isDebugEnabled()) {
					log.debug("Already has copy of album with id: " + getId());
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

		if (album.getId() == -1) {
			album.setId(null);
			album = musicLibraryService.createAlbum(album);
		} else {
			try {
				album = musicLibraryService.updateAlbum(album);
			} catch (AlbumNotFoundException e) {
				return handleException(e);
			}
		}

		return "album?id=" + album.getId();
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
			log.debug("Cancel album edit: " + album.getArtist() + " " + album.getTitle());
		}

		String result;
		if (album.getId() == -1) {
			result = "index";
		} else {
			result = "album?id=" + getId();
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

		return "edit?id=" + album.getId();
	}

	public String deleteSong(Song song) {
		if (log.isDebugEnabled()) {
			log.debug("Deleting song: " + song.getId() + " " + song.getTitle());
		}

		album.getSongs().remove(song);

		return "edit?id=" + album.getId();
	}

	private String errorMessage;

	public String getErrorMessage() {
		return errorMessage;
	}

	private String handleException(Exception e) {
		errorMessage = e.getMessage();
		log.error("Erception caught in AlbumController", e);
		return "error";
	}
}
