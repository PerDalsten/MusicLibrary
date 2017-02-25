package dk.purplegreen.musiclibrary.ui;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dk.purplegreen.musiclibrary.MusicLibraryException;
import dk.purplegreen.musiclibrary.MusicLibraryService;
import dk.purplegreen.musiclibrary.model.Artist;

//@FacesConverter(forClass = Artist.class, value = "artistConverter") //Injection will not work so use managed bean instead
@ManagedBean
@ApplicationScoped
public class ArtistConverter implements Converter {

	private static Logger log = LogManager.getLogger(Converter.class);

	@Inject
	private MusicLibraryService musicLibraryService;

	@Override
	public Object getAsObject(FacesContext fc, UIComponent component, String id) {
		try {
			return musicLibraryService.getArtist(Integer.parseInt(id));
		} catch (NumberFormatException | MusicLibraryException e) {
			log.error("Exception caught in Artist converter", e);
			return null;
		}
	}

	@Override
	public String getAsString(FacesContext fc, UIComponent component, Object artist) {

		if (artist != null && artist.getClass().equals(Artist.class)) {
			Integer id = ((Artist) artist).getId();
			return id != null && id > 0 ? id.toString() : null;
		} else {
			return null;
		}
	}
}
