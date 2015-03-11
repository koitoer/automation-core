package com.staples.sa.automation.commons.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.staples.sa.automation.commons.web.functions.CommonElementFunctions;
import com.staples.sa.automation.commons.web.functions.DownloadFileFunctions;

/**
 * Facade to access all the web elements and their actions.
 * @author mauricio.mena
 * @since 27/06/2014
 */
@Component
public class WebPageFunctions {

	@Autowired
	private CommonElementFunctions commonFunctions;

	@Autowired
	private DownloadFileFunctions downloadDialogFunctions;

	/**
	 * @return the commonFunctions
	 */
	public CommonElementFunctions getCommonFunctions() {

		return commonFunctions;
	}

	/**
	 * @return the downloadDialogFunctions
	 */
	public DownloadFileFunctions getDownloadDialogFunctions() {

		return downloadDialogFunctions;
	}
}
