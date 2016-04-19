package dk.purplegreen.musiclibrary.persistence;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import dk.purplegreen.musiclibrary.model.Album;
import dk.purplegreen.musiclibrary.model.Artist;
import dk.purplegreen.musiclibrary.model.Song;

public class TestEntityManagerFactory {

	static {
		System.setProperty("derby.stream.error.file", "logs/derby.log");
	}

	private static EntityManagerFactory factory = Persistence.createEntityManagerFactory("MusicLibraryTest");

	private static boolean isDataCreated = false;

	public static synchronized EntityManager getEntityManager() {

		EntityManager em = factory.createEntityManager();

		if (!isDataCreated) {
			createData(em);
		}
		return em;
	}

	private static void createData(EntityManager em) {

		em.getTransaction().begin();

		Artist artist = new Artist("The Beatles");
		em.persist(artist);

		Album album = new Album();
		album.setArtist(artist);
		album.setTitle("Abbey Road");
		album.setYear(1969);

		Song song = new Song();
		song.setTitle("Come Together");
		song.setDisc(1);
		song.setTrack(1);
		song.setAlbum(album);

		album.addSong(song);

		song = new Song();
		song.setTitle("Something");
		song.setDisc(1);
		song.setTrack(2);
		song.setAlbum(album);
		album.addSong(song);

		em.persist(album);

		artist = new Artist("AC/DC");
		em.persist(artist);
		album = new Album();
		album.setArtist(artist);
		album.setTitle("For Those About to Rock We Salute You");
		album.setYear(1981);

		song = new Song();
		song.setTitle("For Those About to Rock We Salute You");
		song.setDisc(1);
		song.setTrack(1);
		song.setAlbum(album);

		album.addSong(song);

		song = new Song();
		song.setTitle("I Put the Finger on You");
		song.setDisc(1);
		song.setTrack(2);
		song.setAlbum(album);
		album.addSong(song);

		em.persist(album);

		// Create last
		artist = new Artist("Royal Hunt");
		em.persist(artist);
		album = new Album();
		album.setArtist(artist);
		album.setTitle("Paradox");
		album.setYear(1997);

		song = new Song();
		song.setTitle("The Awakening");
		song.setDisc(1);
		song.setTrack(1);
		song.setAlbum(album);

		album.addSong(song);

		song = new Song();
		song.setTitle("River of Pain");
		song.setDisc(1);
		song.setTrack(2);
		song.setAlbum(album);
		album.addSong(song);

		em.persist(album);
		em.getTransaction().commit();

		royalHuntId = artist.getId();
		paradoxId = album.getId();

		isDataCreated = true;
	}

	private static Integer royalHuntId;
	private static Integer paradoxId;

	public static Integer getRoyalHuntId() {
		return royalHuntId;
	}

	public static Integer getParadoxId() {
		return paradoxId;
	}

}
