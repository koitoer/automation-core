package com.staples.sa.automation.commons;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author mauricio.mena
 * @since 27/06/2014
 *
 */
public class Launch {

	/**
	 * @param args
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	public static void main(final String args[]) throws IllegalArgumentException, IllegalAccessException {
		final ApplicationContext applicationContext =
				new ClassPathXmlApplicationContext("META-INF/spring/application-Context.xml");

		PagesLocatorContainer.initPageLocators(new Launch(), null);

	}
}
