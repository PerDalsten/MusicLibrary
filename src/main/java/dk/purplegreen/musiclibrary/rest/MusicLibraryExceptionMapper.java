package dk.purplegreen.musiclibrary.rest;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dk.purplegreen.musiclibrary.AlbumNotFoundException;
import dk.purplegreen.musiclibrary.ArtistNotFoundException;
import dk.purplegreen.musiclibrary.InvalidAlbumException;
import dk.purplegreen.musiclibrary.InvalidArtistException;
import dk.purplegreen.musiclibrary.MusicLibraryException;

@Provider
public class MusicLibraryExceptionMapper implements ExceptionMapper<MusicLibraryException> {

	private static final Logger log = LogManager.getLogger(MusicLibraryExceptionMapper.class);

	@Override
	public Response toResponse(MusicLibraryException e) {

		Response result;

		log.info("MusicLibraryException: {}", e.getMessage());

		if (AlbumNotFoundException.class.isInstance(e) || ArtistNotFoundException.class.isInstance(e)) {
			result = Response.status(Response.Status.NOT_FOUND).build();
		} else if (InvalidAlbumException.class.isInstance(e) || InvalidArtistException.class.isInstance(e)) {
			result = Response.status(Response.Status.BAD_REQUEST).build();
		} else {

			log.error("Unexpected MusicLibraryException", e);
			result = Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}

		return result;
	}
}
