package com.staples.sa.automation.stackoverflow.steps;

import java.net.URI;
import java.util.List;

import javax.xml.transform.Source;

import com.staples.sa.automation.commons.webservice.rest.RESTBaseIntegration;

import cucumber.api.java.en.Then;

/**
 * @author mauricio.mena
 * @since 28/06/2014
 *
 */
public class RestIntegrationTest extends RESTBaseIntegration {

	@Then("^Call REST service with param$")
	public void call_REST_service_with_param(final List<String> userId) throws Throwable {
		final Source result = this.getXMLfromURL(new URI("http://www.thomas-bayer.com/sqlrest/CUSTOMER/0"));
		final String userNameString = this.getStringFromXMLusingXPATH(result, "/CUSTOMER/FIRSTNAME/text()");
		seleniumSessionBean.saveObject("USERNAME", userNameString);
	}

}
