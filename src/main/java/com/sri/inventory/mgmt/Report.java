package com.sri.inventory.mgmt;

import java.util.List;

public class Report {
	
	private Profit profit;
	private List<Item> items;

	public Profit getProfit() {
		return profit;
	}
	public void setProfit(Profit profit) {
		this.profit = profit;
	}
	public List<Item> getItems() {
		return items;
	}
	public void setItems(List<Item> items) {
		this.items = items;
	}
	
	
}
