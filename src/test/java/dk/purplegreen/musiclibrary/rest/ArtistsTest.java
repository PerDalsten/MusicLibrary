package dk.purplegreen.musiclibrary.rest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.internal.inject.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Before;
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

	private Artist artist;

	@Before
	public void createArtist() throws Exception {
		artist = new Artist(42);
		artist.setName("Iron Maiden");
	}

	@Test
	public void testGetArtist() throws Exception {

		when(service.getArtist(42)).thenReturn(artist);

		Response response = target("/artists/42").request().get();

		assertEquals("Wrong status", 200, response.getStatus());
		assertEquals("Wrong media type", "application/json", response.getMediaType().toString());

		assertEquals("Wrong artist", "Iron Maiden", response.readEntity(Artist.class).getName());
	}

	@Test
	public void testGetArtistNotFound() throws Exception {

		when(service.getArtist(42)).thenThrow(new ArtistNotFoundException());

		Response response = target("/artists/42").request().get();

		assertEquals("Wrong status", 404, response.getStatus());
	}

	@Test
	public void testCreateArtist() throws Exception {

		when(service.createArtist(any(Artist.class))).thenReturn(artist);

		Response response = target("/artists").request().post(Entity.json(artist));

		assertEquals("Wrong status", 201, response.getStatus());

		assertEquals("Wrong artist", "Iron Maiden", response.readEntity(Artist.class).getName());

		assertTrue("Location missing", response.getHeaderString("Location").indexOf("/artists/42") > 0);

	}
}
