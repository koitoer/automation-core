package com.staples.sa.automation;

import cucumber.api.CucumberOptions;
import cucumber.api.testng.AbstractTestNGCucumberTests;

/**
 * This class will start all the test cases.
 */
@CucumberOptions(
		features = "src/test/resources/B-00001-StackOverflow.feature",
		format = { "pretty", "html:cucumber-html-reports",
				"json:cucumber-html-reports/cucumber.json" },
		tags = { "@test", "~@ignore" })
public class RunIntegrationTest extends AbstractTestNGCucumberTests {
}
