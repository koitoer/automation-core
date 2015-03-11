package com.staples.sa.automation.commons.webservice.rest;

import java.io.StringReader;
import java.io.StringWriter;
import java.net.URI;

import javax.xml.transform.Source;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.xml.sax.InputSource;

import com.staples.sa.automation.commons.webservice.WebServiceBaseIntegration;

/**
 * In case of REST technologies use this base class
 * @author mauricio.mena
 * @since 28/06/2014
 *
 */
public class RESTBaseIntegration extends WebServiceBaseIntegration {

	private static final Logger LOGGER = LoggerFactory.getLogger(RESTBaseIntegration.class);

	@Autowired
	private RestTemplate restTemplate;

	/**
	 * GET to an specific URL returning the XML representation
	 * @param uri
	 * @return Source that represent the XML
	 */
	protected Source getXMLfromURL(final URI uri) {
		final ResponseEntity<Source> entity = restTemplate.getForEntity(uri, Source.class);
		return entity.getBody();
	}

	/**
	 * Obtain a specific String value using XPATH from a XML source
	 * @param uri
	 * @return Source that represent the XML
	 */
	protected String getStringFromXMLusingXPATH(final Source source, final String xPathExpression) {
		String result = null;
		try {
			final TransformerFactory transformerFactory = TransformerFactory.newInstance();
			final StringWriter writer = new StringWriter();
			transformerFactory.newTransformer().transform(source, new StreamResult(writer));
			final XPathFactory xpathFactory = XPathFactory.newInstance();
			final XPath xpath = xpathFactory.newXPath();
			result =
					(String) xpath.evaluate(xPathExpression, new InputSource(new StringReader(writer
							.toString())), XPathConstants.STRING);
		}
		catch (final Exception e) {
			RESTBaseIntegration.LOGGER.error(e.getMessage());
			RESTBaseIntegration.LOGGER.error("Can not parse or extract xPATH");
		}
		return result;
	}
}
