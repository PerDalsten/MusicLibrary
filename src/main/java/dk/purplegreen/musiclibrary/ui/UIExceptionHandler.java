package dk.purplegreen.musiclibrary.ui;

import java.util.Iterator;
import java.util.Map;

import javax.faces.application.NavigationHandler;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerWrapper;
import javax.faces.context.FacesContext;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.ExceptionQueuedEventContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dk.purplegreen.musiclibrary.MusicLibraryException;

public class UIExceptionHandler extends ExceptionHandlerWrapper {

	private static final Logger log = LogManager.getLogger(UIExceptionHandler.class);

	private ExceptionHandler wrapped;

	public UIExceptionHandler(ExceptionHandler parent) {
		this.wrapped = parent;
	}

	@Override
	public ExceptionHandler getWrapped() {
		return wrapped;
	}

	@Override
	public void handle() {

		final Iterator<ExceptionQueuedEvent> queue = getUnhandledExceptionQueuedEvents().iterator();

		while (queue.hasNext()) {
			ExceptionQueuedEvent event = queue.next();
			ExceptionQueuedEventContext eventContext = (ExceptionQueuedEventContext) event.getSource();

			try {
				String errorMessage;

				Throwable t = getRootCause(eventContext.getException());
				if (t.getCause() != null)
					t = t.getCause();

				if (t instanceof MusicLibraryException) {
					errorMessage = t.getMessage();
					if (log.isInfoEnabled())
						log.info("Exception caught", t);

				} else {
					errorMessage = "System error. See error log for details";
					log.error("Caught throwable", t);
				}

				FacesContext context = FacesContext.getCurrentInstance();
				Map<String, Object> requestMap = context.getExternalContext().getRequestMap();
				NavigationHandler nav = context.getApplication().getNavigationHandler();

				requestMap.put("error-message", errorMessage);
				nav.handleNavigation(context, null, "/error");
				context.renderResponse();
			} catch (RuntimeException e) {
				log.error("Exception caught while handling error ", e);
			} finally {
				queue.remove();
			}
		}

		getWrapped().handle();
	}
}
