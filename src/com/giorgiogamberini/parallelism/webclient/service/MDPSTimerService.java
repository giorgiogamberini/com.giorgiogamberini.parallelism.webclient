/**
 *
 */
package com.giorgiogamberini.parallelism.webclient.service;

import java.io.Serializable;

import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Singleton;

import com.giorgiogamberini.parallelism.webclient.beans.Process1Bean;
import com.giorgiogamberini.parallelism.webclient.beans.Process2Bean;
import com.giorgiogamberini.parallelism.webclient.beans.Process3Bean;

/**
 * @author giorgio [Giorgio Gamberini - work@giorgiogamberini.com] 31/dic/2014
 *
 */
@Singleton
public class MDPSTimerService implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 6972825980859327650L;
	@EJB
	private Process1Bean process1Bean;

	@EJB
	private Process2Bean process2Bean;

	@EJB
	private Process3Bean process3Bean;

	/*
	 * schedule via JEE6 standard annotation some method's invocation
	 * what is done here is simply to schedule the sending into a JMS queue an instance of a serialized "process" bean
	 */
	@Schedule( hour = "*", second = "0", minute = "*", persistent = false )
	public void fireSomething() throws InterruptedException {
		//		process1Bean.setDesiredTime( "3" );
		//		process2Bean.setDesiredTime( "12" );
		//		process3Bean.setDesiredTime( "5" );
		//
		//		process1Bean.runSchedule( new Process1Bean() );
		//		process2Bean.runSchedule( new Process2Bean() );
		//		process3Bean.runSchedule( new Process3Bean() );
	}

}