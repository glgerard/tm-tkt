package it.unipv.se2.tmtkt.model;
// Generated Dec 23, 2013 1:14:38 PM by Hibernate Tools 3.4.0.CR1


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
 * UserCategory generated by hbm2java
 */
@Entity
@Table(name="USER_CATEGORY"
    ,catalog="teatromanzoni"
    , uniqueConstraints = @UniqueConstraint(columnNames="DESCRIPTION") 
)
public class UserCategory  implements java.io.Serializable {


     private byte userCategoryId;
     private String description;
     private Set<User> users = new HashSet<User>(0);

    public UserCategory() {
    }

	
    public UserCategory(byte userCategoryId, String description) {
        this.userCategoryId = userCategoryId;
        this.description = description;
    }
    public UserCategory(byte userCategoryId, String description, Set<User> users) {
       this.userCategoryId = userCategoryId;
       this.description = description;
       this.users = users;
    }
   
     @Id 

    
    @Column(name="USER_CATEGORY_id", unique=true, nullable=false)
    public byte getUserCategoryId() {
        return this.userCategoryId;
    }
    
    public void setUserCategoryId(byte userCategoryId) {
        this.userCategoryId = userCategoryId;
    }

    
    @Column(name="DESCRIPTION", unique=true, nullable=false, length=45)
    public String getDescription() {
        return this.description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }

@OneToMany(fetch=FetchType.LAZY, mappedBy="userCategory")
    public Set<User> getUsers() {
        return this.users;
    }
    
    public void setUsers(Set<User> users) {
        this.users = users;
    }




}


