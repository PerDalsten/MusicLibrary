package dk.purplegreen.musiclibrary.ui;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.RequestDispatcher;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dk.purplegreen.musiclibrary.AlbumNotFoundException;
import dk.purplegreen.musiclibrary.ArtistNotFoundException;
import dk.purplegreen.musiclibrary.InvalidAlbumException;
import dk.purplegreen.musiclibrary.InvalidArtistException;

@ManagedBean
@RequestScoped
public class ErrorHandler {

	private static final Logger log = LogManager.getLogger(ErrorHandler.class);

	public String getError() {

		Throwable t = (Throwable) FacesContext.getCurrentInstance().getExternalContext().getRequestMap()
				.get(RequestDispatcher.ERROR_EXCEPTION);

		log.error("Unhandled exception", t);

		String errorMessage;

		t = getRootCause(t);

		if (t instanceof AlbumNotFoundException || t instanceof ArtistNotFoundException
				|| t instanceof InvalidAlbumException || t instanceof InvalidArtistException
		) {
			errorMessage = t.getMessage();
		} else {
			errorMessage = "System error. See error log for details";
		}

		return errorMessage;
	}

	private Throwable getRootCause(Throwable e) {
		Throwable cause = null;
		Throwable result = e;

		while (null != (cause = result.getCause()) && (result != cause)) {
			result = cause;
		}
		return result;
	}
}
