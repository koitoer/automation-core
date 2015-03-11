package com.staples.sa.automation.commons.web.functions;

import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.staples.sa.automation.commons.AutomationDriver;

/**
 * Functions related to Download File dialog
 * @author mauricio.mena
 * @since 01/07/2014
 *
 */
@Component
public class DownloadFileFunctions {

	private WebDriver webDriver;

	/**
	 * Constructor for Function class
	 * @param automationDriver
	 */
	@Autowired(required = true)
	public DownloadFileFunctions(final AutomationDriver automationDriver) {
		this.webDriver = automationDriver.getWebDriver();
	}
}
