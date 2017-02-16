package dk.purplegreen.musiclibrary.rest;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dk.purplegreen.musiclibrary.AlbumNotFoundException;

@Provider
public class AlbumNotFoundMapper implements ExceptionMapper<AlbumNotFoundException> {

	private static final Logger log = LogManager.getLogger(AlbumNotFoundMapper.class);

	@Override
	public Response toResponse(AlbumNotFoundException e) {

		if (log.isInfoEnabled()) {
			log.info("AlbumNotFound: "+e.getMessage());
		}

		return Response.status(Response.Status.NOT_FOUND).build();
	}
}
