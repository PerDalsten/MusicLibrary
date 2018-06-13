package dk.purplegreen.musiclibrary.rest;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import dk.purplegreen.musiclibrary.model.Album;
import dk.purplegreen.musiclibrary.model.Artist;
import dk.purplegreen.musiclibrary.model.Song;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class AlbumResource {

	private Integer id;
	private Artist artist;
	private String title;
	private Integer year;
	private Song[] songs;

	public AlbumResource() {
	}

	public AlbumResource(Album album) {
		id = album.getId();
		artist = album.getArtist();
		title = album.getTitle();
		year = album.getYear();
		songs = album.getSongs().toArray(new Song[album.getSongs().size()]);
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

	public Song[] getSongs() {
		return songs;
	}

	public void setSongs(Song[] songs) {
		this.songs = songs;
	}

	public Album getAlbum() {
		Album album = new Album(artist, title, year);
		album.setId(id);

		if (songs != null) {
			for (Song s : songs)
				album.addSong(s);
		}
		return album;
	}
}
