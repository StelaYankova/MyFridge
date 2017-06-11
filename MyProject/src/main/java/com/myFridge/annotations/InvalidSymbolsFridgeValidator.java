package com.myFridge.annotations;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


public class InvalidSymbolsFridgeValidator implements ConstraintValidator<InvalidSymbolsFridge, String> {

	@Override
	public boolean isValid(String name, ConstraintValidatorContext context) {
		boolean valid = true;
		for(int i = 0; i < name.length(); i++){
			if(!((
					(int)name.charAt(i) > 47 && (int)name.charAt(i) < 58)
					||((int)name.charAt(i) > 64 && (int)name.charAt(i) < 91)
					||((int)name.charAt(i) > 96 && (int)name.charAt(i) < 123)|| (int)name.charAt(i) == 32)){
				valid = false;
				break;
			}
		}
		return valid;
	}

	@Override
	public void initialize(InvalidSymbolsFridge constraintAnnotation) {
		// TODO Auto-generated method stub
		
	}

}