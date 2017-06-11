package com.myFridge.DAO;

import java.util.HashSet;

import com.myFridge.exceptions.FridgeException;
import com.myFridge.model.Fridge;
import com.myFridge.model.User;

public interface IFridgeDAO {

	void removeFridge(String username, int fridgeId) throws FridgeException;

	boolean doesTheFridgeBelongToMoreThanOneUser(String username, int fridgeId) throws FridgeException;

	boolean doesUserAlreadyHaveFridge(User u, Fridge f) throws FridgeException;

	void addNewFridgeToUser(User u, Fridge f) throws FridgeException;

	int createNewFridge(User u, Fridge f) throws FridgeException;

	boolean doesFridgeExist(Fridge f) throws FridgeException;

	int getFridgeID(String code) throws FridgeException;

	HashSet<Fridge> getFridgesByUser(User u) throws FridgeException;

	Fridge getFridgeByCode(String username, String code) throws FridgeException;

}