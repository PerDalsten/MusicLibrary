package dk.purplegreen.musiclibrary.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Entity
@Table(name = "album")
@NamedQueries({ @NamedQuery(name = "findAllAlbums", query = "SELECT a FROM Album a ORDER BY a.title ASC, a.artist.name ASC"),
		@NamedQuery(name = "findByArtist", query = "SELECT a FROM Album a WHERE a.artist = :artist"),
		@NamedQuery(name = "findByTitle", query = "SELECT a FROM Album a WHERE a.title = :title") })
public class Album implements Serializable {

	private static final long serialVersionUID = -3189225961340376887L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@ManyToOne
	@JoinColumn(name = "artist_id", nullable = false)
	private Artist artist;
	@Column(name = "album_title", nullable = false)
	private String title;
	@Column(name = "album_year", nullable = false)
	private Integer year;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "album", orphanRemoval = true, fetch = FetchType.EAGER)
	@OrderBy("disc, track")
	private List<Song> songs = new ArrayList<>();

	private static final Logger log = LogManager.getLogger(Album.class);

	public Album() {
	}

	public Album(Integer id) {
		this.id = id;
	}

	public Album(Artist artist, String title, Integer year) {
		this.artist = artist;
		this.title = title;
		this.year = year;
	}
	
	public Album(Integer id,Artist artist, String title, Integer year) {
		this.id = id;
		this.artist = artist;
		this.title = title;
		this.year = year;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Artist getArtist() {
		return artist;
	}

	public void setArtist(Artist artist) {
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
		song.setAlbum(this);
		getSongs().add(song);
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder("<");
		result.append(id);
		result.append("> ");
		result.append(title);
		return result.toString();
	}

	// hashCode and equals implemented to support collections and JSF - do not
	// use on non-persisted objects
	@Override
	public int hashCode() {
		if (id == null) {
			log.warn("hashCode() called for non-persisted object");
			return super.hashCode();
		} else {
			return id.hashCode();
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (id == null) {
			log.warn("equals() called for non-persisted object");
			return super.equals(obj);
		}

		if (obj == null)
			return false;

		if (getClass().equals(obj.getClass())) {
			return id.equals(((Album) obj).getId());
		}

		return false;
	}
}
