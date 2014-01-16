package it.unipv.se2.tmtkt.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.Stateful;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import it.unipv.se2.tmtkt.model.Booking;
import it.unipv.se2.tmtkt.model.Genre;
import it.unipv.se2.tmtkt.model.Row;
import it.unipv.se2.tmtkt.model.Seat;
import it.unipv.se2.tmtkt.model.SeatCategory;
import it.unipv.se2.tmtkt.model.Sector;

@Named
@Stateful
@RequestScoped
public class SeatController implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer eventId;

	@PersistenceContext(type = PersistenceContextType.EXTENDED)
	private EntityManager em;

	private int page;
	private long count;
	private List<Seat> pageItems;

	private Seat example = new Seat();

	public void paginate() {

		CriteriaBuilder builder = this.em.getCriteriaBuilder();

		// Populate this.count

		CriteriaQuery<Long> countCriteria = builder.createQuery(Long.class);
		Root<Seat> root = countCriteria.from(Seat.class);
		Subquery<Long> subquery = countCriteria.subquery(Long.class);

		countCriteria = countCriteria.select(builder.count(root)).where(
				getSearchPredicates(root, subquery));
		Logger logger = Logger.getLogger(SeatController.class.getName());
		try {
			TypedQuery<Long> query = this.em.createQuery(countCriteria);
			// logger.log(Level.INFO, query
			// .unwrap(org.hibernate.Query.class).getQueryString());
			this.count = query.getSingleResult();
		} catch (Exception ex) {
			logger.log(Level.SEVERE, null, ex);
		}

		// Populate this.pageItems

		CriteriaQuery<Seat> criteria = builder.createQuery(Seat.class);
		root = criteria.from(Seat.class);
		subquery = criteria.subquery(Long.class);

		try {
			TypedQuery<Seat> query = this.em.createQuery(criteria.select(root)
					.where(getSearchPredicates(root, subquery)));
			// logger.log(Level.INFO, query
			// .unwrap(org.hibernate.Query.class).getQueryString());
			query.setFirstResult(this.page * getPageSize()).setMaxResults(
					getPageSize());
			this.pageItems = query.getResultList();
		} catch (Exception ex) {
			logger.log(Level.SEVERE, null, ex);
		}

	}

	private Predicate[] getSearchPredicates(Root<Seat> root,
			Subquery<Long> subquery) {

		CriteriaBuilder builder = this.em.getCriteriaBuilder();
		List<Predicate> predicatesList = new ArrayList<Predicate>();

		short seatId = this.example.getSeatId();
		if (seatId != 0) {
			predicatesList.add(builder.equal(root.get("seatId"), seatId));
		}
		SeatCategory seatCategory = this.example.getSeatCategory();
		if (seatCategory != null) {
			predicatesList.add(builder.equal(root.get("seatCategory"),
					seatCategory));
		}
		Sector sector = this.example.getSector();
		if (sector != null) {
			predicatesList.add(builder.equal(root.get("sector"), sector));
		}
		Row row = this.example.getRow();
		if (row != null) {
			predicatesList.add(builder.equal(root.get("row"), row));
		}
		if (eventId != null) {
			Root<Seat> seat = subquery.from(Seat.class);
			Join<Seat, Booking> booking = seat.join("bookings");
			subquery.select(booking.<Long> get("event"));
			List<Predicate> subQueryPredicates = new ArrayList<Predicate>();
			subQueryPredicates.add(builder.equal(root.get("seatId"), booking
					.get("seat").get("seatId")));
			subQueryPredicates.add(builder.equal(
					booking.get("event").get("eventId"), eventId));
			subquery.where(subQueryPredicates.toArray(new Predicate[] {}));
			predicatesList.add(builder.not(builder.exists(subquery)));
		}

		return predicatesList.toArray(new Predicate[predicatesList.size()]);
	}

	public List<Seat> getAll(Short genreId) {
		CriteriaBuilder cb = this.em.getCriteriaBuilder();
		CriteriaQuery<Seat> criteria = cb.createQuery(Seat.class);
		Root<Seat> root = criteria.from(Seat.class);
		criteria.select(root);
		Subquery<Long> subquery = criteria.subquery(Long.class);
		Root<Seat> seat = subquery.from(Seat.class);
		Join<Seat, Genre> genre = seat.join("genres");
		subquery.select(seat.<Long>get("seatId"));
		List<Predicate> subQueryPredicates = new ArrayList<Predicate>();
		subQueryPredicates.add(cb.equal(root.get("seatId"), seat.get("seatId")));
		subQueryPredicates.add(cb.equal(genre.get("genreId"), genreId));
		subquery.where(subQueryPredicates.toArray(new Predicate[] {}));
		criteria.where(cb.not(cb.exists(subquery)));
		return this.em.createQuery(criteria).getResultList();
	}

	public void search() {
		this.page = 0;
	}

	public int getPageSize() {
		return 10;
	}

	public Integer getEventId() {
		return eventId;
	}

	public void setEventId(Integer eventId) {
		this.eventId = eventId;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}

	public Seat getExample() {
		return example;
	}

	public void setExample(Seat example) {
		this.example = example;
	}

	public List<Seat> getPageItems() {
		return pageItems;
	}

	public void setPageItems(List<Seat> pageItems) {
		this.pageItems = pageItems;
	}

}
