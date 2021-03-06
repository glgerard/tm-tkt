package it.unipv.se2.tmtkt.model;
// Generated Jan 15, 2014 2:28:27 PM by Hibernate Tools 3.4.0.CR1


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

/**
 * Dayofweek generated by hbm2java
 */
@Entity
@Table(name="DAYOFWEEK")
public class Dayofweek  implements java.io.Serializable {


     private Short dayofweekId;
     private String day;
     private Set<PriceScheme> priceSchemes = new HashSet<PriceScheme>(0);

    public Dayofweek() {
    }

    public Dayofweek(String day, Set<PriceScheme> priceSchemes) {
       this.day = day;
       this.priceSchemes = priceSchemes;
    }
   
     @Id @GeneratedValue(strategy=IDENTITY)

    
    @Column(name="DAYOFWEEK_id", unique=true, nullable=false)
    public Short getDayofweekId() {
        return this.dayofweekId;
    }
    
    public void setDayofweekId(Short dayofweekId) {
        this.dayofweekId = dayofweekId;
    }

    
    @Column(name="DAY", length=45)
    public String getDay() {
        return this.day;
    }
    
    public void setDay(String day) {
        this.day = day;
    }

@OneToMany(fetch=FetchType.LAZY, mappedBy="dayofweek")
    public Set<PriceScheme> getPriceSchemes() {
        return this.priceSchemes;
    }
    
    public void setPriceSchemes(Set<PriceScheme> priceSchemes) {
        this.priceSchemes = priceSchemes;
    }




}


