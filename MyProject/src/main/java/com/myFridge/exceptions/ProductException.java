package com.myFridge.exceptions;

import java.sql.SQLException;

public class ProductException extends Exception {
	private String problem;
	private static final long serialVersionUID = 1L;

	public ProductException(String string, SQLException e) {
		this.problem = string;
	}

	public String getProblem() {
		return problem;
	}

	public void setProblem(String problem) {
		this.problem = problem;
	}

}
