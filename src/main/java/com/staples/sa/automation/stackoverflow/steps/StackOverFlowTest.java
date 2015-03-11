package com.staples.sa.automation.stackoverflow.steps;

import com.staples.sa.automation.commons.web.BaseWebIntegration;
import com.staples.sa.automation.stackoverflow.page.locators.StackOverFlowPageLocator;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.runtime.java.StepDefAnnotation;

/**
 * @author mauricio.mena
 * @since 26/06/2014
 */
@StepDefAnnotation
public class StackOverFlowTest extends BaseWebIntegration {

	private StackOverFlowPageLocator stackOverFlowPageLocator;

	/**
	 * @throws Throwable
	 */
	@Given("^I Open StackOverFlow application$")
	public void I_Open_StackOverFlow_application() throws Throwable {
		webPageFunctions.getCommonFunctions().openEnvironment("http://stackoverflow.com/");
	}

	@Then("^Search for user (.+)$")
	public void Search_for_user(final String userName) throws Throwable {
		webPageFunctions.getCommonFunctions().setEdit(stackOverFlowPageLocator.searchWebElement, userName + "\n");
	}

	@Then("^Find user (.+)$")
	public void Find_user(final String userName) throws Throwable {
		webPageFunctions.getCommonFunctions().setEdit(stackOverFlowPageLocator.userTabSearch, userName + "\n");
	}

	@Then("^Find from REST call$")
	public void find_from_REST_call() throws Throwable {
		final String userName = (String) seleniumSessionBean.getObject("USERNAME");
		webPageFunctions.getCommonFunctions().setEdit(stackOverFlowPageLocator.userTabSearch, userName + "\n");
		webPageFunctions.getCommonFunctions().waitForElement(stackOverFlowPageLocator.notMatchElement);
		Thread.sleep(5000);
	}

	@Then("^Verify Location is (.+)$")
	public void Verify_Location_is(final String country) throws Throwable {
		final String valueString =
				webPageFunctions.getCommonFunctions().getElementText(stackOverFlowPageLocator.locationInformation);
		System.out.println(">> Input county " + country + " and " + valueString);
	}

}
