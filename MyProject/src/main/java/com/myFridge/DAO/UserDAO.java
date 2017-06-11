package com.myFridge.DAO;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import com.myFridge.DB.DBManager;
import com.myFridge.exceptions.FridgeException;
import com.myFridge.exceptions.UserException;
import com.myFridge.model.FavouriteRec;
import com.myFridge.model.Fridge;
import com.myFridge.model.User;

public class UserDAO implements IUserDAO {

	private static UserDAO instance;
	private DBManager manager;

	private UserDAO() {
		setManager(DBManager.getInstance());
	}

	public static UserDAO getInstance() {
		if (instance == null)
			instance = new UserDAO();
		return instance;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.myFridge.DAO.IUserDAO#createNewUser(com.myFridge.model.User)
	 */
	@Override
	public void createNewUser(User u) throws UserException {
		Connection con = null;
		con = manager.getConnection();
		try {
			PreparedStatement ps = con
					.prepareStatement("INSERT INTO my_fridge.users (username,pass,email,photo) VALUES (?, ?,?,?);");
			ps.setString(1, u.getUsername());
			ps.setString(2, u.getPassword());
			ps.setString(3, u.getEmail());
			// ps.setBinaryStream(4,
			// UserDAO.getInstance().getUserPhotoAsInputStream(u.getPhoto()));
			ps.setString(4, u.getPhoto());
			// ps.setString(5, u.getContentOfPhoto());
			ps.executeUpdate();
		} catch (SQLException e) {
			throw new UserException("Something went wrong with creating new user...", e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.myFridge.DAO.IUserDAO#doesUsernameNotExist(java.lang.String)
	 */
	@Override
	public boolean doesUsernameNotExist(String username) throws UserException {
		Connection con = null;
		con = manager.getConnection();
		try {
			PreparedStatement ps = con.prepareStatement("SELECT username FROM my_fridge.users WHERE username = ?;");
			ps.setString(1, username);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return false;
			}
		} catch (SQLException e) {
			throw new UserException("Something went wrong with checking if username already exists...", e);
		}
		return true;
	}

	private void isUserTheOnlyFrOwner(String username) throws UserException, FridgeException {
		Connection con = null;
		con = manager.getConnection();
		try {
			PreparedStatement ps = con
					.prepareStatement("SELECT fridge_id FROM my_fridge.user_has_fridge WHERE user_username = ?");
			ps.setString(1, username);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				ps = con.prepareStatement(
						"SELECT fridge_id, user_username FROM my_fridge.user_has_fridge WHERE user_username != ? AND fridge_id = ?;");
				ps.setString(1, username);
				ps.setInt(2, rs.getInt(1));
				ResultSet r = ps.executeQuery();
				if (!r.next()) {
					FridgeDAO.getInstance().removeFridge(username, rs.getInt(1));
				}
			}
		} catch (SQLException e) {
			throw new UserException("Something went wrong with checking if a user is the only owner of a fridge...", e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.myFridge.DAO.IUserDAO#deleteUser(com.myFridge.model.User)
	 */
	@Override
	public void deleteUser(User u) throws UserException, FridgeException {
		UserDAO.getInstance().isUserTheOnlyFrOwner(u.getUsername());
		Connection con = null;
		con = manager.getConnection();
		try {
			PreparedStatement ps = con.prepareStatement("DELETE FROM my_fridge.users WHERE username = ?");
			ps.setString(1, u.getUsername());
			ps.execute();

		} catch (SQLException e) {
			throw new UserException("Something went wrong with creating new user...", e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.myFridge.DAO.IUserDAO#updateUserInfo(com.myFridge.model.User)
	 */
	@Override
	public void updateUserInfo(User u) throws UserException {
		Connection con = null;
		con = manager.getConnection();
		try {
			PreparedStatement ps = con
					.prepareStatement("UPDATE my_fridge.users SET pass = ?, email = ?, photo = ?  WHERE username = ? ");
			ps.setString(1, u.getPassword());
			ps.setString(2, u.getEmail());
			ps.setString(3, u.getPhoto());
			ps.setString(4, u.getUsername());
			ps.executeUpdate();
		} catch (SQLException e) {
			throw new UserException("Something went wrong with updating user's information...", e);
		}
	}

	public DBManager getManager() {
		return manager;
	}

	public void setManager(DBManager manager) {
		this.manager = manager;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.myFridge.DAO.IUserDAO#areUsernamePassValid(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public boolean areUsernamePassValid(String username, String pass) throws UserException {
		Connection con = null;
		con = manager.getConnection();
		try {
			PreparedStatement ps = con
					.prepareStatement("SELECT username FROM my_fridge.users WHERE username = ? AND pass = ?;");
			ps.setString(1, username);
			ps.setString(2, pass);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return true;
			}
		} catch (SQLException e) {
			throw new UserException("Something went wrong with checking if username and password are valid...", e);
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.myFridge.DAO.IUserDAO#getUserByUsername(java.lang.String)
	 */
	@Override
	public User getUserByUsername(String username) throws UserException {
		Connection con = null;
		con = manager.getConnection();
		try {
			PreparedStatement ps = con.prepareStatement("SELECT * FROM my_fridge.users WHERE username = ?;");
			ps.setString(1, username);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return new User(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4),
						new HashSet<Fridge>(), new HashMap<Integer, FavouriteRec>());
			}
		} catch (SQLException e) {
			throw new UserException("Something went wrong with getting user by username...", e);
		}
		return null;
	}


}
