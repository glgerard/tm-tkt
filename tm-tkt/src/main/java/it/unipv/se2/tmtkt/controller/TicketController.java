package it.unipv.se2.tmtkt.controller;

import javax.faces.bean.*;

/**
 * Session Bean implementation class Ticket
 */
@ManagedBean
public class TicketController {

	private int qty;
	
    /**
     * Default constructor. 
     */
    public TicketController() {
        // TODO Auto-generated constructor stub
    }

	public int getQty() {
		return qty;
	}

	public void setQty(int qty) {
		this.qty = qty;
	}

	public String purchase() {
		return("completed");
	}
}
