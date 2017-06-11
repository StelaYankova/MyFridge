package com.myFridge.annotations;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class FridgeForm {

	@NotNull
	@InvalidSymbolsFridge(message = "You have invalid symbols in the name of the fridge - you can use letters, numbers and space")
	@Size(min = 4, max = 20, message = "name - between 4 and 20 characters")
	private String name;

	@NotNull
	@InvalidSymbols(message = "You have invalid symbols in your code - you can use letters and numbers")
	@Size(min = 4, max = 12, message = "code - between 5 and 12 characters")
	private String fridgeCode;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name.trim();
	}

	public String getFridgeCode() {
		return fridgeCode;
	}

	public void setFridgeCode(String fridgeCode) {
		this.fridgeCode = fridgeCode;
	}
}
