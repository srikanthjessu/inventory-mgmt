package com.sri.inventory.mgmt;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Profit {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	private double value;

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

}
