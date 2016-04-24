package dk.purplegreen.musiclibrary;

import javax.enterprise.inject.Model;

import dk.purplegreen.musiclibrary.model.Album;

@Model
public class AlbumValidator {

	public void validate(Album album) throws InvalidAlbumException {
		if (album == null) {
			throw new InvalidAlbumException("Album is null");
		}

		if (album.getTitle() == null || album.getTitle().trim().length() == 0) {
			throw new InvalidAlbumException("Album title cannot be empty");
		}

		if (album.getSongs() == null || album.getSongs().size() == 0) {
			throw new InvalidAlbumException("Album does not have any songs");
		}
	}
}
