package it.unipv.se2.tmtkt.model;
// Generated Dec 19, 2013 10:08:59 PM by Hibernate Tools 3.4.0.CR1


import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * SeatCategory generated by hbm2java
 */
@Entity
@Table(name="SEAT_CATEGORY"
    ,catalog="teatromanzoni"
    , uniqueConstraints = @UniqueConstraint(columnNames="CATEGORY_NAME") 
)
public class SeatCategory  implements java.io.Serializable {


     private int seatCategoryId;
     private String categoryName;
     private Set<Seat> seats = new HashSet<Seat>(0);

    public SeatCategory() {
    }

	
    public SeatCategory(int seatCategoryId, String categoryName) {
        this.seatCategoryId = seatCategoryId;
        this.categoryName = categoryName;
    }
    public SeatCategory(int seatCategoryId, String categoryName, Set<Seat> seats) {
       this.seatCategoryId = seatCategoryId;
       this.categoryName = categoryName;
       this.seats = seats;
    }
   
     @Id 

    
    @Column(name="SEAT_CATEGORY_id", unique=true, nullable=false)
    public int getSeatCategoryId() {
        return this.seatCategoryId;
    }
    
    public void setSeatCategoryId(int seatCategoryId) {
        this.seatCategoryId = seatCategoryId;
    }

    
    @Column(name="CATEGORY_NAME", unique=true, nullable=false, length=45)
    public String getCategoryName() {
        return this.categoryName;
    }
    
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

@OneToMany(fetch=FetchType.LAZY, mappedBy="seatCategory")
    public Set<Seat> getSeats() {
        return this.seats;
    }
    
    public void setSeats(Set<Seat> seats) {
        this.seats = seats;
    }




}


