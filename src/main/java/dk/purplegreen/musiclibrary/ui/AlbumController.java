package dk.purplegreen.musiclibrary.ui;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import dk.purplegreen.musiclibrary.AlbumNotFoundException;
import dk.purplegreen.musiclibrary.MusicLibraryService;
import dk.purplegreen.musiclibrary.model.Album;

@Named(value = "albumController")
@SessionScoped
public class AlbumController implements Serializable {

	private static final long serialVersionUID = 7960192974336036316L;

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
		System.out.println("Calling setId: " + id);
		this.id = id;
		album = null;
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
		albums = musicLibraryService.findAlbums(getArtist(), getTitle(), getYear());
		return "index.xhtml";
	}

	public String create() {
		album = new Album();
		return "edit.xhtml";
	}

	public Album getAlbum() {
		try {
			if (album == null) {
				if (-1 == getId()) {
					album = new Album();
				} else {
					album = musicLibraryService.getAlbum(getId());
				}
			}
		} catch (AlbumNotFoundException e) {
			// TODO
			e.printStackTrace();
			return null;
		}
		return album;
	}

	public String save() {
		System.out.println("Saving album: " + album.getArtist() + " " + album.getTitle());
		return "album.xhtml?id=" + album.getId();
	}
}
