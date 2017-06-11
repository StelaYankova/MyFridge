package com.myFridge.model;

import java.util.HashMap;

public class Recipe {

	private String title;
	private String instructions;
	private String image;
	private int readyInMinutes;
	private HashMap<String, HashMap<Double, String>> products;

	public Recipe(String title, String instruction, String image, int readyInMinutes,
			HashMap<String, HashMap<Double, String>> products) {
		this.title = title;
		this.instructions = instruction;
		this.image = image;
		this.readyInMinutes = readyInMinutes;
		this.products = products;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getInstructions() {
		return instructions;
	}

	public void setInstructions(String instruction) {
		this.instructions = instruction;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public int getReadyInMinutes() {
		return readyInMinutes;
	}

	public void setReadyInMinutes(int readyInMinutes) {
		this.readyInMinutes = readyInMinutes;
	}

	public HashMap<String, HashMap<Double, String>> getProducts() {
		return products;
	}

	public void setProducts(HashMap<String, HashMap<Double, String>> products) {
		this.products = products;
	}

}
