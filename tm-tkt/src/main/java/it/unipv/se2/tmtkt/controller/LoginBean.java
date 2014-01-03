package it.unipv.se2.tmtkt.controller;

import javax.faces.context.FacesContext;

import java.io.Serializable;

import javax.inject.Named;

import java.util.logging.Level;

import java.util.logging.Logger;

import javax.faces.bean.RequestScoped;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

@Named
@RequestScoped
public class LoginBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String url;

	  public String logout() {
	    String result = "/login?faces-redirect=true";

	    FacesContext context = FacesContext.getCurrentInstance();
	    HttpServletRequest request = (HttpServletRequest) context.
	        getExternalContext().getRequest();
	    try {
	      request.logout( );
	    } catch (ServletException ex) {
	      Logger.getLogger(LoginBean.class.getName()).
	           log(Level.SEVERE, null, ex);
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
		    result = ((HttpServletRequest)request).isUserInRole("Admin");
		  }
		  return result;
		}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
