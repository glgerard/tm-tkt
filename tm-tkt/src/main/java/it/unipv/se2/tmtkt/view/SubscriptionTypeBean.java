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

import it.unipv.se2.tmtkt.model.SubscriptionType;
import it.unipv.se2.tmtkt.model.Subscription;
import java.util.Iterator;

/**
 * Backing bean for SubscriptionType entities.
 * <p>
 * This class provides CRUD functionality for all SubscriptionType entities. It focuses
 * purely on Java EE 6 standards (e.g. <tt>&#64;ConversationScoped</tt> for
 * state management, <tt>PersistenceContext</tt> for persistence,
 * <tt>CriteriaBuilder</tt> for searches) rather than introducing a CRUD framework or
 * custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class SubscriptionTypeBean implements Serializable
{

   private static final long serialVersionUID = 1L;

   /*
    * Support creating and retrieving SubscriptionType entities
    */

   private Short id;

   public Short getId()
   {
      return this.id;
   }

   public void setId(Short id)
   {
      this.id = id;
   }

   private SubscriptionType subscriptionType;

   public SubscriptionType getSubscriptionType()
   {
      return this.subscriptionType;
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
         this.subscriptionType = this.example;
      }
      else
      {
         this.subscriptionType = findById(getId());
      }
   }

   public SubscriptionType findById(Short id)
   {

      return this.entityManager.find(SubscriptionType.class, id);
   }

   /*
    * Support updating and deleting SubscriptionType entities
    */

   public String update()
   {
      this.conversation.end();

      try
      {
         if (this.id == null)
         {
            this.entityManager.persist(this.subscriptionType);
            return "search?faces-redirect=true";
         }
         else
         {
            this.entityManager.merge(this.subscriptionType);
            return "view?faces-redirect=true&id=" + this.subscriptionType.getSubscriptionTypeId();
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
         SubscriptionType deletableEntity = findById(getId());
         Iterator<Subscription> iterSubscriptions = deletableEntity.getSubscriptions().iterator();
         for (; iterSubscriptions.hasNext();)
         {
            Subscription nextInSubscriptions = iterSubscriptions.next();
            nextInSubscriptions.setSubscriptionType(null);
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
    * Support searching SubscriptionType entities with pagination
    */

   private int page;
   private long count;
   private List<SubscriptionType> pageItems;

   private SubscriptionType example = new SubscriptionType();

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

   public SubscriptionType getExample()
   {
      return this.example;
   }

   public void setExample(SubscriptionType example)
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
      Root<SubscriptionType> root = countCriteria.from(SubscriptionType.class);
      countCriteria = countCriteria.select(builder.count(root)).where(
            getSearchPredicates(root));
      this.count = this.entityManager.createQuery(countCriteria)
            .getSingleResult();

      // Populate this.pageItems

      CriteriaQuery<SubscriptionType> criteria = builder.createQuery(SubscriptionType.class);
      root = criteria.from(SubscriptionType.class);
      TypedQuery<SubscriptionType> query = this.entityManager.createQuery(criteria
            .select(root).where(getSearchPredicates(root)));
      query.setFirstResult(this.page * getPageSize()).setMaxResults(
            getPageSize());
      this.pageItems = query.getResultList();
   }

   private Predicate[] getSearchPredicates(Root<SubscriptionType> root)
   {

      CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
      List<Predicate> predicatesList = new ArrayList<Predicate>();

      short subscriptionTypeId = this.example.getSubscriptionTypeId();
      if (subscriptionTypeId != 0)
      {
         predicatesList.add(builder.equal(root.get("subscriptionTypeId"), subscriptionTypeId));
      }
      String name = this.example.getName();
      if (name != null && !"".equals(name))
      {
         predicatesList.add(builder.like(root.<String> get("name"), '%' + name + '%'));
      }

      return predicatesList.toArray(new Predicate[predicatesList.size()]);
   }

   public List<SubscriptionType> getPageItems()
   {
      return this.pageItems;
   }

   public long getCount()
   {
      return this.count;
   }

   /*
    * Support listing and POSTing back SubscriptionType entities (e.g. from inside an
    * HtmlSelectOneMenu)
    */

   public List<SubscriptionType> getAll()
   {

      CriteriaQuery<SubscriptionType> criteria = this.entityManager
            .getCriteriaBuilder().createQuery(SubscriptionType.class);
      return this.entityManager.createQuery(
            criteria.select(criteria.from(SubscriptionType.class))).getResultList();
   }

   @Resource
   private SessionContext sessionContext;

   public Converter getConverter()
   {

      final SubscriptionTypeBean ejbProxy = this.sessionContext.getBusinessObject(SubscriptionTypeBean.class);

      return new Converter()
      {

         @Override
         public Object getAsObject(FacesContext context,
               UIComponent component, String value)
         {

            return ejbProxy.findById(Short.valueOf(value));
         }

         @Override
         public String getAsString(FacesContext context,
               UIComponent component, Object value)
         {

            if (value == null)
            {
               return "";
            }

            return String.valueOf(((SubscriptionType) value).getSubscriptionTypeId());
         }
      };
   }

   /*
    * Support adding children to bidirectional, one-to-many tables
    */

   private SubscriptionType add = new SubscriptionType();

   public SubscriptionType getAdd()
   {
      return this.add;
   }

   public SubscriptionType getAdded()
   {
      SubscriptionType added = this.add;
      this.add = new SubscriptionType();
      return added;
   }
}