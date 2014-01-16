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

import it.unipv.se2.tmtkt.model.Timeofday;
import it.unipv.se2.tmtkt.model.PriceScheme;
import java.util.Iterator;

/**
 * Backing bean for Timeofday entities.
 * <p>
 * This class provides CRUD functionality for all Timeofday entities. It focuses
 * purely on Java EE 6 standards (e.g. <tt>&#64;ConversationScoped</tt> for
 * state management, <tt>PersistenceContext</tt> for persistence,
 * <tt>CriteriaBuilder</tt> for searches) rather than introducing a CRUD framework or
 * custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class TimeofdayBean implements Serializable
{

   private static final long serialVersionUID = 1L;

   /*
    * Support creating and retrieving Timeofday entities
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

   private Timeofday timeofday;

   public Timeofday getTimeofday()
   {
      return this.timeofday;
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
         this.timeofday = this.example;
      }
      else
      {
         this.timeofday = findById(getId());
      }
   }

   public Timeofday findById(Short id)
   {

      return this.entityManager.find(Timeofday.class, id);
   }

   /*
    * Support updating and deleting Timeofday entities
    */

   public String update()
   {
      this.conversation.end();

      try
      {
         if (this.id == null)
         {
            this.entityManager.persist(this.timeofday);
            return "search?faces-redirect=true";
         }
         else
         {
            this.entityManager.merge(this.timeofday);
            return "view?faces-redirect=true&id=" + this.timeofday.getTimeofdayId();
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
         Timeofday deletableEntity = findById(getId());
         Iterator<PriceScheme> iterPriceSchemes = deletableEntity.getPriceSchemes().iterator();
         for (; iterPriceSchemes.hasNext();)
         {
            PriceScheme nextInPriceSchemes = iterPriceSchemes.next();
            nextInPriceSchemes.setTimeofday(null);
            iterPriceSchemes.remove();
            this.entityManager.merge(nextInPriceSchemes);
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
    * Support searching Timeofday entities with pagination
    */

   private int page;
   private long count;
   private List<Timeofday> pageItems;

   private Timeofday example = new Timeofday();

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

   public Timeofday getExample()
   {
      return this.example;
   }

   public void setExample(Timeofday example)
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
      Root<Timeofday> root = countCriteria.from(Timeofday.class);
      countCriteria = countCriteria.select(builder.count(root)).where(
            getSearchPredicates(root));
      this.count = this.entityManager.createQuery(countCriteria)
            .getSingleResult();

      // Populate this.pageItems

      CriteriaQuery<Timeofday> criteria = builder.createQuery(Timeofday.class);
      root = criteria.from(Timeofday.class);
      TypedQuery<Timeofday> query = this.entityManager.createQuery(criteria
            .select(root).where(getSearchPredicates(root)));
      query.setFirstResult(this.page * getPageSize()).setMaxResults(
            getPageSize());
      this.pageItems = query.getResultList();
   }

   private Predicate[] getSearchPredicates(Root<Timeofday> root)
   {

      CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
      List<Predicate> predicatesList = new ArrayList<Predicate>();

      String endTime = this.example.getEndTime();
      if (endTime != null && !"".equals(endTime))
      {
         predicatesList.add(builder.like(root.<String> get("endTime"), '%' + endTime + '%'));
      }

      return predicatesList.toArray(new Predicate[predicatesList.size()]);
   }

   public List<Timeofday> getPageItems()
   {
      return this.pageItems;
   }

   public long getCount()
   {
      return this.count;
   }

   /*
    * Support listing and POSTing back Timeofday entities (e.g. from inside an
    * HtmlSelectOneMenu)
    */

   public List<Timeofday> getAll()
   {

      CriteriaQuery<Timeofday> criteria = this.entityManager
            .getCriteriaBuilder().createQuery(Timeofday.class);
      return this.entityManager.createQuery(
            criteria.select(criteria.from(Timeofday.class))).getResultList();
   }

   @Resource
   private SessionContext sessionContext;

   public Converter getConverter()
   {

      final TimeofdayBean ejbProxy = this.sessionContext.getBusinessObject(TimeofdayBean.class);

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

            return String.valueOf(((Timeofday) value).getTimeofdayId());
         }
      };
   }

   /*
    * Support adding children to bidirectional, one-to-many tables
    */

   private Timeofday add = new Timeofday();

   public Timeofday getAdd()
   {
      return this.add;
   }

   public Timeofday getAdded()
   {
      Timeofday added = this.add;
      this.add = new Timeofday();
      return added;
   }
}