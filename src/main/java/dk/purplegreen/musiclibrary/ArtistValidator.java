package dk.purplegreen.musiclibrary;

import javax.enterprise.inject.Model;

import dk.purplegreen.musiclibrary.model.Artist;

@Model
public class ArtistValidator {

	public void validate(Artist artist) throws InvalidArtistException {
		if (artist == null) {
			throw new InvalidArtistException("Artist is null");
		}

		if (artist.getName() == null || artist.getName().trim().length() == 0) {
			throw new InvalidArtistException("Artist name cannot be empty");
		}
	}
}
