package it.unipv.se2.tmtkt.model;
// Generated Jan 2, 2014 6:25:18 PM by Hibernate Tools 3.4.0.CR1


import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Seat generated by hbm2java
 */
@Entity
@Table(name="SEAT"
    ,catalog="teatromanzoni"
)
public class Seat  implements java.io.Serializable {


     private short seatId;
     private SeatCategory seatCategory;
     private Sector sector;
     private Row row;
     private Set<Subscription> subscriptions = new HashSet<Subscription>(0);
     private Set<Booking> bookings = new HashSet<Booking>(0);

    public Seat() {
    }

	
    public Seat(short seatId, SeatCategory seatCategory, Sector sector, Row row) {
        this.seatId = seatId;
        this.seatCategory = seatCategory;
        this.sector = sector;
        this.row = row;
    }
    public Seat(short seatId, SeatCategory seatCategory, Sector sector, Row row, Set<Subscription> subscriptions, Set<Booking> bookings) {
       this.seatId = seatId;
       this.seatCategory = seatCategory;
       this.sector = sector;
       this.row = row;
       this.subscriptions = subscriptions;
       this.bookings = bookings;
    }
   
     @Id 

    
    @Column(name="SEAT_id", unique=true, nullable=false)
    public short getSeatId() {
        return this.seatId;
    }
    
    public void setSeatId(short seatId) {
        this.seatId = seatId;
    }

@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="SEAT_CATEGORY_id", nullable=false)
    public SeatCategory getSeatCategory() {
        return this.seatCategory;
    }
    
    public void setSeatCategory(SeatCategory seatCategory) {
        this.seatCategory = seatCategory;
    }

@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="SECTOR_id", nullable=false)
    public Sector getSector() {
        return this.sector;
    }
    
    public void setSector(Sector sector) {
        this.sector = sector;
    }

@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="ROW_id", nullable=false)
    public Row getRow() {
        return this.row;
    }
    
    public void setRow(Row row) {
        this.row = row;
    }

@OneToMany(fetch=FetchType.LAZY, mappedBy="seat")
    public Set<Subscription> getSubscriptions() {
        return this.subscriptions;
    }
    
    public void setSubscriptions(Set<Subscription> subscriptions) {
        this.subscriptions = subscriptions;
    }

@OneToMany(fetch=FetchType.LAZY, mappedBy="seat")
    public Set<Booking> getBookings() {
        return this.bookings;
    }
    
    public void setBookings(Set<Booking> bookings) {
        this.bookings = bookings;
    }




}


