package it.unipv.se2.tmtkt.model;
// Generated Jan 4, 2014 12:15:21 PM by Hibernate Tools 3.4.0.CR1


import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * UserRoles generated by hbm2java
 */
@Entity
@Table(name="USER_ROLES"
    ,catalog="teatromanzoni"
)
public class UserRoles  implements java.io.Serializable {


     private UserRolesId id;

    public UserRoles() {
    }

    public UserRoles(UserRolesId id) {
       this.id = id;
    }
   
     @EmbeddedId

    
    @AttributeOverrides( {
        @AttributeOverride(name="email", column=@Column(name="EMAIL", nullable=false, length=90) ), 
        @AttributeOverride(name="role", column=@Column(name="ROLE", nullable=false, length=45) ) } )
    public UserRolesId getId() {
        return this.id;
    }
    
    public void setId(UserRolesId id) {
        this.id = id;
    }




}

