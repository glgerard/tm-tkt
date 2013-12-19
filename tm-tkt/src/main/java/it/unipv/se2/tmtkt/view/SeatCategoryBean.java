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

import it.unipv.se2.tmtkt.model.SeatCategory;
import it.unipv.se2.tmtkt.model.Seat;
import java.util.Iterator;

/**
 * Backing bean for SeatCategory entities.
 * <p>
 * This class provides CRUD functionality for all SeatCategory entities. It focuses
 * purely on Java EE 6 standards (e.g. <tt>&#64;ConversationScoped</tt> for
 * state management, <tt>PersistenceContext</tt> for persistence,
 * <tt>CriteriaBuilder</tt> for searches) rather than introducing a CRUD framework or
 * custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class SeatCategoryBean implements Serializable
{

   private static final long serialVersionUID = 1L;

   /*
    * Support creating and retrieving SeatCategory entities
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

   private SeatCategory seatCategory;

   public SeatCategory getSeatCategory()
   {
      return this.seatCategory;
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
         this.seatCategory = this.example;
      }
      else
      {
         this.seatCategory = findById(getId());
      }
   }

   public SeatCategory findById(Integer id)
   {

      return this.entityManager.find(SeatCategory.class, id);
   }

   /*
    * Support updating and deleting SeatCategory entities
    */

   public String update()
   {
      this.conversation.end();

      try
      {
         if (this.id == null)
         {
            this.entityManager.persist(this.seatCategory);
            return "search?faces-redirect=true";
         }
         else
         {
            this.entityManager.merge(this.seatCategory);
            return "view?faces-redirect=true&id=" + this.seatCategory.getSeatCategoryId();
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
         SeatCategory deletableEntity = findById(getId());
         Iterator<Seat> iterSeats = deletableEntity.getSeats().iterator();
         for (; iterSeats.hasNext();)
         {
            Seat nextInSeats = iterSeats.next();
            nextInSeats.setSeatCategory(null);
            iterSeats.remove();
            this.entityManager.merge(nextInSeats);
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
    * Support searching SeatCategory entities with pagination
    */

   private int page;
   private long count;
   private List<SeatCategory> pageItems;

   private SeatCategory example = new SeatCategory();

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

   public SeatCategory getExample()
   {
      return this.example;
   }

   public void setExample(SeatCategory example)
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
      Root<SeatCategory> root = countCriteria.from(SeatCategory.class);
      countCriteria = countCriteria.select(builder.count(root)).where(
            getSearchPredicates(root));
      this.count = this.entityManager.createQuery(countCriteria)
            .getSingleResult();

      // Populate this.pageItems

      CriteriaQuery<SeatCategory> criteria = builder.createQuery(SeatCategory.class);
      root = criteria.from(SeatCategory.class);
      TypedQuery<SeatCategory> query = this.entityManager.createQuery(criteria
            .select(root).where(getSearchPredicates(root)));
      query.setFirstResult(this.page * getPageSize()).setMaxResults(
            getPageSize());
      this.pageItems = query.getResultList();
   }

   private Predicate[] getSearchPredicates(Root<SeatCategory> root)
   {

      CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
      List<Predicate> predicatesList = new ArrayList<Predicate>();

      int seatCategoryId = this.example.getSeatCategoryId();
      if (seatCategoryId != 0)
      {
         predicatesList.add(builder.equal(root.get("seatCategoryId"), seatCategoryId));
      }
      String categoryName = this.example.getCategoryName();
      if (categoryName != null && !"".equals(categoryName))
      {
         predicatesList.add(builder.like(root.<String> get("categoryName"), '%' + categoryName + '%'));
      }

      return predicatesList.toArray(new Predicate[predicatesList.size()]);
   }

   public List<SeatCategory> getPageItems()
   {
      return this.pageItems;
   }

   public long getCount()
   {
      return this.count;
   }

   /*
    * Support listing and POSTing back SeatCategory entities (e.g. from inside an
    * HtmlSelectOneMenu)
    */

   public List<SeatCategory> getAll()
   {

      CriteriaQuery<SeatCategory> criteria = this.entityManager
            .getCriteriaBuilder().createQuery(SeatCategory.class);
      return this.entityManager.createQuery(
            criteria.select(criteria.from(SeatCategory.class))).getResultList();
   }

   @Resource
   private SessionContext sessionContext;

   public Converter getConverter()
   {

      final SeatCategoryBean ejbProxy = this.sessionContext.getBusinessObject(SeatCategoryBean.class);

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

            return String.valueOf(((SeatCategory) value).getSeatCategoryId());
         }
      };
   }

   /*
    * Support adding children to bidirectional, one-to-many tables
    */

   private SeatCategory add = new SeatCategory();

   public SeatCategory getAdd()
   {
      return this.add;
   }

   public SeatCategory getAdded()
   {
      SeatCategory added = this.add;
      this.add = new SeatCategory();
      return added;
   }
}