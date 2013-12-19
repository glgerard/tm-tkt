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

import it.unipv.se2.tmtkt.model.Dayofweek;
import it.unipv.se2.tmtkt.model.PriceScheme;
import java.util.Iterator;

/**
 * Backing bean for Dayofweek entities.
 * <p>
 * This class provides CRUD functionality for all Dayofweek entities. It focuses
 * purely on Java EE 6 standards (e.g. <tt>&#64;ConversationScoped</tt> for
 * state management, <tt>PersistenceContext</tt> for persistence,
 * <tt>CriteriaBuilder</tt> for searches) rather than introducing a CRUD framework or
 * custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class DayofweekBean implements Serializable
{

   private static final long serialVersionUID = 1L;

   /*
    * Support creating and retrieving Dayofweek entities
    */

   private Byte id;

   public Byte getId()
   {
      return this.id;
   }

   public void setId(Byte id)
   {
      this.id = id;
   }

   private Dayofweek dayofweek;

   public Dayofweek getDayofweek()
   {
      return this.dayofweek;
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
         this.dayofweek = this.example;
      }
      else
      {
         this.dayofweek = findById(getId());
      }
   }

   public Dayofweek findById(Byte id)
   {

      return this.entityManager.find(Dayofweek.class, id);
   }

   /*
    * Support updating and deleting Dayofweek entities
    */

   public String update()
   {
      this.conversation.end();

      try
      {
         if (this.id == null)
         {
            this.entityManager.persist(this.dayofweek);
            return "search?faces-redirect=true";
         }
         else
         {
            this.entityManager.merge(this.dayofweek);
            return "view?faces-redirect=true&id=" + this.dayofweek.getDayofweekId();
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
         Dayofweek deletableEntity = findById(getId());
         Iterator<PriceScheme> iterPriceSchemes = deletableEntity.getPriceSchemes().iterator();
         for (; iterPriceSchemes.hasNext();)
         {
            PriceScheme nextInPriceSchemes = iterPriceSchemes.next();
            nextInPriceSchemes.setDayofweek(null);
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
    * Support searching Dayofweek entities with pagination
    */

   private int page;
   private long count;
   private List<Dayofweek> pageItems;

   private Dayofweek example = new Dayofweek();

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

   public Dayofweek getExample()
   {
      return this.example;
   }

   public void setExample(Dayofweek example)
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
      Root<Dayofweek> root = countCriteria.from(Dayofweek.class);
      countCriteria = countCriteria.select(builder.count(root)).where(
            getSearchPredicates(root));
      this.count = this.entityManager.createQuery(countCriteria)
            .getSingleResult();

      // Populate this.pageItems

      CriteriaQuery<Dayofweek> criteria = builder.createQuery(Dayofweek.class);
      root = criteria.from(Dayofweek.class);
      TypedQuery<Dayofweek> query = this.entityManager.createQuery(criteria
            .select(root).where(getSearchPredicates(root)));
      query.setFirstResult(this.page * getPageSize()).setMaxResults(
            getPageSize());
      this.pageItems = query.getResultList();
   }

   private Predicate[] getSearchPredicates(Root<Dayofweek> root)
   {

      CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
      List<Predicate> predicatesList = new ArrayList<Predicate>();

      String day = this.example.getDay();
      if (day != null && !"".equals(day))
      {
         predicatesList.add(builder.like(root.<String> get("day"), '%' + day + '%'));
      }

      return predicatesList.toArray(new Predicate[predicatesList.size()]);
   }

   public List<Dayofweek> getPageItems()
   {
      return this.pageItems;
   }

   public long getCount()
   {
      return this.count;
   }

   /*
    * Support listing and POSTing back Dayofweek entities (e.g. from inside an
    * HtmlSelectOneMenu)
    */

   public List<Dayofweek> getAll()
   {

      CriteriaQuery<Dayofweek> criteria = this.entityManager
            .getCriteriaBuilder().createQuery(Dayofweek.class);
      return this.entityManager.createQuery(
            criteria.select(criteria.from(Dayofweek.class))).getResultList();
   }

   @Resource
   private SessionContext sessionContext;

   public Converter getConverter()
   {

      final DayofweekBean ejbProxy = this.sessionContext.getBusinessObject(DayofweekBean.class);

      return new Converter()
      {

         @Override
         public Object getAsObject(FacesContext context,
               UIComponent component, String value)
         {

            return ejbProxy.findById(Byte.valueOf(value));
         }

         @Override
         public String getAsString(FacesContext context,
               UIComponent component, Object value)
         {

            if (value == null)
            {
               return "";
            }

            return String.valueOf(((Dayofweek) value).getDayofweekId());
         }
      };
   }

   /*
    * Support adding children to bidirectional, one-to-many tables
    */

   private Dayofweek add = new Dayofweek();

   public Dayofweek getAdd()
   {
      return this.add;
   }

   public Dayofweek getAdded()
   {
      Dayofweek added = this.add;
      this.add = new Dayofweek();
      return added;
   }
}