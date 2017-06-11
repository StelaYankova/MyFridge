package com.myFridge.model;

public class RandomRec {
	
	private long id;
	private String title;
	private String image;
	private int readyInMinutes;
	
	
	public RandomRec(long id, String title, String image, int readyInMinutes) {
		this.id = id;
		this.title = title;
		this.image = image;
		this.readyInMinutes = readyInMinutes;
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
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
	public int readyInMinutes() {
		return readyInMinutes;
	}
	public void readyInMinutes(int readyInMinutes) {
		this.readyInMinutes = readyInMinutes;
	}
	@Override
	public String toString() {
		return "RandomRec [id=" + id + ", title=" + title + ", image=" + image + ", readyInMinutes=" + readyInMinutes
				+ "]";
	}
}
