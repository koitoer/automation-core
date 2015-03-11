package com.staples.sa.automation.commons;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Assert;
import org.junit.Ignore;

import com.staples.sa.automation.commons.excel.ExcelCommons;

/**
 * @author mauricio.mena
 * @since 23/09/2014
 *
 */
@Ignore
public class ExcelCommonsTest {

	private XSSFWorkbook workbook;
	private XSSFWorkbook workbook2;

	private ExcelCommons excelCommons = new ExcelCommons();;

	@Ignore
	public void obtainFilesTest() throws IOException {
		final File currDir = new File(".");
		String pathProject = currDir.getAbsolutePath();
		pathProject = pathProject.substring(0, pathProject.length() - 1);

		pathProject =
				pathProject
						+ "src\\test\\java\\com\\staples\\sa\\automation\\input\\B-27014-TC002-NoApproval-Feedback.xlsx";

		// excelCommons = new ExcelCommons();
		workbook = excelCommons.getWoorkbookFromFile(pathProject);
		Assert.assertNotNull("Workbook is null", workbook);

		pathProject = "";
		pathProject =
				pathProject
						+ "src\\test\\java\\com\\staples\\sa\\automation\\expected\\B-27014-TC002-NoApproval-Feedback.xlsx";

		excelCommons = new ExcelCommons();
		workbook2 = excelCommons.getWoorkbookFromFile(pathProject);
		Assert.assertNotNull("Workbook is null", workbook2);

	}

	@Ignore
	public void getCellValue() throws IOException {
		final XSSFSheet ws = workbook.getSheet("Instructions and Form");

		final String result = excelCommons.obtainCellValue(ws, "A7");
		System.out.println("Value: " + result);
		Assert.assertTrue("The length is not valid for that SKU", result.length() == 6);
	}

	@Ignore
	public void getCellComment() throws IOException {
		final XSSFSheet ws = workbook.getSheet("Instructions and Form");

		final String result = excelCommons.obtainCellComment(ws, "A6");
		System.out.println("Comment: " + result);
		// Assert.assertTrue("The length is not valid for that SKU", result.length() == 6);
	}

	@Ignore
	public void getRowValue() throws IOException {
		final XSSFSheet ws = workbook.getSheet("Instructions and Form");

		final Map<String, ArrayList<String>> result = excelCommons.obtainRowItems(ws, 5);

		final Set<Entry<String, ArrayList<String>>> entryResult = result.entrySet();

		for (final Map.Entry<String, ArrayList<String>> entry : entryResult) {
			System.out.println(entry.getKey() + "-" + entry.getValue());
		}

		// System.out.println();
		// Assert.assertTrue("The length is not valid for that SKU", result.length() == 6);
	}

	@Ignore
	public void compareWorkbooks() throws IOException {

		final XSSFSheet ws1 = workbook.getSheet("Instructions and Form");
		final XSSFSheet ws2 = workbook2.getSheet("Instructions and Form");

		final boolean resultWorkbook = excelCommons.compareFiles(ws1, ws2, 5);

		Assert.assertTrue("Workbook are not equal", resultWorkbook);

		// Map<String, ArrayList<String>> result = excelCommons.obtainRowItems(ws, 5);

		// Set<Entry<String, ArrayList<String>>> entryResult = result.entrySet();

		/*
		 * for (final Map.Entry<String, ArrayList<String>> entry : entryResult) { System.out.println(entry.getKey() +
		 * "-" + entry.getValue()); }
		 * 
		 * // System.out.println(); // Assert.assertTrue("The length is not valid for that SKU", result.length() == 6);
		 */

	}
}
