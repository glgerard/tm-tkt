package it.unipv.se2.tmtkt.modeltest;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;

import org.jboss.arquillian.junit.Arquillian;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;

import it.unipv.se2.tmtkt.model.Event;
import it.unipv.se2.tmtkt.model.Genre;
import it.unipv.se2.tmtkt.model.Play;

@RunWith(Arquillian.class)
public class GenericModelTest {

    private static final String[] GENRE_NAMES = {
        "Tragedy",
        "Music",
        "Comedy",
        "Opera",
        "Ballet",
        "Drama",
        "Musical"
    };
    
    private static final String[] SHOW_NAMES = {
    	"Mamma Mia",
    	"Othello",
    	"Traviata"
    };
    
    private static final short[] SHOW_GENRES = {
    	7,
    	1,
    	4
    };
    
    private static final String[] SHOW_DATES = {
    	"Jan 1, 2014",
    	"Jan 8, 2014",
    	"Mar 7, 2014"
    };
    
    private static final short[] EVENT_SHOWS = {
    	1, 1, 1, 2, 2, 3
    };
    
    private static final String[] EVENT_DATES = {
    	"Jan 1, 2014 8:30 PM",
    	"Jan 10, 2014 8:30 PM",
    	"Feb 3, 2014 9:00 PM",
    	"Jan 8, 2014 9:00 PM",
    	"Feb 15, 2014 8:45 PM",
    	"Mar 7, 2014 9:15 PM"
    };

    @PersistenceContext  
    EntityManager em;  
    
    @Resource
    UserTransaction utx;
        
    @Before
    public void preparePersistenceTest() throws Exception {
        clearData();
        insertData();
        startTransaction();
    }

    private void clearData() throws Exception {
        utx.begin();
        em.joinTransaction();
        System.out.println("Dumping old records...");
        em.createQuery("delete from Event").executeUpdate();
        em.createQuery("delete from Show").executeUpdate();
        em.createQuery("delete from Genre").executeUpdate();
        utx.commit();
    }

	private void insertData() throws Exception {
		utx.begin();
		em.joinTransaction();
		System.out.println("Inserting records...");
		System.out.println("Populating genres...");
		for (String name : GENRE_NAMES) {
			Genre genre = new Genre(name);
			em.persist(genre);
		}
		System.out.println("Populating shows...");
		DateFormat df = new SimpleDateFormat("MMM d, yyyy",
				Locale.ENGLISH);
		for (int i = 0; i < SHOW_NAMES.length; i++) {
			Genre genre = this.em.find(Genre.class, SHOW_GENRES[i]);
			Play show = new Play(genre, SHOW_NAMES[i]);
			show.setFirstEventDate(df.parse(SHOW_DATES[i]));
			em.persist(show);
		}
		System.out.println("Populating events...");
	    df = new SimpleDateFormat("MMM d, yyyy h:mm a",
				Locale.ENGLISH);
		for (int i = 0; i < EVENT_DATES.length; i++) {
			Play show = this.em.find(Play.class, EVENT_SHOWS[i]);
			Event event = new Event(show, df.parse(EVENT_DATES[i]));
			em.persist(event);
		}
		utx.commit();
		// clear the persistence context (first-level cache)
		em.clear();
	}

    protected void startTransaction() throws Exception {
        utx.begin();
        em.joinTransaction();
    }
    
    @After
    public void commitTransaction() throws Exception {
        utx.commit();
    }
}
