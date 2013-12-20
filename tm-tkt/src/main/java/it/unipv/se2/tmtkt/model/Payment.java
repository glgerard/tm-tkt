package it.unipv.se2.tmtkt.model;
// Generated Dec 20, 2013 3:47:27 PM by Hibernate Tools 3.4.0.CR1


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
import javax.persistence.UniqueConstraint;

/**
 * Payment generated by hbm2java
 */
@Entity
@Table(name="PAYMENT"
    ,catalog="teatromanzoni"
    , uniqueConstraints = {@UniqueConstraint(columnNames="INVOICE_NUMBER"), @UniqueConstraint(columnNames="TRANSACTION_id")} 
)
public class Payment  implements java.io.Serializable {


     private Integer paymentId;
     private String transactionId;
     private String invoiceNumber;
     private String paymentMethod;
     private Set<Sale> sales = new HashSet<Sale>(0);

    public Payment() {
    }

	
    public Payment(String transactionId, String paymentMethod) {
        this.transactionId = transactionId;
        this.paymentMethod = paymentMethod;
    }
    public Payment(String transactionId, String invoiceNumber, String paymentMethod, Set<Sale> sales) {
       this.transactionId = transactionId;
       this.invoiceNumber = invoiceNumber;
       this.paymentMethod = paymentMethod;
       this.sales = sales;
    }
   
     @Id @GeneratedValue(strategy=IDENTITY)

    
    @Column(name="PAYMENT_id", unique=true, nullable=false)
    public Integer getPaymentId() {
        return this.paymentId;
    }
    
    public void setPaymentId(Integer paymentId) {
        this.paymentId = paymentId;
    }

    
    @Column(name="TRANSACTION_id", unique=true, nullable=false, length=45)
    public String getTransactionId() {
        return this.transactionId;
    }
    
    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    
    @Column(name="INVOICE_NUMBER", unique=true, length=45)
    public String getInvoiceNumber() {
        return this.invoiceNumber;
    }
    
    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    
    @Column(name="PAYMENT_METHOD", nullable=false, length=45)
    public String getPaymentMethod() {
        return this.paymentMethod;
    }
    
    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

@OneToMany(fetch=FetchType.LAZY, mappedBy="payment")
    public Set<Sale> getSales() {
        return this.sales;
    }
    
    public void setSales(Set<Sale> sales) {
        this.sales = sales;
    }




}


