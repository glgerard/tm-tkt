package it.unipv.se2.tmtkt.controller;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import javax.ejb.Stateful;
import javax.enterprise.context.RequestScoped;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;

import it.unipv.se2.tmtkt.model.Row;
import it.unipv.se2.tmtkt.view.RowBean;

@Named
@Stateful
@RequestScoped
public class RowController implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Inject private RowBean bean;
	
	private List<SelectItem> items = new LinkedList<SelectItem>();

	public List<SelectItem> getItems() {
		if (items.isEmpty())
			for (Row r : bean.getAll()) {
				items.add(new SelectItem(r, r.getRowName()));
 			}
		return items;
	}

}
