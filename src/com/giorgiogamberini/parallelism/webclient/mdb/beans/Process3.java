/**
 *
 */
package com.giorgiogamberini.parallelism.webclient.mdb.beans;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

import javax.ejb.Asynchronous;

/**
 * @author giorgio [Giorgio Gamberini - work@giorgiogamberini.com] 30/dic/2014
 *
 */

public class Process3 implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	/**
	 *
	 */

	private Long desiredTime = 0L;
	private Long lastElapsedTime = 0L;

	public Process3() {

	}

	public Process3(Long processingTime) throws InterruptedException {
		this.desiredTime = processingTime;
	}

	/**
	 * @return the desiredTime
	 */
	public Long getDesiredTime() {
		return desiredTime;
	}

	/**
	 * @param desiredTime
	 *            the desiredTime to set
	 */
	public void setDesiredTime( Long processingTime ) {
		this.desiredTime = processingTime;
	}

	@Override
	public String toString() {
		return "* " + this.getClass().getName() + ": Elapsed " + this.lastElapsedTime + " seconds.";
	}

	/**
	 * @return the lastElapsedTime
	 */
	public Long getElapsedTime() {
		return lastElapsedTime;
	}

	/**
	 * @param lastElapsedTime
	 *            the lastElapsedTime to set
	 */
	public void setElapsedTime( Long elapsedTime ) {
		this.lastElapsedTime = elapsedTime;
	}

	@Asynchronous
	public void doSomething() throws InterruptedException {
		Long then = System.nanoTime();

		// Application BL
		TimeUnit.SECONDS.sleep( desiredTime );
		// /App BL
		this.lastElapsedTime = TimeUnit.NANOSECONDS.toSeconds( System.nanoTime() - then );
	}
}
