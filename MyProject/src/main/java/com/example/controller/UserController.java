package com.example.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import com.myFridge.DAO.FridgeDAO;
import com.myFridge.DAO.ProductDAO;
import com.myFridge.DAO.RecipeDAO;
import com.myFridge.DAO.UserDAO;
import com.myFridge.annotations.UserForm;
import com.myFridge.annotations.changeInfoForm;
import com.myFridge.exceptions.FridgeException;
import com.myFridge.exceptions.ProductException;
import com.myFridge.exceptions.RecipeException;
import com.myFridge.exceptions.UserException;
import com.myFridge.model.FavouriteRec;
import com.myFridge.model.Fridge;
import com.myFridge.model.User;

@EnableWebMvc
@Controller
public class UserController {

	public static boolean isSessionValid(HttpServletRequest request, HttpServletResponse response) {
		if (request.getSession().getAttribute("user") == null) {
			return false;
		}
		return true;
	}

	@RequestMapping(value = "homePage", method = { RequestMethod.GET, RequestMethod.POST })
	private String returnToHomePage(HttpServletRequest req, HttpServletResponse resp) {
		if (!isSessionValid(req, resp)) {
			return "redirect:./index";
		}
		return "main";
	}

	@RequestMapping(value = "/*")
	public String sayHello(HttpServletRequest req) {
		if (req.getAttribute("hasExpired") != null) {
			req.getSession().invalidate();
		}
		try {
			ArrayList<String> types = null;
			types = ProductDAO.getInstance().getAllTypesOfProducts();
			req.getServletContext().setAttribute("productTypes", types);

		} catch (ProductException e) {
			e.printStackTrace(new PrintStream(System.err));
			return "exceptions";
		}

		return "index";
	}

	@RequestMapping(value = "login", method = RequestMethod.POST)
	public String login(HttpServletRequest req, HttpServletResponse resp) {
		String username = req.getParameter("username");
		String pass = req.getParameter("password");
		try {
			if (UserDAO.getInstance().areUsernamePassValid(username, pass)) {
				User u = UserDAO.getInstance().getUserByUsername(username);
				u.setFridges(FridgeDAO.getInstance().getFridgesByUser(u));
				u.setFavouriteRecipes(RecipeDAO.getInstance().getFavouriteRecipes(u.getUsername()));
				req.getSession().setAttribute("user", u);
				return "redirect:./homePage";
			} else {
				req.setAttribute("wrong", '1');
			}
		} catch (UserException | FridgeException e) {
			e.printStackTrace(new PrintStream(System.err));
			return "exceptions";
		} catch (RecipeException e) {
			e.printStackTrace(new PrintStream(System.err));
			return "exceptions";
		}
		return "index";
	}

	@RequestMapping(value = "addRemoveFav", method = RequestMethod.POST)
	public String addRemFav(HttpServletRequest req, HttpServletResponse resp) {
		if (!isSessionValid(req, resp)) {
			return "redirect:./index";
		}
		String recipeNameNotReady = req.getParameter("recName").toString();
		String recipeName = recipeNameNotReady.replace("%20", "'");
		int id = Integer.parseInt(req.getParameter("id").toString());
		String image = req.getParameter("image").toString();
		User user = (User) req.getSession().getAttribute("user");
		try {
			if (RecipeDAO.getInstance().isRecipeFavFoUser(user.getUsername(), recipeName)) {
				RecipeDAO.getInstance().removeRecipeFromFav(user.getUsername(), id);

			} else {
				RecipeDAO.getInstance().addRecipeToFav(user.getUsername(), id, recipeName, image);
				user.setFavouriteRecipes(RecipeDAO.getInstance().getFavouriteRecipes(user.getUsername()));

			}
		} catch (RecipeException e1) {
			e1.printStackTrace(new PrintStream(System.err));
			return "exceptions";
		} catch (UserException e) {
			e.printStackTrace(new PrintStream(System.err));
			return "exceptions";
		}
		return null;
	}

	@RequestMapping(value = "removeFav", method = RequestMethod.POST)
	public String removeFromFav(HttpServletRequest req, HttpServletResponse resp) {
		if (!isSessionValid(req, resp)) {
			return "redirect:./index";
		}
		int id = Integer.parseInt(req.getParameter("id").toString());
		User user = (User) req.getSession().getAttribute("user");
		try {
			RecipeDAO.getInstance().removeRecipeFromFav(user.getUsername(), id);
			user.setFavouriteRecipes(RecipeDAO.getInstance().getFavouriteRecipes(user.getUsername()));
			resp.setStatus(200);
		} catch (RecipeException e) {
			e.printStackTrace(new PrintStream(System.err));
			return "exceptions";
		}

		return null;
	}

	@RequestMapping(value = "removeFavValue", method = RequestMethod.POST)
	public String removeFromFavValue(HttpServletRequest req, HttpServletResponse resp) {
		if (!isSessionValid(req, resp)) {
			return "redirect:./index";
		}
		int id = Integer.parseInt(req.getParameter("id").toString());
		User user = (User) req.getSession().getAttribute("user");
		try {
			RecipeDAO.getInstance().removeRecipeFromFav(user.getUsername(), id);
			user.setFavouriteRecipes(RecipeDAO.getInstance().getFavouriteRecipes(user.getUsername()));
		} catch (RecipeException e) {
			e.printStackTrace(new PrintStream(System.err));
			return "exceptions";
		}
		return "redirect:./seeFavouriteRecipes";
	}

	@RequestMapping(value = "addToFav", method = RequestMethod.POST)
	public String addToFav(HttpServletRequest req, HttpServletResponse resp) {
		if (!isSessionValid(req, resp)) {
			return "redirect:./index";
		}
		String recipeNameNotReady = req.getParameter("recName").toString();
		String recipeName = recipeNameNotReady.replace("%20", "'");
		int id = Integer.parseInt(req.getParameter("id").toString());
		String image = req.getParameter("image").toString();
		User user = (User) req.getSession().getAttribute("user");
		try {
			RecipeDAO.getInstance().addRecipeToFav(user.getUsername(), id, recipeName, image);
			user.setFavouriteRecipes(RecipeDAO.getInstance().getFavouriteRecipes(user.getUsername()));

		} catch (UserException e) {
			e.printStackTrace(new PrintStream(System.err));
			return "exceptions";
		} catch (RecipeException e) {
			e.printStackTrace(new PrintStream(System.err));
			return "exceptions";
		}

		return null;
	}

	@RequestMapping(value = "Logout", method = RequestMethod.GET)
	public String logout(HttpServletRequest req, HttpServletResponse resp) {
		if (!isSessionValid(req, resp)) {
			return "redirect:./index";
		}
		req.getSession().invalidate();
		return "redirect:./index";
	}

	@RequestMapping(value = "removeProfile", method = RequestMethod.POST)
	public String removeProfile(HttpServletRequest req, HttpServletResponse resp) {
		if (!isSessionValid(req, resp)) {
			return "redirect:./index";
		}
		User u = (User) req.getSession().getAttribute("user");
		try {
			File f = new File("/Users/Stela/Desktop/profilePics/" + u.getPhoto());
			f.delete();
			UserDAO.getInstance().deleteUser(u);

			req.getSession().invalidate();

		} catch (UserException | FridgeException e) {
			e.printStackTrace(new PrintStream(System.err));
			return "exceptions";
		}
		return "redirect:./index";
	}

	@RequestMapping(value = "ProfilePic", method = RequestMethod.GET)
	public String getProfilePic(Model model, HttpServletRequest req, HttpServletResponse resp) {
		if (!isSessionValid(req, resp)) {
			return "redirect:./index";
		}
		User currUser = (User) req.getSession().getAttribute("user");
		try {
			byte[] imageBytes = getImageAsBytes(currUser.getPhoto());
			resp.getOutputStream().write(imageBytes);
		} catch (IOException e) {
			e.printStackTrace(new PrintStream(System.err));
			return "exceptions";
		}
		return null;
	}

	private byte[] getImageAsBytes(String photo) throws IOException {
		File pic = new File("/Users/Stela/Desktop/profilePics/" + photo);
		byte[] fileContent = Files.readAllBytes(pic.toPath());
		return fileContent;
	}

	@RequestMapping(value = "changeProfile", method = RequestMethod.GET)
	public String updateInfo(Model model, HttpServletRequest req, HttpServletResponse resp) {
		if (!isSessionValid(req, resp)) {
			return "redirect:./index";
		}
		User u = new User(new HashSet<Fridge>(), new HashMap<Integer, FavouriteRec>());
		model.addAttribute("user", u);
		return "changeProfile";
	}

	@RequestMapping(value = "updateInfo", method = RequestMethod.POST)
	public String updateInfoForm(@Valid @ModelAttribute("user") changeInfoForm userForm, BindingResult result,
			@RequestParam(value = "photo") MultipartFile userImage, HttpServletRequest req, HttpServletResponse resp) {
		if (!isSessionValid(req, resp)) {
			return "redirect:./index";
		}
		if (result.hasErrors()) {
			return "changeProfile";
		} else {
			User currUser = (User) req.getSession().getAttribute("user");
			String username = currUser.getUsername();
			String fileName = userImage.getContentType();
			String idStr = fileName.substring(fileName.lastIndexOf('/') + 1);
			String contentType = idStr;
			try {
				User u = null;
				File f = null;
				f = new File("/Users/Stela/Desktop/profilePics/");
				if (!f.exists()) {
					f.mkdir();
				}

				if (!userImage.isEmpty()) {
					String nameOfFile = username + "_profilePic." + contentType;
					File newFileInProfilePic = new File(f, nameOfFile);
					if (!newFileInProfilePic.exists()) {
						newFileInProfilePic.createNewFile();

					}
					FileOutputStream f1 = new FileOutputStream(newFileInProfilePic);
					byte[] contentInBytes = userImage.getBytes();
					f1.write(contentInBytes);
					f1.flush();
					f1.close();
					u = new User(username, userForm.getPassword(), userForm.getEmail(), nameOfFile,
							new HashSet<Fridge>(), new HashMap<Integer, FavouriteRec>());
				} else {
					String currImage = currUser.getPhoto();
					u = new User(username, userForm.getPassword(), userForm.getEmail(), currImage,
							new HashSet<Fridge>(), new HashMap<Integer, FavouriteRec>());

				}
				u.setFridges(currUser.getFridges());
				u.setFavouriteRecipes(currUser.getFavouriteRecipes());
				UserDAO.getInstance().updateUserInfo(u);
				req.getSession().removeAttribute("user");
				req.getSession().setAttribute("user", u);
			} catch (IOException | UserException e) {
				e.printStackTrace(new PrintStream(System.err));
				return "exceptions";
			}
			return "changeProfile";
		}
	}

	@RequestMapping(value = "register", method = RequestMethod.GET)
	public String register(Model model) {
		User u = new User(new HashSet<Fridge>(), new HashMap<Integer, FavouriteRec>());
		model.addAttribute("user", u);
		return "registration";
	}

	@RequestMapping(value = "register", method = RequestMethod.POST)
	public String processValidationForm(@RequestParam(value = "photo") MultipartFile userImage,
			@Valid @ModelAttribute("user") UserForm userForm, BindingResult result, HttpServletRequest req,
			HttpServletResponse resp) {
		if (result.hasErrors()) {
			return "registration";
		} else {
			String fileName = userImage.getContentType();
			String idStr = fileName.substring(fileName.lastIndexOf('/') + 1);
			String contentType = idStr;
			try {
				try {
					File f = null;
					f = new File("/Users/Stela/Desktop/profilePics/");
					if (!f.exists()) {
						f.mkdirs();
					}
					String nameOfFile = userForm.getUsername() + "_profilePic." + contentType;
					File newFileInProfilePic = new File(f, nameOfFile);

					if (!newFileInProfilePic.exists()) {
						newFileInProfilePic.createNewFile();
						FileOutputStream f1 = new FileOutputStream(newFileInProfilePic);
						byte[] contentInBytes = userImage.getBytes();
						f1.write(contentInBytes);
						f1.flush();
						f1.close();
					}

					User u = new User(userForm.getUsername(), userForm.getPassword(), userForm.getEmail(), nameOfFile,
							new HashSet<Fridge>(), new HashMap<Integer, FavouriteRec>());
					UserDAO.getInstance().createNewUser(u);
				} catch (IOException e2) {
					e2.printStackTrace(new PrintStream(System.err));
					return "exceptions";
				}

			} catch (UserException e) {
				e.printStackTrace(new PrintStream(System.err));
				return "exceptions";
			}

			return "redirect:./index";
		}
	}

}
