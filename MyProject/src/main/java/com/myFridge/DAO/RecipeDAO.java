package com.myFridge.DAO;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.myFridge.DB.DBManager;
import com.myFridge.exceptions.RecipeException;
import com.myFridge.model.FavouriteRec;
import com.myFridge.model.PossibleRecipe;
import com.myFridge.model.Recipe;

public class RecipeDAO implements IRecipeDAO {

	private static IRecipeDAO instance;
	private DBManager manager;

	private RecipeDAO() {
		setManager(DBManager.getInstance());
	}

	public static IRecipeDAO getInstance() {
		if (instance == null)
			instance = new RecipeDAO();
		return instance;
	}

	public void setManager(DBManager manager) {
		this.manager = manager;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.myFridge.DAO.IRecipeDAO#getFavouriteRecipes(java.lang.String)
	 */
	@Override
	public HashMap<Integer, FavouriteRec> getFavouriteRecipes(String username) throws RecipeException {
		HashMap<Integer, FavouriteRec> rec = new HashMap<Integer, FavouriteRec>();
		Connection con = null;
		con = manager.getConnection();
		try {
			PreparedStatement ps = con.prepareStatement(
					"SELECT id,rec_name,image FROM my_fridge.fav_recipes JOIN my_fridge.user_fav_recipes ON (recipe_id = id) WHERE user_username = ?;");
			ps.setString(1, username);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				FavouriteRec x = new FavouriteRec(rs.getString(2), rs.getString(3));
				rec.put(rs.getInt(1), x);
			}
		} catch (SQLException e) {
			new RecipeException("Something went wrong with getting users favourite recipes...", e);
		}
		return rec;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.myFridge.DAO.IRecipeDAO#addRecipeToFav(java.lang.String, int,
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public void addRecipeToFav(String username, int recipe, String recipeName, String image) throws RecipeException {
		if (RecipeDAO.getInstance().doesRecipeInFavExist(recipe) <= 0) {
			RecipeDAO.getInstance().addRecipeToDB(recipe, recipeName, image);
		}
		Connection con = null;
		con = manager.getConnection();
		PreparedStatement ps;
		try {
			ps = con.prepareStatement("INSERT INTO my_fridge.user_fav_recipes (user_username, recipe_id) VALUES (?,?)");
			ps.setString(1, username);
			ps.setInt(2, recipe);
			ps.execute();
		} catch (SQLException e) {
			new RecipeException("Something went wrong with setting favourite recipe to user...", e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.myFridge.DAO.IRecipeDAO#addRecipeToDB(int, java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public void addRecipeToDB(int recipe, String recipeName, String image) throws RecipeException {
		Connection con = null;
		con = manager.getConnection();
		try {
			PreparedStatement ps = con
					.prepareStatement("INSERT INTO my_fridge.fav_recipes (id, rec_name,image) VALUE (?,?,?);");
			ps.setInt(1, recipe);
			ps.setString(2, recipeName);
			ps.setString(3, image);
			ps.execute();
		} catch (SQLException e) {
			new RecipeException("Something went wrong with adding new recipe to the data base...", e);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.myFridge.DAO.IRecipeDAO#getRecipeIdByName(java.lang.String)
	 */
	@Override
	public int getRecipeIdByName(String name) throws RecipeException {
		Connection con = null;
		con = manager.getConnection();
		try {
			PreparedStatement ps = con.prepareStatement("SELECT id FROM my_fridge.fav_recipes WHERE rec_name = ?;");
			ps.setString(1, name);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return rs.getInt(1);
			}
		} catch (SQLException e) {
			new RecipeException("Something went wrong with getting recipe's id by name...", e);
		}
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.myFridge.DAO.IRecipeDAO#isRecipeFavFoUser(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public boolean isRecipeFavFoUser(String username, String recipeName) throws RecipeException {
		int id = getRecipeIdByName(recipeName);
		Connection con = null;
		con = manager.getConnection();
		try {
			PreparedStatement ps = con.prepareStatement(
					"SELECT * FROM my_fridge.user_fav_recipes WHERE user_username = ? AND recipe_id = ?;");
			ps.setString(1, username);
			ps.setInt(2, id);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return true;
			}
		} catch (SQLException e) {
			new RecipeException("Something went wrong with ckecking if recipe is in user's favourite...", e);
		}
		return false;
	}

	@Override
	public void removeRecFromDB(int recipe) throws RecipeException {
		Connection con = null;
		con = manager.getConnection();
		try {
			PreparedStatement ps = con.prepareStatement("DELETE FROM my_fridge.fav_recipes WHERE id = ?;");
			ps.setInt(1, recipe);
			ps.execute();
		} catch (SQLException e) {
			throw new RecipeException("Something went wrong with removing recipe from favourites...", e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.myFridge.DAO.IRecipeDAO#removeRecipeFromFav(java.lang.String,
	 * int)
	 */
	@Override
	public void removeRecipeFromFav(String username, int id) throws RecipeException {
		Connection con = null;
		con = manager.getConnection();
		try {
			PreparedStatement ps = con.prepareStatement(
					"DELETE FROM my_fridge.user_fav_recipes WHERE user_username = ? AND recipe_id = ?;");
			ps.setString(1, username);
			ps.setInt(2, id);
			ps.execute();
		} catch (SQLException e) {
			throw new RecipeException("Something went wrong with removing recipe from favourites...", e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.myFridge.DAO.IRecipeDAO#seeRecipe(int, java.util.HashMap)
	 */
	@Override
	public Recipe seeRecipe(int id, HashMap<String, HashMap<Double, String>> products)
			throws ClientProtocolException, IOException {
		HttpClient cl = HttpClientBuilder.create().build();
		String address = "https://spoonacular-recipe-food-nutrition-v1.p.mashape.com/recipes/" + id
				+ "/information?includeNutrition=false";
		address.replace(" ", "-");
		HttpUriRequest req1 = new HttpGet(address);
		// remove
		req1.setHeader("X-Mashape-Key", "//putKeyHere");
		req1.setHeader("Accept", "application/json");
		HttpResponse answer;
		answer = cl.execute(req1);
		String data = EntityUtils.toString(answer.getEntity());

		JsonObject obj = new JsonParser().parse(data).getAsJsonObject();
		String title = obj.get("title").toString();
		String image = obj.get("image").toString();
		String instructions = obj.get("instructions").toString();
		if (instructions.equals("null")) {
			instructions = "There are no available instructions for this recipe.";
		}
		int readyInMinutes = obj.get("readyInMinutes").getAsInt();
		JsonArray ingredients = obj.get("extendedIngredients").getAsJsonArray();
		for (int i = 0; i < ingredients.size(); i++) {
			JsonObject r = new JsonObject();
			r = ingredients.get(i).getAsJsonObject();
			String name = r.get("name").getAsString();
			double amount = r.get("amount").getAsDouble();
			String unit = r.get("unit").getAsString();
			products.put(name, new HashMap<>());
			products.get(name).put(amount, unit);
		}
		return new Recipe(title, instructions, image, readyInMinutes, products);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.myFridge.DAO.IRecipeDAO#getRandomRecipes()
	 */
	@Override
	public JsonArray getRandomRecipes() throws ClientProtocolException, IOException {
		JsonArray jsArray = new JsonArray();
		HttpClient cl = HttpClientBuilder.create().build();
		String address = "https://spoonacular-recipe-food-nutrition-v1.p.mashape.com/recipes/random?limitLicense=false&number=4";
		HttpUriRequest req1 = new HttpGet(address);
		// remove
		req1.setHeader("X-Mashape-Key", "//putKeyHere");
		req1.setHeader("Accept", "application/json");
		HttpResponse answer = null;
		answer = cl.execute(req1);
		String data = EntityUtils.toString(answer.getEntity());
		JsonObject obj = new JsonParser().parse(data).getAsJsonObject();
		JsonArray arr = obj.get("recipes").getAsJsonArray();
		for (int i = 0; i < 4; i++) {
			JsonObject obj1 = new JsonObject();
			JsonObject randRec = new JsonObject();
			obj1 = arr.get(i).getAsJsonObject();
			int id = obj1.get("id").getAsInt();
			String title = obj1.get("title").getAsString();
			String image = obj1.get("image").getAsString();
			int readyInMinutes = obj1.get("readyInMinutes").getAsInt();
			randRec.addProperty("id", id);
			randRec.addProperty("title", title);
			randRec.addProperty("image", image);
			randRec.addProperty("readyInMinutes", readyInMinutes);
			jsArray.add(randRec);
		}
		return jsArray;
	}

	@Override
	public int doesRecipeInFavExist(int recipe) throws RecipeException {
		Connection con = null;
		con = manager.getConnection();
		try {
			PreparedStatement ps = con.prepareStatement("SELECT id FROM my_fridge.fav_recipes WHERE id = ?");
			ps.setInt(1, recipe);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return rs.getInt(1);
			}
			return 0;
		} catch (SQLException e) {
			throw new RecipeException("Something went checking if recipe is in user's favourite...", e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.myFridge.DAO.IRecipeDAO#getRecipesByProducts(java.util.ArrayList)
	 */
	@Override
	public ArrayList<PossibleRecipe> getRecipesByProducts(ArrayList<String> productNames)
			throws ClientProtocolException, IOException {
		ArrayList<PossibleRecipe> posRecipes = new ArrayList<>();
		String all = String.join("", productNames);
		HttpClient cl = HttpClientBuilder.create().build();
		String address = "https://spoonacular-recipe-food-nutrition-v1.p.mashape.com/recipes/findByIngredients?fillIngredients=false&ingredients="
				+ all + "&limitLicense=false&number=50&ranking=1";
		String addressReplaced = address.replace(" ", "-");
		HttpUriRequest req1 = new HttpGet(addressReplaced);
		// remove
		req1.setHeader("X-Mashape-Key", "//putKeyHere");
		req1.setHeader("Accept", "application/json");
		HttpResponse answer;
		answer = cl.execute(req1);
		String data = EntityUtils.toString(answer.getEntity());
		JsonArray arr = new JsonParser().parse(data).getAsJsonArray();
		for (int i = 0; i < arr.size(); i++) {
			JsonObject obj = new JsonObject();
			obj = arr.get(i).getAsJsonObject();
			int id = obj.get("id").getAsInt();
			String title = obj.get("title").getAsString();
			String image = obj.get("image").getAsString();
			String imageType = obj.get("imageType").getAsString();
			int usedIngredientCount = obj.get("usedIngredientCount").getAsInt();
			int missedIngredientCount = obj.get("missedIngredientCount").getAsInt();
			posRecipes.add(new PossibleRecipe(id, title, image, imageType, usedIngredientCount, missedIngredientCount));
		}
		return posRecipes;
	}

}
