package it.unipv.se2.tmtkt.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateful;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import it.unipv.se2.tmtkt.model.Payment;
import it.unipv.se2.tmtkt.model.Sale;
import java.util.Iterator;

/**
 * Backing bean for Payment entities.
 * <p>
 * This class provides CRUD functionality for all Payment entities. It focuses
 * purely on Java EE 6 standards (e.g. <tt>&#64;ConversationScoped</tt> for
 * state management, <tt>PersistenceContext</tt> for persistence,
 * <tt>CriteriaBuilder</tt> for searches) rather than introducing a CRUD framework or
 * custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class PaymentBean implements Serializable
{

   private static final long serialVersionUID = 1L;

   /*
    * Support creating and retrieving Payment entities
    */

   private Integer id;

   public Integer getId()
   {
      return this.id;
   }

   public void setId(Integer id)
   {
      this.id = id;
   }

   private Payment payment;

   public Payment getPayment()
   {
      return this.payment;
   }

   @Inject
   private Conversation conversation;

   @PersistenceContext(type = PersistenceContextType.EXTENDED)
   private EntityManager entityManager;

   public String create()
   {

      this.conversation.begin();
      return "create?faces-redirect=true";
   }

   public void retrieve()
   {

      if (FacesContext.getCurrentInstance().isPostback())
      {
         return;
      }

      if (this.conversation.isTransient())
      {
         this.conversation.begin();
      }

      if (this.id == null)
      {
         this.payment = this.example;
      }
      else
      {
         this.payment = findById(getId());
      }
   }

   public Payment findById(Integer id)
   {

      return this.entityManager.find(Payment.class, id);
   }

   /*
    * Support updating and deleting Payment entities
    */

   public String update()
   {
      this.conversation.end();

      try
      {
         if (this.id == null)
         {
            this.entityManager.persist(this.payment);
            return "search?faces-redirect=true";
         }
         else
         {
            this.entityManager.merge(this.payment);
            return "view?faces-redirect=true&id=" + this.payment.getPaymentId();
         }
      }
      catch (Exception e)
      {
         FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
         return null;
      }
   }

   public String delete()
   {
      this.conversation.end();

      try
      {
         Payment deletableEntity = findById(getId());
         Iterator<Sale> iterSales = deletableEntity.getSales().iterator();
         for (; iterSales.hasNext();)
         {
            Sale nextInSales = iterSales.next();
            nextInSales.setPayment(null);
            iterSales.remove();
            this.entityManager.merge(nextInSales);
         }
         this.entityManager.remove(deletableEntity);
         this.entityManager.flush();
         return "search?faces-redirect=true";
      }
      catch (Exception e)
      {
         FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
         return null;
      }
   }

   /*
    * Support searching Payment entities with pagination
    */

   private int page;
   private long count;
   private List<Payment> pageItems;

   private Payment example = new Payment();

   public int getPage()
   {
      return this.page;
   }

   public void setPage(int page)
   {
      this.page = page;
   }

   public int getPageSize()
   {
      return 10;
   }

   public Payment getExample()
   {
      return this.example;
   }

   public void setExample(Payment example)
   {
      this.example = example;
   }

   public void search()
   {
      this.page = 0;
   }

   public void paginate()
   {

      CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();

      // Populate this.count

      CriteriaQuery<Long> countCriteria = builder.createQuery(Long.class);
      Root<Payment> root = countCriteria.from(Payment.class);
      countCriteria = countCriteria.select(builder.count(root)).where(
            getSearchPredicates(root));
      this.count = this.entityManager.createQuery(countCriteria)
            .getSingleResult();

      // Populate this.pageItems

      CriteriaQuery<Payment> criteria = builder.createQuery(Payment.class);
      root = criteria.from(Payment.class);
      TypedQuery<Payment> query = this.entityManager.createQuery(criteria
            .select(root).where(getSearchPredicates(root)));
      query.setFirstResult(this.page * getPageSize()).setMaxResults(
            getPageSize());
      this.pageItems = query.getResultList();
   }

   private Predicate[] getSearchPredicates(Root<Payment> root)
   {

      CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
      List<Predicate> predicatesList = new ArrayList<Predicate>();

      String transactionId = this.example.getTransactionId();
      if (transactionId != null && !"".equals(transactionId))
      {
         predicatesList.add(builder.like(root.<String> get("transactionId"), '%' + transactionId + '%'));
      }
      String invoiceNumber = this.example.getInvoiceNumber();
      if (invoiceNumber != null && !"".equals(invoiceNumber))
      {
         predicatesList.add(builder.like(root.<String> get("invoiceNumber"), '%' + invoiceNumber + '%'));
      }
      String paymentMethod = this.example.getPaymentMethod();
      if (paymentMethod != null && !"".equals(paymentMethod))
      {
         predicatesList.add(builder.like(root.<String> get("paymentMethod"), '%' + paymentMethod + '%'));
      }

      return predicatesList.toArray(new Predicate[predicatesList.size()]);
   }

   public List<Payment> getPageItems()
   {
      return this.pageItems;
   }

   public long getCount()
   {
      return this.count;
   }

   /*
    * Support listing and POSTing back Payment entities (e.g. from inside an
    * HtmlSelectOneMenu)
    */

   public List<Payment> getAll()
   {

      CriteriaQuery<Payment> criteria = this.entityManager
            .getCriteriaBuilder().createQuery(Payment.class);
      return this.entityManager.createQuery(
            criteria.select(criteria.from(Payment.class))).getResultList();
   }

   @Resource
   private SessionContext sessionContext;

   public Converter getConverter()
   {

      final PaymentBean ejbProxy = this.sessionContext.getBusinessObject(PaymentBean.class);

      return new Converter()
      {

         @Override
         public Object getAsObject(FacesContext context,
               UIComponent component, String value)
         {

            return ejbProxy.findById(Integer.valueOf(value));
         }

         @Override
         public String getAsString(FacesContext context,
               UIComponent component, Object value)
         {

            if (value == null)
            {
               return "";
            }

            return String.valueOf(((Payment) value).getPaymentId());
         }
      };
   }

   /*
    * Support adding children to bidirectional, one-to-many tables
    */

   private Payment add = new Payment();

   public Payment getAdd()
   {
      return this.add;
   }

   public Payment getAdded()
   {
      Payment added = this.add;
      this.add = new Payment();
      return added;
   }
}