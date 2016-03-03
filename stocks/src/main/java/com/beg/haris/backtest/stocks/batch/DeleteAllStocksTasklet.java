package com.beg.haris.backtest.stocks.batch;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

public class DeleteAllStocksTasklet implements Tasklet, InitializingBean {
	
	private final JdbcTemplate jdbcTemplate;
	private String deleteSqlString = "DELETE FROM backtest.stock";

	@Autowired
	public DeleteAllStocksTasklet(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
	}

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		jdbcTemplate.execute(deleteSqlString);
		return RepeatStatus.FINISHED;
	}

}
