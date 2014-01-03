package it.unipv.se2.tmtkt.controller;

import java.util.Calendar;

import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;

@Singleton
public class TransactionTestImpl {	
	@Lock(LockType.READ)
	public long getTransaction() {
		return Calendar.getInstance().getTimeInMillis();		
	}

}
