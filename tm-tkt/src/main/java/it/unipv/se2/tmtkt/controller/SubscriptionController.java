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
import it.unipv.se2.tmtkt.model.PaymentMethod;
import it.unipv.se2.tmtkt.model.PriceScheme;
import it.unipv.se2.tmtkt.model.Sale;
import it.unipv.se2.tmtkt.model.Seat;
import it.unipv.se2.tmtkt.model.Show;
import it.unipv.se2.tmtkt.model.Subscription;
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
	
	private Subscription subscription = new Subscription();
	private int seatId;
		
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

			Seat seat = this.em.find(Seat.class, seatId);

		    FacesContext context = FacesContext.getCurrentInstance();
		    HttpServletRequest request = (HttpServletRequest) context.
		        getExternalContext().getRequest();
		    
			Sale sale = new Sale(
					this.em.find(User.class, request.getRemoteUser()),
					payment, priceScheme, 'S');

			this.em.persist(sale);			

			Subscription newSubscription = new Subscription(subscription.getGenre(),sale,
					subscription.getSubscriptionType(),subscriptionCounter.incrementCount());

			switch (subscription.getSubscriptionType().getSubscriptionTypeId()) {
			case ASSIGNED_SEAT :
				if ( seat == null) {
					FacesContext.getCurrentInstance().addMessage(null,
							new FacesMessage("A seat is mandatory for an Assigen Seat type subscription"));
					return null;
				} else {
					newSubscription.setSeat(seat);
					for (Show s: subscription.getGenre().getShows()) {
						for (Event e : s.getEvents()) {
							if (e.getDatetime() == s.getFirstEventDate()) {
								Booking booking = new Booking(
										new BookingId(e.getEventId(), seat.getSeatId()),
										seat, sale, e);								
								this.em.persist(booking);
								sale.getBookings().add(booking);
							}
						}
					}
				}
				break;
			case FREE_SEAT :
				newSubscription.setNumberOfBookings((short)subscription.getGenre().getShows().size());
				break;
			case CARNET :
				if ( subscription.getNumberOfBookings() == 0 ) {
					FacesContext.getCurrentInstance().addMessage(null,
							new FacesMessage("A number of tickets is mandatory for a Carnet type subscription"));
					return null;
				} else {
					newSubscription.setNumberOfBookings(subscription.getNumberOfBookings());
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
	
	public Subscription getSubscription() {
		return subscription;
	}

	public void setSubscription(Subscription subscription) {
		this.subscription = subscription;
	}
	
	public PaymentMethod getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(PaymentMethod paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

}
