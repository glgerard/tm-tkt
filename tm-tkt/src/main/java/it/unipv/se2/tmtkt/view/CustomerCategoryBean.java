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

import it.unipv.se2.tmtkt.model.CustomerCategory;
import it.unipv.se2.tmtkt.model.Customer;
import java.util.Iterator;

/**
 * Backing bean for CustomerCategory entities.
 * <p>
 * This class provides CRUD functionality for all CustomerCategory entities. It focuses
 * purely on Java EE 6 standards (e.g. <tt>&#64;ConversationScoped</tt> for
 * state management, <tt>PersistenceContext</tt> for persistence,
 * <tt>CriteriaBuilder</tt> for searches) rather than introducing a CRUD framework or
 * custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class CustomerCategoryBean implements Serializable
{

   private static final long serialVersionUID = 1L;

   /*
    * Support creating and retrieving CustomerCategory entities
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

   private CustomerCategory customerCategory;

   public CustomerCategory getCustomerCategory()
   {
      return this.customerCategory;
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
         this.customerCategory = this.example;
      }
      else
      {
         this.customerCategory = findById(getId());
      }
   }

   public CustomerCategory findById(Byte id)
   {

      return this.entityManager.find(CustomerCategory.class, id);
   }

   /*
    * Support updating and deleting CustomerCategory entities
    */

   public String update()
   {
      this.conversation.end();

      try
      {
         if (this.id == null)
         {
            this.entityManager.persist(this.customerCategory);
            return "search?faces-redirect=true";
         }
         else
         {
            this.entityManager.merge(this.customerCategory);
            return "view?faces-redirect=true&id=" + this.customerCategory.getCustomerCategoryId();
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
         CustomerCategory deletableEntity = findById(getId());
         Iterator<Customer> iterCustomers = deletableEntity.getCustomers().iterator();
         for (; iterCustomers.hasNext();)
         {
            Customer nextInCustomers = iterCustomers.next();
            nextInCustomers.setCustomerCategory(null);
            iterCustomers.remove();
            this.entityManager.merge(nextInCustomers);
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
    * Support searching CustomerCategory entities with pagination
    */

   private int page;
   private long count;
   private List<CustomerCategory> pageItems;

   private CustomerCategory example = new CustomerCategory();

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

   public CustomerCategory getExample()
   {
      return this.example;
   }

   public void setExample(CustomerCategory example)
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
      Root<CustomerCategory> root = countCriteria.from(CustomerCategory.class);
      countCriteria = countCriteria.select(builder.count(root)).where(
            getSearchPredicates(root));
      this.count = this.entityManager.createQuery(countCriteria)
            .getSingleResult();

      // Populate this.pageItems

      CriteriaQuery<CustomerCategory> criteria = builder.createQuery(CustomerCategory.class);
      root = criteria.from(CustomerCategory.class);
      TypedQuery<CustomerCategory> query = this.entityManager.createQuery(criteria
            .select(root).where(getSearchPredicates(root)));
      query.setFirstResult(this.page * getPageSize()).setMaxResults(
            getPageSize());
      this.pageItems = query.getResultList();
   }

   private Predicate[] getSearchPredicates(Root<CustomerCategory> root)
   {

      CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
      List<Predicate> predicatesList = new ArrayList<Predicate>();

      byte customerCategoryId = this.example.getCustomerCategoryId();
      if (customerCategoryId != 0)
      {
         predicatesList.add(builder.equal(root.get("customerCategoryId"), customerCategoryId));
      }
      String description = this.example.getDescription();
      if (description != null && !"".equals(description))
      {
         predicatesList.add(builder.like(root.<String> get("description"), '%' + description + '%'));
      }

      return predicatesList.toArray(new Predicate[predicatesList.size()]);
   }

   public List<CustomerCategory> getPageItems()
   {
      return this.pageItems;
   }

   public long getCount()
   {
      return this.count;
   }

   /*
    * Support listing and POSTing back CustomerCategory entities (e.g. from inside an
    * HtmlSelectOneMenu)
    */

   public List<CustomerCategory> getAll()
   {

      CriteriaQuery<CustomerCategory> criteria = this.entityManager
            .getCriteriaBuilder().createQuery(CustomerCategory.class);
      return this.entityManager.createQuery(
            criteria.select(criteria.from(CustomerCategory.class))).getResultList();
   }

   @Resource
   private SessionContext sessionContext;

   public Converter getConverter()
   {

      final CustomerCategoryBean ejbProxy = this.sessionContext.getBusinessObject(CustomerCategoryBean.class);

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

            return String.valueOf(((CustomerCategory) value).getCustomerCategoryId());
         }
      };
   }

   /*
    * Support adding children to bidirectional, one-to-many tables
    */

   private CustomerCategory add = new CustomerCategory();

   public CustomerCategory getAdd()
   {
      return this.add;
   }

   public CustomerCategory getAdded()
   {
      CustomerCategory added = this.add;
      this.add = new CustomerCategory();
      return added;
   }
}