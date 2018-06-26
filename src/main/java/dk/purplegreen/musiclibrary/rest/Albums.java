package dk.purplegreen.musiclibrary.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

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
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import dk.purplegreen.musiclibrary.MusicLibraryException;
import dk.purplegreen.musiclibrary.MusicLibraryService;
import dk.purplegreen.musiclibrary.model.Album;

@Path("/albums")
@RequestScoped
public class Albums {

	@Inject
	MusicLibraryService service;

	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAlbum(@PathParam("id") Integer id) throws MusicLibraryException {
		return Response.ok(new AlbumResource(service.getAlbum(id))).build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAlbums(@QueryParam("artist") String artist, @QueryParam("title") String title,
			@QueryParam("year") Integer year) {

		if (artist == null && title == null && year == null)
			return Response.ok(new GenericEntity<List<AlbumResource>>(
					service.getAlbums().stream().map(AlbumResource::new).collect(Collectors.toList())) {
			}).build();
		else
			return Response.ok(new GenericEntity<List<AlbumResource>>(service.findAlbums(artist, title, year).stream()
					.map(AlbumResource::new).collect(Collectors.toList())) {
			}).build();
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createAlbum(AlbumResource album) throws MusicLibraryException, URISyntaxException {

		Album a = service.createAlbum(album.asAlbum());

		return Response.created(new URI("albums/" + a.getId())).entity(new AlbumResource(a)).build();
	}

	@PUT
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateAlbum(@PathParam("id") Integer id, AlbumResource album) throws MusicLibraryException {

		Album a = album.asAlbum();

		a.setId(id);

		a = service.updateAlbum(a);
		return Response.ok(new AlbumResource(a)).build();
	}

	@DELETE
	@Path("/{id}")
	public Response deleteAlbum(@PathParam("id") Integer id) throws MusicLibraryException {

		service.deleteAlbum(new Album(id));
		return Response.ok().build();
	}
}
