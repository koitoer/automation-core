package com.staples.sa.automation.commons.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.staples.sa.automation.commons.SeleniumSessionBean;

/**
 * Excel Class to obtain access to information
 * @author mauricio.mena
 * @since 27/06/2014
 *
 */
@Component
public class ExcelCommons {

	private static final Logger LOGGER = LoggerFactory.getLogger(ExcelCommons.class);
	@Autowired
	private SeleniumSessionBean seleniumSessionBean;

	/**
	 * Method that return a workbook from specific file.
	 * @param fileName
	 * @return XSSFWorkbook
	 * @throws IOException
	 */
	public XSSFWorkbook getWoorkbookFromFile(final String fileName) throws IOException {
		final FileInputStream file = new FileInputStream(new File(fileName));
		final XSSFWorkbook workbook = new XSSFWorkbook(file);
		return workbook;
	}

	/**
	 * Method that return a sheet from specific file.
	 * @param fileName
	 * @return XSSFWorkbook
	 * @throws IOException
	 */
	public XSSFSheet getSheetFromFile(final String fileName, final String commonSheetName) throws IOException {
		final XSSFWorkbook workbook = this.getWoorkbookFromFile(fileName);

		final XSSFSheet sheet = workbook.getSheet(commonSheetName);
		return sheet;
	}

	/**
	 * Method to obtain a the value of specific cell
	 * @param sheet
	 * @param cell
	 * @return String
	 */
	public String obtainCellValue(final Sheet sheet, final String cell) {
		String cellValue = "";
		final CellReference ref = new CellReference(cell);
		final Row r = sheet.getRow(ref.getRow());
		if (r != null) {
			final Cell c = r.getCell(ref.getCol());
			cellValue = c.toString();
		}
		return cellValue;
	}

	/**
	 * Method to obtain comment of specific cell
	 * @param sheet
	 * @param cell
	 * @return String
	 */
	public String obtainCellComment(final Sheet sheet, final String cell) {
		String cellComment = "";
		final CellReference ref = new CellReference(cell);
		final Row r = sheet.getRow(ref.getRow());
		if (r != null) {
			final Cell c = r.getCell(ref.getCol());
			cellComment = c.getCellComment().getString().toString();
		}
		return cellComment;
	}

	/**
	 * Method to obtain all the Items in a row.
	 * @param sheet
	 * @param rowNumber
	 * @return Map<String, ArrayList<String>>
	 */
	public Map<String, ArrayList<String>> obtainRowItems(final Sheet sheet, final int rowNumber) {

		final Map<String, ArrayList<String>> hashmap = new HashMap<String, ArrayList<String>>();
		final int colNum = sheet.getRow(rowNumber).getPhysicalNumberOfCells();
		final Row row = sheet.getRow(rowNumber);

		ArrayList<String> arraylist = null;

		if (!ExcelCommons.isEmptyRow(row)) {
			for (int j = 0; j < colNum; j++) {
				arraylist = new ArrayList<String>();
				final Cell cell = row.getCell(j);
				final String cellValue = obtainCellValue(sheet, ((XSSFCell) cell).getReference().toString());
				String comment = "";
				try {
					comment = obtainCellComment(sheet, ((XSSFCell) cell).getReference().toString());
				}
				catch (final NullPointerException nullPointerException) {
					ExcelCommons.LOGGER.error("There is null pointer exception for " + cellValue);
				}
				arraylist.add(cellValue);
				arraylist.add(comment);
				hashmap.put(((XSSFCell) cell).getReference(), arraylist);
			}
		}
		return hashmap;

	}

	/**
	 * Used to compare files using names
	 * @param file1
	 * @param file2
	 * @param commonSheetName
	 * @param initialRow 
	 * @return boolean
	 * @throws IOException
	 * @since Aug 28, 2014
	 */
	public boolean compareFiles(final String file1, final String file2, final String commonSheetName,
			final int initialRow) throws IOException {

		final XSSFWorkbook workbook1 = this.getWoorkbookFromFile(file1);
		final XSSFWorkbook workbook2 = this.getWoorkbookFromFile(file2);

		final XSSFSheet ws1 = workbook1.getSheet(commonSheetName);
		final XSSFSheet ws2 = workbook2.getSheet(commonSheetName);

		return this.compareFiles(ws1, ws2, initialRow);
	}

	/**
	 * Method that verify if two files are the same using sheets
	 * @param file1
	 * @param file2
	 * @param initialRow 
	 * @return boolean
	 */
	public boolean compareFiles(final Sheet file1, final Sheet file2, final int initialRow) {
		final int rowNum = file1.getLastRowNum() + 1;
		List<String> listSkuWorkbook = new ArrayList<String>();
		List<String> listNewFixedPriceWorkbook = new ArrayList<String>();
		List<String> listCurrentPriceWorkbook = new ArrayList<String>();
		List<String> listAddChangeDeleteWorkbook = new ArrayList<String>();
		List<String> listListLessCostWorkbook = new ArrayList<String>();
		int index = 0;
		for (int rowCount = initialRow; rowCount < rowNum; rowCount++) {
			final XSSFRow row = (XSSFRow) file1.getRow(rowCount);
			if (!ExcelCommons.isEmptyRow(row)) {
				final Map<String, ArrayList<String>> resultFile1 = obtainRowItems(file1, rowCount);
				final Map<String, ArrayList<String>> resultFile2 = obtainRowItems(file2, rowCount);

				final Set<Entry<String, ArrayList<String>>> entryResult1 = resultFile1.entrySet();

				for (final Map.Entry<String, ArrayList<String>> entry : entryResult1) {
					final String keyValue = entry.getKey().toString();
					if (keyValue.contains("A")) {
						listSkuWorkbook.add(index, resultFile1.get(keyValue).get(0).toString());
					}
					if (keyValue.contains("F")) {
						listNewFixedPriceWorkbook.add(index, resultFile1.get(keyValue).get(0).toString());
					}
					if (keyValue.contains("G")) {
						listAddChangeDeleteWorkbook.add(index, resultFile1.get(keyValue).get(0).toString());
					}
					if (keyValue.contains("L")) {
						listCurrentPriceWorkbook.add(index, resultFile1.get(keyValue).get(0).toString());
					}
					if (keyValue.contains("R")) {
						listListLessCostWorkbook.add(index, resultFile1.get(keyValue).get(0).toString());
					}
					if (!resultFile1.get(keyValue).equals(resultFile2.get(keyValue))) {
						return false;
					}
				}
				index++;
			}
		}
		seleniumSessionBean.saveObject("listSkuWorkbook", listSkuWorkbook);
		seleniumSessionBean.saveObject("listNewFixedPriceWorkbook", listNewFixedPriceWorkbook);
		seleniumSessionBean.saveObject("listCurrentPriceWorkbook", listCurrentPriceWorkbook);
		seleniumSessionBean.saveObject("listListLessCostWorkbook", listListLessCostWorkbook);
		seleniumSessionBean.saveObject("listAddChangeDeleteWorkbook", listAddChangeDeleteWorkbook);
		return true;
	}

	/***
	 * This method return true if a row is empty
	 * @param row
	 * @return boolean
	 */
	public static boolean isEmptyRow(final Row row) {
		boolean isEmptyRow = true;
		for (int cellNum = row.getFirstCellNum(); cellNum < row.getLastCellNum(); cellNum++) {
			final Cell cell = row.getCell(cellNum);
			if (cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK && StringUtils.isNotBlank(cell.toString())) {
				isEmptyRow = false;
			}
		}
		return isEmptyRow;
	}

}
