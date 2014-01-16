package it.unipv.se2.tmtkt.controllertest;

import java.io.File;

import it.unipv.se2.tmtkt.controller.PriceSchemeController;
import it.unipv.se2.tmtkt.controller.SubscriptionController;
import it.unipv.se2.tmtkt.model.Event;
import it.unipv.se2.tmtkt.model.PriceScheme;
import it.unipv.se2.tmtkt.model.Seat;
import it.unipv.se2.tmtkt.model.Subscription;
import it.unipv.se2.tmtkt.model.User;
import it.unipv.se2.tmtkt.modeltest.GenericModelTest;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;

import org.junit.Test;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.persistence.UsingDataSet;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.mockito.Mockito;

@RunWith(Arquillian.class)
public class PriceSchemeControllerTest {

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
    
    @PersistenceContext  
    EntityManager em;  
    
    @Resource
    UserTransaction utx;
    
    @Inject private PriceSchemeController priceSchemeController;
    
    @Test
    @UsingDataSet({"datasets/genre.json","datasets/play.json","datasets/event.json",
    	"datasets/paymentMethod.json", "datasets/user.json", "datasets/userCategory.json",
    	"datasets/subscriptionType.json", "datasets/sector.json", "datasets/row.json",
    	"datasets/seatCategory.json", "datasets/seat.json"})
    public void shouldReturnPriceScheme() {
    	Event event = this.em.find(Event.class, (short)1);
    	Seat seat = this.em.find(Seat.class, (short)1);
    	User user = this.em.find(User.class, "steffen_hermann@gmx.de");
    	
    	PriceScheme priceScheme = priceSchemeController.ticketPrice(event, seat, user);
    	
        Assert.assertTrue( priceScheme.getPrice() > 0);
    }
    
}
