package it.unipv.se2.tmtkt.model;
// Generated Dec 19, 2013 10:08:59 PM by Hibernate Tools 3.4.0.CR1


import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * TicketId generated by hbm2java
 */
@Embeddable
public class TicketId  implements java.io.Serializable {


     private int ticketId;
     private int saleId;

    public TicketId() {
    }

    public TicketId(int ticketId, int saleId) {
       this.ticketId = ticketId;
       this.saleId = saleId;
    }
   


    @Column(name="TICKET_id", nullable=false)
    public int getTicketId() {
        return this.ticketId;
    }
    
    public void setTicketId(int ticketId) {
        this.ticketId = ticketId;
    }


    @Column(name="SALE_id", nullable=false)
    public int getSaleId() {
        return this.saleId;
    }
    
    public void setSaleId(int saleId) {
        this.saleId = saleId;
    }


   public boolean equals(Object other) {
         if ( (this == other ) ) return true;
		 if ( (other == null ) ) return false;
		 if ( !(other instanceof TicketId) ) return false;
		 TicketId castOther = ( TicketId ) other; 
         
		 return (this.getTicketId()==castOther.getTicketId())
 && (this.getSaleId()==castOther.getSaleId());
   }
   
   public int hashCode() {
         int result = 17;
         
         result = 37 * result + this.getTicketId();
         result = 37 * result + this.getSaleId();
         return result;
   }   


}


