package com.myFridge.jUnitTests;

import static org.junit.Assert.assertEquals;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import com.myFridge.DAO.FridgeDAO;
import com.myFridge.DAO.ProductDAO;
import com.myFridge.DAO.RecipeDAO;
import com.myFridge.DAO.UserDAO;
import com.myFridge.exceptions.FridgeException;
import com.myFridge.exceptions.ProductException;
import com.myFridge.exceptions.RecipeException;
import com.myFridge.exceptions.UserException;
import com.myFridge.model.FavouriteRec;
import com.myFridge.model.Fridge;
import com.myFridge.model.Product;
import com.myFridge.model.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:Beans.xml" })
@Transactional
@ComponentScan
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestDAOs extends AbstractJUnit4SpringContextTests {

	File f = new File(getClass().getClassLoader().getResource("Beans.xml").getFile());
	ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("file:src/main/resources/Beans.xml");
	User u = context.getBean("user3", User.class);
	Fridge fridge = context.getBean("fridge1", Fridge.class);
	Product p1 = context.getBean("product1", Product.class);
	Product p2 = context.getBean("product2", Product.class);
	Product p3 = context.getBean("product3", Product.class);

	@Test
	public void test01DoesUsernameNotExists() {
		System.out.println("Start testDoesUsernameNotExists");
		boolean answer = false;
		try {
			answer = UserDAO.getInstance().doesUsernameNotExist(u.getUsername());
			System.out.println(answer);
			assertEquals(true, answer);
		} catch (UserException e) {
			assert ("User Exception occured") != null;
		}
	}

	@Test
	public void test02CreateUser() {
		try {
			UserDAO.getInstance().createNewUser(u);
			User u1 = UserDAO.getInstance().getUserByUsername(u.getUsername());
			assertEquals(u1.getUsername(), u.getUsername());
			boolean validData = UserDAO.getInstance().areUsernamePassValid(u.getUsername(), u.getPassword());
			assertEquals(true, validData);
		} catch (UserException e) {
			assert ("User Exception occured") != null;
		}
	}

	@Test
	public void test03UpdateUser() {
		try {
			User userForUpdate = new User("test", "2345", "test@st",
					"/Users/Stela/Desktop/spring/MyProject-3/src/main/webapp/static/profilePics/kkkk_profilePic.jpeg",
					null, null);
			UserDAO.getInstance().updateUserInfo(userForUpdate);
			User testUserFromDB = UserDAO.getInstance().getUserByUsername(userForUpdate.getUsername());
			assertEquals(testUserFromDB.getUsername(), userForUpdate.getUsername());
			assertEquals(testUserFromDB.getPassword(), userForUpdate.getPassword());
			assertEquals(testUserFromDB.getEmail(), userForUpdate.getEmail());
			assertEquals(testUserFromDB.getPhoto(), userForUpdate.getPhoto());
		} catch (UserException e) {
			assert ("User Exception occured") != null;
		}
	}

	@Test
	public void test04DoesFridgeExist() {
		boolean doesFridgeExist;
		try {
			doesFridgeExist = FridgeDAO.getInstance().doesFridgeExist(fridge);
			assertEquals(false, doesFridgeExist);
		} catch (FridgeException e) {
			assert ("Fridge Exception occured") != null;
		}
	}

	@Test
	public void test05DoesUserAlreadyHaveFridge() {
		boolean doesUserAlreadyHaveFridge;
		try {
			doesUserAlreadyHaveFridge = FridgeDAO.getInstance().doesUserAlreadyHaveFridge(u, fridge);
			assertEquals(false, doesUserAlreadyHaveFridge);
		} catch (FridgeException e) {
			assert ("User Exception occured") != null;
		}
	}

	@Test
	public void test06CreatingAFridge() {

		try {
			int id = FridgeDAO.getInstance().createNewFridge(u, fridge);
			Fridge f = FridgeDAO.getInstance().getFridgeByCode(u.getUsername(), fridge.getFridgeCode());
			int id1 = FridgeDAO.getInstance().getFridgeID(f.getFridgeCode());
			;
			assertEquals(id, id1);
		} catch (FridgeException e) {
			assert ("Fridge Exception occured") != null;
		}
	}

	@Test
	public void test07GetFridgesByUser() {
		boolean isFridgeInUsersSet = false;
		HashSet<Fridge> currFridges;
		try {
			currFridges = FridgeDAO.getInstance().getFridgesByUser(u);
			for (Fridge f1 : currFridges) {
				if (f1.getFridgeCode().equals(fridge.getFridgeCode())) {
					isFridgeInUsersSet = true;
				}
			}
			assertEquals(true, isFridgeInUsersSet);
		} catch (FridgeException e) {
			assert ("Fridge Exception occured") != null;
		}
	}

	@Test
	public void test08DoesFridgeBelongToMoreThanOneUser() throws FridgeException {
		boolean doesFridgeBelongToMoreThanOneUser = FridgeDAO.getInstance().doesTheFridgeBelongToMoreThanOneUser(
				u.getUsername(), FridgeDAO.getInstance().getFridgeID(fridge.getFridgeCode()));
		assertEquals(false, doesFridgeBelongToMoreThanOneUser);

	}

	@Test
	public void test09GetAllTypesOfProducts() {
		try {
			ArrayList<String> types = ProductDAO.getInstance().getAllTypesOfProducts();
			ArrayList<String> neededTypes = new ArrayList<>();
			neededTypes.add("Fruits");
			neededTypes.add("Dairy products");
			neededTypes.add("Meat");
			neededTypes.add("Others");
			neededTypes.add("Vegetables");
			boolean valid = false;
			if (types.containsAll(neededTypes)) {
				valid = true;
			}
			assertEquals(true, valid);
		} catch (ProductException e) {
			assert ("Product Exception occured") != null;
		}
	}

	@Test
	public void test10GetFridgeProductsByFridgeCode() {// for recipe
		try {
			ArrayList<Product> products = ProductDAO.getInstance()
					.getFridgeProductsByFridgeCode(fridge.getFridgeCode());
			ArrayList<String> neededProducts = new ArrayList<>();
			ArrayList<String> productNames = new ArrayList<>();
			for (Product p : products) {
				productNames.add(p.getName());
			}
			boolean valid = false;
			if (productNames.containsAll(neededProducts)) {
				valid = true;
			}
			assertEquals(true, valid);
		} catch (FridgeException | ProductException e) {
			assert ("Fridge Exception occured") != null;
		}
	}

	@Test
	public void test11DoesRecipeInFavExist() {
		boolean valid = false;
		try {
			if (RecipeDAO.getInstance().doesRecipeInFavExist(7877) != 0) {
				valid = true;
			}
		} catch (RecipeException e) {
			assert ("Recipe Exception occured") != null;
		}
		assertEquals(true, valid);
	}

	@Test
	public void test12IsRecipeFavToUser() {
		boolean valid = false;
		try {
			if (RecipeDAO.getInstance().isRecipeFavFoUser(u.getUsername(), "Tuna, Tomato, Bean and Basil Salad")) {
				valid = true;
			}
		} catch (RecipeException e) {
			assert ("Recipe Exception occured") != null;
		}
		assertEquals(false, valid);
	}

	@Test
	public void test13AddRecipeToFav() {
		boolean valid = false;
		try {
			RecipeDAO.getInstance().addRecipeToFav(u.getUsername(), 7877, "Tuna, Tomato, Bean and Basil Salad",
					"https://spoonacular.com/recipeImages/tuna-tomato-bean-and-basil-salad-7877.jpg");
			if (RecipeDAO.getInstance().isRecipeFavFoUser(u.getUsername(), "Tuna, Tomato, Bean and Basil Salad")) {
				valid = true;
			}
		} catch (UserException | RecipeException e) {
			assert ("Recipe Exception occured") != null;
		}
		assertEquals(true, valid);
	}

	@Test
	public void test14GetFavouriteRecipes() {
		HashMap<Integer, FavouriteRec> favourite = null;
		try {
			favourite = RecipeDAO.getInstance().getFavouriteRecipes(u.getUsername());
		} catch (RecipeException e) {
			assert ("Recipe Exception occured") != null;
		}
		boolean valid = false;
		if (favourite.size() == 1) {
			if (favourite.containsKey(7877)) {
				if (favourite.get(7877).getName().equals("Tuna, Tomato, Bean and Basil Salad")
						&& favourite.get(7877).getImage().equals(
								"https://spoonacular.com/recipeImages/tuna-tomato-bean-and-basil-salad-7877.jpg")) {
					valid = true;
				}
			}
		}
		assertEquals(true, valid);
	}

	@Test
	public void test15AddRecipeToDB() {
		int needed = 1002121212;
		boolean valid = false;
		try {
			RecipeDAO.getInstance().addRecipeToDB(needed, "testRecipe", "test_rec.jpeg");
			if (needed == RecipeDAO.getInstance().doesRecipeInFavExist(needed)) {
				valid = true;
			}
		} catch (RecipeException e1) {
			assert ("Recipe Exception occured") != null;
		}

		assertEquals(true, valid);
	}

	@Test
	public void test16RemoveRecFromDB() {
		int needed = 1002121212;
		boolean valid = false;
		try {
			RecipeDAO.getInstance().removeRecFromDB(needed);
			if (needed == RecipeDAO.getInstance().doesRecipeInFavExist(needed)) {
				valid = true;
			}
		} catch (RecipeException e) {
			assert ("Recipe Exception occured") != null;
		}
		assertEquals(false, valid);
	}

	@Test
	public void test17GetRecipeIdByName() {
		boolean valid = false;
		int needed = 7877;
		try {
			if (needed == RecipeDAO.getInstance().getRecipeIdByName("Tuna, Tomato, Bean and Basil Salad")) {
				valid = true;
			}
		} catch (RecipeException e) {
			assert ("Recipe Exception occured") != null;
		}
		assertEquals(true, valid);
	}

	@Test
	public void test18RemoveRecFromFav() {
		boolean valid = false;
		int needed = 7877;
		try {
			RecipeDAO.getInstance().removeRecipeFromFav(u.getUsername(), needed);
			if (RecipeDAO.getInstance().isRecipeFavFoUser(u.getUsername(), "Tuna, Tomato, Bean and Basil Salad")) {
				valid = true;
			}
		} catch (RecipeException e) {
			assert ("Recipe Exception occured") != null;
		}
		assertEquals(false, valid);
	}

	@Test
	public void test18GetProductsByFridgeCode() {
		for (Entry<String, HashMap<Product, Double>> entry : fridge.getProducts().entrySet()) {
			for (Entry<Product, Double> products : entry.getValue().entrySet()) {
				String productName = products.getKey().getName();
				double number = products.getValue();
				try {
					ProductDAO.getInstance().addNewProductToFridge(u, fridge.getFridgeCode(), productName, number);
				} catch (FridgeException | RecipeException | ProductException e) {
					assert ("Product Exception occured") != null;
				}
			}
		}
	}

	@Test
	public void test19GetProductsByFridgeCode() {
		try {
			HashMap<String, HashMap<Product, Double>> products = ProductDAO.getInstance()
					.getProductsByFridgeCode(fridge.getFridgeCode());
			HashMap<String, HashMap<Product, Double>> productsNeeded = new HashMap<>();
			productsNeeded.put("Fruits", new HashMap<>());
			productsNeeded.get("Fruits").put(new Product("apple", "apple.jpeg", "gramms"), (double) 62);
			productsNeeded.get("Fruits").put(new Product("mango", "mango.jpeg", "gramms"), (double) 55);
			productsNeeded.put("Meat", new HashMap<>());
			productsNeeded.get("Meat").put(new Product("beef", "beef.jpeg", "gramms"), (double) 60);
			boolean valid = false;
			if (products.toString().equals(productsNeeded.toString())) {
				valid = true;
			}
			assertEquals(true, valid);
		} catch (ProductException e) {
			assert ("Product Exception occured") != null;
		}
	}

	@Test
	public void test20findProductIdByName() {
		int idNeeded = 11;
		boolean valid = false;
		try {
			if (ProductDAO.getInstance().findProductIdByName("apple") == idNeeded) {
				valid = true;
			}
		} catch (ProductException e) {
			assert ("Product Exception occured") != null;
		}
		assertEquals(true, valid);
	}

	@Test
	public void test21GetProductByName() throws FridgeException {
		Product needed = new Product("apple", "apple.jpeg", "gramms");
		boolean valid = false;
		try {
			if (needed.equals(ProductDAO.getInstance().getProductByName("apple"))) {
				valid = true;
			}
		} catch (ProductException e) {
			assert ("Product Exception occured") != null;
		}
		assertEquals(true, valid);
	}

	@Test
	public void test22DoesProductInFridgeExists() {
		boolean needed = true;
		boolean valid = false;
		try {
			if (needed == ProductDAO.getInstance().doesProductInFridgeExists("apple", fridge.getFridgeCode())) {
				valid = true;
			}
		} catch (ProductException | FridgeException e) {
			assert ("Product or Fridge Exception occured") != null;
		}
		assertEquals(true, valid);
	}

	@Test
	public void test23GetProductQuantityByName() {
		double needed = 62;
		boolean valid = false;
		try {
			if (needed == ProductDAO.getInstance().getProductQuantityByName("apple", fridge.getFridgeCode())) {
				valid = true;
			}
		} catch (ProductException e) {
			assert ("Product Exception occured") != null;
		}
		assertEquals(true, valid);
	}

	@Test
	public void test24AddProductQuantityToFridge() {
		double needed = 65;
		boolean valid = false;
		try {
			ProductDAO.getInstance().addProductQuantityToFridge(fridge.getFridgeCode(), "apple", 3);
			if (needed == ProductDAO.getInstance().getProductQuantityByName("apple", fridge.getFridgeCode())) {
				valid = true;
			}
		} catch (ProductException | FridgeException e1) {
			assert ("Product Exception occured") != null;
		}
		assertEquals(true, valid);
	}

	@Test
	public void test25RemoveProductQuantityFromFridge() {
		double needed = 62;
		boolean valid = false;
		try {
			ProductDAO.getInstance().removeProductQuantityFromFridge(fridge.getFridgeCode(), "apple", 3);
			if (needed == ProductDAO.getInstance().getProductQuantityByName("apple", fridge.getFridgeCode())) {
				valid = true;
			}
		} catch (ProductException | FridgeException e1) {
			assert ("Product or Fridge Exception occured") != null;
		}
		assertEquals(true, valid);
	}

	@Test
	public void test26GetAllAvailableProducts() {
		int needed = 8;
		boolean valid = false;
		try {
			if (needed == ProductDAO.getInstance().getProductsByTypes("Fruits").size()) {
				valid = true;
			}
		} catch (ProductException e) {
			assert ("Product Exception occured") != null;
		}

		assertEquals(true, valid);
	}

	@Test
	public void test27GetRandomRecipes() {
		int needed = 4;
		boolean valid = false;
		try {
			if (needed == RecipeDAO.getInstance().getRandomRecipes().size()) {
				valid = true;
			}
		} catch (IOException e) {
			assert ("IO Exception occured") != null;
		}
		assertEquals(true, valid);
	}

	@Test
	public void test28RemoveProductFromFridge() {
		boolean needed = false;
		boolean valid = false;
		try {
			ProductDAO.getInstance().removeProductFromFridge(fridge.getFridgeCode(), "apple");
			if (needed == ProductDAO.getInstance().doesProductInFridgeExists("apple", fridge.getFridgeCode())) {
				valid = true;
			}
		} catch (ProductException | FridgeException e1) {
			assert ("Product or Fridge Exception occured") != null;
		}
		assertEquals(true, valid);
	}

	@Test
	public void test29RemoveFridge() {
		try {
			FridgeDAO.getInstance().removeFridge(u.getUsername(),
					FridgeDAO.getInstance().getFridgeID(fridge.getFridgeCode()));
			assertEquals(null, FridgeDAO.getInstance().getFridgeByCode(u.getUsername(), fridge.getFridgeCode()));
		} catch (FridgeException e) {
			assert ("Fridge Exception occured") != null;
		}
	}

	@Test
	public void test31DeleteUser() {
		try {
			User userToDelete = UserDAO.getInstance().getUserByUsername("test");
			UserDAO.getInstance().deleteUser(userToDelete);
			boolean validData = UserDAO.getInstance().areUsernamePassValid(u.getUsername(), u.getPassword());
			assertEquals(false, validData);
		} catch (UserException e) {
			assert ("User Exception occured") != null;
		} catch (FridgeException e) {
			assert ("Fridge Exception occured") != null;
		}
	}

}
