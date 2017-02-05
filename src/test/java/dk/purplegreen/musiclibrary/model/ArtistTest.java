package dk.purplegreen.musiclibrary.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import javax.persistence.EntityManager;

import org.junit.Rule;
import org.junit.Test;

import dk.purplegreen.musiclibrary.test.Database;

public class ArtistTest {

	@Rule
	public Database database = new Database();

	@Test
	public void testCRUD() {

		EntityManager em = database.getEntityManager();
		em.getTransaction().begin();

		Artist artist = new Artist("Black Sabbath");

		em.persist(artist);

		em.getTransaction().commit();

		assertNotNull("Artist id is null", artist.getId());

		artist = em.find(Artist.class, artist.getId());

		assertNotNull("Artist is null", artist);
		assertEquals("Wrong Artist name", "Black Sabbath", artist.getName());

		artist.setName("Deep Purple");

		em.getTransaction().begin();
		em.persist(artist);

		em.getTransaction().commit();

		artist = em.find(Artist.class, artist.getId());

		assertNotNull("Artist is null", artist);
		assertEquals("Wrong Artist name", "Deep Purple", artist.getName());

		em.getTransaction().begin();
		em.remove(artist);

		em.getTransaction().commit();

		artist = em.find(Artist.class, artist.getId());
		assertNull("Artist is not null", artist);
	}
}
