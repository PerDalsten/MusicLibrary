package dk.purplegreen.musiclibrary.rest;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.internal.inject.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import dk.purplegreen.musiclibrary.ArtistNotFoundException;
import dk.purplegreen.musiclibrary.MusicLibraryService;
import dk.purplegreen.musiclibrary.model.Artist;

public class ArtistsTest extends JerseyTest {

	@Mock
	private MusicLibraryService service;

	@Override
	public Application configure() {

		MockitoAnnotations.initMocks(this);

		return new ResourceConfig().register(Artists.class).register(MusicLibraryExceptionMapper.class)
				.register(new AbstractBinder() {
					@Override
					protected void configure() {
						bind(service).to(MusicLibraryService.class);
					}
				});
	}

	@Test
	public void testGetArtist() throws Exception {

		when(service.getArtist(42)).thenReturn(new Artist("Iron Maiden"));

		Response response = target("/artists/42").request().get();

		assertEquals(response.getStatus(), 200);

		assertEquals("Wrong artist", "Iron Maiden", response.readEntity(Artist.class).getName());
	}

	@Test
	public void testGetArtistNotFound() throws Exception {

		when(service.getArtist(42)).thenThrow(new ArtistNotFoundException());

		Response response = target("/artists/42").request().get();

		assertEquals(response.getStatus(), 404);
	}
}
