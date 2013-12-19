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

import it.unipv.se2.tmtkt.model.PriceScheme;
import it.unipv.se2.tmtkt.model.Dayofweek;
import it.unipv.se2.tmtkt.model.Genre;
import it.unipv.se2.tmtkt.model.Sale;
import it.unipv.se2.tmtkt.model.Season;
import it.unipv.se2.tmtkt.model.Sector;
import it.unipv.se2.tmtkt.model.Timeofday;
import java.util.Iterator;

/**
 * Backing bean for PriceScheme entities.
 * <p>
 * This class provides CRUD functionality for all PriceScheme entities. It focuses
 * purely on Java EE 6 standards (e.g. <tt>&#64;ConversationScoped</tt> for
 * state management, <tt>PersistenceContext</tt> for persistence,
 * <tt>CriteriaBuilder</tt> for searches) rather than introducing a CRUD framework or
 * custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class PriceSchemeBean implements Serializable
{

   private static final long serialVersionUID = 1L;

   /*
    * Support creating and retrieving PriceScheme entities
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

   private PriceScheme priceScheme;

   public PriceScheme getPriceScheme()
   {
      return this.priceScheme;
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
         this.priceScheme = this.example;
      }
      else
      {
         this.priceScheme = findById(getId());
      }
   }

   public PriceScheme findById(Short id)
   {

      return this.entityManager.find(PriceScheme.class, id);
   }

   /*
    * Support updating and deleting PriceScheme entities
    */

   public String update()
   {
      this.conversation.end();

      try
      {
         if (this.id == null)
         {
            this.entityManager.persist(this.priceScheme);
            return "search?faces-redirect=true";
         }
         else
         {
            this.entityManager.merge(this.priceScheme);
            return "view?faces-redirect=true&id=" + this.priceScheme.getPriceSchemeId();
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
         PriceScheme deletableEntity = findById(getId());
         Season season = deletableEntity.getSeason();
         season.getPriceSchemes().remove(deletableEntity);
         deletableEntity.setSeason(null);
         this.entityManager.merge(season);
         Genre genre = deletableEntity.getGenre();
         genre.getPriceSchemes().remove(deletableEntity);
         deletableEntity.setGenre(null);
         this.entityManager.merge(genre);
         Sector sector = deletableEntity.getSector();
         sector.getPriceSchemes().remove(deletableEntity);
         deletableEntity.setSector(null);
         this.entityManager.merge(sector);
         Timeofday timeofday = deletableEntity.getTimeofday();
         timeofday.getPriceSchemes().remove(deletableEntity);
         deletableEntity.setTimeofday(null);
         this.entityManager.merge(timeofday);
         Dayofweek dayofweek = deletableEntity.getDayofweek();
         dayofweek.getPriceSchemes().remove(deletableEntity);
         deletableEntity.setDayofweek(null);
         this.entityManager.merge(dayofweek);
         Iterator<Sale> iterSales = deletableEntity.getSales().iterator();
         for (; iterSales.hasNext();)
         {
            Sale nextInSales = iterSales.next();
            nextInSales.setPriceScheme(null);
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
    * Support searching PriceScheme entities with pagination
    */

   private int page;
   private long count;
   private List<PriceScheme> pageItems;

   private PriceScheme example = new PriceScheme();

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

   public PriceScheme getExample()
   {
      return this.example;
   }

   public void setExample(PriceScheme example)
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
      Root<PriceScheme> root = countCriteria.from(PriceScheme.class);
      countCriteria = countCriteria.select(builder.count(root)).where(
            getSearchPredicates(root));
      this.count = this.entityManager.createQuery(countCriteria)
            .getSingleResult();

      // Populate this.pageItems

      CriteriaQuery<PriceScheme> criteria = builder.createQuery(PriceScheme.class);
      root = criteria.from(PriceScheme.class);
      TypedQuery<PriceScheme> query = this.entityManager.createQuery(criteria
            .select(root).where(getSearchPredicates(root)));
      query.setFirstResult(this.page * getPageSize()).setMaxResults(
            getPageSize());
      this.pageItems = query.getResultList();
   }

   private Predicate[] getSearchPredicates(Root<PriceScheme> root)
   {

      CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
      List<Predicate> predicatesList = new ArrayList<Predicate>();

      Season season = this.example.getSeason();
      if (season != null)
      {
         predicatesList.add(builder.equal(root.get("season"), season));
      }
      Genre genre = this.example.getGenre();
      if (genre != null)
      {
         predicatesList.add(builder.equal(root.get("genre"), genre));
      }
      Sector sector = this.example.getSector();
      if (sector != null)
      {
         predicatesList.add(builder.equal(root.get("sector"), sector));
      }
      Timeofday timeofday = this.example.getTimeofday();
      if (timeofday != null)
      {
         predicatesList.add(builder.equal(root.get("timeofday"), timeofday));
      }
      Dayofweek dayofweek = this.example.getDayofweek();
      if (dayofweek != null)
      {
         predicatesList.add(builder.equal(root.get("dayofweek"), dayofweek));
      }

      return predicatesList.toArray(new Predicate[predicatesList.size()]);
   }

   public List<PriceScheme> getPageItems()
   {
      return this.pageItems;
   }

   public long getCount()
   {
      return this.count;
   }

   /*
    * Support listing and POSTing back PriceScheme entities (e.g. from inside an
    * HtmlSelectOneMenu)
    */

   public List<PriceScheme> getAll()
   {

      CriteriaQuery<PriceScheme> criteria = this.entityManager
            .getCriteriaBuilder().createQuery(PriceScheme.class);
      return this.entityManager.createQuery(
            criteria.select(criteria.from(PriceScheme.class))).getResultList();
   }

   @Resource
   private SessionContext sessionContext;

   public Converter getConverter()
   {

      final PriceSchemeBean ejbProxy = this.sessionContext.getBusinessObject(PriceSchemeBean.class);

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

            return String.valueOf(((PriceScheme) value).getPriceSchemeId());
         }
      };
   }

   /*
    * Support adding children to bidirectional, one-to-many tables
    */

   private PriceScheme add = new PriceScheme();

   public PriceScheme getAdd()
   {
      return this.add;
   }

   public PriceScheme getAdded()
   {
      PriceScheme added = this.add;
      this.add = new PriceScheme();
      return added;
   }
}