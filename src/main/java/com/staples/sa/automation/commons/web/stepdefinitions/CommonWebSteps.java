package com.staples.sa.automation.commons.web.stepdefinitions;

import java.net.MalformedURLException;
import java.net.URL;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.staples.sa.automation.commons.AutomationDriver;
import com.staples.sa.automation.commons.PagesLocatorContainer;
import com.staples.sa.automation.commons.util.ConstantUtil;
import com.staples.sa.automation.commons.web.WebPageFunctions;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

/**
 * This class contains the most common and generic web functions needed for test an UI
 * @author benjamin.concha
 */
public class CommonWebSteps {

	@Autowired
	private WebPageFunctions webPageFunctions;

	@Autowired
	private AutomationDriver automationDriver;

	/**
	 * @param appName
	 * @since Aug 28, 2014
	 */
	@When("^I open '(.+)' application$")
	public void open_application(final String appName) {
	}

	/**
	 * Common function to type text into the given element
	 * @param inputText Text to type
	 * @param webElementName , the attribute name set into the pagelocator class
	 * @throws InterruptedException
	 */
	@When("^(?i:I type) '(.+)' (?i:in) '(.+)'$")
	public void type_text_in_input_field(final String inputText, final String webElementName)
			throws InterruptedException {

		webPageFunctions.getCommonFunctions().setEdit(PagesLocatorContainer.stringToWebElement(webElementName),
				inputText);
	}

	/**
	 * Common function to wait a given time
	 * @param seconds to wait
	 * @throws InterruptedException
	 */
	@When("^(?i:I wait) '([0-9]*)' (?i:seconds)$")
	public void type_text_in_input_field(final String seconds) throws InterruptedException {
		Thread.sleep(Integer.parseInt(seconds) * ConstantUtil.TIME_SLEEP_1x);
	}

	/**
	 * Common function to perform a click on a given element
	 * @param elementName , the attribute name set into the page locator class
	 * @throws InterruptedException
	 */
	@When("^(?i:I click on) '(.+)'$")
	public void click_on_field(final String elementName) throws InterruptedException {
		webPageFunctions.getCommonFunctions().waitTimeOrElementPresent(
				PagesLocatorContainer.stringToWebElement(elementName), 30);
		webPageFunctions.getCommonFunctions().clickElement(PagesLocatorContainer.stringToWebElement(elementName));
		Thread.sleep(ConstantUtil.TIME_SLEEP_1x);
	}

	/**
	 * Common function to validate if a given element is been shown in the screen
	 * @param element , the attribute name set into the page locator class
	 * @throws InterruptedException
	 */
	@Then("^'(.+)' (?i:is displayed)$")
	public void is_element_displayed(final String element) throws InterruptedException {
		final boolean isDisplayed = webPageFunctions.getCommonFunctions()
				.waitTimeOrElementPresent(PagesLocatorContainer.stringToWebElement(element), 2);
		Assert.isTrue(isDisplayed, "Element " + element + " is not properly displayed");
	}

	/**
	 * Common function to validate that a given element must not be present
	 * @param element , the attribute name set into the page locator class
	 * @throws InterruptedException
	 */
	@Then("^'(.+)' (?i:does not exist)$")
	public void element_not_exist(final String element) throws InterruptedException {
		final boolean exists = webPageFunctions.getCommonFunctions()
				.waitTimeOrElementPresent(PagesLocatorContainer.stringToWebElement(element), 2);
		Assert.isTrue(!exists, "Element " + element + " exists in the page");
	}

	/**
	 * Common function to validate if given element is enabled,  if is not ,it means that the element can not be modified or selected
	 * @param element page locator attribute name
	 * @throws InterruptedException
	 */
	@When("^'(.+)' (?i:is enabled)$")
	@Then("^'(.+)' (?i:is active)$")
	public void is_element_Active(final String element) throws InterruptedException {

		final boolean isDisplayed = webPageFunctions.getCommonFunctions()
				.waitTimeOrElementPresent(PagesLocatorContainer.stringToWebElement(element), 2);
		Assert.isTrue(isDisplayed, "Element " + element + " is not properly displayed");

		final boolean isEnabled = webPageFunctions.getCommonFunctions()
				.isElementEnabled(PagesLocatorContainer.stringToWebElement(element));
		Assert.isTrue(isEnabled, "Element " + element + " is not Enabled");
	}

	/**
	 * Common function to validate if given element is in focus
	 * @param element page locator attribute name
	 * @throws InterruptedException
	 */
	@Then("^'(.+)' (?i:is in focus)$")
	public void is_element_in_focus(final String element) throws InterruptedException {

		final boolean isDisplayed = webPageFunctions.getCommonFunctions()
				.waitTimeOrElementPresent(PagesLocatorContainer.stringToWebElement(element), 2);
		Assert.isTrue(isDisplayed, "Element " + element + " is not properly displayed");

		final WebElement webElement = PagesLocatorContainer.stringToWebElement(element);
		final boolean inFocus = webElement.equals(automationDriver.getWebDriver().switchTo().activeElement());
		Assert.isTrue(inFocus, "Element " + element + " is not in focus");

	}

	/**
	 * Common function to move the focus to a given element
	 * @param element page locator attribute name
	 * @throws InterruptedException
	 */
	@Then("^(?i:Move focus to) '(.+)'$")
	public void move_focus_to_element(final String element) throws InterruptedException {

		final WebElement webElement = PagesLocatorContainer.stringToWebElement(element);
		if ("input".equals(webElement.getTagName())) {
			webElement.sendKeys("");
		}
		else {
			new Actions(automationDriver.getWebDriver()).moveToElement(webElement).perform();

		}
		final boolean inFocus = webElement.equals(automationDriver.getWebDriver().switchTo().activeElement());
		Assert.isTrue(inFocus, "Element " + element + " is not in focus");
	}

	/**
	 * Common function to validate if a web element contains the given class
	 * @param element
	 * @param className ,class name String to validate
	 */
	@Then("^'(.+)' (?i:has) '(.+)' (?i:class)$")
	public void element_has_class(final String element, final String className) {
		final WebElement webElement = PagesLocatorContainer.stringToWebElement(element);
		final boolean hasClass = webElement.getAttribute("class").toLowerCase().contains(className.toString());
		Assert.isTrue(hasClass, "Element " + element + " not contains the expected class " + className);
	}

	/**
	 * Common function to set an empty string into element's inputs
	 * @param elementLocator
	 * @throws Throwable
	 */
	@Then("^(?i:Clear inputs from) '(.+)'$")
	public void clear_inputs_from_element(final String elementLocator) throws Throwable {
		final WebElement formInputs = PagesLocatorContainer.stringToWebElement(elementLocator);
		for (final WebElement we : formInputs.findElements(By.xpath(".//input"))) {
			if (webPageFunctions.getCommonFunctions().isElementDisplayed(we)) {
				webPageFunctions.getCommonFunctions().setEdit(we, "");
			}
		}
	}

	/**
	 * Common function to set form's selects to its defaults values
	 * @param elementLocator
	 * @throws Throwable
	 */
	@Then("^(?i:Clear selects from) '(.+)'$")
	public void clear_selects_from_element(final String elementLocator) throws Throwable {
		final WebElement formInputs = PagesLocatorContainer.stringToWebElement(elementLocator);
		for (final WebElement we : formInputs.findElements(By.xpath(".//select"))) {
			final Select select = new Select(we);
			if (select.isMultiple()) {
				select.deselectAll();
			}
			else {
				select.selectByValue("");
			}
		}

	}

	/**
	 * Common function to reset form's elements to its defaults values
	 * @param formName
	 * @throws Throwable
	 */
	@Then("^(?i:i reset) '(.+)'$")
	public void reset_form_values(final String formName) throws Throwable {
		final WebElement formInputs = PagesLocatorContainer.stringToWebElement(formName);
		for (final WebElement we : formInputs.findElements(By.xpath(".//input"))) {
			if (webPageFunctions.getCommonFunctions().isElementDisplayed(we)) {
				webPageFunctions.getCommonFunctions().setEdit(we, "");
			}
		}
		for (final WebElement we : formInputs.findElements(By.xpath(".//select"))) {
			final Select select = new Select(we);
			if (select.isMultiple()) {
				select.deselectAll();
			}
			else {
				select.selectByValue("");
			}
		}

	}

	/**
	 * Common step for select an option by its Visible text
	 * e.g.
	 * 
	 * @param option
	 * @param selectElement
	 */
	@Then("^(?i:I Choose) '(.+)' (?i:option from) '(.+)'$")
	public void select_option_from_select(final String option, final String selectElement) {
		final WebElement webElement = PagesLocatorContainer.stringToWebElement(selectElement);
		final boolean isDisplayed = webPageFunctions.getCommonFunctions()
				.waitTimeOrElementPresent(webElement, 2);
		Assert.isTrue(isDisplayed, "Element " + selectElement + " is not properly displayed");
		final Select select = new Select(PagesLocatorContainer.stringToWebElement(selectElement));
		if (selectElement.isEmpty()) {
			if (select.isMultiple()) {
				select.deselectAll();
			}
			else {
				select.selectByValue("");
			}
		}
		else {
			select.selectByVisibleText(option);
		}
	}

	/**
	 * Common step for navigate through urls
	 * e.g. :
	 *     currentUrl = http://staples.spear.com/maggie/myrequests
	 * "I navigate to '../bart/home'" will redirect to http://staples.spear.com/bart/home
	 * "I navigate to '/request?id=1'" will redirect to  http://staples.spear.com/maggie/myrequests/request?id=1
	 * "I navigate to 'http://sfdc.com/login'" will redirect to http://sfdc.com/login
	 * 
	 * @param url
	 */
	@Then("^(?i:I navigate to) '(.+)'$")
	public void i_navigate_to(final String url) {
		try {
			String nextUrl = url;
			final String currentUrlString = automationDriver.getWebDriver().getCurrentUrl();
			final URL currentURL = new URL(currentUrlString);
			if (url.startsWith("../")) {
				nextUrl = currentURL.getProtocol() + "://" + currentURL.getAuthority() + url.replace("..", "");
			}
			else if (url.startsWith("/")) {
				nextUrl = currentURL.getProtocol() + "://" + currentURL.getAuthority() + currentURL.getPath() + url;
			}
			new URL(nextUrl);
			automationDriver.getWebDriver().navigate().to(nextUrl);
		}
		catch (final MalformedURLException malformedURLException) {
			throw new IllegalArgumentException("Not a valid URL to navigate");
		}

	}

}
