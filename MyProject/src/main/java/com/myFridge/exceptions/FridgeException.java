package com.myFridge.exceptions;

import java.sql.SQLException;

public class FridgeException extends Exception {

	private String problem;

	public String getProblem() {
		return problem;
	}

	public void setProblem(String problem) {
		this.problem = problem;
	}

	public FridgeException(String string, SQLException e) {
		this.problem = string;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
