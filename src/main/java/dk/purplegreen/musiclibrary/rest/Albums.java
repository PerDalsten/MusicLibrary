package dk.purplegreen.musiclibrary.rest;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import dk.purplegreen.musiclibrary.MusicLibraryService;

@Path("/albums")
@RequestScoped
public class Albums {

	@Inject
	MusicLibraryService service;

	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAlbum(@PathParam("id") String id) {
		return Response.ok(service.getAlbum(Integer.valueOf(id))).build();
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
}
