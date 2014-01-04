package it.unipv.se2.tmtkt.controller;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.servlet.http.HttpServletRequest;

import it.unipv.se2.tmtkt.model.Booking;
import it.unipv.se2.tmtkt.model.BookingId;
import it.unipv.se2.tmtkt.model.Event;
import it.unipv.se2.tmtkt.model.Genre;
import it.unipv.se2.tmtkt.model.PaymentMethod;
import it.unipv.se2.tmtkt.model.PriceScheme;
import it.unipv.se2.tmtkt.model.Sale;
import it.unipv.se2.tmtkt.model.Seat;
import it.unipv.se2.tmtkt.model.Show;
import it.unipv.se2.tmtkt.model.Subscription;
import it.unipv.se2.tmtkt.model.SubscriptionType;
import it.unipv.se2.tmtkt.model.User;
import it.unipv.se2.tmtkt.model.Payment;

@Named
@Stateful
@RequestScoped
public class SubscriptionController implements Serializable {
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final short ASSIGNED_SEAT = 1;
	private static final short FREE_SEAT = 2;
	private static final short CARNET = 3;
	
	private Byte genreId;
	private Short subscriptionTypeId;
	private Short seatId;
	private Short numberOfBookings;
		
	private PaymentMethod paymentMethod;
		
	@EJB private SubscriptionCounter subscriptionCounter;
	
	@EJB private TransactionTestImpl transactionManager;
	
	@PersistenceContext(type = PersistenceContextType.TRANSACTION)
	private EntityManager em;

	public String buy() {
		try {
			PriceScheme priceScheme = new PriceScheme(25);

			this.em.persist(priceScheme);

			String transactionID = String.valueOf(transactionManager.getTransaction());

			Payment payment = new Payment(paymentMethod, transactionID);

			this.em.persist(payment);

		    FacesContext context = FacesContext.getCurrentInstance();
		    HttpServletRequest request = (HttpServletRequest) context.
		        getExternalContext().getRequest();
		    
			Sale sale = new Sale(
					this.em.find(User.class, request.getRemoteUser()),
					payment, priceScheme, 'S');

			this.em.persist(sale);			

			Genre genre = this.em.find(Genre.class, this.genreId);
			SubscriptionType subscriptionType = this.em.find(SubscriptionType.class, this.subscriptionTypeId);
			Subscription subscription = new Subscription(genre,sale,
					subscriptionType,subscriptionCounter.incrementCount());

			switch (subscriptionTypeId) {
			case ASSIGNED_SEAT :
				Seat seat = this.em.find(Seat.class, seatId);
				if ( seat == null) {
					FacesContext.getCurrentInstance().addMessage(null,
							new FacesMessage("A seat is mandatory for an Assigen Seat type subscription"));
					return null;
				} else {
					subscription.setSeat(seat);
					for (Show s: genre.getShows()) {
						for (Event e : s.getEvents()) {
							// check that the date is the same excluding time
							if ((e.getDatetime().getTime() - s.getFirstEventDate().getTime()) <= (24*3600*1000) ) {
								BookingId bookingId = new BookingId(e.getEventId(), seat.getSeatId());
								if (this.em.find(Booking.class, bookingId) == null) {
									Booking booking = new Booking(
											bookingId,
											seat, sale, e);	
									this.em.persist(booking);
									sale.getBookings().add(booking);
									seat.getGenres().add(this.em.find(Genre.class,this.genreId));
								} else {
									this.numberOfBookings++;
								}						
							}
						}
					}
					subscription.setNumberOfBookings(this.numberOfBookings);
				}
				break;
			case FREE_SEAT :
				subscription.setNumberOfBookings((short)genre.getShows().size());
				break;
			case CARNET :
				if ( this.numberOfBookings == 0 ) {
					FacesContext.getCurrentInstance().addMessage(null,
							new FacesMessage("A number of tickets is mandatory for a Carnet type subscription"));
					return null;
				} else {
					subscription.setNumberOfBookings(this.numberOfBookings);
				}
				break;
			}
			
			this.em.persist(subscription);

			sale.setSubscription(subscription);

			return "/subscription/search?faces-redirect=true";
		} catch (Exception ex) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage("Exception (" + SubscriptionController.class.getName() + "): " +
							ex.getMessage()));
		    Logger.getLogger(SubscriptionController.class.getName()).
	           log(Level.SEVERE, null, ex);
			return null;
		}
	}

	public Short getSubscriptionTypeId() {
		return subscriptionTypeId;
	}

	public void setSubscriptionTypeId(Short subscriptionTypeId) {
		this.subscriptionTypeId = subscriptionTypeId;
	}

	public Short getSeatId() {
		return seatId;
	}

	public void setSeatId(Short seatId) {
		this.seatId = seatId;
	}

	public Short getNumberOfBookings() {
		return numberOfBookings;
	}

	public void setNumberOfBookings(Short numberOfBookings) {
		this.numberOfBookings = numberOfBookings;
	}

	public PaymentMethod getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(PaymentMethod paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public Byte getGenreId() {
		return genreId;
	}

	public void setGenreId(Byte genreId) {
		this.genreId = genreId;
	}

	public String subscriptionTypeName() {
		switch (this.subscriptionTypeId) {
		case ASSIGNED_SEAT:
			return "assignedSeat";
		case FREE_SEAT:
			return "freeSeat";
		case CARNET:
			return "carnet";
		default:
			return null;
		}
	}
}
