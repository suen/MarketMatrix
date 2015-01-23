package com.daubajee.marketmatrix.agent;

import java.util.Map;

public class MarketAgentAttribute {

	private String produces = "";
	private String consumes = "";
	private double produceRate = 0;
	private double consumeRate = 0;
	private int produceProductStock = 0;
	private int produceProductStockCapacity = 0;
	private int consumeProductStock = 0;
	private int consumeProductStockCapacity = 0;
	private int workFreeCounter = 0;
	private int hungerCounter = 0;
	private double satisfaction = 0;
	private double money = 0;
	private double price = 0;
	
	public String getProduces() {
		return produces;
	}

	public void setProduces(String produces) {
		this.produces = produces;
	}

	public String getConsumes() {
		return consumes;
	}

	public void setConsumes(String consumes) {
		this.consumes = consumes;
	}

	public double getProduceRate() {
		return produceRate;
	}

	public void setProduceRate(double produceRate) {
		this.produceRate = produceRate;
	}

	public double getConsumeRate() {
		return consumeRate;
	}

	public void setConsumeRate(double consumeRate) {
		this.consumeRate = consumeRate;
	}

	public int getProduceProductStock() {
		return produceProductStock;
	}

	public void setProduceProductStock(int produceProductStock) {
		this.produceProductStock = produceProductStock;
	}

	public int getProduceProductStockCapacity() {
		return produceProductStockCapacity;
	}

	public void setProduceProductStockCapacity(int produceProductStockCapacity) {
		this.produceProductStockCapacity = produceProductStockCapacity;
	}

	public int getConsumeProductStock() {
		return consumeProductStock;
	}

	public void setConsumeProductStock(int consumeProductStock) {
		this.consumeProductStock = consumeProductStock;
	}

	public int getConsumeProductStockCapacity() {
		return consumeProductStockCapacity;
	}

	public void setConsumeProductStockCapacity(int consumeProductStockCapacity) {
		this.consumeProductStockCapacity = consumeProductStockCapacity;
	}

	public double getSatisfaction() {
		return satisfaction;
	}

	public void setSatisfaction(double satisfaction) {
		this.satisfaction = satisfaction;
	}

	public double getMoney() {
		return money;
	}

	public void setMoney(double money) {
		this.money = money;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getWorkFreeCounter() {
		return workFreeCounter;
	}

	public void setWorkFreeCounter(int workFreeCounter) {
		this.workFreeCounter = workFreeCounter;
	}

	public int getHungerCounter() {
		return hungerCounter;
	}

	public void setHungerCounter(int hungerCounter) {
		this.hungerCounter = hungerCounter;
	}

	public boolean initialize(Map<String, String> attrMap) {
		
		for(String key: attrMap.keySet()){

			String value = attrMap.get(key);
			
			if (key.equalsIgnoreCase("produces")) {
				produces = value;
			} else if (key.equalsIgnoreCase("produceRate")){
				produceRate = Double.parseDouble(value);
			} else if (key.equalsIgnoreCase("consumes")) {
				consumes = value;
			} else if (key.equalsIgnoreCase("consumeRate")){
				consumeRate = Double.parseDouble(value);
			} else if (key.equalsIgnoreCase("produceStock")){
				produceProductStock = Integer.parseInt(value);
			} else if (key.equalsIgnoreCase("produceStockCapacity")){
				produceProductStockCapacity = Integer.parseInt(value);
			} else if (key.equalsIgnoreCase("consumeStock")){
				consumeProductStock = Integer.parseInt(value);
			} else if (key.equalsIgnoreCase("consumeStockCapacity")){
				consumeProductStockCapacity = Integer.parseInt(value);
			} else if (key.equalsIgnoreCase("satisfaction")){
				satisfaction = Integer.parseInt(value);
			} else if (key.equalsIgnoreCase("money")){
				money = Double.parseDouble(value);
			} else if (key.equalsIgnoreCase("price")){
				price = Double.parseDouble(value);
			}		
		
		}			
		return !(produces.isEmpty() || produceRate ==0|| consumes.isEmpty()
				|| consumeRate == 0 || produceProductStockCapacity == 0 || price == 0);
		

	}
	
	
	
}
