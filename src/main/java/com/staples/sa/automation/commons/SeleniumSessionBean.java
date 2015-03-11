package com.staples.sa.automation.commons;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

/**
 * Session bean that works to store values during the test cycle
 * @author mauricio.mena
 * @since 26/06/2014
 *
 */
@Component
public class SeleniumSessionBean {

	private Map<String, Object> sessionObjects = new HashMap<String, Object>();

	/**
	 * Method used to save any object for future reference
	 * @param key
	 * @param object
	 */
	public void saveObject(final String key, final Object object) {
		sessionObjects.put(key, object);
	}

	/**
	 * Method to extract an specific object from the session bean
	 * @param key
	 * @return Object that have certain key
	 */
	public Object getObject(final String key) {
		return sessionObjects.get(key);
	}

}
