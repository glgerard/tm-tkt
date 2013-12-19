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

import it.unipv.se2.tmtkt.model.Season;
import it.unipv.se2.tmtkt.model.PriceScheme;
import java.util.Iterator;

/**
 * Backing bean for Season entities.
 * <p>
 * This class provides CRUD functionality for all Season entities. It focuses
 * purely on Java EE 6 standards (e.g. <tt>&#64;ConversationScoped</tt> for
 * state management, <tt>PersistenceContext</tt> for persistence,
 * <tt>CriteriaBuilder</tt> for searches) rather than introducing a CRUD framework or
 * custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class SeasonBean implements Serializable
{

   private static final long serialVersionUID = 1L;

   /*
    * Support creating and retrieving Season entities
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

   private Season season;

   public Season getSeason()
   {
      return this.season;
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
         this.season = this.example;
      }
      else
      {
         this.season = findById(getId());
      }
   }

   public Season findById(Byte id)
   {

      return this.entityManager.find(Season.class, id);
   }

   /*
    * Support updating and deleting Season entities
    */

   public String update()
   {
      this.conversation.end();

      try
      {
         if (this.id == null)
         {
            this.entityManager.persist(this.season);
            return "search?faces-redirect=true";
         }
         else
         {
            this.entityManager.merge(this.season);
            return "view?faces-redirect=true&id=" + this.season.getSeasonId();
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
         Season deletableEntity = findById(getId());
         Iterator<PriceScheme> iterPriceSchemes = deletableEntity.getPriceSchemes().iterator();
         for (; iterPriceSchemes.hasNext();)
         {
            PriceScheme nextInPriceSchemes = iterPriceSchemes.next();
            nextInPriceSchemes.setSeason(null);
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
    * Support searching Season entities with pagination
    */

   private int page;
   private long count;
   private List<Season> pageItems;

   private Season example = new Season();

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

   public Season getExample()
   {
      return this.example;
   }

   public void setExample(Season example)
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
      Root<Season> root = countCriteria.from(Season.class);
      countCriteria = countCriteria.select(builder.count(root)).where(
            getSearchPredicates(root));
      this.count = this.entityManager.createQuery(countCriteria)
            .getSingleResult();

      // Populate this.pageItems

      CriteriaQuery<Season> criteria = builder.createQuery(Season.class);
      root = criteria.from(Season.class);
      TypedQuery<Season> query = this.entityManager.createQuery(criteria
            .select(root).where(getSearchPredicates(root)));
      query.setFirstResult(this.page * getPageSize()).setMaxResults(
            getPageSize());
      this.pageItems = query.getResultList();
   }

   private Predicate[] getSearchPredicates(Root<Season> root)
   {

      CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
      List<Predicate> predicatesList = new ArrayList<Predicate>();

      String endDate = this.example.getEndDate();
      if (endDate != null && !"".equals(endDate))
      {
         predicatesList.add(builder.like(root.<String> get("endDate"), '%' + endDate + '%'));
      }

      return predicatesList.toArray(new Predicate[predicatesList.size()]);
   }

   public List<Season> getPageItems()
   {
      return this.pageItems;
   }

   public long getCount()
   {
      return this.count;
   }

   /*
    * Support listing and POSTing back Season entities (e.g. from inside an
    * HtmlSelectOneMenu)
    */

   public List<Season> getAll()
   {

      CriteriaQuery<Season> criteria = this.entityManager
            .getCriteriaBuilder().createQuery(Season.class);
      return this.entityManager.createQuery(
            criteria.select(criteria.from(Season.class))).getResultList();
   }

   @Resource
   private SessionContext sessionContext;

   public Converter getConverter()
   {

      final SeasonBean ejbProxy = this.sessionContext.getBusinessObject(SeasonBean.class);

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

            return String.valueOf(((Season) value).getSeasonId());
         }
      };
   }

   /*
    * Support adding children to bidirectional, one-to-many tables
    */

   private Season add = new Season();

   public Season getAdd()
   {
      return this.add;
   }

   public Season getAdded()
   {
      Season added = this.add;
      this.add = new Season();
      return added;
   }
}