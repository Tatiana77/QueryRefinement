package com.tatiana.study.web;

import java.util.ArrayList;
import java.util.List;

import com.tatiana.study.model.Bussines;

public class Response {

	private Bussines[] bussinesArray;
	private Bussines[] bussinesRefArray;
	private String status;
	private String message;
	private final List<Error> errors = new ArrayList<>();

	public List<Error> getErrors() {
		return errors;
	}

	public Response() {
		super();
	}

	public Response(final String status) {
		super();
		this.status = status;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(final String status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(final String message) {
		this.message = message;
	}

	public Bussines[] getBussinesArray() {
		return bussinesArray;
	}

	public void setBussinesArray(final Bussines[] bussinesArray) {
		this.bussinesArray = bussinesArray;
	}

	public void addError(final String field, final String message) {
		errors.add(new Error(field, message));
	}

	public Bussines[] getBussinesRefArray() {
		return bussinesRefArray;
	}

	public void setBussinesRefArray(final Bussines[] bussinesRefArray) {
		this.bussinesRefArray = bussinesRefArray;
	}

}
