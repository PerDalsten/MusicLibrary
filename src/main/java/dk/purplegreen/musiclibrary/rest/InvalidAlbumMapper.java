package dk.purplegreen.musiclibrary.rest;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dk.purplegreen.musiclibrary.InvalidAlbumException;

@Provider
public class InvalidAlbumMapper implements ExceptionMapper<InvalidAlbumException> {

	private static final Logger log = LogManager.getLogger(InvalidAlbumMapper.class);

	@Override
	public Response toResponse(InvalidAlbumException e) {

		log.error("BadRequest", e);

		return Response.status(Response.Status.BAD_REQUEST).build();
	}
}
