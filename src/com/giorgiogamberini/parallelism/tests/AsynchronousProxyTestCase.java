package com.giorgiogamberini.parallelism.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import javax.xml.namespace.QName;
import javax.xml.transform.Source;
import javax.xml.ws.AsyncHandler;
import javax.xml.ws.Dispatch;
import javax.xml.ws.Response;
import javax.xml.ws.Service;
import javax.xml.ws.Service.Mode;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.giorgiogamberini.parallelism.webclient.ws.HellsWorld;

public class AsynchronousProxyTestCase {
	private final String targetNS = "http://ws.webclient.parallelism.giorgiogamberini.com/";
	private Exception handlerException;
	private boolean asyncHandlerCalled;

	QName serviceName = new QName( targetNS, "HellsWorldService" );
	QName portName = new QName( targetNS, "HellsWorldPort" );
	private static final String URL = "http://localhost:8080/com.giorgiogamberini.parallelism.webclient/HellsWorlds?wsdl";

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testInvokeAsynch() throws Exception
	{
		URL wsdlURL = new URL( URL );

		Service service = grabService();

		HellsWorld port = service.getPort( HellsWorld.class );
		Response<String> response = port.echoAsync( "sjd" );

		// access future
		System.out.println( new Timestamp( System.currentTimeMillis() ) + " before" );
		//		assertEquals( "Hello sjd!", response );
		System.out.println( new Timestamp( System.currentTimeMillis() ) + " after" );
	}

	@Test
	public void testInvokeAsynchHandler() throws Exception
	{
		System.out.println( new Timestamp( System.currentTimeMillis() ) + "testInvokeAsynchHandler" );
		AsyncHandler handler = new AsyncHandler()
		{
			@Override
			public void handleResponse( Response response )
			{
				try
				{
					String retStr = (String) response.get( 100, TimeUnit.MILLISECONDS );
					assertEquals( "Hello", retStr );
					asyncHandlerCalled = true;
				}
				catch (Exception ex)
				{
					handlerException = ex;
				}
			}
		};

		Service service = grabService();
		HellsWorld port = service.getPort( portName, HellsWorld.class );

		Future<String> future = port.echoAsync( "Hello", handler );
		future.get( 1000, TimeUnit.MILLISECONDS );

		if (handlerException != null)
			throw handlerException;

		assertTrue( "Async handler called", asyncHandlerCalled );
	}

	private Dispatch<Source> createDispatch() throws MalformedURLException
	{

		URL wsdlURL = new URL( URL );
		Service service = grabService();

		Dispatch<Source> dispatch = service.createDispatch( portName, Source.class, Mode.PAYLOAD );
		return dispatch;
	}

	private void verifyResponse( Source result ) throws IOException
	{
		System.out.println( new Timestamp( System.currentTimeMillis() ) + ": " + result.toString() );
		assertTrue( new Timestamp( System.currentTimeMillis() ) + "Unexpected response: " + result, result != null );
	}

	private Service grabService() {
		URL wsdlURL;
		try {
			wsdlURL = new URL( URL );

			Service service = Service.create( wsdlURL, serviceName );
			return service;

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}
}
