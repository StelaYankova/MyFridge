package com.example.controller;

import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.google.gson.JsonArray;
import com.myFridge.DAO.FridgeDAO;
import com.myFridge.DAO.ProductDAO;
import com.myFridge.DAO.RecipeDAO;
import com.myFridge.exceptions.FridgeException;
import com.myFridge.exceptions.ProductException;
import com.myFridge.model.Fridge;
import com.myFridge.model.PossibleRecipe;
import com.myFridge.model.Product;
import com.myFridge.model.Recipe;
import com.myFridge.model.User;

@Controller
public class RecipeController {

	private static final int TABLESPOON_TO_GRAMS = 15;
	private static final int LB_TO_GRAMS = 454;
	private static final double OZ_TO_GRAMMS = 0.035274;
	private static final double CUP_TO_MILLILITERS = 236.58;

	public static boolean isSessionValid(HttpServletRequest request, HttpServletResponse response) {
		if (request.getSession().getAttribute("user") == null) {
			return false;
		}
		return true;
	}

	@RequestMapping(value = "seeFavouriteRecipes", method = { RequestMethod.GET, RequestMethod.POST })
	public String getFavRec(HttpServletRequest req, HttpServletResponse resp) {
		if (!isSessionValid(req, resp)) {
			return "redirect:./index";
		}
		return "favouriteRecipes";
	}

	@RequestMapping(value = "seeRecipe", method = RequestMethod.GET)
	public String seeRecipe(HttpServletRequest req, HttpServletResponse resp) {
		if (!isSessionValid(req, resp)) {
			return "redirect:./index";
		}
		if (isIdOfRecipeValid(req.getParameter("id").toString())) {
			int id = Integer.parseInt(req.getParameter("id").toString());
			HashMap<String, HashMap<Double, String>> products = new HashMap<>();
			Recipe recipe;
			try {
				recipe = RecipeDAO.getInstance().seeRecipe(id, products);
				req.getSession().setAttribute("recipe", recipe);
				ArrayList<String> notEnogh = new ArrayList<>();
				if (req.getSession().getAttribute("fridge") != null) {
					Fridge fridge = (Fridge) req.getSession().getAttribute("fridge");
					String fridgeCode = fridge.getFridgeCode();
					fridge.setProducts(ProductDAO.getInstance().getProductsByFridgeCode(fridgeCode));
					HashMap<String, Double> productsToRemove = new HashMap<>();
					for (Entry<String, HashMap<Double, String>> productsInRecipe : products.entrySet()) {
						for (Entry<String, HashMap<Product, Double>> entryProductsInFridge : fridge.getProducts()
								.entrySet()) {
							for (Entry<Product, Double> productsInfridge : entryProductsInFridge.getValue()
									.entrySet()) {
								if (productsInRecipe.getKey().contains(productsInfridge.getKey().getName())) {
									for (Entry<Double, String> prCount : productsInRecipe.getValue().entrySet()) {
										double countNotRounded = prCount.getKey();
										String prUnit = prCount.getValue();
										if (prUnit.equals("cup") || prUnit.equals("cups")) {
											countNotRounded = CUP_TO_MILLILITERS * countNotRounded;
										} else if (prUnit.equals("oz")) {
											countNotRounded = countNotRounded / OZ_TO_GRAMMS;
										} else if (prUnit.equals("lb") || prUnit.equals("lbs")) {
											countNotRounded = countNotRounded * LB_TO_GRAMS;
										} else if (prUnit.equals("tablespoons") || prUnit.equals("tsp")) {
											countNotRounded = countNotRounded * TABLESPOON_TO_GRAMS;
										}
										double count = (double) Math.round(countNotRounded * 100) / 100;
										productsToRemove.put(productsInfridge.getKey().getName(), count);

										if (ProductDAO.getInstance().getProductQuantityByName(
												productsInfridge.getKey().getName(), fridgeCode) < count) {
											notEnogh.add(productsInfridge.getKey().getName());
										}
									}
								}
							}
						}
					}
					req.getSession().setAttribute("productsToRemove", productsToRemove);
					req.setAttribute("notEnough", notEnogh);
					resp.setStatus(200);
				}

			} catch (IOException | ProductException e) {
				e.printStackTrace(new PrintStream(System.err));
				return "exceptions";
			}

		} else {
			req.setAttribute("unknownRecipe", "1");

		}
		return "recipe";
	}

	private boolean isIdOfRecipeValid(String string) {
		boolean valid = true;
		if (string.length() > 10) {
			valid = false;
			return valid;
		}
		for (int i = 0; i < string.length(); i++) {
			if (!(((int) string.charAt(i) > 47 && (int) string.charAt(i) < 58))) {
				valid = false;
				break;
			}
		}
		return valid;
	}

	@RequestMapping(value = "cookNow", method = RequestMethod.POST)
	public String cookNow(HttpServletRequest req, HttpServletResponse resp, ArrayList<String> productsToDecrease) {
		if (!isSessionValid(req, resp)) {
			return "redirect:./index";
		}
		Fridge fridge = (Fridge) req.getSession().getAttribute("fridge");
		String fridgeCode = fridge.getFridgeCode();
		HashMap<String, Double> productsToRemove = (HashMap<String, Double>) req.getSession()
				.getAttribute("productsToRemove");
		for (Entry<String, Double> entry : productsToRemove.entrySet()) {
			try {
				if (ProductDAO.getInstance().getProductQuantityByName(entry.getKey(), fridgeCode) <= entry.getValue()) {
					ProductDAO.getInstance().removeProductFromFridge(fridgeCode, entry.getKey());
				} else {
					ProductDAO.getInstance().removeProductQuantityFromFridge(fridgeCode, entry.getKey(),
							entry.getValue());
				}
			} catch (ProductException | FridgeException e) {
				e.printStackTrace(new PrintStream(System.err));
				return "exceptions";
			}
		}
		req.getSession().setAttribute("success", "1");

		return "redirect:./recipeByProducts";
	}

	@RequestMapping(value = "randomRecipes", method = RequestMethod.GET)
	public String getRandomRecipes(HttpServletRequest req, HttpServletResponse resp) {
		if (!isSessionValid(req, resp)) {
			return "redirect:./index";
		}

		JsonArray jsArray;
		try {
			jsArray = RecipeDAO.getInstance().getRandomRecipes();
			resp.setContentType("application/json");
			resp.getWriter().write(jsArray.toString());
		} catch (IOException e) {
			e.printStackTrace(new PrintStream(System.err));
			return "exceptions";
		}
		return null;

	}

	@RequestMapping(value = "recipeByProducts", method = RequestMethod.GET)
	public String getRecipe(HttpServletRequest req, HttpServletResponse resp) {
		if (!isSessionValid(req, resp)) {
			return "redirect:./index";
		}
		Fridge fridge = null;
		String fridgeCode = null;
		if (req.getParameter("fridgeCode") != null) {
			fridgeCode = req.getParameter("fridgeCode");
			User u = (User) req.getSession().getAttribute("user");

			try {
				fridge = FridgeDAO.getInstance().getFridgeByCode(u.getUsername(), fridgeCode);
			} catch (FridgeException e) {
				e.printStackTrace(new PrintStream(System.err));
				return "exceptions";
			}
			if (fridge == null) {
				req.setAttribute("invalid", "1");
			} else {
				req.getSession().setAttribute("fridge", fridge);
			}
		} else {
			fridge = (Fridge) req.getSession().getAttribute("fridge");

			fridgeCode = fridge.getFridgeCode();
			if (fridge == null) {
				req.setAttribute("invalid", "1");
			}
		}
		ArrayList<Product> products = null;
		try {
			products = ProductDAO.getInstance().getFridgeProductsByFridgeCode(fridgeCode);
		} catch (FridgeException | ProductException e) {
			e.printStackTrace(new PrintStream(System.err));
			return "exceptions";
		}
		ArrayList<String> productNames = new ArrayList<>();
		for (Product p : products) {
			productNames.add(p.getName());
			productNames.add("%2C");
		}
		ArrayList<PossibleRecipe> posRecipes = new ArrayList<>();
		try {
			posRecipes = RecipeDAO.getInstance().getRecipesByProducts(productNames);
			req.setAttribute("possibleRecipes", posRecipes);
			resp.setStatus(200);
		} catch (IOException e) {
			e.printStackTrace(new PrintStream(System.err));
			return "exceptions";
		}
		return "possibleRecipes";
	}
}
