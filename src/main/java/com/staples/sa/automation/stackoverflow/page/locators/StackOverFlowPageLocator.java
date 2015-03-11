package com.staples.sa.automation.stackoverflow.page.locators;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import com.staples.sa.automation.commons.annotations.PageLocator;

/**
 * Page Locators for SO page.
 * @author mauricio.mena
 * @since 26/06/2014
 *
 */
@PageLocator
public class StackOverFlowPageLocator {

	@FindBy(how = How.XPATH, using = "html/body/div[4]/div/div[3]/div[1]/span/a[2]")
	public WebElement loginElement;

	@FindBy(how = How.XPATH, using = ".//*[@id='search']/input")
	public WebElement searchWebElement;

	@FindBy(how = How.XPATH, using = ".//*[@id='nav-users']")
	public WebElement userTab;

	@FindBy(how = How.XPATH, using = ".//*[@id='userfilter']")
	public WebElement userTabSearch;

	@FindBy(how = How.XPATH, using = ".//*[@id='user-browser']/table/tbody/tr/td/div/div[2]/a")
	public WebElement linkUser;

	@FindBy(how = How.XPATH, using = ".//*[@id='large-user-info']/div[1]/div[2]/table/tbody[1]/tr[2]/td[2]")
	public WebElement locationInformation;

	@FindBy(how = How.XPATH, using = ".//*[@id='hlogo']/a")
	public WebElement stackOverFlowElement;

	@FindBy(how = How.XPATH, using = ".//*[@id='user-browser']/div/span")
	public WebElement notMatchElement;
}
