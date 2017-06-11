package com.example.controller;

import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.myFridge.DAO.FridgeDAO;
import com.myFridge.DAO.ProductDAO;
import com.myFridge.exceptions.FridgeException;
import com.myFridge.exceptions.ProductException;
import com.myFridge.exceptions.RecipeException;
import com.myFridge.model.Fridge;
import com.myFridge.model.Product;
import com.myFridge.model.User;

@Controller
public class ProductController {
	private static final int MAX_QAUNTITY_OF_PRODUCT = 40000;

	public static boolean isSessionValid(HttpServletRequest request, HttpServletResponse response) {

		if (request.getSession().getAttribute("user") == null) {
			return false;
		}
		return true;
	}

	@RequestMapping("seeProductFromType")
	protected String seeProduct(HttpServletRequest req, HttpServletResponse resp) {
		if (!isSessionValid(req, resp)) {
			return "redirect:./index";
		}
		String productsName = req.getParameter("productsName").replace("-", " ");
		Fridge fridge = (Fridge) req.getSession().getAttribute("fridge");
		String frCode = fridge.getFridgeCode();
		JsonArray jsonProducts = new JsonArray();
		HashMap<String, HashMap<Product, Double>> products;
		try {
			products = ProductDAO.getInstance().getProductsByFridgeCode(frCode);
			if (products.containsKey(productsName)) {
				HashMap<Product, Double> pr = products.get(productsName);
				for (Entry<Product, Double> entry1 : pr.entrySet()) {
					JsonObject obj = new JsonObject();
					obj.addProperty("name", entry1.getKey().getName());
					obj.addProperty("unit", entry1.getKey().getUnit());
					obj.addProperty("number", entry1.getValue());
					obj.addProperty("image", entry1.getKey().getImage());
					jsonProducts.add(obj);
				}
				resp.setContentType("application/json");
				resp.getWriter().write(jsonProducts.toString());
			}
		} catch (ProductException | IOException e) {
			e.printStackTrace(new PrintStream(System.err));
			return "exceptions";
		}

		return null;
	}

	@RequestMapping(value = "getProductPic", method = RequestMethod.GET)
	public String getProductPic(HttpServletRequest req, HttpServletResponse resp) throws ProductException {
		if (!isSessionValid(req, resp)) {
			return "redirect:./index";
		}
		String pName = req.getParameter("product");
		Product p = ProductDAO.getInstance().getProductByName(pName);
		try {
			resp.getWriter().write(p.getImage());
		} catch (IOException e) {
			e.printStackTrace(new PrintStream(System.err));
			return "exceptions";
		}
		return null;
	}

	@RequestMapping("addProductsByType")
	protected String addProductsByType(HttpServletRequest req, HttpServletResponse resp) {
		if (!isSessionValid(req, resp)) {
			return "redirect:./index";
		}
		String productsName = req.getParameter("productsName");
		ArrayList<String> list = new ArrayList<String>();
		String json = null;
		ArrayList<String> all;
		try {
			all = ProductDAO.getInstance().getProductsByTypes(productsName);
			list.addAll(all);
			json = new Gson().toJson(list);
			resp.setContentType("application/json");
			resp.getWriter().write(json);

		} catch (ProductException e1) {
			e1.printStackTrace(new PrintStream(System.err));
			return "exceptions";
		} catch (IOException e) {
			e.printStackTrace(new PrintStream(System.err));
			return "exceptions";

		}
		return null;
	}

	@RequestMapping("addProductsPage")
	public String seeProductType(HttpServletRequest req, HttpServletResponse resp) {
		if (!isSessionValid(req, resp)) {
			return "redirect:./index";
		}
		String code = null;
		User u = (User) req.getSession().getAttribute("user");
		if (req.getParameter("fridgeCode") != null) {
			code = req.getParameter("fridgeCode");
			Fridge fridge;
			try {
				fridge = FridgeDAO.getInstance().getFridgeByCode(u.getUsername(), code);
				if (fridge == null) {
					req.setAttribute("invalid", "1");
				} else {
					HashMap<String, HashMap<Product, Double>> productsInFridge = ProductDAO.getInstance()
							.getProductsByFridgeCode(code);
					fridge.setProducts(productsInFridge);
					req.getSession().setAttribute("fridge", fridge);
				}
			} catch (FridgeException | ProductException e) {
				e.printStackTrace(new PrintStream(System.err));
				return "exceptions";
			}
		}
		return "addNewProduct";
	}

	@RequestMapping(value = "addProduct", method = { RequestMethod.POST, RequestMethod.GET })
	public String addProduct(HttpServletRequest req, HttpServletResponse resp)
			throws FridgeException, RecipeException, ProductException {
		if (!isSessionValid(req, resp)) {
			return "redirect:./index";
		}

		User u = (User) req.getSession().getAttribute("user");
		if (req.getParameter("product") != null) {
			String p = req.getParameter("product");

			Fridge fridge = (Fridge) req.getSession().getAttribute("fridge");
			String fridgeCode = fridge.getFridgeCode();
			double quantityNotRounded = Double.parseDouble(req.getParameter("quantity").toString());
			double quantity = (double) Math.round(quantityNotRounded * 100) / 100;

			if (ProductDAO.getInstance().getProductQuantityByName(p, fridgeCode)
					+ quantity <= MAX_QAUNTITY_OF_PRODUCT) {
				ProductDAO.getInstance().addNewProductToFridge(u, fridgeCode, p, quantity);
				req.getSession().setAttribute("success", "1");
			} else {
				req.getSession().setAttribute("overMaxNum", "1");
			}
		}
		return "redirect:./addProductsPage";
	}

	@RequestMapping("getProductUnit")
	public String getProductUnit(HttpServletRequest req, HttpServletResponse resp) {
		if (!isSessionValid(req, resp)) {
			return "redirect:./index";
		}
		String pName = req.getParameter("product");
		Product p;
		try {
			p = ProductDAO.getInstance().getProductByName(pName);
			resp.getWriter().write(p.getUnit());
		} catch (ProductException e) {
			e.printStackTrace(new PrintStream(System.err));
			return "exceptions";
		} catch (IOException e) {
			e.printStackTrace(new PrintStream(System.err));
			return "exceptions";
		}

		return null;
	}

	@RequestMapping(value = "SeeProducts", method = { RequestMethod.GET, RequestMethod.POST })
	public String seeProd(HttpServletRequest req, HttpServletResponse resp) {
		if (!isSessionValid(req, resp)) {
			return "redirect:./index";
		}
		String code = null;
		User u = (User) req.getSession().getAttribute("user");
		if (req.getParameter("fridgeCode") != null) {
			req.getSession().setAttribute("fridgeCode", req.getParameter("fridgeCode"));
			code = req.getParameter("fridgeCode");
			Fridge fridge = null;
			try {
				fridge = FridgeDAO.getInstance().getFridgeByCode(u.getUsername(), code);
				if (fridge == null) {
					req.setAttribute("invalid", "1");
				} else {
					HashMap<String, HashMap<Product, Double>> productsInFridge;
					productsInFridge = ProductDAO.getInstance().getProductsByFridgeCode(code);
					fridge.setProducts(productsInFridge);
					req.getSession().setAttribute("fridge", fridge);
				}
			} catch (FridgeException | ProductException e) {
				e.printStackTrace(new PrintStream(System.err));
				return "exceptions";
			}
		}

		return "seeProducts";

	}

	@RequestMapping(value = "removeProducts", method = RequestMethod.POST)
	public String removeProduct(HttpServletRequest req, HttpServletResponse resp) {
		if (!isSessionValid(req, resp)) {
			return "redirect:./index";
		}
		String productName = req.getParameter("prName").replace("%20", " ");
		Fridge fridge = (Fridge) req.getSession().getAttribute("fridge");
		String fridgeCode = fridge.getFridgeCode();
		try {
			ProductDAO.getInstance().removeProductFromFridge(fridgeCode, productName);
			req.getSession().setAttribute("success", "1");
			HashMap<String, HashMap<Product, Double>> productsInFridge;
			productsInFridge = ProductDAO.getInstance().getProductsByFridgeCode(fridgeCode);
			fridge.setProducts(productsInFridge);
		} catch (FridgeException | ProductException e) {
			e.printStackTrace(new PrintStream(System.err));
			return "exceptions";
		}
		return "redirect:./SeeProducts";
	}

	@RequestMapping(value = "removeProductQuantity", method = RequestMethod.POST)
	public String removeProductByQuantity(HttpServletRequest req, HttpServletResponse resp) {
		if (!isSessionValid(req, resp)) {
			return "redirect:./index";
		}
		String productName = req.getParameter("prName").replace("%20", " ");

		double quantityNotRounded = Double.parseDouble(req.getParameter("pr_quantity").toString());
		double quantity = (double) Math.round(quantityNotRounded * 100) / 100;
		Fridge fridge = (Fridge) req.getSession().getAttribute("fridge");
		String fridgeCode = fridge.getFridgeCode();
		try {

			double currQuantity = ProductDAO.getInstance().getProductQuantityByName(productName, fridgeCode);

			if (quantity >= currQuantity) {
				this.removeProduct(req, resp);
				req.getSession().setAttribute("success", "1");
				HashMap<String, HashMap<Product, Double>> productsInFridge = ProductDAO.getInstance()
						.getProductsByFridgeCode(fridgeCode);
				fridge.setProducts(productsInFridge);
				return "redirect:./SeeProducts";
			} else {
				ProductDAO.getInstance().removeProductQuantityFromFridge(fridgeCode, productName, quantity);

				req.getSession().setAttribute("success", "1");
				HashMap<String, HashMap<Product, Double>> productsInFridge;
				productsInFridge = ProductDAO.getInstance().getProductsByFridgeCode(fridgeCode);
				fridge.setProducts(productsInFridge);
			}
		} catch (ProductException | FridgeException e) {
			e.printStackTrace(new PrintStream(System.err));
			return "exceptions";
		}
		return "redirect:./SeeProducts";
	}

	@RequestMapping(value = "addProductQuantity", method = RequestMethod.POST)
	public String addProductQuantity(HttpServletRequest req, HttpServletResponse resp) {
		if (!isSessionValid(req, resp)) {
			return "redirect:./index";
		}
		double quantityNotRounded = Double.parseDouble(req.getParameter("pr_quantity").toString());
		double quantity = (double) Math.round(quantityNotRounded * 100) / 100;

		String productName = req.getParameter("prName").replace("%20", " ");
		Fridge fridge = (Fridge) req.getSession().getAttribute("fridge");
		String fridgeCode = fridge.getFridgeCode();
		try {
			if (ProductDAO.getInstance().getProductQuantityByName(productName, fridgeCode)
					+ quantity <= MAX_QAUNTITY_OF_PRODUCT) {
				ProductDAO.getInstance().addProductQuantityToFridge(fridgeCode, productName, quantity);
				req.getSession().setAttribute("success", "1");
				String code = (String) req.getSession().getAttribute("fridgeCode");
				HashMap<String, HashMap<Product, Double>> productsInFridge;
				productsInFridge = ProductDAO.getInstance().getProductsByFridgeCode(code);
				fridge.setProducts(productsInFridge);
				req.setAttribute("success", "1");
			} else {
				req.getSession().setAttribute("overMaxNum", "1");
			}
		} catch (ProductException | FridgeException e) {
			e.printStackTrace(new PrintStream(System.err));
			return "exceptions";
		}
		return "redirect:./SeeProducts";
	}

}
