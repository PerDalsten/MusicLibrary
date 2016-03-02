package dk.purplegreen.musiclibrary.persistence;

import java.util.ArrayList;
import java.util.List;

//import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import dk.purplegreen.musiclibrary.model.Album;

@Dependent
public class AlbumDAO {

	@PersistenceContext(unitName = "MusicLibrary")
	private EntityManager em;

	public void save(Album album) {
		em.persist(album);
	}

	public Album find(Integer id) {
		return em.find(Album.class, id);
	}

	public List<Album> getAllAlbums() {
		TypedQuery<Album> query = em.createNamedQuery("findAll", Album.class);
		return query.getResultList();
	}

	public List<Album> find(String artist, String title, Integer year) {

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Album> cq = cb.createQuery(Album.class);
		Root<Album> album = cq.from(Album.class);

		List<Predicate> predicates = new ArrayList<Predicate>();

		if (artist != null && !artist.isEmpty()) {
			predicates.add(cb.like(cb.lower(album.get("artist")), "%" + artist.toLowerCase() + "%"));
		}
		if (title != null && !title.isEmpty()) {
			predicates.add(cb.like(cb.lower(album.get("title")), "%" + title.toLowerCase() + "%"));
		}
		if (year != null) {
			predicates.add(cb.equal(album.get("year"), year));
		}

		cq.select(album).where(predicates.toArray(new Predicate[predicates.size()]));

		return em.createQuery(cq).getResultList();
	}
}