package it.unipv.se2.tmtkt.controller;

import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.*;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;

import it.unipv.se2.tmtkt.model.User;
import it.unipv.se2.tmtkt.view.UserBean;

@ManagedBean
@SessionScoped
public class LoginBean {
  private String username;
  private String password;
  
  @PersistenceContext(type = PersistenceContextType.EXTENDED)
  private EntityManager entityManager;
  
  public String login() {
      User user = this.entityManager.find(User.class, username);

	  if (user != null) {
		  if (user.getPassword().equals(password))
			  return("index");
	  }
	 FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Username or password incorrect"));
	 return("login");
  }

  public String logout() {
	  this.username = null;
	  return "index";
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
}
