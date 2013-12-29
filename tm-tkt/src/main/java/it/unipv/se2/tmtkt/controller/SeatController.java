package it.unipv.se2.tmtkt.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateful;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;

import it.unipv.se2.tmtkt.model.Booking;
import it.unipv.se2.tmtkt.model.BookingId;
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
	
	@Inject private SeatBean bean;
	
	@PersistenceContext(type = PersistenceContextType.TRANSACTION)
	private EntityManager em;
	
	private int page;
	private long count;
	private List<Seat> pageItems;

	public void paginate() {
		int removed = 0;
		pageItems = new ArrayList<Seat>();
		bean.paginate();
		List<Seat> seatBeanPageItems = bean.getPageItems();
		if (seatBeanPageItems != null && !seatBeanPageItems.isEmpty()) {
			for (Seat s : seatBeanPageItems) {
				if (isFree(s))
					pageItems.add(s);
				else
					removed++;
			}
		}
		this.count = bean.getCount() - removed;
	}

	public boolean isFree(Seat s) {
		Booking booking = this.em.find(Booking.class, new BookingId(
				eventId, s.getSeatId()));
		return (booking == null);
	}
	
	public void search() {
		bean.setPage(0);
		this.page = 0;
	}
	
	public int getPageSize() {
		return bean.getPageSize();
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
