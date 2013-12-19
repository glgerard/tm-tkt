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
 * Sector generated by hbm2java
 */
@Entity
@Table(name="SECTOR"
    ,catalog="teatromanzoni"
    , uniqueConstraints = @UniqueConstraint(columnNames="SECTOR_NAME") 
)
public class Sector  implements java.io.Serializable {


     private byte sectorId;
     private String sectorName;
     private Set<Seat> seats = new HashSet<Seat>(0);
     private Set<PriceScheme> priceSchemes = new HashSet<PriceScheme>(0);

    public Sector() {
    }

	
    public Sector(byte sectorId, String sectorName) {
        this.sectorId = sectorId;
        this.sectorName = sectorName;
    }
    public Sector(byte sectorId, String sectorName, Set<Seat> seats, Set<PriceScheme> priceSchemes) {
       this.sectorId = sectorId;
       this.sectorName = sectorName;
       this.seats = seats;
       this.priceSchemes = priceSchemes;
    }
   
     @Id 

    
    @Column(name="SECTOR_id", unique=true, nullable=false)
    public byte getSectorId() {
        return this.sectorId;
    }
    
    public void setSectorId(byte sectorId) {
        this.sectorId = sectorId;
    }

    
    @Column(name="SECTOR_NAME", unique=true, nullable=false, length=45)
    public String getSectorName() {
        return this.sectorName;
    }
    
    public void setSectorName(String sectorName) {
        this.sectorName = sectorName;
    }

@OneToMany(fetch=FetchType.LAZY, mappedBy="sector")
    public Set<Seat> getSeats() {
        return this.seats;
    }
    
    public void setSeats(Set<Seat> seats) {
        this.seats = seats;
    }

@OneToMany(fetch=FetchType.LAZY, mappedBy="sector")
    public Set<PriceScheme> getPriceSchemes() {
        return this.priceSchemes;
    }
    
    public void setPriceSchemes(Set<PriceScheme> priceSchemes) {
        this.priceSchemes = priceSchemes;
    }




}


