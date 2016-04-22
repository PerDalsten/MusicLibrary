package dk.purplegreen.musiclibrary;

public class ArtistNotFoundException extends Exception {
	
	private static final long serialVersionUID = -1241055717870486353L;

	public ArtistNotFoundException() {
		super();
	}

	public ArtistNotFoundException(String message) {
		super(message);		
	}
}
