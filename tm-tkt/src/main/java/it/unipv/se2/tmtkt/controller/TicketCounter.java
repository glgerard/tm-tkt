package it.unipv.se2.tmtkt.controller;

import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.Query;

@Singleton
public class TicketCounter {

	private int count = 0;

	@PersistenceContext(type = PersistenceContextType.TRANSACTION)
	private EntityManager em;
	
	@Lock(LockType.READ)
	public int getCount() {

		return count;		
	}
	
	@Lock(LockType.WRITE)
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public int incrementCount() {
		if (count == 0) {
			Object queryResult = null;
			Query q = this.em
						.createQuery("select max(t.ticketId) from Ticket t");
			try {
				queryResult = q.getSingleResult();
			} catch (NoResultException e) {
				count = 0;
			}
			if (queryResult != null )
				count = (Integer)queryResult;
		}
		count++;
		return count;
	}
}
