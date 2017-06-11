package com.myFridge.DAO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.http.client.ClientProtocolException;
import com.google.gson.JsonArray;
import com.myFridge.exceptions.RecipeException;
import com.myFridge.exceptions.UserException;
import com.myFridge.model.FavouriteRec;
import com.myFridge.model.PossibleRecipe;
import com.myFridge.model.Recipe;

public interface IRecipeDAO {

	HashMap<Integer, FavouriteRec> getFavouriteRecipes(String username) throws RecipeException;

	//void setFavRecipeToUser(String username, int recipe);

	//ArrayList<String> seeFavRecipes(String username);

	void addRecipeToFav(String username, int recipe, String recipeName, String image) throws UserException, RecipeException;

	void addRecipeToDB(int recipe, String recipeName, String image) throws RecipeException;

	int getRecipeIdByName(String name) throws RecipeException;

	boolean isRecipeFavFoUser(String username, String recipeName) throws RecipeException;

	void removeRecipeFromFav(String username, int id) throws RecipeException;

	Recipe seeRecipe(int id, HashMap<String, HashMap<Double, String>> products)
			throws ClientProtocolException, IOException;

	JsonArray getRandomRecipes() throws ClientProtocolException, IOException;

	ArrayList<PossibleRecipe> getRecipesByProducts(ArrayList<String> productNames)
			throws ClientProtocolException, IOException;

	int doesRecipeInFavExist(int recipe) throws RecipeException;

	void removeRecFromDB(int recipe) throws RecipeException;

}