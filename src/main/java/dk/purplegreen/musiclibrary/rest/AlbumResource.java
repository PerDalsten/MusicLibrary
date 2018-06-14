package dk.purplegreen.musiclibrary.rest;

import java.util.Optional;
import java.util.stream.Collectors;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import dk.purplegreen.musiclibrary.model.Album;
import dk.purplegreen.musiclibrary.model.Artist;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class AlbumResource {

	@XmlAccessorType(XmlAccessType.FIELD)
	static class Song {
		private Integer id;
		private String title;
		private Integer track;
		private Integer disc;

		public Song() {
		}

		public Song(dk.purplegreen.musiclibrary.model.Song song) {
			this.id = song.getId();
			this.title = song.getTitle();
			this.track = song.getTrack();
			this.disc = song.getDisc();
		}
	}

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

		songs = album.getSongs().stream().map(Song::new).collect(Collectors.toList())
				.toArray(new Song[album.getSongs().size()]);

	}

	public Album asAlbum() {

		Album album = new Album(id, artist, title, year);

		for (Song s : Optional.ofNullable(songs).orElse(new Song[0]))
			album.addSong(new dk.purplegreen.musiclibrary.model.Song(s.id, s.title, s.track, s.disc));

		return album;
	}
}
