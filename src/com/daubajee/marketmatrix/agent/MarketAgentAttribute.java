package com.daubajee.marketmatrix.agent;

import java.util.Map;

public class MarketAgentAttribute {

	private String produces = "";
	private String consumes = "";
	private double produceRate = 0;
	private double consumeRate = 0;
	private int stock = 0;
	private int stockMaximum = 0;
	private int satisfaction = 0;
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
	public int getStock() {
		return stock;
	}
	public void setStock(int stock) {
		this.stock = stock;
	}
	public int getStockMaximum() {
		return stockMaximum;
	}
	public void setStockMaximum(int stockMaximum) {
		this.stockMaximum = stockMaximum;
	}
	public int getSatisfaction() {
		return satisfaction;
	}
	public void setSatisfaction(int satisfaction) {
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
			} else if (key.equalsIgnoreCase("stock")){
				stock = Integer.parseInt(value);
			}else if (key.equalsIgnoreCase("stockMaximum")){
				stockMaximum = Integer.parseInt(value);
			}else if (key.equalsIgnoreCase("satisfaction")){
				satisfaction = Integer.parseInt(value);
			}else if (key.equalsIgnoreCase("money")){
				money = Double.parseDouble(value);
			}else if (key.equalsIgnoreCase("price")){
				price = Double.parseDouble(value);
			}		
		
		}			
		return !(produces.isEmpty() || produceRate ==0|| consumes.isEmpty()
				|| consumeRate == 0 || stockMaximum == 0 || price == 0);
		

	}
	
	
	
}
