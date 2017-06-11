package com.example.controller;

import java.io.PrintStream;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.myFridge.DAO.FridgeDAO;
import com.myFridge.annotations.FridgeForm;
import com.myFridge.exceptions.FridgeException;
import com.myFridge.model.Fridge;
import com.myFridge.model.Product;
import com.myFridge.model.User;

@Controller
public class FridgeController {

	public static boolean isSessionValid(HttpServletRequest request, HttpServletResponse response) {
		if (request.getSession().getAttribute("user") == null) {
			return false;
		}
		return true;
	}

	@RequestMapping(value = "newFridge", method = RequestMethod.GET)
	public String addNewFridgeToUser(Model model, HttpServletRequest req, HttpServletResponse resp) {
		if (!isSessionValid(req, resp)) {
			return "redirect:./index";
		}
		Fridge f = new Fridge(new HashMap<String, HashMap<Product, Double>>());
		model.addAttribute("fridge", f);
		return "addFridge";
	}

	@RequestMapping(value = "newFridge", method = RequestMethod.POST)
	public String processValidationForm(HttpServletRequest req, HttpServletResponse resp,
			@Valid @ModelAttribute("fridge") FridgeForm fridgeForm, BindingResult result) {
		if (!isSessionValid(req, resp)) {
			return "redirect:./index";
		}
		if (result.hasErrors()) {
			return "addFridge";
		}

		Fridge f = new Fridge(fridgeForm.getName(), fridgeForm.getFridgeCode(),
				new HashMap<String, HashMap<Product, Double>>());
		User u = (User) req.getSession().getAttribute("user");
		try {
			if (!FridgeDAO.getInstance().doesUserAlreadyHaveFridge(u, f)) {
				FridgeDAO.getInstance().createNewFridge(u, f);
				u.setFridges((FridgeDAO.getInstance().getFridgesByUser(u)));
			} else {
				req.setAttribute("exists", "1");
			}
		} catch (FridgeException e) {
			e.printStackTrace(new PrintStream(System.err));
			return "exceptions";
		}
		if (req.getAttribute("exists") != null) {
			return "addFridge";
		} else {
			return "redirect:./yourFridges";
		}
	}

	@RequestMapping(value = "removeFridge", method = RequestMethod.POST)
	private String deleteFridge(HttpServletRequest req, HttpServletResponse resp) {
		if (!isSessionValid(req, resp)) {
			return "redirect:./index";
		}
		String fridgeCode = (String) req.getParameter("fridgeCode");
		User u = (User) req.getSession().getAttribute("user");
		try {
			FridgeDAO.getInstance().removeFridge(u.getUsername(), FridgeDAO.getInstance().getFridgeID(fridgeCode));
			u.setFridges(FridgeDAO.getInstance().getFridgesByUser(u));

		} catch (FridgeException e) {
			e.printStackTrace(new PrintStream(System.err));
			return "exceptions";
		}
		return "redirect:./yourFridges";
	}

	@RequestMapping(value = "yourFridges", method = RequestMethod.GET)
	private String getFridges(HttpServletRequest req, HttpServletResponse resp) {
		if (!isSessionValid(req, resp)) {
			return "redirect:./index";
		}

		return "yourFridges";
	}
}
