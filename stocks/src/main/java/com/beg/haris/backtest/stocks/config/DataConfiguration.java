package com.beg.haris.backtest.stocks.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class DataConfiguration {
	
	@Bean
	@ConfigurationProperties(prefix="spring.ds_backtest")
	public DataSource dsBacktest() {
		return DataSourceBuilder.create().build();
	}
	
	@Bean
	@Autowired
	public JdbcTemplate jdbcTemplateBacktest(@Qualifier("dsBacktest") DataSource dataSource) {
		return new JdbcTemplate(dataSource);
	}

}
