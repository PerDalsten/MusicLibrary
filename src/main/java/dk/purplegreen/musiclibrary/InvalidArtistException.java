package dk.purplegreen.musiclibrary;

public class InvalidArtistException extends MusicLibraryException {

	private static final long serialVersionUID = -7726199348868652174L;

	public InvalidArtistException() {
		super();
	}

	public InvalidArtistException(String message) {
		super(message);
	}

}
