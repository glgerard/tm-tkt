package it.unipv.se2.tmtkt.model;
// Generated Dec 26, 2013 12:17:49 PM by Hibernate Tools 3.4.0.CR1


import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Season generated by hbm2java
 */
@Entity
@Table(name="SEASON"
    ,catalog="teatromanzoni"
)
public class Season  implements java.io.Serializable {


     private Byte seasonId;
     private Date startDate;
     private String endDate;
     private Set<PriceScheme> priceSchemes = new HashSet<PriceScheme>(0);

    public Season() {
    }

    public Season(Date startDate, String endDate, Set<PriceScheme> priceSchemes) {
       this.startDate = startDate;
       this.endDate = endDate;
       this.priceSchemes = priceSchemes;
    }
   
     @Id @GeneratedValue(strategy=IDENTITY)

    
    @Column(name="SEASON_id", unique=true, nullable=false)
    public Byte getSeasonId() {
        return this.seasonId;
    }
    
    public void setSeasonId(Byte seasonId) {
        this.seasonId = seasonId;
    }

    @Temporal(TemporalType.DATE)
    @Column(name="START_DATE", length=10)
    public Date getStartDate() {
        return this.startDate;
    }
    
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    
    @Column(name="END_DATE", length=45)
    public String getEndDate() {
        return this.endDate;
    }
    
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

@OneToMany(fetch=FetchType.LAZY, mappedBy="season")
    public Set<PriceScheme> getPriceSchemes() {
        return this.priceSchemes;
    }
    
    public void setPriceSchemes(Set<PriceScheme> priceSchemes) {
        this.priceSchemes = priceSchemes;
    }




}


