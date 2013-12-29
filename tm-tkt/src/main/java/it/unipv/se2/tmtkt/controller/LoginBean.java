package it.unipv.se2.tmtkt.controller;

import javax.ejb.Stateful;
import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;

import it.unipv.se2.tmtkt.model.User;
import it.unipv.se2.tmtkt.model.UserCategory;

@Named
@Stateful
@SessionScoped
public class LoginBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final byte USER_ADMIN_ID = 0;

	private String username;
	private String password;
	private boolean privileged = false;
	private User user;
	
	private String url;
	
	@PersistenceContext(type=PersistenceContextType.EXTENDED)
	private EntityManager em;

	public String login() {
		if (username != null ) {
			user = em.find(User.class, username);

			if (user != null) {
				if (user.getPassword().equals(password)) {
					String admin = em.find(UserCategory.class, (byte)USER_ADMIN_ID).getDescription();
					privileged = user.getUserCategory().getDescription() == admin;
					return(this.url+"?faces-redirect=true");
				}
			}
		}
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Username or password incorrect"));
		return(null);
	}

	public String logout() {
		this.username = null;
		this.privileged = false;
		return this.url;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	/*
	 * Support basic authorization functions
	 */

	public boolean isPrivileged() {
		return privileged;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
