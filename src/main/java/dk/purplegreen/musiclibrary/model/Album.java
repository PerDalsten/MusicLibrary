package dk.purplegreen.musiclibrary.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

@Entity
@Table(name = "ALBUMS")
@NamedQueries({ @NamedQuery(name = "findAll", query = "SELECT a FROM Album a"),
		@NamedQuery(name = "findByArtist", query = "SELECT a FROM Album a WHERE a.artist = :artist"),
		@NamedQuery(name = "findByTitle", query = "SELECT a FROM Album a WHERE a.title = :title") })
public class Album {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column(name = "ALBUM_ARTIST")
	private String artist;
	@Column(name = "ALBUM_TITLE")
	private String title;
	@Column(name = "ALBUM_YEAR")
	private Integer year;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "album")
	@OrderBy("disc, track")
	private List<Song> songs = new ArrayList<Song>();

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public List<Song> getSongs() {
		return songs;
	}

	public void addSong(Song song) {
		if (song.getAlbum() != this) {
			song.setAlbum(this);
		}
		getSongs().add(song);
	}
}
