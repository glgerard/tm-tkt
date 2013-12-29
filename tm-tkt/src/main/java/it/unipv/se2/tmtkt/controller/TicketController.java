package it.unipv.se2.tmtkt.controller;

import java.util.Calendar;
import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.ejb.Stateful;
import javax.enterprise.context.*;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;

import it.unipv.se2.tmtkt.model.Booking;
import it.unipv.se2.tmtkt.model.BookingId;
import it.unipv.se2.tmtkt.model.Event;
import it.unipv.se2.tmtkt.model.PaymentMethod;
import it.unipv.se2.tmtkt.model.PriceScheme;
import it.unipv.se2.tmtkt.model.Sale;
import it.unipv.se2.tmtkt.model.Seat;
import it.unipv.se2.tmtkt.model.Ticket;
import it.unipv.se2.tmtkt.model.User;
import it.unipv.se2.tmtkt.model.Payment;

@Named
@Stateful
@RequestScoped
public class TicketController implements Serializable {
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject private LoginBean loginBean;

	private Integer eventId;

	private Short seatId;

	private String username;
	
	private PaymentMethod paymentMethod;
	
	@PostConstruct
	public void init() {
		username = loginBean.getUsername();
	}

	@PersistenceContext(type = PersistenceContextType.TRANSACTION)
	private EntityManager em;

	public String buy() {
		try {
			PriceScheme priceScheme = new PriceScheme(25);

			this.em.persist(priceScheme);

			Calendar rightNow = Calendar.getInstance();
			String transactionID = String.valueOf(rightNow.getTimeInMillis());

			Payment payment = new Payment(paymentMethod, transactionID);

			this.em.persist(payment);

			Event event = this.em.find(Event.class, eventId);
			Seat seat = this.em.find(Seat.class, seatId);

			Sale sale = new Sale(
					this.em.find(User.class, username),
					payment, priceScheme);

			this.em.persist(sale);			

			Booking booking = new Booking(
					new BookingId(eventId, seatId),
					seat, sale, event);
			
			this.em.persist(booking);

			sale.getBookings().add(booking);

			Ticket ticket = new Ticket(sale);

			this.em.persist(ticket);

			sale.getTickets().add(ticket);

			return "/ticket/search?faces-redirect=true";
		} catch (Exception ex) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Exception: " + ex.getMessage()));

			System.out.println("ERROR (ticketController.buy): " + ex.getMessage());
			return null;
		}
	}
	
	public LoginBean getLoginBean() {
		return loginBean;
	}

	public void setLoginBean(LoginBean loginBean) {
		this.loginBean = loginBean;
	}

	public Integer getEventId() {
		return eventId;
	}

	public void setEventId(Integer eventId) {
		this.eventId = eventId;
	}

	public Short getSeatId() {
		return seatId;
	}

	public void setSeatId(Short seatId) {
		this.seatId = seatId;
	}
	
	public PaymentMethod getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(PaymentMethod paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

}
