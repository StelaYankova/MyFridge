package com.myFridge.annotations;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.web.multipart.MultipartFile;

public class ValidImageExtensionValidator implements ConstraintValidator<ValidImageExtension, MultipartFile> {

	@Override
	public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
		if (!file.isEmpty()) {
			String fileName = file.getContentType();
			String contentType = fileName.split("[/]")[0];
			if (contentType.equals("image")) {
				return true;
			}
			return false;
		}
		return false;
	}

	@Override
	public void initialize(ValidImageExtension constraintAnnotation) {
		// TODO Auto-generated method stub

	}
}
