package com.myFridge.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import com.myFridge.DB.DBManager;
import com.myFridge.exceptions.FridgeException;
import com.myFridge.exceptions.ProductException;
import com.myFridge.exceptions.RecipeException;
import com.myFridge.model.Product;
import com.myFridge.model.User;

public class ProductDAO implements IProductDAO {

	private static ProductDAO instance;
	private DBManager manager;

	private ProductDAO() {
		setManager(DBManager.getInstance());
	}

	public static ProductDAO getInstance() {
		if (instance == null)
			instance = new ProductDAO();
		return instance;
	}

	public void setManager(DBManager manager) {
		this.manager = manager;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.myFridge.DAO.IProductDAO#addNewProductToFridge(com.myFridge.model.
	 * User, java.lang.String, java.lang.String, double)
	 */
	@Override
	public void addNewProductToFridge(User u, String fridgeCode, String p, double quantity)
			throws FridgeException, RecipeException, ProductException {
		Connection con = null;
		con = manager.getConnection();
		int prID = ProductDAO.getInstance().getProductByID(p);
		int fridgeID = FridgeDAO.getInstance().getFridgeID(fridgeCode);
		int numOfWantedProdInFridge = ProductDAO.getInstance().thereIsSuchProductInFridge(fridgeID, prID);
		if (numOfWantedProdInFridge == 0) {
			try {
				PreparedStatement ps = con
						.prepareStatement("INSERT INTO my_fridge.fridge_has_products VALUES (?,?,?);");
				ps.setInt(1, fridgeID);
				ps.setInt(2, prID);
				ps.setDouble(3, quantity);
				ps.executeUpdate();
			} catch (SQLException e) {
				throw new ProductException("Something went wrong with adding new product to fridge...", e);
			}
		} else {
			try {
				PreparedStatement ps = con.prepareStatement(
						"UPDATE my_fridge.fridge_has_products SET quantity = ? WHERE fridge_id = ? AND product_id = ?;");
				ps.setDouble(1, numOfWantedProdInFridge + quantity);
				ps.setInt(2, fridgeID);
				ps.setInt(3, prID);
				ps.executeUpdate();
			} catch (SQLException e) {
				throw new ProductException("Something went wrong with updating products of fridge...", e);
			}
		}

	}

	private int thereIsSuchProductInFridge(int fridgeID, int prID) throws ProductException {
		Connection con = null;
		con = manager.getConnection();
		try {
			PreparedStatement ps = con.prepareStatement(
					"SELECT quantity FROM my_fridge.fridge_has_products WHERE fridge_id = ? AND product_id = ?;");
			ps.setInt(1, fridgeID);
			ps.setInt(2, prID);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return rs.getInt(1);
			}
			return 0;
		} catch (SQLException e) {
			throw new ProductException("Something went wrong with checking if a product in fridge already exists...",
					e);
		}
	}

	private int getProductByID(String p) throws ProductException {
		Connection con = null;
		con = manager.getConnection();
		try {
			PreparedStatement ps = con.prepareStatement("SELECT id FROM my_fridge.product WHERE pr_name = ?;");
			ps.setString(1, p);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return rs.getInt(1);
			}
		} catch (SQLException e) {
			throw new ProductException("Something went wrong with getting product by id...", e);
		}
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.myFridge.DAO.IProductDAO#getProductQuantityByName(java.lang.String,
	 * java.lang.String)
	 */
	public double getProductQuantityByName(String name, String fridgeCode) throws ProductException {
		Connection con = null;
		con = manager.getConnection();
		try {
			PreparedStatement ps = con.prepareStatement(
					"SELECT FHP.quantity FROM my_fridge.product_type T join my_fridge.product P ON (P.type_id = T.id)"
							+ "join my_fridge.fridge_has_products FHP ON (FHP.product_id = P.id) join my_fridge.fridge F ON (FHP.fridge_id = F.id) WHERE F.fridge_code = ? AND P.pr_name = ?");
			ps.setString(1, fridgeCode);
			ps.setString(2, name);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return rs.getInt(1);
			}
		} catch (SQLException e) {
			throw new ProductException("Something went wrong with getting product quantity by products's name...", e);
		}
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.myFridge.DAO.IProductDAO#getFridgeProductsByFridgeCode(java.lang.
	 * String)
	 */
	@Override
	public ArrayList<Product> getFridgeProductsByFridgeCode(String code) throws FridgeException, ProductException {
		ArrayList<Product> p = new ArrayList<>();
		Connection con = null;
		con = manager.getConnection();
		int frId = FridgeDAO.getInstance().getFridgeID(code);
		try {
			PreparedStatement ps = con.prepareStatement(
					"SELECT pr_name, image, isInGramms FROM my_fridge.fridge_has_products JOIN my_fridge.product ON(product_id = id) WHERE fridge_id = ?;");
			ps.setInt(1, frId);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				String unit = null;
				if (rs.getInt(3) == 1) {
					unit = "gramms";
				} else {
					unit = "number";
				}
				p.add(new Product(rs.getString(1), rs.getString(2), unit));
			}
			return p;
		} catch (SQLException e) {
			throw new ProductException("Something went wrong with getting fridge products by fridge code...", e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.myFridge.DAO.IProductDAO#getProductsByFridgeCode(java.lang.String)
	 */
	@Override
	public HashMap<String, HashMap<Product, Double>> getProductsByFridgeCode(String fridgeCode)
			throws ProductException {
		HashMap<String, HashMap<Product, Double>> products = new HashMap<String, HashMap<Product, Double>>();
		Connection con = null;
		con = manager.getConnection();
		try {
			PreparedStatement ps = con.prepareStatement(
					"SELECT T.pr_type, P.pr_name,P.image,P.isInGramms,FHP.quantity FROM my_fridge.product_type T"
							+ " join my_fridge.product P ON (P.type_id = T.id)"
							+ " join my_fridge.fridge_has_products FHP ON (FHP.product_id = P.id)"
							+ " join my_fridge.fridge F ON (FHP.fridge_id = F.id) WHERE F.fridge_code = ?;");
			ps.setString(1, fridgeCode);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				if (!products.containsKey(rs.getString(1))) {
					products.put(rs.getString(1), new HashMap<Product, Double>());
				}
				String unit = null;
				if (rs.getInt(4) == 1) {
					unit = "gramms";
				} else {
					unit = "number";
				}
				products.get(rs.getString(1)).put(new Product(rs.getString(2), rs.getString(3), unit), rs.getDouble(5));
			}
			return products;
		} catch (SQLException e) {
			throw new ProductException("Something went wrong with getting products by fridge code...", e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.myFridge.DAO.IProductDAO#findProductIdByName(java.lang.String)
	 */
	@Override
	public int findProductIdByName(String productName) throws ProductException {
		Connection con = null;
		con = manager.getConnection();
		try {
			PreparedStatement ps = con.prepareStatement("SELECT id FROM my_fridge.product WHERE pr_name = ?");
			ps.setString(1, productName);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return rs.getInt(1);
			}
		} catch (SQLException e) {
			throw new ProductException("Something went wrong with finding product's id by name...", e);
		}
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.myFridge.DAO.IProductDAO#removeProductOfFridge(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public void removeProductFromFridge(String fridgeCode, String productName)
			throws FridgeException, ProductException {
		Connection con = null;
		con = manager.getConnection();
		int fridgeId = FridgeDAO.getInstance().getFridgeID(fridgeCode);
		int prId = ProductDAO.getInstance().findProductIdByName(productName);
		try {
			PreparedStatement ps = con.prepareStatement(
					"DELETE FROM my_fridge.fridge_has_products WHERE fridge_id = ? AND product_id = ?;");
			ps.setInt(1, fridgeId);
			ps.setInt(2, prId);
			ps.execute();
		} catch (SQLException e) {
			throw new ProductException("Something went wrong with removing product from fridge...", e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.myFridge.DAO.IProductDAO#removeProductQuantityOfFridge(java.lang.
	 * String, java.lang.String, double)
	 */
	@Override
	public void removeProductQuantityFromFridge(String fridgeCode, String productName, double quantity)
			throws ProductException, FridgeException {
		Connection con = null;
		con = manager.getConnection();
		int fridgeId = FridgeDAO.getInstance().getFridgeID(fridgeCode);
		int prId = ProductDAO.getInstance().findProductIdByName(productName);
		// double currQuantity =
		// ProductDAO.getInstance().getQuantityOfProdInFridge(fridgeId, prId);
		double currQuantity = ProductDAO.getInstance().getProductQuantityByName(productName, fridgeCode);
		try {
			PreparedStatement ps = con.prepareStatement(
					"UPDATE my_fridge.fridge_has_products SET quantity = ? WHERE fridge_id = ? AND product_id = ?;");
			ps.setDouble(1, (currQuantity - quantity));
			ps.setInt(2, fridgeId);
			ps.setInt(3, prId);
			ps.execute();
		} catch (SQLException e) {
			throw new ProductException("Something went wrong with removing product's quantity from fridge...", e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.myFridge.DAO.IProductDAO#addProductQuantityOfFridge(java.lang.String,
	 * java.lang.String, double)
	 */
	@Override
	public void addProductQuantityToFridge(String fridgeCode, String productName, double quantity)
			throws ProductException, FridgeException {
		Connection con = null;
		con = manager.getConnection();
		int fridgeId = FridgeDAO.getInstance().getFridgeID(fridgeCode);
		int prId = ProductDAO.getInstance().findProductIdByName(productName);
		double currQuantity = ProductDAO.getInstance().getProductQuantityByName(productName, fridgeCode);
		try {
			PreparedStatement ps = con.prepareStatement(
					"UPDATE my_fridge.fridge_has_products SET quantity = ? WHERE fridge_id = ? AND product_id = ?;");
			ps.setDouble(1, (currQuantity + quantity));
			ps.setInt(2, fridgeId);
			ps.setInt(3, prId);
			ps.execute();
		} catch (SQLException e) {
			throw new ProductException("Something went wrong with adding product's quantity to fridge...", e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.myFridge.DAO.IProductDAO#doesProductInFridgeExists(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public boolean doesProductInFridgeExists(String prName, String fridgeCode)
			throws ProductException, FridgeException {
		int prId = ProductDAO.getInstance().findProductIdByName(prName);
		int fridgeId = FridgeDAO.getInstance().getFridgeID(fridgeCode);
		Connection con = null;
		con = manager.getConnection();
		try {
			PreparedStatement ps = con.prepareStatement(
					"SELECT product_id FROM my_fridge.fridge_has_products WHERE product_id = ? AND fridge_id = ?;");
			ps.setInt(1, prId);
			ps.setInt(2, fridgeId);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return true;
			}
		} catch (SQLException e) {
			throw new ProductException("Something went wrong with checking if product does exist in fridge...", e);
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.myFridge.DAO.IProductDAO#getProductByName(java.lang.String)
	 */
	@Override
	public Product getProductByName(String pName) throws ProductException {
		Connection con = null;
		con = manager.getConnection();
		try {
			PreparedStatement ps = con
					.prepareStatement("SELECT pr_name,image,isInGramms FROM my_fridge.product WHERE pr_name = ?;");
			ps.setString(1, pName);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				String unit = null;
				if (rs.getInt(3) == 1) {
					unit = "gramms";
				} else {
					unit = "number";
				}
				return new Product(rs.getString(1), rs.getString(2), unit);
			}
		} catch (SQLException e) {
			throw new ProductException("Something went wrong with getting product's image...", e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.myFridge.DAO.IProductDAO#getAllTypesOfProducts()
	 */
	@Override
	public ArrayList<String> getAllTypesOfProducts() throws ProductException {
		ArrayList<String> types = new ArrayList<>();
		Connection con = null;
		con = manager.getConnection();
		java.sql.Statement st;
		try {
			st = con.createStatement();
			ResultSet rs = st.executeQuery("SELECT pr_type FROM my_fridge.product_type;");
			while (rs.next()) {
				types.add(rs.getString(1));
			}
		} catch (SQLException e) {
			throw new ProductException("Something went wrong with getting product types...", e);
		}
		return types;
	}

	@Override
	public ArrayList<String> getProductsByTypes(String type) throws ProductException {
		ArrayList<String> all = new ArrayList<>();
		Connection con = null;
		con = manager.getConnection();
		try {
			PreparedStatement ps = con.prepareStatement(
					"SELECT P.pr_name FROM my_fridge.product_type T join my_fridge.product P ON (P.type_id = T.id) WHERE T.pr_type = ?");
			ps.setString(1, type);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				all.add(rs.getString(1));
			}
		} catch (SQLException e) {
			throw new ProductException("Something went wrong with getting product types...", e);
		}
		return all;

	}

}
