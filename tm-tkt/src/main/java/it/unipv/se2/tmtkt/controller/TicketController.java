package it.unipv.se2.tmtkt.controller;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.enterprise.context.*;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.servlet.http.HttpServletRequest;

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

	private Integer saleId;
	
	private Integer eventId;

	private Short seatId;
	
	private PaymentMethod paymentMethod;
	
	private Event event;
	private Seat seat;
	private User user;
	private PriceScheme priceScheme;
	
	@Inject private TicketCounter ticketCounter;
	
	@Inject private TransactionTestImpl transactionManager;
	
    @Inject private PriceSchemeController priceSchemeController;

	@PersistenceContext(type = PersistenceContextType.TRANSACTION)
	private EntityManager em;

	public void create() {
	    FacesContext context = FacesContext.getCurrentInstance();
	    HttpServletRequest request = (HttpServletRequest) context.
	        getExternalContext().getRequest();
	    
	    String userId = request.getRemoteUser();
	    
    	this.event = this.em.find(Event.class, eventId);
    	this.seat = this.em.find(Seat.class, seatId);
    	this.user = this.em.find(User.class, userId );
    	
    	this.priceScheme = priceSchemeController.ticketPrice(event, seat, user);		
	}
	
	public long getPrice() {
		if (this.priceScheme == null)
			create();
		return this.priceScheme.getPrice();
	}
	
	public String buy() {
		try {
	    	if (this.priceScheme == null)
	    		create();
	    	// this.em.persist(priceScheme);

			String transactionID = String.valueOf(transactionManager.getTransaction());

			Payment payment = new Payment(paymentMethod, transactionID);

			this.em.persist(payment);

			Sale sale = new Sale(this.user, payment, priceScheme, 'T');

			this.em.persist(sale);			

			Booking booking = new Booking(
					new BookingId(eventId, seatId),
					this.seat, sale, this.event);
			
			this.em.persist(booking);

			sale.getBookings().add(booking);

			Ticket ticket = new Ticket(sale,ticketCounter.incrementCount());

			this.em.persist(ticket);

			sale.setTicket(ticket);

			this.saleId = sale.getSaleId();
					
			return "viewTicket";
		} catch (Exception ex) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage("Exception (" + TicketController.class.getName() + "): " + 
							ex.getMessage()));
		    Logger.getLogger(TicketController.class.getName()).
	           log(Level.SEVERE, null, ex);
			return null;
		}
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

	public Integer getSaleId() {
		return saleId;
	}

	public void setSaleId(Integer saleId) {
		this.saleId = saleId;
	}

}
