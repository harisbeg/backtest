package com.beg.haris.backtest.stocks.batch;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;

import com.beg.haris.backtest.stocks.ApplicationConstants;
import com.beg.haris.backtest.stocks.model.Stock;

public class FilterExistingStocksProcessor implements ItemProcessor<Stock, Stock> {
	
	private static final Logger log = LoggerFactory.getLogger(FilterExistingStocksProcessor.class);
	
//	@Autowired
//	@Lazy(true)
//	@Qualifier("existingStocksList")
	private List<Stock> existingStocksList;
	
//	public FilterExistingStocksProcessor(List<Stock> existingStocksList) {
//		this.existingStocksList = existingStocksList;
//		log.info("Length of existingStocksList passed to FilterExistingStocksProcessor = " + existingStocksList.size());
//	}

	@Override
	public Stock process(Stock stock) throws Exception {
//		log.info("Processing Stock = " + stock.getTicker());
		if (null == existingStocksList) {
			return stock;
		}
		
		int i = 0;
//		for (Stock existingStock: existingStocksList) {
//			log.info("existingStock" + "(" + i + ") = " + existingStock.getTicker());
//			i++;
//		}
		if (existingStocksList.contains(stock)) {
			log.info("Stock already existed in DB, so not being inserted: " + stock.getTicker());
			return null;
		}
		return stock;
	}
	
	@SuppressWarnings("unchecked")
	@BeforeStep
    public void retrieveInterstepData(StepExecution stepExecution) {
        JobExecution jobExecution = stepExecution.getJobExecution();
        ExecutionContext jobContext = jobExecution.getExecutionContext();
        if (jobContext.containsKey(ApplicationConstants.existingStocksListKey)) {
        	this.existingStocksList = (List<Stock>) jobContext.get(ApplicationConstants.existingStocksListKey);
        	log.info("Length of existingStocksList passed to FilterExistingStocksProcessor = " + existingStocksList.size());
        } else {
        	log.warn("Job execution context doesn't contain existingStocksList");
        }
    }

}
