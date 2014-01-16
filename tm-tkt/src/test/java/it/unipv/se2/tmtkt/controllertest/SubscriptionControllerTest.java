package it.unipv.se2.tmtkt.controllertest;

import java.io.File;

import it.unipv.se2.tmtkt.controller.SubscriptionController;
import it.unipv.se2.tmtkt.model.PaymentMethod;
import it.unipv.se2.tmtkt.model.Subscription;
import it.unipv.se2.tmtkt.modeltest.GenericModelTest;

import javax.annotation.Resource;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.UserTransaction;

import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.arquillian.persistence.UsingDataSet;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.mockito.Mockito;

@RunWith(Arquillian.class)
public class SubscriptionControllerTest {

    @Deployment
    public static Archive<?> createDeployment() {    
    	 File mockito = Maven.resolver().resolve("org.mockito:mockito-all:1.9.5")
    			   .withoutTransitivity().asSingle(File.class);
    	
        return ShrinkWrap.create(WebArchive.class, "test.war")
        	.addClass(GenericModelTest.class)
        	.addClass(FacesContextMoker.class)
        	.addClasses(FacesContextMoker.class.getClasses())
            .addPackage(SubscriptionController.class.getPackage())
            .addPackage(Subscription.class.getPackage())
            .addAsLibraries(mockito)  
            .addAsResource("test-persistence.xml", "META-INF/persistence.xml")
            .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
            .addAsWebInfResource("test-mysql-ds.xml");
    }
    
    @Inject SubscriptionController subscriptionController;

    @PersistenceContext  
    EntityManager em;  
    
    @Resource
    UserTransaction utx;
    
    @Before
    public void preparePersistenceTest() throws Exception {
    	FacesContextMoker.mockFacesContext();
    	   
        ExternalContext externalContext = Mockito.mock(ExternalContext.class);
        Mockito.when(FacesContext.getCurrentInstance().getExternalContext())
                .thenReturn(externalContext);
        
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(
                FacesContext.getCurrentInstance().getExternalContext()
                        .getRequest()).thenReturn(request);
        
        Mockito.when(((HttpServletRequest)FacesContext.getCurrentInstance().
        		getExternalContext().getRequest()).getRemoteUser()).thenReturn("steffen_hermann@gmx.de");
    }
    
    @Test @InSequence(1)
    @UsingDataSet({"datasets/genre.json","datasets/play.json","datasets/event.json"})
    public void shouldReturnSuscriptionType() {
    	subscriptionController.setSubscriptionTypeId((short) 1);
    	subscriptionController.setGenreId((short) 4);
        Assert.assertEquals("assignedSeat",
            subscriptionController.subscriptionTypeName());
    }
    
    @Test @InSequence(2)
    @UsingDataSet({"datasets/genre.json","datasets/play.json","datasets/event.json",
    	"datasets/paymentMethod.json", "datasets/user.json", "datasets/userCategory.json",
    	"datasets/subscriptionType.json", "datasets/sector.json", "datasets/row.json",
    	"datasets/seatCategory.json", "datasets/seat.json"})
    public void shouldCreateSubscription() {
    	subscriptionController.setSubscriptionTypeId((short) 1);
    	subscriptionController.setGenreId((short) 4);
    	subscriptionController.setSeatId((short)10);
    	PaymentMethod paymentMethod = em.find(PaymentMethod.class, (short)2);
    	subscriptionController.setPaymentMethod(paymentMethod);
    	Assert.assertEquals("success", subscriptionController.buy());
    	Assert.assertEquals(1,em.createQuery(  // check that a booking has been created
    			"SELECT b FROM Booking b WHERE b.seat.seatId = 10")
    			.getResultList().size());
    	Assert.assertEquals(1,em.createQuery(  // check for a sale
    			"SELECT s FROM Sale s WHERE s.saleType = 'S'")
    			.getResultList().size());
    	Assert.assertEquals(1,em.createQuery( // and a subscription
    			"SELECT s, b FROM Sale s, Subscription b WHERE s.saleId = b.subscriptionId")
    			.getResultList().size());
    	Assert.assertEquals(1,em.createQuery( // check that the seat has been associated with the genre
    			"SELECT s FROM Seat s JOIN s.genres g WHERE g.genreId = 4")
    			.getResultList().size());
    }
}
