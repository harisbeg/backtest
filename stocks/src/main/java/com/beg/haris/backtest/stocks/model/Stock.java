package com.beg.haris.backtest.stocks.model;

import java.io.Serializable;

public class Stock implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4964788044319193405L;
	
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
	
	@Override
	public boolean equals(Object obj) {
		if (null == obj) {
			return false;
		}

		if (!Stock.class.isAssignableFrom(obj.getClass())) {
			return false;
		}
		
		final Stock other = (Stock) obj;
		
		if ((null == this.getTicker()) || (null == other.getTicker())) {
			return false;
		}
		
		if (!this.getTicker().equalsIgnoreCase(other.getTicker())) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {
		int hash = 19;
	    hash = 23 * hash + (this.getTicker() != null ? this.getTicker().hashCode() : 0);
	    return hash;
	}
	
}
