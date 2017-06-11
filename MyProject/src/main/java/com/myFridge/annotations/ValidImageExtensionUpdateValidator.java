package com.myFridge.annotations;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.web.multipart.MultipartFile;

public class ValidImageExtensionUpdateValidator
		implements ConstraintValidator<ValidImageExtensionUpdate, MultipartFile> {
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
		return true;
	}

	@Override
	public void initialize(ValidImageExtensionUpdate constraintAnnotation) {
		// TODO Auto-generated method stub

	}
}
