package dk.purplegreen.musiclibrary;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import dk.purplegreen.musiclibrary.model.Album;

public class AlbumValidatorTest {

	@Rule
	public final ExpectedException exception = ExpectedException.none();

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testValidate() throws Exception {
		Album album = new Album();

		exception.expect(InvalidAlbumException.class);
		new AlbumValidator().validate(album);
	}
}
