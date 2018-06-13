package dk.purplegreen.musiclibrary.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "song")
public class Song implements Serializable {

	private static final long serialVersionUID = 5642619570047715383L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column(name = "song_title", nullable = false)
	private String title;
	private Integer track;
	private Integer disc;
	@ManyToOne
	@JoinColumn(name = "album_id", nullable = false)
	private Album album;

	public Song() {
	}

	public Song(String title, Integer track) {
		this(title, track, 1);
	}

	public Song(String title, Integer track, Integer disc) {
		this.title = title;
		this.track = track;
		this.disc = disc;
	}

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
	 
	public void setAlbum(Album album) {
		this.album = album;
	}
}
