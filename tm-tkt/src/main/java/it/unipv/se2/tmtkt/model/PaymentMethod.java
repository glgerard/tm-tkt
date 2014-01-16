package it.unipv.se2.tmtkt.model;
// Generated Jan 15, 2014 2:28:27 PM by Hibernate Tools 3.4.0.CR1


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
 * PaymentMethod generated by hbm2java
 */
@Entity
@Table(name="PAYMENT_METHOD"
    , uniqueConstraints = @UniqueConstraint(columnNames="PAYMENT_NAME") 
)
public class PaymentMethod  implements java.io.Serializable {


     private short paymentMethodId;
     private String paymentName;
     private Set<Payment> payments = new HashSet<Payment>(0);

    public PaymentMethod() {
    }

	
    public PaymentMethod(short paymentMethodId, String paymentName) {
        this.paymentMethodId = paymentMethodId;
        this.paymentName = paymentName;
    }
    public PaymentMethod(short paymentMethodId, String paymentName, Set<Payment> payments) {
       this.paymentMethodId = paymentMethodId;
       this.paymentName = paymentName;
       this.payments = payments;
    }
   
     @Id 

    
    @Column(name="PAYMENT_METHOD_id", unique=true, nullable=false)
    public short getPaymentMethodId() {
        return this.paymentMethodId;
    }
    
    public void setPaymentMethodId(short paymentMethodId) {
        this.paymentMethodId = paymentMethodId;
    }

    
    @Column(name="PAYMENT_NAME", unique=true, nullable=false, length=45)
    public String getPaymentName() {
        return this.paymentName;
    }
    
    public void setPaymentName(String paymentName) {
        this.paymentName = paymentName;
    }

@OneToMany(fetch=FetchType.LAZY, mappedBy="paymentMethod")
    public Set<Payment> getPayments() {
        return this.payments;
    }
    
    public void setPayments(Set<Payment> payments) {
        this.payments = payments;
    }




}


