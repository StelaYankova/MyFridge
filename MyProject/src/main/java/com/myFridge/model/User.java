package com.myFridge.model;


import java.util.HashMap;
import java.util.HashSet;

public class User {

	private String username;
	private String password;
	private String email;
	private String photo;
	private HashSet<Fridge> fridges;
	private HashMap<Integer, FavouriteRec> favouriteRecipes;
	private String confirmPassword;

	public User(HashSet<Fridge> fridges, HashMap<Integer, FavouriteRec> favouriteRecipes) {
		this.fridges = fridges;
		this.favouriteRecipes = favouriteRecipes;
	}

	public User(String username,HashSet<Fridge> fridges, HashMap<Integer, FavouriteRec> favouriteRecipes) {
		this.username = username;
		this.fridges = fridges;
		this.favouriteRecipes = favouriteRecipes;
	}

	public User(String username, String password, String email, String photo, HashSet<Fridge> fridges, HashMap<Integer, FavouriteRec> favouriteRecipes) {
		
	   
		this.favouriteRecipes = favouriteRecipes;
		this.username = username;
		this.password = password;
		this.email = email;
		this.photo = photo;
		this.fridges = fridges;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public HashSet<Fridge> getFridges() {
		return fridges;
	}

	public void setFridges(HashSet<Fridge> fridges) {
		this.fridges = fridges;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public HashMap<Integer, FavouriteRec> getFavouriteRecipes() {
		return favouriteRecipes;
	}

	public void setFavouriteRecipes(HashMap<Integer, FavouriteRec> favouriteRecipes) {
		this.favouriteRecipes = favouriteRecipes;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

/*	public String getContentOfPhoto() {
		return contentOfPhoto;
	}

	public void setContentOfPhoto(String contentOfPhoto) {
		this.contentOfPhoto = contentOfPhoto;
	}*/

}
