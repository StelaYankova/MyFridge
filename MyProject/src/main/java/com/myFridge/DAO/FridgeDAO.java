package com.myFridge.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import com.myFridge.DB.DBManager;
import com.myFridge.exceptions.FridgeException;
import com.myFridge.model.Fridge;
import com.myFridge.model.Product;
import com.myFridge.model.User;

public class FridgeDAO implements IFridgeDAO {

	private static IFridgeDAO instance;
	private DBManager manager;

	private FridgeDAO() {
		setManager(DBManager.getInstance());
	}

	public static IFridgeDAO getInstance() {
		if (instance == null)
			instance = new FridgeDAO();
		return instance;
	}

	public void setManager(DBManager manager) {
		this.manager = manager;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.myFridge.DAO.IFridgeDAO#removeFridge(java.lang.String, int)
	 */
	@Override
	public void removeFridge(String username, int fridgeId) throws FridgeException {
		Connection con = null;
		con = manager.getConnection();
		if (!FridgeDAO.getInstance().doesTheFridgeBelongToMoreThanOneUser(username, fridgeId)) {
			try {
				PreparedStatement ps = con.prepareStatement("DELETE FROM my_fridge.fridge WHERE id = ?");
				ps.setInt(1, fridgeId);
				ps.execute();
			} catch (SQLException e) {
				throw new FridgeException("Something went wrong with removing a fridge...", e);
			}
		} else {
			try {
				PreparedStatement ps = con.prepareStatement(
						"DELETE FROM my_fridge.user_has_fridge WHERE user_username = ? AND fridge_id = ?;");
				ps.setString(1, username);
				ps.setInt(2, fridgeId);
				ps.execute();
			} catch (SQLException e) {
				throw new FridgeException("Something went wrong with removing a fridge...", e);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.myFridge.DAO.IFridgeDAO#doesTheFridgeBelongToMoreThanOneUser(java.
	 * lang.String, int)
	 */
	@Override
	public boolean doesTheFridgeBelongToMoreThanOneUser(String username, int fridgeId) throws FridgeException {
		Connection con = null;
		con = manager.getConnection();
		try {
			PreparedStatement ps = con.prepareStatement(
					"SELECT user_username FROM my_fridge.user_has_fridge WHERE fridge_id = ? AND user_username != ?;");
			ps.setInt(1, fridgeId);
			ps.setString(2, username);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return true;
			}
		} catch (SQLException e) {
			throw new FridgeException("Something went wrong with checking if the fridge belongs just to one user...",
					e);
		}

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.myFridge.DAO.IFridgeDAO#doesUserAlreadyHaveFridge(com.myFridge.model.
	 * User, com.myFridge.model.Fridge)
	 */
	@Override
	public boolean doesUserAlreadyHaveFridge(User u, Fridge f) throws FridgeException {
		Connection con = null;
		con = manager.getConnection();
		try {
			PreparedStatement ps = con.prepareStatement(
					"SELECT * FROM my_fridge.user_has_fridge WHERE user_username= ? AND fridge_id=?;");
			ps.setString(1, u.getUsername());
			ps.setInt(2, FridgeDAO.getInstance().getFridgeID(f.getFridgeCode()));
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return true;
			}
			return false;
		} catch (SQLException e) {
			throw new FridgeException("Something went wrong with checking if fridge belongs to user...", e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.myFridge.DAO.IFridgeDAO#addNewFridgeToUser(com.myFridge.model.User,
	 * com.myFridge.model.Fridge)
	 */
	@Override
	public void addNewFridgeToUser(User u, Fridge f) throws FridgeException {
		Connection con = null;
		con = manager.getConnection();
		// if (!doesUserAlreadyHaveFridge(u, f)) {
		PreparedStatement ps;
		try {
			ps = con.prepareStatement("INSERT INTO my_fridge.user_has_fridge VALUES (?,?,?);");
			ps.setString(1, u.getUsername());
			ps.setInt(2, FridgeDAO.getInstance().getFridgeID(f.getFridgeCode()));// ID
			ps.setString(3, f.getName());
			ps.execute();
		} catch (SQLException e) {
			throw new FridgeException("Something went wrong with adding new fridge to user...", e);
		}
		// }
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.myFridge.DAO.IFridgeDAO#createNewFridge(com.myFridge.model.User,
	 * com.myFridge.model.Fridge)
	 */
	@Override
	public int createNewFridge(User u, Fridge f) throws FridgeException {
		Connection con = null;
		con = manager.getConnection();

		if (!doesFridgeExist(f)) {

			PreparedStatement ps;
			try {
				ps = con.prepareStatement("INSERT INTO my_fridge.fridge (fridge_code) VALUES (?);");
				ps.setString(1, f.getFridgeCode());
				ps.execute();

			} catch (SQLException e) {
				throw new FridgeException("Something went wrong with creating new fridge...", e);
			}
		}
		FridgeDAO.getInstance().addNewFridgeToUser(u, f);
		return FridgeDAO.getInstance().getFridgeID(f.getFridgeCode());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.myFridge.DAO.IFridgeDAO#doesFridgeExist(com.myFridge.model.Fridge)
	 */
	@Override
	public boolean doesFridgeExist(Fridge f) throws FridgeException {
		Connection con = null;
		con = manager.getConnection();
		try {
			PreparedStatement ps = con.prepareStatement("SELECT * FROM my_fridge.fridge where fridge_code = ?;");
			ps.setString(1, f.getFridgeCode());
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return true;
			}
		} catch (SQLException e) {
			throw new FridgeException("Something went wrong with checking if fridge already exists...", e);
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.myFridge.DAO.IFridgeDAO#getFridgeID(java.lang.String)
	 */
	@Override
	public int getFridgeID(String code) throws FridgeException {
		Connection con = null;
		con = manager.getConnection();
		try {
			PreparedStatement ps = con.prepareStatement("SELECT id FROM my_fridge.fridge WHERE fridge_code = ?;");
			ps.setString(1, code);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return rs.getInt(1);
			}
		} catch (SQLException e) {
			throw new FridgeException("Something went wrong with getting fridge's id...", e);
		}
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.myFridge.DAO.IFridgeDAO#getFridgesByUser(com.myFridge.model.User)
	 */
	@Override
	public HashSet<Fridge> getFridgesByUser(User u) throws FridgeException {
		HashSet<Fridge> fridges = new HashSet<>();
		Connection con = null;
		con = manager.getConnection();
		try {

			PreparedStatement ps = con
					.prepareStatement("SELECT U.fridge_name,M.fridge_code FROM my_fridge.user_has_fridge U "
							+ "JOIN my_fridge.fridge M ON (U.fridge_id = M.id) WHERE U.user_username = ?;");
			ps.setString(1, u.getUsername());
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				fridges.add(
						new Fridge(rs.getString(1), rs.getString(2), new HashMap<String, HashMap<Product, Double>>()));
			}
			return fridges;
		} catch (SQLException e) {
			throw new FridgeException("Something went wrong with getting fridges by user...", e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.myFridge.DAO.IFridgeDAO#getFridgeByCode(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public Fridge getFridgeByCode(String username, String code) throws FridgeException {
		Connection con = null;
		con = manager.getConnection();
		try {
			PreparedStatement ps1 = con.prepareStatement(
					"SELECT fridge_name FROM my_fridge.user_has_fridge WHERE fridge_id = ? AND user_username = ?;");
			ps1.setInt(1, FridgeDAO.getInstance().getFridgeID(code));
			ps1.setString(2, username);
			ResultSet rs1 = ps1.executeQuery();
			if (rs1.next()) {
				String name = rs1.getString(1);
				return new Fridge(name, code, new HashMap<String, HashMap<Product, Double>>());
			}
			return null;
		} catch (SQLException e) {
			throw new FridgeException("Something went wrong with getting fridge by code...", e);
		}
	}
}
