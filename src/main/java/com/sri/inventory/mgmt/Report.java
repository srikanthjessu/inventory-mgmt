package com.sri.inventory.mgmt;

import java.util.List;

public class Report {
	
	private Profit profit;
	private double totalValue;
	private List<Item> items;

	public Profit getProfit() {
		return profit;
	}
	public void setProfit(Profit profit) {
		this.profit = profit;
	}
	public double getTotalValue() {
		return totalValue;
	}
	public void setTotalValue(double totalValue) {
		this.totalValue = totalValue;
	}
	public List<Item> getItems() {
		return items;
	}
	public void setItems(List<Item> items) {
		this.items = items;
	}
	
	
}
