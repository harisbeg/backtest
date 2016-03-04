package com.beg.haris.backtest.stocks.batch;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.scope.context.StepContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.beg.haris.backtest.stocks.ApplicationConstants;
import com.beg.haris.backtest.stocks.config.BatchConfiguration;
import com.beg.haris.backtest.stocks.model.Stock;

public class ReadPreExistingStocksTasklet implements Tasklet, InitializingBean {
	
	private final static Logger log = LoggerFactory.getLogger(ReadPreExistingStocksTasklet.class);

	private final JdbcTemplate jdbcTemplate;
	private List<Stock> existingStocksList = new ArrayList<Stock>();
	private String readSqlString = "SELECT ticker FROM backtest.stock";

	public ReadPreExistingStocksTasklet(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		existingStocksList.clear();
		existingStocksList = jdbcTemplate.query(readSqlString, new RowMapper<Stock>() {
			@Override
			public Stock mapRow(ResultSet rs, int row) throws SQLException {
				return new Stock(rs.getString(1));
			}
		});
		
//		for (Stock stock : existingStocksList) {
//			log.info("Found pre-existing stock in DB: " + stock.getTicker());
//		}
		log.info("Length of existingStocksList in ReadPreExistingStocksTasklet = " + existingStocksList.size());
		
		StepContext stepContext = chunkContext.getStepContext();
		stepContext.getStepExecution().getExecutionContext().put(ApplicationConstants.existingStocksListKey, existingStocksList);
		
		return RepeatStatus.FINISHED;
	}

}
