package dk.purplegreen.musiclibrary;

public class InvalidArtistException extends Exception {

	private static final long serialVersionUID = -7726199348868652174L;

	public InvalidArtistException() {
		super();
	}

	public InvalidArtistException(String message) {
		super(message);
	}

}
