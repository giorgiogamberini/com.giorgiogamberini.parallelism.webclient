package com.giorgiogamberini.parallelism.webclient.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.giorgiogamberini.parallelism.webclient.mdb.beans.Process1;
import com.giorgiogamberini.parallelism.webclient.mdb.beans.Process2;
import com.giorgiogamberini.parallelism.webclient.mdb.beans.Process3;

/**
 * Servlet implementation class MessageProducerServlet
 */
@WebServlet( asyncSupported = true, urlPatterns = { "/MessageProducerServlet" } )
public class MessageProducerServlet extends HttpServlet {

	/**
	 *
	 */
	private static final long serialVersionUID = -6465796749181162635L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public MessageProducerServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void doGet( HttpServletRequest request,
			HttpServletResponse response )
			throws ServletException, IOException {
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doPost( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
		// configure JMS queue to be used
		final String QUEUE_LOOKUP = "java:jboss/exported/jms/queue/paraQueue";
		final String CONNECTION_FACTORY = "ConnectionFactory";

		PrintWriter out = response.getWriter();
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

			MapMessage mapMessage = session.createMapMessage();

			/*
			 * this is what happens when you click one of the buttons via the web interface:
			 * basically an instance of the correct object bean gets sent into the correct JMS queue
			 */
			if (request.getParameter( "button1" ) != null) {
				Process1 process = new Process1();
				objMsg.setObject( process );
				sender.send( objMsg );

				out.println( "Sent Process1 to queue.." );
			} else if (request.getParameter( "button2" ) != null) {
				Process2 process = new Process2();
				objMsg.setObject( process );
				sender.send( objMsg );

				out.println( "Sent Process2 to queue.." );
			} else if (request.getParameter( "button3" ) != null) {
				Process3 process = new Process3();
				//				objMsg.setObject( process );
				//				sender.send( objMsg );

				mapMessage.setString( "keyString", "PROVA1" );
				mapMessage.setObject( "keyObject", process );

				sender.send( mapMessage );

				out.println( "Sent Process3 to queue.." );
			} else {
				// ???
			}
			session.close();
			connection.close();
			context.close();
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//		request.getRequestDispatcher( "/WEB-INF/some-result.jsp" ).forward( request, response );

	}
}
