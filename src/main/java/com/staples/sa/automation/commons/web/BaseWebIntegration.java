package com.staples.sa.automation.commons.web;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.staples.sa.automation.commons.AutomationDriver;
import com.staples.sa.automation.commons.BaseIntegration;
import com.staples.sa.automation.commons.PagesLocatorContainer;

/**
 * Base class to add spring to the test steps for web support
 * @author mauricio.mena
 * @since 28/06/2014
 */
public class BaseWebIntegration extends BaseIntegration {

	private static final Logger LOGGER = LoggerFactory.getLogger(BaseWebIntegration.class);

	/**
	 * No-arg constructor
	 */
	public BaseWebIntegration() {
		BaseWebIntegration.LOGGER.info("Start/End BaseWebIntegration");
	}

	/**
	 * After the object is build inject a PageLocator to the step test class
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	@PostConstruct
	public void initLocators() throws IllegalArgumentException, IllegalAccessException {
		BaseWebIntegration.LOGGER.info("Start BaseWebIntegration - InitLocators");

		if (webDriver.getWebDriver() == null) {
			webDriver.renewWebDriver();
			pagesLocatorContainer.restartLocators();
		}

		PagesLocatorContainer.initPageLocators(this, webDriver.getWebDriver());

	}

	/**
	 * Method used when the test ends, just apply for web test
	 */
	@PreDestroy
	public void destroy() {
		BaseWebIntegration.LOGGER.info("End BaseWebIntegration - Destroy Web Driver");
		if ((webDriver != null) && (webDriver.getWebDriver() != null)) {
			if (webDriver.getWebDriver().toString() != null) {
				webDriver.getWebDriver().close();
				webDriver.getWebDriver().quit();
				webDriver.setNullWebDriver();
			}
		}
	}

	/**
	 * Instance of AutomationDriverOperations.
	 */
	@Autowired
	protected AutomationDriver webDriver;

	/**
	 * Instance of the webPageFunctions facade.
	 */
	@Autowired
	protected WebPageFunctions webPageFunctions;

	@Autowired
	private PagesLocatorContainer pagesLocatorContainer;

}
