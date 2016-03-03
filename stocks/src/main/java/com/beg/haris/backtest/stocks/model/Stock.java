package com.beg.haris.backtest.stocks.model;

public class Stock {
	
	private String ticker;

	public Stock(String ticker) {
		super();
		this.ticker = ticker;
	}

	public Stock() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getTicker() {
		return ticker;
	}

	public void setTicker(String ticker) {
		this.ticker = ticker;
	}

	@Override
	public String toString() {
		return "Stock [ticker=" + ticker + "]";
	}
	
}
