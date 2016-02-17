package dk.purplegreen.musiclibrary.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.junit.Test;

import dk.purplegreen.musiclibrary.persistence.TestEntityManagerFactory;

public class AlbumTest {

	@Test
	public void testCreateAlbum() {

		EntityManager em = TestEntityManagerFactory.getEntityManager();
		em.getTransaction().begin();

		Album album = new Album();
		album.setArtist("Thin Lizzy");
		album.setTitle("Chinatown");
		album.setYear(1990);

		Song song = new Song();
		song.setTitle("We Will Be Strong");
		song.setDisc(1);
		song.setTrack(1);
		song.setAlbum(album);

		album.addSong(song);

		song = new Song();
		song.setTitle("Chinatown");
		song.setDisc(1);
		song.setTrack(2);
		song.setAlbum(album);
		album.addSong(song);

		em.persist(album);

		em.getTransaction().commit();

		album = em.find(Album.class, album.getId());
		em.close();

		assertEquals("Wrong artist", "Thin Lizzy", album.getArtist());
		assertEquals("Wrong number of songs", 2, album.getSongs().size());
	}

	@Test
	public void testFindAllAlbums() {
		EntityManager em = TestEntityManagerFactory.getEntityManager();
		TypedQuery<Album> query = em.createNamedQuery("findAll", Album.class);
		List<Album> result = query.getResultList();
		em.close();
		assertTrue("To few albums", result.size() >= 2);
	}

	@Test
	public void testFindByArtist() {
		EntityManager em = TestEntityManagerFactory.getEntityManager();
		TypedQuery<Album> query = em.createNamedQuery("findByArtist", Album.class);
		query.setParameter("artist", "Royal Hunt");

		List<Album> result = query.getResultList();
		em.close();

		assertEquals("Wrong number of albums", result.size(), 1);
		assertEquals("Wrong artist", "Royal Hunt", result.get(0).getArtist());
		assertEquals("Wrong number of songs", result.get(0).getSongs().size(), 2);
	}

	@Test
	public void testFindByTitle() {
		EntityManager em = TestEntityManagerFactory.getEntityManager();
		TypedQuery<Album> query = em.createNamedQuery("findByTitle", Album.class);
		query.setParameter("title", "Paradox");

		List<Album> result = query.getResultList();
		em.close();

		assertEquals("Wrong number of albums", result.size(), 1);
		assertEquals("Wrong artist", "Royal Hunt", result.get(0).getArtist());
		assertEquals("Wrong number of songs", result.get(0).getSongs().size(), 2);
	}
}
