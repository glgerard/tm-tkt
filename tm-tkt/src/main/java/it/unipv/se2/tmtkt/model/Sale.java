package it.unipv.se2.tmtkt.model;
// Generated Dec 20, 2013 3:47:27 PM by Hibernate Tools 3.4.0.CR1


import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Sale generated by hbm2java
 */
@Entity
@Table(name="SALE"
    ,catalog="teatromanzoni"
)
public class Sale  implements java.io.Serializable {


     private Integer saleId;
     private User user;
     private Payment payment;
     private PriceScheme priceScheme;
     private Set<Booking> bookings = new HashSet<Booking>(0);
     private Set<Ticket> tickets = new HashSet<Ticket>(0);
     private Set<Subscription> subscriptions = new HashSet<Subscription>(0);

    public Sale() {
    }

	
    public Sale(User user, Payment payment, PriceScheme priceScheme) {
        this.user = user;
        this.payment = payment;
        this.priceScheme = priceScheme;
    }
    public Sale(User user, Payment payment, PriceScheme priceScheme, Set<Booking> bookings, Set<Ticket> tickets, Set<Subscription> subscriptions) {
       this.user = user;
       this.payment = payment;
       this.priceScheme = priceScheme;
       this.bookings = bookings;
       this.tickets = tickets;
       this.subscriptions = subscriptions;
    }
   
     @Id @GeneratedValue(strategy=IDENTITY)

    
    @Column(name="SALE_id", unique=true, nullable=false)
    public Integer getSaleId() {
        return this.saleId;
    }
    
    public void setSaleId(Integer saleId) {
        this.saleId = saleId;
    }

@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="USER_id", nullable=false)
    public User getUser() {
        return this.user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }

@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="PAYMENT_id", nullable=false)
    public Payment getPayment() {
        return this.payment;
    }
    
    public void setPayment(Payment payment) {
        this.payment = payment;
    }

@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="PRICE_SCHEME_id", nullable=false)
    public PriceScheme getPriceScheme() {
        return this.priceScheme;
    }
    
    public void setPriceScheme(PriceScheme priceScheme) {
        this.priceScheme = priceScheme;
    }

@OneToMany(fetch=FetchType.LAZY, mappedBy="sale")
    public Set<Booking> getBookings() {
        return this.bookings;
    }
    
    public void setBookings(Set<Booking> bookings) {
        this.bookings = bookings;
    }

@OneToMany(fetch=FetchType.LAZY, mappedBy="sale")
    public Set<Ticket> getTickets() {
        return this.tickets;
    }
    
    public void setTickets(Set<Ticket> tickets) {
        this.tickets = tickets;
    }

@OneToMany(fetch=FetchType.LAZY, mappedBy="sale")
    public Set<Subscription> getSubscriptions() {
        return this.subscriptions;
    }
    
    public void setSubscriptions(Set<Subscription> subscriptions) {
        this.subscriptions = subscriptions;
    }

    public String toString() {
    	return String.valueOf(this.getSaleId());
    }

}


