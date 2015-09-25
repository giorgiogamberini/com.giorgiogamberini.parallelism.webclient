package com.giorgiogamberini.parallelism.webclient.ws;

import java.util.concurrent.Future;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.ws.AsyncHandler;
import javax.xml.ws.Response;
import javax.xml.ws.soap.Addressing;

@WebService( )
@Addressing( enabled = true, required = false )
public class HellsWorld {

	public void HellsWorld() {

	}

	@WebMethod( operationName = "echo" )
	@WebResult( name = "result" )
	public String echo( @WebParam( name = "String_1" ) String msg ) {

		try {
			System.out.println( "Executing long operation.." );
			Thread.sleep( 3000 );
			System.out.println( "Hello: " + msg );
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "Hello " + msg + "!";
	}

	public Future<String> echoAsync( String string, AsyncHandler handler ) {
		// TODO Auto-generated method stub
		return null;
	}

	public Response<String> echoAsync( String string ) {
		// TODO Auto-generated method stub
		return null;
	}

}