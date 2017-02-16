package dk.purplegreen.musiclibrary.rest;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dk.purplegreen.musiclibrary.InvalidArtistException;

@Provider
public class InvalidArtistMapper implements ExceptionMapper<InvalidArtistException> {

	private static final Logger log = LogManager.getLogger(InvalidArtistException.class);

	@Override
	public Response toResponse(InvalidArtistException e) {

		log.error("BadRequest", e);

		return Response.status(Response.Status.BAD_REQUEST).build();
	}
}
