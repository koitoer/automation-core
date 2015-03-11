package com.staples.sa.automation.commons.web.functions;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.staples.sa.automation.commons.AutomationDriver;
import com.staples.sa.automation.commons.util.ConstantUtil;

import cucumber.api.java.en.Then;

/**
 * @author mauricio.mena
 * @since 26/06/2014
 *
 */
@Component("commonWebElement")
public class CommonElementFunctions {

	private static final Logger LOGGER = LoggerFactory.getLogger(CommonElementFunctions.class);

	private final AutomationDriver automationDriver;

	private String parentWindow;

	/**
	 * Constructor for Function class
	 * @param automationDriver
	 */
	@Autowired(required = true)
	public CommonElementFunctions(final AutomationDriver automationDriver) {
		this.automationDriver = automationDriver;
	}

	/**
	 * Open the URL and launch the browser
	 * @param urlString
	 */
	public void openEnvironment(final String urlString) {
		CommonElementFunctions.LOGGER.info("Opening browser with url :" + urlString);
		automationDriver.getWebDriver().manage().window().maximize();
		automationDriver.getWebDriver().manage().deleteAllCookies();
		automationDriver.getWebDriver().get(urlString);
	}


	/**
	 * 
	 * @since Aug 28, 2014
	 */
	public void closeBrowser() {
		CommonElementFunctions.LOGGER.info("Closing browser");
		automationDriver.getWebDriver().close();
	}

	/**
	 * 
	 * @param element
	 * @throws InterruptedException
	 */
	public void highlightElement(final WebElement element) throws InterruptedException {
		for (int i = 0; i < 1; i++) {
			final JavascriptExecutor js = (JavascriptExecutor) automationDriver.getWebDriver();
			js.executeScript("arguments[0].setAttribute('style', arguments[1]);", element,
					"color: red; border: 2px solid red;");
			// Thread.sleep(300);
			js.executeScript("arguments[0].setAttribute('style', arguments[1]);", element, "");
		}
	}

	/**
	 * @param frameName
	 */
	public void switchFrame(final String frameName) {
		automationDriver.getWebDriver().switchTo().frame(frameName);
	}

	/**
	 * 
	 */
	public void switchDefaultContent() {
		automationDriver.getWebDriver().switchTo().defaultContent();
	}

	/**
	 * 
	 */
	public void javaAlertAccept() {
		automationDriver.getWebDriver().switchTo().alert().accept();
	}

	/**
	 * @param parentWindow
	 */
	public void switchDefaultDriver(final String parentWindow) {
		automationDriver.getWebDriver().switchTo().window(parentWindow);
	}

	/**
	 * 
	 */
	public void refreshDriver() {
		automationDriver.getWebDriver().navigate().refresh();
	}

	/**
	 * @return String
	 * 
	 */
	public String switchGetParentWindow() {
		parentWindow = automationDriver.getWebDriver().getWindowHandle();
		for (final String winHandle : automationDriver.getWebDriver().getWindowHandles()) {
			if (!parentWindow.equals(winHandle)) {
				automationDriver.getWebDriver().switchTo().window(winHandle);
			}
		}
		automationDriver.getWebDriver().manage().window().maximize();
		return parentWindow;
	}

	/**
	 * @param webElement
	 * @return boolean
	 */
	public boolean isElementDisplayed(final WebElement webElement) {
		boolean isElementDisplayed = false;
		try {
			if (webElement.isDisplayed()) {
				isElementDisplayed = true;
			}
		}

		catch (final NoSuchElementException e) {
			isElementDisplayed = false;
		}
		return isElementDisplayed;
	}


	/**
	 * Verify if element exists in the current page
	 * @param webElement
	 * @return true si element exists on DOM
	 */
	public boolean isExistingElement(final WebElement webElement) {
		boolean isElementDisplayed = false;
		try {
			if (webElement.isDisplayed()) {
				isElementDisplayed = true;
			}
		}
		catch (final StaleElementReferenceException e) {
			CommonElementFunctions.LOGGER.info("Element not in DOM: " + e.getMessage());
		}
		catch (final NoSuchElementException e) {
			CommonElementFunctions.LOGGER.info("Element not in DOM: " + e.getMessage());
		}
		return isElementDisplayed;
	}

	/**
	 * Click on the Element that is received as parameter
	 * @param element
	 * @throws InterruptedException
	 */
	public void clickElement(final WebElement element) throws InterruptedException {
		waitForElement(element);
		isExistingElement(element);
		element.click();
	}

	/**
	 * @param element
	 * @throws InterruptedException
	 */
	public void waitForElement(final WebElement element) throws InterruptedException {
		final WebDriverWait wait = new WebDriverWait(automationDriver.getWebDriver(), 30);
		try {
			wait.until(ExpectedConditions.visibilityOf(element));
			Thread.sleep(ConstantUtil.TIME_SLEEP_1x);
		}
		catch (final NoSuchElementException e) {
			isExistingElement(element);
		}
	}

	/**
	 * Common function for wait to elemen hide, example : wait until loder_gif hide
	 * @param element
	 * @throws InterruptedException
	 */
	public void waitElementHide(final WebElement element) throws InterruptedException{
		boolean isLoaderShow=true;
		while(isLoaderShow){
			Thread.sleep(500);
			isLoaderShow = this.isElementDisplayed(element);
		}		
	}
	
	/**
	 * Makes a loop during certain period of seconds or if element appears, break the loop.
	 * 
	 * @param webElement
	 * @param seconds
	 * @return true if element appears before loop ends
	 */
	public boolean waitTimeOrElementPresent(final WebElement webElement, final int seconds) {
		boolean elementPresent = false;
		final long max_millis_to_wait = seconds * ConstantUtil.TIME_SLEEP_1x;
		final long futureDate = System.currentTimeMillis() + max_millis_to_wait;

		CommonElementFunctions.LOGGER.info("Waiting for element...");
		// Wait 'n' minutes max...
		while ((System.currentTimeMillis() < futureDate) && !elementPresent) {
			try {
				elementPresent = isExistingElement(webElement);
				Thread.sleep(ConstantUtil.TIME_SLEEP_HALF_SEC);
			}
			catch (final InterruptedException e) {
				CommonElementFunctions.LOGGER.info("Wait error: " + e.getMessage());
			}
		}
		return elementPresent;
	}

	/**
	 * 
	 */
	public void waitElementTable() {
		automationDriver.getWebDriver().manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	}

	/**
	 * Get the text from Element
	 * @param element
	 * @return String
	 * @throws InterruptedException
	 */
	public String getElementText(final WebElement element) throws InterruptedException {
		isExistingElement(element);
		return element.getText();
	}

	/**
	 * Get Attribute for Element that is received as parameter
	 * @param element
	 * @param attributeName
	 * @return content of the attribute
	 * @throws InterruptedException
	 */
	public String getAttribute(final WebElement element, final String attributeName) throws InterruptedException {
		isExistingElement(element);
		return element.getAttribute(attributeName);
	}

	/**
	 * Select an element from a list
	 * @param element
	 * @param selectedItem
	 * @throws InterruptedException
	 */
	public void selectListElement(final WebElement element, final String selectedItem)
			throws InterruptedException {
		Select list = null;
		waitForElement(element);
		list = new Select(element);
		list.selectByValue(selectedItem);
	}

	/**
	 * Select an element from a list using its visible text value
	 * @param element
	 * @param selectedItem
	 * @throws InterruptedException
	 */
	public void selectListElementByVisibleText(final WebElement element, final String selectedItem)
			throws InterruptedException {
		Select list = null;
		waitForElement(element);
		list = new Select(element);
		list.selectByVisibleText(selectedItem);
	}

	/**
	 * Set value into Text box
	 * @param edit
	 * @param inputText
	 * @throws InterruptedException
	 */
	public void setEdit(final WebElement edit, final String inputText)
			throws InterruptedException {
		waitForElement(edit);

		if (edit.isEnabled()) {
			edit.clear();
			edit.sendKeys(inputText);
		}
	}

	/**
	 * @param webElement
	 * @return boolean
	 * @throws InterruptedException
	 */
	public boolean isElementEnabled(final WebElement webElement)
			throws InterruptedException {
		if (webElement.isEnabled()) {
			return true;
		}
		return false;
	}

	/**
	 * @return String
	 */
	public String getURL() {
		return automationDriver.getWebDriver().getCurrentUrl();
	}
}
