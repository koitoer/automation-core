package com.staples.sa.automation.commons.database;

import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class will scan the path for a valid SQL queries file
 * and allow the developer extract the queries from the xml file.
 * @author mauricio.mena
 * @since 19/08/2014
 *
 */
public class QueryExtractor {

	private static final Logger LOGGER = LoggerFactory.getLogger(QueryExtractor.class);

	private Properties properties = null;

	/**
	 * Constructor that load the xmlFile
	 * @param xmlFileString
	 */
	public QueryExtractor(final String xmlFileString) {
		QueryExtractor.LOGGER.info("The xml for query trying to insert is " + xmlFileString);
		try {
			QueryExtractor.LOGGER.info("Start creation of QueryExtractor bean");
			this.readProperties(xmlFileString);
		}
		catch (final Exception e) {
			QueryExtractor.LOGGER.equals("Error loading the queries and extracting them");
		}
		QueryExtractor.LOGGER.info("End creation of QueryExtractor bean");
	}

	/**
	 * Method that take the xml file with the queries and create the properties object
	 * with each of the values within the xml file.
	 * @param xmlFileName
	 * @throws Exception
	 */
	public void readProperties(final String xmlFileName) throws Exception {
		if (properties == null) {
			properties = new Properties();
			final InputStream is = this.getClass().getClassLoader().getResourceAsStream(xmlFileName);
			properties.loadFromXML(is);
		}
	}

	/**
	 * Retrieve the query based on the key name
	 * @param sqlStringKey
	 * @return SQL statement
	 */
	public String getQueryByName(final String sqlStringKey) {
		final String sqlQuery = properties.getProperty(sqlStringKey);
		QueryExtractor.LOGGER.info(sqlQuery);
		return sqlQuery;
	}
}
