/**
 *
 */
package com.giorgiogamberini.parallelism.webclient.beans;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.enterprise.event.Observes;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.giorgiogamberini.parallelism.webclient.mdb.beans.Process3;

/**
 * @author giorgio [Giorgio Gamberini - work@giorgiogamberini.com] 30/dic/2014
 *
 */
@ManagedBean
@SessionScoped
@Stateless
public class Process3Bean implements Serializable {

	private static final long serialVersionUID = 1L;

	private String name;
	private String desiredTime;

	public String getName() {
		return name;
	}

	public void setName( String name ) {
		this.name = name;
	}

	public String getSayWelcome() {
		//check if null?
		if ("".equals( name ) || (name == null))
			return "";
		else
			return "Ajax message : Welcome " + name;
	}

	@Asynchronous
	public void runSchedule( @Observes Process3Bean process3Bean ) {
		this.scheduleProcess3();
	}

	public String scheduleProcess3() {
		final String QUEUE_LOOKUP = "java:jboss/exported/jms/queue/paraQueue";
		final String CONNECTION_FACTORY = "ConnectionFactory";

		Context context;
		try {
			context = new InitialContext();
			QueueConnectionFactory factory =
					(QueueConnectionFactory) context.lookup( CONNECTION_FACTORY );
			QueueConnection connection = factory.createQueueConnection();
			QueueSession session =
					connection.createQueueSession( false,
							QueueSession.AUTO_ACKNOWLEDGE );

			Queue queue = (Queue) context.lookup( QUEUE_LOOKUP );
			QueueSender sender = session.createSender( queue );

			ObjectMessage objMsg = session.createObjectMessage();
			//			MapMessage mapMessage = session.createMapMessage();

			Process3 process = new Process3();
			process.setDesiredTime( Long.parseLong( desiredTime ) );

			// create the map
			Map<String, Object> mapObject = new HashMap<String, Object>();
			mapObject.put( "keyString", "Test-3" );
			mapObject.put( "keyObject", process );
			objMsg.setObject( (Serializable) mapObject );

			sender.send( objMsg );

			System.out.println( "Sent Process3 to queue.." );

			//			Process3 process = new Process3();

			/*
			 * set some parameters into the bean so we can then gather the info once the JMS onMessage method gets the deserialized object
			 * and invokes methods using a standard interface
			 */

			//			process.setDesiredTime( Long.parseLong( desiredTime ) );
			//			objMsg.setObject( process );
			//			sender.send( objMsg );

			session.close();
			connection.close();
			context.close();

			//			System.out.println( "* Scheduled MDSP 3: " + process.getDesiredTime() + "s" );
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		name = "Sent Process3 to queue..";
		return name;
	}

	/**
	 * @param desiredTime
	 *            the desiredTime to set
	 */
	public void setDesiredTime( String desiredTime ) {
		this.desiredTime = desiredTime;
	}

	public String getDesiredTime() {
		return desiredTime;
	}

}