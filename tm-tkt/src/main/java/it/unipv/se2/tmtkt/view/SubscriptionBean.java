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

import it.unipv.se2.tmtkt.model.Subscription;
import it.unipv.se2.tmtkt.model.Genre;
import it.unipv.se2.tmtkt.model.Sale;
import it.unipv.se2.tmtkt.model.Seat;
import it.unipv.se2.tmtkt.model.SubscriptionType;

/**
 * Backing bean for Subscription entities.
 * <p>
 * This class provides CRUD functionality for all Subscription entities. It focuses
 * purely on Java EE 6 standards (e.g. <tt>&#64;ConversationScoped</tt> for
 * state management, <tt>PersistenceContext</tt> for persistence,
 * <tt>CriteriaBuilder</tt> for searches) rather than introducing a CRUD framework or
 * custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class SubscriptionBean implements Serializable
{

   private static final long serialVersionUID = 1L;

   /*
    * Support creating and retrieving Subscription entities
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

   private Subscription subscription;

   public Subscription getSubscription()
   {
      return this.subscription;
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
         this.subscription = this.example;
      }
      else
      {
         this.subscription = findById(getId());
      }
   }

   public Subscription findById(Integer id)
   {

      return this.entityManager.find(Subscription.class, id);
   }

   /*
    * Support updating and deleting Subscription entities
    */

   public String update()
   {
      this.conversation.end();

      try
      {
         if (this.id == null)
         {
            this.entityManager.persist(this.subscription);
            return "search?faces-redirect=true";
         }
         else
         {
            this.entityManager.merge(this.subscription);
            return "view?faces-redirect=true&id=" + this.subscription.getSaleId();
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
         Subscription deletableEntity = findById(getId());
         Seat seat = deletableEntity.getSeat();
         seat.getSubscriptions().remove(deletableEntity);
         deletableEntity.setSeat(null);
         this.entityManager.merge(seat);
         Genre genre = deletableEntity.getGenre();
         genre.getSubscriptions().remove(deletableEntity);
         deletableEntity.setGenre(null);
         this.entityManager.merge(genre);
         Sale sale = deletableEntity.getSale();
         sale.setSubscription(null);
         this.entityManager.merge(sale);
         SubscriptionType subscriptionType = deletableEntity.getSubscriptionType();
         subscriptionType.getSubscriptions().remove(deletableEntity);
         deletableEntity.setSubscriptionType(null);
         this.entityManager.merge(subscriptionType);
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
    * Support searching Subscription entities with pagination
    */

   private int page;
   private long count;
   private List<Subscription> pageItems;

   private Subscription example = new Subscription();

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

   public Subscription getExample()
   {
      return this.example;
   }

   public void setExample(Subscription example)
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
      Root<Subscription> root = countCriteria.from(Subscription.class);
      countCriteria = countCriteria.select(builder.count(root)).where(
            getSearchPredicates(root));
      this.count = this.entityManager.createQuery(countCriteria)
            .getSingleResult();

      // Populate this.pageItems

      CriteriaQuery<Subscription> criteria = builder.createQuery(Subscription.class);
      root = criteria.from(Subscription.class);
      TypedQuery<Subscription> query = this.entityManager.createQuery(criteria
            .select(root).where(getSearchPredicates(root)));
      query.setFirstResult(this.page * getPageSize()).setMaxResults(
            getPageSize());
      this.pageItems = query.getResultList();
   }

   private Predicate[] getSearchPredicates(Root<Subscription> root)
   {

      CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
      List<Predicate> predicatesList = new ArrayList<Predicate>();

      Seat seat = this.example.getSeat();
      if (seat != null)
      {
         predicatesList.add(builder.equal(root.get("seat"), seat));
      }
      Genre genre = this.example.getGenre();
      if (genre != null)
      {
         predicatesList.add(builder.equal(root.get("genre"), genre));
      }
      SubscriptionType subscriptionType = this.example.getSubscriptionType();
      if (subscriptionType != null)
      {
         predicatesList.add(builder.equal(root.get("subscriptionType"), subscriptionType));
      }
      int subscriptionId = this.example.getSubscriptionId();
      if (subscriptionId != 0)
      {
         predicatesList.add(builder.equal(root.get("subscriptionId"), subscriptionId));
      }
      Short numberOfBookings = this.example.getNumberOfBookings();
      if (numberOfBookings != null && numberOfBookings.intValue() != 0)
      {
         predicatesList.add(builder.equal(root.get("numberOfBookings"), numberOfBookings));
      }

      return predicatesList.toArray(new Predicate[predicatesList.size()]);
   }

   public List<Subscription> getPageItems()
   {
      return this.pageItems;
   }

   public long getCount()
   {
      return this.count;
   }

   /*
    * Support listing and POSTing back Subscription entities (e.g. from inside an
    * HtmlSelectOneMenu)
    */

   public List<Subscription> getAll()
   {

      CriteriaQuery<Subscription> criteria = this.entityManager
            .getCriteriaBuilder().createQuery(Subscription.class);
      return this.entityManager.createQuery(
            criteria.select(criteria.from(Subscription.class))).getResultList();
   }

   @Resource
   private SessionContext sessionContext;

   public Converter getConverter()
   {

      final SubscriptionBean ejbProxy = this.sessionContext.getBusinessObject(SubscriptionBean.class);

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

            return String.valueOf(((Subscription) value).getSaleId());
         }
      };
   }

   /*
    * Support adding children to bidirectional, one-to-many tables
    */

   private Subscription add = new Subscription();

   public Subscription getAdd()
   {
      return this.add;
   }

   public Subscription getAdded()
   {
      Subscription added = this.add;
      this.add = new Subscription();
      return added;
   }
}