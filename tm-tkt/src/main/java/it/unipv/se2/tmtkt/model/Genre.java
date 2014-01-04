package it.unipv.se2.tmtkt.model;
// Generated Jan 4, 2014 12:15:21 PM by Hibernate Tools 3.4.0.CR1


import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * Genre generated by hbm2java
 */
@Entity
@Table(name="GENRE"
    ,catalog="teatromanzoni"
    , uniqueConstraints = @UniqueConstraint(columnNames="NAME") 
)
public class Genre  implements java.io.Serializable {


     private Byte genreId;
     private String name;
     private Set<Show> shows = new HashSet<Show>(0);
     private Set<Seat> seats = new HashSet<Seat>(0);
     private Set<PriceScheme> priceSchemes = new HashSet<PriceScheme>(0);
     private Set<Subscription> subscriptions = new HashSet<Subscription>(0);

    public Genre() {
    }

	
    public Genre(String name) {
        this.name = name;
    }
    public Genre(String name, Set<Show> shows, Set<Seat> seats, Set<PriceScheme> priceSchemes, Set<Subscription> subscriptions) {
       this.name = name;
       this.shows = shows;
       this.seats = seats;
       this.priceSchemes = priceSchemes;
       this.subscriptions = subscriptions;
    }
   
     @Id @GeneratedValue(strategy=IDENTITY)

    
    @Column(name="GENRE_id", unique=true, nullable=false)
    public Byte getGenreId() {
        return this.genreId;
    }
    
    public void setGenreId(Byte genreId) {
        this.genreId = genreId;
    }

    
    @Column(name="NAME", unique=true, nullable=false, length=45)
    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
    }

@OneToMany(fetch=FetchType.LAZY, mappedBy="genre")
    public Set<Show> getShows() {
        return this.shows;
    }
    
    public void setShows(Set<Show> shows) {
        this.shows = shows;
    }

@ManyToMany(fetch=FetchType.LAZY, mappedBy="genres")
    public Set<Seat> getSeats() {
        return this.seats;
    }
    
    public void setSeats(Set<Seat> seats) {
        this.seats = seats;
    }

@OneToMany(fetch=FetchType.LAZY, mappedBy="genre")
    public Set<PriceScheme> getPriceSchemes() {
        return this.priceSchemes;
    }
    
    public void setPriceSchemes(Set<PriceScheme> priceSchemes) {
        this.priceSchemes = priceSchemes;
    }

@OneToMany(fetch=FetchType.LAZY, mappedBy="genre")
    public Set<Subscription> getSubscriptions() {
        return this.subscriptions;
    }
    
    public void setSubscriptions(Set<Subscription> subscriptions) {
        this.subscriptions = subscriptions;
    }




}


