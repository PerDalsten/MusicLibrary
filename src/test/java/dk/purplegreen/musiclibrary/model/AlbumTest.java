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

	@Test
	public void testCascade() {

		EntityManager em = TestEntityManagerFactory.getEntityManager();

		Album album = new Album("Testament", "Practice What You Preach", 1989);
		album.addSong(new Song("Practice What You Preach", 1));
		album.addSong(new Song("Perilouz Nation", 2));
		album.addSong(new Song("Bogus Song", 3));
		em.getTransaction().begin();
		em.persist(album);
		em.getTransaction().commit();

		em.clear();

		album = em.find(Album.class, album.getId());

		assertEquals("Wrong artist", "Testament", album.getArtist());
		assertEquals("Wrong number of songs", 3, album.getSongs().size());
		assertEquals("Wrong first song", "Practice What You Preach", album.getSongs().get(0).getTitle());
		assertEquals("Wrong mispelled song", "Perilouz Nation", album.getSongs().get(1).getTitle());
		assertEquals("Wrong bogus song", "Bogus Song", album.getSongs().get(2).getTitle());

		album.getSongs().get(1).setTitle("Perilous Nation");
		album.getSongs().remove(2);
		album.addSong(new Song("Envy Life", 3));
		album.addSong(new Song("Time Is Coming", 4));

		em.getTransaction().begin();
		album = em.merge(album);
		em.getTransaction().commit();

		em.clear();

		album = em.find(Album.class, album.getId());
		assertEquals("Wrong number of songs", 4, album.getSongs().size());
		assertEquals("Wrong first song", "Practice What You Preach", album.getSongs().get(0).getTitle());
		assertEquals("Wrong correctly spelled song", "Perilous Nation", album.getSongs().get(1).getTitle());
		assertEquals("Wrong real song", "Envy Life", album.getSongs().get(2).getTitle());
		assertEquals("Wrong additional song", "Time Is Coming", album.getSongs().get(3).getTitle());

		em.close();
	}

}
