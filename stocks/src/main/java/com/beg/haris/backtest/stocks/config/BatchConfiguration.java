package com.beg.haris.backtest.stocks.config;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.BatchConfigurer;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.support.PassThroughItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;

import com.beg.haris.backtest.stocks.batch.DeleteAllStocksTasklet;
import com.beg.haris.backtest.stocks.batch.JobCompletionNotificationListener;
import com.beg.haris.backtest.stocks.model.Stock;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {
	
	@Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Autowired
    @Qualifier("dsBacktest")
    public DataSource dsBacktest;
    
    private static String insertSqlString = "INSERT INTO backtest.stock (ticker) VALUES (:ticker)";
    
    @Bean
    public BatchConfigurer configurer() {
		return new BacktestBatchConfigurer();
    }
    
    // tag::readerwriterprocessor[]
    @Bean
    public FlatFileItemReader<Stock> reader() {
        
    	FlatFileItemReader<Stock> reader = new FlatFileItemReader<Stock>();
        
        reader.setResource(new ClassPathResource("stocks-list.csv"));
        reader.setLineMapper(new DefaultLineMapper<Stock>() {{
            setLineTokenizer(new DelimitedLineTokenizer() {{
                setNames(new String[] { "ticker" });
            }});
            setFieldSetMapper(new BeanWrapperFieldSetMapper<Stock>() {{
                setTargetType(Stock.class);
            }});
        }});
        return reader;
    }
    
    @Bean
    public PassThroughItemProcessor<Stock> processor() {
    	return new PassThroughItemProcessor<Stock>();
    }
    
    @Bean
    public JdbcBatchItemWriter<Stock> writer() {
        JdbcBatchItemWriter<Stock> writer = new JdbcBatchItemWriter<Stock>();
        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<Stock>());
        writer.setSql(insertSqlString);
        writer.setDataSource(dsBacktest);
        return writer;
    }
    // end::readerwriterprocessor[]
    
    // tag::listener[]

    @Bean
    public JobExecutionListener listener() {
        return new JobCompletionNotificationListener(new JdbcTemplate(dsBacktest));
    }

    // end::listener[]

    // tag::jobstep[]
    @Bean
    public Job importStocksJob() {
        return jobBuilderFactory.get("importStocksJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener())
                .flow(deleteAllStocksStep()).next(mainProcessStep())
                .end()
                .build();
    }
    
    @Bean
    public Step deleteAllStocksStep() {
    	Tasklet tasklet = new DeleteAllStocksTasklet(new JdbcTemplate(dsBacktest));
    	return stepBuilderFactory.get("deleteAllStocksStep").tasklet(tasklet).build();
    }

    @Bean
    public Step mainProcessStep() {
        return stepBuilderFactory.get("step1")
                .<Stock, Stock> chunk(10)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .build();
    }
    // end::jobstep[]

    

}
