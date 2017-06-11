package com.myFridge.annotations;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.springframework.web.multipart.MultipartFile;

public class UserForm {

	@NotNull
	@Size(min = 4, max = 8, message = "username - between 4 and 8 characters")
	@UniqueUsername(message = "Username already exists")
	@InvalidSymbols(message = "You have invalid symbols in your username - you can use letters and numbers")
	private String username;

	@NotNull
	@Size(min = 4, max = 6, message = "Password - between 4 and 6 characters")
	@InvalidSymbols(message = "You have invalid symbols in your password - you can use letters and numbers")
	private String password;

	@NotNull(message = "Passwords don't match")
	private String confirmPassword;

	@NotNull
	@Email(message = "Invalid email")
	private String email;

	@isTooHeavy(message = "Image is too heavy")
	@NotNull(message = "Upload a photo")
	@ValidImageExtension(message = "Upload a photo")
	private MultipartFile photo;

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
		checkPassword();
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public MultipartFile getPhoto() {
		return photo;
	}

	public void setPhoto(MultipartFile photo) {
		this.photo = photo;
	}

	private void checkPassword() {
		if (this.password == null || this.confirmPassword == null) {
			return;
		} else if (!this.password.equals(confirmPassword)) {
			this.confirmPassword = null;
		}
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
		checkPassword();

	}

}
