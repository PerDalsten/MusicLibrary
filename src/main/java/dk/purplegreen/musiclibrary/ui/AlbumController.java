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

import dk.purplegreen.musiclibrary.MusicLibraryException;
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

	private List<Artist> artists;

	// Current album
	private Album album;

	// Current artist
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

	
	public Album getAlbum() throws MusicLibraryException {

		if (log.isDebugEnabled()) {
			log.debug("Getting album: " + album);
		}
		return album;
	}

	public String saveAlbum() throws MusicLibraryException {
		if (log.isDebugEnabled()) {
			log.debug("Saving album: " + album.getArtist() + " " + album.getTitle());
		}

		if (album.getId() == -1) {

			album = musicLibraryService.createAlbum(album);
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

		return "album";
	}

	public String deleteAlbum() throws MusicLibraryException {

		if (log.isDebugEnabled()) {
			log.debug("Deleting album: " + album.getArtist() + " " + album.getTitle());
		}

		musicLibraryService.deleteAlbum(album);
		albums.remove(album);

		album = null;

		return "index";
	}

	public String cancelEditAlbum() throws MusicLibraryException {
		if (log.isDebugEnabled()) {
			log.debug("Cancel album edit: " + album.getArtist().getName() + " " + album.getTitle());
		}

		String result;

		if (album.getId() == -1) {
			// New album - back to index
			result = "index";

		} else {
			// Existing album - refresh from service to reset any changes and
			// return to album
			album = musicLibraryService.getAlbum(album.getId());
			result = "album";
		}

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

		return artists;
	}

	public Artist getArtist() throws MusicLibraryException {

		if (log.isDebugEnabled()) {
			log.debug("Get artist: " + artist);
		}

		return artist;
	}

	public String saveArtist() throws MusicLibraryException {

		if (log.isDebugEnabled()) {
			log.debug("Saving artist: " + artist.getId() + " " + artist.getName());
		}

		String result;

		if (artist.getId() == -1) {
			musicLibraryService.createArtist(artist);
			result = "edit";
		} else {
			musicLibraryService.updateArtist(artist);
			result = "artistlist";
		}

		artists = musicLibraryService.getArtists();

		return result;
	}

	public String deleteArtist() throws MusicLibraryException {

		if (log.isDebugEnabled()) {
			log.debug("Deleting artist: " + artist);
		}

		musicLibraryService.deleteArtist(artist);

		artists = musicLibraryService.getArtists();

		return "artistlist";
	}

	public String cancelEditArtist() throws MusicLibraryException {

		if (artist.getId() == -1) {
			return "edit";
		} else {
			artist = musicLibraryService.getArtist(artist.getId());

			return "artistlist";
		}
	}

	public String artistsOK() {
		return "edit";
	}

	public String viewAlbum(Album album) {
		if (log.isDebugEnabled()) {
			log.debug("View album: " + album);
		}
		this.album = album;
		return "album";
	}

	public String newAlbum() {

		if (log.isDebugEnabled()) {
			log.debug("New album");
		}

		if (artists == null) {
			artists = musicLibraryService.getArtists();
		}
		
		this.album = new Album(-1);
		return "edit";
	}

	public String editAlbum() {

		if (log.isDebugEnabled()) {
			log.debug("Edit album: " + album);
		}

		if (artists == null) {
			artists = musicLibraryService.getArtists();
		}

		return "edit";
	}

	public String newArtist() {
		if (log.isDebugEnabled()) {
			log.debug("New artist");
		}

		this.artist = new Artist(-1);
		return "artist";
	}

	public String editArtists() {

		if (log.isDebugEnabled()) {
			log.debug("Edit artists");
		}
		if (artists == null) {
			artists = musicLibraryService.getArtists();
		}

		return "artistlist";
	}

	public String editArtist(Artist artist) {

		if (log.isDebugEnabled()) {
			log.debug("Edit artist: " + artist);
		}
		this.artist = artist;

		return "artist";
	}
}
