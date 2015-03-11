package com.staples.sa.automation.commons;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * Base class for any kind of test scenario.
 * @author mauricio.mena
 * @since 01/07/2014
 *
 */
public abstract class BaseIntegration {

	/**
	 * Instance of the sessionBean ready to save values
	 */
	@Autowired
	protected SeleniumSessionBean seleniumSessionBean;
}
