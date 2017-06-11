package com.myFridge.model;

import java.util.HashMap;

public class Fridge {

	private String name;
	private HashMap<String, HashMap<Product, Double>> products;
	private String fridgeCode;

	public Fridge(HashMap<String, HashMap<Product, Double>> products) {
		this.products = products;

	}

	public Fridge(String name, String fridgeCode, HashMap<String, HashMap<Product, Double>> products) {
		this.name = name;
		this.products = products;
		this.fridgeCode = fridgeCode;
	}

	public HashMap<String, HashMap<Product, Double>> getProducts() {
		return products;
	}

	public void setProducts(HashMap<String, HashMap<Product, Double>> products) {
		this.products = products;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFridgeCode() {
		return fridgeCode;
	}

	public void setFridgeCode(String fridgeCode) {
		this.fridgeCode = fridgeCode;
	}

}
