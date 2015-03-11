package com.staples.sa.automation.commons.database;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;

import com.staples.sa.automation.commons.util.ConditionalUtil;
import com.staples.sa.automation.commons.util.ConstantUtil;

/**
 * Connector Driver to execute SQL statements
 * @author mauricio.mena, david.hernandezg
 * @since 27/06/2014
 *
 */
public class DatabaseCommons {

	private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseCommons.class);

	protected final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	protected final QueryExtractor queryExtractor;
	private static final String SQL_FN_GET_PRICE_SOURCE = "GetPriceSource";
	/**
	 * Key for function returned value.
	 */
	public static final String RETURN_VALUE_KEY = "RETURN_VALUE";

	private JdbcTemplate jdbcTemplate;

	/**
	 * @param dataSource
	 */
	@Autowired
	@Qualifier("dataSource")
	public void setDataSource(final DataSource dataSource) {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}

	/**
	 * @param dataSource
	 * @param queryExtractor
	 */
	public DatabaseCommons(final DataSource dataSource, final QueryExtractor queryExtractor) {
		super();
		this.queryExtractor = queryExtractor;
		this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}

	/**
	 * Given a SQL executes it with params related added to map.
	 * Query example: SELECT * FROM my_table WHERE field = :valueName";
	 *
	 * @param query
	 * @param sqlParameterSource
	 * @return list with all rows found
	 */
	public List<Map<String, Object>> obtainResultsByQuery(final String query,
			final SqlParameterSource sqlParameterSource) {
		final List<Map<String, Object>> results = namedParameterJdbcTemplate.queryForList(query, sqlParameterSource);
		DatabaseCommons.LOGGER.info("# Results found: " + results.size());
		return results;
	}

	/**
	 * This method take a name of query and execute it with the
	 * named parameters.
	 * @param sqlStringKey
	 * @param sqlParameterSource 
	 * @return List of Result maps
	 */
	public List<Map<String, Object>> obtainResultsQueryByName(final String sqlStringKey,
			final SqlParameterSource sqlParameterSource) {
		return this.obtainResultsByQuery(queryExtractor.getQueryByName(sqlStringKey), sqlParameterSource);
	}

	/**
	 * This method take a name of query and execute and update operation with the
	 * named parameters.
	 * @param sqlStringKey
	 * @param valueMap
	 * @return Number of Affected Rows
	 */
	public Integer executeQueryUpdateByName(final String sqlStringKey, final Map<String, ? extends Object> valueMap) {
		return namedParameterJdbcTemplate.update(queryExtractor.getQueryByName(sqlStringKey), valueMap);
	}

	/**
	 * This method take a name of query and execute and update operation with the
	 * named parameters.
	 * @param sqlStringKey
	 * @param sqlParameterSource 
	 * @return Number of Affected Rows
	 */
	public Long executeQueryForCount(final String sqlStringKey, final SqlParameterSource sqlParameterSource) {
		return namedParameterJdbcTemplate.queryForObject(queryExtractor.getQueryByName(sqlStringKey),
				sqlParameterSource, Long.class);
	}

	/**
	 * This method return a List of List of String with the query resultSet.
	 * @param sqlStringKey
	 * @param sqlParameterSource
	 * @return List<List<String>>
	 */
	public List<List<String>>
			executeQueryByName(final String sqlStringKey, final SqlParameterSource sqlParameterSource) {
		final List<Map<String, Object>> result = this.obtainResultsQueryByName(sqlStringKey, sqlParameterSource);
		final List<List<String>> mainList = new ArrayList<List<String>>();
		for (final Map<String, Object> auxMap : result) {
			final List<String> secondaryList = new ArrayList<String>();
			for (final String auxString : auxMap.keySet()) {
				secondaryList.add(auxMap.get(auxString).toString());
			}
			mainList.add(secondaryList);
		}
		return mainList;

	}

	/**
	 * It is used to call FUNCTION [dbo].[GetPriceSource] to return on contract flag using:
	 * SP return value 		Function return value
	 * Master 				Y
	 * Billto 				Y
	 * Cons 				Y
	 * Other 				N
	 * @param division
	 * @param customerNumber
	 * @param sku
	 * @return boolean
	 */
	public boolean getOnContractFlag(final String division, final long customerNumber, final String sku) {
		final SimpleJdbcCall call = this.getSimpleJdbcCall();
		final MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
		mapSqlParameterSource.addValue("Div", division);
		mapSqlParameterSource.addValue("Cus", customerNumber);
		mapSqlParameterSource.addValue("Sku", sku);

		call.declareParameters(new SqlParameter("Div", Types.VARCHAR))
				.declareParameters(new SqlParameter("Cus", Types.INTEGER))
				.declareParameters(new SqlParameter("Sku", Types.VARCHAR));

		DatabaseCommons.LOGGER.info("QUERY_SQL = " + DatabaseCommons.SQL_FN_GET_PRICE_SOURCE);
		final long msStart = System.currentTimeMillis();
		String resultString = ConstantUtil.EMPTY_STRING;

		try {
			final Map<String, Object> result = call.execute(mapSqlParameterSource);
			resultString = (String) result.get(DatabaseCommons.RETURN_VALUE_KEY);
		}
		catch (final Exception exception) {
			DatabaseCommons.LOGGER.error("Exception onContract Flag jdbc " + exception.getMessage());
			return false;
		}

		DatabaseCommons.LOGGER.info("Response to " + sku + " is " + resultString);
		DatabaseCommons.LOGGER.info("QUERY_COMPLETED: {} ms", System.currentTimeMillis() - msStart);

		if (ConditionalUtil.anyOfTheseStrings(resultString, new String[] { ConstantUtil.MASTER, ConstantUtil.BILLTO,
				ConstantUtil.CONS })) {
			return true;
		}

		return false;
	}

	/**
	 * Used to obtain a simple jdbcall already configured.
	 * @return SimpleJdbcCall
	 */
	protected SimpleJdbcCall getSimpleJdbcCall() {
		return new SimpleJdbcCall(jdbcTemplate).withFunctionName(DatabaseCommons.SQL_FN_GET_PRICE_SOURCE)
				.withCatalogName("BID").withSchemaName("dbo");
	}
}
