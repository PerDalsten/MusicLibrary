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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dk.purplegreen.musiclibrary.ArtistNotFoundException;
import dk.purplegreen.musiclibrary.InvalidArtistException;
import dk.purplegreen.musiclibrary.MusicLibraryService;
import dk.purplegreen.musiclibrary.model.Artist;

@Path("/artists")
@RequestScoped
public class Artists {

	private static final Logger log = LogManager.getLogger(Artists.class);

	@Inject
	MusicLibraryService service;

	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getArtist(@PathParam("id") Integer id) {
		try {
			return Response.ok(service.getArtist(id)).build();
		} catch (ArtistNotFoundException e) {
			log.error("Exception in getArtist", e);
			return Response.status(Status.NOT_FOUND).build();
		}
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getArtists() {
		return Response.ok(service.getArtists()).build();
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createArtist(Artist artist) {
		artist.setId(null);
		try {
			artist = service.createArtist(artist);
			return Response.created(new URI("artist/" + artist.getId())).entity(artist).build();
		} catch (URISyntaxException e) {
			log.error("Exception in createArtist", e);
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		} catch (InvalidArtistException e) {
			log.error("Exception in createArtist", e);
			return Response.status(Status.BAD_REQUEST).build();
		}
	}

	@PUT
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateArtist(@PathParam("id") Integer id, Artist artist) {
		artist.setId(id);
		try {
			artist = service.updateArtist(artist);
			return Response.ok(artist).build();
		} catch (ArtistNotFoundException e) {
			log.error("Exception in updateArtist", e);
			return Response.status(Status.NOT_FOUND).build();
		} catch (InvalidArtistException e) {
			log.error("Exception in updateArtist", e);
			return Response.status(Status.BAD_REQUEST).build();
		}
	}

	@DELETE
	@Path("/{id}")
	public Response deleteArtist(@PathParam("id") Integer id) {
		try {
			service.deleteArtist(id);
			return Response.ok().build();
		} catch (ArtistNotFoundException e) {
			log.error("Exception in deleteArtist", e);
			return Response.status(Status.NOT_FOUND).build();
		}
	}

	@GET
	@Path("/{id}/albums")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getArtistAlbums(@PathParam("id") Integer id) {

		try {
			return Response.ok(service.getAlbums(service.getArtist(id))).build();
		} catch (ArtistNotFoundException e) {
			log.error("Exception in getArtistAlbums", e);
			return Response.status(Status.NOT_FOUND).build();
		}
	}
}
