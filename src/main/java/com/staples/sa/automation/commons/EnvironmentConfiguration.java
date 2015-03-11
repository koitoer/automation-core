package com.staples.sa.automation.commons;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * This class is used to configure and prepare the environment to run the entire flow.
 * @author mauricio.mena
 * @since 26/06/2014
 */
@Component
public class EnvironmentConfiguration {

	private static final Logger LOGGER = LoggerFactory.getLogger(EnvironmentConfiguration.class);

	/**
	 * Create temporal directory for the test scenarios.
	 */
	public void createTemporalDirectory() {
		EnvironmentConfiguration.LOGGER.debug("Creating temporary directory ");
		final File downloadDir = new File("c:\\tmp");
		if (!downloadDir.exists()) {
			downloadDir.mkdir();
		}
	}
}
