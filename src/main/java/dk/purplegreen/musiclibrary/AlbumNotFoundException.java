package dk.purplegreen.musiclibrary;

public class AlbumNotFoundException extends MusicLibraryException {
	
	private static final long serialVersionUID = -6484065311755614153L;

	public AlbumNotFoundException() {
		super();
	}

	public AlbumNotFoundException(String message) {
		super(message);		
	}
}
