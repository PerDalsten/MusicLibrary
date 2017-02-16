package dk.purplegreen.musiclibrary.rest;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Provider
public class DefaultMapper implements ExceptionMapper<Exception> {

	private static final Logger log = LogManager.getLogger(DefaultMapper.class);

	@Override
	public Response toResponse(Exception e) {
		log.error(e);
		return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
	}
}
