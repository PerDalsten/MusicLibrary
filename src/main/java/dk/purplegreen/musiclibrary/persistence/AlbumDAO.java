package dk.purplegreen.musiclibrary.persistence;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import dk.purplegreen.musiclibrary.model.Album;
import dk.purplegreen.musiclibrary.model.Artist;
import dk.purplegreen.musiclibrary.model.Song;

@Dependent
public class AlbumDAO {

	@PersistenceContext(unitName = "MusicLibrary")
	private EntityManager em;

	public Album save(Album album) {
		if (album.getId() == null) {
			em.persist(album);
		} else {
			album = em.merge(album);
		}
		return album;
	}

	public Album find(Integer id) {
		return em.find(Album.class, id);
	}

	public void delete(Album album) {
		// This should not be necessary due to the Cascade.ALL on Album, but a bug in
		// OpenJPA will issue the delete on Album before the Songs causing ALBUM_FK
		// violation.
		for (Song song : album.getSongs()) {
			em.remove(song);
		}
		em.remove(album);
	}

	public List<Album> getAllAlbums() {
		TypedQuery<Album> query = em.createNamedQuery("findAllAlbums", Album.class);
		return query.getResultList();
	}

	public List<Album> find(String artist, String title, Integer year) {

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Album> cq = cb.createQuery(Album.class);
		Root<Album> album = cq.from(Album.class);

		List<Predicate> predicates = new ArrayList<>();

		if (artist != null && !artist.isEmpty()) {
			predicates.add(cb.like(cb.lower(album.get("artist").get("name")), "%" + artist.toLowerCase() + "%"));
		}
		if (title != null && !title.isEmpty()) {
			predicates.add(cb.like(cb.lower(album.get("title")), "%" + title.toLowerCase() + "%"));
		}
		if (year != null) {
			predicates.add(cb.equal(album.get("year"), year));
		}

		cq.select(album).where(predicates.toArray(new Predicate[predicates.size()]));
		cq.orderBy(cb.asc(album.get("title")));

		return em.createQuery(cq).getResultList();
	}

	public List<Album> findByArtist(Artist artist) {
		TypedQuery<Album> query = em.createNamedQuery("findByArtist", Album.class);
		query.setParameter("artist", artist);
		return query.getResultList();
	}

	public Integer getArtistAlbumCount(Artist artist) {
		return ((Number) em.createQuery("SELECT COUNT(a) FROM Album a WHERE a.artist = :artist")
				.setParameter("artist", artist).getSingleResult()).intValue();
	}
}
