package com.giorgiogamberini.parallelism.webclient.listener;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Map;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.TextMessage;

/**
 * Message-Driven Bean implementation class for: QueueListenerMDB
 */

@MessageDriven( activationConfig = {
		@ActivationConfigProperty(
				propertyName = "destinationType", propertyValue = "javax.jms.Queue" ),
		@ActivationConfigProperty(
				propertyName = "destination", propertyValue = "java:jboss/exported/jms/queue/paraQueue" ) } )
public class QueueListenerMDB implements MessageListener {
	private static final String actionMeth = "doSomething";
	private static final String setLapse = "setDesiredTime";
	private static final String getLapse = "getDesiredTime";
	private static final String twoString = "toString";

	public QueueListenerMDB() {
	}

	@Override
	public void onMessage( Message message ) {
		try {
			if (message instanceof TextMessage) {
				System.out.println( "! Queue: I received a TextMessage at "
						+ new Date() );
				TextMessage msg = (TextMessage) message;
				System.out.println( "! Message is : " + msg.getText() );
			} else if (message instanceof MapMessage) {
				System.out.print( "******** Queue: I received an MapMessage at " + new Date() + ": " );
				// NOP right now ;)
			} else if (message instanceof ObjectMessage) {
				System.out.print( "! Queue: I received an ObjectMessage at " + new Date() + ": " );

				ObjectMessage msg = (ObjectMessage) message;
				Serializable objInQueue = msg.getObject();

				// new part: extract expected objects from the map
				Map<String, Object> mapObj = (Map<String, Object>) objInQueue;
				String stringFromMessage = (String) mapObj.get( "keyString" );
				Object objectFromMessage = mapObj.get( "keyObject" );

				System.out.println( "*** stringFromMessage: " + stringFromMessage );
				System.out.println( "*** objectFromMessage: " + objectFromMessage.getClass().toString() );
				// /new part

				objInQueue = (Serializable) objectFromMessage;

				// Setup reflection
				Method actMeth = objInQueue.getClass().getMethod( actionMeth );
				Method lapMeth = objInQueue.getClass().getMethod( setLapse, Long.class );
				Method getLapMeth = objInQueue.getClass().getMethod( getLapse );
				Method twoStr = objInQueue.getClass().getMethod( twoString );
				//				lapMeth.invoke( objInQueue, new Long( 3 ) );

				// invoke the method which returns the (externally) configured "time to sleep" long
				Long desiredTime = (Long) getLapMeth.invoke( objInQueue );
				System.out.println( "* desiredTime: " + desiredTime + "s" );

				// now invoke the "actionable"
				actMeth.invoke( objInQueue );
				// /Setup

				System.out.println( twoStr.invoke( objInQueue ) );
			} else {
				System.out.println( "! Not a valid message for this Queue MDB" );
			}

		} catch (JMSException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}