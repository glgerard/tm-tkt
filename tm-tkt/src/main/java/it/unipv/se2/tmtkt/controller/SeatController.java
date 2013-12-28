package it.unipv.se2.tmtkt.controller;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.ejb.Stateful;
import javax.enterprise.context.RequestScoped;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;

import it.unipv.se2.tmtkt.model.Booking;
import it.unipv.se2.tmtkt.model.Event;
import it.unipv.se2.tmtkt.model.Genre;
import it.unipv.se2.tmtkt.model.Seat;
import it.unipv.se2.tmtkt.view.GenreBean;
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
	
	private List<SelectItem> items = new LinkedList<SelectItem>();

	public List<SelectItem> getItems() {
		Event event = this.em.find(Event.class, eventId);
		
		Set<Booking> bookings = event.getBookings();
		
		if (items.isEmpty())
			for (Seat g : bean.getAll()) {
				if (bookings.isEmpty())
					items.add(new SelectItem(g, g.toString()));
 			}
		return items;
	}

	public Integer getEventId() {
		return eventId;
	}

	public void setEventId(Integer eventId) {
		this.eventId = eventId;
	}

}
