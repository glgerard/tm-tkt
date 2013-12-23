package it.unipv.se2.tmtkt.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
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
import javax.faces.model.SelectItem;
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

import it.unipv.se2.tmtkt.model.UserCategory;
import it.unipv.se2.tmtkt.model.User;

import java.util.Iterator;

/**
 * Backing bean for UserCategory entities.
 * <p>
 * This class provides CRUD functionality for all UserCategory entities. It focuses
 * purely on Java EE 6 standards (e.g. <tt>&#64;ConversationScoped</tt> for
 * state management, <tt>PersistenceContext</tt> for persistence,
 * <tt>CriteriaBuilder</tt> for searches) rather than introducing a CRUD framework or
 * custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class UserCategoryBean implements Serializable
{

   private static final long serialVersionUID = 1L;

   /*
    * Support creating and retrieving UserCategory entities
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

   private UserCategory userCategory;

   public UserCategory getUserCategory()
   {
      return this.userCategory;
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
         this.userCategory = this.example;
      }
      else
      {
         this.userCategory = findById(getId());
      }
   }

   public UserCategory findById(Byte id)
   {

      return this.entityManager.find(UserCategory.class, id);
   }

   /*
    * Support updating and deleting UserCategory entities
    */

   public String update()
   {
      this.conversation.end();

      try
      {
         if (this.id == null)
         {
            this.entityManager.persist(this.userCategory);
            return "search?faces-redirect=true";
         }
         else
         {
            this.entityManager.merge(this.userCategory);
            return "view?faces-redirect=true&id=" + this.userCategory.getUserCategoryId();
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
         UserCategory deletableEntity = findById(getId());
         Iterator<User> iterUsers = deletableEntity.getUsers().iterator();
         for (; iterUsers.hasNext();)
         {
            User nextInUsers = iterUsers.next();
            nextInUsers.setUserCategory(null);
            iterUsers.remove();
            this.entityManager.merge(nextInUsers);
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
    * Support searching UserCategory entities with pagination
    */

   private int page;
   private long count;
   private List<UserCategory> pageItems;

   private UserCategory example = new UserCategory();

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

   public UserCategory getExample()
   {
      return this.example;
   }

   public void setExample(UserCategory example)
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
      Root<UserCategory> root = countCriteria.from(UserCategory.class);
      countCriteria = countCriteria.select(builder.count(root)).where(
            getSearchPredicates(root));
      this.count = this.entityManager.createQuery(countCriteria)
            .getSingleResult();

      // Populate this.pageItems

      CriteriaQuery<UserCategory> criteria = builder.createQuery(UserCategory.class);
      root = criteria.from(UserCategory.class);
      TypedQuery<UserCategory> query = this.entityManager.createQuery(criteria
            .select(root).where(getSearchPredicates(root)));
      query.setFirstResult(this.page * getPageSize()).setMaxResults(
            getPageSize());
      this.pageItems = query.getResultList();
   }

   private Predicate[] getSearchPredicates(Root<UserCategory> root)
   {

      CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
      List<Predicate> predicatesList = new ArrayList<Predicate>();

      byte userCategoryId = this.example.getUserCategoryId();
      if (userCategoryId != 0)
      {
         predicatesList.add(builder.equal(root.get("userCategoryId"), userCategoryId));
      }
      String description = this.example.getDescription();
      if (description != null && !"".equals(description))
      {
         predicatesList.add(builder.like(root.<String> get("description"), '%' + description + '%'));
      }

      return predicatesList.toArray(new Predicate[predicatesList.size()]);
   }

   public List<UserCategory> getPageItems()
   {
      return this.pageItems;
   }

   public long getCount()
   {
      return this.count;
   }

   /*
    * Support listing and POSTing back UserCategory entities (e.g. from inside an
    * HtmlSelectOneMenu)
    */

   public List<UserCategory> getAll()
   {

      CriteriaQuery<UserCategory> criteria = this.entityManager
            .getCriteriaBuilder().createQuery(UserCategory.class);
      return this.entityManager.createQuery(
            criteria.select(criteria.from(UserCategory.class))).getResultList();
   }
   
   private List<SelectItem> userCategoryItems = new LinkedList<SelectItem>();
   
   public List<SelectItem> getUserCategoryItems()
   {
	   for (UserCategory uc : this.getAll()) {
		   userCategoryItems.add(new SelectItem(uc, uc.getDescription()));
	   }
	   return userCategoryItems;
   }

   @Resource
   private SessionContext sessionContext;

   public Converter getConverter()
   {

      final UserCategoryBean ejbProxy = this.sessionContext.getBusinessObject(UserCategoryBean.class);

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

            return String.valueOf(((UserCategory) value).getUserCategoryId());
         }
      };
   }

   /*
    * Support adding children to bidirectional, one-to-many tables
    */

   private UserCategory add = new UserCategory();

   public UserCategory getAdd()
   {
      return this.add;
   }

   public UserCategory getAdded()
   {
      UserCategory added = this.add;
      this.add = new UserCategory();
      return added;
   }
}