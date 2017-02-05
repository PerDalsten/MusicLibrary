package dk.purplegreen.musiclibrary.test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.rules.ExternalResource;

public class Database extends ExternalResource {

	static {
		System.setProperty("derby.stream.error.file", "logs/derby.log");
	}

	private EntityManagerFactory factory = Persistence.createEntityManagerFactory("MusicLibraryTest");

	private EntityManager em;

	public EntityManager getEntityManager() {
		return em;
	}

	@Override
	protected void before() throws Throwable {
		em = factory.createEntityManager();
	}

	@Override
	protected void after() {
		em.close();
		factory.close();
	}

}
