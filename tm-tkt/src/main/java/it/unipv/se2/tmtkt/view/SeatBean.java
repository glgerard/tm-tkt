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

import it.unipv.se2.tmtkt.model.Seat;
import it.unipv.se2.tmtkt.model.Booking;
import it.unipv.se2.tmtkt.model.Row;
import it.unipv.se2.tmtkt.model.SeatCategory;
import it.unipv.se2.tmtkt.model.Sector;
import it.unipv.se2.tmtkt.model.Subscription;
import java.util.Iterator;

/**
 * Backing bean for Seat entities.
 * <p>
 * This class provides CRUD functionality for all Seat entities. It focuses
 * purely on Java EE 6 standards (e.g. <tt>&#64;ConversationScoped</tt> for
 * state management, <tt>PersistenceContext</tt> for persistence,
 * <tt>CriteriaBuilder</tt> for searches) rather than introducing a CRUD framework or
 * custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class SeatBean implements Serializable
{

   private static final long serialVersionUID = 1L;

   /*
    * Support creating and retrieving Seat entities
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

   private Seat seat;

   public Seat getSeat()
   {
      return this.seat;
   }

   @Inject
   private Conversation conversation;

   @PersistenceContext(type = PersistenceContextType.EXTENDED)
protected EntityManager entityManager;

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
         this.seat = this.example;
      }
      else
      {
         this.seat = findById(getId());
      }
   }

   public Seat findById(Short id)
   {

      return this.entityManager.find(Seat.class, id);
   }

   /*
    * Support updating and deleting Seat entities
    */

   public String update()
   {
      this.conversation.end();

      try
      {
         if (this.id == null)
         {
            this.entityManager.persist(this.seat);
            return "search?faces-redirect=true";
         }
         else
         {
            this.entityManager.merge(this.seat);
            return "view?faces-redirect=true&id=" + this.seat.getSeatId();
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
         Seat deletableEntity = findById(getId());
         SeatCategory seatCategory = deletableEntity.getSeatCategory();
         seatCategory.getSeats().remove(deletableEntity);
         deletableEntity.setSeatCategory(null);
         this.entityManager.merge(seatCategory);
         Sector sector = deletableEntity.getSector();
         sector.getSeats().remove(deletableEntity);
         deletableEntity.setSector(null);
         this.entityManager.merge(sector);
         Row row = deletableEntity.getRow();
         row.getSeats().remove(deletableEntity);
         deletableEntity.setRow(null);
         this.entityManager.merge(row);
         Iterator<Subscription> iterSubscriptions = deletableEntity.getSubscriptions().iterator();
         for (; iterSubscriptions.hasNext();)
         {
            Subscription nextInSubscriptions = iterSubscriptions.next();
            nextInSubscriptions.setSeat(null);
            iterSubscriptions.remove();
            this.entityManager.merge(nextInSubscriptions);
         }
         Iterator<Booking> iterBookings = deletableEntity.getBookings().iterator();
         for (; iterBookings.hasNext();)
         {
            Booking nextInBookings = iterBookings.next();
            nextInBookings.setSeat(null);
            iterBookings.remove();
            this.entityManager.merge(nextInBookings);
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
    * Support searching Seat entities with pagination
    */

   private int page;
   private long count;
   private List<Seat> pageItems;

   private Seat example = new Seat();

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

   public Seat getExample()
   {
      return this.example;
   }

   public void setExample(Seat example)
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
      Root<Seat> root = countCriteria.from(Seat.class);
      countCriteria = countCriteria.select(builder.count(root)).where(
            getSearchPredicates(root));
      this.count = this.entityManager.createQuery(countCriteria)
            .getSingleResult();

      // Populate this.pageItems

      CriteriaQuery<Seat> criteria = builder.createQuery(Seat.class);
      root = criteria.from(Seat.class);
      TypedQuery<Seat> query = this.entityManager.createQuery(criteria
            .select(root).where(getSearchPredicates(root)));
      query.setFirstResult(this.page * getPageSize()).setMaxResults(
            getPageSize());
      this.pageItems = query.getResultList();
   }

   private Predicate[] getSearchPredicates(Root<Seat> root)
   {

      CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
      List<Predicate> predicatesList = new ArrayList<Predicate>();

      short seatId = this.example.getSeatId();
      if (seatId != 0)
      {
         predicatesList.add(builder.equal(root.get("seatId"), seatId));
      }
      SeatCategory seatCategory = this.example.getSeatCategory();
      if (seatCategory != null)
      {
         predicatesList.add(builder.equal(root.get("seatCategory"), seatCategory));
      }
      Sector sector = this.example.getSector();
      if (sector != null)
      {
         predicatesList.add(builder.equal(root.get("sector"), sector));
      }
      Row row = this.example.getRow();
      if (row != null)
      {
         predicatesList.add(builder.equal(root.get("row"), row));
      }

      return predicatesList.toArray(new Predicate[predicatesList.size()]);
   }

   public List<Seat> getPageItems()
   {
      return this.pageItems;
   }

   public long getCount()
   {
      return this.count;
   }

   /*
    * Support listing and POSTing back Seat entities (e.g. from inside an
    * HtmlSelectOneMenu)
    */

   public List<Seat> getAll()
   {

      CriteriaQuery<Seat> criteria = this.entityManager
            .getCriteriaBuilder().createQuery(Seat.class);
      return this.entityManager.createQuery(
            criteria.select(criteria.from(Seat.class))).getResultList();
   }

   @Resource
   private SessionContext sessionContext;

   public Converter getConverter()
   {

      final SeatBean ejbProxy = this.sessionContext.getBusinessObject(SeatBean.class);

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

            return String.valueOf(((Seat) value).getSeatId());
         }
      };
   }

   /*
    * Support adding children to bidirectional, one-to-many tables
    */

   private Seat add = new Seat();

   public Seat getAdd()
   {
      return this.add;
   }

   public Seat getAdded()
   {
      Seat added = this.add;
      this.add = new Seat();
      return added;
   }
}