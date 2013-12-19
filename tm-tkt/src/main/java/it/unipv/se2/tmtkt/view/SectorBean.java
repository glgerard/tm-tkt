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

import it.unipv.se2.tmtkt.model.Sector;
import it.unipv.se2.tmtkt.model.PriceScheme;
import it.unipv.se2.tmtkt.model.Seat;
import java.util.Iterator;

/**
 * Backing bean for Sector entities.
 * <p>
 * This class provides CRUD functionality for all Sector entities. It focuses
 * purely on Java EE 6 standards (e.g. <tt>&#64;ConversationScoped</tt> for
 * state management, <tt>PersistenceContext</tt> for persistence,
 * <tt>CriteriaBuilder</tt> for searches) rather than introducing a CRUD framework or
 * custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class SectorBean implements Serializable
{

   private static final long serialVersionUID = 1L;

   /*
    * Support creating and retrieving Sector entities
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

   private Sector sector;

   public Sector getSector()
   {
      return this.sector;
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
         this.sector = this.example;
      }
      else
      {
         this.sector = findById(getId());
      }
   }

   public Sector findById(Byte id)
   {

      return this.entityManager.find(Sector.class, id);
   }

   /*
    * Support updating and deleting Sector entities
    */

   public String update()
   {
      this.conversation.end();

      try
      {
         if (this.id == null)
         {
            this.entityManager.persist(this.sector);
            return "search?faces-redirect=true";
         }
         else
         {
            this.entityManager.merge(this.sector);
            return "view?faces-redirect=true&id=" + this.sector.getSectorId();
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
         Sector deletableEntity = findById(getId());
         Iterator<Seat> iterSeats = deletableEntity.getSeats().iterator();
         for (; iterSeats.hasNext();)
         {
            Seat nextInSeats = iterSeats.next();
            nextInSeats.setSector(null);
            iterSeats.remove();
            this.entityManager.merge(nextInSeats);
         }
         Iterator<PriceScheme> iterPriceSchemes = deletableEntity.getPriceSchemes().iterator();
         for (; iterPriceSchemes.hasNext();)
         {
            PriceScheme nextInPriceSchemes = iterPriceSchemes.next();
            nextInPriceSchemes.setSector(null);
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
    * Support searching Sector entities with pagination
    */

   private int page;
   private long count;
   private List<Sector> pageItems;

   private Sector example = new Sector();

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

   public Sector getExample()
   {
      return this.example;
   }

   public void setExample(Sector example)
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
      Root<Sector> root = countCriteria.from(Sector.class);
      countCriteria = countCriteria.select(builder.count(root)).where(
            getSearchPredicates(root));
      this.count = this.entityManager.createQuery(countCriteria)
            .getSingleResult();

      // Populate this.pageItems

      CriteriaQuery<Sector> criteria = builder.createQuery(Sector.class);
      root = criteria.from(Sector.class);
      TypedQuery<Sector> query = this.entityManager.createQuery(criteria
            .select(root).where(getSearchPredicates(root)));
      query.setFirstResult(this.page * getPageSize()).setMaxResults(
            getPageSize());
      this.pageItems = query.getResultList();
   }

   private Predicate[] getSearchPredicates(Root<Sector> root)
   {

      CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
      List<Predicate> predicatesList = new ArrayList<Predicate>();

      byte sectorId = this.example.getSectorId();
      if (sectorId != 0)
      {
         predicatesList.add(builder.equal(root.get("sectorId"), sectorId));
      }
      String sectorName = this.example.getSectorName();
      if (sectorName != null && !"".equals(sectorName))
      {
         predicatesList.add(builder.like(root.<String> get("sectorName"), '%' + sectorName + '%'));
      }

      return predicatesList.toArray(new Predicate[predicatesList.size()]);
   }

   public List<Sector> getPageItems()
   {
      return this.pageItems;
   }

   public long getCount()
   {
      return this.count;
   }

   /*
    * Support listing and POSTing back Sector entities (e.g. from inside an
    * HtmlSelectOneMenu)
    */

   public List<Sector> getAll()
   {

      CriteriaQuery<Sector> criteria = this.entityManager
            .getCriteriaBuilder().createQuery(Sector.class);
      return this.entityManager.createQuery(
            criteria.select(criteria.from(Sector.class))).getResultList();
   }

   @Resource
   private SessionContext sessionContext;

   public Converter getConverter()
   {

      final SectorBean ejbProxy = this.sessionContext.getBusinessObject(SectorBean.class);

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

            return String.valueOf(((Sector) value).getSectorId());
         }
      };
   }

   /*
    * Support adding children to bidirectional, one-to-many tables
    */

   private Sector add = new Sector();

   public Sector getAdd()
   {
      return this.add;
   }

   public Sector getAdded()
   {
      Sector added = this.add;
      this.add = new Sector();
      return added;
   }
}