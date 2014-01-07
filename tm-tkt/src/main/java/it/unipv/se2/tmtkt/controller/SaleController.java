package it.unipv.se2.tmtkt.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateful;
import javax.enterprise.context.ConversationScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;

import it.unipv.se2.tmtkt.model.Sale;
import it.unipv.se2.tmtkt.model.User;

@Named
@Stateful
@ConversationScoped
public class SaleController implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String userName;
	private char saleType = 'S';
	
	@PersistenceContext(type = PersistenceContextType.EXTENDED)
	private EntityManager em;
	
	private int page;
	private long count;
	private List<Sale> pageItems;
	
	public void paginate() {
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) context
				.getExternalContext().getRequest();

		if (this.userName == null) {
			this.userName = request.getRemoteUser();
		}

		if (this.saleType != 'S' && this.saleType != 'T') {
			this.saleType = 'S';
		}

		CriteriaBuilder builder = this.em.getCriteriaBuilder();

		// Populate this.count

		CriteriaQuery<Long> countCriteria = builder.createQuery(Long.class);
		Root<Sale> root = countCriteria.from(Sale.class);
		countCriteria = countCriteria.select(builder.count(root)).where(
				getSearchPredicates(root));
		this.count = this.em.createQuery(countCriteria).getSingleResult();

		// Populate this.pageItems

		CriteriaQuery<Sale> criteria = builder.createQuery(Sale.class);
		root = criteria.from(Sale.class);
		TypedQuery<Sale> query = this.em.createQuery(criteria.select(root)
				.where(getSearchPredicates(root)));
		query.setFirstResult(this.page * getPageSize()).setMaxResults(
				getPageSize());
		this.pageItems = query.getResultList();
	}

	private Predicate[] getSearchPredicates(Root<Sale> root) {

		CriteriaBuilder builder = this.em.getCriteriaBuilder();
		List<Predicate> predicatesList = new ArrayList<Predicate>();

		User user = this.em.getReference(User.class, this.userName);
		if (user != null) {
			predicatesList.add(builder.equal(root.get("user"), user));
		}
		predicatesList.add(builder.equal(root.get("saleType"), this.saleType));

		return predicatesList.toArray(new Predicate[predicatesList.size()]);
	}
		
	public void search() {
		this.page = 0;
	}
	
	public int getPageSize() {
		return 10;
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
