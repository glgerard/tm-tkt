package it.unipv.se2.tmtkt.controller;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.Stateful;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;

import it.unipv.se2.tmtkt.model.Booking;
import it.unipv.se2.tmtkt.model.BookingId;
import it.unipv.se2.tmtkt.model.Event;
import it.unipv.se2.tmtkt.model.Genre;
import it.unipv.se2.tmtkt.model.PaymentMethod;
import it.unipv.se2.tmtkt.model.PriceScheme;
import it.unipv.se2.tmtkt.model.Sale;
import it.unipv.se2.tmtkt.model.Seat;
import it.unipv.se2.tmtkt.model.Play;
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
	
	private Short genreId;
	private Integer saleId;
	private Short subscriptionTypeId;
	private Integer eventId;
	private Short seatId;
	private Short prepaidTickets;
		
	private PaymentMethod paymentMethod;
		
	@Inject private SubscriptionCounter subscriptionCounter;
	
	@Inject private TransactionTestImpl transactionManager;
	
	@PersistenceContext(type = PersistenceContextType.EXTENDED)
	private EntityManager em;

	public String buy() {
		try {
			PriceScheme priceScheme = new PriceScheme(15*countEventsForGenre());

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
					context.addMessage(null,
							new FacesMessage("A seat is mandatory for an Assigned Seat type subscription"));
					return null;
				} else {
					subscription.setSeat(seat);
					for (Play s: genre.getPlays()) {
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
									this.prepaidTickets++;
								}						
							}
						}
					}
					subscription.setPrepaidTickets(this.prepaidTickets);
				}
				break;
			case FREE_SEAT :
				subscription.setPrepaidTickets((short)genre.getPlays().size());
				break;
			case CARNET :
				if ( this.prepaidTickets == 0 ) {
					context.addMessage(null,
							new FacesMessage("A number of tickets is mandatory for a Carnet type subscription"));
					return null;
				} else {
					subscription.setPrepaidTickets((short) (
							this.prepaidTickets < countEventsForGenre() ? this.prepaidTickets : countEventsForGenre()));
				}
				break;
			}
			
			this.em.persist(subscription);

			sale.setSubscription(subscription);

			return "success";
		} catch (Exception ex) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage("Exception (" + SubscriptionController.class.getName() + "): " +
							ex.getMessage()));
		    Logger.getLogger(SubscriptionController.class.getName()).
	           log(Level.SEVERE, null, ex);
			return null;
		}
	}

	public String addBooking() {

	    FacesContext context = FacesContext.getCurrentInstance();
	    HttpServletRequest request = (HttpServletRequest) context.
	        getExternalContext().getRequest();
	    
		String userName = request.getRemoteUser();
		
		Sale sale = this.em.find(Sale.class, this.saleId);

		if (userName.contentEquals(sale.getUser().getEmail())) {
			Seat seat = this.em.find(Seat.class, this.seatId);
			Event event = this.em.find(Event.class, this.eventId);
			Booking booking = new Booking(new BookingId(this.eventId, this.seatId), seat, sale, event);
			sale.getBookings().add(booking);
			this.em.persist(booking);
			this.em.persist(sale);
			sale.getSubscription().setPrepaidTickets((short) (sale.getSubscription().getPrepaidTickets()-(short)1));
			return "subscriptions";
		} else {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage("ERROR: this sale id is not associated with your user!"));
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

	public PaymentMethod getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(PaymentMethod paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public Short getGenreId() {
		return genreId;
	}

	public void setGenreId(Short genreId) {
		this.genreId = genreId;
	}

	public String subscriptionTypeName() {		
		if (countEventsForGenre() > 0) {
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
		} else {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage("Sorry no Plays with a matching genre found on schedule for this year!"));
			return null;
		}
	}

	private static Short _countEventsForGenre;
	
	private int countEventsForGenre() {
		if (_countEventsForGenre == null) {
			_countEventsForGenre = 0;
			Genre genre = this.em.find(Genre.class, genreId);
			Set<Play> Plays = genre.getPlays();
			for (Play s : Plays) {
				_countEventsForGenre = (short) (_countEventsForGenre + s
						.getEvents().size());
			}
		}
		return _countEventsForGenre;
	}

	public List<Genre> getAllGenreOnSchedule() {
		CriteriaBuilder cb = this.em.getCriteriaBuilder();
		CriteriaQuery<Genre> criteria = cb.createQuery(Genre.class);
		Root<Genre> root = criteria.from(Genre.class);
		criteria.select(root);
		root.join("plays");
		return this.em.createQuery(criteria).getResultList();
	}
	
	public Short getPrepaidTickets() {
		return prepaidTickets;
	}

	public void setPrepaidTickets(Short prepaidTickets) {
		this.prepaidTickets = prepaidTickets;
	}

	public Integer getEventId() {
		return eventId;
	}

	public void setEventId(Integer eventId) {
		this.eventId = eventId;
	}

	public Event getEvent() {
		return this.em.getReference(Event.class, eventId);
	}

	public Seat getSeat() {
		return this.em.getReference(Seat.class, seatId);
	}

	public Integer getSaleId() {
		return saleId;
	}

	public void setSaleId(Integer saleId) {
		this.saleId = saleId;
	}
}
