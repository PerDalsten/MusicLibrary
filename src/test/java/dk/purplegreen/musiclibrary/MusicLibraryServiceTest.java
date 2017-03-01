package dk.purplegreen.musiclibrary;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import dk.purplegreen.musiclibrary.model.Album;
import dk.purplegreen.musiclibrary.model.Artist;
import dk.purplegreen.musiclibrary.persistence.AlbumDAO;
import dk.purplegreen.musiclibrary.persistence.ArtistDAO;

@RunWith(MockitoJUnitRunner.class)
public class MusicLibraryServiceTest {

	@InjectMocks
	private MusicLibraryService service;

	@Mock
	private AlbumDAO albumDAO;

	@Mock
	private ArtistDAO artistDAO;

	@Mock
	private AlbumValidator albumValidator;

	@Test
	public void testCreate() throws Exception {

		Album album = new Album(42);
		album.setArtist(new Artist("Black Sabbath"));
		album.setTitle("Heaven and Hell");
		album.setYear(1980);

		when(artistDAO.find(any())).thenReturn(album.getArtist());
		when(albumDAO.save(any(Album.class))).then(returnsFirstArg());

		album = service.createAlbum(album);

		assertEquals("Wrong title", "Heaven and Hell", album.getTitle());
		assertNull("Album id not null", album.getId());
	}
}
