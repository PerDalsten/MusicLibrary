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

	public Album getAlbum() {
		Album album = new Album(artist, title, year);
		album.setId(id);

		for (Song s : songs)
			album.addSong(s);

		return album;
	}
}
