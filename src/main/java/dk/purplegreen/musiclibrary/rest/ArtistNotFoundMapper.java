package dk.purplegreen.musiclibrary.rest;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dk.purplegreen.musiclibrary.ArtistNotFoundException;

@Provider
public class ArtistNotFoundMapper implements ExceptionMapper<ArtistNotFoundException> {

	private static final Logger log = LogManager.getLogger(ArtistNotFoundMapper.class);

	@Override
	public Response toResponse(ArtistNotFoundException e) {

		if (log.isInfoEnabled()) {
			log.info("ArtistNotFound: "+e.getMessage());
		}

		return Response.status(Response.Status.NOT_FOUND).build();
	}
}
