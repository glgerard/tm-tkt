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

/**
 * Ticket generated by hbm2java
 */
@Entity
@Table(name="TICKET"
    ,catalog="teatromanzoni"
)
public class Ticket  implements java.io.Serializable {


     private TicketId id;
     private Sale sale;

    public Ticket() {
    }

    public Ticket(TicketId id, Sale sale) {
       this.id = id;
       this.sale = sale;
    }
   
     @EmbeddedId

    
    @AttributeOverrides( {
        @AttributeOverride(name="ticketId", column=@Column(name="TICKET_id", nullable=false) ), 
        @AttributeOverride(name="saleId", column=@Column(name="SALE_id", nullable=false) ) } )
    public TicketId getId() {
        return this.id;
    }
    
    public void setId(TicketId id) {
        this.id = id;
    }

@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="SALE_id", nullable=false, insertable=false, updatable=false)
    public Sale getSale() {
        return this.sale;
    }
    
    public void setSale(Sale sale) {
        this.sale = sale;
    }




}


