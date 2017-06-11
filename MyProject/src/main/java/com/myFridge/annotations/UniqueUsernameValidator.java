package com.myFridge.annotations;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.myFridge.DAO.UserDAO;
import com.myFridge.exceptions.UserException;

public class UniqueUsernameValidator implements ConstraintValidator<UniqueUsername, String> {

	@Override
	public void initialize(UniqueUsername constraintAnnotation) {

	}

	@Override
	public boolean isValid(String name, ConstraintValidatorContext context) {
		try {
			return UserDAO.getInstance().doesUsernameNotExist(name);
		} catch (UserException e) {
			return false;
		}
	}

}
