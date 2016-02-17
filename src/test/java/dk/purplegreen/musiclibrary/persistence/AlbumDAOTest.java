package dk.purplegreen.musiclibrary.persistence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import dk.purplegreen.musiclibrary.model.Album;

@RunWith(MockitoJUnitRunner.class)
public class AlbumDAOTest {

	@Mock
	private EntityManager em;

	@InjectMocks
	private AlbumDAO albumDAO = new AlbumDAO();

	@Test
	public void testFindById() {

		EntityManager testEM = TestEntityManagerFactory.getEntityManager();

		ArgumentCaptor<Integer> argument = ArgumentCaptor.forClass(Integer.class);
		when(em.find(any(), argument.capture())).then(new Answer<Album>() {
			@Override
			public Album answer(InvocationOnMock invocation) {
				return testEM.find(Album.class, argument.getValue());
			}
		});

		Album album = albumDAO.find(TestEntityManagerFactory.getRoyalHuntId());

		testEM.close();

		assertNotNull("Album is null", album);
		assertEquals("Wrong artist", "Royal Hunt", album.getArtist());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testFindByArtist() {

		EntityManager testEM = TestEntityManagerFactory.getEntityManager();

		when(em.getCriteriaBuilder()).thenReturn(testEM.getCriteriaBuilder());
		@SuppressWarnings("rawtypes")
		ArgumentCaptor<CriteriaQuery> argument = ArgumentCaptor.forClass(CriteriaQuery.class);
		when(em.createQuery(argument.capture())).then(new Answer<TypedQuery<Album>>() {
			public TypedQuery<Album> answer(InvocationOnMock invocation) {
				return testEM.createQuery(argument.getValue());
			}
		});

		List<Album> albums = albumDAO.find("beatles", null, null);

		testEM.close();

		assertEquals("Wrong number of results", 1, albums.size());
		assertEquals("Wrong artist", "The Beatles", albums.get(0).getArtist());
	}
}
