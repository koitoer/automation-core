package com.staples.sa.automation.commons;

import java.net.URL;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import com.staples.sa.automation.commons.util.ConstantUtil;

/**
 * Manage the life cycle for the WebDriver and its configuration
 * @author mauricio.mena
 * @since 26/06/2014
 */
public class AutomationDriver {

	private static final Logger LOGGER = LoggerFactory.getLogger(AutomationDriver.class);

	@Value("${com.staples.automation.config.driver}")
	private final String browserString;

	@Value("${com.driver.type}")
	private String driverType;

	private WebDriver webDriver;

	/**
	 * Constructor for the AutomationDriver
	 * @param webDriverString
	 * @param driverType 
	 */
	public AutomationDriver(final String webDriverString, final String driverType) {

		System.out.println("Artifact");

		AutomationDriver.LOGGER.info("Start AutomationDriver as type : " + driverType);
		AutomationDriver.LOGGER.info("Start AutomationDriver object creation : " + webDriverString);
		try {
			DesiredCapabilities capabilities = new DesiredCapabilities();

			if (driverType.equalsIgnoreCase("grid")) {
				// Configure selenium grid
				if (webDriverString.equals(ConstantUtil.FIREFOX)) {
					capabilities = DesiredCapabilities.firefox();
					capabilities.setCapability(FirefoxDriver.PROFILE, firefoxConfiguration());
					webDriver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), capabilities);
				}
				else if (webDriverString.equals(ConstantUtil.IE)) {
					capabilities = DesiredCapabilities.internetExplorer();
					capabilities
							.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS,
									true);
					capabilities.setVersion("8");
					webDriver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), capabilities);
				}
				else {
					throw new IllegalArgumentException("Browser is not supported");
				}
			}
			else {
				// Configure webDriver
				if (webDriverString.equals(ConstantUtil.FIREFOX)) {
					webDriver = new FirefoxDriver(capabilities);
				}
				else if (webDriverString.equals(ConstantUtil.IE)) {
					capabilities = DesiredCapabilities.internetExplorer();
					capabilities
							.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS,
									true);
					capabilities.setVersion("8");
					webDriver = new InternetExplorerDriver(capabilities);
				}
				else {
					throw new IllegalArgumentException("Browser is not supported");
				}
			}

		}
		catch (final Exception exception) {
			exception.printStackTrace();
		}

		this.browserString = webDriverString;

	}

	/**
	 * Add configuration for the Firefox webDriver
	 * */
	private FirefoxProfile firefoxConfiguration() {
		final FirefoxProfile firefoxProfile = new FirefoxProfile();
		firefoxProfile.setPreference("browser.download.folderList", 2);
		firefoxProfile.setPreference("browser.download.manager.showWhenStarting", false);
		firefoxProfile.setPreference("browser.download.dir", "c:\\tmp");
		firefoxProfile
				.setPreference(
						"browser.helperApps.neverAsk.saveToDisk",
						"image/jpg, text/csv,text/xml,application/xml,application/vnd.ms-excel,application/x-excel,"
								+ "application/x-msexcel,application/excel,application/pdf, application/vnd.openxmlformats-officedocument.spreadsheetml.sheet, application/vnd.openxmlformats-officedocument.presentationml.presentation");

		return firefoxProfile;
	}

	/**
	 * Used to obtain the configured WebDriver
	 * @return The WebDriver for the entire test cycle
	 */
	public WebDriver getWebDriver() {
		return webDriver;
	}

	/**
	 * Renew Webdriver on quit event or after test execution
	 */
	public void renewWebDriver() {
		AutomationDriver.LOGGER.info("Renew WebDriver using " + browserString);
		this.webDriver = new AutomationDriver(browserString, driverType).getWebDriver();
	}

	/**
	 * Set to null the webDriver which indicate needs to create new one
	 */
	public void setNullWebDriver() {
		this.webDriver = null;
	}

}
