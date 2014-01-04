package it.unipv.se2.tmtkt.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;

import it.unipv.se2.tmtkt.model.Booking;
import it.unipv.se2.tmtkt.model.BookingId;
import it.unipv.se2.tmtkt.model.Genre;
import it.unipv.se2.tmtkt.model.Seat;
import it.unipv.se2.tmtkt.view.SeatBean;

@Named
@Stateful
@RequestScoped
public class SeatController implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer eventId;
	
	@Inject private SeatBean seatBean;
	
	@PersistenceContext(type = PersistenceContextType.TRANSACTION)
	private EntityManager em;
	
	private int page;
	private long count;
	private List<Seat> pageItems;
	private List<Seat> all = new ArrayList<Seat>();

	public void paginate() {
		int removed = 0;
		pageItems = new ArrayList<Seat>();
		seatBean.paginate();
		List<Seat> seatBeanPageItems = seatBean.getPageItems();
		if (seatBeanPageItems != null && !seatBeanPageItems.isEmpty()) {
			for (Seat s : seatBeanPageItems) {
				if (isFree(s))
					pageItems.add(s);
				else
					removed++;
			}
		}
		this.count = seatBean.getCount() - removed;
	}

	public List<Seat> getAll(Byte genreId) {
		List<Seat> allSeats = seatBean.getAll();
		if (genreId != null) {
			for (Seat s: allSeats) {
				if (!s.getGenres().contains(this.em.find(Genre.class, genreId))) {
					all.add(s);
				}
			}
		} else {
			all = allSeats;
		}
		return all;
	}
	
	public boolean isFree(Seat s) {
		Booking booking = this.em.find(Booking.class,
				new BookingId(eventId, s.getSeatId()));
		return (booking == null);
	}
	
	public void search() {
		seatBean.setPage(0);
		this.page = 0;
	}
	
	public int getPageSize() {
		return seatBean.getPageSize();
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

	public List<Seat> getPageItems() {
		return pageItems;
	}

	public void setPageItems(List<Seat> pageItems) {
		this.pageItems = pageItems;
	}

}
