package it.unipv.se2.tmtkt.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateful;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;

import it.unipv.se2.tmtkt.model.Sale;
import it.unipv.se2.tmtkt.view.SaleBean;

@Named
@Stateful
@RequestScoped
public class SaleController implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String userName;
	private char saleType;
	
	@Inject private SaleBean saleBean;
	
	@PersistenceContext(type = PersistenceContextType.TRANSACTION)
	private EntityManager em;
	
	private int page;
	private long count;
	private List<Sale> pageItems;

	public void paginate() {
		int removed = 0;
		pageItems = new ArrayList<Sale>();
		saleBean.paginate();
		List<Sale> saleBeanPageItems = saleBean.getPageItems();
		if (saleBeanPageItems != null && !saleBeanPageItems.isEmpty()) {
			for (Sale s : saleBeanPageItems) {
				String email = s.getUser().getEmail();
				if (email.equals(this.userName) &&
					s.getSaleType() == this.saleType)
					pageItems.add(s);
				else
					removed++;
			}
		}
		this.count = saleBean.getCount() - removed;
	}
		
	public void search() {
		saleBean.setPage(0);
		this.page = 0;
	}
	
	public int getPageSize() {
		return saleBean.getPageSize();
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}

	public List<Sale> getPageItems() {
		return pageItems;
	}

	public void setPageItems(List<Sale> pageItems) {
		this.pageItems = pageItems;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public char getSaleType() {
		return saleType;
	}

	public void setSaleType(char saleType) {
		this.saleType = saleType;
	}

}
