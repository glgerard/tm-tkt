package it.unipv.se2.tmtkt.controller;

import it.unipv.se2.tmtkt.model.Event;
import it.unipv.se2.tmtkt.model.Genre;
import it.unipv.se2.tmtkt.model.Play;
import it.unipv.se2.tmtkt.model.PriceScheme;
import it.unipv.se2.tmtkt.model.Season;
import it.unipv.se2.tmtkt.model.Seat;
import it.unipv.se2.tmtkt.model.Sector;
import it.unipv.se2.tmtkt.model.User;
import it.unipv.se2.tmtkt.model.UserCategory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.faces.bean.SessionScoped;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@Named
@Singleton
@SessionScoped
public class PriceSchemeController implements Serializable {
	@PersistenceContext(type = PersistenceContextType.EXTENDED)
	private EntityManager em;

	private PriceScheme example = new PriceScheme();

	private List<String> constraintsList = new ArrayList<String>();
	
	public PriceScheme ticketPrice(Event event, Seat seat, User user) {
		example.setSaleType('T');
		example.setEvent(event);
		example.setPlay(event.getPlay());
		example.setGenre(event.getPlay().getGenre());
		example.setSector(seat.getSector());
		example.setUserCategory(user.getUserCategory());
	
		String[] constraints = {
				"saleType",
				"event",
				"play",
				"genre",
				"sector",
				"userCategory"
		};
		
		constraintsList = Arrays.asList(constraints);
		
		return findPriceScheme();
	}
	
	public PriceScheme subscriptionPrice(Genre genre, Sector sector, User user) {
		example.setSaleType('S');
		example.setGenre(genre);
		example.setSector(sector);
		example.setUserCategory(user.getUserCategory());
		
		String[] constraints = {
				"saleType",
				"genre",
				"sector",
				"userCategory"
		};
		
		constraintsList = Arrays.asList(constraints);
		
		return findPriceScheme();
	}


	private PriceScheme findPriceScheme() {
		Map<String, Predicate> constraintsMap;
		
		CriteriaBuilder builder = this.em.getCriteriaBuilder();

		// Populate this.count

		CriteriaQuery<Long> countCriteria = builder.createQuery(Long.class);
		Root<PriceScheme> root = countCriteria.from(PriceScheme.class);

		constraintsMap = getSearchPredicates(root);
		
		List<Predicate> predicatesList = new ArrayList<Predicate>();
		
		Long resultsCount;
		for (Iterator<String> constraintsIter = constraintsList.iterator();
				constraintsIter.hasNext(); )
		{			
			Predicate predicate = constraintsMap.get(constraintsIter.next());
			if (predicate != null) {
				countCriteria = countCriteria.select(builder.count(root))
						.where(predicate);
				resultsCount = this.em.createQuery(countCriteria)
						.getSingleResult();
				if (resultsCount >= 1) {
					predicatesList.add(predicate);
				}
			}
		}
		
		CriteriaQuery<PriceScheme> criteria = builder
				.createQuery(PriceScheme.class);
		root = criteria.from(PriceScheme.class);
		TypedQuery<PriceScheme> query = this.em.createQuery(criteria.select(
				root).where(predicatesList.toArray(new Predicate[predicatesList.size()])));

		return query.getSingleResult();
	}
	
	private PriceScheme defaultPriceScheme() {
		// TODO Auto-generated method stub
		return null;
	}

	private Map<String, Predicate> getSearchPredicates(Root<PriceScheme> root) {

		CriteriaBuilder builder = this.em.getCriteriaBuilder();
		Map<String, Predicate> predicatesMap = new HashMap<String, Predicate>();

		Character saleType = this.example.getSaleType();
		if (saleType != null ) {
			predicatesMap.put("saleType", builder.equal(root.get("saleType"), saleType));
		}
		
		UserCategory userCategory = this.example.getUserCategory();
		if (userCategory != null) {
			predicatesMap.put("userCategory",builder.equal(root.get("userCategory"),
					userCategory));
		}
		Play play = this.example.getPlay();
		if (play != null) {
			predicatesMap.put("play",builder.equal(root.get("play"), play));
		}
		Season season = this.example.getSeason();
		if (season != null) {
			predicatesMap.put("season",builder.equal(root.get("season"), season));
		}
		Genre genre = this.example.getGenre();
		if (genre != null) {
			predicatesMap.put("genre",builder.equal(root.get("genre"), genre));
		}
		Sector sector = this.example.getSector();
		if (sector != null) {
			predicatesMap.put("sector",builder.equal(root.get("sector"), sector));
		}
		
		Event event = this.example.getEvent();
		if (event != null ) {
			predicatesMap.put("event",builder.equal(root.get("event"), event));	
		}

		return predicatesMap;
	}
}
