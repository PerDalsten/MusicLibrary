package dk.purplegreen.musiclibrary;

public class InvalidAlbumException extends MusicLibraryException {

	private static final long serialVersionUID = 8594820532607930403L;

	public InvalidAlbumException() {
		super();
	}

	public InvalidAlbumException(String message) {
		super(message);
	}

}
