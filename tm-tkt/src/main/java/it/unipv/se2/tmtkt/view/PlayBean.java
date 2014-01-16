package it.unipv.se2.tmtkt.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
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

import it.unipv.se2.tmtkt.model.Play;
import it.unipv.se2.tmtkt.model.Event;
import it.unipv.se2.tmtkt.model.Genre;

import java.util.Iterator;

/**
 * Backing bean for Play entities.
 * <p>
 * This class provides CRUD functionality for all Play entities. It focuses
 * purely on Java EE 6 standards (e.g. <tt>&#64;ConversationScoped</tt> for
 * state management, <tt>PersistenceContext</tt> for persistence,
 * <tt>CriteriaBuilder</tt> for searches) rather than introducing a CRUD framework or
 * custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class PlayBean implements Serializable
{

   private static final long serialVersionUID = 1L;

   /*
    * Support creating and retrieving Play entities
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

   private Play Play;

   public Play getPlay()
   {
      return this.Play;
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
         this.Play = this.example;
      }
      else
      {
         this.Play = findById(getId());
      }
   }

   public Play findById(Short id)
   {

      return this.entityManager.find(Play.class, id);
   }

   /*
    * Support updating and deleting Play entities
    */

   public String update()
   {
      this.conversation.end();

      try
      {
         if (this.id == null)
         {
            this.entityManager.persist(this.Play);
            return "search?faces-redirect=true";
         }
         else
         {
            this.entityManager.merge(this.Play);
            return "view?faces-redirect=true&id=" + this.Play.getShowId();
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
         Play deletableEntity = findById(getId());
         Genre genre = deletableEntity.getGenre();
         genre.getPlays().remove(deletableEntity);
         deletableEntity.setGenre(null);
         this.entityManager.merge(genre);
         Iterator<Event> iterEvents = deletableEntity.getEvents().iterator();
         for (; iterEvents.hasNext();)
         {
            Event nextInEvents = iterEvents.next();
            nextInEvents.setPlay(null);
            iterEvents.remove();
            this.entityManager.merge(nextInEvents);
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
    * Support searching Play entities with pagination
    */

   private int page;
   private long count;
   private List<Play> pageItems;

   private Play example = new Play();

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

   public Play getExample()
   {
      return this.example;
   }

   public void setExample(Play example)
   {
      this.example = example;
   }

   public void search()
   {
      this.page = 0;
   }
   
   private Short genreId;

   public void paginate()
   {

	  if (genreId != null ) {
		  this.example.setGenre(this.entityManager.find(Genre.class, genreId));
	  }
	  
      CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();

      // Populate this.count

      CriteriaQuery<Long> countCriteria = builder.createQuery(Long.class);
      Root<Play> root = countCriteria.from(Play.class);
      countCriteria = countCriteria.select(builder.count(root)).where(
            getSearchPredicates(root));
      this.count = this.entityManager.createQuery(countCriteria)
            .getSingleResult();

      // Populate this.pageItems

      CriteriaQuery<Play> criteria = builder.createQuery(Play.class);
      root = criteria.from(Play.class);
      TypedQuery<Play> query = this.entityManager.createQuery(criteria
            .select(root).where(getSearchPredicates(root)));
      query.setFirstResult(this.page * getPageSize()).setMaxResults(
            getPageSize());
      this.pageItems = query.getResultList();
   }

   private Predicate[] getSearchPredicates(Root<Play> root)
   {

      CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
      List<Predicate> predicatesList = new ArrayList<Predicate>();

      Genre genre = this.example.getGenre();
      if (genre != null)
      {
         predicatesList.add(builder.equal(root.get("genre"), genre));
      }
      String title = this.example.getTitle();
      if (title != null && !"".equals(title))
      {
         predicatesList.add(builder.like(root.<String> get("title"), '%' + title + '%'));
      }
      String authorName = this.example.getAuthorName();
      if (authorName != null && !"".equals(authorName))
      {
         predicatesList.add(builder.like(root.<String> get("authorName"), '%' + authorName + '%'));
      }
      String directorName = this.example.getDirectorName();
      if (directorName != null && !"".equals(directorName))
      {
         predicatesList.add(builder.like(root.<String> get("directorName"), '%' + directorName + '%'));
      }
      String description = this.example.getDescription();
      if (description != null && !"".equals(description))
      {
         predicatesList.add(builder.like(root.<String> get("description"), '%' + description + '%'));
      }
      Date firstEventDate = this.example.getFirstEventDate();
      if ( firstEventDate != null )
      {
    	  predicatesList.add(builder.greaterThanOrEqualTo(root.<Date> get("lastEventDate"), firstEventDate));
      }
      Date lastEventDate = this.example.getLastEventDate();
      if ( lastEventDate != null )
      {
    	  predicatesList.add(builder.lessThanOrEqualTo(root.<Date> get("firstEventDate"), lastEventDate));
      }
      return predicatesList.toArray(new Predicate[predicatesList.size()]);
   }

   public List<Play> getPageItems()
   {
      return this.pageItems;
   }

   public long getCount()
   {
      return this.count;
   }

   /*
    * Support listing and POSTing back Play entities (e.g. from inside an
    * HtmlSelectOneMenu)
    */

   public List<Play> getAll()
   {

      CriteriaQuery<Play> criteria = this.entityManager
            .getCriteriaBuilder().createQuery(Play.class);
      return this.entityManager.createQuery(
            criteria.select(criteria.from(Play.class))).getResultList();
   }

   @Resource
   private SessionContext sessionContext;

   public Converter getConverter()
   {

      final PlayBean ejbProxy = this.sessionContext.getBusinessObject(PlayBean.class);

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

            return String.valueOf(((Play) value).getShowId());
         }
      };
   }

   /*
    * Support adding children to bidirectional, one-to-many tables
    */

   private Play add = new Play();

   public Play getAdd()
   {
      return this.add;
   }

   public Play getAdded()
   {
      Play added = this.add;
      this.add = new Play();
      return added;
   }

public Short getGenreId() {
	return genreId;
}

public void setGenreId(Short genreId) {
	this.genreId = genreId;
}
}