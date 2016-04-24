package dk.purplegreen.musiclibrary;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import dk.purplegreen.musiclibrary.model.Artist;

public class ArtistValidatorTest {

	@Rule
	public final ExpectedException exception = ExpectedException.none();

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testValidate() throws Exception {
		Artist artist = new Artist();

		exception.expect(InvalidArtistException.class);
		new ArtistValidator().validate(artist);
	}	
}
