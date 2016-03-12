package dk.purplegreen.musiclibrary;

public class AlbumNotFoundException extends Exception {
	
	private static final long serialVersionUID = -6484065311755614153L;

	public AlbumNotFoundException() {
		super();
	}

	public AlbumNotFoundException(String message) {
		super(message);		
	}
}
