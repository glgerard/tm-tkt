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

import it.unipv.se2.tmtkt.model.Row;
import it.unipv.se2.tmtkt.model.Seat;
import java.util.Iterator;

/**
 * Backing bean for Row entities.
 * <p>
 * This class provides CRUD functionality for all Row entities. It focuses
 * purely on Java EE 6 standards (e.g. <tt>&#64;ConversationScoped</tt> for
 * state management, <tt>PersistenceContext</tt> for persistence,
 * <tt>CriteriaBuilder</tt> for searches) rather than introducing a CRUD framework or
 * custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class RowBean implements Serializable
{

   private static final long serialVersionUID = 1L;

   /*
    * Support creating and retrieving Row entities
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

   private Row row;

   public Row getRow()
   {
      return this.row;
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
         this.row = this.example;
      }
      else
      {
         this.row = findById(getId());
      }
   }

   public Row findById(Integer id)
   {

      return this.entityManager.find(Row.class, id);
   }

   /*
    * Support updating and deleting Row entities
    */

   public String update()
   {
      this.conversation.end();

      try
      {
         if (this.id == null)
         {
            this.entityManager.persist(this.row);
            return "search?faces-redirect=true";
         }
         else
         {
            this.entityManager.merge(this.row);
            return "view?faces-redirect=true&id=" + this.row.getRowId();
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
         Row deletableEntity = findById(getId());
         Iterator<Seat> iterSeats = deletableEntity.getSeats().iterator();
         for (; iterSeats.hasNext();)
         {
            Seat nextInSeats = iterSeats.next();
            nextInSeats.setRow(null);
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
    * Support searching Row entities with pagination
    */

   private int page;
   private long count;
   private List<Row> pageItems;

   private Row example = new Row();

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

   public Row getExample()
   {
      return this.example;
   }

   public void setExample(Row example)
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
      Root<Row> root = countCriteria.from(Row.class);
      countCriteria = countCriteria.select(builder.count(root)).where(
            getSearchPredicates(root));
      this.count = this.entityManager.createQuery(countCriteria)
            .getSingleResult();

      // Populate this.pageItems

      CriteriaQuery<Row> criteria = builder.createQuery(Row.class);
      root = criteria.from(Row.class);
      TypedQuery<Row> query = this.entityManager.createQuery(criteria
            .select(root).where(getSearchPredicates(root)));
      query.setFirstResult(this.page * getPageSize()).setMaxResults(
            getPageSize());
      this.pageItems = query.getResultList();
   }

   private Predicate[] getSearchPredicates(Root<Row> root)
   {

      CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
      List<Predicate> predicatesList = new ArrayList<Predicate>();

      int rowId = this.example.getRowId();
      if (rowId != 0)
      {
         predicatesList.add(builder.equal(root.get("rowId"), rowId));
      }
      String rowName = this.example.getRowName();
      if (rowName != null && !"".equals(rowName))
      {
         predicatesList.add(builder.like(root.<String> get("rowName"), '%' + rowName + '%'));
      }

      return predicatesList.toArray(new Predicate[predicatesList.size()]);
   }

   public List<Row> getPageItems()
   {
      return this.pageItems;
   }

   public long getCount()
   {
      return this.count;
   }

   /*
    * Support listing and POSTing back Row entities (e.g. from inside an
    * HtmlSelectOneMenu)
    */

   public List<Row> getAll()
   {

      CriteriaQuery<Row> criteria = this.entityManager
            .getCriteriaBuilder().createQuery(Row.class);
      return this.entityManager.createQuery(
            criteria.select(criteria.from(Row.class))).getResultList();
   }

   @Resource
   private SessionContext sessionContext;

   public Converter getConverter()
   {

      final RowBean ejbProxy = this.sessionContext.getBusinessObject(RowBean.class);

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

            return String.valueOf(((Row) value).getRowId());
         }
      };
   }

   /*
    * Support adding children to bidirectional, one-to-many tables
    */

   private Row add = new Row();

   public Row getAdd()
   {
      return this.add;
   }

   public Row getAdded()
   {
      Row added = this.add;
      this.add = new Row();
      return added;
   }
}