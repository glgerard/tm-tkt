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

import it.unipv.se2.tmtkt.model.Sale;
import it.unipv.se2.tmtkt.model.Booking;
import it.unipv.se2.tmtkt.model.Customer;
import it.unipv.se2.tmtkt.model.Payment;
import it.unipv.se2.tmtkt.model.PriceScheme;
import it.unipv.se2.tmtkt.model.Subscription;
import it.unipv.se2.tmtkt.model.Ticket;
import java.util.Iterator;

/**
 * Backing bean for Sale entities.
 * <p>
 * This class provides CRUD functionality for all Sale entities. It focuses
 * purely on Java EE 6 standards (e.g. <tt>&#64;ConversationScoped</tt> for
 * state management, <tt>PersistenceContext</tt> for persistence,
 * <tt>CriteriaBuilder</tt> for searches) rather than introducing a CRUD framework or
 * custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class SaleBean implements Serializable
{

   private static final long serialVersionUID = 1L;

   /*
    * Support creating and retrieving Sale entities
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

   private Sale sale;

   public Sale getSale()
   {
      return this.sale;
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
         this.sale = this.example;
      }
      else
      {
         this.sale = findById(getId());
      }
   }

   public Sale findById(Integer id)
   {

      return this.entityManager.find(Sale.class, id);
   }

   /*
    * Support updating and deleting Sale entities
    */

   public String update()
   {
      this.conversation.end();

      try
      {
         if (this.id == null)
         {
            this.entityManager.persist(this.sale);
            return "search?faces-redirect=true";
         }
         else
         {
            this.entityManager.merge(this.sale);
            return "view?faces-redirect=true&id=" + this.sale.getSaleId();
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
         Sale deletableEntity = findById(getId());
         Customer customer = deletableEntity.getCustomer();
         customer.getSales().remove(deletableEntity);
         deletableEntity.setCustomer(null);
         this.entityManager.merge(customer);
         Payment payment = deletableEntity.getPayment();
         payment.getSales().remove(deletableEntity);
         deletableEntity.setPayment(null);
         this.entityManager.merge(payment);
         PriceScheme priceScheme = deletableEntity.getPriceScheme();
         priceScheme.getSales().remove(deletableEntity);
         deletableEntity.setPriceScheme(null);
         this.entityManager.merge(priceScheme);
         Iterator<Booking> iterBookings = deletableEntity.getBookings().iterator();
         for (; iterBookings.hasNext();)
         {
            Booking nextInBookings = iterBookings.next();
            nextInBookings.setSale(null);
            iterBookings.remove();
            this.entityManager.merge(nextInBookings);
         }
         Iterator<Ticket> iterTickets = deletableEntity.getTickets().iterator();
         for (; iterTickets.hasNext();)
         {
            Ticket nextInTickets = iterTickets.next();
            nextInTickets.setSale(null);
            iterTickets.remove();
            this.entityManager.merge(nextInTickets);
         }
         Iterator<Subscription> iterSubscriptions = deletableEntity.getSubscriptions().iterator();
         for (; iterSubscriptions.hasNext();)
         {
            Subscription nextInSubscriptions = iterSubscriptions.next();
            nextInSubscriptions.setSale(null);
            iterSubscriptions.remove();
            this.entityManager.merge(nextInSubscriptions);
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
    * Support searching Sale entities with pagination
    */

   private int page;
   private long count;
   private List<Sale> pageItems;

   private Sale example = new Sale();

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

   public Sale getExample()
   {
      return this.example;
   }

   public void setExample(Sale example)
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
      Root<Sale> root = countCriteria.from(Sale.class);
      countCriteria = countCriteria.select(builder.count(root)).where(
            getSearchPredicates(root));
      this.count = this.entityManager.createQuery(countCriteria)
            .getSingleResult();

      // Populate this.pageItems

      CriteriaQuery<Sale> criteria = builder.createQuery(Sale.class);
      root = criteria.from(Sale.class);
      TypedQuery<Sale> query = this.entityManager.createQuery(criteria
            .select(root).where(getSearchPredicates(root)));
      query.setFirstResult(this.page * getPageSize()).setMaxResults(
            getPageSize());
      this.pageItems = query.getResultList();
   }

   private Predicate[] getSearchPredicates(Root<Sale> root)
   {

      CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
      List<Predicate> predicatesList = new ArrayList<Predicate>();

      Customer customer = this.example.getCustomer();
      if (customer != null)
      {
         predicatesList.add(builder.equal(root.get("customer"), customer));
      }
      Payment payment = this.example.getPayment();
      if (payment != null)
      {
         predicatesList.add(builder.equal(root.get("payment"), payment));
      }
      PriceScheme priceScheme = this.example.getPriceScheme();
      if (priceScheme != null)
      {
         predicatesList.add(builder.equal(root.get("priceScheme"), priceScheme));
      }

      return predicatesList.toArray(new Predicate[predicatesList.size()]);
   }

   public List<Sale> getPageItems()
   {
      return this.pageItems;
   }

   public long getCount()
   {
      return this.count;
   }

   /*
    * Support listing and POSTing back Sale entities (e.g. from inside an
    * HtmlSelectOneMenu)
    */

   public List<Sale> getAll()
   {

      CriteriaQuery<Sale> criteria = this.entityManager
            .getCriteriaBuilder().createQuery(Sale.class);
      return this.entityManager.createQuery(
            criteria.select(criteria.from(Sale.class))).getResultList();
   }

   @Resource
   private SessionContext sessionContext;

   public Converter getConverter()
   {

      final SaleBean ejbProxy = this.sessionContext.getBusinessObject(SaleBean.class);

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

            return String.valueOf(((Sale) value).getSaleId());
         }
      };
   }

   /*
    * Support adding children to bidirectional, one-to-many tables
    */

   private Sale add = new Sale();

   public Sale getAdd()
   {
      return this.add;
   }

   public Sale getAdded()
   {
      Sale added = this.add;
      this.add = new Sale();
      return added;
   }
}