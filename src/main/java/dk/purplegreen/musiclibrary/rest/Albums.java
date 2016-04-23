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
import javax.ws.rs.core.Response.Status;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dk.purplegreen.musiclibrary.AlbumNotFoundException;
import dk.purplegreen.musiclibrary.ArtistNotFoundException;
import dk.purplegreen.musiclibrary.InvalidArtistException;
import dk.purplegreen.musiclibrary.MusicLibraryService;
import dk.purplegreen.musiclibrary.model.Album;

@Path("/albums")
@RequestScoped
public class Albums {

	private final static Logger log = LogManager.getLogger(Albums.class);

	@Inject
	MusicLibraryService service;

	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAlbum(@PathParam("id") Integer id) {

		try {
			return Response.ok(service.getAlbum(id)).build();
		} catch (AlbumNotFoundException e) {
			return Response.status(Status.NOT_FOUND).build();
		}
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
	public Response createAlbum(Album album) {

		album.setId(null);
		try {
			album = service.createAlbum(album);
			return Response.created(new URI("albums/" + album.getId())).entity(album).build();
		} catch (URISyntaxException e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		} catch (ArtistNotFoundException e) {
			log.error("Exception caught in createAlbum", e);
			return Response.status(Status.NOT_FOUND).build();
		} catch (InvalidArtistException e) {
			return Response.status(Status.BAD_REQUEST).build();
		}
	}

	@PUT
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateAlbum(@PathParam("id") Integer id, Album album) {

		album.setId(id);

		try {
			album = service.updateAlbum(album);
			return Response.ok(album).build();
		} catch (AlbumNotFoundException e) {
			return Response.status(Status.NOT_FOUND).build();
		} catch (ArtistNotFoundException e) {
			log.error("Exception caught in createAlbum", e);
			return Response.status(Status.NOT_FOUND).build();
		} catch (InvalidArtistException e) {
			return Response.status(Status.BAD_REQUEST).build();
		}
	}

	@DELETE
	@Path("/{id}")
	public Response deleteAlbum(@PathParam("id") Integer id) {
		try {
			service.deleteAlbum(id);
			return Response.ok().build();
		} catch (AlbumNotFoundException e) {
			return Response.status(Status.NOT_FOUND).build();
		}
	}
}
