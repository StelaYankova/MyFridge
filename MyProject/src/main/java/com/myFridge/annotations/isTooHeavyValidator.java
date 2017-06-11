package com.myFridge.annotations;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

public class isTooHeavyValidator implements ConstraintValidator<isTooHeavy, MultipartFile> {

	private static final int MAX_SIZE_IN_BYTES = 10000000;

	@Override
	public void initialize(isTooHeavy constraintAnnotation) {

	}
	@Override
	public boolean isValid(MultipartFile value, ConstraintValidatorContext context) {
		if(value.getSize() > MAX_SIZE_IN_BYTES){//do 10 MB
			return false;
		}
		return true;
	}

}
