package com.myFridge.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;

@Target({ FIELD })
@Retention(RUNTIME)
@Constraint(validatedBy = { isTooHeavyValidator.class })
@Documented

public @interface isTooHeavy {

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	String message() default "{javax.validation.constraints.isTooHeavy.message}";
}
