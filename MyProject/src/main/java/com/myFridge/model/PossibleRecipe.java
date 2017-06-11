package com.myFridge.model;

public class PossibleRecipe {
	private int id;
	private String title;
	private String image;
	private String imageType;
	private int usedIngredientCount;
	private int missedIngredientCount;

	public PossibleRecipe(int id, String title, String image, String imageType, int usedIngredientCount,
			int missedIngredientCount) {
		this.id = id;
		this.title = title;
		this.image = image;
		this.imageType = imageType;
		this.usedIngredientCount = usedIngredientCount;
		this.missedIngredientCount = missedIngredientCount;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getImageType() {
		return imageType;
	}

	public void setImageType(String imageType) {
		this.imageType = imageType;
	}

	public int getUsedIngredientCount() {
		return usedIngredientCount;
	}

	public void setUsedIngredientCount(int usedIngredientCount) {
		this.usedIngredientCount = usedIngredientCount;
	}

	public int getMissedIngredientCount() {
		return missedIngredientCount;
	}

	public void setMissedIngredientCount(int missedIngredientCount) {
		this.missedIngredientCount = missedIngredientCount;
	}


}
