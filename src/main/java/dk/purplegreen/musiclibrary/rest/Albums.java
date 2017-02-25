package dk.purplegreen.musiclibrary.rest;

import java.net.URI;
import java.net.URISyntaxException;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dk.purplegreen.musiclibrary.MusicLibraryException;
import dk.purplegreen.musiclibrary.MusicLibraryService;
import dk.purplegreen.musiclibrary.model.Album;

@Path("/albums")
@RequestScoped
public class Albums {

	private static final Logger log = LogManager.getLogger(Albums.class);

	@Inject
	MusicLibraryService service;

	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAlbum(@PathParam("id") Integer id) throws MusicLibraryException {

		return Response.ok(service.getAlbum(id)).build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAlbums(@QueryParam("artist") String artist, @QueryParam("title") String title,
			@QueryParam("year") Integer year) {

		if (artist == null && title == null && year == null)
			return Response.ok(service.getAlbums()).build();
		else
			return Response.ok(service.findAlbums(artist, title, year)).build();
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createAlbum(Album album) throws MusicLibraryException, URISyntaxException {

		album = service.createAlbum(album);
		return Response.created(new URI("albums/" + album.getId())).entity(album).build();
	}

	@PUT
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateAlbum(@PathParam("id") Integer id, Album album) throws MusicLibraryException {

		album.setId(id);

		album = service.updateAlbum(album);
		return Response.ok(album).build();
	}

	@DELETE
	@Path("/{id}")
	public Response deleteAlbum(@PathParam("id") Integer id) throws MusicLibraryException {

		service.deleteAlbum(new Album(id));
		return Response.ok().build();
	}
}
