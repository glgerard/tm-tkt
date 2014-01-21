package it.unipv.se2.tmtkt.controller;

import it.unipv.se2.tmtkt.model.Sale;
import it.unipv.se2.tmtkt.model.User;
import it.unipv.se2.tmtkt.model.UserCategory;
import it.unipv.se2.tmtkt.model.UserRoles;
import it.unipv.se2.tmtkt.model.UserRolesId;

import javax.ejb.Stateful;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.faces.context.FacesContext;

import java.io.Serializable;

import javax.inject.Inject;
import javax.inject.Named;

import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.application.FacesMessage;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

@Named
@Stateful
@ConversationScoped
public class LoginBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String id;
	private User user;
	private UserRoles userRoles;

	private User example = new User();
	private UserRoles exampleRoles = new UserRoles();

	@Inject
	private Conversation conversation;

	@PersistenceContext(type = PersistenceContextType.EXTENDED)
	private EntityManager entityManager;

	public String create() {

		this.conversation.begin();
		return "create?faces-redirect=true";
	}

	public void retrieve() {

		if (FacesContext.getCurrentInstance().isPostback()) {
			return;
		}

		if (this.conversation.isTransient()) {
			this.conversation.begin();
		}

		if (this.id == null) {
			this.user = this.example;
			this.userRoles = this.exampleRoles;
		} else {
			this.user = findById(getId());
			this.userRoles = findCustomerRoleById(getId());
		}
	}

	public User findById(String id) {
		return this.entityManager.find(User.class, id);
	}
	
	public UserRoles findCustomerRoleById(String id) {
		UserRolesId userRolesId = new UserRolesId(id, "Customer");
		return this.entityManager.find(UserRoles.class, userRolesId);
	}

	/*
	 * Support updating and deleting User entities
	 */

	public String update() {
		this.conversation.end();

		try {
			if (this.id == null) {
				this.entityManager.persist(this.user);
				this.userRoles.setId(new UserRolesId(this.user.getEmail(),"Customer"));
				this.entityManager.persist(this.userRoles);
				return "login";
			} else {
				this.entityManager.merge(this.user);
				this.userRoles.setId(new UserRolesId(this.user.getEmail(),"Customer"));
				this.entityManager.merge(this.userRoles);
				return "view?faces-redirect=true&id=" + this.user.getEmail();
			}
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(e.getMessage()));
			return null;
		}
	}

	public String delete() {
		this.conversation.end();

		try {
			User deletableEntity = findById(getId());
			UserCategory userCategory = deletableEntity.getUserCategory();
			userCategory.getUsers().remove(deletableEntity);
			deletableEntity.setUserCategory(null);
			this.entityManager.merge(userCategory);
			Iterator<Sale> iterSales = deletableEntity.getSales().iterator();
			for (; iterSales.hasNext();) {
				Sale nextInSales = iterSales.next();
				nextInSales.setUser(null);
				iterSales.remove();
				this.entityManager.merge(nextInSales);
			}
			this.entityManager.remove(deletableEntity);
			this.entityManager.flush();
			return "search?faces-redirect=true";
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(e.getMessage()));
			return null;
		}
	}

	public String register() {
		this.conversation.end();

		try {
			if (this.id == null) {
				this.entityManager.persist(this.user);
				return "index?faces-redirect=true";
			} else {
				this.entityManager.merge(this.user);
				return "view?faces-redirect=true&id=" + this.user.getEmail();
			}
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(e.getMessage()));
			return null;
		}
	}

	public String logout() {
		String result = "/login?faces-redirect=true";

		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) context
				.getExternalContext().getRequest();
		try {
			request.logout();
		} catch (ServletException ex) {
			Logger.getLogger(LoginBean.class.getName()).log(Level.SEVERE, null,
					ex);
			result = "/loginError?faces-redirect=true";
		}
		return result;
	}

	/*
	 * Support basic authorization functions
	 */

	public boolean isPrivileged() {
		FacesContext context = FacesContext.getCurrentInstance();
		Object request = context.getExternalContext().getRequest();
		boolean result = false;
		if (request instanceof HttpServletRequest) {
			result = ((HttpServletRequest) request).isUserInRole("Admin");
		}
		return result;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public UserRoles getUserRoles() {
		return userRoles;
	}

	public void setUserRoles(UserRoles userRoles) {
		this.userRoles = userRoles;
	}

	public User getExample() {
		return this.example;
	}

	public void setExample(User example) {
		this.example = example;
	}

}
