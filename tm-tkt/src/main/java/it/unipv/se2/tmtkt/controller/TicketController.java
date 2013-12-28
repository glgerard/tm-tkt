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

	private PriceScheme priceScheme = new PriceScheme(25);

	private Payment payment = new Payment();

	private Sale sale = new Sale();

	private Ticket ticket = new Ticket();

	private Booking booking = new Booking();
	
	@PostConstruct
	public void init() {
		username = loginBean.getUsername();
	}

	@PersistenceContext(type = PersistenceContextType.TRANSACTION)
	private EntityManager em;

	public String buy() {

		try {
			this.em.persist(priceScheme);

			Calendar rightNow = Calendar.getInstance();
			String transactionID = String.valueOf(rightNow.getTimeInMillis());

			payment.setPaymentMethod(paymentMethod);
			payment.setTransactionId(transactionID);

			this.em.persist(payment);

			Event event = this.em.find(Event.class, eventId);
			Seat seat = this.em.find(Seat.class, seatId);

			sale.setUser(this.em.find(User.class, username));
			sale.setPayment(payment);
			sale.setPriceScheme(priceScheme);

			booking.setSeat(seat);
			booking.setSale(sale);
			booking.setEvent(event);

			this.em.persist(sale);
			
			this.em.persist(booking);

			sale.getBookings().add(booking);

			ticket.setSale(sale);

			sale.getTickets().add(ticket);

			this.em.merge(sale);
			this.em.persist(ticket);
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
