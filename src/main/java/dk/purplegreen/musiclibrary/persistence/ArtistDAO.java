package dk.purplegreen.musiclibrary.persistence;

import java.util.List;

import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import dk.purplegreen.musiclibrary.model.Artist;

@Dependent
public class ArtistDAO {

	@PersistenceContext(unitName = "MusicLibrary")
	private EntityManager em;

	public Artist save(Artist artist) {
		if (artist.getId() == null) {
			em.persist(artist);
		} else {
			artist = em.merge(artist);
		}
		return artist;
	}

	public Artist find(Integer id) {
		return em.find(Artist.class, id);
	}

	public void delete(Artist artist) {
		em.remove(em.merge(artist));
	}

	public List<Artist> getAllArtists() {
		TypedQuery<Artist> query = em.createNamedQuery("findAllArtists", Artist.class);
		return query.getResultList();
	}

}
