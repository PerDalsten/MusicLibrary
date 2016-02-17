package dk.purplegreen.musiclibrary.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(name = "SONGS")
public class Song {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column(name = "SONG_TITLE")
	private String title;
	private Integer track;
	private Integer disc;
	@ManyToOne 
	@JoinColumn(name = "ALBUM_ID")
	private Album album;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getTrack() {
		return track;
	}

	public void setTrack(Integer track) {
		this.track = track;
	}

	public Integer getDisc() {
		return disc;
	}

	public void setDisc(Integer disc) {
		this.disc = disc;
	}

	@XmlTransient //Avoid infinite recursion from bi-directional relationship when serializing to JSON
	public Album getAlbum() {
		return album;
	}

	public void setAlbum(Album album) {
		this.album = album;
	}
}
