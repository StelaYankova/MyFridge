package com.myFridge.annotations;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


public class InvalidSymbolsValidator implements ConstraintValidator<InvalidSymbols, String> {

	@Override
	public void initialize(InvalidSymbols constraintAnnotation) {

	}

	@Override
	public boolean isValid(String name, ConstraintValidatorContext context) {
		boolean valid = true;
		for (int i = 0; i < name.length(); i++) {
			if (!(((int) name.charAt(i) > 47 && (int) name.charAt(i) < 58)
					|| ((int) name.charAt(i) > 64 && (int) name.charAt(i) < 91)
					|| ((int) name.charAt(i) > 96 && (int) name.charAt(i) < 123))) {
				valid = false;
				break;
			}
		}
		return valid;
	}

}