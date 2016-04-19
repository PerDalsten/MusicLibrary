package dk.purplegreen.musiclibrary.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;

@Entity
@Table(name = "ARTIST")
@NamedQueries({ @NamedQuery(name = "findAllArtists", query = "SELECT a FROM Artist a ORDER BY a.name"),
		@NamedQuery(name = "findByName", query = "SELECT a FROM Artist a WHERE a.name = :name") })
public class Artist {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column(name = "ARTIST_NAME", nullable = false)
	@Size(min=1)
	private String name;

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
}
