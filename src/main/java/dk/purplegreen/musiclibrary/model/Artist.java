package dk.purplegreen.musiclibrary.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Entity
@Table(name = "ARTIST")
@NamedQueries({ @NamedQuery(name = "findAllArtists", query = "SELECT a FROM Artist a ORDER BY a.name"),
		@NamedQuery(name = "findByName", query = "SELECT a FROM Artist a WHERE a.name = :name") })
public class Artist implements Serializable {
	
	private static final long serialVersionUID = 6206086785411646677L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column(name = "ARTIST_NAME", nullable = false)
	private String name;

	private final static Logger log = LogManager.getLogger(Artist.class);

	public Artist() {
	}

	public Artist(String name) {
		this.name = name;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public int hashCode() {

		if (id == null) {
			log.warn("hashCode() called for non-persisted object");
			return super.hashCode();
		} else {
			return id.hashCode();
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (id == null) {
			log.warn("equals() called for non-persisted object");
			return super.equals(obj);
		}

		if (obj == null)
			return false;

		if (getClass().equals(obj.getClass())) {
			return id.equals(((Artist) obj).getId());
		}

		return false;
	}
}
