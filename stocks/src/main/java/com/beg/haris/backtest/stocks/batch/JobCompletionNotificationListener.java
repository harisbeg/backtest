package com.beg.haris.backtest.stocks.batch;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.beg.haris.backtest.stocks.model.Stock;

public class JobCompletionNotificationListener implements JobExecutionListener {

	private static final Logger log = LoggerFactory.getLogger(JobCompletionNotificationListener.class);

	private final JdbcTemplate jdbcTemplate;

	@Autowired
	public JobCompletionNotificationListener(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public void beforeJob(JobExecution jobExecution) {
	}

	@Override
	public void afterJob(JobExecution jobExecution) {
		if(jobExecution.getStatus() == BatchStatus.COMPLETED) {
			log.info("!!! JOB FINISHED! Time to verify the results");

			List<Stock> results = jdbcTemplate.query("SELECT ticker FROM backtest.stock", new RowMapper<Stock>() {
				@Override
				public Stock mapRow(ResultSet rs, int row) throws SQLException {
					return new Stock(rs.getString(1));
				}
			});

			log.info("Found " + results.size() + " stocks in the database.");
			
//			for (Stock stock : results) {
//				log.info("Found <" + stock + "> in the database.");
//			}

		}
	}

}
