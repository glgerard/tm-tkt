package it.unipv.se2.tmtkt.model;
// Generated Dec 19, 2013 3:47:23 PM by Hibernate Tools 3.4.0.CR1


import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * Subscription generated by hbm2java
 */
@Entity
@Table(name="SUBSCRIPTION"
    ,catalog="teatromanzoni"
    , uniqueConstraints = @UniqueConstraint(columnNames="TYPE") 
)
public class Subscription  implements java.io.Serializable {


     private SubscriptionId id;
     private Seat seat;
     private Sale sale;
     private String type;

    public Subscription() {
    }

    public Subscription(SubscriptionId id, Seat seat, Sale sale, String type) {
       this.id = id;
       this.seat = seat;
       this.sale = sale;
       this.type = type;
    }
   
     @EmbeddedId

    
    @AttributeOverrides( {
        @AttributeOverride(name="subscriptionId", column=@Column(name="SUBSCRIPTION_id", nullable=false) ), 
        @AttributeOverride(name="saleId", column=@Column(name="SALE_id", nullable=false) ) } )
    public SubscriptionId getId() {
        return this.id;
    }
    
    public void setId(SubscriptionId id) {
        this.id = id;
    }

@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="SEAT_id", nullable=false)
    public Seat getSeat() {
        return this.seat;
    }
    
    public void setSeat(Seat seat) {
        this.seat = seat;
    }

@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="SALE_id", nullable=false, insertable=false, updatable=false)
    public Sale getSale() {
        return this.sale;
    }
    
    public void setSale(Sale sale) {
        this.sale = sale;
    }

    
    @Column(name="TYPE", unique=true, nullable=false, length=45)
    public String getType() {
        return this.type;
    }
    
    public void setType(String type) {
        this.type = type;
    }




}


