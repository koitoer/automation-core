package com.staples.sa.automation.commons;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.staples.sa.automation.commons.annotations.PageLocator;

/**
 * This class is used to manage pageLocators and webElements  initialization  and providing 
 * @author benjamin.concha
 * 
 */
@Component
public class PagesLocatorContainer {

	private static final Logger LOGGER = LoggerFactory.getLogger(PagesLocatorContainer.class);

	@Autowired
	protected AutomationDriver automationDriver;

	private static WebDriver WEB_DRIVER;

	/**
	 * Map to store the pageLocators instances
	 * The Key corresponds with the class name.
	 * The value corresponds wiht any class that contains at least one webElement
	 */
	private final static Map<String, Object> pageLocators = new HashMap<String, Object>();

	/**
	 * Map to store the webElelements proxies
	 * The Key corresponds with the field name.
	 * The value corresponds wiht any webElement contained in pageLocators
	 */
	private final static Map<String, Object> webElements = new HashMap<String, Object>();

	/**
	 * 
	 * @param locatorKey class name
	 * @return the PageLocator instance related wiht the provided locatorKey
	 */
	public static Object getLocator(final String locatorKey) {
		return PagesLocatorContainer.pageLocators.get(locatorKey);
	}

	/**
	 * Method for return the webElement based on its name
	 * @param elementKey
	 * @return webElement 
	 */
	public static WebElement stringToWebElement(final String elementKey) {

		final String key = elementKey.toUpperCase();
		Object tmp = PagesLocatorContainer.webElements.get(key);
		if (tmp == null) {
			tmp = PagesLocatorContainer.WEB_DRIVER.findElement(By.xpath(elementKey));
		}
		Assert.notNull(tmp, " Locator " + elementKey + " Not Found ");
		return (WebElement) tmp;
	}

	/**
	 * Method for return the webElement based on its name
	 * @param elementKey
	 * @return webElement 
	 */
	public static List<WebElement> stringToWebElements(final String elementKey) {

		final String key = elementKey.toUpperCase();
		Object tmp = PagesLocatorContainer.webElements.get(key);
		if (tmp == null) {
			tmp = PagesLocatorContainer.WEB_DRIVER.findElements(By.xpath(elementKey));
		}
		Assert.notNull(tmp, " Locator " + elementKey + " Not Found ");
		return (List<WebElement>) tmp;
	}

	/**
	 * Method for group all webElements into a Map
	 * @param elementKey
	 * @param object
	 */
	private static void addWebElement(final String elementKey, final Object object) {
		final String key = elementKey.toUpperCase();
		if (PagesLocatorContainer.webElements.containsKey(key)) {
			PagesLocatorContainer.LOGGER.warn("Replacing ==> {}::{}", key, object.getClass().getName());
		}
		PagesLocatorContainer.webElements.put(key, object);
	}

	/**
	 * @return  Set<String> that contains the pageLocators class names
	 */
	public static Set<String> getWebElementKeys() {
		return PagesLocatorContainer.webElements.keySet();
	}

	/**
	 * @param webObject class under Test
	 * @param webDriver context web driver
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public static void initPageLocators(final Object webObject, final WebDriver webDriver)
			throws IllegalArgumentException, IllegalAccessException {
		PagesLocatorContainer.LOGGER.debug("injecting PageLocators in " + webObject.getClass().getSimpleName());

		for (final Field field : webObject.getClass().getDeclaredFields()) {

			if (field.getName().toLowerCase().contains("pagelocator")) {
				PagesLocatorContainer.LOGGER.debug("Field for injection has been found");
				field.setAccessible(true);
				field.set(webObject, PagesLocatorContainer.getLocator(field.getType().getName()));
				PagesLocatorContainer.LOGGER.debug("Injection of {} has been done", field.getType().getName());
			}
		}
	}

	/**
	 * Every time we call quit within the webDriver we need to init the locators
	 * again using the new webDriver reference
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public void restartLocators() throws IllegalArgumentException, IllegalAccessException {
		if (PagesLocatorContainer.pageLocators != null) {
			this.init();
		}
	}

	/**
	 * init PageLocators and WebElements proxies
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	@PostConstruct
	private void init() throws IllegalArgumentException, IllegalAccessException {

		PagesLocatorContainer.WEB_DRIVER = automationDriver.getWebDriver();

		final Reflections reflections = new Reflections("com.staples.sa");
		final Set<Class<?>> annotated = reflections.getTypesAnnotatedWith(PageLocator.class);
		if (annotated.isEmpty()) {
			throw new IllegalArgumentException("No PageLocator at all");
		}

		PagesLocatorContainer.LOGGER.info("" + PagesLocatorContainer.pageLocators);
		PagesLocatorContainer.LOGGER.info("" + PagesLocatorContainer.pageLocators.size());

		// Init all pagelocator classes and web elements contained in
		PagesLocatorContainer.LOGGER.info("PageLocators Init");
		for (final Class<?> clazz : annotated) {
			PagesLocatorContainer.pageLocators.put(clazz.getName(), PageFactory.initElements(automationDriver
					.getWebDriver(),
					clazz));
		}

		// Going throght all pagelocators to find and store all web elelement proxies
		PagesLocatorContainer.LOGGER.info("WebElements Init");
		for (final Object locator : PagesLocatorContainer.pageLocators.values()) {
			for (final Field WEField : locator.getClass().getDeclaredFields()) {

				final String fieldType = WEField.getType().getSimpleName();
				final String genericType = WEField.getGenericType().toString();

				if (fieldType.equals(WebElement.class.getSimpleName())) {
					PagesLocatorContainer.addWebElement(WEField.getName(), WEField.get(locator));
				}
				else if (Collection.class.isAssignableFrom(WEField.getType())
						&& genericType.contains(WebElement.class.getSimpleName())) {
					PagesLocatorContainer.addWebElement(WEField.getName(), WEField.get(locator));
				}

			}
		}

	}

}
