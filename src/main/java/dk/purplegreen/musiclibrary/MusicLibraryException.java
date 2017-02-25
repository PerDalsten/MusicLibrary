package dk.purplegreen.musiclibrary;

public abstract class MusicLibraryException extends Exception {

	private static final long serialVersionUID = 3294153914223209196L;

	protected MusicLibraryException() {
	}

	protected MusicLibraryException(String message) {
		super(message);
	}
}
