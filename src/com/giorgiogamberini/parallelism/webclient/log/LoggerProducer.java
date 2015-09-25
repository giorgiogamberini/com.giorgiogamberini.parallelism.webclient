package com.giorgiogamberini.parallelism.webclient.log;

import javax.ejb.Stateless;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;

import org.jboss.logging.Logger;

@Stateless
public class LoggerProducer {
	/**
	 * @param injectionPoint
	 * @return logger
	 */
	@Produces
	public Logger produceLogger( InjectionPoint injectionPoint ) {
		return Logger.getLogger( injectionPoint.getMember().getDeclaringClass().getName() );
	}
}