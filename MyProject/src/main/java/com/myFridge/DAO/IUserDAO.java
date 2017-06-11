package com.myFridge.DAO;

import com.myFridge.exceptions.FridgeException;
import com.myFridge.exceptions.UserException;
import com.myFridge.model.User;

public interface IUserDAO {

	void createNewUser(User u) throws UserException;

	boolean doesUsernameNotExist(String username) throws UserException;

	void deleteUser(User u) throws UserException, FridgeException;

	void updateUserInfo(User u) throws UserException;

	boolean areUsernamePassValid(String username, String pass) throws UserException;

	User getUserByUsername(String username) throws UserException;

	//int doesRecipeInFavExist(int recipe) throws UserException;

}