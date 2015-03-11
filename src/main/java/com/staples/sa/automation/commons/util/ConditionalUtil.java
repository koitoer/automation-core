package com.staples.sa.automation.commons.util;

/**
 * Conditional helper methods.
 * */
public final class ConditionalUtil {

	private ConditionalUtil() {
	}

	/**
	 * Evaluate if input is within the array.
	 * @param input
	 * @param arrayToCompare
	 * @return boolean
	 */
	public static boolean anyOfTheseStrings(final String input, final String[] arrayToCompare) {
		if (input == null || arrayToCompare == null) {
			return false;
		}
		for (final String auxString : arrayToCompare) {
			if (input.equals(auxString)) {
				return true;
			}
		}
		return false;
	}
}
