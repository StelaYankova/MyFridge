package com.example.controller;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.annotation.ControllerAdvice;

//..
@ControllerAdvice
public class ExceptionController {

	@ExceptionHandler(Exception.class)
	public String exception(Exception e) {
		ModelAndView mav = new ModelAndView("exception");
		mav.addObject("name", e.getClass().getSimpleName());
		mav.addObject("message", e.getMessage());
		e.printStackTrace(System.err);
		return "exceptions";
	}

}