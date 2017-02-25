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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dk.purplegreen.musiclibrary.MusicLibraryException;
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
	public Response getArtist(@PathParam("id") Integer id) throws MusicLibraryException {

		return Response.ok(service.getArtist(id)).build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getArtists() {
		return Response.ok(service.getArtists()).build();
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createArtist(Artist artist) throws MusicLibraryException, URISyntaxException {

		artist = service.createArtist(artist);
		return Response.created(new URI("artists/" + artist.getId())).entity(artist).build();
	}

	@PUT
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateArtist(@PathParam("id") Integer id, Artist artist) throws MusicLibraryException {
		artist.setId(id);

		artist = service.updateArtist(artist);
		return Response.ok(artist).build();
	}

	@DELETE
	@Path("/{id}")
	public Response deleteArtist(@PathParam("id") Integer id) throws MusicLibraryException {
		service.deleteArtist(service.getArtist(id));
		return Response.ok().build();
	}

	@GET
	@Path("/{id}/albums")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getArtistAlbums(@PathParam("id") Integer id) throws MusicLibraryException {

		return Response.ok(service.getAlbums(new Artist(id))).build();
	}
}
