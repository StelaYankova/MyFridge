package com.myFridge.DAO;

import java.util.ArrayList;
import java.util.HashMap;
import com.myFridge.exceptions.FridgeException;
import com.myFridge.exceptions.ProductException;
import com.myFridge.exceptions.RecipeException;
import com.myFridge.model.Product;
import com.myFridge.model.User;

public interface IProductDAO {

	ArrayList<String> getAllTypesOfProducts() throws ProductException;

	ArrayList<Product> getFridgeProductsByFridgeCode(String code) throws FridgeException, ProductException;// za
																											// recipebyproducts

	HashMap<String, HashMap<Product, Double>> getProductsByFridgeCode(String fridgeCode) throws ProductException;//

	int findProductIdByName(String productName) throws ProductException;

	Product getProductByName(String pName) throws ProductException;

	boolean doesProductInFridgeExists(String prName, String fridgeCode) throws ProductException, FridgeException;

	double getProductQuantityByName(String name, String fridgeCode) throws ProductException;

	void addNewProductToFridge(User u, String fridgeCode, String p, double quantity)
			throws FridgeException, RecipeException, ProductException;

	void removeProductFromFridge(String fridgeCode, String productName) throws FridgeException, ProductException;

	void removeProductQuantityFromFridge(String fridgeCode, String productName, double quantity)
			throws ProductException, FridgeException;

	void addProductQuantityToFridge(String fridgeCode, String productName, double quantity)
			throws ProductException, FridgeException;

	ArrayList<String> getProductsByTypes(String type) throws ProductException;

}