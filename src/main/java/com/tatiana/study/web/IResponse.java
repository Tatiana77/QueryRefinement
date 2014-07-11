package com.tatiana.study.web;

import java.util.ArrayList;
import java.util.List;

import com.tatiana.study.model.Incident;

public class IResponse {
	
	private Incident[] incidentArray;
	private Incident[] incidentRefArray;
	private double[] r_q;
	private String status;
	private String message;
	private int refCardinality = 1;
	private double deviation;
	private String[] names = new String[3];
	private float[] costs = { 0, 0, 0 };
	private float[] deviations = { 0, 0, 0 };
	private int cardinality;



	public int getRefCardinality() {
		return refCardinality;
	}

	public void setRefCardinality(int refCardinality) {
		this.refCardinality = refCardinality;
	}

	public void setDeviation(float deviation) {
		this.deviation = deviation;
	}

	private final List<Error> errors = new ArrayList<>();

	public double[] getR_q() {
		return r_q;
	}

	public void setR_q(final double[] r_q) {
		this.r_q = r_q;
	}

	public IResponse(final String status) {
		super();
		this.status = status;
	}

	public Incident[] getIncidentArray() {
		return incidentArray;
	}

	public void setIncidentArray(final Incident[] incidentArray) {
		this.incidentArray = incidentArray;
	}

	public Incident[] getIncidentRefArray() {
		return incidentRefArray;
	}

	public void setIncidentRefArray(final Incident[] incidentRefArray) {
		this.incidentRefArray = incidentRefArray;
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

	public double getDeviation() {
		return deviation;
	}

	public void setDeviation(final double deviation) {
		this.deviation = deviation;
	}

	public List<Error> getErrors() {
		return errors;
	}

	public void addError(final String field, final String message) {
		errors.add(new Error(field, message));
	}

	public String[] getNames() {
		return names;
	}

	public void setNames(String[] names) {
		this.names = names;
	}

	public float[] getCosts() {
		return costs;
	}

	public void setCosts(float[] costs) {
		this.costs = costs;
	}

	public float[] getDeviations() {
		return deviations;
	}

	public void setDeviations(float[] deviations) {
		this.deviations = deviations;
	}

	public int getCardinality() {
		return cardinality;
	}

	public void setCardinality(int cardinality) {
		this.cardinality = cardinality;
	}

	
	
}
